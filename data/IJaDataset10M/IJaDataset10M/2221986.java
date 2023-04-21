package oracle.toplink.essentials.internal.ejb.cmp3.naming;

import java.util.Hashtable;
import javax.persistence.EntityManagerFactory;

public class InitialContextImpl extends oracle.toplink.essentials.internal.ejb.cmp3.naming.base.InitialContextImpl {

    public InitialContextImpl() {
        super();
    }

    public InitialContextImpl(Hashtable env) {
        super(env);
    }

    /************************/
    protected Object handleEntityManagerFactory(Object obj) {
        if (obj instanceof EntityManagerFactory) {
            debug("Ctx - create EM from bound EMFactory");
            return ((EntityManagerFactory) obj).createEntityManager();
        } else {
            return obj;
        }
    }
}
