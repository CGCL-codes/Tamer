package org.opencms.jsp.decorator;

import org.opencms.file.CmsObject;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.util.CmsHtmlParser;
import org.opencms.util.CmsStringUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.util.Translate;

/**
 * The CmsHtmlDecorator is the main object for processing the text decorations.<p>
 * 
 * It uses the information of a <code>{@link CmsDecoratorConfiguration}</code> to process the
 * text decorations.
 *
 * @author Michael Emmerich  
 * 
 * @version $Revision: 1.8 $ 
 * 
 * @since 6.1.3 
 */
public class CmsHtmlDecorator extends CmsHtmlParser {

    /** Delimiters for string seperation. */
    private static final String[] DELIMITERS = { " ", ",", ".", ";", ":", "!", "(", ")", "'", "?", "\"", "&nbsp;", "&quot;", "\r\n", "\n" };

    /** Delimiters for second level string seperation. */
    private static final String[] DELIMITERS_SECOND_LEVEL = { "-", "@", "/", ".", "," };

    /** Steps for forward lookup in workd list. */
    private static final int FORWARD_LOOKUP = 7;

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsHtmlDecorator.class);

    /** Non translators, strings starting with those values must not be translated. */
    private static final String[] NON_TRANSLATORS = { "&nbsp;", "&quot;" };

    /** The decoration configuration.<p> */
    I_CmsDecoratorConfiguration m_config;

    /** Decoration bundle to be used by the decorator. */
    CmsDecorationBundle m_decorations;

    /** decorate flag. */
    private boolean m_decorate;

    /** the CmsObject. */
    private CmsObject m_cms;

    /**
     * Constructor, creates a new, empty CmsHtmlDecorator.<p>
     * 
     * @param cms the CmsObject
     * @throws CmsException if something goes wrong
     */
    public CmsHtmlDecorator(CmsObject cms) throws CmsException {
        m_config = new CmsDecoratorConfiguration(cms);
        m_decorations = m_config.getDecorations();
        m_result = new StringBuffer(512);
        m_echo = true;
        m_decorate = true;
    }

    /**
     * Constructor, creates a new CmsHtmlDecorator with a given configuration.<p>
     * 
     * @param cms the CmsObject
     * @param config the configuration to be used
     * 
     */
    public CmsHtmlDecorator(CmsObject cms, I_CmsDecoratorConfiguration config) {
        m_config = config;
        m_decorations = config.getDecorations();
        m_result = new StringBuffer(512);
        m_echo = true;
        m_decorate = true;
        m_cms = cms;
    }

    /**
     * Splits a String into substrings along the provided delimiter list and returns
     * the result as a List of Substrings.<p>
     *
     * @param source the String to split
     * @param delimiters the delimiters to split at
     * @param trim flag to indicate if leading and trailing whitespaces should be omitted
     * @param includeDelimiters flag to indicate if the delimiters should be included as well
     *
     * @return the List of splitted Substrings
     */
    public static List splitAsList(String source, String[] delimiters, boolean trim, boolean includeDelimiters) {
        List result = new ArrayList();
        String delimiter = "";
        int i = 0;
        int l = source.length();
        int n = -1;
        int max = Integer.MAX_VALUE;
        for (int j = 0; j < delimiters.length; j++) {
            if (source.indexOf(delimiters[j]) > -1) {
                if (source.indexOf(delimiters[j]) < max) {
                    max = source.indexOf(delimiters[j]);
                    n = source.indexOf(delimiters[j]);
                    delimiter = delimiters[j];
                }
            }
        }
        while (n != -1) {
            if ((i < n) || (i > 0) && (i < l)) {
                result.add(trim ? source.substring(i, n).trim() : source.substring(i, n));
                if (includeDelimiters && n + delimiter.length() <= l) {
                    result.add(source.substring(n, n + delimiter.length()));
                }
            } else {
                if (includeDelimiters && source.startsWith(delimiter)) {
                    result.add(delimiter);
                }
            }
            i = n + delimiter.length();
            max = Integer.MAX_VALUE;
            n = -1;
            for (int j = 0; j < delimiters.length; j++) {
                if (source.indexOf(delimiters[j], i) > -1) {
                    if (source.indexOf(delimiters[j], i) < max) {
                        max = source.indexOf(delimiters[j], i);
                        n = source.indexOf(delimiters[j], i);
                        delimiter = delimiters[j];
                    }
                }
            }
        }
        if (n < 0) {
            n = source.length();
        }
        if (i < n) {
            result.add(trim ? source.substring(i).trim() : source.substring(i));
        }
        return result;
    }

    /**
     * Processes a HTML string and adds text decorations according to the decoration configuration.<p>
     * 
     * @param html a string holding the HTML code that should be added with text decorations
     * @param encoding the encoding to be used
     * @return a HTML string with the decorations added.
     * @throws Exception if something goes wrong
     */
    public String doDecoration(String html, String encoding) throws Exception {
        return process(html, encoding);
    }

    /**
     * Resets the first occurance flags of all decoration objects.<p>
     * 
     * This is nescessary if decoration objects should be used for processing more than once.     *
     */
    public void resetDecorationDefinitions() {
        m_config.resetMarkedDecorations();
    }

    /**
     * @see org.htmlparser.visitors.NodeVisitor#visitStringNode(org.htmlparser.Text)
     */
    public void visitStringNode(Text text) {
        appendText(text.toPlainTextString(), DELIMITERS, true);
    }

    /**
     * @see org.htmlparser.visitors.NodeVisitor#visitTag(org.htmlparser.Tag)
     */
    public void visitTag(Tag tag) {
        super.visitTag(tag);
        String tagname = tag.getTagName();
        if (m_config.isExcluded(tagname)) {
            m_decorate = false;
        } else {
            m_decorate = true;
        }
    }

    /**
     * Appends a text decoration to the output.<p>
     * 
     * A lookup is made to find a text decoration for each word in the given text.
     * If a text decoration is found, the word will be decorated and added to the output.
     * If no text decoration is found, the word alone will be added to the output.
     * 
     * @param text the text to add a text decoration for
     * @param delimiters delimiters for text seperation
     * @param recursive flag for recusrive search
     */
    private void appendText(String text, String[] delimiters, boolean recursive) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.get().getBundle().key(Messages.LOG_HTML_DECORATOR_APPEND_TEXT_2, m_config, text));
        }
        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(text) && m_decorate) {
            List wordList = splitAsList(text, delimiters, false, true);
            for (int i = 0; i < wordList.size(); i++) {
                String word = (String) wordList.get(i);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.get().getBundle().key(Messages.LOG_HTML_DECORATOR_PROCESS_WORD_2, word, Boolean.valueOf(mustDecode(word, wordList, i))));
                }
                if (mustDecode(word, wordList, i)) {
                    word = Translate.decode(word);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(Messages.get().getBundle().key(Messages.LOG_HTML_DECORATOR_DECODED_WORD_1, word));
                    }
                }
                CmsDecorationObject decObj = null;
                if (!hasDelimiter(word, delimiters)) {
                    decObj = (CmsDecorationObject) m_decorations.get(word);
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.get().getBundle().key(Messages.LOG_HTML_DECORATOR_DECORATION_FOUND_2, decObj, word));
                }
                if (decObj == null) {
                    if (hasDelimiter(word, DELIMITERS_SECOND_LEVEL) && recursive) {
                        String secondLevel = word;
                        if (i < wordList.size() - 1) {
                            if (!((String) wordList.get(i + 1)).equals(" ")) {
                                secondLevel = word + (String) wordList.get(i + 1);
                                i++;
                            }
                        }
                        appendText(secondLevel, DELIMITERS_SECOND_LEVEL, false);
                    } else {
                        StringBuffer decKey = new StringBuffer();
                        decKey.append(word);
                        int forwardLookup = wordList.size() - i - 1;
                        if (forwardLookup > FORWARD_LOOKUP) {
                            forwardLookup = FORWARD_LOOKUP;
                        }
                        if (i < wordList.size() - forwardLookup) {
                            for (int j = 1; j <= forwardLookup; j++) {
                                decKey.append(wordList.get(i + j));
                                decObj = (CmsDecorationObject) m_decorations.get(decKey.toString());
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug(Messages.get().getBundle().key(Messages.LOG_HTML_DECORATOR_DECORATION_FOUND_FWL_3, decObj, word, new Integer(j)));
                                }
                                if (decObj != null) {
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug(Messages.get().getBundle().key(Messages.LOG_HTML_DECORATOR_DECORATION_APPEND_DECORATION_1, decObj.getContentDecoration(m_config, decKey.toString(), m_cms.getRequestContext().getLocale().toString())));
                                    }
                                    m_result.append(decObj.getContentDecoration(m_config, decKey.toString(), m_cms.getRequestContext().getLocale().toString()));
                                    i += j;
                                    break;
                                }
                            }
                        }
                        if (decObj == null) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(Messages.get().getBundle().key(Messages.LOG_HTML_DECORATOR_DECORATION_APPEND_WORD_1, word));
                            }
                            m_result.append(word);
                        }
                    }
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(Messages.get().getBundle().key(Messages.LOG_HTML_DECORATOR_DECORATION_APPEND_DECORATION_1, decObj.getContentDecoration(m_config, word, m_cms.getRequestContext().getLocale().toString())));
                    }
                    m_result.append(decObj.getContentDecoration(m_config, word, m_cms.getRequestContext().getLocale().toString()));
                }
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.get().getBundle().key(Messages.LOG_HTML_DECORATOR_DECORATION_APPEND_ORIGINALTEXT_1, text));
            }
            m_result.append(text);
        }
    }

    /** 
     * Checks if a word contains a given delimiter.<p>
     * 
     * @param word the word to test
     * @param delimiters array of delimiter strings
     * @return true if the word contains the delimiter, false otherwiese
     */
    private boolean hasDelimiter(String word, String[] delimiters) {
        boolean delim = false;
        for (int i = 0; i < delimiters.length; i++) {
            if (word.indexOf(delimiters[i]) > -1) {
                delim = true;
                break;
            }
        }
        return delim;
    }

    /**
     * Checks if a word must be decoded.<p>
     * 
     * The given word is compared to a negative list of words which must not be decoded.<p>
     * 
     * @param word the word to test
     * @param wordList the list of words which must not be decoded
     * @param count the count in the list
     * 
     * @return true if the word must be decoded, false otherweise
     */
    private boolean mustDecode(String word, List wordList, int count) {
        boolean decode = true;
        String nextWord = null;
        if (count < wordList.size() - 1) {
            nextWord = (String) wordList.get(count + 1);
        }
        if (nextWord != null && word.indexOf("&") > -1 && nextWord.startsWith(";")) {
            return false;
        } else {
            for (int i = 0; i < NON_TRANSLATORS.length; i++) {
                if (word.startsWith(NON_TRANSLATORS[i])) {
                    decode = false;
                    break;
                }
            }
        }
        return decode;
    }
}
