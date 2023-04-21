package net.itsite.portal.bookmark;

import java.util.Map;
import net.itsite.portal.MyPortalUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

public class BookmarkTablePagerAction extends AbstractAjaxRequestHandle {

    public IForward doExport(final ComponentParameter compParameter) {
        return jsonForward(compParameter, new JsonCallback() {

            @Override
            public void doAction(final Map<String, Object> json) throws Exception {
                final String dl = MyPortalUtils.resource + BookmarkUtils.deploy + "/bookmark_export_dl.jsp?" + UrlForward.putRequestData(compParameter, "p");
                json.put("dl", dl);
            }
        });
    }
}
