package com.liferay.portlet.journal.action;

import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.journal.model.impl.JournalTemplateImpl;
import com.liferay.portlet.journal.util.JournalUtil;
import com.liferay.util.JS;
import com.liferay.util.servlet.ServletResponseUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="GetTemplateContentAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class GetTemplateContentAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String xslContent = JS.decodeURIComponent(ParamUtil.getString(request, "xslContent"));
            boolean formatXsl = ParamUtil.getBoolean(request, "formatXsl");
            String langType = ParamUtil.getString(request, "langType", JournalTemplateImpl.LANG_TYPE_XSL);
            if (formatXsl) {
                if (langType.equals(JournalTemplateImpl.LANG_TYPE_VM)) {
                    xslContent = JournalUtil.formatVM(xslContent);
                } else {
                    xslContent = JournalUtil.formatXML(xslContent);
                }
            }
            String fileName = "template." + langType;
            byte[] bytes = xslContent.getBytes();
            ServletResponseUtil.sendFile(response, fileName, bytes, ContentTypes.TEXT_XML_UTF8);
            return null;
        } catch (Exception e) {
            PortalUtil.sendError(e, request, response);
            return null;
        }
    }
}
