package net.teqlo.components.salesforce.salesforceV0_1;

import net.teqlo.TeqloException;
import net.teqlo.components.OutputSet;
import net.teqlo.components.salesforce.salesforceV0_1.support.SalesforceApex;
import net.teqlo.components.salesforce.salesforceV0_1.support.SalesforceWrapper;
import net.teqlo.components.standard.javascriptV0_01.AbstractScriptExecutor;
import net.teqlo.components.util.TransactionWrapper;
import net.teqlo.db.ActivityLookup;
import net.teqlo.db.User;
import net.teqlo.util.Loggers;

public class SalesforceRemoveActivity extends AbstractSalesforceActivity {

    private static final String SALESFORCE_KEY = "salesforceKey";

    private static final String FAILURE_OUTPUT_KEY = "Failure";

    private static final String FAILURE_MESSAGE_KEY = "errorMessage";

    /**
	 * @param executor
	 * @param al
	 * @throws TeqloException
	 */
    public SalesforceRemoveActivity(User user, AbstractScriptExecutor executor, ActivityLookup al) throws TeqloException {
        super(user, executor, al);
    }

    @Override
    protected void actionsOnRun() throws Exception {
        String salesforceRecordId = (String) this.input.get(SALESFORCE_KEY);
        try {
            this.deleteSalesforceRecord(salesforceRecordId);
            this.addOutputSet();
        } catch (TeqloException e) {
            this.clearOutputs();
            OutputSet output = this.addOutputSet(FAILURE_OUTPUT_KEY);
            String message = "";
            if (e.getMessage() != null) message = e.getMessage();
            output.put(FAILURE_MESSAGE_KEY, message);
            Loggers.XML_RUNTIME.error(message, e);
        }
    }

    /**
	 * This method delegates to the SalesforceApex object
	 * @param salesforceRecordId
	 * @throws TeqloException
	 */
    private void deleteSalesforceRecord(String salesforceRecordId) throws TeqloException {
        final String deleteSalesforceId = salesforceRecordId;
        SalesforceApex salesforceApex = this.getSalesforceApex();
        TransactionWrapper.doTry(new SalesforceWrapper<Object>(salesforceApex) {

            public Object run() throws TeqloException {
                this.sfApex.deleteRecord(deleteSalesforceId);
                return null;
            }
        });
    }
}
