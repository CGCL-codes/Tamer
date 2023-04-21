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
import com.prolix.editor.graph.templates.commands.detailelements.GraphicalTemplateCreateSynPoint;
import com.prolix.editor.graph.templates.commands.detailelements.GraphicalTemplateGetGeneratetElement;
import com.prolix.editor.graph.templates.commands.dialogs.basictemplates.ConfigTestCommandDialogOnlyRoles;

/**
 * <<class description>>
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public class GraphicalTemplateCommandBrainstorming extends GraphicalTemplateCommand {

    private GraphicalTemplateGetGeneratetElement r01;

    private GraphicalTemplateGetGeneratetElement r02;

    private GraphicalTemplateCreateLearningActivity la01;

    private GraphicalTemplateCreateLearningActivity la02;

    private GraphicalTemplateCreateLearningActivity la03;

    private GraphicalTemplateCreateSupportActivity sa01;

    private GraphicalTemplateCreateSupportActivity sa02;

    private GraphicalTemplateCreateSupportActivity sa03;

    private GraphicalTemplateCreateSynPoint sync01;

    public GraphicalTemplateCommandBrainstorming() {
        super("Template: Brainstorming");
    }

    public GraphicalTemplateBasicDialog createConfigDialog() {
        ConfigTestCommandDialogOnlyRoles rolesDialog = new ConfigTestCommandDialogOnlyRoles(getLearningDesignDataModel(), getLabel());
        rolesDialog.addRoleName("Brainstorm Participant");
        rolesDialog.addRoleName("Brainstorm Facilitator");
        return rolesDialog;
    }

    public ConfigTestCommandDialogOnlyRoles getDialog() {
        return (ConfigTestCommandDialogOnlyRoles) getConfigDialog();
    }

    protected void assignConnections() {
        addCommand(new GraphicalTemplateAssignConnection(la01, sa01));
        addCommand(new GraphicalTemplateAssignConnection(sa01, la02));
        addCommand(new GraphicalTemplateAssignConnection(sa01, sa02));
        addCommand(new GraphicalTemplateAssignConnection(la02, sync01));
        addCommand(new GraphicalTemplateAssignConnection(sa02, sync01));
        addCommand(new GraphicalTemplateAssignConnection(sync01, la03));
        addCommand(new GraphicalTemplateAssignConnection(sync01, sa03));
    }

    protected void assignEnvironments() {
    }

    protected void assignOperations() {
    }

    protected void assignRoles() {
        addCommand(new GraphicalTemplateAssignRoleCommand(la01, r01));
        addCommand(new GraphicalTemplateAssignRoleCommand(la02, r01));
        addCommand(new GraphicalTemplateAssignRoleCommand(la03, r01));
        addCommand(new GraphicalTemplateAssignRoleCommand(sa01, r02));
        addCommand(new GraphicalTemplateAssignRoleCommand(sa02, r02));
        addCommand(new GraphicalTemplateAssignRoleCommand(sa03, r02));
    }

    protected void assignTextResources() {
        addCommand(new GraphicalTemplateAddTextRessource(getLearningDesignDataModel(), la01, GraphicalTemplateAddTextRessource.TYPE_ACTIVITYDESCRIPTION, "Define problem Activity Description", "Define your problem or issue as a creative challenge. Creative challenges typically start with: \"In what ways might we...?\" or \"How could we...?\" Your creative challenge should be concise, to the point and exclude any information other than the challenge itself. For example: \"In what ways might we improve product X?\" or \"How could we encourage more local people to join our club?\""));
        addCommand(new GraphicalTemplateAddTextRessource(getLearningDesignDataModel(), la02, GraphicalTemplateAddTextRessource.TYPE_ACTIVITYDESCRIPTION, "Activity Description for Contribute solutions", "Contribute solutions to the problem by loudly saying them to the entire group."));
        addCommand(new GraphicalTemplateAddTextRessource(getLearningDesignDataModel(), la03, GraphicalTemplateAddTextRessource.TYPE_ACTIVITYDESCRIPTION, "Generate criteria & score Activity Description", "Generate about five criteria for judging which ideas best solve the problem. Criteria should start with the word \"should\", for example, \"it should be cost effective\", \"it should be legal\", \"it should be possible to finish before July 15\", etc.\n\nThen, assign each proposed solution a score of 0 to 5 points depending on how well it meets each criterion that you generated.\n\nAdd up the scores. The idea with the highest score may best solve the problem."));
        addCommand(new GraphicalTemplateAddTextRessource(getLearningDesignDataModel(), sa01, GraphicalTemplateAddTextRessource.TYPE_ACTIVITYDESCRIPTION, "Activity Description for Set up brainstorm", "Give a time limit for the brainstorming. The recommendation is around 25 minutes, but experience will show how much time is required. Larger groups may need more time to get everyone's ideas out.\n\nAnnounce the time limit. Then, start the brainstorming session."));
        addCommand(new GraphicalTemplateAddTextRessource(getLearningDesignDataModel(), sa02, GraphicalTemplateAddTextRessource.TYPE_ACTIVITYDESCRIPTION, "Moderate & record Activity Description", "Visibly write down ideas being contributed by the brainstorm participants. No matter how daft, how impossible or how silly an idea is, it must be written down.\n\nDiscourage the criticizing of ideas � there must be none of it. Laughing is to be encouraged, criticism is not.\n\nEnd discussion when the time limit is reached."));
        addCommand(new GraphicalTemplateAddTextRessource(getLearningDesignDataModel(), sa03, GraphicalTemplateAddTextRessource.TYPE_ACTIVITYDESCRIPTION, "Record criteria & scores Activity Description", "Record the criteria generated by the brainstorm participants.\n\nThen, record the scores being assigned.\n\nAdd up the scores. The idea with the highest score may best solve the problem."));
    }

    protected void createEnvironments() {
    }

    protected void createInteractions() {
    }

    protected void createNewActivities() {
        la01 = new GraphicalTemplateCreateLearningActivity(this, getDialog().getName() + "Define problem", new Point(0, 75), 0);
        addCommand(la01);
        sa01 = new GraphicalTemplateCreateSupportActivity(this, getDialog().getName() + "Set up brainstorm", new Point(150, 75), 0);
        sa01.addSupportetRole(r01);
        addCommand(sa01);
        la02 = new GraphicalTemplateCreateLearningActivity(this, getDialog().getName() + "Contribute solutions", new Point(300, 0), 150);
        addCommand(la02);
        la03 = new GraphicalTemplateCreateLearningActivity(this, getDialog().getName() + "Generate criteria & scores", new Point(500, 0), 170);
        addCommand(la03);
        sa02 = new GraphicalTemplateCreateSupportActivity(this, getDialog().getName() + "Moderate & record ideas", new Point(300, 150), 160);
        sa02.addSupportetRole(r01);
        addCommand(sa02);
        sa03 = new GraphicalTemplateCreateSupportActivity(this, getDialog().getName() + "Record criteria & scores", new Point(500, 150), 160);
        sa03.addSupportetRole(r01);
        addCommand(sa03);
    }

    protected void createNewPoints() {
        sync01 = new GraphicalTemplateCreateSynPoint(this, new Point(450, 80));
        addCommand(sync01);
    }

    protected void createNewRoles() {
        if (getDialog().getRolePos(0).isNewRole()) r01 = addCommandWithGeneratedElement(new GraphicalTemplateCreateRole(getLearningDesignDataModel(), "Brainstorm Participant", true, new Color(null, 220, 192, 175), null)); else r01 = getDialog().getRolePos(0).getPackedSelectedRole();
        if (getDialog().getRolePos(1).isNewRole()) r02 = addCommandWithGeneratedElement(new GraphicalTemplateCreateRole(getLearningDesignDataModel(), "Brainstorm Facilitator", false, new Color(null, 174, 221, 236), null)); else r02 = getDialog().getRolePos(1).getPackedSelectedRole();
    }
}
