package de.fraunhofer.isst.axbench.resolver;

import java.text.MessageFormat;
import java.util.List;
import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.axlang.syntaxtree.NodeToken;
import de.fraunhofer.isst.axbench.axlang.utilities.MetaInformation;
import de.fraunhofer.isst.axbench.axlang.utilities.Role;

/**
 * @brief abstract class for all references; defines the possible fields
 * 
 * A Reference is an object that holds the information that is necessary
 * to find an aXLang-model element that is referred to by another aXLang-model element
 * by its identifier.
 * 
 * Information that is always needed to find and attach the referenced element:
 * - the referencing element
 * - the identifier of the referenced element, given via a node token (or an idPath, ie a list of identifiers)
 * - the role of the reference
 * - the name of the file that contains the referencing element (typically theAXLFile.getTheFilename())
 * 
 * In the IReferenceResolver-classes different search strategies are implemented. 
 * In general, to find an element by its identifier (name) one has to find its 
 * parents, since parents know their children by name 
 * (method getChild(String identifier)).
 * 
 * The different search strategies require different further information that must 
 * therefore be held in a Reference that supports some strategy:
 * - the idPath (list of identifiers) that denotes the referenced element 
 * 		(this replaces the node token that holds the single identifier)
 * - the "generation", i.e. the level in the tree where the referenced element resides
 * - one or more search roles: these indicate references that have to be followed in order to find the referenced element
 * - the axlType: the type of the parent (or grandparent) of the referenced element 
 * 		(this is needed if the generation w.r.t. the referencing element is not unique)
 * 
 * @todo getter-methods for elements
 * 
 * @author mgrosse
 * @author ekleinod
 * @version 0.9.0
 * @since 0.8.0
 */
public abstract class AbstractReference {

    public IAXLangElement referencingElement = null;

    public NodeToken nodeToken = null;

    public List<String> idPath = null;

    public Role role = null;

    public Generation generation = null;

    public Integer upSteps1 = null;

    public Integer upSteps2 = null;

    public Role childRole = null;

    public Role searchRole1 = null;

    public Role searchRole2 = null;

    public Class<?> axlType = null;

    public String firstChildsName = null;

    public String fileName = null;

    private MetaInformation theMetaInformation = null;

    /**
 	 * @brief Constructor setting available elements.
 	 * @param newReferencingElement new referencing element
 	 * @param newRole role of the reference within the referencing element
 	 * @param newToken identifying token
 	 * @param newMetaInformation meta information
 	 */
    public AbstractReference(IAXLangElement newReferencingElement, Role newRole, NodeToken newToken, String newFileName, MetaInformation newMetaInformation) {
        referencingElement = newReferencingElement;
        role = newRole;
        nodeToken = newToken;
        fileName = newFileName;
        theMetaInformation = newMetaInformation;
    }

    /**
 	 * @brief Returns the identifying string for the missing reference.
 	 * @return referencing string
 	 */
    public String getRefString() {
        return nodeToken.tokenImage;
    }

    /**
 	 * @brief Returns the formatted error message.
 	 * @return formatted error message
 	 */
    public String getFormattedErrorMessage() {
        if (role == null) {
            return MessageFormat.format("''{0}'' does not exist.", getRefString());
        }
        return MessageFormat.format("{0} ''{1}'' does not exist.", role.toString(), getRefString());
    }

    public MetaInformation getMetaInformation() {
        return theMetaInformation;
    }

    /**
	 * @brief Returns referencing element.
	 * @return referencing element
	 */
    public IAXLangElement getReferencingElement() {
        return referencingElement;
    }

    /**
	 * @brief Returns role of the global instance within the referencing element.
	 * @return role of the global instance within the referencing element
	 */
    public Role getRole() {
        return role;
    }
}
