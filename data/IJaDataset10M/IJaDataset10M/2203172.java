package org.omegat.gui.exttrans;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.omegat.core.Core;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.core.data.StringEntry;
import org.omegat.gui.common.EntryInfoPane;
import org.omegat.gui.common.EntryInfoSearchThread;
import org.omegat.gui.main.DockableScrollPane;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.WikiGet;
import org.omegat.util.gui.UIThreadsUtil;

/**
 * Pane for display translation from Google Translate.
 *
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
public class GoogleTranslateTextArea extends EntryInfoPane<String> {

    private ProxyAuthenticator pa;

    public boolean useProxy = false;

    public String proxyAddress;

    public GoogleTranslateTextArea() {
        super(true);
        setEditable(false);
        String title = OStrings.getString("GUI_MATCHWINDOW_SUBWINDOWTITLE_GoogleTranslate");
        Core.getMainWindow().addDockable(new DockableScrollPane("GOOGLE_TRANSLATE", title, this, true));
    }

    public void activateProxy() {
        if (useProxy) {
            pa = new ProxyAuthenticator(proxyAddress);
            pa.setProxy();
        }
    }

    @Override
    protected void onProjectClose() {
        UIThreadsUtil.mustBeSwingThread();
        setText("");
    }

    @Override
    protected void startSearchThread(final SourceTextEntry newEntry) {
        UIThreadsUtil.mustBeSwingThread();
        if (Preferences.isPreference(Preferences.ALLOW_GOOGLE_TRANSLATE)) {
            new FindThread(newEntry).start();
        } else {
            setText("Google Translate Deactivated");
        }
    }

    @Override
    protected void setFoundResult(String data) {
        UIThreadsUtil.mustBeSwingThread();
        setText(data != null ? data : "");
    }

    protected class FindThread extends EntryInfoSearchThread<String> {

        private final String src;

        public FindThread(final SourceTextEntry newEntry) {
            super(GoogleTranslateTextArea.this, newEntry);
            src = newEntry.getSrcText();
        }

        @Override
        protected String search() throws Exception {
            String source = Core.getProject().getProjectProperties().getSourceLanguage().getLanguageCode();
            String target = Core.getProject().getProjectProperties().getTargetLanguage().getLanguageCode();
            if (source == null || target == null) {
                return null;
            }
            return translate(src, source, target);
        }
    }

    protected static String GT_URL = "http://ajax.googleapis.com/ajax/services/language/translate?v=1.0&langpair=#sourceLang#|#targetLang#&q=";

    protected static String MARK_BEG = "{\"translatedText\":\"";

    protected static String MARK_END = "\"}";

    protected static Pattern RE_UNICODE = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");

    protected static Pattern RE_HTML = Pattern.compile("&#([0-9]+);");

    /**
     * Call google api and parse JSON result.
     *
     * @param text
     *            text to translate
     * @param from
     *            source language
     * @param to
     *            target language
     * @return translation result
     * @throws Exception
     */
    protected static String translate(final String text, final String from, final String to) throws Exception {
        String trText = text.length() > 5000 ? text.substring(0, 4997) + "..." : text;
        String from1 = from;
        String to1 = to;
        if (from1.length() == 3) {
            from1 = GoogleLanguages.languages.get(from1);
            if (from1 == null) from1 = from.substring(0, 2);
        }
        if (to1.length() == 3) {
            to1 = GoogleLanguages.languages.get(to1);
            if (to1 == null) to1 = to.substring(0, 2);
        }
        String url = GT_URL.replace("#sourceLang#", from1).replace("#targetLang#", to1) + URLEncoder.encode(trText, "UTF-8");
        String v = WikiGet.getURL(url);
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
        int beg = v.indexOf(MARK_BEG) + MARK_BEG.length();
        int end = v.indexOf(MARK_END, beg);
        String tr = v.substring(beg, end);
        return tr;
    }
}
