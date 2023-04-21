package net.simpleframework.content.component.remark;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleframework.web.page.component.ui.validatecode.DefaultValidateCodeHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class RemarkAction extends AbstractAjaxRequestHandle {

    public IForward saveRemark(final ComponentParameter compParameter) {
        final ComponentParameter nComponentParameter = RemarkUtils.getComponentParameter(compParameter);
        final IRemarkHandle rHandle = (IRemarkHandle) nComponentParameter.getComponentHandle();
        return jsonForward(compParameter, new JsonCallback() {

            @Override
            public void doAction(final Map<String, Object> json) {
                if ((Boolean) nComponentParameter.getBeanProperty("showValidateCode") && !DefaultValidateCodeHandle.isValidateCode(compParameter.request, "textRemarkHtmlEditorValidateCode")) {
                    json.put("validateCode", DefaultValidateCodeHandle.getErrorString());
                } else {
                    final Map<String, Object> data = new HashMap<String, Object>();
                    data.put("content", compParameter.getRequestParameter("textareaRemarkHtmlEditor"));
                    final String itemId = compParameter.getRequestParameter("itemId");
                    if (StringUtils.hasText(itemId)) {
                        rHandle.doEdit(nComponentParameter, itemId, data);
                    } else {
                        data.put("parentId", compParameter.getRequestParameter("parentId"));
                        rHandle.doAdd(nComponentParameter, data);
                    }
                }
            }
        });
    }

    public IForward save2Remark(final ComponentParameter compParameter) {
        final ComponentParameter nComponentParameter = RemarkUtils.getComponentParameter(compParameter);
        final IRemarkHandle rHandle = (IRemarkHandle) nComponentParameter.getComponentHandle();
        return jsonForward(compParameter, new JsonCallback() {

            @Override
            public void doAction(final Map<String, Object> json) {
                if ((Boolean) nComponentParameter.getBeanProperty("showValidateCode") && !DefaultValidateCodeHandle.isValidateCode(compParameter.request, "textRemarkEditorValidateCode")) {
                    json.put("validateCode", DefaultValidateCodeHandle.getErrorString());
                } else {
                    final Map<String, Object> data = new HashMap<String, Object>();
                    data.put("content", ContentUtils.doTextContent(compParameter.getRequestParameter("textareaRemarkEditor")));
                    rHandle.doAdd(nComponentParameter, data);
                }
            }
        });
    }

    public IForward doSupport(final ComponentParameter compParameter) {
        return doSupportOpposition(compParameter, true);
    }

    public IForward doOpposition(final ComponentParameter compParameter) {
        return doSupportOpposition(compParameter, false);
    }

    private IForward doSupportOpposition(final ComponentParameter compParameter, final boolean support) {
        final String itemId = compParameter.getRequestParameter("itemId");
        final String sessionKey = "remark_support_opposition_" + itemId;
        final HttpSession httpSession = compParameter.getSession();
        if (Boolean.TRUE.equals(httpSession.getAttribute(sessionKey))) {
            throw HandleException.wrapException(LocaleI18n.getMessage("RemarkAction.0"));
        }
        final ComponentParameter nComponentParameter = RemarkUtils.getComponentParameter(compParameter);
        final IRemarkHandle rHandle = (IRemarkHandle) nComponentParameter.getComponentHandle();
        final RemarkItem remark = rHandle.doSupportOpposition(nComponentParameter, itemId, support);
        return jsonForward(compParameter, new JsonCallback() {

            @Override
            public void doAction(final Map<String, Object> json) {
                httpSession.setAttribute(sessionKey, Boolean.TRUE);
                json.put("k", "remark_" + (support ? "support" : "opposition") + "_" + remark.getId());
                json.put("v", "(" + (support ? remark.getSupport() : remark.getOpposition()) + ")");
            }
        });
    }

    public IForward doDelete(final ComponentParameter compParameter) {
        final ComponentParameter nComponentParameter = RemarkUtils.getComponentParameter(compParameter);
        final IRemarkHandle rHandle = (IRemarkHandle) nComponentParameter.getComponentHandle();
        return jsonForward(compParameter, new JsonCallback() {

            @Override
            public void doAction(final Map<String, Object> json) {
                rHandle.doDelete(nComponentParameter, new ExpressionValue("id=?", new Object[] { compParameter.getRequestParameter("itemId") }));
            }
        });
    }

    @Override
    public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
        if ("selector".equals(beanProperty)) {
            final ComponentParameter nComponentParameter = RemarkUtils.getComponentParameter(compParameter);
            final String selector = (String) nComponentParameter.getBeanProperty("selector");
            final String componentName = compParameter.componentBean.getName();
            if ("ajaxSaveRemark".equals(componentName)) {
                return selector + ", #formRemarkHtmlEditor";
            } else if ("ajaxSave2Remark".equals(componentName)) {
                return selector + ", #remark_tb_" + nComponentParameter.componentBean.hashId();
            } else {
                return selector;
            }
        }
        return super.getBeanProperty(compParameter, beanProperty);
    }
}
