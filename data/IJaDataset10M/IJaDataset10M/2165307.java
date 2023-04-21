package org.jcompany.control.helper;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.jcompany.config.PlcConfigControlHelper;
import org.jcompany.config.PlcConfigHelper;
import org.jcompany.config.control.geral.PlcConfigGroupControlApplication;
import org.jcompany.control.PlcConstants;

/**
 * jCompany 2.0. Auxiliar para internacionaliza��o
 * @since jCompany 2.0
 * @version $Id: PlcLocaleHelper.java,v 1.4 2006/06/21 19:12:25 bruno_grossi Exp $
 * @author alvim
 */
public class PlcLocaleHelper {

    private static PlcLocaleHelper INSTANCE = new PlcLocaleHelper();

    private PlcLocaleHelper() {
    }

    public static PlcLocaleHelper getInstance() {
        return INSTANCE;
    }

    protected static Logger log = Logger.getLogger(PlcLocaleHelper.class);

    /**
     * Recebe a sess�o corrente e disponibiliza o Bundle default ou selecionado
     * para JSTL
     * @param session
     */
    public void makeAvailableLocaleJSTL(HttpSession session, String localeS, Locale locale) {
        if (log.isDebugEnabled()) log.debug("######## Entrou em disponibilizaLocaleJSTL " + locale.getDisplayCountry() + " e " + locale.getLanguage());
        session.setAttribute(Globals.LOCALE_KEY, locale);
        session.setAttribute("localeKeyPlc", locale);
        java.util.ResourceBundle bundle = null;
        bundle = java.util.ResourceBundle.getBundle("ApplicationResources", locale);
        try {
            PlcLocalizationContextWrapper lc = new PlcLocalizationContextWrapper(bundle);
            javax.servlet.jsp.jstl.core.Config.set(session, javax.servlet.jsp.jstl.core.Config.FMT_LOCALIZATION_CONTEXT, lc);
        } catch (IllegalArgumentException e) {
            log.warn("Nao foi possivel inicializar o contexto jstl na sessao. Provavelmente porque o Tomcat 5.5.x adiante nao aceita objetos nao serializaveis na sessao. Desprezar se for o caso");
            e.printStackTrace();
        }
        javax.servlet.jsp.jstl.core.Config.set(session, javax.servlet.jsp.jstl.core.Config.FMT_LOCALE, locale);
        makeAvailableFormats(session, bundle);
    }

    /**
     * jCompany 2.0. Se n�o houver locale ou bundle default na sess�o corrente, utiliza bundle sem extens�o
     * e locale pt_BR.
     * @param session
     */
    public void makeAvailableLocaleDefault(HttpSession session) {
        log.debug("######## Entrou em disponibilizaLocalePadrao");
        if (javax.servlet.jsp.jstl.core.Config.get(session, javax.servlet.jsp.jstl.core.Config.FMT_LOCALIZATION_CONTEXT) == null) {
            Locale locale = new Locale("pt", "BR");
            java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("ApplicationResources", locale);
            try {
                PlcLocalizationContextWrapper lc = new PlcLocalizationContextWrapper(bundle);
                javax.servlet.jsp.jstl.core.Config.set(session, javax.servlet.jsp.jstl.core.Config.FMT_LOCALIZATION_CONTEXT, lc);
            } catch (IllegalArgumentException e) {
                log.warn("Nao foi possivel inicializar o contexto jstl na sessao. " + "Provavelmente porque o Tomcat 5.5.x adiante nao aceita objetos nao " + "serializaveis na sessao. Desprezar se for o caso");
                e.printStackTrace();
            }
            session.setAttribute(Globals.LOCALE_KEY, locale);
            makeAvailableFormats(session, bundle);
        }
        if (session.getAttribute("localeKeyPlc") == null) session.setAttribute("localeKeyPlc", new Locale("pt", "BR"));
    }

    /**
     * Disponibiliza formatos conforme idioma
     * @param session
     * @param bundle Arquivo contendo chaves padr�es para formato de data e dinheiro
     */
    private void makeAvailableFormats(HttpSession session, ResourceBundle bundle) {
        log.debug("######## Entrou em disponibilizaFormatos");
        try {
            session.setAttribute("jcompanyFormatoData", bundle.getString("jcompany.formato.data"));
        } catch (Exception e) {
            session.setAttribute("jcompanyFormatoData", "dd/MM/yyyy");
        }
        try {
            session.setAttribute("jcompanyFormatoDataHora", bundle.getString("jcompany.formato.data.hora"));
        } catch (Exception e) {
            session.setAttribute("jcompanyFormatoDataHora", "dd/MM/yyyy HH:mm");
        }
    }

    /**
 	 * Configura o idioma �nico para mensagens da aplica��o de acordo com o par�metro localeUnico definido no web.xml
 	 * @param httpSession Sess�o da aplica��o
 	 */
    public void configureUniqueIdiom(HttpSession httpSession) {
        log.debug("Vai configurar idioma unico");
        String idiomaUnico = null;
        if (idiomaUnico == null) {
            try {
                PlcConfigGroupControlApplication appConfig = PlcConfigHelper.getInstance().get(PlcConfigGroupControlApplication.class);
                if ((appConfig != null && appConfig.internacionalization().localeUnique())) idiomaUnico = appConfig.internacionalization().idioms()[0];
            } catch (Exception e) {
            }
        }
        if (idiomaUnico != null && !idiomaUnico.equals("#")) {
            setApplicationIdiom(httpSession, mountLocale(idiomaUnico));
            makeAvailableLocaleJSTL(httpSession, idiomaUnico, mountLocale(idiomaUnico));
        } else {
            if (httpSession.getAttribute(Globals.LOCALE_KEY) == null) httpSession.setAttribute(Globals.LOCALE_KEY, new Locale("pt", "BR"));
            makeAvailableLocaleJSTL(httpSession, "pt_BR", new Locale("pt", "BR"));
        }
    }

    /**
 	 * Seta o idioma da aplica��o.
 	 * @param httpSession Sess�o da aplica��o
 	 * @param idiom Novo idioma para a aplica��o
 	 */
    public void setApplicationIdiom(HttpSession httpSession, String idiom) {
        if (idiom != null && idiom.equals("")) httpSession.setAttribute(Globals.LOCALE_KEY, mountLocale(idiom));
    }

    /**
 	 * Seta o idioma da aplica��o.
 	 * @param httpSession Sess�o da aplica��o
 	 * @param locale Novo objeto Locale para a aplica��o.
	 */
    public void setApplicationIdiom(HttpSession httpSession, Locale locale) {
        if (log.isDebugEnabled()) log.debug("vai incluir idioma no session listener com " + locale.getCountry() + "_" + locale.getLanguage());
        if (locale != null) httpSession.setAttribute(Globals.LOCALE_KEY, locale);
    }

    /**
 	 * Configura o locale da aplicac�o de acordo com o par�metro localeUnico definido no web.xml.<br>
 	 * @param servletContext Contexto da aplicacao
 	 * TODO Rever nome do m�todo da aplica��o.
 	 */
    public void configureUniqueLocaleApplication(ServletContext servletContext) {
        log.debug("Vai configurar locale unico para aplicacao");
        String idiomas = PlcConfigControlHelper.getInstance().get(PlcConstants.CONTEXTPARAM.INI_IDIOMS);
        if (StringUtils.isNotBlank(idiomas) && !idiomas.equals("#")) {
            String[] _idiomas = idiomas.split("\\s*,\\s*");
            setApplicationLocale(_idiomas[0]);
        } else {
            setApplicationLocale("pt_BR");
        }
    }

    /**
 	 * Seta o locale da aplica��o.
 	 * @param locale Novo locale para a aplica��o. Exemplo: pt_BR, en_US, es_ES.
 	 */
    public void setApplicationLocale(String locale) {
        if (locale != null && !locale.equals("")) setApplicationLocale(mountLocale(locale));
    }

    /**
 	 * Seta o locale da aplica��o.
 	 * @param locale Novo objeto Locale para a aplica��o.
 	 */
    public void setApplicationLocale(Locale locale) {
        if (locale != null) Locale.setDefault(locale);
    }

    /**
 	 * Recebe o locale como texto (pt_BR, en_US, es_ES) e gera um objeto locale.<br>
 	 * Se nenhum locale for informado assume locale default como "pt_BR".
 	 * @param strLocale Locale no formato texto. Exemplo: pt_BR, en_US, es_ES.
 	 * @return Objeto Locale conforme String passado
 	 */
    public Locale mountLocale(String strLocale) {
        Locale locale = new Locale("pt", "BR");
        if (strLocale != null && !strLocale.equals("#")) {
            String language = "";
            String country = "";
            String[] locs = StringUtils.split(strLocale, "_");
            language = locs[0];
            if (locs.length > 1) country = locs[1];
            locale = new Locale(language, country);
        }
        return locale;
    }
}
