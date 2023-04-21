package com.loribel.commons.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Tools for CSS.
 * 
 * See Test : GB_CssToolsTest
 * 
 * @author Gregory Borelli
 */
public final class GB_JsTools {

    /**
     * Extraction des URL trouv�es dans un fichier JS.
     * 
     * Recherche les URL absolue uniquement.
     */
    public static String[] extractJavascriptTags(String a_html) {
        return GB_HtmlTools.extractTags(a_html, "script");
    }

    /**
     * Extrait une url � partir de l'index a_index
     * � partir de l'index recherche le dernier 1er caract�re qui ne peut �tre dans une URL pour 
     * trouver l'URL � extraire. 
     */
    private static String extractUrl(String a_js, int a_index) {
        int l_index = STools.getMinIndexWithChars(a_js, a_index, ";,'\" )\r\n\t");
        if (l_index == -1) {
            return null;
        }
        return a_js.substring(a_index, l_index);
    }

    /**
     * Extraction des URL trouv�es dans un fichier JS.
     * 
     * Recherche les URL absolue uniquement.
     */
    public static String[] extractUrls(String a_jsContent, String a_domain) {
        List retour = new ArrayList();
        int[] l_indexes = STools.indexesOf(a_jsContent, a_domain);
        int len = CTools.getSize(l_indexes);
        for (int i = len - 1; i >= 0; i--) {
            int l_index = l_indexes[i];
            String l_url = extractUrl(a_jsContent, l_index);
            CTools.addNotNull(retour, l_url);
        }
        return CTools.toArrayOfString(retour);
    }

    /**
     * Extraits les URL d'un bloc Javascript
     *  
     * @param a_html
     * @param a_domain Domain (ex: http://www.loribel.com)
     * @param a_treatRelative Transforme les url relatives commnn�ants par "/" 
     */
    public static String[] extractUrlsFromTag(String a_tag, String a_domain, boolean a_treatRelative) {
        String l_tag = STools.replace(a_tag, "'/", "'" + a_domain + "/");
        l_tag = STools.replace(l_tag, "\"/", "\"" + a_domain + "/");
        String[] retour = extractUrls(l_tag, a_domain);
        return retour;
    }

    /**
     * Extraits les URL d'un bloc Javascript
     *  
     * @param a_html
     * @param a_domain Domain (ex: http://www.loribel.com)
     * @param a_treatRelative Transforme les url relatives commnn�ants par "/" 
     */
    public static String[] extractUrlsFromTags(String a_html, String a_domain, boolean a_treatRelative) {
        String[] l_tags = extractJavascriptTags(a_html);
        List retour = new ArrayList();
        int len = CTools.getSize(l_tags);
        for (int i = 0; i < len; i++) {
            String l_tag = l_tags[i];
            String[] l_urls = extractUrlsFromTag(l_tag, a_domain, a_treatRelative);
            CTools.addAll(retour, l_urls);
        }
        return (String[]) retour.toArray(new String[retour.size()]);
    }

    private GB_JsTools() {
    }
}
