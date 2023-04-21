package de.mpiwg.vspace.diagram.sitemap;

import java.util.Map;
import de.mpiwg.vspace.languages.language.Language;
import de.mpiwg.vspace.languages.providers.LanguagePropertyProvider;

public class SitemapHelper {

    public static String getGroupTitle(SitemapGroup group) {
        Language currentLanguage = LanguagePropertyProvider.INSTANCE.getMainLanguage();
        Map<String, String> titleMap = group.getGroupTitles();
        String title = titleMap.get(currentLanguage.getLanguageCode());
        return title != null ? title : "";
    }
}
