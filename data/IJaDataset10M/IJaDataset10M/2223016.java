package net.sf.mmm.content.parser.impl.poi;

import javax.inject.Named;
import javax.inject.Singleton;
import net.sf.mmm.content.parser.api.ContentParserOptions;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * This is the implementation of the
 * {@link net.sf.mmm.content.parser.api.ContentParser} interface for binary
 * MS-Word documents.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
@Singleton
@Named
public class ContentParserDoc extends AbstractContentParserPoi {

    /** The mimetype. */
    public static final String KEY_MIMETYPE = "application/msword";

    /** The default extension. */
    public static final String KEY_EXTENSION = "doc";

    /**
   * The constructor.
   */
    public ContentParserDoc() {
        super();
    }

    /**
   * {@inheritDoc}
   */
    public String getExtension() {
        return KEY_EXTENSION;
    }

    /**
   * {@inheritDoc}
   */
    public String getMimetype() {
        return KEY_MIMETYPE;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public String[] getAlternativeKeyArray() {
        return new String[] { "dot" };
    }

    /**
   * {@inheritDoc}
   */
    @Override
    protected String extractText(POIFSFileSystem poiFs, long filesize, ContentParserOptions options) throws Exception {
        WordExtractor extractor = new WordExtractor(poiFs);
        return extractor.getText();
    }
}
