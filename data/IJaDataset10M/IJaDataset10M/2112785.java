package org.archive.processors.extractor;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import org.archive.processors.ProcessorURI;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.readers.SWFReader;
import com.anotherbigidea.flash.readers.TagParser;
import com.anotherbigidea.io.InStream;

/**
 * Extracts URIs from SWF (flash/shockwave) files.
 * 
 * To test, here is a link to an swf that has links
 * embedded inside of it: http://www.hitspring.com/index.swf.
 *
 * @author Igor Ranitovic
 */
public class ExtractorSWF extends ContentExtractor {

    private static final long serialVersionUID = 3L;

    private static Logger logger = Logger.getLogger(ExtractorSWF.class.getName());

    protected AtomicLong linksExtracted = new AtomicLong(0);

    private static final int MAX_READ_SIZE = 1024 * 1024;

    /**
     * @param name
     */
    public ExtractorSWF() {
    }

    @Override
    protected boolean shouldExtract(ProcessorURI uri) {
        String contentType = uri.getContentType();
        if (contentType == null) {
            return false;
        }
        if ((contentType.toLowerCase().indexOf("x-shockwave-flash") < 0) && (!uri.toString().toLowerCase().endsWith(".swf"))) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean innerExtract(ProcessorURI curi) {
        InputStream documentStream = null;
        try {
            documentStream = curi.getRecorder().getRecordedInput().getContentReplayInputStream();
            if (documentStream == null) {
                return false;
            }
            CrawlUriSWFAction curiAction = new CrawlUriSWFAction(curi);
            CustomSWFTags customTags = new CustomSWFTags(curiAction);
            SWFReader reader = new SWFReader(getTagParser(customTags), documentStream) {

                public int readOneTag() throws IOException {
                    int header = mIn.readUI16();
                    int type = header >> 6;
                    int length = header & 0x3F;
                    boolean longTag = (length == 0x3F);
                    if (longTag) {
                        length = (int) mIn.readUI32();
                    }
                    if (length > MAX_READ_SIZE) {
                        mIn.skipBytes(length);
                        logger.info("oversized SWF tag (type=" + type + ";length=" + length + ") skipped");
                    } else {
                        byte[] contents = mIn.read(length);
                        mConsumer.tag(type, longTag, contents);
                    }
                    return type;
                }
            };
            reader.readFile();
            linksExtracted.addAndGet(curiAction.getLinkCount());
            logger.fine(curi + " has " + curiAction.getLinkCount() + " links.");
        } catch (IOException e) {
            curi.getNonFatalFailures().add(e);
        } finally {
            try {
                documentStream.close();
            } catch (IOException e) {
                curi.getNonFatalFailures().add(e);
            }
        }
        return true;
    }

    public String report() {
        StringBuffer ret = new StringBuffer();
        ret.append("Processor: org.archive.crawler.extractor.ExtractorSWF\n");
        ret.append("  Function:          Link extraction on Shockwave Flash " + "documents (.swf)\n");
        ret.append("  CrawlURIs handled: " + getURICount() + "\n");
        ret.append("  Links extracted:   " + linksExtracted + "\n\n");
        return ret.toString();
    }

    /**
     * Get a TagParser
     * 
     * A custom ExtractorTagParser which ignores all the big binary image/
     * sound/font types which don't carry URLs is used, to avoid the 
     * occasionally fatal (OutOfMemoryError) memory bloat caused by the
     * all-in-memory SWF library handling. 
     * 
     * @param customTags A custom tag parser.
     * @return An SWFReader.
     */
    private TagParser getTagParser(CustomSWFTags customTags) {
        return new ExtractorTagParser(customTags);
    }

    /**
     * TagParser customized to ignore SWFTags that 
     * will never contain extractable URIs. 
     */
    protected class ExtractorTagParser extends TagParser {

        protected ExtractorTagParser(SWFTagTypes tagtypes) {
            super(tagtypes);
        }

        protected void parseDefineBits(InStream in) throws IOException {
        }

        protected void parseDefineBitsJPEG3(InStream in) throws IOException {
        }

        protected void parseDefineBitsLossless(InStream in, int length, boolean hasAlpha) throws IOException {
        }

        protected void parseDefineButtonSound(InStream in) throws IOException {
        }

        protected void parseDefineFont(InStream in) throws IOException {
        }

        protected void parseDefineJPEG2(InStream in, int length) throws IOException {
        }

        protected void parseDefineJPEGTables(InStream in) throws IOException {
        }

        protected void parseDefineShape(int type, InStream in) throws IOException {
        }

        protected void parseDefineSound(InStream in) throws IOException {
        }

        protected void parseFontInfo(InStream in, int length, boolean isFI2) throws IOException {
        }

        protected void parseDefineFont2(InStream in) throws IOException {
        }
    }
}
