package au.gov.qld.dnr.dss.v1.ui.result;

import au.gov.qld.dnr.dss.v1.framework.Framework;
import au.gov.qld.dnr.dss.v1.framework.interfaces.ButtonFactory;
import au.gov.qld.dnr.dss.v1.framework.interfaces.ResourceManager;
import au.gov.qld.dnr.dss.v1.report.interfaces.ResultItem;
import au.gov.qld.dnr.dss.v1.view.graph.GraphProperties;
import au.gov.qld.dnr.dss.v1.view.graph.ResultModel;
import au.gov.qld.dnr.dss.v1.view.graph.ResultAlternative;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.swzoo.log2.core.*;
import au.gov.qld.dnr.dss.v1.util.comp.MemoryCollapsibleToolbar;

/**
 * A raw graph panel.  Currently this is used only to obtain values for 
 * printing.  Conceptually it works in the same way as the BarGraphPanel
 * and PolarGraphPanel objects.
 */
public final class RawGraphPanel extends JPanel {

    ResultModel model = null;

    public RawGraphPanel(Properties props) {
    }

    public void setModel(ResultModel model) {
        LogTools.trace(logger, 25, "RawGraphPanel.setModel(ResultModel)");
        this.model = model;
    }

    public ResultItem[] getRawResultData() {
        int size = model.getSize();
        ResultItem[] items = new ResultItem[size];
        for (int i = 0; i < size; i++) {
            items[i] = new OurResultItem(model.getAlternative(i));
        }
        return items;
    }

    private static class OurResultItem implements ResultItem {

        ResultAlternative item;

        OurResultItem(ResultAlternative item) {
            this.item = item;
        }

        public String getName() {
            return item.getName();
        }

        public double getMin() {
            return item.getMin();
        }

        public double getMax() {
            return item.getMax();
        }
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();

    /** Button factory. */
    ButtonFactory buttons = Framework.getGlobalManager().getButtonFactory();

    /** Resources **/
    ResourceManager resources = Framework.getGlobalManager().getResourceManager();
}
