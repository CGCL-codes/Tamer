package database.protocol;

import java.util.ArrayList;
import java.util.List;
import database.objects.Operate;
import database.objects.Transaction;

/**
 * Gerenciador de bloqueios.
 * @author Landim - Arthur Landim
 *
 */
public class LockManagement {

    /**
     * Lista de bloqueios
     */
    private List<Lock> lockList = new ArrayList<Lock>();

    private Protocol protocol;

    /**
     * Verificar se existe conflito.
     * @param oper
     * @return Lock causador de conflito.
     */
    public Lock hasConflict(Operate oper) {
        boolean read = (Operate.READ.equals(oper.getType()) ? true : false);
        for (Lock lock : lockList) {
            if ((!Transaction.ABORTED.equals(lock.getTransaction().getState())) && lock.getObject().equals(oper.getObject()) && !lock.getTransaction().equals(oper.getTransaction())) {
                if ((!(read && Lock.BLOCK_READ.equals(lock.getLockType())))) {
                    protocol.log(oper + " est� solicitando bloqueio de " + (read ? Lock.BLOCK_READ : Lock.BLOCK_WRITE) + " do objeto " + oper.getObject().getId() + " pertencente � transa��o " + lock.getTransaction().getId());
                    return lock;
                }
            }
        }
        return null;
    }

    /**
     * @return Returns the lockList.
     */
    public List<Lock> getLockList() {
        return lockList;
    }

    /**
     * @param lockList The lockList to set.
     */
    public void setLockList(List<Lock> lockList) {
        this.lockList = lockList;
    }

    public Lock createLock(Operate oper) {
        Lock newLock = new Lock();
        newLock.setLockType(Operate.READ.equals(oper.getType()) ? Lock.BLOCK_READ : Lock.BLOCK_WRITE);
        newLock.setObject(oper.getObject());
        newLock.setTransaction(oper.getTransaction());
        newLock.setOperate(oper);
        if (Operate.ABORT.equals(oper.getType())) {
            return newLock;
        }
        lockList.add(newLock);
        return newLock;
    }

    public Lock getLockByTransaction(Transaction trans) {
        for (int i = 0; i < lockList.size(); i++) {
            Lock lock = lockList.get(i);
            if (lock.getTransaction().equals(trans)) {
                return lock;
            }
        }
        return null;
    }

    public void releaseLocks(Operate oper) {
        for (int i = 0; i < lockList.size(); i++) {
            Lock lock = lockList.get(i);
            if (lock.getTransaction().equals(oper.getTransaction())) {
                protocol.insertObject(lock);
                lockList.remove(lock);
                i = -1;
            }
        }
    }

    /**
     * @return Returns the protocol.
     */
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     * @param protocol The protocol to set.
     */
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
}
