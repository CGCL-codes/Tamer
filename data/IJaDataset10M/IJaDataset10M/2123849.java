package net.sourceforge.wildlife.core.components.animals;

import net.sourceforge.wildlife.core.components.Component;
import net.sourceforge.wildlife.core.components.ComponentFactoryAdapter;
import net.sourceforge.wildlife.core.conf.admin.XMLActor;
import net.sourceforge.wildlife.core.environment.World;

/**
 *
 * @author Jean Barata
 */
public class ComponentFactory extends ComponentFactoryAdapter {

    /**
	 * 
	 */
    public ComponentFactory() {
    }

    /**
	 * @see net.sourceforge.wildlife.core.components.IComponentFactory#createNewComponent(World)
	 */
    public Component createNewComponent(World world_p) {
        return new Puceron(world_p);
    }

    /**
	 * @see net.sourceforge.wildlife.core.components.IComponentFactory#createNewComponent(ThreadGroup, World)
	 */
    public Component createNewComponent(ThreadGroup threadGroup_p, World world_p) {
        return new Puceron(threadGroup_p, world_p);
    }

    /**
	 * @see net.sourceforge.wildlife.core.components.IComponentFactory#deleteComponent(net.sourceforge.wildlife.core.components.Component)
	 */
    public void deleteComponent(Component component_p) {
    }

    /**
	 * @see net.sourceforge.wildlife.core.components.IComponentFactory#get_configuration()
	 */
    public XMLActor getConfiguration() {
        return getConfiguration(PuceronPlugin.getDefault().getBundle(), "data/puceron.admin");
    }
}
