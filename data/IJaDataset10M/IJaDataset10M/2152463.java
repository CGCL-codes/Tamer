package purej.core.module.sequence;

/**
 * Home interface for SequenceGenerator.
 * 
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:hani@formicary.net">Hani Suleiman</a>
 * @version $Revision: 1.2 $
 */
public interface SequenceGeneratorHome extends javax.ejb.EJBHome {

    public purej.core.module.sequence.SequenceGenerator create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
