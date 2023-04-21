package net.sf.karatasi.databaseoperations;

import javax.swing.event.ChangeEvent;
import org.jetbrains.annotations.NotNull;

/** The interface which the database control center provides towards the database action controllers.
 *
 * @author <a href="mailto:kussinger@sourceforge.net">Mathias Kussinger</a>
 */
public interface ControlCenterInterface {

    /** Event from an action controller that it has finished it's activity.
     * This event may be called from any thread context.
     *
     * @param event a ChangeEvent object
     */
    void actionFinished(final ChangeEvent event);

    /** Update the tab title for a controller.
     * @param oldTitle the old title string
     * @param newTitle the new title string
     */
    void updatePanelTitle(@NotNull final String oldTitle, @NotNull final String newTitle);
}
