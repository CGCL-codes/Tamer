package cn.vlabs.duckling.vwb.tags;

import javax.servlet.jsp.JspException;
import cn.vlabs.duckling.vwb.VWBSite;
import cn.vlabs.duckling.vwb.services.auth.permissions.PagePermission;
import cn.vlabs.duckling.vwb.services.render.DPageRendable;

/**
 * Introduction Here.
 * @date Mar 11, 2010
 * @author xiejj@cnic.cn
 */
public class FooterTag extends RenderTag {

    private static final long serialVersionUID = 1L;

    @Override
    public int doVWBStart() throws JspException {
        if (content instanceof DPageRendable) {
            VWBSite site = vwbcontext.getSite();
            if (!site.checkPermission(vwbcontext.getVWBSession(), new PagePermission(Integer.toString(content.getId()), PagePermission.VIEW_ACTION))) {
                content = new DPageRendable(VWBSite.DEFAULT_FOOTER);
            }
        }
        return super.doVWBStart();
    }
}
