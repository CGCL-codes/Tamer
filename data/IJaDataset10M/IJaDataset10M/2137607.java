package docBuilder.wiki;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.mylyn.wikitext.core.parser.markup.MarkupLanguage;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Implementation of a WikiPage Special here a MediaWikiPage for most common use cases
 */
public class MediaWikiPageImpl implements WikiPage {

    private final Document oDocument;

    private final MarkupLanguage oDialect = new MediaWikiLanguage();

    private final int oNodeNumber;

    /**
     * @param file to parse the xml content
     * @throws ParserConfigurationException if parser is not configured well
     * @throws SAXException                 if an error occurs during parsing
     * @throws IOException                  at any file operation error
     */
    public MediaWikiPageImpl(final File file) throws ParserConfigurationException, SAXException, IOException {
        this(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file));
    }

    /**
     * @param document xml
     * @param number   of pages to be parsed
     */
    public MediaWikiPageImpl(final Document document, final int number) {
        oDocument = document;
        oNodeNumber = number;
    }

    /**
     * @param document xml
     */
    public MediaWikiPageImpl(final Document document) {
        this(document, 0);
    }

    /**
     * @return title of the MediaWiki-Page
     */
    public String getTitle() {
        return getContentFromTag("title");
    }

    /**
     * @return markup of the MediaWiki-Page
     */
    public String getMarkup() {
        return getContentFromTag("text");
    }

    /**
     * @return the dialect which is always MediaWikiDialect
     * @see org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage
     */
    public final MarkupLanguage getDialect() {
        return oDialect;
    }

    /**
     * @return timestamp of last change
     */
    public String getTimeStamp() {
        return getContentFromTag("timestamp");
    }

    @Override
    public String toString() {
        return getTitle() + " " + getTimeStamp();
    }

    private String getContentFromTag(final String name) {
        return oDocument.getElementsByTagName(name).item(oNodeNumber).getTextContent();
    }
}
