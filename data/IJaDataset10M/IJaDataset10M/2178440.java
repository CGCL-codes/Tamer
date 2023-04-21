package graphlab.gui.plugins.reports.extension;

import graphlab.gui.plugins.reports.ui.ReportsUI;
import graphlab.main.core.BlackBoard.BlackBoard;
import graphlab.main.core.action.AbstractAction;
import graphlab.ui.xml.ExtensionHandler;

/**
 * @author azin azadi
 * @email aazadi@gmail.com
 */
public class GraphReportExtensionHandler implements ExtensionHandler {

    AbstractAction a = null;

    public static final String REPORTS_UI = "reports ui";

    /**
     * @return null if clazz doesn't implements GraphGeneratorExtension
     */
    public AbstractAction Handle(BlackBoard b, Class clazz) {
        a = null;
        if (GraphReportExtension.class.isAssignableFrom(clazz)) {
            try {
                ReportsUI rsd = b.get(REPORTS_UI);
                if (rsd == null) {
                    rsd = new ReportsUI(b);
                    b.set(REPORTS_UI, rsd);
                }
                GraphReportExtension gr = (GraphReportExtension) clazz.newInstance();
                a = new GraphReportExtensionAction(b, gr);
                rsd.addReport(gr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return a;
    }
}
