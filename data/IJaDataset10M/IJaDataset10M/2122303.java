package yahoofinance.filter;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.nodes.TextNode;

public class TextFilter implements NodeFilter {

    /**
     * 
     */
    private static final long serialVersionUID = 1861911096272129923L;

    @Override
    public boolean accept(final Node arg0) {
        if (arg0 instanceof TextNode) {
            return true;
        }
        return false;
    }
}
