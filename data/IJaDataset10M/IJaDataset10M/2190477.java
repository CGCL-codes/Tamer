package fr.mywiki.business.wiki.render.filter;

import java.util.Iterator;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;
import org.radeox.filter.CacheFilter;
import org.radeox.filter.context.FilterContext;
import org.radeox.filter.regex.LocaleRegexTokenFilter;
import org.radeox.filter.regex.RegexTokenFilter;
import org.radeox.regex.MatchResult;
import fr.mywiki.business.utils.StringUtils;
import fr.mywiki.business.wiki.render.WikiRenderEngine;
import fr.mywiki.business.xml.XmlManager;

public class WikiTableFilter extends LocaleRegexTokenFilter implements CacheFilter {

    public static final String[] CELL_DELIM = { "|", "^" };

    protected String getLocaleKey() {
        return "filter.wikitable";
    }

    /**
	 * <p>
	 * Method called for every occurence of the regular expression
	 * "filter.wikitable.match". The buffer is concatened with html code computed
	 * here from the match result.
	 * </p>
	 * 
	 * @param buffer
	 *          Buffer to write replacement string to
	 * @param matchResult
	 *          Hit with the found regular expression
	 * @param context
	 *          FilterContext for filters
	 * 
	 * @see RegexTokenFilter#handleMatch(java.lang.StringBuffer,
	 *      org.radeox.regex.MatchResult, org.radeox.filter.context.FilterContext)
	 */
    public void handleMatch(StringBuffer buffer, MatchResult matchResult, FilterContext context) {
        String tablePattern = matchResult.group(0);
        String[] lines = tablePattern.split(WikiRenderEngine.LINE_DELIM_REGEX);
        Element table = new Element("table");
        table.addAttribute("class", "inline");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Element tableLine = new Element("tr");
            Iterator itCells = StringUtils.sliceString(line, CELL_DELIM).iterator();
            while (itCells.hasNext()) {
                String cellContent = (String) itCells.next();
                Element cell = null;
                if (cellContent.startsWith("^")) cell = new Element("th"); else cell = new Element("td");
                String text = cellContent.substring(1);
                if (new String().equals(text)) {
                    List children = tableLine.getChildren();
                    if (!children.isEmpty()) {
                        Element previousCell = (Element) children.get(children.size() - 1);
                        Attribute colspan = previousCell.getAttribute("colspan");
                        if (colspan != null) {
                            int colspanValue = new Integer(previousCell.getAttributeValue("colspan")).intValue();
                            colspan.setValue(Integer.toString(colspanValue + 1));
                        } else {
                            previousCell.addAttribute(new Attribute("colspan", "2"));
                        }
                    }
                } else {
                    String style = "leftalign";
                    if (text.startsWith(" ")) {
                        style = "rightalign";
                        if (text.endsWith(" ")) style = "centeralign";
                    }
                    cell.addAttribute("class", style);
                    cell.setText(text.trim());
                    tableLine.addContent(cell);
                }
            }
            table.addContent(tableLine);
        }
        buffer.append(XmlManager.XmlToString(table));
    }
}
