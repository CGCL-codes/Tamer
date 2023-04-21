package au.gov.naa.digipres.xena.plugin.naa;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;
import au.gov.naa.digipres.xena.javatools.FileName;
import au.gov.naa.digipres.xena.kernel.MultiInputSource;
import au.gov.naa.digipres.xena.kernel.XenaInputSource;
import au.gov.naa.digipres.xena.kernel.metadatawrapper.AbstractMetaDataWrapper;
import au.gov.naa.digipres.xena.util.UrlEncoder;

/**
 * Wrap the XML with NAA approved meta-data.
 *
 */
public class NaaInnerWrapNormaliser extends XMLFilterImpl {

    private SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private AbstractMetaDataWrapper parent;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private String packageURI = "";

    public NaaInnerWrapNormaliser(AbstractMetaDataWrapper parent) {
        super();
        this.parent = parent;
    }

    @Override
    public void startDocument() throws org.xml.sax.SAXException {
        String fileName;
        char[] id;
        XMLReader normaliser = (XMLReader) getProperty("http://xena/normaliser");
        if (normaliser == null) {
            throw new SAXException("http://xena/normaliser is not set for Package Wrapper");
        }
        boolean isBinary = normaliser.getClass().getName().equals("au.gov.naa.digipres.xena.plugin.basic.BinaryToXenaBinaryNormaliser");
        XenaInputSource xis = (XenaInputSource) getProperty("http://xena/input");
        super.startDocument();
        File outfile = ((File) getProperty("http://xena/file"));
        ContentHandler th = getContentHandler();
        AttributesImpl att = new AttributesImpl();
        th.startElement(packageURI, NaaTagNames.PACKAGE, NaaTagNames.PACKAGE_PACKAGE, att);
        th.startElement(packageURI, NaaTagNames.META, NaaTagNames.PACKAGE_META, att);
        th.startElement(NaaTagNames.NAA_URI, NaaTagNames.WRAPPER, NaaTagNames.NAA_WRAPPER, att);
        th.characters(NaaTagNames.NAA_PACKAGE.toCharArray(), 0, NaaTagNames.NAA_PACKAGE.toCharArray().length);
        th.endElement(NaaTagNames.NAA_URI, NaaTagNames.WRAPPER, NaaTagNames.NAA_WRAPPER);
        th.startElement(NaaTagNames.DCTERMS_URI, NaaTagNames.CREATED, NaaTagNames.DCCREATED, att);
        char[] sDate = isoDateFormat.format(new java.util.Date(System.currentTimeMillis())).toCharArray();
        th.characters(sDate, 0, sDate.length);
        th.endElement(NaaTagNames.DCTERMS_URI, NaaTagNames.CREATED, NaaTagNames.DCCREATED);
        if (xis.getFile() != null || outfile != null) {
            if (outfile != null) {
                th.startElement(NaaTagNames.DC_URI, NaaTagNames.IDENTIFIER, NaaTagNames.DCIDENTIFIER, att);
                fileName = xis.getOutputFileName().substring(0, xis.getOutputFileName().lastIndexOf('.'));
                id = fileName.toCharArray();
                th.characters(id, 0, id.length);
                th.endElement(NaaTagNames.DC_URI, NaaTagNames.IDENTIFIER, NaaTagNames.DCIDENTIFIER);
            }
            th.startElement(NaaTagNames.NAA_URI, NaaTagNames.DATASOURCES, NaaTagNames.NAA_DATASOURCES, att);
            {
                List<XenaInputSource> xenaInputSourceList = new ArrayList<XenaInputSource>();
                if (xis instanceof MultiInputSource) {
                    Iterator it = ((MultiInputSource) xis).getSystemIds().iterator();
                    while (it.hasNext()) {
                        String url = (String) it.next();
                        xenaInputSourceList.add(new XenaInputSource(url, null));
                    }
                } else {
                    xenaInputSourceList.add(xis);
                }
                Iterator it = xenaInputSourceList.iterator();
                while (it.hasNext()) {
                    XenaInputSource source = (XenaInputSource) it.next();
                    th.startElement(NaaTagNames.NAA_URI, NaaTagNames.DATASOURCE, NaaTagNames.NAA_DATASOURCE, att);
                    XenaInputSource relsource = null;
                    try {
                        java.net.URI uri = new java.net.URI(source.getSystemId());
                        if (uri.getScheme().equals("file")) {
                            File file = new File(uri);
                            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss");
                            char[] lastModStr = sdf.format(new Date(file.lastModified())).toCharArray();
                            th.startElement(NaaTagNames.NAA_URI, NaaTagNames.LASTMODIFIED, NaaTagNames.NAA_LASTMODIFIED, att);
                            th.characters(lastModStr, 0, lastModStr.length);
                            th.endElement(NaaTagNames.NAA_URI, NaaTagNames.LASTMODIFIED, NaaTagNames.NAA_LASTMODIFIED);
                            String relativePath = null;
                            File baseDir;
                            if (parent.getMetaDataWrapperManager().getPluginManager().getMetaDataWrapperManager().getBasePathName() != null) {
                                try {
                                    baseDir = new File(parent.getMetaDataWrapperManager().getPluginManager().getMetaDataWrapperManager().getBasePathName());
                                    if (baseDir != null) {
                                        relativePath = FileName.relativeTo(baseDir, file);
                                    }
                                } catch (IOException iox) {
                                    relativePath = null;
                                }
                            }
                            if (relativePath == null) {
                                relativePath = file.getName();
                            }
                            String encodedPath = null;
                            try {
                                encodedPath = UrlEncoder.encode(relativePath);
                            } catch (UnsupportedEncodingException x) {
                                throw new SAXException(x);
                            }
                            relsource = new XenaInputSource(new java.net.URI("file:/" + encodedPath).toASCIIString(), null);
                        } else {
                            relsource = source;
                        }
                    } catch (java.net.URISyntaxException x) {
                        x.printStackTrace();
                    }
                    th.startElement(NaaTagNames.DC_URI, NaaTagNames.SOURCE, NaaTagNames.DCSOURCE, att);
                    char[] src = relsource.getSystemId().toCharArray();
                    th.characters(src, 0, src.length);
                    th.endElement(NaaTagNames.DC_URI, NaaTagNames.SOURCE, NaaTagNames.DCSOURCE);
                    if (isBinary) {
                        char[] typename = "binary data".toCharArray();
                        th.startElement(NaaTagNames.NAA_URI, NaaTagNames.TYPE, NaaTagNames.NAA_TYPE, att);
                        th.characters(typename, 0, typename.length);
                        th.endElement(NaaTagNames.NAA_URI, NaaTagNames.TYPE, NaaTagNames.NAA_TYPE);
                    }
                    th.endElement(NaaTagNames.NAA_URI, NaaTagNames.DATASOURCE, NaaTagNames.NAA_DATASOURCE);
                }
            }
            th.endElement(NaaTagNames.NAA_URI, NaaTagNames.DATASOURCES, NaaTagNames.NAA_DATASOURCES);
        }
        th.endElement(packageURI, NaaTagNames.META, NaaTagNames.PACKAGE_META);
        th.startElement(packageURI, "content", "package:content", att);
    }

    @Override
    public void endDocument() throws org.xml.sax.SAXException {
        XenaInputSource xis = (XenaInputSource) getProperty("http://xena/input");
        File outfile = ((File) getProperty("http://xena/file"));
        ContentHandler th = getContentHandler();
        th.endElement(packageURI, "content", "package:content");
        th.endElement(packageURI, NaaTagNames.PACKAGE, NaaTagNames.PACKAGE_PACKAGE);
        super.endDocument();
    }

    /**
	 * @return the packageURI
	 */
    public String getPackageURI() {
        return packageURI;
    }

    /**
	 * @param packageURI the packageURI to set
	 */
    public void setPackageURI(String packageURI) {
        this.packageURI = packageURI;
    }
}
