package tec.stan.ling.core.notifier;

import org.eclipse.emf.common.notify.Notifier;

/**
 * A description of a feature change that has occurred for some notifier.
 *
 * @author Stan 张新潮
 * @date Jan 13, 2010
 */
public interface Notification {

    /**
	   * An {@link Notification#getEventType event type} indicating that 
	   * a feature of the notifier has been set.
	   * This applies for simple features.
	   * @see Notification#getEventType
	   */
    int SET = 1;

    /**
	   * An {@link Notification#getEventType event type} indicating that 
	   * a feature of the notifier has been unset.
	   * This applies for unsettable features.
	   * @see Notification#getEventType
	   */
    int UNSET = 2;

    /**
	   * An {@link Notification#getEventType event type} indicating that 
	   * a value has been inserted into a list-based feature of the notifier.
	   * @see Notification#getEventType
	   */
    int ADD = 3;

    /**
	   * An {@link Notification#getEventType event type} indicating that 
	   * a value has been removed from a list-based feature of the notifier.
	   * @see Notification#getEventType
	   */
    int REMOVE = 4;

    /**
	   * An {@link Notification#getEventType event type} indicating that 
	   * a several values have been added into a list-based feature of the notifier.
	   * @see Notification#getEventType
	   */
    int ADD_MANY = 5;

    /**
	   * An {@link Notification#getEventType event type} indicating that 
	   * a several values have been removed from a list-based feature of the notifier.
	   * @see Notification#getEventType
	   */
    int REMOVE_MANY = 6;

    /**
	   * Returns the type of change that has occurred.
	   * The valid types of events are defined by the constants in this class.
	   * @return the type of change that has occurred.
	   * @see Notifier
	   */
    int getEventType();

    /**
	   * Returns the object representing the feature of the notifier that has changed.
	   * @return the feature that has changed.
	   */
    String getFeature();

    /**
	   * Returns the object affected by the change.
	   * @return the object affected by the change.
	   */
    Object getNotifier();

    /**
	   * Returns the value of the notifier's feature before the change occurred.
	   * For a list-based feature, this represents a value, or a list of values, removed from the list.
	   * For a move, this represents the old position of the moved value.
	   * @return the old value of the notifier's feature.
	   */
    Object getOldValue();

    /**
	   * Returns the value of the notifier's feature after the change occurred.
	   * For a list-based feature, this represents a value, or a list of values, added to the list.
	   * @return the new value of the notifier's feature.
	   */
    Object getNewValue();
}
