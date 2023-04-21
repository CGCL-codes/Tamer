package de.fraunhofer.isst.axbench.axlang.elements.mappings;

import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.ApplicationModel;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.AbstractArchitectureModel;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLException;
import de.fraunhofer.isst.axbench.axlang.utilities.ReferenceKind;
import de.fraunhofer.isst.axbench.axlang.utilities.Role;
import de.fraunhofer.isst.axbench.axlang.utilities.ValidType;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Cardinality;

public class F2AMapping extends AbstractF2ArchitectureMapping {

    /**
	 * @brief Constructor.
	 */
    public F2AMapping() {
        super();
        setValidElementTypes(ReferenceKind.REFERENCE, new ValidType(Role.APPLICATIONMODEL, new Cardinality(1, 1)));
    }

    /**
	 * @brief getter method for the referenced application model
	 * @return the referenced application model
	 */
    public ApplicationModel getApplicationModel() {
        return (ApplicationModel) getReference(Role.APPLICATIONMODEL);
    }

    /**
	 * @brief setter method for the referenced application model
	 * @param architecturemodel - referenced application model
	 * @throws AXLException 
	 */
    public void setApplicationModel(ApplicationModel architecturemodel) throws AXLException {
        addReference(architecturemodel, Role.APPLICATIONMODEL);
    }

    /**
	 * @brief getter method for the referenced architecture model
	 * @return the referenced architecture model
	 */
    public ApplicationModel getArchitectureModel() {
        return getApplicationModel();
    }

    /**
	 * @brief sets the architecture model
	 * @param newArchitectureModel the new architecture model
	 */
    public void setArchitectureModel(AbstractArchitectureModel newArchitectureModel) throws AXLException {
        if (newArchitectureModel instanceof ApplicationModel) {
            setApplicationModel((ApplicationModel) newArchitectureModel);
        }
    }
}
