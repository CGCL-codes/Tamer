package de.ibis.permoto.gui.result.panes;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import de.ibis.permoto.gui.result.ResultWizard;
import de.ibis.permoto.gui.result.panes.panels.AbstractResultPanel;

/**
 * Abstract class representing a general tabbed pane.
 * @author Thomas Jansson
 * @author Oliver H�hn
 */
public abstract class TabbedPane extends JTabbedPane {

    private static final long serialVersionUID = 1L;

    /** The ResultWizard holding this pane. */
    public ResultWizard parentWizard;

    /** A list in which all panels are saved. */
    private List<AbstractResultPanel> panels = new ArrayList<AbstractResultPanel>();

    /** The actual number of panels in use. */
    private int panelCount;

    /** The panel in focus. */
    private AbstractResultPanel inFocus;

    /**
	 * Constructor.
	 * @param name - String representation of the name of this tabbed pane.
	 * @param parent - ResultWizard holding this panel.
	 */
    public TabbedPane(final String name, final ResultWizard parent) {
        this.setName(name);
        this.parentWizard = parent;
    }

    /**
	 * Adds an AbstractResultPanel to the tabbed pane.
	 * @param panel - The AbstractResultPanel to be added to this instance.
	 */
    public final void addPanel(final AbstractResultPanel panel) {
        this.panels.add(panel);
        this.add(panel.getName(), new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        this.panelCount = this.getTabCount();
        if (this.panelCount == 1) {
            this.setSelectedIndex(0);
        }
        this.inFocus = panel;
    }

    /** Initiate the components of the pane. */
    protected abstract void initComponent(de.ibis.permoto.gui.result.ResultWizard r);

    /**
	 * Returns the panel in focus.
	 * @return AbstractResultPanel, the panel in focus.
	 */
    public final AbstractResultPanel getFocus() {
        return this.inFocus;
    }

    /**
	 * Returns the ResultWizard of this panel.
	 * @return A ResultWizard
	 */
    public final de.ibis.permoto.gui.result.ResultWizard getParentWizard() {
        return this.parentWizard;
    }

    /**
	 * Removes an AbstractResultPanel from the Wizard.
	 * @param panel - The AbstractResultPanel to be removed
	 */
    public final void removePanel(final AbstractResultPanel panel) {
        this.remove(panel);
        this.panelCount = this.getTabCount();
        this.panels.remove(panel);
    }

    /**
	 * Sets the ResultWizard parent of this panel.
	 * @param parent - A ResultWizard
	 */
    public final void setParentWizard(final de.ibis.permoto.gui.result.ResultWizard parent) {
        this.parentWizard = parent;
    }
}
