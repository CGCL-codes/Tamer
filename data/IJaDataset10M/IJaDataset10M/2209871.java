package org.juddi.function;

import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.juddi.datastore.DataStore;
import org.juddi.datastore.DataStoreFactory;
import org.juddi.datatype.KeyedReference;
import org.juddi.datatype.Name;
import org.juddi.datatype.RegistryObject;
import org.juddi.datatype.assertion.PublisherAssertion;
import org.juddi.datatype.business.BusinessEntity;
import org.juddi.datatype.publisher.Publisher;
import org.juddi.datatype.request.AddPublisherAssertions;
import org.juddi.datatype.request.AuthInfo;
import org.juddi.datatype.request.GetAuthToken;
import org.juddi.datatype.request.SaveBusiness;
import org.juddi.datatype.response.AuthToken;
import org.juddi.datatype.response.BusinessDetail;
import org.juddi.datatype.response.DispositionReport;
import org.juddi.datatype.response.Result;
import org.juddi.datatype.tmodel.TModel;
import org.juddi.error.InvalidKeyPassedException;
import org.juddi.error.RegistryException;
import org.juddi.error.UserMismatchException;
import org.juddi.util.Config;

/**
 * @author Steve Viens (sviens@users.sourceforge.net)
 */
public class AddPublisherAssertionsFunction extends AbstractFunction {

    private static Log log = LogFactory.getLog(AddPublisherAssertionsFunction.class);

    /**
   *
   */
    public AddPublisherAssertionsFunction() {
        super();
    }

    /**
   *
   */
    public RegistryObject execute(RegistryObject regObject) throws RegistryException {
        AddPublisherAssertions request = (AddPublisherAssertions) regObject;
        AuthInfo authInfo = request.getAuthInfo();
        Vector assertionVector = request.getPublisherAssertionVector();
        String generic = request.getGeneric();
        DataStoreFactory factory = DataStoreFactory.getFactory();
        DataStore dataStore = factory.acquireDataStore();
        try {
            dataStore.beginTrans();
            Publisher publisher = getPublisher(authInfo, dataStore);
            String publisherID = publisher.getPublisherID();
            for (int i = 0; i < assertionVector.size(); i++) {
                PublisherAssertion assertion = (PublisherAssertion) assertionVector.elementAt(i);
                String fromKey = assertion.getFromKey();
                if ((fromKey == null) || (fromKey.length() == 0)) throw new InvalidKeyPassedException("FromKey: " + fromKey);
                String toKey = assertion.getToKey();
                if ((toKey == null) || (toKey.length() == 0)) throw new InvalidKeyPassedException("ToKey: " + toKey);
                KeyedReference keyedRef = assertion.getKeyedReference();
                if (keyedRef == null) throw new InvalidKeyPassedException("KeyedRef: " + keyedRef);
                String tModelKey = keyedRef.getTModelKey();
                if ((tModelKey == null) || (tModelKey.length() == 0)) throw new InvalidKeyPassedException("TModelKey: " + keyedRef);
                if ((!dataStore.isValidBusinessKey(fromKey)) && (!dataStore.isValidTModelKey(fromKey))) throw new InvalidKeyPassedException("FromKey: " + fromKey);
                if ((!dataStore.isValidBusinessKey(toKey)) && (!dataStore.isValidTModelKey(toKey))) throw new InvalidKeyPassedException("ToKey: " + toKey);
                if ((!dataStore.isBusinessPublisher(fromKey, publisherID)) && (!dataStore.isBusinessPublisher(toKey, publisherID)) && (!dataStore.isTModelPublisher(fromKey, publisherID)) && (!dataStore.isTModelPublisher(toKey, publisherID))) throw new UserMismatchException("fromKey: " + fromKey + " or toKey: " + toKey);
            }
            dataStore.saveAssertions(publisherID, assertionVector);
            dataStore.commit();
        } catch (Exception ex) {
            try {
                dataStore.rollback();
            } catch (Exception e) {
            }
            log.error(ex);
            if (ex instanceof RegistryException) throw (RegistryException) ex; else throw new RegistryException(ex);
        } finally {
            factory.releaseDataStore(dataStore);
        }
        DispositionReport dispRpt = new DispositionReport();
        dispRpt.setGeneric(generic);
        dispRpt.setOperator(Config.getOperator());
        dispRpt.addResult(new Result(Result.E_SUCCESS));
        return dispRpt;
    }

    /***************************************************************************/
    public static void main(String[] args) {
        org.juddi.registry.RegistryEngine reg = org.juddi.registry.RegistryEngine.getInstance();
        reg.init();
        try {
            GetAuthToken authTokenRequest1 = new GetAuthToken("sviens", "password");
            AuthToken authToken1 = (AuthToken) reg.execute(authTokenRequest1);
            AuthInfo authInfo1 = authToken1.getAuthInfo();
            BusinessEntity business1 = new BusinessEntity();
            business1.addName(new Name("Blockbuster", "en"));
            Vector businessVector1 = new Vector(1);
            businessVector1.addElement(business1);
            SaveBusiness sbReq1 = new SaveBusiness();
            sbReq1.setAuthInfo(authInfo1);
            sbReq1.setBusinessEntityVector(businessVector1);
            BusinessDetail detail1 = (BusinessDetail) reg.execute(sbReq1);
            Vector detailVector1 = detail1.getBusinessEntityVector();
            BusinessEntity b1 = (BusinessEntity) detailVector1.elementAt(0);
            GetAuthToken authTokenRequest2 = new GetAuthToken("steveviens", "password");
            AuthToken authToken2 = (AuthToken) reg.execute(authTokenRequest2);
            AuthInfo authInfo2 = authToken2.getAuthInfo();
            BusinessEntity business2 = new BusinessEntity();
            business2.addName(new Name("PopSecret", "en"));
            Vector businessVector2 = new Vector(1);
            businessVector2.addElement(business2);
            SaveBusiness sbReq2 = new SaveBusiness();
            sbReq2.setAuthInfo(authInfo2);
            sbReq2.setBusinessEntityVector(businessVector2);
            BusinessDetail detail2 = (BusinessDetail) reg.execute(sbReq2);
            Vector detailVector2 = detail2.getBusinessEntityVector();
            BusinessEntity b2 = (BusinessEntity) detailVector2.elementAt(0);
            String fromKey = b1.getBusinessKey();
            String toKey = b2.getBusinessKey();
            KeyedReference keyedReference = new KeyedReference("Partner Company", "peer-peer");
            keyedReference.setTModelKey(TModel.RELATIONSHIPS_TMODEL_KEY);
            PublisherAssertion assertion = new PublisherAssertion(fromKey, toKey, keyedReference);
            Vector assertionVector = new Vector();
            assertionVector.addElement(assertion);
            AddPublisherAssertions apaReq = new AddPublisherAssertions();
            apaReq.setAuthInfo(authInfo1);
            apaReq.setPublisherAssertionVector(assertionVector);
            DispositionReport dspRpt1 = (DispositionReport) reg.execute(apaReq);
            System.out.println("errno: " + dspRpt1.toString());
            System.out.println();
            DispositionReport dspRpt2 = (DispositionReport) reg.execute(apaReq);
            System.out.println("errno: " + dspRpt2.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            reg.dispose();
        }
    }
}
