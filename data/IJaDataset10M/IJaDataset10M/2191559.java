package com.i3sp.sso.authserver;

import com.i3sp.tptk.authserver.AuthInterface;
import com.i3sp.tptk.authserver.BasicCredential;
import com.i3sp.tptk.authserver.Credential;
import com.i3sp.tptk.authserver.AuthException;
import com.i3sp.tptk.authserver.InvalidCredentialException;
import com.i3sp.tptk.authserver.AuthResult;
import com.i3sp.sso.SSOUser;
import com.i3sp.jndi.ServiceException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import com.mortbay.Util.Code;

public class AuthClient {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java AuthClient <username> <password>");
            System.exit(1);
        }
        try {
            AuthInterface as = (AuthInterface) Naming.lookup("rmi://newtown:1099/AuthenticationServer");
            BasicCredential creds = new BasicCredential(args[0], args[1]);
            Code.debug("Auth credentials are: " + creds.toString());
            System.out.flush();
            AuthResult result = (AuthResult) as.authenticate(creds);
            System.out.println("User " + creds.getUserName() + " authenticated");
        } catch (NotBoundException nbe) {
            System.out.println();
            System.out.println("NotBoundException ");
            System.out.println(nbe);
        } catch (InvalidCredentialException ice) {
            System.out.println();
            System.out.println("InvalidCredentialException " + ice.getMessage());
        } catch (ServiceException se) {
            System.out.println();
            System.out.println("ServiceException");
            System.out.println(se);
        } catch (AuthException ae) {
            System.out.println();
            System.out.println("AuthException");
            System.out.println(ae);
        } catch (MalformedURLException murle) {
            System.out.println();
            System.out.println("MalformedURLException");
            System.out.println(murle);
        } catch (RemoteException re) {
            System.out.println();
            System.out.println("RemoteException");
            System.out.println(re);
        }
    }
}
