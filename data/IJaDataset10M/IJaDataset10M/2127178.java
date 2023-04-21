package org.isi.monet.core.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.isi.monet.core.model.Account;
import org.isi.monet.core.model.AttributeList;
import org.isi.monet.core.model.DataRequest;
import org.isi.monet.core.model.DataResultItemList;
import org.isi.monet.core.model.LogBookNode;
import org.isi.monet.core.model.LogBookTask;
import org.isi.monet.core.model.LogEntryList;
import org.isi.monet.core.model.LogSubscriberList;
import org.isi.monet.core.model.Node;
import org.isi.monet.core.model.NodeList;
import org.isi.monet.core.model.NodeReference;
import org.isi.monet.core.model.RecommendationList;
import org.isi.monet.core.model.SearchRequest;
import org.isi.monet.core.model.SearchResult;
import org.isi.monet.core.model.ServerConfiguration;
import org.isi.monet.core.model.Task;
import org.isi.monet.core.model.TaskList;
import org.isi.monet.core.model.TaskSearchRequest;
import org.isi.monet.core.model.Team;
import org.isi.monet.core.model.TeamList;

public abstract class ComponentPersistence extends Component {

    protected static ComponentPersistence oInstance;

    protected ComponentPersistence() {
        super();
    }

    public static synchronized ComponentPersistence getInstance() {
        return oInstance;
    }

    public static Boolean started() {
        if (oInstance == null) return false;
        return oInstance.bStarted;
    }

    public abstract Node addNode(String code, Node oParent);

    public abstract Node addNode(String code);

    public abstract NodeList addNodes(NodeList oNodeList, Node oParent);

    public abstract Node attachNode(String id);

    public abstract NodeList attachNodes(String sNodes);

    public abstract Node copyNode(String id, Node oParent, String sLabel, String sDescription);

    public abstract Node copyNode(Node oNode, Node oParent, String sLabel, String sDescription);

    public abstract Boolean deleteNode(String id);

    public abstract Boolean deleteNodes(String sData);

    public abstract Boolean emptyTrash();

    public abstract NodeList filterNodeList(Node oNode, String sNodes, String sNodeTypes, Date dtFrom, Date dtTo);

    public abstract String exportNode(Node oNode, String codeFormat);

    public abstract String exportNode(Node oNode);

    public abstract String exportNode(String idNode, String codeFormat);

    public abstract String exportNode(String idNode);

    public abstract ArrayList<String> importNode(String idNode, String sData, String sOption, String sNodesDescription);

    public abstract ArrayList<String> importNode(String idNode, String sData);

    public abstract AttributeList loadAttributes(Node oNode);

    public abstract DataResultItemList loadAttributeItems(String code, HashMap<String, String> hmParameters);

    public abstract Node loadNode(String id);

    public abstract Node loadNodeFromData(String id, String sData);

    public abstract String loadNodeAttributes(String id);

    public abstract NodeReference loadNodeReference(String id);

    public abstract DataResultItemList loadNodeItems(String code, HashMap<String, String> hmParameters);

    public abstract String locateNodeId(String code, String sCondition);

    public abstract String locateNodeId(String code);

    public abstract NodeList loadNodes(String idNode);

    public abstract NodeList loadNodes();

    public abstract NodeList loadNodesFromTrash(DataRequest oDataRequest);

    public abstract Boolean recoverNodeFromTrash(String id);

    public abstract Boolean recoverNodesFromTrash(String sData);

    public abstract Boolean removeNodeFromTrash(String id);

    public abstract Boolean saveNode(Node oNode);

    public abstract Boolean saveNode(Node oNode, String sData);

    public abstract Boolean saveNodeAttributes(Node oNode, String sData);

    public abstract Boolean saveNodeReferenceAttributes(Node oNode, String sData);

    public abstract Boolean saveNodePermissions(Node oNode);

    public abstract SearchResult search(Node oNode, SearchRequest oSearchRequest);

    public abstract Boolean setNodesFiltering(String sData);

    public abstract String getNodesSorting();

    public abstract Boolean setNodesSorting(String sData);

    public abstract Boolean shareNode(Node oNode, String sUsers, String sDescription, Date dtExpireDate);

    public abstract LogSubscriberList loadNodeSubscribers(Integer type);

    public abstract Boolean addNodeSubscriber(ServerConfiguration oConfiguration, Integer iEventType);

    public abstract Boolean removeNodeSubscriber(ServerConfiguration oConfiguration, Integer iEventType);

    public abstract LogBookNode loadLogBookNode();

    public abstract LogEntryList searchLogBookNodeItems(Integer iEventType, Date dtFrom, Date dtTo);

    public abstract LogEntryList requestLogBookNodeItems(DataRequest oDataRequest);

    public abstract Integer requestLogBookNodeItemsCount(String idNode);

    public abstract Boolean makeNodePublic(Node oNode);

    public abstract Boolean makeNodePrivate(Node oNode);

    public abstract Task loadTask(String idTask);

    public abstract SearchResult searchTasks(Account oAccount, TaskSearchRequest oSearchRequest);

    public abstract TaskList loadTasks(Account oAccount, String sState, String Type);

    public abstract TaskList loadTasks(Node oNode, String sState, String Type);

    public abstract Boolean recoverTaskFromTrash(String id);

    public abstract Task createTask(String code);

    public abstract Boolean removeTask(String id);

    public abstract Boolean removeTasks(String sData);

    public abstract Boolean finishTask(String id);

    public abstract Boolean finishTasks(String sData);

    public abstract Boolean abortTask(String id);

    public abstract Boolean abortTasks(String sData);

    public abstract Boolean saveTask(Task oTask);

    public abstract Boolean setTaskVisited(Task oTask, Boolean bValue);

    public abstract Boolean setTaskTeam(Task oTask, String idTeam);

    public abstract Boolean addTaskMilestone(Task oTask, String codeParent, String sData);

    public abstract Boolean deleteTaskMilestone(Task oTask, String codeMilestone);

    public abstract Boolean setTaskMilestoneState(Task oTask, String codeMilestone, String State, String sAuthor, String sDate);

    public abstract Boolean addTaskMilestoneFact(Task oTask, String codeMilestone, String sFact);

    public abstract Boolean deleteTaskMilestoneFact(Task oTask, String codeMilestone, String sFact);

    public abstract RecommendationList loadRecommendations();

    public abstract Boolean existsTaskMilestoneRecommendation(Task oTask, String codeMilestone, String idUser);

    public abstract Boolean addTaskMilestoneRecommendation(Task oTask, String codeMilestone, String idUser, String sMessage);

    public abstract Boolean deleteTaskMilestoneRecommendation(Task oTask, String codeMilestone, String idUser);

    public abstract Boolean assignTasks(Account oAccount, String sData);

    public abstract Boolean assignTask(Account oAccount, String idTask);

    public abstract Boolean freeTasks(String sData);

    public abstract Boolean freeTask(String id);

    public abstract LogBookTask loadLogBookTask();

    public abstract LogEntryList searchLogBookTaskItems(Integer iEventType, Date dtFrom, Date dtTo);

    public abstract LogEntryList requestLogBookTaskItems(DataRequest oDataRequest);

    public abstract Integer requestLogBookTaskItemsCount(String idTask);

    public abstract LogSubscriberList loadTaskSubscribers(Integer type);

    public abstract Boolean addTaskSubscriber(ServerConfiguration oConfiguration, Integer iEventType);

    public abstract Boolean removeTaskSubscriber(ServerConfiguration oConfiguration, Integer iEventType);

    public abstract Team loadTeam(String idTeam);

    public abstract TeamList loadTeams();

    public abstract Boolean isTeamManager();

    public abstract Team createTeam(String sLabel, String sDescription, String sUserList);

    public abstract Boolean saveTeam(Team oTeam, String sData);

    public abstract Team removeTeam(String id);

    public abstract Boolean removeTeams(String sData);
}
