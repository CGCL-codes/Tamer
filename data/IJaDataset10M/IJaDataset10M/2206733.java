package org.vizzini.example.stockanalyzer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Text;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * Provides base functionality for HTML processors.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.4
 */
public abstract class AbstractHtmlProcessor implements IHtmlProcessor {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(AbstractHtmlProcessor.class.getName());

    /** Base URL. */
    private String _baseUrl;

    /** Date utilities. */
    private DateUtilities _dateUtilities;

    /** Parser. */
    private List<Parser> _parsers = new ArrayList<Parser>();

    /** Source URL. */
    private List<String> _sourceUrls = new ArrayList<String>();

    /**
     * @return  baseUrl.
     *
     * @since   v0.4
     */
    public String getBaseUrl() {
        return _baseUrl;
    }

    /**
     * @param   index  Index.
     *
     * @return  source URL.
     *
     * @since   v0.4
     */
    public String getSourceUrl(int index) {
        String answer = null;
        if (index < _sourceUrls.size()) {
            answer = _sourceUrls.get(index);
        }
        if (answer == null) {
            answer = getBaseUrl();
        }
        return answer;
    }

    /**
     * @param  baseUrl  The baseUrl to set.
     *
     * @since  v0.4
     */
    public void setBaseUrl(String baseUrl) {
        _baseUrl = baseUrl;
    }

    /**
     * @param  index      Index.
     * @param  sourceUrl  The source URL to set.
     *
     * @since  v0.4
     */
    public void setSourceUrl(int index, String sourceUrl) {
        if (_parsers.size() > index) {
            _parsers.set(index, null);
        }
        if (_sourceUrls.size() <= index) {
            _sourceUrls.add(sourceUrl);
        } else {
            _sourceUrls.set(index, sourceUrl);
        }
    }

    /**
     * @param   index  Index.
     *
     * @return  parser.
     *
     * @since   v0.4
     */
    protected Parser getParser(int index) {
        Parser answer = null;
        if (index < _parsers.size()) {
            answer = _parsers.get(index);
        }
        if (answer == null) {
            try {
                String sourceUrl = getSourceUrl(index);
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("sourceUrl = " + sourceUrl);
                }
                answer = new Parser(sourceUrl);
                if (_parsers.size() <= index) {
                    _parsers.add(answer);
                } else {
                    _parsers.set(index, answer);
                }
            } catch (ParserException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return answer;
    }

    /**
     * @param   node  Node.
     *
     * @return  the first Text child of the given node.
     *
     * @since   v0.4
     */
    protected String getTextChildOf(Node node) {
        String answer = null;
        Node myNode = node;
        while ((answer == null) && (myNode != null)) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("node = " + myNode.getClass().getName());
                LOGGER.finest("node = " + myNode.toHtml());
                LOGGER.finest("node instanceof Text ? " + (myNode instanceof Text));
            }
            if (myNode instanceof Text) {
                String valueStr = myNode.getText();
                if (valueStr != null) {
                    valueStr = valueStr.trim();
                    valueStr = valueStr.replaceAll("&amp;", "&");
                    if (valueStr.endsWith(":")) {
                        valueStr = valueStr.substring(0, valueStr.length() - 1);
                    }
                }
                if (!"".equals(valueStr) && !"\n".equals(valueStr)) {
                    answer = valueStr;
                }
            }
            if (answer == null) {
                NodeList nodeList = myNode.getChildren();
                if ((nodeList != null) && (nodeList.size() > 0)) {
                    for (int i = 0; (answer == null) && (i < nodeList.size()); i++) {
                        if (LOGGER.isLoggable(Level.FINEST)) {
                            LOGGER.finest("nodeList.size() = " + nodeList.size());
                        }
                        myNode = nodeList.elementAt(i);
                        answer = getTextChildOf(myNode);
                    }
                } else {
                    myNode = null;
                }
            }
        }
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("answer = " + answer);
        }
        return answer;
    }

    /**
     * @param   node  Node.
     *
     * @return  a double parsed from the given node.
     *
     * @since   v0.4
     */
    protected Date parseDate(Node node) {
        Date answer = null;
        if (node != null) {
            String valueStr = getTextChildOf(node);
            answer = parseDate(valueStr);
        }
        return answer;
    }

    /**
     * @param   valueStr  Value string.
     *
     * @return  a Date parsed from the given node.
     *
     * @since   v0.4
     */
    protected Date parseDate(String valueStr) {
        Date answer = null;
        if (valueStr != null) {
            if (!"N/A".equals(valueStr)) {
                DateUtilities dateUtils = getDateUtilities();
                try {
                    answer = dateUtils.parseYahooDate(valueStr);
                } catch (ParseException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
        return answer;
    }

    /**
     * @param   valueStr  String to parse.
     *
     * @return  the given string as a double value.
     *
     * @since   v0.4
     */
    protected double parseDouble(String valueStr) {
        double answer = Double.NEGATIVE_INFINITY;
        if (!"N/A".equals(valueStr)) {
            String myValueStr = valueStr;
            if (myValueStr.endsWith("%")) {
                myValueStr = myValueStr.substring(0, myValueStr.length() - 1);
                answer = Double.parseDouble(myValueStr);
                answer = answer / 100.0;
            } else {
                if (myValueStr.endsWith("B")) {
                    myValueStr = myValueStr.substring(0, myValueStr.length() - 1);
                    myValueStr += "e09";
                } else if (myValueStr.endsWith("M")) {
                    myValueStr = myValueStr.substring(0, myValueStr.length() - 1);
                    myValueStr += "e06";
                } else if (myValueStr.endsWith("K")) {
                    myValueStr = myValueStr.substring(0, myValueStr.length() - 1);
                    myValueStr += "e03";
                }
                myValueStr = myValueStr.replaceAll(",", "");
                answer = Double.parseDouble(myValueStr);
            }
        }
        return answer;
    }

    /**
     * @param   node  Node.
     *
     * @return  a double parsed from the given node.
     *
     * @since   v0.4
     */
    protected double parseDouble(Node node) {
        double answer = Double.NEGATIVE_INFINITY;
        if (node != null) {
            String valueStr = getTextChildOf(node);
            double value = parseDouble(valueStr);
            if (value != Double.NEGATIVE_INFINITY) {
                answer = value;
            }
        }
        return answer;
    }

    /**
     * @param   node  Node.
     *
     * @return  a String parsed from the given node.
     *
     * @since   v0.4
     */
    protected String parseString(Node node) {
        String answer = null;
        if (node != null) {
            answer = getTextChildOf(node);
        }
        return answer;
    }

    /**
     * @param  index   Index.
     * @param  parser  The parser to set.
     *
     * @since  v0.4
     */
    protected void setParser(int index, Parser parser) {
        _parsers.set(index, parser);
    }

    /**
     * @return  the dateUtilities
     */
    private DateUtilities getDateUtilities() {
        if (_dateUtilities == null) {
            _dateUtilities = new DateUtilities();
        }
        return _dateUtilities;
    }
}
