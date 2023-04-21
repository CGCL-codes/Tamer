package com.prolix.editor.interaction.files;

import uk.ac.reload.straker.datamodel.DataComponent;
import uk.ac.reload.straker.datamodel.learningdesign.components.environments.Environment;
import uk.ac.reload.straker.datamodel.learningdesign.components.environments.EnvironmentRef;
import uk.ac.reload.straker.datamodel.learningdesign.components.environments.Monitor;
import uk.ac.reload.straker.datamodel.learningdesign.types.ItemContainer;
import com.prolix.editor.graph.model.activities.ModelDiagramActivity;
import com.prolix.editor.graph.model.activities.ModelSelectionPoint;

/**
 * <<class description>>
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public class FileToMonitor extends BasicFile {

    private Environment environment;

    private String role;

    /**
	 * @param activity
	 */
    public FileToMonitor(ModelDiagramActivity activity, String role) {
        super(activity);
        this.role = role;
    }

    protected ItemContainer getAddPlace() {
        environment = new Environment(getLearningDesignDataModel());
        getLearningDesignDataModel().getLearningDesign().getComponents().getEnvironments().addChild(environment);
        addEnvrionmnetRefToActivity(environment);
        environment.setTitle("Monitor Container");
        Monitor monitor = new Monitor(getLearningDesignDataModel());
        monitor.setTitle("Monitor Service");
        if (role != null) monitor.setRoleRef(role);
        environment.addChild(monitor);
        return monitor.getItemContainer();
    }

    private void addEnvrionmnetRefToActivity(Environment environment) {
        DataComponent activity = getActivity().getDataComponent();
        if (getActivity() instanceof ModelSelectionPoint) {
            activity = getLearningDesignDataModel().getLearningDesign().getComponents().getActivities().getActivityStructureGroup().getChildByIdentifer(getActivity().getIdentifier(), false);
        }
        if (activity != null) activity.addChild(new EnvironmentRef(environment));
    }

    public void clean() {
        super.clean();
        if (environment != null) {
            environment.getParent().removeChild(environment);
            getLearningDesignDataModel().fireDataComponentRemoved(environment);
        }
    }
}
