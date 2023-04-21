package de.erdesignerng.model;

import java.util.Vector;

/**
 * A list of domains.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-11-12 19:11:45 $
 */
public class DomainList extends Vector<Domain> implements ModelList<Domain> {

    /**
	 * Find a domain by system id.
	 * 
	 * @param aSystemId
	 *			the system id
	 * @return the domain or null if domain does not exist
	 */
    @Override
    public Domain findBySystemId(String aSystemId) {
        for (Domain theDomain : this) {
            if (aSystemId.equals(theDomain.getSystemId())) {
                return theDomain;
            }
        }
        return null;
    }

    /**
	 * Find a domain by a given name.
	 * 
	 * @param aName
	 *			the name
	 * @return the found element
	 */
    public Domain findByName(String aName) {
        for (Domain theDomain : this) {
            if (aName.equals(theDomain.getName())) {
                return theDomain;
            }
        }
        return null;
    }
}
