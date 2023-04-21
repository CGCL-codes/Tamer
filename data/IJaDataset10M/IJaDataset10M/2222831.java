package org.localstorm.mcc.web.gtd.actions;

import org.localstorm.mcc.web.gtd.GtdBaseActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import org.localstorm.mcc.ejb.gtd.RefObjectManager;
import org.localstorm.mcc.ejb.gtd.entity.ReferencedObject;
import org.localstorm.mcc.web.gtd.GtdSessionKeys;
import org.localstorm.mcc.web.util.SessionUtil;
import org.localstorm.tools.aop.runtime.Logged;

/**
 * @author Alexey Kuznetsov
 */
@UrlBinding("/actions/gtd/ctx/obj/ToggleStateRefObj")
public class RefObjToggleStateActionBean extends GtdBaseActionBean {

    @Validate(required = true)
    private int objectId;

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    @DefaultHandler
    @Logged
    public Resolution toggle() throws Exception {
        RefObjectManager rom = super.getRefObjectManager();
        ReferencedObject ro = rom.findById(this.getObjectId());
        ro.setArchived(!ro.isArchived());
        rom.update(ro);
        SessionUtil.clear(getSession(), GtdSessionKeys.REFERENCE_OBJECTS);
        return new RedirectResolution(RefObjEditActionBean.class);
    }
}
