package com.mindquarry.persistence.jcr;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import com.mindquarry.common.init.InitializationException;

/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class ClientRepository implements Repository {

    Repository repository_;

    public ClientRepository() {
        repository_ = createClientRepository("rmi://localhost:1099/jackrabbit");
    }

    private Repository createClientRepository(String remoteRepoUrl) {
        ClientRepositoryFactory factory = new ClientRepositoryFactory();
        try {
            return factory.getRepository(remoteRepoUrl);
        } catch (ClassCastException e) {
            throw new InitializationException("could not create client " + "jcr repository for repository URL:" + remoteRepoUrl, e);
        } catch (MalformedURLException e) {
            throw new InitializationException("could not create client " + "repository for repository URL:" + remoteRepoUrl, e);
        } catch (RemoteException e) {
            throw new InitializationException("could not create client " + "repository for repository URL:" + remoteRepoUrl, e);
        } catch (NotBoundException e) {
            throw new InitializationException("could not create client " + "repository for repository URL:" + remoteRepoUrl, e);
        }
    }

    public String getDescriptor(String arg0) {
        return repository_.getDescriptor(arg0);
    }

    public String[] getDescriptorKeys() {
        return repository_.getDescriptorKeys();
    }

    public Session login() throws LoginException, RepositoryException {
        return repository_.login();
    }

    public Session login(Credentials arg0) throws LoginException, RepositoryException {
        return repository_.login(arg0);
    }

    public Session login(String arg0) throws LoginException, NoSuchWorkspaceException, RepositoryException {
        return repository_.login(arg0);
    }

    public Session login(Credentials arg0, String arg1) throws LoginException, NoSuchWorkspaceException, RepositoryException {
        return repository_.login(arg0, arg1);
    }
}
