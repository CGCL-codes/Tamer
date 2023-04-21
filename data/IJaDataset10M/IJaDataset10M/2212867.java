package org.jprovocateur.basis.businesslayer.accessrights;

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
@Service("accessRightsAddService")
@Scope("prototype")
@Transactional(readOnly = false)
public class AccessRightsAddService extends BusinessLogic<BasisClass> {

    public String getExecutionType() {
        return BusinessLogicInt.DELETE;
    }
}
