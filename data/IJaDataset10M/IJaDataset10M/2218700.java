package ro.xblue.translator;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import ro.gateway.aida.db.PersistenceManager;
import ro.gateway.aida.db.PersistenceToken;
import ro.gateway.aida.servlet.Constants;
import ro.gateway.aida.srv.ICFService;
import ro.gateway.aida.srv.IIDGenerator;
import ro.gateway.aida.utils.CSVParser;

/**
 * <p>Title: Romanian AIDA</p>
 * <p>Description: :D application</p>
 * <p>Copyright: Copyright (comparator) 2003</p>
 * <p>Company: Romania Development Gateway </p>
 * @author Mihai Popoaei, mihai_popoaei@yahoo.com, smike@intellisource.ro
 * @version 1.0-* @version $Id: LoadTranslatorServlet.java,v 1.1 2004/10/24 23:37:02 mihaipostelnicu Exp $
 */
public class LoadTranslatorServlet extends HttpServlet {

    public void init() throws ServletException {
        super.init();
        ServletContext application = this.getServletContext();
        PersistenceToken token = PersistenceManager.tokenLookup(application);
        if (!loadTranslatorDB(application)) {
            System.out.println("Loading translator (file)");
            loadTranslator(application);
        } else {
            System.out.println("Loading translator (db)");
            Hashtable services = (Hashtable) application.getAttribute(Constants.SERVICES);
            IIDGenerator idgens = (IIDGenerator) services.get(Constants.SERVICE_IDGEN);
            ICFService cfService = (ICFService) services.get(Constants.SERVICE_CF);
            idgens.registerGenerator(Constants.IDGEN_TRANS_ITEMS);
            try {
                idgens.initGenerator(Constants.IDGEN_TRANS_ITEMS, 1 + TranslatorDBAccessor.getManager(token).getLastId());
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
        }
    }

    /**
       * Load interface labels from db.
       * Returns true if there is the iface_trans in db.
       *
       * @return
       */
    public static boolean loadTranslatorDB(ServletContext application) {
        PersistenceToken token = PersistenceManager.tokenLookup(application);
        Hashtable services = (Hashtable) application.getAttribute(Constants.SERVICES);
        if (services == null) {
            System.err.println("Can't load translator DB (services null)");
            return false;
        }
        ICFService cfService = (ICFService) services.get(Constants.SERVICE_CF);
        try {
            if (!TranslatorDBAccessor.getManager(token).checkTranslatorTable()) return false;
        } catch (SQLException ex) {
        }
        Translator_DB translator = Translator_DB.getManager(token);
        application.setAttribute(TranslatorConstants.DB_TRANSLATOR, translator);
        LanguageBean def_lang = new LanguageBean();
        String sdef_lang = translator.getMessage("en", "translator", "lng.default");
        if (sdef_lang == null) {
            sdef_lang = "en";
        }
        def_lang.setLanguage(sdef_lang);
        application.setAttribute(TranslatorConstants.BLUE_DEF_LANG, def_lang);
        ArrayList list = new ArrayList();
        Hashtable tr_items = translator.getModule("translator").getMessages("en");
        String avail_langs = "en,ro";
        if (tr_items != null) {
            avail_langs = ((TransItem) tr_items.get("lng.avail")).getText();
        }
        String[] langs = CSVParser.decode(avail_langs);
        if ((langs != null) && (langs.length > 0)) {
            for (int i = 0; i < langs.length; i++) {
                LanguageBean lbean = new LanguageBean();
                lbean.setLanguage(langs[i]);
                if (!langs[i].equals(lbean.locale.getDisplayLanguage())) {
                    list.add(lbean);
                }
            }
        }
        if (list.size() < 1) {
            list.add(new LanguageBean("ro"));
            list.add(new LanguageBean("en"));
        }
        LanguageBean[] avail_languages = new LanguageBean[list.size()];
        list.toArray(avail_languages);
        application.setAttribute(TranslatorConstants.BLUE_AVAIL_LANGS, avail_languages);
        return true;
    }

    public static void loadTranslator(ServletContext application) {
        File translatorDir = new File(application.getRealPath("WEB-INF/translator"));
        if (!translatorDir.exists()) {
            translatorDir.mkdir();
        }
        Translator_DB translator_db = (Translator_DB) application.getAttribute(TranslatorConstants.DB_TRANSLATOR);
        LanguageBean def_lang = new LanguageBean();
        String sdef_lang = translator_db.getMessage("en", "translator", "lng.default");
        if (sdef_lang == null) {
            sdef_lang = "en";
        }
        def_lang.setLanguage(sdef_lang);
        application.setAttribute(TranslatorConstants.BLUE_DEF_LANG, def_lang);
        ArrayList list = new ArrayList();
        Hashtable props = translator_db.getModule("translator").getMessages("en");
        String avail_langs = "en,ro";
        if (props != null) {
            avail_langs = (String) props.get("lng.avail");
        }
        String[] langs = CSVParser.decode(avail_langs);
        if ((langs != null) && (langs.length > 0)) {
            for (int i = 0; i < langs.length; i++) {
                LanguageBean lbean = new LanguageBean();
                lbean.setLanguage(langs[i]);
                if (!langs[i].equals(lbean.locale.getDisplayLanguage())) {
                    list.add(lbean);
                }
            }
        }
        if (list.size() < 1) {
            list.add(new LanguageBean("ro"));
            list.add(new LanguageBean("en"));
        }
        LanguageBean[] avail_languages = new LanguageBean[list.size()];
        list.toArray(avail_languages);
        application.setAttribute(TranslatorConstants.BLUE_AVAIL_LANGS, avail_languages);
    }
}
