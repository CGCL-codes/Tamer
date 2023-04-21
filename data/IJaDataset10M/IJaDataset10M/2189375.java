package com.loribel.tools.web.bo;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.loribel.commons.exception.GB_LoadException;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_ArrayTools;
import com.loribel.commons.util.GB_CharTools;
import com.loribel.commons.util.GB_MD5Tools;
import com.loribel.commons.util.GB_MapTools;
import com.loribel.commons.util.GB_StringTools;
import com.loribel.commons.util.STools;
import com.loribel.tools.web.abstraction.GBW_BORepository;
import com.loribel.tools.web.abstraction.GBW_HtmlLinkAbstractBO;
import com.loribel.tools.web.abstraction.GBW_HtmlSimpleLinkJsOwnerBO;
import com.loribel.tools.web.abstraction.GBW_HtmlSimpleLinkOwnerBO;
import com.loribel.tools.web.abstraction.GBW_HtmlSimpleLinkROwnerBO;
import com.loribel.tools.web.bo.generated.GBW_HtmlLinkBOGen;
import com.loribel.tools.web.tools.GBW_HtmlLinkTools;
import com.loribel.tools.web.tools.GBW_HtmlSimpleLinkOwnerTools;

/**
 * Class GBW_HtmlLinkBO.
 */
public class GBW_HtmlLinkBO extends GBW_HtmlLinkBOGen implements GBW_HtmlSimpleLinkOwnerBO, GBW_HtmlSimpleLinkROwnerBO, GBW_HtmlSimpleLinkJsOwnerBO, GBW_HtmlLinkAbstractBO {

    public static String[] TITLE_ORDER = { TITLE2_NAME.H1, TITLE2_NAME.HTML, TITLE2_NAME.LINK };

    public GBW_HtmlLinkBO() {
        super();
    }

    public GBW_HtmlLinkBO(GBW_BORepository a_repository, String a_url) {
        this.setRepository(a_repository);
        this.setUrl(a_url);
        String l_id = a_repository.urlToId(a_url);
        this.setId(l_id);
        String l_path = a_repository.urlToPath(a_url, true);
        this.setPath(l_path);
    }

    public void addLinkSimple(GBW_HtmlSimpleLinkBO a_link) {
        GBW_HtmlSimpleLinkOwnerTools.addLink(this, a_link);
    }

    public GBW_HtmlSimpleLinkBO addLinkSimple(String a_url, String a_label, String a_urlSource) {
        return GBW_HtmlSimpleLinkOwnerTools.addLink(this, a_url, a_label, a_urlSource);
    }

    public void addType(String a_type) {
        String l_type = getType();
        l_type = GB_CharTools.appendChar(l_type, a_type, true);
        setType(l_type);
    }

    public String getEpure(boolean a_useXml) {
        if (a_useXml) {
            return getEpureXml();
        } else {
            return getEpureHtml();
        }
    }

    /**
     * Retourne GBW_HtmlInfoBO associ� � uniquement si le champ epure n'est pas vide.
     */
    public GBW_HtmlInfoBO getHtmlInfoEpure() {
        String l_gabarit = this.getEpureXml();
        if (STools.isNull(l_gabarit)) {
            return null;
        }
        GBW_HtmlInfoBO retour = getHtmlInfo(HTML_INFO_NAME.EPURE);
        return retour;
    }

    /**
     * Returns the list of lang s�par�e par "-" (langue + traduction)
     */
    public String getLangs() {
        List l_langs = new ArrayList();
        String l_lang = getLang();
        if (STools.isNotNull(l_lang)) {
            l_langs.add(l_lang);
        }
        Map l_map = getLinkIdByLangMap();
        String[] l_keys = GB_MapTools.getStringKeys(l_map);
        CTools.addAll(l_langs, l_keys);
        CTools.sortStrings(l_langs);
        return GB_StringTools.toString(l_langs, "-");
    }

    public GBW_HtmlLinkBO getLinkByLang(String a_lang, int a_option) throws GB_LoadException {
        String l_id = getLinkIdByLang(a_lang);
        if (STools.isNull(l_id)) {
            return null;
        }
        return getRepository().getAccessor().getHtmlLink(l_id, a_option);
    }

    /**
     * Returns the list of langs.
     */
    public String[] getLinkIdLangs() {
        return GB_MapTools.getStringKeys(getLinkIdByLangMap());
    }

    /**
     * Returns link for all languages.
     */
    public String[] getLinkIds() {
        Map l_maps = getLinkIdByLangMap();
        return GB_MapTools.valuesString(l_maps);
    }

    public GBW_HtmlLinkBO[] getLinksByLang(int a_option) throws GB_LoadException {
        String[] l_ids = getLinkIds();
        return GBW_HtmlLinkTools.getHtmlLinks(getRepository(), l_ids);
    }

    public String getMd5NotNull() {
        String retour = getMd5();
        if (retour == null) {
            String l_url = getUrl();
            try {
                retour = GB_MD5Tools.toMd5(l_url);
                if (retour != null) {
                    retour = retour.toLowerCase();
                    setMd5(retour);
                }
            } catch (NoSuchAlgorithmException ex) {
                return null;
            }
        }
        return retour;
    }

    /**
     * Utilise un titre bas� sur le titre user, h1, html en fonction de l'ordre de TITLE_ORDER.
     * Ceci permet d'avoir automatiquement un titre souvent sinificatif sans ajouter de titre manuel.
     */
    public String getTitleAuto() {
        String retour = getTitle();
        if (STools.isNotNull(retour)) {
            return retour;
        }
        int len = CTools.getSize(TITLE_ORDER);
        for (int i = 0; i < len; i++) {
            String l_type = TITLE_ORDER[i];
            retour = getTitle2(l_type);
            if (!STools.isNull(retour)) {
                return retour;
            }
        }
        return null;
    }

    public String getTitleH1() {
        return getTitle2(TITLE2_NAME.H1);
    }

    public String getTitleHtml() {
        return getTitle2(TITLE2_NAME.HTML);
    }

    public String getTitleLink() {
        return getTitle2(TITLE2_NAME.LINK);
    }

    public boolean isContentValid() {
        return getStatusUrl() == 200;
    }

    /**
     * Retourne true si l'objet est de type a_char
     * See : TYPE
     */
    public boolean isType(String a_char) {
        String l_type = getType();
        return GB_CharTools.containChar(l_type, a_char);
    }

    /**
     * Retourne true si le lien n'a pas de langue, si la langue est a_lang
     * ou si aucune traduction dans la langue a_lang est disponible.
     */
    public boolean isUseForLang(String a_lang) {
        String l_lang = this.getLang();
        if ((l_lang == null) || l_lang.equals(a_lang)) {
            return true;
        }
        String[] l_langs = this.getLinkIdLangs();
        return !GB_ArrayTools.contains(l_langs, a_lang);
    }

    public void removeType(String a_type) {
        String l_type = getType();
        l_type = GB_CharTools.removeChar(l_type, a_type);
        setType(l_type);
    }

    public void setTitle(String a_name, String a_title) {
        if (STools.isNull(a_title)) {
            return;
        }
        putTitle2(a_name, a_title);
    }

    public void setTitleH1(String a_title) {
        setTitle(TITLE2_NAME.H1, a_title);
    }

    public void setTitleHtml(String a_title) {
        setTitle(TITLE2_NAME.HTML, a_title);
    }

    public void setTitleLink(String a_title) {
        setTitle(TITLE2_NAME.LINK, a_title);
    }

    public String toString() {
        return BO_NAME + " [" + getId() + "]";
    }
}
