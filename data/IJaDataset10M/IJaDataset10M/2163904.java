package net.sf.woko.renderer.edit;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.renderer.util.BaseForwardCommandFacet;
import net.sf.woko.usermgt.WokoRole;

@FacetKey(name = EditObject.FACET_NAME, profileId = WokoRole.ROLE_WOKO_USER)
public class EditObjectImpl extends BaseForwardCommandFacet implements EditObject {

    public static final String JSP = "/WEB-INF/woko/edit-object.jsp";

    protected String getJsp() {
        return JSP;
    }
}
