package org.snipsnap.render.macro;

import org.apache.lucene.search.Hits;
import org.radeox.macro.BaseMacro;
import org.radeox.macro.parameter.MacroParameter;
import org.radeox.util.i18n.ResourceManager;
import org.radeox.util.logging.Logger;
import org.snipsnap.components.LuceneSearchService;
import snipsnap.api.container.Components;
import snipsnap.api.search.SearchService;
import snipsnap.sniplink.SnipLink;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;

public class SearchMacro extends BaseMacro {

    private SearchService searchService;

    public SearchMacro() {
        searchService = (SearchService) Components.getComponent(LuceneSearchService.class);
    }

    public String getName() {
        return "search";
    }

    public String getDescription() {
        return ResourceManager.getString("i18n.messages", "macro.search.description");
    }

    public String[] getParamDescription() {
        return ResourceManager.getString("i18n.messages", "macro.search.params").split(";");
    }

    public void execute(Writer writer, MacroParameter params) throws IllegalArgumentException, IOException {
        if (params.getLength() == 1 || params.getLength() == 2) {
            int maxHits = 10;
            if (params.getLength() == 2) {
                maxHits = Integer.parseInt(params.get("1"));
            }
            String searchString = params.get("0");
            Hits hits = null;
            try {
                hits = searchService.search(searchString);
            } catch (Exception e) {
                Logger.warn("SearchMacro: exception while searching: " + e);
            }
            if (hits != null && hits.length() > 0) {
                writer.write("<div class=\"list\"><div class=\"list-title\">");
                MessageFormat mf = new MessageFormat(ResourceManager.getString("i18n.messages", "macro.search.title"), ResourceManager.getLocale("i18n.messages"));
                writer.write(mf.format(new Object[] { searchString, new Integer(hits.length()) }));
                writer.write("</div>");
                int start = 0;
                int end = Math.min(maxHits, hits.length());
                writer.write("<blockquote>");
                try {
                    for (int i = start; i < end; i++) {
                        SnipLink.appendLink(writer, hits.doc(i).get("title"));
                        if (i < end - 1) {
                            writer.write(", ");
                        }
                    }
                    writer.write("</blockquote></div>");
                } catch (IOException e) {
                    Logger.warn("I/O error while iterating over search results.");
                }
            } else {
                writer.write(ResourceManager.getString("i18n.messages", "macro.search.notfound"));
            }
            return;
        } else {
            throw new IllegalArgumentException("Number of arguments does not match");
        }
    }
}
