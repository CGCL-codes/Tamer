package yahoofinance.filter;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.tags.TableTag;

public class TableFilter implements NodeFilter {

    private static final long serialVersionUID = 3842703475744474740L;

    public boolean accept(Node node) {
        if (node instanceof TableTag) return true;
        return false;
    }
}
