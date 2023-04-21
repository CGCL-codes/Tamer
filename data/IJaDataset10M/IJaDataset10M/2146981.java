package edu.toronto.cs.openome.testing;

import java.util.Collection;
import org.eclipse.emf.common.command.Command;
import edu.toronto.cs.openome_model.Container;
import edu.toronto.cs.openome_model.impl.ActorImpl;
import edu.toronto.cs.openome_model.impl.ContainerImpl;
import edu.toronto.cs.openome_model.impl.GoalImpl;
import edu.toronto.cs.openome_model.impl.ModelImpl;
import edu.toronto.cs.openome_model.impl.ResourceImpl;
import edu.toronto.cs.openome_model.impl.openome_modelFactoryImpl;

public class ResourceImplCreateCommand implements Command {

    private ContainerImpl container;

    private ModelImpl model;

    private String intentionName = "";

    private static openome_modelFactoryImpl factory = new openome_modelFactoryImpl();

    /**
	 * Command to add an actor inside a model
	 * @param model
	 */
    public ResourceImplCreateCommand(ModelImpl m) {
        model = m;
    }

    /**
	 * Command to add an actor inside a container
	 * @param c - the container
	 */
    public ResourceImplCreateCommand(ContainerImpl c) {
        container = c;
    }

    /**
	 * Command to add an actor inside a model
	 * @param model
	 */
    public ResourceImplCreateCommand(ModelImpl m, String name) {
        model = m;
        intentionName = name;
    }

    /**
	 * Command to add an actor inside a container
	 * @param model
	 */
    public ResourceImplCreateCommand(ContainerImpl c, String name) {
        container = c;
        intentionName = name;
    }

    @Override
    public boolean canExecute() {
        return true;
    }

    @Override
    public boolean canUndo() {
        return false;
    }

    @Override
    public Command chain(Command command) {
        return null;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void execute() {
        ResourceImpl resource = (ResourceImpl) factory.createResource();
        resource.setName(intentionName);
        if (container != null) {
            resource.setContainer(container);
            container.getIntentions().add(resource);
        } else if (model != null) {
            resource.setModel(model);
            model.getIntentions().add(resource);
        }
    }

    @Override
    public Collection<?> getAffectedObjects() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getLabel() {
        return "Create Actor model";
    }

    @Override
    public Collection<?> getResult() {
        return null;
    }

    @Override
    public void redo() {
    }

    @Override
    public void undo() {
    }
}
