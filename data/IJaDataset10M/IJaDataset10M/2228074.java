package net.sf.karatasi.databaseoperations.edit;

import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

/** This is the parent of all controllers that provide access to components of the database.
 *
 * @author <a href="mailto:kussinger@sourceforge.net">Mathias Kussinger</a>
 */
public abstract class DetailController {

    /** The overlooking database edit controller. */
    @NotNull
    private DatabaseEditController databaseEditController;

    /** Constructor.
     * @param databaseEditController the overlooking controller.
     */
    DetailController(@NotNull final DatabaseEditController databaseEditController) {
        this.databaseEditController = databaseEditController;
    }

    /** Getter for the databaseEditController.
     * @return the overlooking databaseEditController.
     */
    DatabaseEditController getDatabaseEditController() {
        return databaseEditController;
    }

    /** Get the tab on the main panel.
     *
     * @return the panel.
     */
    public abstract JPanel getPanel();

    /** Shut down the controller and unlink the panel.
     * We need this to break up the circle reference controller -> panel -> controller
     */
    public void close() {
        databaseEditController = null;
    }

    /** Return a list container for the panel.
     *
     * @return the list container, or null if the panel represents no list.
     */
    public abstract ListContainer getAsListContainer();
}
