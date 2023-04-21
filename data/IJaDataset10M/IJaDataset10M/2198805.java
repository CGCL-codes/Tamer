package com.controltier.ctl.common.creationhandler;

import com.controltier.ctl.common.creationhandler.AbstractCreationHandler;
import com.controltier.ctl.common.IObject;
import com.controltier.ctl.common.DepotObject;
import com.controltier.ctl.common.IDepotMgr;

/**
 * Default handler to create any {@link com.controltier.ctl.common.DepotObject} instance
 * <p/>
 * ControlTier Software Inc.
 * User: alexh
 * Date: Jul 18, 2005
 * Time: 7:03:47 PM
 */
public class DefaultCreationHandler extends AbstractCreationHandler {

    public DefaultCreationHandler() {
    }

    /**
     * Calls {@link com.controltier.ctl.common.IObject#isObjectContext()} to check if the context is object level. Doesn't
     * try to match on specific type or entity name.
     *
     * @param obj
     * @return
     */
    public boolean isInContext(final IObject obj) {
        return obj.isObjectContext();
    }

    /**
     * Calls  ...
     *
     * @param obj
     * @param depotResourceMgr
     * @return
     */
    public DepotObject create(final IObject obj, final IDepotMgr depotResourceMgr) {
        return DepotObject.create(obj, depotResourceMgr);
    }
}
