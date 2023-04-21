package com.liferay.portlet.words.action;

import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.words.ScramblerException;
import com.liferay.portlet.words.util.WordsUtil;
import com.liferay.util.servlet.SessionErrors;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="ViewAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ViewAction extends PortletAction {

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        try {
            String cmd = ParamUtil.getString(req, Constants.CMD);
            if (cmd.equals(Constants.SEARCH)) {
                String word = ParamUtil.getString(req, "word");
                boolean scramble = ParamUtil.getBoolean(req, "scramble");
                String[] words = null;
                if (scramble) {
                    words = WordsUtil.scramble(word);
                } else {
                    words = WordsUtil.unscramble(word);
                }
                req.setAttribute(WebKeys.WORDS_LIST, words);
            }
        } catch (ScramblerException se) {
            SessionErrors.add(req, se.getClass().getName());
        }
        return mapping.findForward("portlet.words.view");
    }
}
