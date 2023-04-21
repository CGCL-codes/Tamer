package it.battlehorse.rcp.tools.log.internal;

import it.battlehorse.rcp.sl.ServiceException;
import it.battlehorse.rcp.sl.ServiceFactoryAdapter;
import it.battlehorse.rcp.tools.log.dialogs.DetailHelper;

/**
 * A factory which serves {@link DetailHelper} instances.
 * @author battlehorse
 * @since May 7, 2006
 */
public class DetailHelperFactory extends ServiceFactoryAdapter {

    @Override
    public Object getServiceInstance() throws ServiceException {
        return new DetailHelper();
    }
}
