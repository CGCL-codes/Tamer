package ca.ucalgary.cpsc.ebe.fitClipse.render.wikitext.JSPWikiWidgets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ca.ucalgary.cpsc.ebe.fitClipse.render.html.HtmlTag;
import ca.ucalgary.cpsc.ebe.fitClipse.render.html.HtmlUtil;
import ca.ucalgary.cpsc.ebe.fitClipse.render.wikitext.WikiWidget;

public class FormattedTextWidget extends WikiWidget {

    public static final String REGEXP = "%%\\([^(^)]*\\) .*?%%";

    public static final Pattern pattern = Pattern.compile("%%\\(([^(^)]*)\\) (.*?)%%");

    private String style = null;

    private String text = "";

    public FormattedTextWidget(ParentWidget parent, String text) {
        super(parent);
        Matcher match = pattern.matcher(text);
        if (match.find()) {
            this.style = match.group(1);
            this.text = match.group(2);
        }
    }

    @Override
    public String render() throws Exception {
        HtmlTag span = new HtmlTag("span", text);
        if (style != null) {
            span.addAttribute("style", style);
        }
        return span.html();
    }
}
