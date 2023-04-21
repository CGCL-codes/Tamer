package org.qtitools.qti.node.result;

import java.util.List;
import org.qtitools.qti.group.result.IdentificationGroup;
import org.qtitools.qti.group.result.SessionIdentifierGroup;
import org.qtitools.qti.node.AbstractObject;

/**
 * Context of assessmentResult.
 * 
 * @author Jiri Kajaba
 */
public class Context extends AbstractObject {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static final String CLASS_TAG = "context";

    /**
	 * Constructs object.
	 *
	 * @param parent parent of constructed object
	 */
    public Context(AssessmentResult parent) {
        super(parent);
        getNodeGroups().add(new SessionIdentifierGroup(this));
        getNodeGroups().add(new IdentificationGroup(this));
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }

    /**
	 * Gets sessionIdentifier children.
	 *
	 * @return sessionIdentifier children
	 */
    public List<SessionIdentifier> getSessionIdentifiers() {
        return getNodeGroups().getSessionIdentifierGroup().getSessionIdentifiers();
    }

    /**
	 * Gets identification child.
	 *
	 * @return identification child
	 * @see #setIdentification
	 */
    public Identification getIdentification() {
        return getNodeGroups().getIdentificationGroup().getIdentification();
    }

    /**
	 * Sets new identification child.
	 *
	 * @param identification new identification child
	 * @see #getIdentification
	 */
    public void setIdentification(Identification identification) {
        getNodeGroups().getIdentificationGroup().setIdentification(identification);
    }
}
