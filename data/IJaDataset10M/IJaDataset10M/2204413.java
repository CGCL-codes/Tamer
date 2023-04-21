package org.openhealthexchange.messagestore.grid;

import java.util.List;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.renderer.HtmlCellRendererImpl;
import org.openhealthexchange.messagestore.vo.PixManagerBean;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;

/**
 * @author Reddy
 *
 */
public class PatientIDCell extends HtmlCellRendererImpl {

    PixManagerBean bean = null;

    @Override
    public Object render(Object item, int rowcount) {
        String property = getColumn().getProperty();
        Object value = getCellEditor().getValue(item, property, rowcount);
        Object bean = getCellEditor().getValue(item, "bean", rowcount);
        HtmlBuilder html = new HtmlBuilder();
        html.td(2);
        html.width(getColumn().getWidth());
        if (property.equalsIgnoreCase("nameString")) {
            html.style(" text-decoration: underline");
        } else {
            html.style(getStyle());
        }
        html.styleClass(getStyleClass());
        html.close();
        html.span();
        html.close();
        String hid = null;
        if (bean instanceof PixManagerBean) {
            PixManagerBean pixbean = (PixManagerBean) bean;
            List<PatientIdentifier> pidlist = pixbean.getPidlist();
            hid = pixbean.getNameString() + pixbean.getDob() + pixbean.getFullAddress();
            hid = hid.replaceAll(",", "");
            hid = hid.replaceAll("/", "");
            String patientIDTooltipDiv = org.openhealthexchange.messagestore.grid.Tooltip.getPatientIDTooltipDiv(pidlist, "domTT_" + hid);
            html.append(patientIDTooltipDiv);
        }
        html.append("<a onMouseOver=\"domTT_activate(this, event, 'caption','List of matched Patient IDs','content', document.getElementById('domTT_" + hid + "') , 'fade', 'both', 'fadeMax', 87, 'position', 'absolute', 'delay', 0);\">");
        if (value != null) {
            html.append(value.toString());
        }
        html.append("</a>");
        html.spanEnd();
        html.tdEnd();
        return html.toString();
    }
}
