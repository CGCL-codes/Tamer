package org.jbfilter.core;

/**
 * The components of a {@link Filter}.
 * 
 * @author Marcus Adrian
 * @param <E> the type of the objects the filter component is conceived for
 */
public interface FilterComponent<E> {

    /**
	 * Enables or disables this object.
	 * @param enabled
	 * @see #isActive()
	 */
    void setEnabled(boolean enabled);

    /**
	 * Only if this object is {@code enabled} it might be {@code active}.
	 * Filtering uses {@link #isActive()} to decide to consider a component for filtering.
	 * @see #isActive()
	 * @return
	 */
    boolean isEnabled();

    /**
	 * Only if the component is {@code active} it will take effect on filtering.
	 * An component is {@code active} if:
	 * <ul>
	 * <li>{@code enabled} is {@code true} and {@code enableStrategy} is {@code null}.</li>
	 * <br/>
	 * OR
	 * <li>{@code enabled} is {@code true} and the (not {@code null}) {@code enableStrategy} decides that the component is enabled.</li>
	 * </ul>
	 * Otherwise the component is NOT {@code active}.
	 * <p/>
	 * Formally that means:
	 * <code>
	 * return enabled && (enableStrategy == null || enableStrategy.decideEnabled());
	 * </code>
	 * <br/>
	 * If the component is a {@link FilterComponentContainer} there is an additional condition to be active:
	 * At least one of its containing components must be active, otherwise the container gets inactive, too.
	 * @return the enabled flag
	 * @see #setEnabled(boolean)
	 */
    public boolean isActive();

    /**
	 * Cleaning this object, which means setting the filter component's properties to their 'blank' state.
	 * What is meant by 'blank' is up to you and your special needs for the component.
	 * Mostly this will mean resetting the properties to {@code null} or to default values.
	 */
    void clean();

    /**
	 * Possibility to customize the {@link #clean()} method.
	 * @param cleaningStrategy
	 */
    void setCleaningStrategy(CleaningStrategy cleaningStrategy);

    /**
	 * The identifier for this {@link FilterComponent}.
	 * @return the id
	 */
    String getId();

    /**
	 * @see #getId()
	 */
    void setId(String id);

    /**
	 * Says if the filter logic is currently inverted.
	 * @return {@code true} if inverted otherwise {@code false}
	 */
    boolean isInverted();

    /**
	 * Inverts the filter logic if set to {@code true}.
	 * @param inverted
	 */
    void setInverted(boolean inverted);

    /**
	 * Allows customization and dynamic behavior of the decision if the component is enabled.
	 * @param enableStrategy
	 */
    void setEnableStrategy(EnableStrategy enableStrategy);

    /**
	 * @see #setCleaningStrategy(CleaningStrategy)
	 */
    CleaningStrategy getCleaningStrategy();

    /**
	 * @see #setEnableStrategy(EnableStrategy)
	 */
    EnableStrategy getEnableStrategy();
}
