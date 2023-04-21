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
@Service("accessRightsTreeRemoveService")
@Scope("prototype")
@Transactional(readOnly = false)
public class AccessRightsTreeRemoveService extends BusinessLogic<AccessRightsTree> {

    public String getExecutionType() {
        return BusinessLogicInt.DELETE;
    }

    public void validate(AccessRightsTree data) throws BasisException {
    }
}
