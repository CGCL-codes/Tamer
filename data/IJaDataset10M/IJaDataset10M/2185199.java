package org.wikimodel.wem.mediawiki;

import java.io.Reader;
import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.impl.WikiScannerContext;
import org.wikimodel.wem.mediawiki.javacc.MediawikiScanner;
import org.wikimodel.wem.mediawiki.javacc.ParseException;

/**
 * @author MikhailKotelnikov
 */
public class MediaWikiParser implements IWikiParser {

    /**
     * 
     */
    public MediaWikiParser() {
        super();
    }

    /**
     * @see org.wikimodel.wem.IWikiParser#parse(java.io.Reader,
     *      org.wikimodel.wem.IWemListener)
     */
    public void parse(Reader reader, IWemListener listener) throws WikiParserException {
        try {
            MediawikiScanner scanner = new MediawikiScanner(reader);
            WikiScannerContext context = new WikiScannerContext(listener);
            scanner.parse(context);
        } catch (ParseException e) {
            throw new WikiParserException(e);
        }
    }
}
