package com.prolix.editor.graph.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Display;
import uk.ac.reload.straker.datamodel.DataComponent;
import uk.ac.reload.straker.datamodel.learningdesign.components.activities.SupportActivity;
import uk.ac.reload.straker.datamodel.learningdesign.components.environments.Conference;
import uk.ac.reload.straker.datamodel.learningdesign.components.environments.Environment;
import uk.ac.reload.straker.datamodel.learningdesign.components.environments.SendMail;
import uk.ac.reload.straker.datamodel.learningdesign.components.roles.Role;
import uk.ac.reload.straker.datamodel.learningdesign.types.EmailDataType;
import com.prolix.editor.DND.CopyPastModelTransfer;
import com.prolix.editor.commands.roles.DeleteRoleCommand;
import com.prolix.editor.dialogs.roles.CopyPasteMapRolesDialog;
import com.prolix.editor.general.CopyClass;
import com.prolix.editor.graph.editparts.MainEditPart;
import com.prolix.editor.graph.model.ModelDiagramElement;
import com.prolix.editor.graph.model.ModelDiagramMain;
import com.prolix.editor.graph.model.activities.ModelDiagramActivity;
import com.prolix.editor.graph.model.activities.ModelDiagramActivityLearning;
import com.prolix.editor.graph.model.activities.ModelDiagramActivitySupport;
import com.prolix.editor.graph.model.activities.ModelSelectionPoint;
import com.prolix.editor.graph.model.connections.ModelConnection;
import com.prolix.editor.graph.model.points.ModelSyncPoint;
import com.prolix.editor.graph.model.points.ModelTextNode;
import com.prolix.editor.interaction.commands.interaction.CreateInteractionCommand;
import com.prolix.editor.interaction.commands.interaction.DeleteInteractionCommand;
import com.prolix.editor.interaction.model.Interaction;
import com.prolix.editor.interaction.operation.Operation;
import com.prolix.editor.interaction.operation.basic.BasicOperationDisplay;
import com.prolix.editor.interaction.operation.basic.BasicOperationEvaluate;
import com.prolix.editor.interaction.operation.basic.BasicOperationReadOnly;
import com.prolix.editor.interaction.operation.test.TestOperationDisplay;
import com.prolix.editor.interaction.operation.test.TestOperationEvaluate;
import com.prolix.editor.interaction.operation.test.TestOperationReadOnly;
import com.prolix.editor.resourcemanager.commands.RemoveResourceItemCommand;
import com.prolix.editor.resourcemanager.model.ResourceTreeItem;
import com.prolix.editor.resourcemanager.zip.LearningDesignDataModel;
import com.prolix.editor.roleview.roles.RoleItem;
import com.prolix.editor.roleview.roles.RoleRole;

public class PasteModelCommand extends Command {

    private LearningDesignDataModel lddm;

    private boolean eigenesLD;

    private Point basisQuelle;

    private Point courserLocation;

    private Map rolesSpeicher;

    private List neueRollen;

    private Map neueActivities;

    private Map neueEnvrionments;

    private Map neueSyncPoints;

    private Map neueNodePoints;

    private Map neueInteractions;

    private List undoCommands;

    private List fileSpeicher;

    /**
	 * 
	 */
    public PasteModelCommand(LearningDesignDataModel lddm) {
        super("Paste");
        this.lddm = lddm;
        this.neueActivities = new HashMap();
        this.neueEnvrionments = new HashMap();
        this.neueInteractions = new HashMap();
        this.neueSyncPoints = new HashMap();
        this.neueNodePoints = new HashMap();
    }

    public boolean canExecute() {
        if (lddm == null) {
            return false;
        }
        return CopyPastModelTransfer.getInstance().hasTransfer();
    }

    public void execute() {
        if (!this.initCopy()) {
            this.lddm = null;
            return;
        }
        Iterator it = CopyPastModelTransfer.getInstance().getActivities().iterator();
        while (it.hasNext()) {
            ModelDiagramActivity activity = (ModelDiagramActivity) it.next();
            this.copyActivty(activity);
        }
        it = CopyPastModelTransfer.getInstance().getSyncPoints().iterator();
        while (it.hasNext()) {
            ModelSyncPoint syncPoint = (ModelSyncPoint) it.next();
            this.copySyncPoint(syncPoint);
        }
        it = CopyPastModelTransfer.getInstance().getNotes().iterator();
        while (it.hasNext()) {
            ModelTextNode node = (ModelTextNode) it.next();
            copyNodePoint(node);
        }
        List tmpSpeicher = new ArrayList();
        tmpSpeicher.addAll(this.neueActivities.keySet());
        tmpSpeicher.addAll(this.neueSyncPoints.keySet());
        it = tmpSpeicher.iterator();
        while (it.hasNext()) {
            ModelDiagramElement original = (ModelDiagramElement) it.next();
            this.linkElements(original, (ModelDiagramElement) findElementCopy(original));
        }
        this.fileSpeicher = CopyClass.getResourceSpeicher();
        CopyClass.closeResourceSpeicher();
        selectElements();
    }

    private void selectElements() {
        lddm.getEditor().getGraphicalViewer().deselectAll();
        ModelDiagramMain maindiag = lddm.getModel();
        GraphicalViewer viewer = lddm.getEditor().getGraphicalViewer();
        MainEditPart mainEditPart = (MainEditPart) viewer.getEditPartRegistry().get(maindiag);
        List toSelectElements = new ArrayList();
        toSelectElements.addAll(neueActivities.values());
        toSelectElements.addAll(this.neueSyncPoints.values());
        toSelectElements.addAll(this.neueNodePoints.values());
        Iterator it = toSelectElements.iterator();
        while (it.hasNext()) {
            ModelDiagramElement activity = (ModelDiagramElement) it.next();
            EditPart part = mainEditPart.findEditPartForModel(activity);
            viewer.appendSelection(part);
        }
    }

    private boolean initCopy() {
        this.getCursorLocation();
        Iterator it = CopyPastModelTransfer.getInstance().getActivities().iterator();
        if (it.hasNext()) {
            ModelDiagramActivity activity = (ModelDiagramActivity) it.next();
            this.basisQuelle = activity.getLocation().getCopy();
            if (lddm.equals(activity.getLearningDesign().getDataModel())) {
                this.eigenesLD = true;
            }
        }
        while (it.hasNext()) {
            Point comp = ((ModelDiagramActivity) it.next()).getLocation();
            if (this.basisQuelle.x > comp.x) {
                this.basisQuelle.x = comp.x;
            }
            if (this.basisQuelle.y > comp.y) {
                this.basisQuelle.y = comp.y;
            }
        }
        if (!this.eigenesLD) {
            if (!this.initRoles()) {
                return false;
            }
            this.initEnvrionments();
            this.initInteractions();
            CopyClass.initResourceSpeicher();
        }
        return true;
    }

    private boolean initRoles() {
        List rollen = new ArrayList();
        Iterator it = CopyPastModelTransfer.getInstance().getActivities().iterator();
        while (it.hasNext()) {
            ModelDiagramActivity activity = (ModelDiagramActivity) it.next();
            if (!rollen.contains(activity.getRole()) && activity.getRole() != null) {
                rollen.add(activity.getRole());
            }
            if (activity instanceof ModelDiagramActivitySupport) {
                SupportActivity support = ((ModelDiagramActivitySupport) activity).getSupportActivity();
                Role[] roles = support.getRoles();
                if (roles != null && roles.length > 0) {
                    for (int i = 0; i < roles.length; i++) {
                        RoleRole role = (RoleRole) ((LearningDesignDataModel) support.getDataModel()).getRoleGroupMain().findMenueItem(roles[i].getIdentifier());
                        if (role != null && !rollen.contains(role)) {
                            rollen.add(role);
                        }
                    }
                }
            }
            this.initRolesEnvironments(activity, rollen);
            this.initRolesOperations(activity, rollen);
        }
        if (rollen.isEmpty()) {
            this.rolesSpeicher = new HashMap();
            this.neueRollen = new ArrayList();
            return true;
        }
        CopyPasteMapRolesDialog diag = new CopyPasteMapRolesDialog(Display.getCurrent().getActiveShell(), this.lddm, rollen);
        boolean ret = diag.openDialog();
        this.rolesSpeicher = diag.getLinkRollenMap();
        this.neueRollen = diag.getNeueRollen();
        return ret;
    }

    private void initInteractions() {
        List interactions = new ArrayList();
        Iterator it = CopyPastModelTransfer.getInstance().getActivities().iterator();
        while (it.hasNext()) {
            ModelDiagramActivity activity = (ModelDiagramActivity) it.next();
            Iterator it1 = activity.getInteractionOperations().iterator();
            while (it1.hasNext()) {
                Operation operation = (Operation) it1.next();
                if (!interactions.contains(operation.getInteraction())) {
                    interactions.add(operation.getInteraction());
                }
            }
        }
        it = interactions.iterator();
        while (it.hasNext()) {
            Interaction orig = (Interaction) it.next();
            Interaction copy = orig.duplicate(this.lddm.getInteractionRoot());
            CreateInteractionCommand command = new CreateInteractionCommand(copy);
            if (command.canExecute()) {
                command.execute();
                this.neueInteractions.put(orig, copy);
            }
        }
    }

    private void initRolesEnvironments(ModelDiagramActivity activity, List rollen) {
        if (!activity.hasEnvironments()) {
            return;
        }
        Iterator it = activity.getEnvironments().iterator();
        RoleItem rolesmain = ((LearningDesignDataModel) activity.getLearningDesign().getDataModel()).getRoleGroupMain();
        while (it.hasNext()) {
            Environment environment = (Environment) it.next();
            DataComponent[] dc = (DataComponent[]) environment.getChildren();
            for (int i = 0; i < dc.length; i++) {
                if (dc[i] instanceof Conference) {
                    Conference c = (Conference) dc[i];
                    Object role = rolesmain.findMenueItem(c.getManagerRoleRef());
                    if (role != null && !rollen.contains(role)) {
                        rollen.add(role);
                    }
                    role = rolesmain.findMenueItem(c.getModeratorRoleRef());
                    if (role != null && !rollen.contains(role)) {
                        rollen.add(role);
                    }
                    Role[] roles = c.getObservers();
                    for (int j = 0; j < roles.length; j++) {
                        role = rolesmain.findMenueItem(roles[j].getIdentifier());
                        if (!rollen.contains(role)) {
                            rollen.add(role);
                        }
                    }
                    roles = c.getParticipants();
                    for (int j = 0; j < roles.length; j++) {
                        role = rolesmain.findMenueItem(roles[j].getIdentifier());
                        if (!rollen.contains(role)) {
                            rollen.add(role);
                        }
                    }
                }
                if (dc[i] instanceof SendMail) {
                    SendMail m = (SendMail) dc[i];
                    EmailDataType[] edt = m.getEmailData();
                    for (int j = 0; j < edt.length; j++) {
                        Object role = rolesmain.findMenueItem(edt[j].getRoleRef().getRef());
                        if (!rollen.contains(role)) {
                            rollen.add(role);
                        }
                    }
                }
            }
        }
    }

    private void initRolesOperations(ModelDiagramActivity activity, List rollen) {
        Iterator it = activity.getInteractionOperations().iterator();
        while (it.hasNext()) {
            Operation o = (Operation) it.next();
            RoleRole tmp = null;
            if (o instanceof BasicOperationEvaluate) {
                if (((BasicOperationEvaluate) o).isReadOther()) {
                    tmp = ((BasicOperationEvaluate) o).getReadOtherRole();
                }
            }
            if (o instanceof BasicOperationDisplay || o instanceof BasicOperationReadOnly) {
                if (((BasicOperationDisplay) o).isReadOther()) {
                    tmp = ((BasicOperationDisplay) o).getReadOtherRole();
                }
            }
            if (o instanceof TestOperationDisplay || o instanceof TestOperationReadOnly) {
                if (((TestOperationDisplay) o).isReadOther()) {
                    tmp = ((TestOperationDisplay) o).getReadOtherRole();
                }
            }
            if (o instanceof TestOperationEvaluate) {
                if (((TestOperationEvaluate) o).isReadOther()) {
                    tmp = ((TestOperationDisplay) o).getReadOtherRole();
                }
            }
            if (tmp != null) {
                if (!rollen.contains(tmp)) {
                    rollen.add(tmp);
                }
            }
        }
    }

    private void initEnvrionments() {
        Iterator it = CopyPastModelTransfer.getInstance().getActivities().iterator();
        List environments = new ArrayList();
        while (it.hasNext()) {
            ModelDiagramActivity activity = (ModelDiagramActivity) it.next();
            Iterator envis = activity.getEnvironments().iterator();
            while (envis.hasNext()) {
                Object envi = envis.next();
                if (!environments.contains(envi)) {
                    environments.add(envi);
                }
            }
        }
        it = environments.iterator();
        while (it.hasNext()) {
            Environment original = (Environment) it.next();
            Environment copy = new Environment(this.lddm);
            this.lddm.getLearningDesign().getComponents().getEnvironments().addChild(copy);
            this.neueEnvrionments.put(original.getIdentifier(), copy);
            CopyClass.copyEnvironment(original, copy, this.rolesSpeicher);
            copy.getDataModel().fireDataComponentAdded(copy);
        }
    }

    private void copyActivty(ModelDiagramActivity original) {
        ModelDiagramActivity activity = this.getNewActivityInstance(original);
        this.neueActivities.put(original, activity);
        activity.setLearningDesign(this.lddm.getLearningDesign());
        activity.setLocation(this.getLocation(original));
        activity.setParent(this.lddm.getModel());
        activity.executeGen();
        DataComponent data = activity.getDataComponent();
        if (data != null) {
            data.getParent().addChild(data);
            data.getDataModel().fireDataComponentAdded(data);
        }
        this.lddm.getModel().addChild(activity);
        this.getCopyActivityInstance(original, activity);
        activity.setName(activity.getName());
        activity.setSize(original.getSize().getCopy());
        linkRole(original, activity);
        linkInteractions(original, activity);
        activity.getParent().updatePosElements();
        activity.fireRefresh();
    }

    private void linkElements(ModelDiagramElement original, ModelDiagramElement copy) {
        Iterator it = original.getConnectionStart().iterator();
        while (it.hasNext()) {
            Object ziel = ((ModelConnection) it.next()).getTarget();
            if (ziel instanceof ModelDiagramElement) {
                try {
                    ConnectionCommand command = new ConnectionCommand();
                    command.setSource(copy);
                    command.setTarget(findElementCopy((ModelDiagramElement) ziel));
                    command.setConnection(new ModelConnection());
                    command.execute();
                } catch (IllegalArgumentException e) {
                }
            }
        }
    }

    private void linkRole(ModelDiagramActivity original, ModelDiagramActivity copy) {
        if (original.getRole() == null) {
            return;
        }
        if (this.eigenesLD) {
            copy.setRole(original.getRole());
            return;
        }
        Object role = this.rolesSpeicher.get(original.getRole().getData().getIdentifier());
        if (role != null && role instanceof RoleRole) {
            copy.setRole((RoleRole) role);
        }
    }

    private void linkInteractions(ModelDiagramActivity original, ModelDiagramActivity copy) {
        Iterator it = original.getInteractionOperations().iterator();
        while (it.hasNext()) {
            Operation oOrig = (Operation) it.next();
            Interaction interaction = oOrig.getInteraction();
            if (!this.eigenesLD) {
                interaction = (Interaction) this.neueInteractions.get(interaction);
            }
            Operation oCopy = oOrig.dublicate(interaction, copy, this.rolesSpeicher);
            copy.addInteractionOperation(oCopy);
        }
    }

    private Point getLocation(ModelDiagramElement original) {
        Point location = this.getCursorLocation();
        location.x += original.getLocation().x - this.basisQuelle.x;
        location.y += original.getLocation().y - this.basisQuelle.y;
        return location;
    }

    private Point getCursorLocation() {
        if (this.courserLocation != null) {
            return this.courserLocation.getCopy();
        }
        int x = 0;
        int y = 0;
        org.eclipse.swt.graphics.Point p = this.lddm.getEditor().getGEFEditor().getAbsolutPosition();
        org.eclipse.swt.graphics.Point pos = this.lddm.getEditor().getGraphicalViewer().getControl().getDisplay().getCursorLocation();
        x = pos.x - p.x;
        y = pos.y - p.y - 50;
        this.courserLocation = new Point(x, y);
        this.courserLocation = this.lddm.getEditor().getGEFEditor().translateLocationScrollbar(this.courserLocation);
        return this.courserLocation.getCopy();
    }

    private ModelDiagramActivity getNewActivityInstance(ModelDiagramActivity original) {
        if (original instanceof ModelDiagramActivityLearning) {
            return new ModelDiagramActivityLearning();
        }
        if (original instanceof ModelDiagramActivitySupport) {
            return new ModelDiagramActivitySupport();
        }
        if (original instanceof ModelSelectionPoint) {
            return new ModelSelectionPoint();
        }
        throw new IllegalArgumentException();
    }

    private void getCopyActivityInstance(ModelDiagramActivity original, ModelDiagramActivity copy) {
        Map neueElemente = null;
        if (!this.eigenesLD) {
            neueElemente = new HashMap();
            neueElemente.putAll(this.rolesSpeicher);
            neueElemente.putAll(this.neueEnvrionments);
        }
        if (copy instanceof ModelDiagramActivityLearning) {
            if (this.eigenesLD) {
                CopyClass.copyLearningActivity(((ModelDiagramActivityLearning) original).getLearningActivity(), ((ModelDiagramActivityLearning) copy).getLearningActivity());
                return;
            }
            CopyClass.copyLearningActivity(((ModelDiagramActivityLearning) original).getLearningActivity(), ((ModelDiagramActivityLearning) copy).getLearningActivity(), neueElemente);
            return;
        }
        if (copy instanceof ModelDiagramActivitySupport) {
            if (this.eigenesLD) {
                CopyClass.copySupportActivity(((ModelDiagramActivitySupport) original).getSupportActivity(), ((ModelDiagramActivitySupport) copy).getSupportActivity());
                return;
            }
            CopyClass.copySupportActivity(((ModelDiagramActivitySupport) original).getSupportActivity(), ((ModelDiagramActivitySupport) copy).getSupportActivity(), neueElemente);
            return;
        }
        if (copy instanceof ModelSelectionPoint) {
            ModelSelectionPoint mspCopy = (ModelSelectionPoint) copy;
            ModelSelectionPoint mspOrig = (ModelSelectionPoint) original;
            mspCopy.setNumberToSelect(mspOrig.getNumberToSelect());
            if (mspOrig.hasEnvironments()) {
                Iterator it = mspOrig.getEnvironments().iterator();
                while (it.hasNext()) {
                    Environment envi = (Environment) it.next();
                    if (!this.eigenesLD) {
                        envi = (Environment) this.neueEnvrionments.get(envi.getIdentifier());
                    }
                    if (envi != null) {
                        mspCopy.addEnvironment(envi);
                    }
                }
            }
            return;
        }
        throw new IllegalArgumentException();
    }

    private void copySyncPoint(ModelSyncPoint original) {
        ModelSyncPoint syncPoint = new ModelSyncPoint();
        this.neueSyncPoints.put(original, syncPoint);
        syncPoint.setLearningDesign(this.lddm.getLearningDesign());
        syncPoint.setLocation(this.getLocation(original));
        syncPoint.setParent(this.lddm.getModel());
        syncPoint.executeGen();
        this.lddm.getModel().addChild(syncPoint);
        syncPoint.getParent().updatePosElements();
        syncPoint.fireRefresh();
    }

    private void copyNodePoint(ModelTextNode original) {
        ModelTextNode node = new ModelTextNode();
        this.neueNodePoints.put(original, node);
        node.setLearningDesign(this.lddm.getLearningDesign());
        node.setText(original.getText());
        node.setLocation(this.getLocation(original));
        node.setParent(this.lddm.getModel());
        node.executeGen();
        this.lddm.getModel().addChild(node);
        node.getParent().updatePosElements();
        node.fireRefresh();
    }

    private ModelDiagramElement findElementCopy(ModelDiagramElement original) {
        if (this.neueActivities.containsKey(original)) {
            return (ModelDiagramElement) this.neueActivities.get(original);
        }
        if (this.neueSyncPoints.containsKey(original)) {
            return (ModelDiagramElement) this.neueSyncPoints.get(original);
        }
        throw new IllegalArgumentException();
    }

    public boolean canUndo() {
        return lddm != null;
    }

    public void redo() {
        if (this.undoCommands == null) {
            throw new IllegalAccessError();
        }
        for (int i = this.undoCommands.size() - 1; i >= 0; i--) {
            if (((Command) this.undoCommands.get(i)).canUndo()) {
                ((Command) this.undoCommands.get(i)).undo();
            }
        }
    }

    public void undo() {
        boolean exe = false;
        if (this.undoCommands == null) {
            exe = true;
            this.undoCommands = new ArrayList();
            undoCreateElementsCommands();
            undoCreateEnvironmentsCommands();
            undoCreateInteractionsCommands();
            undoCreateRolesCommands();
            undoCreateResources();
        }
        Iterator it = this.undoCommands.iterator();
        while (it.hasNext()) {
            if (exe) {
                ((Command) it.next()).execute();
            } else {
                ((Command) it.next()).redo();
            }
        }
    }

    private void undoCreateElementsCommands() {
        List tmpListe = new ArrayList();
        tmpListe.addAll(this.neueActivities.values());
        tmpListe.addAll(this.neueSyncPoints.values());
        tmpListe.addAll(this.neueNodePoints.values());
        Iterator it = tmpListe.iterator();
        while (it.hasNext()) {
            ModelDiagramElement element = (ModelDiagramElement) it.next();
            DeleteCommand del = new DeleteCommand();
            del.setChild(element);
            del.setParent(element.getParent());
            this.undoCommands.add(del);
        }
    }

    private void undoCreateEnvironmentsCommands() {
        Iterator it = this.neueEnvrionments.values().iterator();
        while (it.hasNext()) {
            Environment environment = (Environment) it.next();
            com.prolix.editor.commands.DeleteCommand del = new com.prolix.editor.commands.DeleteCommand(environment);
            del.buildChain();
            this.undoCommands.add(del);
        }
    }

    private void undoCreateRolesCommands() {
        if (neueRollen == null) {
            return;
        }
        Iterator it = this.neueRollen.iterator();
        while (it.hasNext()) {
            RoleRole role = (RoleRole) it.next();
            DeleteRoleCommand del = new DeleteRoleCommand(role);
            del.buildChain();
            this.undoCommands.add(del);
        }
    }

    private void undoCreateInteractionsCommands() {
        Iterator it = this.neueInteractions.values().iterator();
        while (it.hasNext()) {
            Interaction interaction = (Interaction) it.next();
            DeleteInteractionCommand del = new DeleteInteractionCommand();
            del.setInteraction(interaction);
            this.undoCommands.add(del);
        }
    }

    private void undoCreateResources() {
        if (this.fileSpeicher == null) {
            return;
        }
        Iterator it = this.fileSpeicher.iterator();
        while (it.hasNext()) {
            ResourceTreeItem item = (ResourceTreeItem) it.next();
            this.undoCommands.add(new RemoveResourceItemCommand(item));
        }
    }
}
