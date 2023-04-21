package net.sf.karatasi.desktop;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;

/** This is the main panel (on the right, tabbed) of the GUI.
 * It holds edit views, file check views, imports, ...
 *
 * @author <a href="mailto:kussinger@sourceforge.net">Mathias Kussinger</a>
 */
public class GuiMainPanel extends JPanel {

    /** Serialization Id */
    private static final long serialVersionUID = -6964148627597060118L;

    /** The ActionBuilder. */
    private final ActionBuilder actionBuilder = ActionBuilderFactory.getInstance().getActionBuilder(GuiMain.class);

    /** The tabbed pane. We don't derive directly from it because we want to control the interfaces. */
    private final JTabbedPane tabbedPane;

    /** The default constructor.
     *
     */
    public GuiMainPanel() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder(actionBuilder.getString("centerPane.title")));
        tabbedPane = new JTabbedPane();
        this.add(tabbedPane);
    }

    /** Add a database action panel.
    *
    */
    public void addDatabaseActionPanel(@NotNull final JPanel panel, @NotNull final String databaseFullName) {
        tabbedPane.add(panel, databaseFullName, 0);
        tabbedPane.setSelectedIndex(0);
    }

    /** Remove a panel.
     *
     * @param panel the panel to be removed.
     */
    public void removePanel(@NotNull final Component panel) {
        tabbedPane.remove(panel);
    }

    /** Display a panel.
     *
     * @param the panel to be displayed.
     */
    public void showPanel(@NotNull final Component panel) {
        tabbedPane.setSelectedComponent(panel);
    }
}
