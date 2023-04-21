package org.omegat.filters2.html2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.Remark;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.visitors.NodeVisitor;
import org.omegat.util.PatternConsts;
import org.omegat.util.StaticUtils;

/**
 * The part of HTML filter that actually does the job.
 * This class is called back by HTMLParser (http://sf.net/projects/htmlparser/).
 *
 * @author Maxym Mykhalchuk
 * @author Didier Briel
 * @author Henry Pijffers (henry.pijffers@saxnot.com)
 * @author Martin Fleurke
 */
public class FilterVisitor extends NodeVisitor {

    private HTMLFilter2 filter;

    private BufferedWriter writer;

    private HTMLOptions options;

    public FilterVisitor(HTMLFilter2 htmlfilter, BufferedWriter bufwriter, HTMLOptions options) {
        this.filter = htmlfilter;
        this.options = options;
        this.writer = bufwriter;
    }

    /** Should the parser call us for this tag's ending tag and its inner tags. */
    boolean recurse = true;

    /** Do we collect the translatable text now. */
    boolean text = false;

    /** Did the PRE block start (it means we mustn't compress the spaces). */
    boolean preformatting = false;

    /**
     * The list of non-paragraph tags before a chunk of text.
     * <ul>
     * <li>If a chunk of text follows, they get prepended to the translatable paragraph,
     *     (starting from the first tag having a pair inside a chunk of text)
     * <li>Otherwise they are written out directly.
     * </ul>
     */
    List<Node> befors;

    /** The list of nodes forming a chunk of text. */
    List<Node> translatable;

    /**
     * The list of non-paragraph tags following a chunk of text.
     * <ul>
     * <li>If another chunk of text follows, they get appended to the translatable paragraph,
     * <li>Otherwise (paragraph tag follows), they are written out directly.
     * </ul>
     */
    List<Node> afters;

    /** The tags behind the shortcuts */
    List<Tag> s_tags;

    /** The tag numbers of shorcutized tags */
    List<Integer> s_tag_numbers;

    /** The list of all the tag shortcuts */
    List<String> s_shortcuts;

    /** The number of shortcuts stored */
    int s_nshortcuts;

    /**
     * Self traversal predicate.
     * @return <code>true</code> if a node itself is to be visited.
     */
    public boolean shouldRecurseSelf() {
        return recurse;
    }

    /**
     * Depth traversal predicate.
     * @return <code>true</code> if children are to be visited.
     */
    public boolean shouldRecurseChildren() {
        return recurse;
    }

    /**
     * Called for each <code>Tag</code> visited.
     * @param tag The tag being visited.
     */
    public void visitTag(Tag tag) {
        if (isIntactTag(tag)) {
            if (text) endup(); else flushbefors();
            writeout(tag.toHtml());
            if (tag.getEndTag() != null) recurse = false;
        } else {
            if (isParagraphTag(tag) && text) endup();
            if (isPreformattingTag(tag)) preformatting = true;
            maybeTranslateAttribute(tag, "abbr");
            maybeTranslateAttribute(tag, "alt");
            if (options.getTranslateHref()) maybeTranslateAttribute(tag, "href");
            if (options.getTranslateHreflang()) maybeTranslateAttribute(tag, "hreflang");
            if (options.getTranslateLang()) {
                maybeTranslateAttribute(tag, "lang");
                maybeTranslateAttribute(tag, "xml:lang");
            }
            if ("IMG".equals(tag.getTagName()) && options.getTranslateSrc()) maybeTranslateAttribute(tag, "src");
            maybeTranslateAttribute(tag, "summary");
            maybeTranslateAttribute(tag, "title");
            if ("INPUT".equals(tag.getTagName()) && (options.getTranslateValue() || "submit".equalsIgnoreCase(tag.getAttribute("type")) || "button".equalsIgnoreCase(tag.getAttribute("type")) || "reset".equalsIgnoreCase(tag.getAttribute("type")) && options.getTranslateButtonValue())) maybeTranslateAttribute(tag, "value");
            if ("META".equals(tag.getTagName())) {
                Vector<Attribute> tagAttributes = tag.getAttributesEx();
                Iterator<Attribute> i = tagAttributes.iterator();
                boolean doSkipMetaTag = false;
                while (i.hasNext() && doSkipMetaTag == false) {
                    Attribute attribute = i.next();
                    String name = attribute.getName();
                    String value = attribute.getValue();
                    if (name == null || value == null) continue;
                    doSkipMetaTag = this.filter.checkDoSkipMetaTag(name, value);
                }
                if (!doSkipMetaTag) {
                    maybeTranslateAttribute(tag, "content");
                }
            }
            queuePrefix(tag);
        }
    }

    /**
     * If the attribute of the tag is not empty,
     * it translates it as a separate segment.
     *
     * @param tag the tag object
     * @param key the name of the attribute
     */
    protected void maybeTranslateAttribute(Tag tag, String key) {
        String attr = tag.getAttribute(key);
        if (attr != null) {
            String trans = filter.privateProcessEntry(attr);
            tag.setAttribute(key, trans);
        }
    }

    boolean firstcall = true;

    /**
     * Called for each chunk of text (<code>StringNode</code>) visited.
     * @param string The string node being visited.
     */
    public void visitStringNode(Text string) {
        recurse = true;
        String trimmedtext = string.getText().trim();
        if (trimmedtext.length() > 0) {
            if (firstcall && PatternConsts.XML_HEADER.matcher(trimmedtext).matches()) {
                writeout(string.toHtml());
                return;
            }
            text = true;
            firstcall = false;
        }
        if (text) queueTranslatable(string); else queuePrefix(string);
    }

    /**
     * Called for each comment (<code>RemarkNode</code>) visited.
     * @param remark The remark node being visited.
     */
    public void visitRemarkNode(Remark remark) {
        recurse = true;
        if (text) endup();
        writeout(remark.toHtml());
    }

    /**
     * Called for each end <code>Tag</code> visited.
     * @param tag The end tag being visited.
     */
    public void visitEndTag(Tag tag) {
        recurse = true;
        if (isParagraphTag(tag) && text) endup();
        if (isPreformattingTag(tag)) preformatting = false;
        queuePrefix(tag);
    }

    /**
     * This method is called before the parsing.
     */
    public void beginParsing() {
        cleanup();
    }

    /**
     * Called upon parsing completion.
     */
    public void finishedParsing() {
        if (text) endup(); else flushbefors();
    }

    /**
     * Does the tag lead to starting (ending) a paragraph.
     * <p>
     * Contains code donated by JC to have dictionary list parsed as segmenting.
     * http://sourceforge.net/support/tracker.php?aid=1348792
     */
    private boolean isParagraphTag(Tag tag) {
        String tagname = tag.getTagName();
        return tagname.equals("ADDRESS") || tagname.equals("BLOCKQUOTE") || tagname.equals("BODY") || tagname.equals("CENTER") || tagname.equals("DIV") || tagname.equals("H1") || tagname.equals("H2") || tagname.equals("H3") || tagname.equals("H4") || tagname.equals("H5") || tagname.equals("H6") || tagname.equals("HTML") || tagname.equals("HEAD") || tagname.equals("TITLE") || tagname.equals("TABLE") || tagname.equals("TR") || tagname.equals("TD") || tagname.equals("TH") || tagname.equals("P") || tagname.equals("PRE") || tagname.equals("OL") || tagname.equals("UL") || tagname.equals("LI") || tagname.equals("DL") || tagname.equals("DT") || tagname.equals("DD") || tagname.equals("FORM") || tagname.equals("TEXTAREA") || tagname.equals("FIELDSET") || tagname.equals("LEGEND") || tagname.equals("LABEL") || tagname.equals("SELECT") || tagname.equals("OPTION") || tagname.equals("HR") || (tagname.equals("BR") && options.getParagraphOnBr());
    }

    /** Should a contents of this tag be kept intact? */
    private boolean isIntactTag(Tag tag) {
        String tagname = tag.getTagName();
        return tagname.equals("!DOCTYPE") || tagname.equals("STYLE") || tagname.equals("SCRIPT") || tagname.equals("OBJECT") || tagname.equals("EMBED") || (tagname.equals("META") && "content-type".equalsIgnoreCase(tag.getAttribute("http-equiv")));
    }

    /** Is the tag space-preserving? */
    private boolean isPreformattingTag(Tag tag) {
        String tagname = tag.getTagName();
        return tagname.equals("PRE") || tagname.equals("TEXTAREA");
    }

    /** Writes something to writer. */
    private void writeout(String something) {
        try {
            writer.write(something);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    /**
     * Ends the segment collection and sends the translatable text out
     * to OmegaT core,
     * and some extra tags to writer.
     */
    protected void endup() {
        List<Node> all = new ArrayList<Node>();
        all.addAll(befors);
        all.addAll(translatable);
        int firstgoodlimit = befors.size();
        int firstgood = 0;
        while (firstgood < firstgoodlimit) {
            Node good_node = all.get(firstgood);
            if (!(good_node instanceof Tag)) {
                firstgood++;
                continue;
            }
            Tag good = (Tag) good_node;
            int recursion = 1;
            boolean found = false;
            for (int i = firstgood + 1; i < all.size(); i++) {
                Node cand_node = all.get(i);
                if (cand_node instanceof Tag) {
                    Tag cand = (Tag) cand_node;
                    if (cand.getTagName().equals(good.getTagName())) {
                        if (!cand.isEndTag()) recursion++; else {
                            recursion--;
                            if (recursion == 0) {
                                if (i >= firstgoodlimit) found = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (found) break;
            firstgood++;
        }
        for (int i = 0; i < firstgood; i++) {
            Node node = all.get(i);
            if (node instanceof Tag) writeout("<" + node.getText() + ">"); else writeout(node.getText());
        }
        int lastgoodlimit = all.size() - 1;
        all.addAll(afters);
        int lastgood = all.size() - 1;
        while (lastgood > lastgoodlimit) {
            Node good_node = all.get(lastgood);
            if (!(good_node instanceof Tag)) {
                lastgood--;
                continue;
            }
            Tag good = (Tag) good_node;
            int recursion = 1;
            boolean found = false;
            for (int i = lastgood - 1; i >= firstgoodlimit; i--) {
                Node cand_node = all.get(i);
                if (cand_node instanceof Tag) {
                    Tag cand = (Tag) cand_node;
                    if (cand.getTagName().equals(good.getTagName())) {
                        if (cand.isEndTag()) recursion++; else {
                            recursion--;
                            if (recursion == 0) {
                                if (i <= lastgoodlimit) found = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (found) break;
            lastgood--;
        }
        StringBuffer paragraph = new StringBuffer();
        for (int i = firstgood; i <= lastgood; i++) {
            Node node = all.get(i);
            if (node instanceof Tag) {
                shortcut((Tag) node, paragraph);
            } else {
                paragraph.append(entitiesToChars(node.toHtml()));
            }
        }
        String uncompressed = paragraph.toString();
        String compressed = uncompressed;
        String spacePrefix = "";
        String spacePostfix = "";
        int size = uncompressed.length();
        if (!preformatting) {
            for (int i = 0; i < size; i++) {
                if (!Character.isWhitespace(uncompressed.charAt(i))) {
                    spacePrefix = uncompressed.substring(0, i);
                    break;
                }
            }
            for (int i = size - 1; i > 0; i--) {
                if (!Character.isWhitespace(uncompressed.charAt(i))) {
                    spacePostfix = uncompressed.substring(i + 1, size);
                    break;
                }
            }
            compressed = StaticUtils.compressSpaces(uncompressed);
        }
        String translation = filter.privateProcessEntry(compressed);
        if (compressed.equals(translation)) translation = uncompressed;
        translation = charsToEntities(translation);
        translation = unshorcutize(translation);
        writeout(spacePrefix);
        writeout(translation);
        writeout(spacePostfix);
        for (int i = lastgood + 1; i < all.size(); i++) {
            Node node = all.get(i);
            if (node instanceof Tag) writeout("<" + node.getText() + ">"); else writeout(node.getText());
        }
        cleanup();
    }

    /**
     * Inits a new paragraph.
     */
    private void cleanup() {
        text = false;
        recurse = true;
        befors = new ArrayList<Node>();
        translatable = new ArrayList<Node>();
        afters = new ArrayList<Node>();
        s_tags = new ArrayList<Tag>();
        s_tag_numbers = new ArrayList<Integer>();
        s_shortcuts = new ArrayList<String>();
        s_nshortcuts = 0;
    }

    /**
     * Creates and stores a shortcut for the tag.
     */
    private void shortcut(Tag tag, StringBuffer paragraph) {
        StringBuffer result = new StringBuffer();
        result.append('<');
        int n = -1;
        if (tag.isEndTag()) {
            result.append('/');
            int recursion = 1;
            for (int i = s_tags.size() - 1; i >= 0; i--) {
                Tag othertag = s_tags.get(i);
                if (othertag.getTagName().equals(tag.getTagName())) {
                    if (othertag.isEndTag()) recursion++; else {
                        recursion--;
                        if (recursion == 0) {
                            n = s_tag_numbers.get(i);
                            break;
                        }
                    }
                }
            }
            if (n < 0) {
                n = s_nshortcuts;
                s_nshortcuts++;
            }
        } else {
            n = s_nshortcuts;
            s_nshortcuts++;
        }
        if ("BR".equals(tag.getTagName())) result.append("br"); else result.append(Character.toLowerCase(tag.getTagName().charAt(0)));
        result.append(n);
        if (tag.isEmptyXmlTag()) result.append('/');
        result.append('>');
        String shortcut = result.toString();
        s_tags.add(tag);
        s_tag_numbers.add(n);
        s_shortcuts.add(shortcut);
        paragraph.append(shortcut);
    }

    /**
     * Recovers tag shortcuts into full tags.
     */
    private String unshorcutize(String str) {
        for (int i = 0; i < s_shortcuts.size(); i++) {
            String shortcut = s_shortcuts.get(i);
            int pos = -1;
            while ((pos = str.indexOf(shortcut, pos + 1)) >= 0) {
                Tag tag = s_tags.get(i);
                try {
                    str = str.substring(0, pos) + "<" + tag.getText() + ">" + str.substring(pos + shortcut.length());
                } catch (StringIndexOutOfBoundsException sioobe) {
                    break;
                }
            }
        }
        return str;
    }

    /**
     * Queues the text to the translatable paragraph.
     * <p>
     * Note that the queued text (if not-purely-whitespace)
     * will also append the previously queued tags and whitespace tags
     * to the translatable paragraph.
     * <p>
     * Whitespace text is simply added to the queue.
     */
    private void queueTranslatable(Text text) {
        if (text.toHtml().trim().length() > 0) {
            translatable.addAll(afters);
            afters.clear();
            translatable.add(text);
        } else afters.add(text);
    }

    /**
     * Queues the tag to the translatable paragraph.
     * <p>
     * Note that the tag is simply added to the queue,
     * and will be appended to the translatable text only
     * if some meaningful text follows it.
     */
    private void queueTranslatable(Tag tag) {
        afters.add(tag);
    }

    /**
     * Queues up something, possibly before a text.
     * If the text is collected now, the tag is queued up as translatable
     * by calling {@link #queueTranslatable(Tag)},
     * otherwise it's collected to a special list that is inspected
     * when the translatable text is sent to OmegaT core.
     */
    protected void queuePrefix(Tag tag) {
        if (text) queueTranslatable(tag); else if (isParagraphTag(tag)) {
            flushbefors();
            writeout("<" + tag.getText() + ">");
        } else befors.add(tag);
    }

    /**
     * Queues up some text, possibly before a meaningful text.
     * If the text is collected now, the tag is queued up as translatable
     * by calling {@link #queueTranslatable(Tag)},
     * otherwise it's collected to a special list that is inspected
     * when the translatable text is sent to OmegaT core.
     */
    private void queuePrefix(Text text) {
        befors.add(text);
    }

    /** Saves "Befors" to output stream and cleans the list. */
    private void flushbefors() {
        for (Node node : befors) {
            if (node instanceof Tag) writeout("<" + node.getText() + ">"); else writeout(node.getText());
        }
        befors.clear();
    }

    /** Named HTML Entities and corresponding numeric character references */
    private static final Object ENTITIES[][] = { { "quot", new Integer(34) }, { "amp", new Integer(38) }, { "lt", new Integer(60) }, { "gt", new Integer(62) }, { "OElig", new Integer(338) }, { "oelig", new Integer(339) }, { "Scaron", new Integer(352) }, { "scaron", new Integer(353) }, { "Yuml", new Integer(376) }, { "circ", new Integer(710) }, { "tilde", new Integer(732) }, { "ensp", new Integer(8194) }, { "emsp", new Integer(8195) }, { "thinsp", new Integer(8201) }, { "zwnj", new Integer(8204) }, { "zwj", new Integer(8205) }, { "lrm", new Integer(8206) }, { "rlm", new Integer(8207) }, { "ndash", new Integer(8211) }, { "mdash", new Integer(8212) }, { "lsquo", new Integer(8216) }, { "rsquo", new Integer(8217) }, { "sbquo", new Integer(8218) }, { "ldquo", new Integer(8220) }, { "rdquo", new Integer(8221) }, { "bdquo", new Integer(8222) }, { "dagger", new Integer(8224) }, { "Dagger", new Integer(8225) }, { "permil", new Integer(8240) }, { "lsaquo", new Integer(8249) }, { "rsaquo", new Integer(8250) }, { "euro", new Integer(8364) }, { "nbsp", new Integer(160) }, { "iexcl", new Integer(161) }, { "cent", new Integer(162) }, { "pound", new Integer(163) }, { "curren", new Integer(164) }, { "yen", new Integer(165) }, { "brvbar", new Integer(166) }, { "sect", new Integer(167) }, { "uml", new Integer(168) }, { "copy", new Integer(169) }, { "ordf", new Integer(170) }, { "laquo", new Integer(171) }, { "not", new Integer(172) }, { "shy", new Integer(173) }, { "reg", new Integer(174) }, { "macr", new Integer(175) }, { "deg", new Integer(176) }, { "plusmn", new Integer(177) }, { "sup2", new Integer(178) }, { "sup3", new Integer(179) }, { "acute", new Integer(180) }, { "micro", new Integer(181) }, { "para", new Integer(182) }, { "middot", new Integer(183) }, { "cedil", new Integer(184) }, { "sup1", new Integer(185) }, { "ordm", new Integer(186) }, { "raquo", new Integer(187) }, { "frac14", new Integer(188) }, { "frac12", new Integer(189) }, { "frac34", new Integer(190) }, { "iquest", new Integer(191) }, { "Agrave", new Integer(192) }, { "Aacute", new Integer(193) }, { "Acirc", new Integer(194) }, { "Atilde", new Integer(195) }, { "Auml", new Integer(196) }, { "Aring", new Integer(197) }, { "AElig", new Integer(198) }, { "Ccedil", new Integer(199) }, { "Egrave", new Integer(200) }, { "Eacute", new Integer(201) }, { "Ecirc", new Integer(202) }, { "Euml", new Integer(203) }, { "Igrave", new Integer(204) }, { "Iacute", new Integer(205) }, { "Icirc", new Integer(206) }, { "Iuml", new Integer(207) }, { "ETH", new Integer(208) }, { "Ntilde", new Integer(209) }, { "Ograve", new Integer(210) }, { "Oacute", new Integer(211) }, { "Ocirc", new Integer(212) }, { "Otilde", new Integer(213) }, { "Ouml", new Integer(214) }, { "times", new Integer(215) }, { "Oslash", new Integer(216) }, { "Ugrave", new Integer(217) }, { "Uacute", new Integer(218) }, { "Ucirc", new Integer(219) }, { "Uuml", new Integer(220) }, { "Yacute", new Integer(221) }, { "THORN", new Integer(222) }, { "szlig", new Integer(223) }, { "agrave", new Integer(224) }, { "aacute", new Integer(225) }, { "acirc", new Integer(226) }, { "atilde", new Integer(227) }, { "auml", new Integer(228) }, { "aring", new Integer(229) }, { "aelig", new Integer(230) }, { "ccedil", new Integer(231) }, { "egrave", new Integer(232) }, { "eacute", new Integer(233) }, { "ecirc", new Integer(234) }, { "euml", new Integer(235) }, { "igrave", new Integer(236) }, { "iacute", new Integer(237) }, { "icirc", new Integer(238) }, { "iuml", new Integer(239) }, { "eth", new Integer(240) }, { "ntilde", new Integer(241) }, { "ograve", new Integer(242) }, { "oacute", new Integer(243) }, { "ocirc", new Integer(244) }, { "otilde", new Integer(245) }, { "ouml", new Integer(246) }, { "divide", new Integer(247) }, { "oslash", new Integer(248) }, { "ugrave", new Integer(249) }, { "uacute", new Integer(250) }, { "ucirc", new Integer(251) }, { "uuml", new Integer(252) }, { "yacute", new Integer(253) }, { "thorn", new Integer(254) }, { "yuml", new Integer(255) }, { "fnof", new Integer(402) }, { "Alpha", new Integer(913) }, { "Beta", new Integer(914) }, { "Gamma", new Integer(915) }, { "Delta", new Integer(916) }, { "Epsilon", new Integer(917) }, { "Zeta", new Integer(918) }, { "Eta", new Integer(919) }, { "Theta", new Integer(920) }, { "Iota", new Integer(921) }, { "Kappa", new Integer(922) }, { "Lambda", new Integer(923) }, { "Mu", new Integer(924) }, { "Nu", new Integer(925) }, { "Xi", new Integer(926) }, { "Omicron", new Integer(927) }, { "Pi", new Integer(928) }, { "Rho", new Integer(929) }, { "Sigma", new Integer(931) }, { "Tau", new Integer(932) }, { "Upsilon", new Integer(933) }, { "Phi", new Integer(934) }, { "Chi", new Integer(935) }, { "Psi", new Integer(936) }, { "Omega", new Integer(937) }, { "alpha", new Integer(945) }, { "beta", new Integer(946) }, { "gamma", new Integer(947) }, { "delta", new Integer(948) }, { "epsilon", new Integer(949) }, { "zeta", new Integer(950) }, { "eta", new Integer(951) }, { "theta", new Integer(952) }, { "iota", new Integer(953) }, { "kappa", new Integer(954) }, { "lambda", new Integer(955) }, { "mu", new Integer(956) }, { "nu", new Integer(957) }, { "xi", new Integer(958) }, { "omicron", new Integer(959) }, { "pi", new Integer(960) }, { "rho", new Integer(961) }, { "sigmaf", new Integer(962) }, { "sigma", new Integer(963) }, { "tau", new Integer(964) }, { "upsilon", new Integer(965) }, { "phi", new Integer(966) }, { "chi", new Integer(967) }, { "psi", new Integer(968) }, { "omega", new Integer(969) }, { "thetasym", new Integer(977) }, { "upsih", new Integer(978) }, { "piv", new Integer(982) }, { "bull", new Integer(8226) }, { "hellip", new Integer(8230) }, { "prime", new Integer(8242) }, { "Prime", new Integer(8243) }, { "oline", new Integer(8254) }, { "frasl", new Integer(8260) }, { "weierp", new Integer(8472) }, { "image", new Integer(8465) }, { "real", new Integer(8476) }, { "trade", new Integer(8482) }, { "alefsym", new Integer(8501) }, { "larr", new Integer(8592) }, { "uarr", new Integer(8593) }, { "rarr", new Integer(8594) }, { "darr", new Integer(8595) }, { "harr", new Integer(8596) }, { "crarr", new Integer(8629) }, { "lArr", new Integer(8656) }, { "uArr", new Integer(8657) }, { "rArr", new Integer(8658) }, { "dArr", new Integer(8659) }, { "hArr", new Integer(8660) }, { "forall", new Integer(8704) }, { "part", new Integer(8706) }, { "exist", new Integer(8707) }, { "empty", new Integer(8709) }, { "nabla", new Integer(8711) }, { "isin", new Integer(8712) }, { "notin", new Integer(8713) }, { "ni", new Integer(8715) }, { "prod", new Integer(8719) }, { "sum", new Integer(8722) }, { "minus", new Integer(8722) }, { "lowast", new Integer(8727) }, { "radic", new Integer(8730) }, { "prop", new Integer(8733) }, { "infin", new Integer(8734) }, { "ang", new Integer(8736) }, { "and", new Integer(8869) }, { "or", new Integer(8870) }, { "cap", new Integer(8745) }, { "cup", new Integer(8746) }, { "int", new Integer(8747) }, { "there4", new Integer(8756) }, { "sim", new Integer(8764) }, { "cong", new Integer(8773) }, { "asymp", new Integer(8773) }, { "ne", new Integer(8800) }, { "equiv", new Integer(8801) }, { "le", new Integer(8804) }, { "ge", new Integer(8805) }, { "sub", new Integer(8834) }, { "sup", new Integer(8835) }, { "nsub", new Integer(8836) }, { "sube", new Integer(8838) }, { "supe", new Integer(8839) }, { "oplus", new Integer(8853) }, { "otimes", new Integer(8855) }, { "perp", new Integer(8869) }, { "sdot", new Integer(8901) }, { "lceil", new Integer(8968) }, { "rceil", new Integer(8969) }, { "lfloor", new Integer(8970) }, { "rfloor", new Integer(8971) }, { "lang", new Integer(9001) }, { "rang", new Integer(9002) }, { "loz", new Integer(9674) }, { "spades", new Integer(9824) }, { "clubs", new Integer(9827) }, { "hearts", new Integer(9829) }, { "diams", new Integer(9830) } };

    /** Converts HTML entities to normal characters */
    private String entitiesToChars(String str) {
        int strlen = str.length();
        StringBuffer res = new StringBuffer(strlen);
        for (int i = 0; i < strlen; i++) {
            char ch = str.charAt(i);
            switch(ch) {
                case '&':
                    char ch1;
                    if ((i + 1) >= strlen) {
                        res.append(ch);
                        break;
                    } else ch1 = str.charAt(i + 1);
                    if (ch1 == '#') {
                        char ch2 = str.charAt(i + 2);
                        if (ch2 == 'x' || ch2 == 'X') {
                            int n = i + 3;
                            while (n < strlen && isHexDigit(str.charAt(n))) n++;
                            String s_entity = str.substring(i + 3, n);
                            try {
                                int n_entity = Integer.parseInt(s_entity, 16);
                                if (n_entity > 0 && n_entity <= 65535) {
                                    res.append((char) n_entity);
                                    if (n < strlen && str.charAt(n) == ';') i = n; else i = n - 1;
                                } else {
                                    res.append(ch);
                                }
                            } catch (NumberFormatException nfe) {
                                res.append(ch);
                            }
                        } else {
                            int n = i + 2;
                            while (n < strlen && isDecimalDigit(str.charAt(n))) n++;
                            String s_entity = str.substring(i + 2, n);
                            try {
                                int n_entity = Integer.parseInt(s_entity, 10);
                                if (n_entity > 0 && n_entity <= 65535) {
                                    res.append((char) n_entity);
                                    if (n < strlen && str.charAt(n) == ';') i = n; else i = n - 1;
                                } else {
                                    res.append(ch);
                                }
                            } catch (NumberFormatException nfe) {
                                res.append(ch);
                            }
                        }
                    } else if (isLatinLetter(ch1)) {
                        int n = i + 1;
                        while (n < strlen && isLatinLetter(str.charAt(n))) n++;
                        String s_entity = str.substring(i + 1, n);
                        int n_entity = lookupEntity(s_entity);
                        if (n_entity > 0 && n_entity <= 65535) {
                            res.append((char) n_entity);
                            if (n < strlen && str.charAt(n) == ';') i = n; else i = n - 1;
                        } else {
                            res.append(ch);
                        }
                    } else {
                        res.append(ch);
                    }
                    break;
                default:
                    res.append(ch);
            }
        }
        return res.toString();
    }

    /** Returns true if a char is a latin letter */
    private boolean isLatinLetter(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    /** Returns true if a char is a decimal digit */
    private boolean isDecimalDigit(char ch) {
        return (ch >= '0' && ch <= '9');
    }

    /** Returns true if a char is a hex digit */
    private boolean isHexDigit(char ch) {
        return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
    }

    /** returns a character for HTML entity, or -1 if the passed string is not an entity */
    private int lookupEntity(String entity) {
        for (int i = 0; i < ENTITIES.length; i++) {
            Object[] ONENT = ENTITIES[i];
            if (entity.equals(ONENT[0])) return ((Integer) ONENT[1]).intValue();
        }
        return -1;
    }

    /**
     * Converts characters that must be converted
     * (&lt; &gt; &amp; '&nbsp;' (nbsp))
     * into HTML entities
     */
    private String charsToEntities(String str) {
        int strlen = str.length();
        StringBuffer res = new StringBuffer(strlen * 5);
        for (int i = 0; i < strlen; i++) {
            char ch = str.charAt(i);
            switch(ch) {
                case ' ':
                    res.append("&nbsp;");
                    break;
                case '&':
                    res.append("&amp;");
                    break;
                case '>':
                    res.append("&gt;");
                    break;
                case '<':
                    int gtpos = str.indexOf('>', i);
                    if (gtpos >= 0) {
                        String maybeShortcut = str.substring(i, gtpos + 1);
                        boolean foundShortcut = false;
                        for (String currShortcut : s_shortcuts) {
                            if (maybeShortcut.equals(currShortcut)) {
                                foundShortcut = true;
                                break;
                            }
                        }
                        if (foundShortcut) {
                            res.append(maybeShortcut);
                            i = gtpos;
                            continue;
                        } else {
                            res.append("&lt;");
                        }
                    } else {
                        res.append("&lt;");
                    }
                    break;
                default:
                    res.append(ch);
            }
        }
        String contents = res.toString();
        String encoding = this.filter.getTargetEncoding();
        if (encoding != null) {
            CharsetEncoder charsetEncoder = Charset.forName(encoding).newEncoder();
            int i = 0;
            boolean notfinished = true;
            while (notfinished) {
                for (; i < contents.length(); i++) {
                    char x = contents.charAt(i);
                    if (!charsetEncoder.canEncode(x)) {
                        String regexp;
                        if (x == '[' || x == '\\' || x == '^' || x == '$' || x == '.' || x == '|' || x == '?' || x == '*' || x == '+' || x == '(' || x == ')') {
                            regexp = "\\" + x;
                        } else regexp = "" + x;
                        String replacement = "&#" + (int) x + ';';
                        contents = contents.replaceAll(regexp, replacement);
                        break;
                    }
                }
                if (i == contents.length()) notfinished = false;
            }
        }
        return contents;
    }
}
