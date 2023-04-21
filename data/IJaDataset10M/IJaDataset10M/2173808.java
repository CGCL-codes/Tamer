package org.eclipse.core.commands.common;

/**
 * <p>
 * An event fired from a <code>NamedHandleObject</code>. This provides
 * notification of changes to the defined state, the name and the description.
 * </p>
 */
public abstract class AbstractNamedHandleEvent extends AbstractBitSetEvent {

    /**
	 * The bit used to represent whether the category has changed its defined
	 * state.
	 */
    protected static final int CHANGED_DEFINED = 1;

    /**
	 * The bit used to represent whether the category has changed its
	 * description.
	 */
    protected static final int CHANGED_DESCRIPTION = 1 << 1;

    /**
	 * The bit used to represent whether the category has changed its name.
	 */
    protected static final int CHANGED_NAME = 1 << 2;

    /**
	 * The last used bit so that subclasses can add more properties.
	 */
    protected static final int LAST_USED_BIT = CHANGED_NAME;

    /**
	 * Constructs a new instance of <code>AbstractHandleObjectEvent</code>.
	 * 
	 * @param definedChanged
	 *            <code>true</code>, iff the defined property changed.
	 * @param descriptionChanged
	 *            <code>true</code>, iff the description property changed.
	 * @param nameChanged
	 *            <code>true</code>, iff the name property changed.
	 */
    protected AbstractNamedHandleEvent(final boolean definedChanged, final boolean descriptionChanged, final boolean nameChanged) {
        if (definedChanged) {
            changedValues |= CHANGED_DEFINED;
        }
        if (descriptionChanged) {
            changedValues |= CHANGED_DESCRIPTION;
        }
        if (nameChanged) {
            changedValues |= CHANGED_NAME;
        }
    }

    /**
	 * Returns whether or not the defined property changed.
	 * 
	 * @return <code>true</code>, iff the defined property changed.
	 */
    public final boolean isDefinedChanged() {
        return ((changedValues & CHANGED_DEFINED) != 0);
    }

    /**
	 * Returns whether or not the description property changed.
	 * 
	 * @return <code>true</code>, iff the description property changed.
	 */
    public final boolean isDescriptionChanged() {
        return ((changedValues & CHANGED_DESCRIPTION) != 0);
    }

    /**
	 * Returns whether or not the name property changed.
	 * 
	 * @return <code>true</code>, iff the name property changed.
	 */
    public final boolean isNameChanged() {
        return ((changedValues & CHANGED_NAME) != 0);
    }
}
