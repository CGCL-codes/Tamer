package net.sf.adatagenerator.api;

import java.util.Properties;
import com.choicemaker.shared.api.CMPair;

/**
 * A interface for cloning, modifying and marking pairs of objects.
 * Clones are used extensively during data synthesis. This interface also
 * defines a set of properties that may be attached to a template pair and
 * referenced during the synthesis process.
 * 
 * @param <R>
 *            the underlying type held by a pair
 */
public interface TemplatePair<R> extends CMPair<Template<R>>, Cloneable {

    /** Returns this instance cast to the underlying type extended by this interface */
    CMPair<R> cast();

    /** Creates a copy of this instance */
    Object clone() throws CloneNotSupportedException;

    /** A typesafe clone method */
    TemplatePair<R> cloneTemplatePair() throws CreationException;

    /** Returns a copy of this instance's properties */
    Properties getProperties();

    /**
	 * Sets a property value for this instance.
	 * 
	 * @param pn
	 *            the (non-null) property name
	 * @param pv
	 *            the (non-null) property value
	 */
    void setProperty(String pn, String pv);

    /**
	 * Removes a property from this instance.
	 * 
	 * @param pn
	 *            the (non-null) property name
	 * @return the value to which the name had been mapped, or null if the name
	 *         did not have a mapping.
	 */
    String removeProperty(String pn);

    /**
	 * Searches for the property with the specified name in the properties of
	 * this instance. The method returns <code>null</code> if the property is
	 * not found.
	 * 
	 * @param pn
	 *            the property name.
	 * @return the value in this property list with the specified key value, or
	 *         null if the property does not exist.
	 */
    String getProperty(String pn);

    /**
	 * When a pair is first constructed, its members may be marked as:
	 * <ul>
	 * <li/><code>MATCH</code> the elements identify the same underlying entity;
	 * <li/><code>DIFFER</code> the elements identify different underlying
	 * entities;
	 * <li/><code>HOLD</code> the elements are too ambiguous to be judged as
	 * either a <code>MATCH</code> or a <code>DIFFER</code>;
	 * <li/><code>NO_DECISION</code> there is no context in which to compare the
	 * elements, or the pair was synthesized without regard to whether its
	 * elements should be marked as <code>MATCH</code>, <code>DIFFER</code> or
	 * <code>HOLD</code>.
	 * </ul>
	 * 
	 * @see #ModificationStatus
	 */
    public enum GeneratedDecision {

        MATCH, HOLD, DIFFER, NO_DECISION
    }

    public GeneratedDecision getGeneratedDecision();

    /**
	 * After a pair is constructed, it may be modified; see {@link
	 * #acceptModification(Modifier<TemplatePair<R>>) acceptModification}. If
	 * it is modified, its elements should be remarked as one of the following:
	 * <ul>
	 * <li/><code>MATCH</code> the elements identify the same underlying entity;
	 * <li/><code>DIFFER</code> the elements identify different underlying
	 * entities;
	 * <li/><code>HOLD</code> the elements are too ambiguous to be judged as
	 * either a <code>MATCH</code> or a <code>DIFFER</code>;
	 * <li/><code>INDETERMINATE</code> the effect of the modification is
	 * unknown.
	 * </ul>
	 * 
	 * @see #GeneratedDecision
	 */
    enum ModificationStatus {

        MATCH, HOLD, DIFFER, INDETERMINATE
    }

    /**
	 * Returns the result of the last modification to this instance.
	 */
    ModificationStatus getModificationStatus();

    /**
	 * Sets the result of the last modification to this instance.
	 */
    void setModificationStatus(ModificationStatus currentStatus);
}
