package org.mitre.rt.client.ui.cchecks.complex.subcomplex;

import java.util.List;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.mitre.rt.client.ui.AbsXmlObjTableModel;
import org.mitre.rt.rtclient.ApplicationType.ComplexComplianceChecks;
import org.mitre.rt.rtclient.ComplexComplianceCheckType;

/**
 *
 * @author JWINSTON
 */
public class SubComplexCheckAddTableModel extends AbsXmlObjTableModel<ComplexComplianceCheckType> {

    private static final Logger logger = Logger.getLogger(SubComplexCheckAddTableModel.class.getPackage().getName());

    public static final int TITLE = 0;

    public static final int STATUS = 1;

    public static final int LAST_MODIFIED = 2;

    public SubComplexCheckAddTableModel(XmlObject parent, ComplexComplianceChecks rules) {
        super(parent, rules);
        super.setData(parent, rules);
    }

    @Override
    public void processData(XmlObject parent, XmlObject dataContainer) {
        if (dataContainer != null) {
            List<ComplexComplianceCheckType> unusedComplexChecks = ((ComplexComplianceChecks) dataContainer).getComplexComplianceCheckList();
            super.setData(unusedComplexChecks);
        } else {
            super.setData(null);
        }
    }

    @Override
    protected void createHeaders() {
        String[] headers = new String[] { "Title", "Status", "Last Modified" };
        super.setColumnHeaders(headers);
    }
}
