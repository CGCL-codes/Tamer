package com.sptci.rwt.webui;

import nextapp.echo2.app.Component;
import nextapp.echo2.app.event.ActionEvent;
import com.sptci.echo2.Configuration;
import com.sptci.echo2.Confirmation;
import com.sptci.echo2.Executor;
import com.sptci.echo2.Listener;
import com.sptci.rwt.Connections;
import com.sptci.rwt.DatabaseType;

/**
 * The component that is used to delete saved connections.
 *
 * <p>&copy; Copyright 2007 Sans Pareil Technologies, Inc.</p>
 * @author Rakesh Vidyadharan 2007-10-14
 * @version $Id: DeleteSavedConnectionListener.java 20 2007-11-10 00:40:51Z rakesh.vidyadharan $
 */
public class DeleteSavedConnectionListener extends Listener<MainController> {

    /** The database type under which the connection is saved. */
    private final String databaseType;

    /** The name of the saved connection. */
    private final String name;

    /**
   * Create a new instance of the listener using the specified controller.
   *
   * @param databaseType The name of the database type under which the
   *   saved connection is organised.
   * @param name The name of the saved connection.
   * @param controller The controller to use to interact with the
   *   application
   */
    public DeleteSavedConnectionListener(final String databaseType, final String name, final MainController controller) {
        super(controller);
        this.databaseType = databaseType;
        this.name = name;
    }

    /**
   * The action listener implementation.  Launches a {@link
   * com.sptci.echo2.Confirmation} dialogue prompting user to confirm
   * the delete action.
   *
   * @param event The action event that was triggered.
   */
    public void actionPerformed(final ActionEvent event) {
        Executor<DeleteSavedConnectionListener> executor = new Executor<DeleteSavedConnectionListener>(this, "delete");
        executor.addParameter(Component.class, (Component) event.getSource());
        String message = Configuration.getString(this, "delete.message");
        message = message.replaceAll("\\$name\\$", name);
        Confirmation confirmation = new Confirmation(Configuration.getString(this, "delete.title"), message, executor);
        controller.addPane(confirmation);
    }

    /**
   * Delete the saved connection and update the {@link
   * ManageSavedConnectionsView} view component.
   *
   * @see com.sptci.rwt.Connections#delete( String, String )
   * @see com.sptci.rwt.Connections#delete( String )
   * @see MainController#resetMenu
   * @param component The component that is to be removed from the view.
   */
    protected void delete(final Component component) {
        Connections connections = controller.getConnections();
        connections.delete(databaseType, name);
        Component parent = component.getParent();
        int index = parent.indexOf(component);
        parent.remove(--index);
        parent.remove(index);
        DatabaseType type = connections.getDatabaseType(databaseType);
        if (type.getConnectionData().isEmpty()) {
            connections.delete(databaseType);
            Component p = parent.getParent().getParent();
            p.remove(parent.getParent());
        }
        controller.resetMenu();
    }
}
