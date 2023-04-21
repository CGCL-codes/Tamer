package tyrex.tm.impl;

import org.omg.CosTransactions.Control;
import org.omg.CosTransactions._ControlImplBase;
import org.omg.CosTransactions.RecoveryCoordinator;
import org.omg.CosTransactions.Terminator;
import org.omg.CosTransactions.Coordinator;
import org.omg.CosTransactions.PropagationContext;
import org.omg.CosTransactions.TransIdentity;
import org.omg.CosTransactions.Unavailable;
import org.omg.CosTransactions.Status;
import org.omg.CosTransactions.Resource;
import org.omg.CosTransactions.otid_t;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Any;
import javax.transaction.xa.Xid;

/**
 * Implements a {@link Control} interface into a transaction.
 * Transactions are implemented strictly by {@link TransactionImpl},
 * however when using the OTS API or communicating with other CORBA
 * servers it is necessary to use the control interface. This object
 * serves as lightweight adapter between the transaction and control
 * interface.
 * <p>
 * Control objects are produced directly only by {@link TransactionImpl}
 * and indirectly by {@link TransactionFactory}.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision: 1.4 $ $Date: 2001/03/21 20:02:48 $
 * @see TransactionImpl
 *
 * Changes 
 *
 * J. Daniel : Changed code to be compliant with CORBA developing rules.
 */
final class ControlImpl extends _ControlImplBase implements Control, RecoveryCoordinator {

    /**
     * The underlying transaction to which this control serves
     * as an interface.
     */
    protected final TransactionImpl _tx;

    /**
     * The list of parents of this transaction, the immediate parent
     * at index 0 and the top level parent at index n-1. If this
     * transaction is a top level, this variable is null.
     */
    protected final TransIdentity[] _parents;

    /**
     * The propagation context created from this control.
     * Set when the propagation context is first requested and
     * held for subsequent requests.
     */
    private PropagationContext _pgContext;

    /**
     * CORBA Reference to the terminator interface
     */
    private Terminator _terminator;

    /**
     * CORBA Reference to the coordinator interface
     */
    private Coordinator _coordinator;

    /**
     * Creates a new control for a transaction that has been imported
     * using the specified propagation context. The local transaction
     * has it's own Xid, but no parents. The control has the parent
     * list passed through the propagation.
     *
     * @param tx The local transaction
     * @param pgContext The propagation context
     */
    ControlImpl(TransactionImpl tx, PropagationContext pgContext) {
        if (tx == null) throw new IllegalArgumentException("Argument tx is null");
        if (pgContext == null) throw new IllegalArgumentException("Argument pgContext is null");
        _tx = tx;
        _parents = new TransIdentity[pgContext.parents.length + 1];
        for (int i = pgContext.parents.length; i-- > 0; ) _parents[i + 1] = pgContext.parents[i];
        _parents[0] = pgContext.current;
    }

    /**
     * Creates a new control for a local transaction that could be
     * used to propagate the transaction to a different server.
     *
     * @param tx The local transaction
     */
    ControlImpl(TransactionImpl tx) {
        ControlImpl parent;
        if (tx == null) throw new IllegalArgumentException("Argument tx is null");
        _tx = tx;
        if (tx.getParent() != null) {
            tx = (TransactionImpl) tx.getParent();
            parent = (ControlImpl) tx.getControl();
            if (parent._parents == null) {
                _parents = new TransIdentity[] { parent.getIdentity() };
            } else {
                _parents = new TransIdentity[parent._parents.length + 1];
                for (int i = parent._parents.length; i-- > 0; ) _parents[i + 1] = parent._parents[i];
                _parents[0] = parent.getIdentity();
            }
        } else _parents = null;
    }

    public Terminator get_terminator() throws Unavailable {
        int status;
        status = _tx.getStatus();
        if (status == javax.transaction.Status.STATUS_ACTIVE || status == javax.transaction.Status.STATUS_MARKED_ROLLBACK) {
            return getTerminator();
        }
        throw new Unavailable();
    }

    public Coordinator get_coordinator() throws Unavailable {
        int status;
        status = _tx.getStatus();
        if (status == javax.transaction.Status.STATUS_ACTIVE || status == javax.transaction.Status.STATUS_MARKED_ROLLBACK) {
            return getCoordinator();
        }
        throw new Unavailable();
    }

    protected Terminator getTerminator() {
        ORB orb;
        if (_terminator != null) return _terminator;
        _terminator = new TerminatorImpl(this);
        orb = _tx._txDomain._orb;
        if (orb != null) orb.connect(_terminator);
        return _terminator;
    }

    protected Coordinator getCoordinator() {
        ORB orb;
        if (_coordinator != null) return _coordinator;
        _coordinator = new CoordinatorImpl(this);
        orb = _tx._txDomain._orb;
        if (orb != null) orb.connect(_coordinator);
        return _coordinator;
    }

    protected synchronized PropagationContext getPropagationContext() {
        ORB orb;
        Any any;
        Xid xid;
        otid_t otid;
        byte[] global;
        TransIdentity identity;
        if (_pgContext != null) return _pgContext;
        xid = _tx._xid;
        global = xid.getGlobalTransactionId();
        otid = new otid_t(xid.getFormatId(), global.length, global);
        identity = new TransIdentity(getCoordinator(), getTerminator(), otid);
        orb = _tx._txDomain._orb;
        if (orb == null) _pgContext = new PropagationContext(_tx._txDomain.getTransactionTimeout(_tx), identity, _parents != null ? _parents : new TransIdentity[0], null); else {
            any = orb.create_any();
            _pgContext = new PropagationContext(_tx._txDomain.getTransactionTimeout(_tx), identity, _parents != null ? _parents : new TransIdentity[0], any);
        }
        return _pgContext;
    }

    protected TransIdentity getIdentity() {
        Xid xid;
        otid_t otid;
        byte[] global;
        TransIdentity identity;
        xid = _tx._xid;
        global = xid.getGlobalTransactionId();
        otid = new otid_t(xid.getFormatId(), global.length, global);
        identity = new TransIdentity(getCoordinator(), getTerminator(), otid);
        return identity;
    }

    /**
     * This operation is used to deactivate all CORBA objects. It is used
     * when a transaction is ended ( rolled back or commited ).
     */
    protected void deactivate() {
        ORB orb;
        orb = _tx._txDomain._orb;
        if (orb != null) {
            orb.disconnect(this);
            if (_coordinator != null) orb.disconnect(_coordinator);
            if (_terminator != null) orb.disconnect(_terminator);
        }
    }

    public Status replay_completion(Resource resource) {
        return CoordinatorImpl.fromJTAStatus(_tx.getStatus());
    }

    /**
     * Return the transaction object which this control interface
     * represents.
     */
    TransactionImpl getTransaction() {
        return _tx;
    }
}
