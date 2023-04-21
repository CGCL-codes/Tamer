package org.jprovocateur.basis.businesslayer.accessrights;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import org.jprovocateur.basis.error.BasisError;
import org.jprovocateur.basis.error.BasisErrorDef;
import org.jprovocateur.basis.error.BasisException;
import org.jprovocateur.basis.objectmodel.accessrights.impl.AccessRights;
import org.jprovocateur.basis.objectmodel.accessrights.impl.BasisClass;
import org.jprovocateur.basis.serviceslayer.BusinessLogicInt;
import org.jprovocateur.basis.serviceslayer.impl.BusinessLogic;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p/>
 * 
 * @author Michael Pitsounis
 */
@Service("bcCreateService")
@Scope("prototype")
@Transactional(readOnly = false)
public class BCCreateService extends BusinessLogic<BasisClass> {

    public String getExecutionType() {
        return BusinessLogicInt.CREATE;
    }

    public void initMetadata(BasisClass bc) throws BasisException {
        this.metaData.setCascadeUpdate(true);
        this.metaData.setCascadeCreate(true);
    }

    public void validate(BasisClass bc) throws BasisException {
        super.validate(bc);
        List<BasisError> errorsHash = new ArrayList<BasisError>();
        errorsHash.addAll(super.validate(bc, false));
        Session sess = getDao().getSession();
        String SQLRequest = "SELECT service.* FROM Service service " + "WHERE service.classname = '" + bc.getClassname() + "'";
        List returnList = (List) sess.createSQLQuery(SQLRequest).addEntity(BasisClass.class).list();
        if (returnList.size() > 0) {
            BasisError error = new BasisError("msg for the developer : service already exists", BasisErrorDef.CLASS_ALREADY_EXISTS_EXCEPTION, BasisError.BUSINESS, this, "classname", null);
            errorsHash.add(error);
        }
        try {
            Class.forName(bc.getClassname());
        } catch (Exception e) {
            BasisError error = new BasisError("msg for the developer", BasisErrorDef.CLASS_NOT_FOUND_EXCEPTION, BasisError.BUSINESS, this, "classname", null);
            errorsHash.add(error);
        }
        if (errorsHash.size() > 0) {
            throw new BasisException(errorsHash);
        }
    }

    public Object execute(BasisClass bc) throws BasisException {
        Set accessRights = (Set) bc.getAccessRights();
        Iterator iterAccessRights = accessRights.iterator();
        while (iterAccessRights.hasNext()) {
            AccessRights accessRight = (AccessRights) iterAccessRights.next();
            accessRight.setBc(bc);
        }
        return super.execute(bc);
    }
}
