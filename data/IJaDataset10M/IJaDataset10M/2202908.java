package net.sf.woko.facets.command;

import net.sf.woko.facets.BaseFacet;
import net.sf.woko.facets.ICommandFacet;
import net.sf.woko.usermgt.IWokoUserManager;
import net.sf.woko.util.Util;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.ValidationErrors;

@FacetKey(name = "toString", profileId = IWokoUserManager.ROLE_WOKO_USER, targetObjectType = Object.class)
public class ToStringCommand extends BaseFacet implements ICommandFacet {

    public void validateInputs(ValidationErrors errors) {
    }

    public Resolution execute(ActionBeanContext actionBeanContext) {
        String toString = getTargetObject().toString();
        actionBeanContext.getMessages().add(new SimpleMessage("toString() = " + toString));
        return Util.redirectToView(getTargetObject(), getPersistenceUtil());
    }

    public boolean isAjaxUpdate() {
        return false;
    }
}
