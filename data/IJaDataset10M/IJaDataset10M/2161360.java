package org.mitre.rt.client.ui.cchecks.complex.subcomplex;

import javax.swing.JTable;
import org.apache.log4j.Logger;
import org.mitre.rt.client.ui.AbsColorTableTextRenderer;
import org.mitre.rt.client.util.DateUtils;
import org.mitre.rt.client.util.MixedContent;
import org.mitre.rt.client.xml.ComplexComplianceCheckHelper;
import org.mitre.rt.client.xml.RTHelper;
import org.mitre.rt.rtclient.ApplicationType;
import org.mitre.rt.rtclient.ComplexComplianceCheckType;
import org.mitre.rt.rtclient.SharedIdType;

/**
 *
 * @author JWINSTON
 */
public class SubComplexCheckTextRenderer extends AbsColorTableTextRenderer {

    private static final Logger logger = Logger.getLogger(SubComplexCheckTextRenderer.class.getPackage().getName());

    private ApplicationType application = null;

    private final ComplexComplianceCheckHelper complexCheckHelper = new ComplexComplianceCheckHelper();

    private final MixedContent mixedContent = new MixedContent();

    public SubComplexCheckTextRenderer(JTable table, ApplicationType app) {
        super(table);
        application = app;
    }

    @Override
    public void setData(Object value, int row, int column) {
        SharedIdType complexCheckRef = (SharedIdType) value;
        String text = "";
        column = table.convertColumnIndexToModel(column);
        if (complexCheckRef != null) {
            ComplexComplianceCheckType complexCheck = complexCheckHelper.getItem(application.getComplexComplianceChecks().getComplexComplianceCheckList(), complexCheckRef.getStringValue());
            if (complexCheck != null) {
                if (column == SubComplexCheckTableModel.TITLE) {
                    text = complexCheck.getTitle();
                } else if (column == SubComplexCheckTableModel.STATUS) {
                    String status = complexCheck.getStatusId();
                    text = RTHelper.getCheckStatusStr(status);
                } else if (column == SubComplexCheckTableModel.LAST_MODIFIED) {
                    text = DateUtils.formatDate(complexCheck.getModified());
                }
            }
        }
        super.setText(text);
    }
}
