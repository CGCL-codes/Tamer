package com.prolix.editor.graph.templates.commands.basictemplates;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;
import com.prolix.editor.graph.templates.commands.GraphicalTemplateBasicDialog;
import com.prolix.editor.graph.templates.commands.GraphicalTemplateCommand;
import com.prolix.editor.graph.templates.commands.detailelements.GraphicalTemplateAddTextRessource;
import com.prolix.editor.graph.templates.commands.detailelements.GraphicalTemplateAssignConnection;
import com.prolix.editor.graph.templates.commands.detailelements.GraphicalTemplateAssignRoleCommand;
import com.prolix.editor.graph.templates.commands.detailelements.GraphicalTemplateCreateLearningActivity;
import com.prolix.editor.graph.templates.commands.detailelements.GraphicalTemplateCreateRole;
import com.prolix.editor.graph.templates.commands.detailelements.GraphicalTemplateCreateSupportActivity;
import com.prolix.editor.graph.templates.commands.detailelements.GraphicalTemplateGetGeneratetElement;
import com.prolix.editor.graph.templates.commands.dialogs.basictemplates.ConfigTestCommandDialogOnlyRoles;

/**
 * <<class description>>
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public class GraphicalTemplateCommandOneminutepaper extends GraphicalTemplateCommand {

    private GraphicalTemplateGetGeneratetElement r01;

    private GraphicalTemplateGetGeneratetElement r02;

    private GraphicalTemplateCreateSupportActivity sa01;

    private GraphicalTemplateCreateSupportActivity sa02;

    private GraphicalTemplateCreateLearningActivity la01;

    public GraphicalTemplateCommandOneminutepaper() {
        super("Template: One-Minute-Paper");
    }

    public GraphicalTemplateBasicDialog createConfigDialog() {
        ConfigTestCommandDialogOnlyRoles rolesDialog = new ConfigTestCommandDialogOnlyRoles(getLearningDesignDataModel(), getLabel());
        rolesDialog.addRoleName("Learner");
        rolesDialog.addRoleName("Trainer");
        return rolesDialog;
    }

    public ConfigTestCommandDialogOnlyRoles getDialog() {
        return (ConfigTestCommandDialogOnlyRoles) getConfigDialog();
    }

    protected void assignConnections() {
        addCommand(new GraphicalTemplateAssignConnection(sa01, la01));
        addCommand(new GraphicalTemplateAssignConnection(la01, sa02));
    }

    protected void assignEnvironments() {
    }

    protected void assignOperations() {
    }

    protected void assignRoles() {
        addCommand(new GraphicalTemplateAssignRoleCommand(sa01, r02));
        addCommand(new GraphicalTemplateAssignRoleCommand(sa02, r02));
        addCommand(new GraphicalTemplateAssignRoleCommand(la01, r01));
    }

    protected void assignTextResources() {
        addCommand(new GraphicalTemplateAddTextRessource(getLearningDesignDataModel(), sa01, GraphicalTemplateAddTextRessource.TYPE_ACTIVITYDESCRIPTION, "Prepare one-minute-paper reflection Activity Description", "Set aside the first or last 5 minutes of the training session for the one-minute-paper reflection. Construct one or two questions that learners can answer quickly and briefly. The purpose of the questions may be to enhance thinking about an issue, to collect feedback on a training session or to check learners' level of knowledge after a presentation.\n\nPut the questions on an overhead or on the board. Distribute index cards or ask learners to use a half-sheet of paper to write their responses. Learner names aren�t necessary, although you may choose to have them identify themselves. Ask learners to respond to the questions frankly and concisely. They may use single words, short phrases or very short sentences."));
        addCommand(new GraphicalTemplateAddTextRessource(getLearningDesignDataModel(), sa02, GraphicalTemplateAddTextRessource.TYPE_ACTIVITYDESCRIPTION, "Evaluate & act on reflection Activity Description", "Collect the sheets or index cards from learners. Tabulate the provided answers by creating categories. Make note of useful comments.\n\nAct on the feedback, for instance, by integrating some of the issues named by learners in the next presentation."));
        addCommand(new GraphicalTemplateAddTextRessource(getLearningDesignDataModel(), la01, GraphicalTemplateAddTextRessource.TYPE_ACTIVITYDESCRIPTION, "Answer questions Activity Description", "Respond to the posed questions."));
    }

    protected void createEnvironments() {
    }

    protected void createInteractions() {
    }

    protected void createNewActivities() {
        sa01 = new GraphicalTemplateCreateSupportActivity(this, getDialog().getName() + "Prepare one-minute-paper reflection", null, 220);
        sa01.addSupportetRole(r01);
        addCommand(sa01);
        sa02 = new GraphicalTemplateCreateSupportActivity(this, getDialog().getName() + "Evaluate & act on reflection", new Point(470, 0), 200);
        sa02.addSupportetRole(r01);
        addCommand(sa02);
        la01 = new GraphicalTemplateCreateLearningActivity(this, getDialog().getName() + "Answer questions", new Point(270, 0), 150);
        addCommand(la01);
    }

    protected void createNewPoints() {
    }

    protected void createNewRoles() {
        if (getDialog().getRolePos(0).isNewRole()) r01 = addCommandWithGeneratedElement(new GraphicalTemplateCreateRole(getLearningDesignDataModel(), "Learner", true, new Color(null, 165, 226, 174), null)); else r01 = getDialog().getRolePos(0).getPackedSelectedRole();
        if (getDialog().getRolePos(1).isNewRole()) r02 = addCommandWithGeneratedElement(new GraphicalTemplateCreateRole(getLearningDesignDataModel(), "Trainer", false, new Color(null, 174, 192, 236), null)); else r02 = getDialog().getRolePos(1).getPackedSelectedRole();
    }
}
