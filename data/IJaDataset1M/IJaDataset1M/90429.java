package ca.ucalgary.cpsc.agilePlanner.persister.datachangeimplement;

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import ca.ucalgary.cpsc.agilePlanner.applicationWorkbench.Editor;
import ca.ucalgary.cpsc.agilePlanner.applicationWorkbench.uielements.ChangePersisterConnectDialogComoBox;
import ca.ucalgary.cpsc.agilePlanner.applicationWorkbench.uielements.ConfigurationDialog;
import ca.ucalgary.cpsc.agilePlanner.cards.model.BacklogModel;
import ca.ucalgary.cpsc.agilePlanner.cards.model.ContainerModel;
import ca.ucalgary.cpsc.agilePlanner.cards.model.IndexCardModel;
import ca.ucalgary.cpsc.agilePlanner.cards.model.IterationCardModel;
import ca.ucalgary.cpsc.agilePlanner.cards.model.ProjectModel;
import ca.ucalgary.cpsc.agilePlanner.cards.model.StoryCardModel;
import ca.ucalgary.cpsc.agilePlanner.filesystemaccess.FileSystemUtility;
import ca.ucalgary.cpsc.agilePlanner.mouse.remote.RemoteMouseModel;
import ca.ucalgary.cpsc.agilePlanner.persister.Backlog;
import ca.ucalgary.cpsc.agilePlanner.persister.IndexCardLiveUpdate;
import ca.ucalgary.cpsc.agilePlanner.persister.IndexCardNotFoundException;
import ca.ucalgary.cpsc.agilePlanner.persister.Iteration;
import ca.ucalgary.cpsc.agilePlanner.persister.Legend;
import ca.ucalgary.cpsc.agilePlanner.persister.MouseMove;
import ca.ucalgary.cpsc.agilePlanner.persister.Owner;
import ca.ucalgary.cpsc.agilePlanner.persister.PlannerDataChangeListener;
import ca.ucalgary.cpsc.agilePlanner.persister.PlannerUIChangeListener;
import ca.ucalgary.cpsc.agilePlanner.persister.Project;
import ca.ucalgary.cpsc.agilePlanner.persister.StoryCard;
import ca.ucalgary.cpsc.agilePlanner.persister.factory.PersisterConnectionInfo;
import ca.ucalgary.cpsc.agilePlanner.persister.xml.DisconnectData;

public class DataCallback extends ca.ucalgary.cpsc.agilePlanner.util.Object implements PlannerDataChangeListener, PlannerUIChangeListener {

    private CombinedTemplateCreationEntry combinedCreateEntry = null;

    private long ktime = 0;

    private long ltime = 0;

    private HashMap<Long, RemoteMouseModel> miceList = new HashMap<Long, RemoteMouseModel>();

    private ProjectModel projectModel;

    private Timestamp start, end;

    public char ch;

    public Editor editor;

    public Point keyPoint = new Point(0, 0);

    public Point mouseMovePoint = new Point(0, 0);

    public DataCallback() {
        this.start = new Timestamp(Date.valueOf("1000-1-1").getTime());
        this.end = new Timestamp(Date.valueOf("3000-1-1").getTime());
    }

    public DataCallback(ProjectModel projectModel) {
        this.projectModel = projectModel;
        this.start = new Timestamp(Date.valueOf("1000-1-1").getTime());
        this.end = new Timestamp(Date.valueOf("3000-1-1").getTime());
    }

    public void asynchronousException(Exception exception, int messageType) {
    }

    public void broughtToFront(long id) {
    }

    public void createdBacklog(Backlog backlog) {
        BacklogModel backlogModel = new BacklogModel(backlog);
        if (projectModel.getBacklogModel() == null) {
            projectModel.setNewBacklogModel(backlogModel);
        }
    }

    public void createdIteration(Iteration iteration) {
        IterationCardModel iterationCardModel = new IterationCardModel(iteration);
        projectModel.addNewIteration(iterationCardModel);
    }

    public void createdProjectOnInitialLoadFromServer(Project project) {
        if (projectModel != null) {
            createProjectOnSubsequentLoadsFromServer(project);
            return;
        }
        projectModel = new ProjectModel();
        ProjectModel.clearProjectModel(projectModel);
        projectModel.setProjectDataObject(project);
        Backlog backlog = project.getBacklog();
        if (backlog != null) {
            BacklogModel backlogModel = new BacklogModel(backlog);
            backlogModel.setLocation(new Point(0, 0));
            backlogModel.setSize(new Dimension(0, 0));
            backlogModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
            backlogModel.setProjectModel(projectModel);
            List<StoryCard> listStoryCardChildren = new ArrayList<StoryCard>(backlog.getStoryCardChildren());
            if (!(listStoryCardChildren.isEmpty())) {
                for (StoryCard storycard : listStoryCardChildren) {
                    StoryCardModel storyCardModel = new StoryCardModel(storycard);
                    projectModel.addChildIncommingOnLoad(storyCardModel);
                    storyCardModel = null;
                }
            }
        }
        if (project.getIterationChildren() != null) {
            for (Iteration iteration : project.getIterationChildren()) {
                IterationCardModel iterationCardModel = new IterationCardModel(iteration);
                iterationCardModel.setIterationEndDateIncomming(iteration.getEndDate().toString());
                iterationCardModel.setIterationStartDateIncomming(iteration.getStartDate().toString());
                iterationCardModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
                List<StoryCard> listStoryCardChildren = new ArrayList<StoryCard>(iteration.getStoryCardChildren());
                if (!(listStoryCardChildren.isEmpty())) {
                    for (StoryCard storycard : listStoryCardChildren) {
                        StoryCardModel storyCardModel = new StoryCardModel(storycard);
                        iterationCardModel.addChildIncommingOnLoad(storyCardModel);
                    }
                }
                this.projectModel.createIterationForInitialLoad(iterationCardModel);
            }
        }
        editor.setProjectModel(projectModel);
    }

    public void createProjectOnSubsequentLoadsFromServer(Project project) {
        ProjectModel.clearProjectModel(projectModel);
        projectModel.updateProject();
        projectModel.setProjectDataObject(project);
        Backlog backlog = project.getBacklog();
        if (backlog != null) {
            BacklogModel backlogModel = new BacklogModel(backlog);
            backlogModel.setLocation(new Point(0, 0));
            backlogModel.setSize(new Dimension(0, 0));
            backlogModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
            backlogModel.setProjectModel(projectModel);
            List<StoryCard> listStoryCardChildren = new ArrayList<StoryCard>(backlog.getStoryCardChildren());
            if (!(listStoryCardChildren.isEmpty())) {
                for (StoryCard storycard : listStoryCardChildren) {
                    StoryCardModel storyCardModel = new StoryCardModel(storycard);
                    projectModel.addChildIncommingOnLoad(storyCardModel);
                    storyCardModel = null;
                }
            }
        }
        if (project.getIterationChildren() != null) {
            for (Iteration iteration : project.getIterationChildren()) {
                IterationCardModel iterationCardModel = new IterationCardModel(iteration);
                iterationCardModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
                List<StoryCard> listStoryCardChildren = new ArrayList<StoryCard>(iteration.getStoryCardChildren());
                if (!(listStoryCardChildren.isEmpty())) {
                    for (StoryCard storycard : listStoryCardChildren) {
                        StoryCardModel storyCardModel = new StoryCardModel(storycard);
                        iterationCardModel.addChildIncommingOnLoad(storyCardModel);
                    }
                }
                this.projectModel.createIterationForInitialLoad(iterationCardModel);
            }
        }
        editor.updateTitleTab(this.projectModel.getProjectDataObject().getName());
        projectModel.updateProject();
    }

    public void arrangeProject(Project project) {
        projectModel.setProjectDataObject(project);
        Backlog backlog = project.getBacklog();
        if (backlog != null) {
            projectModel.getBacklogModel().setBacklogDataObject(backlog);
            projectModel.getBacklogModel().setTimestamp(new Timestamp(System.currentTimeMillis()));
            List<StoryCard> listStoryCardChildren = new ArrayList<StoryCard>(backlog.getStoryCardChildren());
            if (!(listStoryCardChildren.isEmpty())) {
                for (StoryCard storyCard : listStoryCardChildren) {
                    StoryCardModel storyCardModel = null;
                    try {
                        storyCardModel = (StoryCardModel) this.projectModel.findCard(storyCard.getId());
                    } catch (IndexCardNotFoundException e) {
                        ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
                    }
                    storyCardModel.setStoryCardDataObject(storyCard);
                    storyCardModel.setParent(projectModel.getBacklogModel());
                    storyCardModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
                }
            }
        }
        if (project.getIterationChildren() != null) {
            for (Iteration iteration : project.getIterationChildren()) {
                IterationCardModel iterationCardModel = null;
                try {
                    iterationCardModel = (IterationCardModel) projectModel.findCard(iteration.getId());
                } catch (IndexCardNotFoundException e) {
                    ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
                }
                iterationCardModel.setIterationDataObject(iteration);
                iterationCardModel.setIterationEndDateIncomming(iteration.getEndDate().toString());
                iterationCardModel.setIterationStartDateIncomming(iteration.getStartDate().toString());
                iterationCardModel.setLocationIncomming(new Point(iteration.getLocationX(), iteration.getLocationY()));
                if (!(iteration.getStoryCardChildren().isEmpty())) {
                    for (StoryCard storyCard : iteration.getStoryCardChildren()) {
                        StoryCardModel storyCardModel = null;
                        try {
                            storyCardModel = (StoryCardModel) this.projectModel.findCard(storyCard.getId());
                        } catch (IndexCardNotFoundException e) {
                            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
                        }
                        storyCardModel.setStoryCardDataObject(storyCard);
                    }
                    iterationCardModel.updateChildPriorities();
                    iterationCardModel.updateValuesFromChildren();
                }
            }
        }
        projectModel.updateChildrenFigure();
    }

    public void createdStoryCard(StoryCard storycard) {
        if (storycard.getHandwritingImage() != null) {
            storycard.setCurrentSideUp(storycard.HANDWRITING_SIDE);
        }
        StoryCardModel storyCardModel = new StoryCardModel(storycard);
        IndexCardModel parentCardModel = null;
        try {
            parentCardModel = projectModel.findCard(storycard.getParent());
        } catch (IndexCardNotFoundException e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
        }
        if (parentCardModel instanceof IterationCardModel) {
            ((IterationCardModel) parentCardModel).addNewChild(storyCardModel);
        }
        if (parentCardModel instanceof BacklogModel) {
            storyCardModel.setParent(this.projectModel.getBacklogModel());
            projectModel.addStoryCardAsChild(storyCardModel);
        }
    }

    public void deletedIteration(long id) {
        IterationCardModel iterationCardModel = null;
        try {
            iterationCardModel = (IterationCardModel) projectModel.findCard(id);
        } catch (IndexCardNotFoundException e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
        }
        this.projectModel.removeIteration(iterationCardModel);
    }

    public void deletedStoryCard(long id) {
        StoryCardModel storyCardModel = null;
        try {
            storyCardModel = (StoryCardModel) this.projectModel.findCard(id);
        } catch (IndexCardNotFoundException e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
        }
        if ((storyCardModel.getParent()) instanceof IterationCardModel) {
            ((IterationCardModel) storyCardModel.getParent()).removeChild(storyCardModel);
        } else projectModel.removeChildStoryCard(storyCardModel);
    }

    public void disconnectMouse(DisconnectData data) {
        try {
            List<IndexCardModel> children = projectModel.getRemoteMice();
            IndexCardModel toRemove = null;
            for (IndexCardModel model : children) {
                if (model instanceof RemoteMouseModel && ((RemoteMouseModel) model).getClientId() == data.getClientId()) {
                    toRemove = model;
                    break;
                }
            }
            children.remove(toRemove);
            projectModel.setRemoteMouseList(children, (RemoteMouseModel) toRemove);
        } catch (Exception e) {
            System.out.println("Ignoring disconnect mouse command.");
        }
    }

    public void downloadedFile(boolean bool) {
    }

    public Timestamp getEnd() {
        return end;
    }

    public long getKtime() {
        return ktime;
    }

    public long getLtime() {
        return ltime;
    }

    public ProjectModel getProjectModel() {
        return projectModel;
    }

    public Timestamp getStart() {
        return start;
    }

    public void gotProjectNames(List<String> str) {
        String[] temp = new String[str.size()];
        for (int loop = 0; loop < str.size(); loop++) {
            temp[loop] = str.get(loop);
        }
        ChangePersisterConnectDialogComoBox.gotProjectsNames(temp);
    }

    public void gotProjectNamesForLoginEvent(List<String> str) {
        String[] temp = new String[str.size()];
        for (int loop = 0; loop < str.size(); loop++) {
            temp[loop] = str.get(loop);
        }
        ChangePersisterConnectDialogComoBox.gotProjectsNamesForLoginEvent(temp);
    }

    public void liveTextUpdate(IndexCardLiveUpdate data) {
        IndexCardModel model = null;
        try {
            model = projectModel.findCard(data.getId());
        } catch (IndexCardNotFoundException e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
        }
        if (model instanceof StoryCardModel) {
            if (data.getField() == IndexCardLiveUpdate.FIELD_TITLE) ((StoryCardModel) model).setNameIncomming(data.getText()); else if (data.getField() == IndexCardLiveUpdate.FIELD_DESCRIPTION) {
                ((StoryCardModel) model).setDescriptionIncomming(data.getText());
            } else if (data.getField() == IndexCardLiveUpdate.FIELD_TESTTEXT) {
                ((StoryCardModel) model).setTestTextIncoming(data.getText());
            }
        } else if (model instanceof IterationCardModel) {
            if (data.getField() == IndexCardLiveUpdate.FIELD_TITLE) ((IterationCardModel) model).setNameIncomming(data.getText());
            if (data.getField() == IndexCardLiveUpdate.FIELD_DESCRIPTION) ((IterationCardModel) model).setDescriptionIncomming(data.getText());
        }
    }

    public void movedMouse(MouseMove mm) {
        try {
            this.mouseMovePoint = new Point(mm.getLocationX(), mm.getLocationY());
            RemoteMouseModel mouse;
            if (miceList.containsKey(mm.getId())) {
                mouse = miceList.get(mm.getId());
                mouse.setLocation(new Point(mm.getLocationX(), mm.getLocationY()));
            } else {
                mouse = new RemoteMouseModel(mm);
                miceList.put(mm.getId(), mouse);
                projectModel.addRemoteMouseIncomming(mouse);
            }
            List<IterationCardModel> children = projectModel.getIterations();
            LinkedList<IndexCardModel> cards = new LinkedList<IndexCardModel>();
            LinkedList<IndexCardModel> mice = new LinkedList<IndexCardModel>();
            for (IndexCardModel model : children) {
                if (model instanceof RemoteMouseModel) {
                    mice.add(model);
                } else {
                    cards.add(model);
                }
            }
            ltime = System.currentTimeMillis();
        } catch (Exception e) {
            System.out.println("Ignoring Mouse Move Event.");
        }
    }

    public void movedStoryCardToNewParent(StoryCard movedStorycard) {
        StoryCardModel storyCardModel = null;
        try {
            storyCardModel = (StoryCardModel) projectModel.findCard(movedStorycard.getId());
        } catch (IndexCardNotFoundException e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
        }
        ContainerModel storyCardModelOldParent = storyCardModel.getParent();
        storyCardModel.setStoryCardDataObject(movedStorycard);
        if (storyCardModelOldParent instanceof IterationCardModel) {
            ((IterationCardModel) storyCardModelOldParent).removeChild(storyCardModel);
        } else {
            this.projectModel.removeChildStoryCard(storyCardModel);
        }
        IndexCardModel storyCardModelNewParent = null;
        try {
            storyCardModelNewParent = projectModel.findCard(movedStorycard.getParent());
        } catch (IndexCardNotFoundException e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
        }
        if (storyCardModelNewParent instanceof IterationCardModel) {
            ((IterationCardModel) storyCardModelNewParent).addChildSetParent(storyCardModel);
        } else {
            storyCardModel.setParent(this.projectModel.getBacklogModel());
            this.projectModel.addStoryCardAsChild(storyCardModel);
        }
    }

    public void projectInXML(String xmlFileContents) {
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public void setProjectModel(ProjectModel table) {
        this.projectModel = table;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public void undeletedIteration(Iteration iteration) {
        IterationCardModel iterationCardModel = new IterationCardModel(iteration);
        iterationCardModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
        List<StoryCard> listStoryCardChildren = new ArrayList<StoryCard>(iteration.getStoryCardChildren());
        if (!(listStoryCardChildren.isEmpty())) {
            for (StoryCard storycard : listStoryCardChildren) {
                StoryCardModel storyCardModel = new StoryCardModel(storycard);
                iterationCardModel.addChildIncommingOnLoad(storyCardModel);
            }
        }
        this.projectModel.addNewIteration(iterationCardModel);
    }

    public void undeletedStoryCard(StoryCard storycard) {
        this.createdStoryCard(storycard);
    }

    public void updatedBacklog(Backlog backlog) {
        BacklogModel backlogModel = projectModel.getBacklogModel();
        backlogModel.setBacklogDataObject(backlog);
        backlogModel.setLocationIncomming(new Point(backlog.getLocationX(), backlog.getLocationY()));
        if (!(backlog.getStoryCardChildren().isEmpty())) {
            for (StoryCard storycard : backlog.getStoryCardChildren()) {
                this.updatedStoryCard(storycard);
            }
        }
        backlogModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
        this.projectModel.updateChildrenFigure();
    }

    public void updatedIteration(Iteration iteration) {
        IterationCardModel iterationCardModel = null;
        try {
            iterationCardModel = (IterationCardModel) projectModel.findCard(iteration.getId());
        } catch (IndexCardNotFoundException e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
        }
        this.projectModel.updateIteration(iterationCardModel, iteration);
        iterationCardModel.setIterationEndDateIncomming(iteration.getEndDate().toString());
        iterationCardModel.setIterationStartDateIncomming(iteration.getStartDate().toString());
        if (!(iteration.getStoryCardChildren().isEmpty())) {
            for (StoryCard storycard : iteration.getStoryCardChildren()) {
                this.updatedStoryCardForContainerCard(storycard);
            }
        }
    }

    public void updatedProjectName(Project project) {
    }

    public void updatedStoryCard(StoryCard storyCard) {
        StoryCardModel storyCardModel = null;
        try {
            storyCardModel = (StoryCardModel) this.projectModel.findCard(storyCard.getId());
        } catch (IndexCardNotFoundException e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
        }
        ContainerModel cm = storyCardModel.getParent();
        if (cm instanceof IterationCardModel) {
            ((IterationCardModel) cm).updateChild(storyCardModel);
        } else {
        }
        storyCardModel.setStoryCardDataObject(storyCard);
    }

    public void updatedStoryCardForContainerCard(StoryCard storycard) {
        StoryCardModel storyCardModel = null;
        try {
            storyCardModel = (StoryCardModel) this.projectModel.findCard(storycard.getId());
        } catch (IndexCardNotFoundException e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
        }
        storyCardModel.setLocationIntern(new Point(storycard.getLocationX(), storycard.getLocationY()));
    }

    public void uploadedFile(boolean bool) {
    }

    public void lostConnectionEvent() {
        String path = PersisterConnectionInfo.getPersisterConnectionInfo().getProjectLocationLocalMode();
        String fileName = this.projectModel.getProjectDataObject().getName();
        File file = new File(path + File.separator + fileName + ".xml");
        FileSystemUtility.getFileSystemUtility().saveFile(this.projectModel, file);
        String absoluteFileName = file.getAbsolutePath();
        this.editor.resetPersisterToLocalMode("dummy");
        MessageBox mbox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR);
        mbox.setText("Connection problem.");
        mbox.setMessage("Could not retrieve project model data from server. Switching to local mode now. File will be saved as " + absoluteFileName);
        mbox.open();
        this.projectModel.removeAllMiceIncoming();
    }

    public void updatedLegend(Legend leg) {
        this.projectModel.getProjectDataObject().setLegend(leg);
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                IWorkbenchWindow window = DataCallback.this.editor.getSite().getWorkbenchWindow();
                Editor ed = (Editor) ConfigurationDialog.resetEditor(window);
                DataCallback.this.editor = ed;
                DataCallback.this.editor.setProjectModel(DataCallback.this.projectModel);
            }
        });
    }

    public void createdOwner(Owner owner) {
    }
}
