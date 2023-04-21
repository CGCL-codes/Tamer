package com.bocoon.app.cms.front.directive;

import static com.bocoon.app.cms.Constants.TPL_STYLE_LIST;
import static com.bocoon.app.cms.Constants.TPL_SUFFIX;
import static com.bocoon.app.cms.utils.FrontUtils.PARAM_STYLE_LIST;
import static com.bocoon.common.web.Constants.UTF8;
import static com.bocoon.common.web.freemarker.DirectiveUtils.OUT_LIST;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.bocoon.app.cms.front.directive.abs.AbstractCmsGuestbookDirective;
import com.bocoon.app.cms.utils.FrontUtils;
import com.bocoon.common.web.freemarker.DirectiveUtils;
import com.bocoon.common.web.freemarker.ParamsRequiredException;
import com.bocoon.common.web.freemarker.DirectiveUtils.InvokeType;
import com.bocoon.entity.cms.assist.CmsGuestbook;
import com.bocoon.entity.cms.main.CmsSite;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 评论列表标签
 * 
 * @author liufang
 * 
 */
public class CmsGuestbookListDirective extends AbstractCmsGuestbookDirective {

    /**
	 * 模板名称
	 */
    public static final String TPL_NAME = "guestbook_list";

    /**
	 * 输入参数，内容ID。
	 */
    public static final String PARAM_SITE_ID = "siteId";

    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        CmsSite site = FrontUtils.getSite(env);
        int first = FrontUtils.getFirst(params);
        int max = FrontUtils.getCount(params);
        List<CmsGuestbook> list = cmsGuestbookMng.getList(getSiteId(params), getCtgId(params), getRecommend(params), getChecked(params), getDesc(params), true, first, max);
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put(OUT_LIST, DEFAULT_WRAPPER.wrap(list));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        InvokeType type = DirectiveUtils.getInvokeType(params);
        String listStyle = DirectiveUtils.getString(PARAM_STYLE_LIST, params);
        if (InvokeType.sysDefined == type) {
            if (StringUtils.isBlank(listStyle)) {
                throw new ParamsRequiredException(PARAM_STYLE_LIST);
            }
            env.include(TPL_STYLE_LIST + listStyle + TPL_SUFFIX, UTF8, true);
        } else if (InvokeType.userDefined == type) {
            if (StringUtils.isBlank(listStyle)) {
                throw new ParamsRequiredException(PARAM_STYLE_LIST);
            }
            FrontUtils.includeTpl(TPL_STYLE_LIST, site, env);
        } else if (InvokeType.custom == type) {
            FrontUtils.includeTpl(TPL_NAME, site, params, env);
        } else if (InvokeType.body == type) {
            body.render(env.getOut());
        } else {
            throw new RuntimeException("invoke type not handled: " + type);
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
    }
}
