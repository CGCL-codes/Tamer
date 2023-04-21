package org.xaware.salesforce.bizcomp.channel;

import java.rmi.RemoteException;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.logging.XAwareLogger;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.SoapBindingStub;
import com.sforce.soap.partner.fault.ApiFault;
import com.sforce.soap.partner.sobject.SObject;

/**
 * @author tferguson
 *
 */
public class SalesforceInsertBatch extends SalesforceBatch {

    private static final String className = SalesforceInsertBatch.class.getName();

    private static final XAwareLogger lf = XAwareLogger.getXAwareLogger(className);

    /**
	 * @param batchCount
	 */
    public SalesforceInsertBatch(SoapBindingStub binding, int batchCount) {
        super(binding, batchCount, TYPE_INSERT);
    }

    /**
	 * Will return an array of {@link com.sforce.soap.partner.SaveResult}
	 * 
	 * @see com.sforce.soap.partner.SoapBindingStub#upsert(String, SObject[])
	 * @see org.xaware.salesforce.bizcomp.channel.SalesforceBatch#executeBatch()
	 */
    @Override
    public Object executeBatch() throws XAwareException {
        SObject[] insertObjects = this.getObjectArray();
        try {
            if (insertObjects.length > 0) {
                SaveResult[] result = binding.create(insertObjects);
                return result;
            } else {
                return null;
            }
        } catch (ApiFault e) {
            throw new XAwareException(" Error creating :" + e.getLocalizedMessage());
        } catch (RemoteException e) {
            throw new XAwareException(" Error creating :" + e.getLocalizedMessage());
        } finally {
            lf.fine("Inserted batch of " + insertObjects.length + " Salesforce objects", className, "executeBatch");
        }
    }
}
