package org.jamwiki.parser.jflex;

import java.io.IOException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jamwiki.DataAccessException;
import org.jamwiki.parser.LinkUtil;
import org.jamwiki.parser.ParserException;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.parser.TableOfContents;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.utils.WikiUtil;

/**
 * Abstract parent class used for parsing wiki & HTML heading tags.
 */
public abstract class AbstractHeadingTag implements JFlexParserTag {

    private static final WikiLogger logger = WikiLogger.getLogger(AbstractHeadingTag.class.getName());

    /** Path to the template used to format a header edit link, relative to the classpath. */
    private static final String TEMPLATE_HEADER_EDIT_LINK = "templates/header-edit-link.template";

    /**
	 *
	 */
    private String buildSectionEditLink(ParserInput parserInput, int section) throws ParserException {
        if (!parserInput.getAllowSectionEdit()) {
            return "";
        }
        if (parserInput.getLocale() == null) {
            logger.info("Unable to build section edit links for " + parserInput.getTopicName() + " - locale is empty");
            return "";
        }
        Integer inclusion = (Integer) parserInput.getTempParam(TemplateTag.TEMPLATE_INCLUSION);
        boolean disallowInclusion = (inclusion != null && inclusion > 0);
        if (disallowInclusion) {
            return "";
        }
        String url = "";
        try {
            url = LinkUtil.buildEditLinkUrl(parserInput.getContext(), parserInput.getVirtualWiki(), parserInput.getTopicName(), null, section);
        } catch (DataAccessException e) {
            logger.error("Failure while building link for topic " + parserInput.getVirtualWiki() + " / " + parserInput.getTopicName(), e);
        }
        Object[] args = new Object[2];
        args[0] = url;
        args[1] = Utilities.formatMessage("common.sectionedit", parserInput.getLocale());
        try {
            return WikiUtil.formatFromTemplate(TEMPLATE_HEADER_EDIT_LINK, args);
        } catch (IOException e) {
            throw new ParserException(e);
        }
    }

    /**
	 *
	 */
    private String buildTagName(JFlexLexer lexer, String tocText) {
        String tagName = StringEscapeUtils.unescapeHtml4(tocText);
        return lexer.getParserInput().getTableOfContents().buildUniqueName(tagName);
    }

    /**
	 *
	 */
    private String buildTocText(JFlexLexer lexer, String tagText) throws ParserException {
        ParserInput tmpParserInput = new ParserInput(lexer.getParserInput());
        ParserOutput parserOutput = new ParserOutput();
        String tocText = this.processTocText(tmpParserInput, parserOutput, tagText, JFlexParser.MODE_PROCESS);
        return Utilities.stripMarkup(tocText);
    }

    /**
	 *
	 */
    private String generateOutput(JAMWikiLexer lexer, String tagName, String tocText, String tagText, int level, String raw, Object... args) throws ParserException {
        lexer.peekTag().getTagContent().append(this.updateToc(lexer.getParserInput(), tagName, tocText, level));
        int nextSection = lexer.getParserInput().getTableOfContents().size();
        String tagType = "h" + level;
        lexer.pushTag(tagType, this.generateTagOpen(raw, args));
        lexer.peekTag().getTagContent().append(this.buildSectionEditLink(lexer.getParserInput(), nextSection));
        String parsedTocText = this.processTocText(lexer.getParserInput(), lexer.getParserOutput(), tagText, lexer.getMode());
        lexer.peekTag().getTagContent().append("<span id=\"").append(tagName).append("\">").append(parsedTocText).append("</span>");
        lexer.popTag(tagType);
        return "";
    }

    /**
	 *
	 */
    protected abstract int generateTagLevel(String raw, Object... args) throws ParserException;

    /**
	 *
	 */
    protected abstract String generateTagOpen(String raw, Object... args) throws ParserException;

    /**
	 *
	 */
    protected abstract String generateTagText(String raw, Object... args) throws ParserException;

    /**
	 * Parse a Mediawiki heading of the form "==heading==" and return the
	 * resulting HTML output.
	 */
    public String parse(JFlexLexer lexer, String raw, Object... args) throws ParserException {
        if (logger.isTraceEnabled()) {
            logger.trace("heading: " + raw + " (" + lexer.yystate() + ")");
        }
        raw = raw.trim();
        int level = this.generateTagLevel(raw, args);
        String tagText = this.generateTagText(raw, args);
        String tocText = this.buildTocText(lexer, tagText);
        String tagName = this.buildTagName(lexer, tocText);
        if (lexer.getMode() <= JFlexParser.MODE_SLICE) {
            String sectionName = StringEscapeUtils.unescapeHtml4(tocText);
            lexer.getParserOutput().setSectionName(sectionName);
            return raw;
        }
        if (!(lexer instanceof JAMWikiLexer)) {
            throw new IllegalStateException("Cannot parse heading tags except with instances of JAMWikiLexer or in slice/splice mode");
        }
        JAMWikiLexer jamwikiLexer = (JAMWikiLexer) lexer;
        if (jamwikiLexer.paragraphIsOpen()) {
            jamwikiLexer.popTag("p");
        }
        return this.generateOutput(jamwikiLexer, tagName, tocText, tagText, level, raw, args);
    }

    /**
	 * Process all text inside of the equals signs.
	 */
    private String processTocText(ParserInput parserInput, ParserOutput parserOutput, String tagText, int mode) throws ParserException {
        return JFlexParserUtil.parseFragmentNonLineStart(parserInput, parserOutput, tagText, mode);
    }

    /**
	 *
	 */
    private String updateToc(ParserInput parserInput, String name, String text, int level) {
        String output = "";
        if (parserInput.getTableOfContents().getStatus() == TableOfContents.STATUS_TOC_UNINITIALIZED) {
            output += "__TOC__";
        }
        parserInput.getTableOfContents().addEntry(name, text, level);
        return output;
    }
}
