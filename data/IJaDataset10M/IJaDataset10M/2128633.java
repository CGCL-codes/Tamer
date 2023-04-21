package org.digitalcure.refactordw.entities;

import org.digitalcure.refactordw.entities.WikiFileURL.URLSeparator;
import org.digitalcure.refactordw.operations.RegularExpressions;
import org.digitalcure.refactordw.util.exception.RefactorDWException;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents an <i>article link</i> to another Wiki file like
 * article or media file. Each <i>article link</i> consists of its original text
 * string and the owning article (this is the article that contains this article
 * link in its content). Those two components are needed in order to create the
 * complete article URL for the case that the link text string contains a
 * relative link path.<br>
 * Examples for article links:
 * <ul>
 * <li>[[start]]
 * <li>[[namespace1:namespace2:article_name]]
 * <li>[[namespace1:namespace2:article_name|alias name]]
 * <li>[[namespace1:namespace2:article_name#a chapter|alias name]]
 * <li>[[#a chapter]]
 * <li>{{my:namespace:document.pdf}}
 * </ul>
 * @author Manfred Novotny, Stefan Diener
 * @version 1.6
 * @since 1.0, 16.04.2009
 * @lastChange $Date$ by $Author$
 */
public class ArticleLink {

    /** Logger instance. */
    private static final Logger LOGGER = Logger.getLogger(ArticleLink.class);

    /**
     * Enumeration defining article link braces.
     * @author novotny
     * @version 1.0
     * @since 1.2, 04.06.2009
     * @lastChange $Date$ by $Author$
     */
    public enum Braces {

        /** The default link intro: <i>[[</i>. */
        INTRO_DEFAULT("[["), /** The default link extro: <i>]]</i>. */
        EXTRO_DEFAULT("]]"), /**
         * The alternative link intro: <i>{{</i>.<br>
         * This is used to link to MediaFiles supporting their MIME type.
         */
        INTRO_CURLY("{{"), /**
         * The alternative link extro: <i>}}</i>.<br>
         * This is used to link to MediaFiles supporting their MIME type.
         */
        EXTRO_CURLY("}}");

        /** The sequence containing the braces. */
        private final String sequence;

        /** The sequence containing the braces, but escaped. */
        private final String escapedSequence;

        /**
         * Constructor.
         * @param sequence the braces sequence
         */
        private Braces(final String sequence) {
            this.sequence = sequence;
            final StringBuilder builder = new StringBuilder();
            builder.append(RegularExpressions.ESCAPE_SEQUENCE);
            builder.append(sequence.charAt(0));
            builder.append(RegularExpressions.ESCAPE_SEQUENCE);
            builder.append(sequence.charAt(1));
            escapedSequence = builder.toString();
        }

        /**
         * Returns the braces sequence string.<br> Example: <i>[[</i>
         * @param escaped if <code>true</code> the returned sequence will be
         *  escaped, e.g. <i>\\[\\[</i>; if <code>false</code> the original
         *  sequence will be returned, e.g. <i>[[</i>.
         * @return the braces sequence string, escaped or un-escaped depending
         *  on flag <code>escaped</code>
         */
        public String getSequence(final boolean escaped) {
            return (escaped) ? escapedSequence : sequence;
        }

        /**
         * Returns the string sequence depending on the given <i>brace type</i>
         * and <i>intro</i> flag.
         * @param bracetype the <i>brace type</i> to retrieve
         * @param intro if <code>true</code> the <i>intro</i> brace sequence
         *  will be returned, else the <i>extro</i> sequence
         * @param escaped if <code>true</code> the returned sequence will be
         *  escaped, e.g. <i>\\[\\[</i>; if <code>false</code> the original
         *  sequence will be returned, e.g. <i>[[</i>.
         * @return the braces sequence string, Escaped or un-escaped depending
         *  on flag <code>escaped</code>
         */
        public static String getSequenceByType(final BraceType bracetype, final boolean intro, final boolean escaped) {
            switch(bracetype) {
                case CURLY:
                    return (intro) ? INTRO_CURLY.getSequence(escaped) : EXTRO_CURLY.getSequence(escaped);
                case DEFAULT:
                    return (intro) ? INTRO_DEFAULT.getSequence(escaped) : EXTRO_DEFAULT.getSequence(escaped);
                default:
                    return null;
            }
        }
    }

    /**
     * Defines the type of braces to use for this <i>article link</i>.
     * @author novotny
     * @version 1.0
     * @since 1.2, 04.06.2009
     * @lastChange $Date$ by $Author$
     */
    public enum BraceType {

        /** Curly braces: <i>{{...}}</i>. */
        CURLY, /** Default braces: <i>[[...]]</i>. */
        DEFAULT
    }

    /** The separator character that is used for chapters. */
    public static final String CHAPTER_SEPARATOR = "#";

    /** The separator character that is used for alias names. */
    public static final String ALIAS_SEPARATOR = "|";

    /**
     * This prefix determines links that link to an internal chapter of the
     * owner article. The special case here is that there is no article URL
     * information contained in the link text.
     */
    public static final String LINK_TO_INTERNAL_CHAPTER_PREFIX = "[[#";

    /**
     * This list contains invalid link prefixes. That means that if an article
     * URL starts with one of those prefixes, it cannot be treated like a valid
     * link. Invalid means that <code>RefactorDW</code> does not support those
     * link syntax. Please note that this list is not complete! It has been
     * created for fitting the needs of handling article links in
     * <code>RefactorDW</code>.
     */
    private static final List<String> INVALID_LINK_PREFIXES = new ArrayList<String>();

    static {
        INVALID_LINK_PREFIXES.add(Braces.INTRO_DEFAULT.getSequence(false) + "..");
        INVALID_LINK_PREFIXES.add(Braces.INTRO_DEFAULT.getSequence(false) + "::");
        INVALID_LINK_PREFIXES.add(Braces.INTRO_DEFAULT.getSequence(false) + "[");
        INVALID_LINK_PREFIXES.add(Braces.INTRO_DEFAULT.getSequence(false) + ":|");
        INVALID_LINK_PREFIXES.add(Braces.INTRO_CURLY.getSequence(false) + "..");
        INVALID_LINK_PREFIXES.add(Braces.INTRO_CURLY.getSequence(false) + "::");
        INVALID_LINK_PREFIXES.add(Braces.INTRO_CURLY.getSequence(false) + "{");
        INVALID_LINK_PREFIXES.add(Braces.INTRO_CURLY.getSequence(false) + ":|");
    }

    /** Separator for email addresses. */
    private static final String EMAIL_AT_SEPARATOR = "@";

    /** Relative link that is denoted by a dot. */
    public static final String DOT_RELATIVE_LINK = ".";

    /** The URL this link points to. */
    private WikiFileURL wikiFileURL;

    /**
     * This is the so called alias part of the link. It will be <code>null</code>
     * if there is no alias name for this link. Example:
     * <code>[[article_name|alias name]]</code>.
     */
    private String aliasName;

    /**
     * This is the so called chapter part to the link. It will be
     * <code>null</code> if there is no chapter information. Example:
     * <code>[[article_name#a chapter]]</code>
     */
    private String chapter;

    /**
     * The owner article is the article that contains the link text in its
     * content. It is used to reconstruct the {@link WikiFileURL}.
     */
    private Article owner;

    /** The textual String representing the article link. */
    private String textLink;

    /** The type of the braces of this <i>article link</i>. */
    private BraceType braceType;

    /**
     * Creates a new instance of an {@link ArticleLink}.
     * @param articleLink the complete <i>article link</i> in string
     *  representation
     * @param owner the article that contains the <code>articleLink</code> in
     *  its content. This is needed in order to create the complete article URL
     *  for the case that the link text string contains a relative link path
     * @return the new link instance or <code>null</code> if an error occured
     *  when parsing the string
     */
    public static ArticleLink createInstance(final String articleLink, final Article owner) {
        final ArticleLink link = new ArticleLink();
        try {
            link.parse(articleLink, owner);
        } catch (final Exception e) {
            LOGGER.error("ArticleLink.createInstance(...): " + e.getMessage(), e);
            return null;
        }
        return link;
    }

    /**
     * Throws an exception if the given link is invalid.
     * @param linkString the link string to be validated
     * @throws RefactorDWException if the link is not a valid DokuWiki article link
     */
    protected static void validateLink(final String linkString) throws RefactorDWException {
        if (!RegularExpressions.isLink(linkString)) {
            throw new RefactorDWException("Link string does not seem to be a valid DokuWiki article link: " + linkString);
        }
    }

    /** Constructor. */
    protected ArticleLink() {
        super();
    }

    /**
     * Parses the given article link String.
     * @param articleLink the link string to parse.
     * @param theOwner the article that contains the <code>articleLink</code> in
     *  its content
     * @throws RefactorDWException if the given link is not a valid link
     */
    private void parse(final String articleLink, final Article theOwner) throws RefactorDWException {
        if (articleLink == null) {
            throw new IllegalArgumentException("Parameter 'articleLink' must not be null");
        }
        if (theOwner == null) {
            throw new IllegalArgumentException("Parameter 'owner' must not be null");
        }
        textLink = articleLink;
        owner = theOwner;
        String localArticleLink = textLink;
        if (textLink.contains(DOT_RELATIVE_LINK)) {
            localArticleLink = cutOffDotAndUrlSeparator(textLink, Braces.INTRO_DEFAULT, URLSeparator.DEFAULT);
            if (localArticleLink == null) {
                localArticleLink = cutOffDotAndUrlSeparator(textLink, Braces.INTRO_DEFAULT, URLSeparator.ALTERNATIVE);
                if (localArticleLink == null) {
                    localArticleLink = cutOffDotAndUrlSeparator(textLink, Braces.INTRO_CURLY, URLSeparator.DEFAULT);
                    if (localArticleLink == null) {
                        localArticleLink = cutOffDotAndUrlSeparator(textLink, Braces.INTRO_CURLY, URLSeparator.ALTERNATIVE);
                        if (localArticleLink == null) {
                            localArticleLink = textLink;
                        }
                    }
                }
            }
        }
        validateLink(localArticleLink);
        braceType = extractBraceType(localArticleLink);
        wikiFileURL = extractArticleURL(localArticleLink, braceType, owner);
        aliasName = extractAliasName(localArticleLink);
        chapter = extractChapter(localArticleLink);
    }

    /**
     * Cuts off the dot and the URL separator from the given link, if the link
     * starts with the given braces, a dot and the URL separator.
     * @param link link to be examined
     * @param braces braces to be used for checking
     * @param urlSeparator URL separator to be used for checking
     * @return processed link, or <code>null</code> of the link does not match
     *  the pattern
     */
    private String cutOffDotAndUrlSeparator(final String link, final Braces braces, final URLSeparator urlSeparator) {
        final String pattern = braces.getSequence(false) + DOT_RELATIVE_LINK;
        if (link.startsWith(pattern)) {
            if (link.startsWith(pattern + urlSeparator.getSeparator())) {
                return link.substring(0, braces.getSequence(false).length()) + link.substring(pattern.length() + urlSeparator.getSeparator().length());
            }
            if (link.startsWith(pattern)) {
                int index = link.indexOf(ALIAS_SEPARATOR);
                if (index < 0) {
                    index = link.length() - braces.getSequence(false).length();
                }
                final String rest = (pattern.length() == index) ? "" : link.substring(pattern.length(), index).trim();
                if (rest.length() == 0) {
                    return link.substring(0, braces.getSequence(false).length()) + link.substring(pattern.length());
                }
            }
        }
        return null;
    }

    /**
     * Extracts the type of the braces from the given article link.
     * @param articleLink the <i>article link</i> in string representation
     * @return the type of the braces that are used in the link
     */
    protected BraceType extractBraceType(final String articleLink) {
        if (articleLink.startsWith(Braces.INTRO_DEFAULT.getSequence(false))) {
            return BraceType.DEFAULT;
        }
        if (articleLink.startsWith(Braces.INTRO_CURLY.getSequence(false))) {
            return BraceType.CURLY;
        }
        return null;
    }

    /**
     * Extracts the alias name of the given link string.
     * @param articleLink the link string to parse
     * @return the alias name of the link or <code>null</code>
     */
    protected String extractAliasName(final String articleLink) {
        final String simplifiedArticleLink = articleLink.substring(0, articleLink.length() - 1 - 1);
        final String regexpLink = "\\" + ALIAS_SEPARATOR + ".*";
        final Pattern pattern = Pattern.compile(regexpLink);
        final Matcher matcher = pattern.matcher(simplifiedArticleLink);
        String alias = null;
        if (matcher.find()) {
            final MatchResult mr = matcher.toMatchResult();
            alias = mr.group();
        }
        if (alias != null) {
            return alias.substring(1, alias.length());
        }
        return null;
    }

    /**
     * Extracts the chapter of the given link string.
     * @param articleLink the link string to parse
     * @return the chapter of the link or <code>null</code>
     */
    protected String extractChapter(final String articleLink) {
        final String regexpLink = "\\" + CHAPTER_SEPARATOR + ".*?[^\\|((\\]\\])|(\\}\\}))]+";
        final Pattern pattern = Pattern.compile(regexpLink);
        final Matcher matcher = pattern.matcher(articleLink);
        String chapter = null;
        if (matcher.find()) {
            final MatchResult mr = matcher.toMatchResult();
            chapter = mr.group();
        }
        if (chapter != null) {
            return chapter.substring(1, chapter.length());
        }
        return null;
    }

    /**
     * Extracts the article URL from the given article link string.
     * @param articleLink the link to extract the target article URL from
     * @param currentBraceType current brace type to be used
     * @param owner the article that contains the link in its content
     * @return the DokuWiki URL of the target article
     */
    protected WikiFileURL extractArticleURL(final String articleLink, final BraceType currentBraceType, final Article owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Parameter 'owner' must not be null");
        }
        if (articleLink.startsWith(LINK_TO_INTERNAL_CHAPTER_PREFIX)) {
            return new WikiFileURL(owner.getName(true), owner.getNamespace(), null);
        }
        if (isInvalid(articleLink)) {
            LOGGER.warn("ArticleLink.extractArticleURL(...): Invalid article link: \"" + articleLink + "\" in article \"" + owner.getWikiFileURL(URLSeparator.DEFAULT) + "\"");
            return null;
        }
        final String simplifiedArticleLink = articleLink.substring(Braces.getSequenceByType(currentBraceType, true, false).length());
        if (simplifiedArticleLink.trim().startsWith(ALIAS_SEPARATOR)) {
            final StringBuilder builder = new StringBuilder();
            final List<String> spaces = owner.getNamespace().getNamespaces();
            for (int i = 0; i < spaces.size(); ++i) {
                if (i > 0) {
                    builder.append(URLSeparator.DEFAULT.getSeparator());
                }
                builder.append(spaces.get(i));
            }
            builder.append(Article.FILE_EXTENSION);
            return new WikiFileURL(builder.toString(), owner.getRoot());
        }
        final String regexpLink = "[.]*[^\\|\\]\\}#]+";
        final Pattern pattern = Pattern.compile(regexpLink);
        final Matcher matcher = pattern.matcher(simplifiedArticleLink);
        String extractedDokuwikiURL;
        if (matcher.find()) {
            final MatchResult mr = matcher.toMatchResult();
            extractedDokuwikiURL = mr.group();
        } else {
            final RuntimeException ex = new RuntimeException("Unable to parse DokuWiki article link for DokuWiki URL: " + articleLink);
            LOGGER.error("ArticleLink.extractArticleURL(...): " + ex.getMessage(), ex);
            throw ex;
        }
        return new WikiFileURL(extractedDokuwikiURL, null);
    }

    /**
     * Returns <code>true</code> if this link is a relative link. A DokuWiki
     * article link is a relative link, if
     * <ul>
     * <li>there is no article URL information at all: the article link just
     *  links to a chapter in the owner article (e.g. <code>[[#chapter|alias]]</code>),
     * <li>there is no name space information contained in the link's text
     *  (e.g. <code>[[article_name|alias]]</code>) <b>and</b>
     * <li>the name space of the host article is not the root name space.
     * </ul>
     * Example: the link <code>[[start]]</code> could be either a relative link,
     * or a link from an article in the root name space to an article that is
     * also located in the root name space.
     * @return <code>true</code>, if relative
     * @throws IllegalStateException if this method is called, but no host name
     *  space is given (is <code>null</code>). Reason: it cannot be
     *  differentiated between relative and absolute links.
     */
    public boolean isRelative() throws IllegalStateException {
        if (textLink.startsWith(LINK_TO_INTERNAL_CHAPTER_PREFIX)) {
            return true;
        }
        if (textLink.startsWith(Braces.INTRO_DEFAULT.getSequence(false) + DOT_RELATIVE_LINK) || textLink.startsWith(Braces.INTRO_CURLY.getSequence(false) + DOT_RELATIVE_LINK)) {
            return true;
        }
        if (textLink.contains(EMAIL_AT_SEPARATOR) && wikiFileURL.getWikiFileName(true).contains(EMAIL_AT_SEPARATOR)) {
            return false;
        }
        if (isInvalid(textLink)) {
            return false;
        }
        final String intro1 = Braces.INTRO_DEFAULT.getSequence(false) + WikiFileURL.URLSeparator.DEFAULT.getSeparator();
        final String intro2 = Braces.INTRO_DEFAULT.getSequence(false) + WikiFileURL.URLSeparator.ALTERNATIVE.getSeparator();
        final String intro3 = Braces.INTRO_CURLY.getSequence(false) + WikiFileURL.URLSeparator.DEFAULT.getSeparator();
        final String intro4 = Braces.INTRO_CURLY.getSequence(false) + WikiFileURL.URLSeparator.ALTERNATIVE.getSeparator();
        if (textLink.startsWith(intro1) || textLink.startsWith(intro2) || textLink.startsWith(intro3) || textLink.startsWith(intro4)) {
            return false;
        }
        if (wikiFileURL.getNamespace().getNamespaces().size() != 0) {
            return false;
        }
        if (owner.getNamespace().getNamespaces().size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Returns <code>true</code> if this link is an invalid email address link.
     * @return <code>true</code> if the link is an invalid email address link,
     *  <code>false</code> otherwise
     */
    public boolean isInvalidEmailLink() {
        return textLink.contains(ArticleLink.EMAIL_AT_SEPARATOR) && !textLink.toLowerCase(Locale.getDefault()).startsWith("mailto:") && wikiFileURL.getWikiFileName(true).contains(ArticleLink.EMAIL_AT_SEPARATOR);
    }

    /**
     * Returns if the given link is invalid. Invalid Links won't be re-factored
     * by <code>RefactorDW</code>. A link is invalid, if it contains strings in
     * its article link text that are not supported by <code>RefactorDW</code>.<p>
     * Examples:
     * <ul>
     * <li> [[ | empty link]]
     * <li> [[.|empty link]]
     * </ul>
     * @param link link to be checked
     * @return <code>true</code>, if there is no article URL information included
     */
    public static boolean isInvalid(final String link) {
        for (final String prefix : INVALID_LINK_PREFIXES) {
            if (link.startsWith(prefix)) {
                return true;
            }
        }
        if (link.equals(Braces.INTRO_DEFAULT.getSequence(false) + "." + Braces.EXTRO_DEFAULT.getSequence(false))) {
            return true;
        }
        if (link.equals(Braces.INTRO_CURLY.getSequence(false) + "." + Braces.EXTRO_CURLY.getSequence(false))) {
            return true;
        }
        return false;
    }

    /**
     * Returns the type of the braces of this <i>article link</i>.
     * @return the type of the braces
     */
    public BraceType getBraceType() {
        return braceType;
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(Braces.getSequenceByType(braceType, true, false));
        if (wikiFileURL != null) {
            final String urlString = wikiFileURL.getURL(WikiFileURL.URLSeparator.DEFAULT);
            sb.append(urlString);
        }
        if ((chapter != null) && !chapter.isEmpty()) {
            sb.append(CHAPTER_SEPARATOR);
            sb.append(chapter);
        }
        if ((aliasName != null) && !aliasName.isEmpty()) {
            sb.append(ALIAS_SEPARATOR);
            sb.append(aliasName);
        }
        sb.append(Braces.getSequenceByType(braceType, false, false));
        return sb.toString();
    }

    /**
     * Returns the Wiki file URL.
     * @return the Wiki file URL
     */
    public WikiFileURL getWikiFileURL() {
        return wikiFileURL;
    }

    /**
     * Sets the Wiki file URL.
     * @param wikiFileURL the Wiki file URL to set, never <code>null</code>
     */
    public void setWikiFileURL(final WikiFileURL wikiFileURL) {
        if (wikiFileURL == null) {
            throw new IllegalArgumentException("Parameter 'wikiFileURL' must not be null");
        }
        this.wikiFileURL = wikiFileURL;
    }

    /**
     * Returns the alias name.
     * @return aliasName the alias name, may be <code>null</code> if there is no
     *  alias
     */
    public String getAliasName() {
        return aliasName;
    }

    /**
     * Sets the alias name.
     * @param aliasName the alias name to set, may be <code>null</code> if there
     *  is no alias
     */
    public void setAliasName(final String aliasName) {
        this.aliasName = aliasName;
    }

    /**
     * Returns the chapter.
     * @return chapter
     */
    public String getChapter() {
        return chapter;
    }

    /**
     * Sets the chapter.
     * @param chapter the chapter to set, may be <code>null</code> if there is
     *  no chapter
     */
    public void setChapter(final String chapter) {
        this.chapter = chapter;
    }

    /**
     * Returns the original text link as it has been extracted from the
     * article's text content.
     * @return the original text link
     */
    public String getOriginalTextLink() {
        return textLink;
    }

    /**
     * Sets the type of the braces to use.
     * @param type the brace's type
     */
    public void setBraceType(final BraceType type) {
        braceType = type;
    }
}
