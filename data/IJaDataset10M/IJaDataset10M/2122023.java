package com.bocoon.app.cms.front.directive;

import static com.bocoon.app.cms.Constants.TPL_STYLE_LIST;
import static com.bocoon.app.cms.Constants.TPL_SUFFIX;
import static com.bocoon.app.cms.utils.FrontUtils.PARAM_STYLE_LIST;
import static com.bocoon.common.web.Constants.UTF8;
import static com.bocoon.common.web.freemarker.DirectiveUtils.OUT_LIST;
import static com.bocoon.common.web.freemarker.DirectiveUtils.OUT_PAGINATION;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.bocoon.app.cms.front.directive.abs.AbstractContentDirective;
import com.bocoon.app.cms.utils.FrontUtils;
import com.bocoon.common.page.Pagination;
import com.bocoon.common.web.freemarker.DirectiveUtils;
import com.bocoon.common.web.freemarker.ParamsRequiredException;
import com.bocoon.common.web.freemarker.DirectiveUtils.InvokeType;
import com.bocoon.entity.cms.main.CmsSite;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 内容分页标签
 * 
 * @author liufang
 * 
 */
public class ContentPageDirective extends AbstractContentDirective {

    /**
	 * 模板名称
	 */
    public static final String TPL_NAME = "content_page";

    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        CmsSite site = FrontUtils.getSite(env);
        Pagination page = (Pagination) super.getData(params, env);
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put(OUT_PAGINATION, DEFAULT_WRAPPER.wrap(page));
        paramWrap.put(OUT_LIST, DEFAULT_WRAPPER.wrap(page.getList()));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        InvokeType type = DirectiveUtils.getInvokeType(params);
        String listStyle = DirectiveUtils.getString(PARAM_STYLE_LIST, params);
        if (InvokeType.sysDefined == type) {
            if (StringUtils.isBlank(listStyle)) {
                throw new ParamsRequiredException(PARAM_STYLE_LIST);
            }
            env.include(TPL_STYLE_LIST + listStyle + TPL_SUFFIX, UTF8, true);
            FrontUtils.includePagination(site, params, env);
        } else if (InvokeType.userDefined == type) {
            if (StringUtils.isBlank(listStyle)) {
                throw new ParamsRequiredException(PARAM_STYLE_LIST);
            }
            FrontUtils.includeTpl(TPL_STYLE_LIST, site, env);
            FrontUtils.includePagination(site, params, env);
        } else if (InvokeType.custom == type) {
            FrontUtils.includeTpl(TPL_NAME, site, params, env);
            FrontUtils.includePagination(site, params, env);
        } else if (InvokeType.body == type) {
            if (body != null) {
                body.render(env.getOut());
                FrontUtils.includePagination(site, params, env);
            }
        } else {
            throw new RuntimeException("invoke type not handled: " + type);
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
    }

    @Override
    protected boolean isPage() {
        return true;
    }
}
