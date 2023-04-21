package net.sourceforge.ondex.webservice2.result;

import net.sourceforge.ondex.core.ConceptName;
import net.sourceforge.ondex.webservice2.ONDEXServiceWS;

/**
 * A ConceptName has a name and a preferred flag. It belongs to a Concept.
 * 
 * @author taubertj
 * 
 */
public class WSConceptName {

    /**
	 * The name is a non-empty human readable String.
	 */
    private String name;

    /**
	 * The preferred property of a ConceptName. The default is false. (Optional)
	 */
    private Boolean isPreferred;

    public WSConceptName() {
    }

    public WSConceptName(ConceptName conceptName) {
        if (conceptName == null) {
            name = "";
            isPreferred = false;
        } else {
            name = conceptName.getName();
            isPreferred = conceptName.isPreferred();
        }
    }

    /**
	 * Returns the name.
	 * 
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Returns the isPreferred.
	 * 
	 * @return the isPreferred
	 */
    public Boolean getIsPreferred() {
        return isPreferred;
    }

    /**
	 * Sets the isPreferred.
	 * 
	 * @param isPreferred
	 *            the new isPreferred
	 */
    public void setIsPreferred(Boolean isPreferred) {
        this.isPreferred = isPreferred;
    }
}
