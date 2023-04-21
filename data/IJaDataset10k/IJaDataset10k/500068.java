package com.j2biz.blogunity.web.actions.secure.blog.resources;

import com.j2biz.blogunity.i18n.I18N;
import com.j2biz.blogunity.web.ActionResultFactory;
import com.j2biz.blogunity.web.IActionResult;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class ListImagesAction extends ListResourcesAction {

    protected IActionResult getNaviStackResult() {
        return ActionResultFactory.buildRedirect(I18N.MESSAGES.NAVI_LIST_IMAGES, currentActionPath);
    }

    protected IActionResult getSuccessRedirect() {
        return null;
    }

    protected IActionResult getErrorForward() {
        return null;
    }

    protected IActionResult getSuccessForward() {
        return ActionResultFactory.buildForward("/jsp/secure/listBlogImages.jsp");
    }

    protected int getResourceType() {
        return IMAGE;
    }
}
