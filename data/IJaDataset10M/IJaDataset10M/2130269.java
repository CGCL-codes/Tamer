package org.elf.datalayer.kernel.impl.transaction;

import javax.naming.*;
import javax.transaction.*;
import org.elf.datalayer.kernel.services.transaction.KernelTransaction;

/**
 * Implementa las transacciones mediante el protocolo JTA/XA. 
 * Para usar esta clase esta clase hace falta un servidor de aplicaciones
 * que soporte el protocolo JTA/XA
 * 
 * @author  <a href="mailto:logongas@users.sourceforge.net">Lorenzo Gonz�lez</a>
 */
public class KernelTransactionImplJTA implements KernelTransaction {

    public void begin() {
        try {
            Context myCntxt = new InitialContext();
            UserTransaction ut = (UserTransaction) myCntxt.lookup("java:comp/UserTransaction");
            ut.begin();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void commit() {
        try {
            Context myCntxt = new InitialContext();
            UserTransaction ut = (UserTransaction) myCntxt.lookup("java:comp/UserTransaction");
            ut.commit();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void rollback() {
        try {
            Context myCntxt = new InitialContext();
            UserTransaction ut = (UserTransaction) myCntxt.lookup("java:comp/UserTransaction");
            ut.rollback();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
