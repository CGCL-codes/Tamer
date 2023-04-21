package com.dna.motion.filters;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;
import com.dna.motion.dao.TranslationDAO;
import com.dna.motion.entities.Page;
import com.dna.motion.entities.Translation;
import com.dna.motion.tools.ApplicationSession;
import com.dna.motion.tools.Response;

/**
 * The Class TranslationsFilter.
 */
public class TranslationsFilter implements AjaxFilter {

    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception {
        Object objectReturned = chain.doFilter(obj, method, params);
        return objectReturned;
    }

    /**
	 * Replace translations.
	 * 
	 * @param page
	 *            the page
	 */
    private void replaceTranslations(Page page) {
        TranslationDAO dao = new TranslationDAO();
        String html_content = page.getHtmlContent();
        String css_content = page.getCssContent();
        String js_content = page.getJsContent();
        Locale locale = ApplicationSession.getLocale();
        List<Translation> translations = dao.getAllByName(locale.getLanguage()).getResponse();
        Map<String, Translation> mapTranslations = this.mapVariables(translations);
        this.process(html_content, mapTranslations);
        this.process(css_content, mapTranslations);
        this.process(js_content, mapTranslations);
    }

    private void process(String content, Map<String, Translation> mapTranslations) {
        StringTokenizer tokens = new StringTokenizer(content, " ", true);
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if (token.toUpperCase().contains("TRANSLATE={")) {
                String newToken = new String(token);
                newToken = newToken.toLowerCase();
                newToken.replace("translate={", "");
                newToken.replace("}", "");
                Translation translation = mapTranslations.get(newToken);
                content.replace(token, translation.getValue());
            }
        }
    }

    /**
	 * Map variables.
	 * 
	 * @param translations
	 *            the translations
	 * @return the map
	 */
    private Map<String, Translation> mapVariables(List<Translation> translations) {
        Map<String, Translation> mapOfVariables = new HashMap<String, Translation>();
        for (Translation t : translations) {
            mapOfVariables.put(t.getName(), t);
        }
        return mapOfVariables;
    }
}
