package ordas.ejb.core.utils;

import ordas.model.orgs.OrgInternal;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ordas.model.core.FuncArea;
import ordas.model.core.SeqItemInstanceNumberGenerator;
import ordas.model.core.UserPropsOrdas;

/**
 * We use this class because SessionContext can't be accessed from JPA Entity Beans.Various usages
 * Usamos esta clase porque no se puede acceder a SessionContext desde JPA Entity Beans. Diversos usos
 * Nous utilisons cette class parce que on ne peut pas accéder de JPA Entity Beans. Diverses usages
 */
@Stateless
public class SessionContextUtility {

    @Resource
    SessionContext sctx;

    @PersistenceContext
    EntityManager pcem;

    private SeqItemInstanceNumberGenerator siing;

    private Byte returnedvalue;

    public SessionContextUtility() {
    }

    public String getCurrentUserName() {
        return sctx.getCallerPrincipal().getName();
    }

    public OrgInternal getCurrentUserDefaultOrg() {
        UserPropsOrdas oup = (UserPropsOrdas) pcem.find(UserPropsOrdas.class, getCurrentUserName());
        return oup.getUser_default_org();
    }

    public FuncArea getCurrentUserDefaultFuncArea() {
        UserPropsOrdas oup = (UserPropsOrdas) pcem.find(UserPropsOrdas.class, getCurrentUserName());
        return oup.getUser_default_func_area();
    }

    public Byte getNextDatabaseInstanceNumber() {
        siing = (SeqItemInstanceNumberGenerator) pcem.find(SeqItemInstanceNumberGenerator.class, (int) 1);
        if (siing == null) {
            siing = new SeqItemInstanceNumberGenerator();
        }
        returnedvalue = siing.getNext_instance_number();
        pcem.merge(siing);
        return returnedvalue;
    }
}
