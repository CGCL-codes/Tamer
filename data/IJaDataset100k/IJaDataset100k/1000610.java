package org.juddi.function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.juddi.auth.Authenticator;
import org.juddi.auth.AuthenticatorFactory;
import org.juddi.datastore.DataStore;
import org.juddi.datastore.DataStoreFactory;
import org.juddi.datatype.RegistryObject;
import org.juddi.datatype.publisher.Publisher;
import org.juddi.datatype.request.AuthInfo;
import org.juddi.datatype.request.GetAuthToken;
import org.juddi.datatype.response.AuthToken;
import org.juddi.error.RegistryException;
import org.juddi.error.UnknownUserException;
import org.juddi.util.Config;

/**
 * @author Steve Viens (sviens@users.sourceforge.net)
 */
public class GetAuthTokenFunction extends AbstractFunction {

    private static Log log = LogFactory.getLog(GetAuthTokenFunction.class);

    /**
   *
   */
    public GetAuthTokenFunction() {
        super();
    }

    /**
   *
   */
    public RegistryObject execute(RegistryObject regObject) throws RegistryException {
        GetAuthToken request = (GetAuthToken) regObject;
        String generic = request.getGeneric();
        String userID = request.getUserID();
        String cred = request.getCredential();
        DataStoreFactory dataFactory = DataStoreFactory.getFactory();
        DataStore dataStore = dataFactory.acquireDataStore();
        AuthenticatorFactory authFactory = AuthenticatorFactory.getFactory();
        Authenticator authenticator = authFactory.acquireAuthenticator();
        try {
            dataStore.beginTrans();
            String publisherID = authenticator.authenticate(userID, cred);
            if (publisherID == null) throw new UnknownUserException("user ID: " + userID);
            Publisher publisher = dataStore.getPublisher(publisherID);
            if (publisher == null) throw new UnknownUserException("user ID: " + userID);
            String token = dataStore.generateToken(publisher);
            dataStore.storeAuthToken(token, publisher);
            dataStore.commit();
            AuthToken authToken = new AuthToken();
            authToken.setGeneric(generic);
            authToken.setOperator(Config.getOperator());
            authToken.setAuthInfo(new AuthInfo(token));
            return authToken;
        } catch (Exception ex) {
            try {
                dataStore.rollback();
            } catch (Exception e) {
            }
            log.error(ex);
            if (ex instanceof RegistryException) throw (RegistryException) ex; else throw new RegistryException(ex);
        } finally {
            dataFactory.releaseDataStore(dataStore);
            authFactory.releaseAuthenticator(authenticator);
        }
    }

    /***************************************************************************/
    public static void main(String[] args) {
        org.juddi.registry.RegistryEngine reg = org.juddi.registry.RegistryEngine.getInstance();
        reg.init();
        try {
            GetAuthToken request = new GetAuthToken("sviens", "password");
            AuthToken response = (AuthToken) (new GetAuthTokenFunction().execute(request));
            System.out.println("Function: getAuthToken(sviens/password)");
            System.out.println(" AuthInfo: " + response.getAuthInfo());
            request = new GetAuthToken("jdoe", "password");
            System.out.println("Function: getAuthToken(jdoe/password)");
            response = (AuthToken) (new GetAuthTokenFunction().execute(request));
            System.out.println(" AuthInfo: " + response.getAuthInfo());
            request = new GetAuthToken("guest", "password");
            System.out.println("Function: getAuthToken(guest/password)");
            response = (AuthToken) (new GetAuthTokenFunction().execute(request));
            System.out.println(" AuthInfo: " + response.getAuthInfo());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            reg.dispose();
        }
    }
}
