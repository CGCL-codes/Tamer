package net.sf.woko.renderer.view;

import net.sf.woko.renderer.util.BaseForwardCommandFacet;
import net.sf.woko.usermgt.WokoRole;
import net.sourceforge.jfacets.annotations.FacetKey;

@FacetKey(name = ViewObject.FACET_NAME, profileId = WokoRole.ROLE_WOKO_USER)
public class ViewObjectImpl extends BaseForwardCommandFacet implements ViewObject {

    public static final String JSP = "/WEB-INF/woko/view-object.jsp";

    protected String getJsp() {
        return JSP;
    }
}
