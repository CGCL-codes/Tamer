package org.personalsmartspace.psm.groupmgmt.api.pss3p;

import org.osgi.service.component.ComponentFactory;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;

/**
 * This interface is implemented by classes that will evaluate, based on specific
 * conditions, wether a given PssId belongs or not to a PssGroup. In other words,
 * an instance of a GroupMembershipEvaluator contains the conditions that have to
 * be satisfied by a PssGroup member in order to belong to the group.
 * 
 * A set of evaluators is associated to a group.
 * @author Guido Spadotto
 * @version 1.0 
 */
public interface IPssGroupMembershipEvaluator {

    /**
	 * Triggers the membership evaluation. Returns true if this Pss satisfies the
	 * criteria of this evaluator, false otherwise.
	 * @param targetDPI the DPI that has to be used for accessing the "relevant" ContextDb.
	 *                  Might be null and ignored by the implementing MER.
	 */
    public boolean evaluate(IDigitalPersonalIdentifier targetDPI);

    /**
	 * A human readable description for the Mer
	 */
    public String getMerDescription();

    /**
	 * A Mer is in the valid state if it can be evaluated. A Mer might be in the
	 * invalid state if it is composed of atomic Mers, and one of those Mers is
	 * removed from the container.
	 */
    public boolean isValid();

    /**
	 * A human readable description for the Mer
	 * 
	 * @param newVal
	 */
    public void setMerDescription(String newVal);

    /**
	 * The identifier of the Membership Evaluator Rule
	 */
    public String getMerId();

    /**
	 * The identifier of the Membership Evaluator Rule
	 * 
	 * @param newVal
	 */
    public void setMerId(String newVal);

    /**
         * Returns the OSGi factory for this MER, or null if not available
         */
    public ComponentFactory getMerFactory();

    /**
         * Returns the Evaluator Factory Id.
         * It identifies the Evaluator Factory service
         * that creates instances of any given type of MER. 
         */
    public String getFactoryId();
}
