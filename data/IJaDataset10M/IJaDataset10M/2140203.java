package net.sf.association4j;

import java.util.Map;
import net.sf.joafip.java.util.PTreeMap;

/**
 * Super class of all object of class that participate to association(s)<br>
 * Subclass constructors can throws:<br>
 * <ul>
 * <li>{@link DuplicateAssociationNameException}</li>
 * </ul>
 * 
 * @author luc peuvrier
 * 
 */
public abstract class AbstractAssociatedObject {

    /** all endpoint of this, accessible by association name */
    private final Map<String, AssociationEndpoint<?, ?>> endpointMap = new PTreeMap<String, AssociationEndpoint<?, ?>>();

    /**
	 * 
	 * @param associationEndpoint
	 * @throws DuplicateAssociationNameException
	 *             duplicate association name for association of object
	 */
    protected void addEndpoint(final AssociationEndpoint<?, ?> associationEndpoint) {
        final AssociationEndpoint<?, ?> previous = endpointMap.put(associationEndpoint.getAssociationName(), associationEndpoint);
        if (previous != null) {
            throw new DuplicateAssociationNameException(associationEndpoint, previous);
        }
    }

    /**
	 * 
	 * @param associationName
	 *            the association name
	 * @return endpoint of association for this, null if does not exist
	 */
    protected AssociationEndpoint<?, ?> getEndpointOfAssociation(final String associationName) {
        return endpointMap.get(associationName);
    }
}
