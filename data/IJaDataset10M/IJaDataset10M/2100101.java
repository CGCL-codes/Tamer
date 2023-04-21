package jmodnews.gui.view;

import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import jmodnews.gui.ModuleGroup;
import jmodnews.gui.ModulePanel;

/**
 * A panel containing module panels laid out by a {@link View}.
 * @author Michael Schierl <schierlm@gmx.de>
 */
public class ViewPanel extends JPanel {

    private View view;

    private ModuleGroup group;

    private final ViewPanelContainer vpc;

    private JComponent viewPanel;

    public ViewPanel(View v, ModuleGroup g, ViewPanelContainer vpc) {
        super(new BorderLayout());
        this.vpc = vpc;
        g.registerView(this);
        view = v;
        group = g;
        add(BorderLayout.CENTER, viewPanel = v.getNewPanel(g, this));
    }

    /**
	 * @param panel
	 *
	 */
    public boolean containsModule(ModulePanel panel) {
        return view.containsModule(panel.getPanelType());
    }

    /**
	 * 
	 */
    public void ensureFocused() {
        vpc.ensureFocused(this);
    }

    public List lostPanels = new LinkedList();

    public void panelLost(ModulePanel mp) {
        lostPanels.add(mp);
    }

    public void regenerate() {
        if (lostPanels.size() != 0) view.regenerate(viewPanel, lostPanels);
        if (lostPanels.size() != 0) throw new IllegalStateException("Could not regenerate panels");
    }

    public List getAllModulePanels() {
        return view.getAllPanels(viewPanel);
    }

    public View getView() {
        return view;
    }

    /**
     * @return Returns the module group.
     */
    public ModuleGroup getGroup() {
        return group;
    }

    public void disposePanels() {
        List p = view.getAllPanels(viewPanel);
        for (Iterator it = p.iterator(); it.hasNext(); ) {
            ModulePanel mp = (ModulePanel) it.next();
            mp.removeViewPanel();
        }
        group.unregisterView(this);
    }
}
