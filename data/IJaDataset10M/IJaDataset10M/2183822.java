package java.rmi.activation;

import java.lang.reflect.Method;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;

/**
 * A stub class for {@link ActivationGroup} implementations.
 *
 * @author Roman Kennke (kennke@aicas.com)
 */
public class ActivationGroup_Stub extends RemoteStub implements ActivationInstantiator, Remote {

    private static final long serialVersionUID = 2L;

    /**
   * Creates a new instance of ActivationGroup_Stub.
   *
   * @param ref the remote reference
   */
    public ActivationGroup_Stub(RemoteRef ref) {
        super(ref);
    }

    /**
   * Stub method for <code>ActivationGroup.newInstance()</code>.
   *
   * @param id the activation ID
   * @param desc the activation description
   *
   * @return the return value of the invocation
   *
   * @throws RemoteException if the invocation throws a RemoteException
   * @throws ActivationException if the invocation throws an
   *         ActivationException
   */
    public MarshalledObject newInstance(ActivationID id, ActivationDesc desc) throws RemoteException, ActivationException {
        try {
            Method method = ActivationGroup_Stub.class.getDeclaredMethod("newInstance", new Class[] { ActivationID.class, ActivationDesc.class });
            return (MarshalledObject) ref.invoke(this, method, new Object[] { id, desc }, -5274445189091581345L);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (RemoteException ex) {
            throw ex;
        } catch (ActivationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnexpectedException("Unexpected exception", ex);
        }
    }
}
