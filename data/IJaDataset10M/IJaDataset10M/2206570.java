package org.juddi.function;

import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.juddi.datastore.DataStore;
import org.juddi.datastore.DataStoreFactory;
import org.juddi.datatype.RegistryObject;
import org.juddi.datatype.binding.BindingTemplate;
import org.juddi.datatype.publisher.Publisher;
import org.juddi.datatype.request.AuthInfo;
import org.juddi.datatype.request.SaveBinding;
import org.juddi.datatype.response.BindingDetail;
import org.juddi.error.InvalidKeyPassedException;
import org.juddi.error.RegistryException;
import org.juddi.error.UserMismatchException;
import org.juddi.util.Config;
import org.juddi.uuidgen.UUIDGen;
import org.juddi.uuidgen.UUIDGenFactory;

/**
 * @author Steve Viens (sviens@users.sourceforge.net)
 */
public class SaveBindingFunction extends AbstractFunction {

    private static Log log = LogFactory.getLog(SaveBindingFunction.class);

    /**
   *
   */
    public SaveBindingFunction() {
        super();
    }

    /**
   *
   */
    public RegistryObject execute(RegistryObject regObject) throws RegistryException {
        SaveBinding request = (SaveBinding) regObject;
        String generic = request.getGeneric();
        AuthInfo authInfo = request.getAuthInfo();
        Vector bindingVector = request.getBindingTemplateVector();
        UUIDGen uuidgen = UUIDGenFactory.getUUIDGen();
        DataStoreFactory dataFactory = DataStoreFactory.getFactory();
        DataStore dataStore = dataFactory.acquireDataStore();
        try {
            dataStore.beginTrans();
            Publisher publisher = getPublisher(authInfo, dataStore);
            String publisherID = publisher.getPublisherID();
            for (int i = 0; i < bindingVector.size(); i++) {
                BindingTemplate binding = (BindingTemplate) bindingVector.elementAt(i);
                String serviceKey = binding.getServiceKey();
                String bindingKey = binding.getBindingKey();
                if ((serviceKey == null) || (serviceKey.length() == 0) || (!dataStore.isValidServiceKey(serviceKey))) throw new InvalidKeyPassedException("ServiceKey: " + serviceKey);
                if (!dataStore.isServicePublisher(serviceKey, publisherID)) throw new UserMismatchException("ServiceKey: " + serviceKey);
                if ((bindingKey != null) && (bindingKey.length() > 0) && (!dataStore.isValidBindingKey(bindingKey))) throw new InvalidKeyPassedException("BindingKey: " + bindingKey);
            }
            for (int i = 0; i < bindingVector.size(); i++) {
                BindingTemplate binding = (BindingTemplate) bindingVector.elementAt(i);
                String bindingKey = binding.getBindingKey();
                if ((bindingKey != null) && (bindingKey.length() > 0)) dataStore.deleteBinding(bindingKey); else binding.setBindingKey(uuidgen.uuidgen());
                dataStore.saveBinding(binding);
            }
            dataStore.commit();
            BindingDetail detail = new BindingDetail();
            detail.setGeneric(generic);
            detail.setOperator(Config.getOperator());
            detail.setTruncated(false);
            detail.setBindingTemplateVector(bindingVector);
            return detail;
        } catch (Exception ex) {
            try {
                dataStore.rollback();
            } catch (Exception e) {
            }
            log.error(ex);
            if (ex instanceof RegistryException) throw (RegistryException) ex; else throw new RegistryException(ex);
        } finally {
            dataFactory.releaseDataStore(dataStore);
        }
    }

    /***************************************************************************/
    public static void main(String[] args) {
        org.juddi.registry.RegistryEngine reg = org.juddi.registry.RegistryEngine.getInstance();
        reg.init();
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            reg.dispose();
        }
    }
}
