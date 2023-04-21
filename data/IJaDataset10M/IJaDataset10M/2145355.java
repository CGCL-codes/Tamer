package com.ikarkharkov.dictour.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.ikarkharkov.dictour.data.Phrase;
import com.ikarkharkov.dictour.db.DBService;

/**
 * Class used for checking authorization for runs at upload page
 */
public class UploadAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getSession().getAttribute("user") == null) {
            return mapping.findForward("denied");
        }
        PhraseForm pform = (PhraseForm) form;
        if (pform != null && pform.getId() > 0) {
            Phrase phrase = DBService.loadPhrase(pform.getId());
            pform.setPhrase(phrase);
        }
        request.setAttribute("categoryList", DBService.listCategories());
        request.setAttribute("languageList", DBService.listLanguages());
        return mapping.findForward("upload-page");
    }
}
