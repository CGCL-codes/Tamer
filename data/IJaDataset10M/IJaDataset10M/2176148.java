package com.ivis.xprocess.ui.datawrappers.project;

import java.util.ArrayList;
import java.util.Set;
import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Color;
import com.ivis.xprocess.core.ArtifactReferenceGroup;
import com.ivis.xprocess.core.Assignment;
import com.ivis.xprocess.core.Gateway;
import com.ivis.xprocess.core.PrioritizedGroup;
import com.ivis.xprocess.core.RequiredResource;
import com.ivis.xprocess.core.SchedulingType;
import com.ivis.xprocess.core.WorkPackage;
import com.ivis.xprocess.core.Xproject;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.core.CompositeDelegate.CompositeType;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.ui.UIType;
import com.ivis.xprocess.ui.datawrappers.IContainArtifactReferences;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.datawrappers.IFolderWrapper;
import com.ivis.xprocess.ui.datawrappers.IPatternElement;
import com.ivis.xprocess.ui.datawrappers.ITask;
import com.ivis.xprocess.ui.datawrappers.NoneWrapper;
import com.ivis.xprocess.ui.datawrappers.ParentWrapper;
import com.ivis.xprocess.ui.datawrappers.TaskDirWrapper;
import com.ivis.xprocess.ui.datawrappers.WorkPackageWrapper;
import com.ivis.xprocess.ui.datawrappers.process.CategoryWrapper;
import com.ivis.xprocess.ui.editors.EditorAttributes;
import com.ivis.xprocess.ui.util.DateFormatter;
import com.ivis.xprocess.ui.util.ElementUtil;
import com.ivis.xprocess.ui.util.FontAndColorManager;
import com.ivis.xprocess.ui.wizards.taskplanner.RequiredResourceUtil;
import com.ivis.xprocess.util.Day;

/**
 * ElementWrapper for instantiated Xtasks.
 *
 */
public class TaskWrapper extends ParentWrapper implements ITask, WorkPackageWrapper, IPatternElement, IContainArtifactReferences {

    private static final Logger logger = Logger.getLogger(TaskWrapper.class);

    public TaskWrapper(IElementWrapper parent, Xelement xelement) {
        super(parent, xelement);
    }

    @Override
    public boolean testAttribute(Object target, String name, String value) {
        if (isGhost()) {
            return false;
        }
        boolean flag = super.testAttribute(target, name, value);
        if (name.equals("hasClipboardMenu")) {
            flag = true;
        }
        if (name.equals("canCut")) {
            return false;
        }
        if (name.equals("hasEditor")) {
            return true;
        }
        if (name.equals(EditorAttributes.isOverheadTask)) {
            return getTask().getSchedulingType() == SchedulingType.OVERHEAD;
        }
        if (name.equals("hasHyperlinkMenu")) {
            return true;
        }
        if (name.equals("isOpen")) {
            return !getTask().isClosed();
        }
        if (name.equals("hasBeenAssigned")) {
            Xtask task = getTask();
            return task.getAllAssignments().iterator().hasNext();
        }
        if (name.equals("isClosed") && value.toLowerCase().equals("false")) {
            if (getTask().isClosed()) {
                return false;
            }
            return true;
        }
        if (name.equals("isActive")) {
            if (value.equals("true")) {
                return getTask().isActive();
            }
            return !getTask().isActive();
        }
        if (name.equals("canDelete")) {
            if (this instanceof TaskDirWrapper) {
                return false;
            }
            if ((this.getParent() != null) && this.getParent() instanceof CategoryWrapper) {
                CategoryWrapper categoryWrapper = (CategoryWrapper) this.getParent();
                if (categoryWrapper.getCategory().getCategoryType().getUncategorizedCategory().equals(categoryWrapper.getCategory())) {
                    return false;
                }
            }
            return true;
        }
        if (name.equals("hasCategories")) {
            return true;
        }
        if (name.equals("canViewCategories")) {
            return true;
        }
        if (name.equals("member")) {
            if (parent instanceof IFolderWrapper) {
                return true;
            }
        }
        if (name.equals("canMakeActive")) {
            if (getTask().isDesignatedAsParent()) {
                return false;
            }
            return RequiredResourceUtil.showActiveMenu(getTask()) && !RequiredResourceUtil.showInActiveMenu(getTask());
        }
        if (name.equals("canMakeInActive")) {
            if (getTask().isDesignatedAsParent()) {
                return false;
            }
            return RequiredResourceUtil.showInActiveMenu(getTask()) && !RequiredResourceUtil.showActiveMenu(getTask());
        }
        if (name.equals("canMakeInActiveOrActive")) {
            return RequiredResourceUtil.showInActiveMenu(getTask()) && RequiredResourceUtil.showActiveMenu(getTask());
        }
        if (name.equals("canMakeParent")) {
            if ((getTask().getSchedulingType() != SchedulingType.OVERHEAD) && (getTask().getTasks().size() == 0)) {
                return !RequiredResourceUtil.showInActiveMenu(getTask());
            }
            return false;
        }
        if (name.equals("isParent")) {
            return getTask().isDesignatedAsParent();
        }
        if (name.equals("hasComposite")) {
            return getTask().getCompositeDelegate() != null;
        }
        if (name.equals("canPrioritize")) {
            if (getTask().isDesignatedAsParent()) {
                return true;
            }
            return false;
        }
        if (name.equals("notAGhost")) {
            return true;
        }
        return flag;
    }

    @Override
    public boolean hasChildren() {
        Xtask task = getTask();
        if (isDeleted() || task.isGhost()) {
            return false;
        }
        for (Xelement xelement : task.getExplorerContents()) {
            if (xelement instanceof Assignment && !xelement.isGhost()) {
                Assignment assignment = (Assignment) xelement;
                if (assignment.getCurrentRole() != null) {
                    return true;
                }
                if (assignment.getPreviousRoles().size() > 0) {
                    return true;
                }
            }
            if (xelement instanceof Xtask && !xelement.isGhost()) {
                return true;
            }
            if (xelement instanceof ArtifactReferenceGroup) {
                return true;
            }
            if (xelement instanceof Gateway) {
                return true;
            }
        }
        return false;
    }

    @Override
    public UIType getUIType() {
        return getUIType(getTask());
    }

    public static UIType getUIType(Xtask task) {
        if (task.isDesignatedAsParent()) {
            CompositeType compositeType = task.isComposite() ? task.getCompositeDelegate().getCompositeType() : CompositeType.COLLECTION;
            switch(compositeType) {
                case SELECTION:
                    if (task.isTopDown()) {
                        if (task.isNotToBeScheduled()) {
                            return UIType.parent_selection_top_down_ntbs;
                        } else {
                            return UIType.parent_selection_top_down;
                        }
                    } else {
                        if (task.isNotToBeScheduled()) {
                            return UIType.parent_selection_bottom_up_ntbs;
                        } else {
                            return UIType.parent_selection_bottom_up;
                        }
                    }
                case ITERATION:
                    if (task.isTopDown()) {
                        if (task.isNotToBeScheduled()) {
                            return UIType.parent_iteration_top_down_ntbs;
                        } else {
                            return UIType.parent_iteration_top_down;
                        }
                    } else {
                        if (task.isNotToBeScheduled()) {
                            return UIType.parent_iteration_bottom_up_ntbs;
                        } else {
                            return UIType.parent_iteration_bottom_up;
                        }
                    }
                case COLLECTION:
                default:
                    if (task.isTopDown()) {
                        if (task.isNotToBeScheduled()) {
                            return UIType.parent_collection_top_down_ntbs;
                        } else {
                            return UIType.parent_collection_top_down;
                        }
                    } else {
                        if (task.isNotToBeScheduled()) {
                            return UIType.parent_collection_bottom_up_ntbs;
                        } else {
                            return UIType.parent_collection_bottom_up;
                        }
                    }
            }
        } else {
            if (task.getSchedulingType() == SchedulingType.OVERHEAD) {
                if (task.isNotToBeScheduled()) {
                    return UIType.overheadtask_ntbs;
                } else {
                    return UIType.overheadtask;
                }
            } else {
                if (task.isNotToBeScheduled()) {
                    return UIType.task_ntbs;
                } else {
                    if (task.isActive()) {
                        return UIType.activetask;
                    } else {
                        return UIType.task;
                    }
                }
            }
        }
    }

    @Override
    public String getDecoratorInfo() {
        String decoratorInfo = super.getDecoratorInfo();
        Xtask task = getTask();
        if (task.isClosed()) {
            return "closed";
        }
        if (task.getPriorities() != null) {
            if (decoratorInfo.length() > 0) {
                decoratorInfo += ",";
            }
            decoratorInfo += "prioritized";
        }
        if (task.getSchedulingType().equals(SchedulingType.OVERHEAD)) {
            return decoratorInfo;
        }
        if (task.getTargetEnd() == null) {
            return decoratorInfo;
        }
        if (!task.isNotToBeScheduled()) {
            if (decoratorInfo.length() > 0) {
                decoratorInfo += ",";
            }
            decoratorInfo += getStatus(task.getTargetEnd(), task.getEnd50(), task.getEnd75(), task.getEnd95());
        }
        return decoratorInfo;
    }

    /**
     * @param elementWrapper
     * @return the status colour of the Task, Red, Orange, Yellow, Green, or White (default)
     */
    public Color getBackgroundColorRelativeToContext(IElementWrapper elementWrapper) {
        Xtask task = getTask();
        if (task.isClosed() || task.isNotToBeScheduled()) {
            return FontAndColorManager.getInstance().getColor("255:255:255");
        }
        Day targetEnd = ElementUtil.getTargetEnd(elementWrapper);
        String status = "";
        status = getStatus(targetEnd, task.getEnd50(), task.getEnd75(), task.getEnd95());
        if (status.length() == 0) {
            return FontAndColorManager.getInstance().getColor("255:255:255");
        }
        Color statusColor = FontAndColorManager.getStatusColor(status);
        if (task.getEnd50() != null) {
            return statusColor;
        }
        return FontAndColorManager.getInstance().getColor("255:255:255");
    }

    @Override
    public Object[] getContents() {
        Xtask task = getTask();
        Day startDay;
        startDay = task.getDayProperty("WORKPACKAGE_TARGET_START_DAY");
        Day endDay;
        endDay = task.getDayProperty("WORKPACKAGE_TARGET_END_DAY");
        DateFormatter dateFormatter = new DateFormatter();
        ArrayList<Object> content = new ArrayList<Object>();
        if (startDay != null) {
            content.add("Target Start: " + dateFormatter.formatDay(startDay));
        } else {
            content.add("Target Start: none");
        }
        if (endDay != null) {
            content.add("Target End: " + dateFormatter.formatDay(endDay));
        } else {
            content.add("Target End: none");
        }
        for (int i = 0; i < getChildren().length; i++) {
            content.add(getChildren()[i]);
        }
        return content.toArray();
    }

    @Override
    public Object[] getContentsByType(String type) {
        if (type.equals("GatewayTypes")) {
            Xtask task = this.getTask();
            if (task.getContainedIn() instanceof Xproject) {
                Xproject project = (Xproject) task.getContainedIn();
                IElementWrapper[] elementWrappers = ElementUtil.getGatewayTypesAvailableFor(project.getProcess());
                ArrayList<IElementWrapper> wrappers = new ArrayList<IElementWrapper>();
                for (IElementWrapper elementWrapper : elementWrappers) {
                    wrappers.add(elementWrapper);
                }
                wrappers.add(new NoneWrapper());
                return wrappers.toArray();
            }
        }
        logger.error("Type - " + type + " not supported by: " + this);
        return null;
    }

    public Xtask getTask() {
        return (Xtask) getElement();
    }

    /**
     * @return the instantiated Project that this Task is in
     */
    public Xproject getProject() {
        return getTask().getProject();
    }

    public Object[] getTasks() {
        return getChildrenByType(TaskWrapper.class);
    }

    public Object[] getTasksInScheduleOrder() {
        return getTasks();
    }

    public Object[] getRequiredResources() {
        Xtask task = getTask();
        Set<RequiredResource> set = task.getRequiredResources();
        return set.toArray();
    }

    public IElementWrapper getElementWrapper() {
        return this;
    }

    public Set<ArtifactReferenceGroup> getArtifactReferences() {
        return getTask().getArtifactReferenceGroups();
    }

    public WorkPackage getWorkPackage() {
        return (WorkPackage) xelement;
    }

    @Override
    @Deprecated
    public Xelement getElement() {
        return super.getElement();
    }

    /**
     * @return the PrioritizedGroup of the Task
     */
    public PrioritizedGroup getPrioritizedGroup() {
        return getTask().getPriorities();
    }
}
