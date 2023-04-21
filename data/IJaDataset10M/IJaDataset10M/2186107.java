package net.sf.jtypogrify.pants;

import net.sf.jtypogrify.token.Token;
import net.sf.jtypogrify.token.Tokenizer;
import net.sf.jtypogrify.token.FastTokenizer;
import net.sf.jtypogrify.StringTransformer;

/**
 * <p>JavaPants is a Java port of the smart-quotes library SmartyPants and RubyPants.</p>
 * <p>The original "SmartyPants" is a free web publishing plug-in for Movable Type, Blosxom, and BBEdit that easily translates plain ASCII punctuation
 * 	characters into "smart" typographic punctuation HTML entities.</p>
 *
 * <h3>Description</h3>
 * <p>RubyPants can perform the following transformations:</p>
 * <ul>
 * 	<li>Straight quotes ({@code "} and {@code '}) into "curly" quote HTML entities</li>
 * 	<li>Backticks-style quotes ({@code ``like this''}) into "curly" quote HTML entities</li>
 * 	<li>Dashes ({@code --} and {@code ---}) into en- and em-dash entities</li>
 * 	<li>Three consecutive dots ({@code ...} or {@code . . .}) into an ellipsis entity</li>
 * </ul>
 *
 * <p>This means you can write, edit, and save your posts using plain old ASCII straight quotes, plain dashes, and plain dots, but your published posts (and
 * 	final HTML output) will appear with smart quotes, em-dashes, and proper ellipses.</p>
 * <p>JavaPants does not modify characters within {@code <pre>}, {@code <code>}, {@code <kbd>}, {@code <math>} or {@code <script>} tag blocks. Typically, these
 * 	tags are used to display text where smart quotes and other "smart punctuation" would not be appropriate, such as source code or example markup.</p>
 *
 * <h3>Backslash Escapes</h3>
 * <p>If you need to use literal straight quotes (or plain hyphens and periods), RubyPants accepts the following backslash escape sequences to force non-smart
 * 	punctuation. It does so by transforming the escape sequence into a decimal-encoded HTML entity: {@code \\}, {@code \"}, {@code \'}, {@code \.},
 * 	{@code \-}, and {@code \`}. This is useful, for example, when you want to use straight quotes as foot and inch marks: 6'2" tall; a 17" iMac.  (Use
 * 	{@code 6\'2\"} resp. {@code 17\"}.)</p>
 *
 * <h3>Algorithmic Shortcomings</h3>
 * <p>One situation in which quotes will get curled the wrong way is when apostrophes are used at the start of leading contractions. For example:
 * 	{@code 'Twas the night before Christmas.}  In the case above, RubyPants will turn the apostrophe into an opening single-quote, when in fact it should
 * 	be a closing one. I don't think this problem can be solved in the general case&mdash;every word processor I've tried gets this wrong as well.  In such
 * 	cases, it's best to use the proper HTML entity for closing single-quotes ("{@code &#8217;}") by hand.
 *
 * <h3>Authors</h3>
 * <p>John Gruber did all of the hard work of writing this software in Perl for Movable Type and almost all of this useful documentation.  Chad Miller ported
 * 	it to Python to use with Pyblosxom.</p>
 * <p>Christian Neukirchen provided the Ruby port, as a general-purpose library that follows the *Cloth API.</p>
 *
 * <h3>Links</h3>
 * <ul>
 * 	<li>John Gruber:: http://daringfireball.net</li>
 * 	<li>SmartyPants:: http://daringfireball.net/projects/smartypants</li>
 * 	<li>Chad Miller:: http://web.chad.org</li>
 *	<li>Christian Neukirchen:: http://chneukirchen.org</li>
 * </ul>
 *
 * @author Andy Marek (andrew.marek@aurora.org)
 * @author Christian Neukirchen (chneukirchen@gmail.com), creator of RubyPants
 * @author Chad Miller, incooporates ideas, comments and documentation
 * @author John Gruber, creator of SmartyPants
 * @version Jun 1, 2007
 */
public class JavaPants implements StringTransformer {

    protected Backticks backticks;

    protected boolean convertQuoteEntities;

    protected Dashes dashes;

    protected boolean ellipses;

    protected boolean quotes;

    protected Tokenizer tokenizer;

    public static String getVersion() {
        return "0.2";
    }

    public JavaPants() {
        this.setBackticks(null);
        this.setConvertQuoteEntities(false);
        this.setDashes(null);
        this.setEllipses(true);
        this.setQuotes(true);
        this.setTokenizer(null);
    }

    public String transform(String text) {
        if (!isProcessing()) {
            return text;
        }
        boolean skipTag = false;
        StringBuilder result = new StringBuilder();
        String previousTokenLastChar = null;
        for (Token token : tokenizer.parse(text)) {
            if (token.getType() == Token.Type.tag) {
                result.append(token.getValue());
                if (token.getValue().matches("<(/?)(?:pre|code|kbd|script|math)[\\s>]")) {
                    skipTag = !token.getValue().startsWith("</");
                }
                continue;
            }
            String t = token.getValue();
            String lastChar = t.substring(t.length() - 1, t.length());
            if (!skipTag) {
                t = processEscapes(t);
                if (convertQuoteEntities) {
                    t = t.replaceAll("&quot;", "\"");
                }
                switch(dashes) {
                    case oldSchool:
                        t = educateDashesOldschool(t);
                        break;
                    case normal:
                        t = educateDashes(t);
                        break;
                    case inverted:
                        t = educateDashesInverted(t);
                        break;
                }
                if (ellipses) {
                    t = educateEllipses(t);
                }
                if (backticks != Backticks.none) {
                    if (backticks == Backticks.both || backticks == Backticks.doubleOnly) {
                        t = educateBackticks(t);
                    }
                    if (backticks == Backticks.both || backticks == Backticks.singleOnly) {
                        t = educateSingleBackticks(t);
                    }
                }
                if (quotes) {
                    if (t.equals("'")) {
                        t = previousTokenLastChar != null && previousTokenLastChar.matches("\\S") ? "&#8217;" : "&#8216;";
                    } else if (t.equals("\"")) {
                        t = previousTokenLastChar != null && previousTokenLastChar.matches("\\S") ? "&#8221;" : "&#8220;";
                    } else {
                        t = educateQuotes(t);
                    }
                }
            }
            previousTokenLastChar = lastChar;
            result.append(t);
        }
        return result.toString();
    }

    /**
	 * Return the string, with after processing the following backslash escape sequences. This is useful if you want to force a "dumb" quote or other character to
	 * appear.
	 * <p/>
	 * Escaped are: \\	\"	\'	\.	\-	\`
	 */
    protected String processEscapes(String str) {
        return str.replaceAll("\\\\\\\\\\\\", "&#92;").replaceAll("\\\\\"", "&#34;").replaceAll("\\\\'", "&#39;").replaceAll("\\\\\\.", "&#46;").replaceAll("\\\\-", "&#45;").replaceAll("\\\\`", "&#96;");
    }

    /**
	 * The string, with each instance of "<tt>--</tt>" translated to an em-dash HTML entity.
	 */
    protected String educateDashes(String str) {
        return str.replaceAll("--", "&#8212;");
    }

    /**
	 * The string, with each instance of "<tt>--</tt>" translated to an en-dash HTML entity, and each "<tt>---</tt>" translated to an em-dash HTML entity.
	 */
    protected String educateDashesOldschool(String str) {
        return str.replaceAll("---", "&#8212;").replaceAll("--", "&#8211;");
    }

    /**
	 * Return the string, with each instance of "<tt>--</tt>" translated to an em-dash HTML entity, and each "<tt>---</tt>" translated to an en-dash HTML entity.
	 * Two reasons why: First, unlike the en- and em-dash syntax supported by +educateDashesOldschool+, it's compatible with existing entries written before
	 * SmartyPants 1.1, back when "<tt>--</tt>" was only used for em-dashes.  Second, em-dashes are more common than en-dashes, and so it sort of makes sense that
	 * the shortcut should be shorter to type. (Thanks to Aaron Swartz for the idea.)
	 */
    protected String educateDashesInverted(String str) {
        return str.replaceAll("---", "&#8211;").replaceAll("--", "&#8212;");
    }

    /**
	 * Return the string, with each instance of "<tt>...</tt>" translated to an ellipsis HTML entity. Also converts the case where there are spaces between the
	 * dots.
	 */
    protected String educateEllipses(String str) {
        return str.replaceAll("\\.\\.\\.", "&#8230;").replaceAll("\\. \\. \\.", "&#8230;");
    }

    /**
	 * Return the string, with "<tt>``backticks''</tt>"-style single quotes translated into HTML curly quote entities.
	 */
    protected String educateBackticks(String str) {
        return str.replaceAll("``", "&#8220;").replaceAll("''", "&#8221;");
    }

    /**
	 * Return the string, with "<tt>`backticks'</tt>"-style single quotes translated into HTML curly quote entities.
	 */
    protected String educateSingleBackticks(String str) {
        return str.replaceAll("`", "&#8216;").replaceAll("'", "&#8217;");
    }

    /**
	 * Return the string, with "educated" curly quote HTML entities.
	 */
    protected String educateQuotes(String str) {
        String punct_class = "[!\\\"#\\$\\%\\'()*+,\\-.\\/:;<=>?\\@\\[\\\\\\]\\^_`{|}~]";
        str = str.replaceAll("^'(?=" + punct_class + "\\B)", "&#8217;");
        str = str.replaceAll("^\"(?=" + punct_class + "\\B)", "&#8221;");
        str = str.replaceAll("\"'(?=\\w)", "&#8220;&#8216;");
        str = str.replaceAll("'\"(?=\\w)", "&#8216;&#8220;");
        str = str.replaceAll("'(?=\\d\\ds)", "&#8217;");
        String close_class = "[^\\ \\t\\r\\n\\[\\{\\(\\-]";
        String dec_dashes = "&#8211;|&#8212;";
        str = str.replaceAll("(\\s|&nbsp;|--|&[mn]dash;|" + dec_dashes + "|&#x201[34];)'(?=\\w)", "$1&#8216;");
        str = str.replaceAll("(" + close_class + ")\\'", "$1&#8217;");
        str = str.replaceAll("'(\\s|s\\b)", "&#8217;$1");
        str = str.replaceAll("'", "&#8216;");
        str = str.replaceAll("(\\s|&nbsp;|--|&[mn]dash;|" + dec_dashes + "|&#x201[34];)\"(?=\\w)", "$1&#8220;");
        str = str.replaceAll("(" + close_class + ")\"", "$1&#8221;");
        str = str.replaceAll("\"\\s", "&#8221;$1");
        str = str.replaceAll("\"", "&#8220;");
        return str;
    }

    protected boolean isProcessing() {
        return backticks != Backticks.none || convertQuoteEntities || dashes != null || ellipses || quotes;
    }

    /**
	 * Specify which backticks to convert.  By default, no backticks are converted.
	 *
	 * @param backticks The backticks to convert as {@link Backticks}
	 */
    public void setBackticks(Backticks backticks) {
        this.backticks = backticks != null ? backticks : Backticks.doubleOnly;
    }

    /**
	 * Specify whether to consider existing HTML quote entities when converting quotes.
	 *
	 * @param quotes Set to {@code true} to convert existing quotes entities.
	 * @see #setQuotes(boolean)
	 */
    public void setConvertQuoteEntities(boolean convertQuoteEntities) {
        this.convertQuoteEntities = convertQuoteEntities;
    }

    /**
	 * Specify which dashes to convert.
	 *
	 * @param dashes The dashes to convert as {@link Dashes}.
	 */
    public void setDashes(Dashes dashes) {
        this.dashes = dashes != null ? dashes : Dashes.oldSchool;
    }

    /**
	 * Specify whether to convert ellipses to HTML entities.
	 *
	 * @param ellipses Set to {@code true} to convert ellipses.
	 */
    public void setEllipses(boolean ellipses) {
        this.ellipses = ellipses;
    }

    /**
	 * Specify whether to convert quotation marks to HTML entities.
	 *
	 * @param quotes Set to {@code true} to convert quotes.
	 * @see #setConvertQuoteEntities(boolean)
	 */
    public void setQuotes(boolean quotes) {
        this.quotes = quotes;
    }

    /**
	 * Specify which instance of a tokenizer should be used.
	 *
	 * @param tokenizer The tokenizer as an instance of {@link Tokenizer}.
	 */
    public void setTokenizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer != null ? tokenizer : new FastTokenizer();
    }
}
