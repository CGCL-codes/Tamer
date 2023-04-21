package org.jprovocateur.businesslayer.servicesImpl.accessrights;

import org.jprovocateur.businesslayer.businesslogic.BasisLogic;
import org.jprovocateur.businesslayer.objectmodel.accessrights.BasisClass;
import org.jprovocateur.error.BasisException;

/**
 * <p/>
 *
 * @author Michael Pitsounis 
 */
public class BcDisableEnableBC extends BasisLogic {

    public Object initializer(Object data) throws Exception {
        BasisClass bc = (BasisClass) data;
        bc = (BasisClass) super.initializer(data);
        bc.getAccessRights().size();
        return bc;
    }

    public Object realization(Object data) throws BasisException {
        BasisClass bc = (BasisClass) data;
        if (bc.getDisabled().equals(new Long(0))) {
            bc.setDisabled(new Long(1));
        } else {
            bc.setDisabled(new Long(0));
        }
        super.realization(bc);
        return bc;
    }
}
