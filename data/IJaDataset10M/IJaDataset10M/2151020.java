package org.omegat.core.machinetranslators;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.omegat.util.Language;
import org.omegat.util.OStrings;
import org.omegat.util.PatternConsts;
import org.omegat.util.Preferences;
import org.omegat.util.WikiGet;

/**
 * Support of Google Translate API v.2 machine translation.
 * https://code.google.com/apis/language/translate/v2/getting_started.html
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Didier Briel
 * @author Briac Pilpre
 */
public class Google2Translate extends BaseTranslate {

    protected static final String GT_URL = "https://www.googleapis.com/language/translate/v2";

    protected static final Pattern RE_UNICODE = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");

    protected static final Pattern RE_HTML = Pattern.compile("&#([0-9]+);");

    @Override
    protected String getPreferenceName() {
        return Preferences.ALLOW_GOOGLE2_TRANSLATE;
    }

    public String getName() {
        return OStrings.getString("MT_ENGINE_GOOGLE2");
    }

    @Override
    protected String translate(Language sLang, Language tLang, String text) throws Exception {
        String trText = text.length() > 5000 ? text.substring(0, 4997) + "..." : text;
        String targetLang = tLang.getLanguageCode();
        if ((tLang.getLanguage().compareToIgnoreCase("zh-cn") == 0) || (tLang.getLanguage().compareToIgnoreCase("zh-tw") == 0)) targetLang = tLang.getLanguage(); else if ((tLang.getLanguage().compareToIgnoreCase("zh-hk") == 0)) targetLang = "ZH-TW";
        StringBuffer queryString = new StringBuffer();
        String googleKey = System.getProperty("google.api.key");
        queryString.append("key=" + googleKey);
        queryString.append("&source=" + sLang.getLanguageCode());
        queryString.append("&target=" + targetLang);
        queryString.append("&q=" + URLEncoder.encode(trText, "utf-8"));
        String v;
        try {
            v = WikiGet.getURL(GT_URL + "?" + queryString.toString());
        } catch (IOException e) {
            return e.getLocalizedMessage();
        }
        while (true) {
            Matcher m = RE_UNICODE.matcher(v);
            if (!m.find()) {
                break;
            }
            String g = m.group();
            char c = (char) Integer.parseInt(m.group(1), 16);
            v = v.replace(g, Character.toString(c));
        }
        v = v.replace("&quot;", "&#34;");
        v = v.replace("&nbsp;", "&#160;");
        v = v.replace("&amp;", "&#38;");
        while (true) {
            Matcher m = RE_HTML.matcher(v);
            if (!m.find()) {
                break;
            }
            String g = m.group();
            char c = (char) Integer.parseInt(m.group(1));
            v = v.replace(g, Character.toString(c));
        }
        Pattern pattern = java.util.regex.Pattern.compile("\\{\\s*\"translatedText\"\\s*:\\s*\"(.*?)\"\\s*\\s*\\}\\s*]");
        Matcher matcher = pattern.matcher(v);
        boolean matchFound = matcher.find();
        String tr = "";
        if (matchFound) {
            tr = matcher.group(1);
        }
        Matcher tag = PatternConsts.OMEGAT_TAG_SPACE.matcher(tr);
        while (tag.find()) {
            String searchTag = tag.group();
            if (text.indexOf(searchTag) == -1) {
                String replacement = searchTag.substring(0, searchTag.length() - 1);
                tr = tr.replace(searchTag, replacement);
            }
        }
        tag = PatternConsts.SPACE_OMEGAT_TAG.matcher(tr);
        while (tag.find()) {
            String searchTag = tag.group();
            if (text.indexOf(searchTag) == -1) {
                String replacement = searchTag.substring(1, searchTag.length());
                tr = tr.replace(searchTag, replacement);
            }
        }
        return tr;
    }
}
