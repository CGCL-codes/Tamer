package org.vizzini.example.stockanalyzer;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

/**
 * Provides base functionality for processors to obtain symbols for stocks. This
 * class accesses the Yahoo! Finance site on the web for ticker symbols and
 * parses the results.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.4
 */
public abstract class AbstractSymbolProcessor extends AbstractHtmlProcessor {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(AbstractSymbolProcessor.class.getName());

    /**
     * @return  the stock symbols by querying a web site.
     *
     * @since   v0.4
     */
    public abstract List<String> fetchSymbols();

    /**
     * @return  the default stock symbols.
     *
     * @since   v0.4
     */
    protected abstract List<String> fetchDefaultSymbols();

    /**
     * Parse the stock symbols using the given parameters.
     *
     * @param  parser  HTML parser.
     * @param  list    Symbol list to fill.
     *
     * @since  v0.4
     */
    protected void parseSymbols(Parser parser, List<String> list) {
        NodeClassFilter filter0 = new NodeClassFilter(TableColumn.class);
        HasAttributeFilter filter1 = new HasAttributeFilter("class", "yfnc_tabledata1");
        AndFilter filter = new AndFilter(filter0, filter1);
        int numCols = 5;
        try {
            NodeList nodeList = parser.extractAllNodesThatMatch(filter);
            SimpleNodeIterator iter = nodeList.elements();
            int count = 0;
            while (iter.hasMoreNodes()) {
                Node node = iter.nextNode();
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("node = " + node.toHtml());
                }
                if ((count % numCols) == 0) {
                    String symbol = getTextChildOf(node);
                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.finer("symbol = " + symbol);
                    }
                    if (symbol != null) {
                        symbol = symbol.trim();
                        if ((symbol.length() > 0) && !"\n".equals(symbol)) {
                            list.add(symbol);
                        }
                    }
                }
                count++;
            }
        } catch (ParserException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
