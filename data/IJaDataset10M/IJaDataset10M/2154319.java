package purej.core.module.sequence;

/**
 * Remote interface for SequenceGenerator.
 * 
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:hani@formicary.net">Hani Suleiman</a>
 * @version $Revision: 1.2 $
 */
public interface SequenceGenerator extends javax.ejb.EJBObject {

    public long getCount(java.lang.String sequenceName) throws java.rmi.RemoteException;
}
