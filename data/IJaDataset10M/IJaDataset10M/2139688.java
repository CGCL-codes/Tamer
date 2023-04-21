package com.ikarkharkov.dictour.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import com.ikarkharkov.dictour.data.Category;
import com.ikarkharkov.dictour.data.Cart;
import com.ikarkharkov.dictour.data.Language;
import com.ikarkharkov.dictour.data.Phrase;
import com.ikarkharkov.dictour.db.DBService;
import com.ikarkharkov.dictour.db.HibernateUtil;
import com.ikarkharkov.dictour.PathLocator;

/**
 * This class used as startup - point for application
 * It used by index.jsp page and proceed view and search phrase actions
 */
public class PhraseListAction extends DispatchAction {

    private static final String ALL = "==ALL==";

    static {
        DBService.setHibSession(HibernateUtil.currentSession());
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return list(mapping, form, request, response);
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DBService.initDatabase();
        String audiopath = PathLocator.getInstance().getPath2audio();
        if (audiopath == null) {
            log.error("Can not load pathes from properties in PhraseListAction");
            request.setAttribute("Error", "Can not load pathes from properties");
            return mapping.findForward("error");
        }
        List<String> list_c = new ArrayList<String>();
        List<String> list_l = new ArrayList<String>();
        List<Phrase> list_p = new ArrayList<Phrase>();
        list_c.add(ALL);
        for (Category cat : DBService.listCategories()) {
            list_c.add(cat.getName());
        }
        list_l.add(ALL);
        for (Language lang : DBService.listLanguages()) {
            list_l.add(lang.getName());
        }
        for (Phrase phrase : DBService.listPhrases()) {
            File file = new File(audiopath + phrase.getId() + ".mp3");
            phrase.setFilesize((int) file.length());
            list_p.add(phrase);
        }
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        if (cart != null) {
            request.setAttribute("amount", cart.getSize());
        } else {
            request.setAttribute("amount", 0);
        }
        request.setAttribute("srch", "");
        request.setAttribute("phraseList", list_p);
        request.setAttribute("categoryList", list_c);
        request.setAttribute("languageList", list_l);
        request.setAttribute("languageList2", list_l);
        return mapping.findForward("view-all");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Phrase> list = new ArrayList<Phrase>();
        List<String> list_c = new ArrayList<String>();
        List<String> list_l = new ArrayList<String>();
        List<String> list_pl = new ArrayList<String>();
        String category = request.getParameter("category");
        String srch = request.getParameter("srch");
        String text_lang = request.getParameter("text_lang");
        String phrase_lang = request.getParameter("phrase_lang");
        for (Phrase phrase : DBService.listPhrases()) {
            if ((category.equals(phrase.getCategory().getName()) || category.equals(ALL)) && (text_lang.equals(phrase.getText_language().getName()) || text_lang.equals(ALL)) && (phrase_lang.equals(phrase.getPhrase_language().getName()) || phrase_lang.equals(ALL)) && (srch.trim().equals("") || phrase.getText().contains(srch))) {
                list.add(phrase);
            }
        }
        list_c.add(category);
        if (!category.equals(ALL)) list_c.add(ALL);
        for (Category cat : DBService.listCategories()) {
            if (!cat.getName().equals(category)) list_c.add(cat.getName());
        }
        list_l.add(text_lang);
        if (!text_lang.equals(ALL)) list_l.add(ALL);
        for (Language cat : DBService.listLanguages()) {
            if (!cat.getName().equals(text_lang)) list_l.add(cat.getName());
        }
        list_pl.add(phrase_lang);
        if (!phrase_lang.equals(ALL)) list_pl.add(ALL);
        for (Language cat : DBService.listLanguages()) {
            if (!cat.getName().equals(phrase_lang)) list_pl.add(cat.getName());
        }
        if ((Integer) request.getSession().getAttribute("userID") != null && (Integer) request.getSession().getAttribute("userID") > 0) request.setAttribute("login", "true");
        request.setAttribute("phraseList", list);
        if (srch == null || srch.equals("null")) srch = " ";
        request.setAttribute("srch", srch);
        request.setAttribute("categoryList", list_c);
        request.setAttribute("languageList", list_l);
        request.setAttribute("languageList2", list_pl);
        return mapping.findForward("view-all");
    }
}
