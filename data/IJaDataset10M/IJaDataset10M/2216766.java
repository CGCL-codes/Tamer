package org.mitre.rt.client.ui.cchecks;

import java.util.Collections;
import java.util.List;
import org.mitre.rt.client.ui.AbsXmlObjTableModel;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.mitre.rt.client.xml.ComplianceCheckHelper;
import org.mitre.rt.rtclient.ApplicationType;
import org.mitre.rt.rtclient.ComplianceCheckType;
import org.mitre.rt.rtclient.RecommendationType.ComplianceCheckRefs;
import org.mitre.rt.rtclient.SharedIdType;

public class CChecksTableModel extends AbsXmlObjTableModel<ComplianceCheckType> {

    private static final Logger logger = Logger.getLogger(CChecksTableModel.class.getPackage().getName());

    public static final int TITLE = 0, FILE = 1, STATUS = 2, LAST_MODIFIED = 3;

    private final ComplianceCheckHelper complianceCheckHelper = new ComplianceCheckHelper();

    public CChecksTableModel(ComplianceCheckRefs complianceCheckRefs, ApplicationType application) {
        super(application, complianceCheckRefs);
        super.setData(application, complianceCheckRefs);
    }

    @Override
    public void processData(XmlObject parent, XmlObject dataContainer) {
        ApplicationType app = (ApplicationType) parent;
        ComplianceCheckRefs myCCheckRefs = (ComplianceCheckRefs) dataContainer;
        List<ComplianceCheckType> myData = null;
        if (myCCheckRefs != null && app.isSetComplianceChecks()) {
            List<SharedIdType> cCheckRefList = myCCheckRefs.xgetComplianceCheckRefList();
            List<ComplianceCheckType> allComplianceChecks = app.getComplianceChecks().getComplianceCheckList();
            myData = complianceCheckHelper.getReferencedItems(allComplianceChecks, cCheckRefList);
        } else myData = Collections.emptyList();
        super.setData(myData);
    }

    @Override
    protected void createHeaders() {
        String[] headers = new String[] { "Title", "File", "Status", "Last Modified" };
        super.setColumnHeaders(headers);
    }
}
