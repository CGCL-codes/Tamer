package prisms.arch.event;

/**
 * Allows inspection of a property's value in an application or a session from the manager
 * application
 */
public interface DataInspector {

    /** A change listener that can be notified if a property's value changes */
    public static interface ChangeListener {

        /**
		 * To be called if the entire property's value changes
		 * 
		 * @param value The new value for the property
		 */
        void propertyChanged(Object value);

        /**
		 * To be called if only a small part of a property's value has changed
		 * 
		 * @param item The item within the property's value that was changed
		 * @param recursive Whether the target item's children have been affected by the change
		 */
        void valueChanged(Object item, boolean recursive);
    }

    /** Represents a manager client's view of the data provided by this inspector */
    public static interface InspectSession {

        /** @return The user using the manager application */
        prisms.arch.ds.User getUser();

        /** @return The UI widget to use to communicate with the user */
        prisms.ui.UI getUI();

        /** @return The status plugin to use to communicate with the user */
        prisms.ui.StatusPlugin getStatus();

        /** @return The client user's preferences */
        prisms.util.preferences.Preferences getPreferences();
    }

    /** Represents a piece of data represented to the manager users */
    public static interface NodeController {

        /** @return The application that is being inspected */
        prisms.arch.PrismsApplication getApp();

        /**
		 * @return The session that is being inspected--will be null if the global (application)
		 *         value is being inspected
		 */
        prisms.arch.PrismsSession getSession();

        /** @return The value represented by this node. This will be null for property-wide methods. */
        Object getValue();

        /**
		 * Causes this node's child data to be displayed to the given session
		 * 
		 * @param session The session to display the child data to
		 * @param recursive Whether to display all this node's descendants or just the immediate
		 *        children
		 */
        void load(InspectSession session, boolean recursive);

        /**
		 * Causes this node's child data to removed from the view of the given session
		 * 
		 * @param session The session to remove the child data from
		 */
        void unload(InspectSession session);
    }

    /** Contains metadata that may help determine the behavior of a node */
    public static class ItemMetadata {

        private boolean isVolatile;

        /**
		 * @return Whether the node's representation changes over time without any events to herald
		 *         the change.
		 */
        public boolean isVolatile() {
            return isVolatile;
        }

        /**
		 * @param vol Whether this node's representation in the client should be refreshed
		 *        periodically.
		 */
        public void setVolatile(boolean vol) {
            isVolatile = vol;
        }
    }

    /**
	 * Configures the inspector
	 * 
	 * @param property The property that this inspector is for
	 * @param pclConfig The configuration for the property change listener that this inspector is
	 *        configured within
	 * @param inspectorConfig The inspector's configuration in the application configuration
	 */
    void configure(PrismsProperty<?> property, prisms.arch.PrismsConfig pclConfig, prisms.arch.PrismsConfig inspectorConfig);

    /**
	 * @param session The session to display the icon to
	 * @return The icon to display for the property as a whole
	 * @param node The controller of the node to get the information for
	 */
    String getPropertyIcon(InspectSession session, NodeController node);

    /**
	 * @param session The session to display the description to
	 * @return The description to display for the property as a whole
	 * @param node The controller of the node to get the information for
	 */
    String getPropertyDescrip(InspectSession session, NodeController node);

    /**
	 * @param session The session to display the color to
	 * @return The background color to display for the property as a whole
	 * @param node The controller of the node to get the information for
	 */
    java.awt.Color getPropertyBackground(InspectSession session, NodeController node);

    /**
	 * @param session The session to display the color to
	 * @return The text color to display for the property as a whole
	 * @param node The controller of the node to get the information for
	 */
    java.awt.Color getPropertyForeground(InspectSession session, NodeController node);

    /**
	 * Gets actions that may be performed on a property as a whole
	 * 
	 * @param session The session to display the actions to
	 * @return The actions to be available on the item from the manager
	 * @param node The controller of the node to get the information for
	 */
    prisms.ui.list.NodeAction[] getPropertyActions(InspectSession session, NodeController node);

    /**
	 * Performs an action on a property as a whole
	 * 
	 * @param session The session performing the action
	 * @param node The controller of the node to get the information for
	 * @param action The action to perform on the item
	 */
    void performPropertyAction(InspectSession session, NodeController node, String action);

    /**
	 * @param session The session to display the text to
	 * @param node The controller of the node to get the information for
	 * @return The text to use to represent the item in the property value display
	 */
    String getText(InspectSession session, NodeController node);

    /**
	 * @param session The session to display the description to
	 * @param node The controller of the node to get the information for
	 * @return The full description to use to represent the item in the property value display
	 */
    String getDescrip(InspectSession session, NodeController node);

    /**
	 * @param session The session to display the icon to
	 * @param node The controller of the node to get the information for
	 * @return The icon to use to represent the item in the property value display
	 */
    String getIcon(InspectSession session, NodeController node);

    /**
	 * @param session The session to display the color to
	 * @param node The controller of the node to get the information for
	 * @return The background color to use to represent the item in the property value display
	 */
    java.awt.Color getBackground(InspectSession session, NodeController node);

    /**
	 * @param session The session to display the color to
	 * @param node The controller of the node to get the information for
	 * @return The text color to use to represent the item in the property value display
	 */
    java.awt.Color getForeground(InspectSession session, NodeController node);

    /**
	 * Gets actions that may be performed on a property value or a part thereof
	 * 
	 * @param session The session to display the icon to
	 * @param node The controller of the node to get the information for
	 * @return The actions to be available on the item from the manager
	 */
    prisms.ui.list.NodeAction[] getActions(InspectSession session, NodeController node);

    /**
	 * Performs an action on a property value or a part thereof
	 * 
	 * @param session The session performing the action to
	 * @param node The controller of the node to get the information for
	 * @param action The action to perform on the item
	 */
    void performAction(InspectSession session, NodeController node, String action);

    /**
	 * @param session The session to display the action label to
	 * @param node The controller of the node to get the information for
	 * @param allDescendants Whether the returned label will be for loading all the sub-data instead
	 *        of just the immediate children
	 * @return The action label to display allowing a user can view the child data of a value
	 */
    String canDescend(InspectSession session, NodeController node, boolean allDescendants);

    /**
	 * @param session The session to display the action label to
	 * @param node The controller of the node to get the information for
	 * @return The action label to display allowing a user to hide the child data of a value
	 */
    String getHideLabel(InspectSession session, NodeController node);

    /**
	 * @param node The controller of the node to get the information for
	 * @return The metadata describing this node, or null for default
	 */
    ItemMetadata getMetadata(NodeController node);

    /**
	 * Gets items that may be represented under a property value or part thereof as children
	 * 
	 * @param node The controller of the node to get the information for
	 * @return The child items to represent under the given item
	 */
    Object[] getChildren(NodeController node);

    /**
	 * Registers a listener that will be notified whenever the global value of a property changes
	 * 
	 * @param app The application to listen to changes in
	 * @param cl The change listener to register
	 */
    void registerGlobalListener(prisms.arch.PrismsApplication app, ChangeListener cl);

    /**
	 * Un-registers a listener to no longer be notified when the global value of a property changes
	 * 
	 * @param cl The change listener to un-register
	 */
    void deregisterGlobalListener(ChangeListener cl);

    /**
	 * Registers a listener that will be notified whenever the session value of a property changes
	 * 
	 * @param session The session to listen to changes in
	 * @param cl The change listener to register
	 */
    void registerSessionListener(prisms.arch.PrismsSession session, ChangeListener cl);

    /**
	 * Un-registers a listener to no longer be notified when the session value of a property changes
	 * 
	 * @param cl The change listener to un-register
	 */
    void deregisterSessionListener(ChangeListener cl);
}
