package org.yafra.wicket.admin;

import java.util.List;
import org.apache.cayenne.BaseContext;
import org.apache.cayenne.access.DataContext;
import org.apache.wicket.model.LoadableDetachableModel;
import org.yafra.model.MYafraUser;
import org.yafra.modelhandler.MHYafraUser;

/**
 * description
 *
 * @author mwn
 * @version 
 * @since 1.0
 */
public class YafraUserDM extends LoadableDetachableModel<List<MYafraUser>> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3724988370581514193L;

    /**
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
    @Override
    protected List<MYafraUser> load() {
        try {
            AdminApplication.getLogging().logInfo("internet - query users as loadable detachable model");
            MHYafraUser mhyu = new MHYafraUser((DataContext) BaseContext.getThreadObjectContext(), AdminApplication.getLogging());
            return mhyu.getMUsers();
        } catch (IndexOutOfBoundsException e) {
            AdminApplication.getLogging().logError("no entries found - returning null", e);
            return null;
        }
    }
}
