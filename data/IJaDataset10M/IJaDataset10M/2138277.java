package net.sf.woko.facets.guest;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;
import net.sf.woko.facets.IResolutionFacet;
import net.sf.woko.facets.FacetConstants;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

/**
 * Home Page Resolution facet. 
 * This one returns a Stripes <code>Resolution</code> 
 * object that is used in order to show the user home page.
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : homePageResolution</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name = FacetConstants.homePageResolution, profileId = "ROLE_WOKO_GUEST")
public class HomePageResolution extends BaseFacet implements IResolutionFacet {

    /**
	 * returns a Stripes <code>ForwardResolution</code> 
	 * object that is used in order to show the user home page :
	 * <code>/WEB-INF/woko/home-page-guest.jsp</code>
	 */
    public Resolution getResolution() {
        return new ForwardResolution("/WEB-INF/woko/home-page-guest.jsp");
    }
}
