package gate.corpora;

import gate.Document;
import gate.GateConstants;
import gate.Resource;
import gate.TextualDocument;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.AutoInstance;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.event.StatusListener;
import gate.html.NekoHtmlDocumentHandler;
import gate.util.DocumentFormatException;
import gate.util.Out;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.cyberneko.html.HTMLConfiguration;

/**
 * <p>
 * DocumentFormat that uses Andy Clark's <a
 * href="http://people.apache.org/~andyc/neko/doc/html/">NekoHTML</a>
 * parser to parse HTML documents. It tries to render HTML in a similar
 * way to a web browser, i.e. whitespace is normalized, paragraphs are
 * separated by a blank line, etc. By default the text content of style
 * and script tags is ignored completely, though the set of tags treated
 * in this way is configurable via a CREOLE parameter.
 * </p>
 */
@CreoleResource(name = "GATE HTML Document Format", isPrivate = true, autoinstances = { @AutoInstance(hidden = true) })
public class NekoHtmlDocumentFormat extends TextualDocumentFormat {

    /** Debug flag */
    private static final boolean DEBUG = false;

    /** Default construction */
    public NekoHtmlDocumentFormat() {
        super();
    }

    /**
   * The set of tags whose text content is to be ignored when parsing.
   */
    private Set<String> ignorableTags = null;

    @CreoleParameter(comment = "HTML tags whose text content should be ignored", defaultValue = "script;style")
    public void setIgnorableTags(Set<String> newTags) {
        this.ignorableTags = newTags;
    }

    public Set<String> getIgnorableTags() {
        return ignorableTags;
    }

    /**
   * We support repositioning info for HTML files.
   */
    public Boolean supportsRepositioning() {
        return Boolean.TRUE;
    }

    /**
   * Old-style unpackMarkup, without repositioning info.
   */
    public void unpackMarkup(Document doc) throws DocumentFormatException {
        unpackMarkup(doc, null, null);
    }

    /**
   * Unpack the markup in the document. This converts markup from the
   * native format into annotations in GATE format. If the document was
   * created from a String, then is recomandable to set the doc's
   * sourceUrl to <b>null</b>. So, if the document has a valid URL,
   * then the parser will try to parse the XML document pointed by the
   * URL.If the URL is not valid, or is null, then the doc's content
   * will be parsed. If the doc's content is not a valid XML then the
   * parser might crash.
   *
   * @param doc The gate document you want to parse. If
   *          <code>doc.getSourceUrl()</code> returns <b>null</b>
   *          then the content of doc will be parsed. Using a URL is
   *          recomended because the parser will report errors corectlly
   *          if the document is not well formed.
   */
    public void unpackMarkup(Document doc, RepositioningInfo repInfo, RepositioningInfo ampCodingInfo) throws DocumentFormatException {
        if ((doc == null) || (doc.getSourceUrl() == null && doc.getContent() == null)) {
            throw new DocumentFormatException("GATE document is null or no content found. Nothing to parse!");
        }
        StatusListener statusListener = new StatusListener() {

            public void statusChanged(String text) {
                fireStatusChanged(text);
            }
        };
        boolean docHasContentButNoValidURL = hasContentButNoValidUrl(doc);
        NekoHtmlDocumentHandler handler = null;
        try {
            org.cyberneko.html.HTMLConfiguration parser = new HTMLConfiguration();
            parser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
            parser.setProperty("http://cyberneko.org/html/properties/names/attrs", "lower");
            parser.setFeature(NekoHtmlDocumentHandler.AUGMENTATIONS, true);
            handler = new NekoHtmlDocumentHandler(doc, null, ignorableTags);
            handler.addStatusListener(statusListener);
            handler.setRepositioningInfo(repInfo);
            handler.setAmpCodingInfo(ampCodingInfo);
            int[] lineOffsets = buildLineOffsets(doc.getContent().toString());
            handler.setLineOffsets(lineOffsets);
            parser.setDocumentHandler(handler);
            parser.setErrorHandler(handler);
            XMLInputSource is;
            if (docHasContentButNoValidURL) {
                is = new XMLInputSource(null, null, null, new StringReader(doc.getContent().toString()), null);
            } else if (doc instanceof TextualDocument) {
                String docEncoding = ((TextualDocument) doc).getEncoding();
                Reader docReader = new InputStreamReader(doc.getSourceUrl().openStream(), docEncoding);
                is = new XMLInputSource(null, doc.getSourceUrl().toString(), doc.getSourceUrl().toString(), docReader, docEncoding);
                parser.setFeature("http://cyberneko.org/html/features/scanner/ignore-specified-charset", true);
            } else {
                is = new XMLInputSource(null, doc.getSourceUrl().toString(), doc.getSourceUrl().toString());
            }
            parser.parse(is);
            ((DocumentImpl) doc).setNextAnnotationId(handler.getCustomObjectsId());
        } catch (IOException e) {
            throw new DocumentFormatException("I/O exception for " + doc.getSourceUrl().toString(), e);
        } catch (Exception e) {
            doc.getFeatures().put("parsingError", Boolean.TRUE);
            Boolean bThrow = (Boolean) doc.getFeatures().get(GateConstants.THROWEX_FORMAT_PROPERTY_NAME);
            if (bThrow != null && bThrow.booleanValue()) {
                throw new DocumentFormatException(e);
            } else {
                Out.println("Warning: Document remains unparsed. \n" + "\n  Stack Dump: ");
                e.printStackTrace(Out.getPrintWriter());
            }
        } finally {
            if (handler != null) handler.removeStatusListener(statusListener);
        }
    }

    /**
   * Pattern that matches the beginning of every line in a multi-line
   * string. The regular expression engine handles the different types
   * of newline characters (\n, \r\n or \r) automatically.
   */
    private static Pattern afterNewlinePattern = Pattern.compile("^", Pattern.MULTILINE);

    /**
   * Build an array giving the starting character offset of each line in
   * the document. The HTML parser only reports event positions as line
   * and column numbers, so we need this information to be able to
   * correctly infer the repositioning information.
   *
   * @param docContent
   * @return
   */
    private int[] buildLineOffsets(String docContent) {
        Matcher m = afterNewlinePattern.matcher(docContent);
        int numMatches = 0;
        while (m.find()) {
            if (DEBUG) {
                System.out.println("found line starting at offset " + m.start());
            }
            numMatches++;
        }
        int[] lineOffsets = new int[numMatches];
        m.reset();
        for (int i = 0; i < lineOffsets.length; i++) {
            m.find();
            lineOffsets[i] = m.start();
        }
        return lineOffsets;
    }

    /** Initialise this resource, and return it. */
    public Resource init() throws ResourceInstantiationException {
        MimeType mime = new MimeType("text", "html");
        mimeString2ClassHandlerMap.put(mime.getType() + "/" + mime.getSubtype(), this);
        mimeString2mimeTypeMap.put(mime.getType() + "/" + mime.getSubtype(), mime);
        suffixes2mimeTypeMap.put("html", mime);
        suffixes2mimeTypeMap.put("htm", mime);
        magic2mimeTypeMap.put("<html", mime);
        setMimeType(mime);
        return this;
    }
}
