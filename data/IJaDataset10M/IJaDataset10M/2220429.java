package org.jmove.zui.views;

import org.jmove.core.Thing;
import org.jmove.java.loader.aspects.Location;
import org.jmove.java.metrics.RCMartin;
import org.jmove.java.model.JPackage;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Display metrics for all packages containing types.
 *
 * @author Michael Juergens
 * @version $Revision: 1.3 $
 */
public class MetricReport extends ReportView {

    public String getTitle() {
        return "Metrics";
    }

    protected String createReport() {
        StringBuffer report = new StringBuffer("<html>");
        report.append(createHeader());
        report.append("<table border='3' bgcolor='").append(HTML_TABLE_COLOR_BACKGROUND).append("' width='800'>");
        report.append("<tr bgcolor='").append(HTML_TABLE_COLOR_HEADER).append("'>");
        report.append("<th>Package</th>");
        report.append("<th width='90'>Distance</th>");
        report.append("<th width='90'>Abstractness</th>");
        report.append("<th width='90'>Instability</th>");
        report.append("<th width='90'>Afferent C.</th>");
        report.append("<th width='90'>Efferent C.</th>");
        report.append("</tr>");
        List packages = Collections.list(Collections.enumeration(getModel().getPackages()));
        Collections.sort(packages, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((Thing) o1).id().compareTo(((Thing) o2).id());
            }
        });
        for (int i = 0; i < packages.size(); i++) {
            JPackage jPackage = (JPackage) packages.get(i);
            if (isApplicable(jPackage)) {
                report.append(render(jPackage));
            }
        }
        report.append("</table>");
        report.append(createFooter());
        report.append("</html>");
        return report.toString();
    }

    private boolean isApplicable(JPackage aPackage) {
        Iterator types = aPackage.getTypes().iterator();
        if (types.hasNext()) {
            Thing first = (Thing) types.next();
            return !first.aspects(Location.class).isEmpty();
        }
        return false;
    }

    private StringBuffer render(JPackage aPackage) {
        RCMartin rcm = RCMartin.forPackage(aPackage);
        StringBuffer html = new StringBuffer();
        html.append("<tr>");
        html.append("<td>");
        html.append(aPackage.id());
        html.append("</td>");
        html.append("<td>");
        html.append(format(rcm.getDistance()));
        html.append("</td>");
        html.append("<td>");
        html.append(format(rcm.getAbstractness()));
        html.append("</td>");
        html.append("<td>");
        html.append(format(rcm.getInstability()));
        html.append("</td>");
        html.append("<td>");
        html.append(rcm.getAfferentCoupling());
        html.append("</td>");
        html.append("<td>");
        html.append(rcm.getEfferentCoupling());
        html.append("</td>");
        html.append("</tr>");
        return html;
    }

    private String format(double aValue) {
        DecimalFormat df = new DecimalFormat("######.##");
        return df.format(aValue);
    }
}
