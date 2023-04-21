package org.jprovocateur.basis.businesslayer.tree;

import org.jprovocateur.basis.error.BasisException;
import org.jprovocateur.basis.objectmodel.tree.AccessRightsTree;
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
@Service("accessRightsTreeDisableEnableService")
@Scope("prototype")
@Transactional(readOnly = false)
public class AccessRightsTreeDisableEnableService extends BusinessLogic<AccessRightsTree> {

    public String getExecutionType() {
        return BusinessLogicInt.UPDATE;
    }

    public Object execute(AccessRightsTree art) throws BasisException {
        if (art.getDisabled().equals(new Long(0))) {
            art.setDisabled(new Long(1));
        } else {
            art.setDisabled(new Long(0));
        }
        super.execute(art);
        return art;
    }
}
