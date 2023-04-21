package edu.mit.lcs.haystack.ozone.core;

import edu.mit.lcs.haystack.rdf.*;

/**
 * Interface exposed by objects that can navigate to different resources.
 * @version 	1.0
 * @author		Dennis Quan
 * @author		David Huynh
 */
public interface INavigationMaster {

    /**
	 * Requests a viewing of the given resource. The navigation
	 * master chooses the appropriate view navigator to perform
	 * the required navigation.
	 */
    public void requestViewing(Resource res);

    /**
	 * Requests a viewing of the given resource. The navigation
	 * master chooses the appropriate view navigator to perform
	 * the required navigation.
	 */
    public void requestViewing(Resource res, Resource viewInstance);

    /**
	 * Registers a view navigator with the given id and returns
	 * a cookie.
	 */
    public Object registerViewNavigator(Resource id, IViewNavigator vn);

    /**
	 * Unregisters a view navigator given its id.
	 */
    public void unregisterViewNavigator(Object cookie);

    /**
	 * Retrieves a view navigator given its id.
	 */
    public IViewNavigator getViewNavigator(Resource id);
}
