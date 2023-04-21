package info.bliki.wiki.client;

import info.bliki.wiki.client.filter.WikipediaFilter;
import java.util.HashMap;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class WikiTextarea extends TextArea implements KeyboardListener {

    private WikipediaFilter fWikiFilter;

    private final HashMap<String, String> fWikiSettings;

    public WikiTextarea(HashMap<String, String> wikiSettings) {
        super();
        fWikiFilter = new WikipediaFilter();
        fWikiSettings = wikiSettings;
        addKeyboardListener(this);
    }

    public void onKeyDown(Widget sender, char keyCode, int modifiers) {
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        String str = Wiki.WIKI_EDITOR_BODY.getText().trim();
        if (str.length() > 0) {
            String result = fWikiFilter.filter(str, fWikiSettings);
            Wiki.PREVIEW_HTML.setHTML(result);
            Wiki.renderCode();
        } else {
            Wiki.PREVIEW_HTML.setHTML(" ");
        }
        Wiki.WIKI_EDITOR_BODY.setFocus(true);
    }
}
