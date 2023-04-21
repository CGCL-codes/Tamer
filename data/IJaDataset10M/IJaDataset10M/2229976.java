package edu.mit.wi.omnigene.service.security;

/**
 *
 * @author  rajesh kuttan
 * @version 
 */
public final class IAuthenticatorFactory {

    private IAuthenticatorFactory() {
    }

    public static IAuthenticator create() throws java.rmi.RemoteException, javax.ejb.CreateException, javax.naming.NamingException {
        return create("Authenticator");
    }

    public static IAuthenticator create(String name) throws java.rmi.RemoteException, javax.ejb.CreateException, javax.naming.NamingException {
        if (name == null || name.length() == 0) name = "Authenticator";
        try {
            IAuthenticatorHome objHome = (IAuthenticatorHome) edu.mit.wi.omnigene.util.BeanReference.getEJBHome(name, IAuthenticatorHome.class);
            return objHome.create();
        } catch (javax.ejb.CreateException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        }
    }

    public static IAuthenticator narrow(javax.ejb.EJBObject object) throws java.rmi.RemoteException {
        return (IAuthenticator) edu.mit.wi.omnigene.util.BeanReference.narrow(object, IAuthenticator.class);
    }
}
