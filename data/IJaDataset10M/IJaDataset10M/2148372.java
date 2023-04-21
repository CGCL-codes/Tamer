package de.fraunhofer.isst.axbench.eastadlinterface.subcomponents.converter;

import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Component;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.SubComponent;
import de.fraunhofer.isst.axbench.eastadlinterface.Env;
import de.fraunhofer.isst.axbench.eastadlinterface.util.ShortNameConverter;
import de.fraunhofer.isst.eastadl.functionmodeling.DesignFunctionPrototype;
import de.fraunhofer.isst.eastadl.functionmodeling.DesignFunctionType;

public class SubComponentConverter {

    private Env env = null;

    public SubComponentConverter(Env e) {
        env = e;
    }

    /**
	 * copy the "identifier" property
	 * from an instance of type SubComponent
	 * to the related instance of type DesignFunctionPrototype
	 * 
	 * @param axlSubComponent the instance of type SubComponent
	 */
    public void patchIdentifier(SubComponent axlSubComponent) {
        DesignFunctionPrototype eaDesignFunctionPrototype = env.getSubComponentRelation().getDesignFunctionPrototype(axlSubComponent);
        String identifier = axlSubComponent.getIdentifier();
        eaDesignFunctionPrototype.setName(identifier);
        eaDesignFunctionPrototype.setShortName(ShortNameConverter.get(identifier));
    }

    /**
	 * copy the "type" property
	 * from an instance of type SubComponent
	 * to the related instance of type DesignFunctionPrototype
	 * 
	 * @param axlSubComponent the instance of type SubComponent
	 */
    public void patchComponent(SubComponent axlSubComponent) {
        DesignFunctionPrototype eaPrototype = env.getSubComponentRelation().getDesignFunctionPrototype(axlSubComponent);
        Component axlComponent = axlSubComponent.getComponentType();
        DesignFunctionType eaFunction = env.getComponentRelation().getDesignFunctionType(axlComponent);
        eaPrototype.setType(eaFunction);
    }

    /**
	 * copy the "parent" information
	 * from an instance of type SubComponent
	 * to the related instance of type DesignFunctionPrototype
	 * @param axlSubComponent the instance of type SubComponent
	 */
    public void patchParent(SubComponent axlSubComponent) {
        Component axlComponent = (Component) axlSubComponent.getParent();
        DesignFunctionType eaFunction = env.getComponentRelation().getDesignFunctionType(axlComponent);
        DesignFunctionPrototype eaPrototype = env.getSubComponentRelation().getDesignFunctionPrototype(axlSubComponent);
        if (!eaFunction.getPart().contains(eaPrototype)) {
            eaFunction.getPart().add(eaPrototype);
        }
    }

    /**
	 * copy all properties
	 * from an instance of type SubComponent
	 * to the related instance of type DesignFunctionPrototype
	 * 
	 * @param axlSubComponent the instance of type SubComponent
	 */
    public void patch(SubComponent axlSubComponent) {
        patchParent(axlSubComponent);
        patchIdentifier(axlSubComponent);
        patchComponent(axlSubComponent);
    }

    public void remove(Component axlComponent, SubComponent axlSubComponent) {
        DesignFunctionType eaFunction = env.getComponentRelation().getDesignFunctionType(axlComponent);
        DesignFunctionPrototype eaPrototype = env.getSubComponentRelation().getDesignFunctionPrototype(axlSubComponent);
        env.getSubComponentRelation().remove(axlSubComponent);
        eaFunction.getPart().remove(eaPrototype);
    }
}
