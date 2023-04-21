package com.modelmetrics.cloudconverter.describe;

import java.util.Iterator;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.modelmetrics.common.sforce.SalesforceCredentials;
import com.modelmetrics.common.sforce.SalesforceSession;
import com.modelmetrics.common.sforce.SalesforceSessionFactory;

public class LayoutsBuilderV2Test extends TestCase {

    private Log log = LogFactory.getLog(LayoutsBuilderV2Test.class);

    public void testBasic() throws Exception {
        SalesforceCredentials cred = new SalesforceCredentials();
        cred.setPassword("blah1234");
        cred.setUsername("reid_carlberg@modelmetrics.com");
        SalesforceSession salesforceSession = SalesforceSessionFactory.factory.build(cred);
        LayoutsBuilderV2 builder = new LayoutsBuilderV2();
        LayoutsSummary summary = builder.execute(salesforceSession, "AAA__c");
        int size = 0;
        for (Iterator iter = summary.getRows().iterator(); iter.hasNext(); ) {
            LayoutsFieldVOV2 element = (LayoutsFieldVOV2) iter.next();
            if (size == 0) {
                size = element.getLayouts().size();
            }
            assertEquals(size, element.getLayouts().size());
            assertEquals(size, summary.getHeaders().size());
        }
    }
}
