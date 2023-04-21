package org.geoforge.guitlcolg.panel.ctr.wwd.oxp.scrollpane;

import org.geoforge.guitlc.panel.ctr.scrollpane.ScrControlManDskAbs;
import java.awt.event.ActionListener;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionListener;
import org.geoforge.guillcolg.tree.oxp.TreControlManDskOxp;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class ScrControlManDskOxp extends ScrControlManDskAbs {

    public ScrControlManDskOxp(ActionListener alrParentPanelMvc, TreeSelectionListener lstTreeSelectionToolbar, TreeExpansionListener lstTreeExpansionToolbar, TreeModelListener lstTreeModelToolbar) throws Exception {
        super();
        super._tre_ = new TreControlManDskOxp(alrParentPanelMvc, lstTreeSelectionToolbar, lstTreeExpansionToolbar, lstTreeModelToolbar);
    }
}
