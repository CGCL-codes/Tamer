package pl.put.dcs.workManagerServer.projectManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import pl.put.dcs.workManagerServer.projectManager.queueManager.QueueManager;
import pl.put.dcs.workManagerServer.projectManager.computingObjectManger.ComputingObjectManger;
import pl.put.model.flowModel.FlowModel;
import pl.put.model.flowModel.connection.Connection;
import pl.put.model.flowModel.flowComputingObjectElement.FlowComputingObjectElement;
import pl.put.model.project.Project;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import pl.put.dcs.core.utils.serializer.SerializeManager;
import pl.put.dcs.core.utils.serializer.SerializerInterfejs;
import pl.put.dcs.workManagerServer.classLoaders.LibraryClassLoader;
import pl.put.dcs.workManagerServer.config.ProjectConfigManager;
import pl.put.dcs.workManagerServer.config.ServerConfigManager;
import pl.put.dcs.workManagerServer.projectManager.exceptions.ProjectErrorException;
import pl.put.dcs.workManagerServer.serviceConnectors.ControlModuleServerConnector;

public class ProjectManager {

    private List<ComputingObjectManger> computingObjectMangerList;

    private ProjectConfigManager pcm;

    private Project project;

    public static Logger log = Logger.getLogger(ProjectManager.class.getName());

    private int actualComputedTaskGroupId = 0;

    private FlowModel flowModel;

    private ControlModuleServerConnector controlModeuleServerConnector;

    private LinkedList<List<FlowComputingObjectElement>> flowComputingObjectElementQueue;

    private QueueManager queueManager;

    private String projectId;

    public ProjectManager(Project project) {
        this.project = project;
        pcm = new ProjectConfigManager(project);
        computingObjectMangerList = new ArrayList<ComputingObjectManger>();
        controlModeuleServerConnector = (ControlModuleServerConnector) ControlModuleServerConnector.getInstance();
    }

    public void continueStopedProject(String projectId) {
        this.projectId = projectId;
    }

    public void initProject() throws pl.put.dcs.workManagerServer.projectManager.exceptions.ProjectErrorException {
        initQueueManager();
        flowModel = getProjectFlowModelFromResources(project);
        createComputingPlan();
        storeComputingPlan(flowComputingObjectElementQueue, project);
        List<FlowComputingObjectElement> fcoList = getNextTaskGroup();
        for (FlowComputingObjectElement fco : fcoList) {
            ComputingObjectManger com = createComputingObjectManger(fco);
            computingObjectMangerList.add(com);
        }
    }

    private ComputingObjectManger createComputingObjectManger(FlowComputingObjectElement fco) {
        ComputingObjectManger coom = new ComputingObjectManger(fco);
        return coom;
    }

    private boolean isFlowComputingObjectGroupComplateTasks(int groupId) {
        List<FlowComputingObjectElement> fcoList = flowComputingObjectElementQueue.get(groupId);
        boolean ret = true;
        if (groupId != actualComputedTaskGroupId) {
            for (FlowComputingObjectElement fco : fcoList) {
                if (controlModeuleServerConnector.getTaskStatus(fco) != FlowComputingObjectElement.TASK_STATUS.COMPLATE) {
                    ret = false;
                    break;
                }
            }
        } else {
            for (ComputingObjectManger com : computingObjectMangerList) {
                if (!com.isFinish()) {
                    ret = false;
                    break;
                }
            }
        }
        return ret;
    }

    public LinkedList<List<FlowComputingObjectElement>> getFlowComputingObjectElementQueue() {
        return flowComputingObjectElementQueue;
    }

    public void setFlowComputingObjectElementQueue(LinkedList<List<FlowComputingObjectElement>> val) {
        flowComputingObjectElementQueue = val;
    }

    public FlowModel getFlowModel() {
        return flowModel;
    }

    public void setFlowModel(FlowModel val) {
        flowModel = val;
    }

    private void initQueueManager() {
        queueManager = new QueueManager(project);
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public void setQueueManager(QueueManager val) {
        queueManager = val;
    }

    private void createComputingPlan() {
        setFlowComputingObjectElementQueue(sortFlowComputingObjectElementList(flowModel));
    }

    private LinkedList<List<FlowComputingObjectElement>> storeComputingPlan(LinkedList<List<FlowComputingObjectElement>> flowComputingObjectElementQueue, Project project) {
        int groupNumber = 0;
        for (List<FlowComputingObjectElement> group : flowComputingObjectElementQueue) {
            groupNumber++;
            for (FlowComputingObjectElement fco : group) {
                fco.setProject(project);
                fco = controlModeuleServerConnector.storeFlowComputingObjectElement(fco, groupNumber);
            }
        }
        return flowComputingObjectElementQueue;
    }

    private List<FlowComputingObjectElement> getNextTaskGroup() {
        return flowComputingObjectElementQueue.pop();
    }

    public List<FlowComputingObjectElement> getTaskGroupById(int id) {
        return flowComputingObjectElementQueue.get(id);
    }

    private LinkedList<List<FlowComputingObjectElement>> sortFlowComputingObjectElementList(FlowModel model) {
        LinkedList<FlowComputingObjectElement> coList = new LinkedList<FlowComputingObjectElement>();
        List<Connection> conns = model.getConnections();
        int coListSize = model.getComputingObjectList().size();
        Map<FlowComputingObjectElement, List<FlowComputingObjectElement>> coConnectionMap = new HashMap<FlowComputingObjectElement, List<FlowComputingObjectElement>>();
        if (coListSize > 1) {
            for (Connection conn : conns) {
                FlowComputingObjectElement start = conn.getStartPin().getComputingObject().getFlowComputingObjectElement();
                FlowComputingObjectElement end = conn.getEndPin().getComputingObject().getFlowComputingObjectElement();
                int startIndex = coList.indexOf(start);
                int endIndex = coList.indexOf(end);
                if ((endIndex == -1)) {
                    coList.add(end);
                    endIndex = coList.size() - 1;
                }
                if (startIndex == -1) {
                    coList.add(endIndex, start);
                } else if (startIndex > endIndex) {
                    FlowComputingObjectElement rmvCO = coList.remove(startIndex);
                    coList.add(endIndex, start);
                }
                List<FlowComputingObjectElement> connList = coConnectionMap.get(start);
                if (connList == null) {
                    connList = new ArrayList<FlowComputingObjectElement>();
                    coConnectionMap.put(start, connList);
                }
                connList.add(end);
            }
        } else if (coListSize == 1) {
            log.debug("There is one CO in project");
            Set<String> keys = model.getComputingObjectList().keySet();
            String key = keys.iterator().next();
            coList.push(model.getComputingObjectList().get(key));
        }
        return groupFlowComputingObjectElementList(coList, coConnectionMap);
    }

    private LinkedList<List<FlowComputingObjectElement>> groupFlowComputingObjectElementList(LinkedList<FlowComputingObjectElement> sortedList, Map<FlowComputingObjectElement, List<FlowComputingObjectElement>> connectionMap) {
        LinkedList<List<FlowComputingObjectElement>> ret = new LinkedList<List<FlowComputingObjectElement>>();
        List<FlowComputingObjectElement> fcoList = null;
        FlowComputingObjectElement tmp = null;
        log.debug("Group computing object: ");
        for (FlowComputingObjectElement fco : sortedList) {
            if ((tmp == null) || (connectionMap.get(tmp).contains(fco))) {
                fcoList = new ArrayList<FlowComputingObjectElement>();
                ret.add(fcoList);
            }
            log.debug("- next element on grouped list: " + fco.getName());
            fcoList.add(fco);
            tmp = fco;
        }
        return ret;
    }

    private FlowModel getProjectFlowModelFromResources(Project dcsProject) throws pl.put.dcs.workManagerServer.projectManager.exceptions.ProjectErrorException {
        String pathToProjectsPackage = ServerConfigManager.getProperty("path_to_projects_package");
        String flowModelDir = ServerConfigManager.getProperty("flow_model_dir");
        String projectFlowModelName = ServerConfigManager.getProperty("project_flow_model_name");
        String flowModelPath = pathToProjectsPackage + "/" + dcsProject.getProjectName() + "/" + flowModelDir + "/" + projectFlowModelName;
        log.debug("Flow model dir: " + flowModelPath);
        InputStream flowModelIS = LibraryClassLoader.getResourceAsStreamFromClassPath(flowModelPath);
        SerializerInterfejs sm = new SerializeManager();
        sm.init();
        FlowModel ret = (FlowModel) sm.unSerializeFromFile(flowModelIS);
        if (ret == null) {
            throw new ProjectErrorException("Incorect flow model definition. Object unserializable.");
        }
        return ret;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project val) {
        project = val;
    }

    public List<ComputingObjectManger> getComputingObjectMangerList() {
        return computingObjectMangerList;
    }

    public void setComputingObjectMangerList(List<ComputingObjectManger> val) {
        this.computingObjectMangerList = val;
    }
}
