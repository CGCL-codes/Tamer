package vqwiki.lex;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import org.apache.log4j.Logger;

/** * * Default lexer implementation for VQWiki. * */
public class DefaultWikiParser extends AbstractParser {

    private static final Logger logger = Logger.getLogger(DefaultWikiParser.class);

    private static final String PARSER_BUNDLE = "defaultParser";

    private static final String PARSER_VERSION = "2.7.91";

    private static final String PARSER_NAME = "Default Parser";

    /**     *     */
    public DefaultWikiParser() {
        super(new ParserInfo(PARSER_NAME, PARSER_VERSION, PARSER_BUNDLE));
    }

    /**     *     */
    protected DefaultWikiParser(ParserInfo parserInfo) {
        super(parserInfo);
    }

    /**     *     * Parse text for online display.     *     */
    public String parseHTML(String rawtext, String virtualwiki) throws IOException {
        StringBuffer contents = new StringBuffer();
        Reader raw = new StringReader(rawtext.toString());
        contents = this.parseFormat(raw, virtualwiki);
        raw = new StringReader(contents.toString());
        contents = this.parseLayout(raw, virtualwiki);
        raw = new StringReader(contents.toString());
        contents = this.parseLinks(raw, virtualwiki);
        return this.removeTrailingNewlines(contents.toString());
    }

    /**     *     * Parse raw text and return something suitable for export     *     */
    public String parseExportHTML(String rawtext, String virtualwiki) throws IOException {
        StringBuffer contents = new StringBuffer();
        Reader raw = new StringReader(rawtext.toString());
        contents = this.parseFormat(raw, virtualwiki);
        contents = this.parseLayout(raw, virtualwiki);
        raw = new StringReader(contents.toString());
        contents = this.parseExportLinks(raw, virtualwiki);
        return this.removeTrailingNewlines(contents.toString());
    }

    /**     * Parse raw text and return a list of all found topic names.     */
    public List getTopics(String rawtext, String virtualwiki) throws Exception {
        StringReader reader = new StringReader(rawtext);
        LinkLexConvertBackLinks convert = new LinkLexConvertBackLinks();
        LinkLex lexer = new LinkLex(reader);
        lexer.setLinkLexConvert(convert);
        lexer.setVirtualWiki(virtualwiki);
        while (lexer.yylex() != null) {
        }
        reader.close();
        return convert.getLinks();
    }

    /**     *     *     *     */
    private StringBuffer parseFormat(Reader raw, String virtualWiki) throws IOException {
        StringBuffer contents = new StringBuffer();
        FormatLex lexer = new FormatLex(raw);
        lexer.setVirtualWiki(virtualWiki);
        boolean external = false;
        String tag = null;
        StringBuffer externalContents = null;
        while (true) {
            String line = null;
            try {
                line = lexer.yylex();
            } catch (ArrayIndexOutOfBoundsException e) {
                logger.debug(e);
            }
            logger.debug(line);
            if (line == null) {
                break;
            }
            if (line.startsWith("[<")) {
                if (!external) {
                    external = true;
                    tag = line.substring(2, line.length() - 2);
                    logger.debug("External lex call (tag=" + tag + ")");
                    externalContents = new StringBuffer();
                    contents.append(line);
                } else {
                    external = false;
                    String converted = LexExtender.getInstance().lexify(tag, externalContents.toString());
                    if (converted != null) {
                        contents.append(converted);
                    }
                    contents.append(line);
                    logger.debug("External ends");
                }
            } else {
                if (!external) {
                    contents.append(line);
                } else {
                    externalContents.append(line);
                }
            }
        }
        return contents;
    }

    /**     *     *     *     */
    private StringBuffer parseLayout(Reader raw, String virtualWiki) throws IOException {
        LayoutLex lexer = new LayoutLex(raw);
        lexer.setVirtualWiki(virtualWiki);
        return this.lex(lexer);
    }

    /**     *     *     *     */
    private StringBuffer parseLinks(Reader raw, String virtualWiki) throws IOException {
        LinkLex lexer = new LinkLex(raw);
        lexer.setVirtualWiki(virtualWiki);
        return this.lex(lexer);
    }

    /**     *     *     *     */
    private StringBuffer parseExportLinks(Reader raw, String virtualWiki) throws IOException {
        LinkLexConvertBackLinks convert = new LinkLexConvertBackLinks();
        LinkLex lexer = new LinkLex(raw);
        lexer.setLinkLexConvert(convert);
        lexer.setVirtualWiki(virtualWiki);
        return this.lex(lexer);
    }

    /**     *     *     *     */
    private String removeTrailingNewlines(String content) {
        if (content.endsWith("<br/>\n")) {
            content = content.substring(0, content.length() - 6);
            while (content.endsWith("<br/>")) {
                content = content.substring(0, content.length() - 5);
            }
        }
        return content;
    }

    /**     * Utility method for executing a lexer parse.     * FIXME - this is copy & pasted here and in MediaWikiParser     */
    protected StringBuffer lex(Lexer lexer) throws IOException {
        StringBuffer contents = new StringBuffer();
        while (true) {
            String line = lexer.yylex();
            if (line == null) {
                break;
            }
            contents.append(line);
        }
        return contents;
    }
}
