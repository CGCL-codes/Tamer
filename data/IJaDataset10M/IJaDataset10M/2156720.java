package fitClipse.render.wikitext.JSPWikiWidgets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ca.ucalgary.cpsc.ebe.fitClipse.render.wikitext.WikiWidget;

public class MockLinkWidget extends WikiWidget {

    public static final String REGEXP = "\\[[^|^\\]^\\[]*(?:\\|(?:[^\\]]*))?\\]";

    public static final Pattern pattern = Pattern.compile("\\[([^|^\\]^\\[]*)(?:\\|(?:[^\\]]*))?\\]");

    private String caption = "";

    public MockLinkWidget(ParentWidget parent, String text) {
        super(parent);
        Matcher match = pattern.matcher(text);
        if (match.find()) {
            caption = match.group(1);
        }
    }

    @Override
    public String render() throws Exception {
        return "<a href=\"#\">" + caption + "</a>";
    }
}
