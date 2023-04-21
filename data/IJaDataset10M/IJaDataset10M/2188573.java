package com.prolix.editor.commands.roles;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Color;
import com.prolix.editor.resourcemanager.zip.LearningDesignDataModel;
import com.prolix.editor.roleview.roles.RoleItem;
import com.prolix.editor.roleview.roles.RoleRole;
import uk.ac.reload.straker.datamodel.learningdesign.components.roles.Learner;
import uk.ac.reload.straker.datamodel.learningdesign.components.roles.Role;
import uk.ac.reload.straker.datamodel.learningdesign.components.roles.RoleGroup;
import uk.ac.reload.straker.datamodel.learningdesign.components.roles.Staff;

/**
 * @author zander and prenner
 * 
 */
public class CreateRoleCommand extends Command {

    private Object parent;

    private boolean isLearner;

    private String name;

    private Color color;

    private Role newRole;

    private LearningDesignDataModel learningDesignDataModel;

    /**
	 * 
	 */
    public CreateRoleCommand(LearningDesignDataModel learningDesignDataModel) {
        super("create Role");
        this.learningDesignDataModel = learningDesignDataModel;
    }

    public boolean canExecute() {
        if (name == null) return false;
        return true;
    }

    public void execute() {
        if (isLearner) newRole = new Learner(learningDesignDataModel); else newRole = new Staff(learningDesignDataModel);
        newRole.setTitle(name);
        if (parent instanceof RoleRole) ((Role) ((RoleRole) parent).getData()).addChild(newRole); else getRootGroup().addChild(newRole);
        learningDesignDataModel.fireDataComponentAdded(newRole);
        updateColor();
    }

    private void updateColor() {
        if (color == null) return;
        RoleItem prolixRoleTmp = learningDesignDataModel.getRoleGroupMain().findMenueItem(newRole.getIdentifier());
        if (prolixRoleTmp != null && prolixRoleTmp instanceof RoleRole) ((RoleRole) prolixRoleTmp).setColor(color);
    }

    public void redo() {
        newRole.getParent().addChild(newRole);
        learningDesignDataModel.fireDataComponentAdded(newRole);
        updateColor();
    }

    public void undo() {
        newRole.getParent().removeChild(newRole);
        learningDesignDataModel.fireDataComponentRemoved(newRole);
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public boolean isLearner() {
        return isLearner;
    }

    public boolean isStaff() {
        return !isLearner;
    }

    public void setLearner(boolean isLearner) {
        this.isLearner = isLearner;
    }

    public void setStaff(boolean isStaff) {
        isLearner = !isStaff;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private RoleGroup getRootGroup() {
        if (isLearner) return learningDesignDataModel.getLearningDesign().getComponents().getRoles().getLearnerGroup();
        return learningDesignDataModel.getLearningDesign().getComponents().getRoles().getStaffGroup();
    }

    public Role getCreatedRole() {
        return this.newRole;
    }

    public RoleRole getCreatedRoleRole() {
        return (RoleRole) learningDesignDataModel.getRoleGroupMain().findMenueItem(newRole.getIdentifier());
    }
}
