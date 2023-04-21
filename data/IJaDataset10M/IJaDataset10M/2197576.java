package org.openscience.cdk.io.chemrss;

import java.io.StringReader;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemModel;
import org.openscience.cdk.ChemSequence;
import org.openscience.cdk.io.CMLReader;
import org.openscience.cdk.io.ChemicalRSSReader;
import org.openscience.cdk.tools.LoggingTool;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX2 implementation for a RSS handler. Data is stored into a ChemSequence
 * where each channel item is one ChemModel in this sequence.
 *
 * @cdk.module io
 *
 * @author  Egon Willighagen <egonw@sci.kun.nl>
 * @cdk.created 2003-09-07
 */
public class RSSHandler extends DefaultHandler {

    private LoggingTool logger;

    private ChemSequence channelSequence;

    private String cmlString;

    private String cData;

    private String whiteSpace;

    private boolean readdedNamespace;

    private String objectTitle;

    private String objectDesc;

    private String objectDate;

    private String objectLink;

    private String dcCreator;

    private String dcPublisher;

    /**
     * Constructor for the RSSHandler.
     */
    public RSSHandler() {
        logger = new LoggingTool(this);
    }

    public ChemSequence getChemSequence() {
        return channelSequence;
    }

    public void characters(char ch[], int start, int length) {
        if (cData == null) {
            cData = new String();
        }
        cData += new String(ch, start, length);
    }

    public void ignorableWhitespace(char ch[], int start, int length) {
        logger.debug("Ignorable whitespace found of length: ", length);
        if (whiteSpace == null) {
            whiteSpace = new String();
        }
        whiteSpace += new String(ch, start, length);
    }

    public void doctypeDecl(String name, String publicId, String systemId) throws Exception {
    }

    public void startDocument() {
        channelSequence = new ChemSequence();
        cmlString = "";
        whiteSpace = null;
        readdedNamespace = false;
        resetStrings();
    }

    public void endDocument() {
    }

    public void endElement(String uri, String local, String raw) {
        logger.debug("</" + raw + ">");
        if (uri.equals("http://www.xml-cml.org/schema/cml2/core")) {
            cmlString += cData;
            cmlString += toEndTag(raw);
        } else if (uri.equals("http://purl.org/dc/elements/1.1/")) {
            if (local.equals("publisher")) {
                dcPublisher = cData;
            } else if (local.equals("creator")) {
                dcCreator = cData;
            } else if (local.equals("date")) {
                objectDate = cData;
            }
        } else if (uri.equals("http://purl.org/rss/1.0/")) {
            if (local.equals("item")) {
                org.openscience.cdk.interfaces.ChemModel model = null;
                if (cmlString.length() > 0) {
                    StringReader reader = new StringReader(cmlString);
                    logger.debug("Parsing CML String: ", cmlString);
                    CMLReader cmlReader = new CMLReader(reader);
                    try {
                        ChemFile file = (ChemFile) cmlReader.read(new ChemFile());
                        if (file.getChemSequenceCount() > 0) {
                            org.openscience.cdk.interfaces.ChemSequence sequence = file.getChemSequence(0);
                            if (sequence.getChemModelCount() > 0) {
                                model = sequence.getChemModel(0);
                            } else {
                                logger.warn("ChemSequence contains no ChemModel");
                            }
                            if (model.getSetOfMolecules() != null) {
                                if (model.getSetOfMolecules().getMoleculeCount() > 0) {
                                    org.openscience.cdk.interfaces.Molecule molecule = model.getSetOfMolecules().getMolecule(0);
                                    String inchi = (String) molecule.getProperty("iupac.nist.chemical.identifier");
                                    if (inchi != null) model.setProperty(ChemicalRSSReader.RSS_ITEM_INCHI, inchi);
                                } else {
                                    logger.warn("ChemModel does not contain Molecule; could not extract INChI");
                                }
                            }
                        } else {
                            logger.warn("ChemFile contains no ChemSequene");
                        }
                    } catch (Exception exception) {
                        logger.error("Error while parsing CML");
                        logger.debug(exception);
                    }
                    model.setProperty(ChemicalRSSReader.RSS_ITEM_SOURCE, cmlString);
                } else {
                    logger.warn("No CML content found");
                }
                if (model == null) {
                    logger.warn("Read empty model");
                    model = new ChemModel();
                }
                model.setProperty(ChemicalRSSReader.RSS_ITEM_TITLE, objectTitle);
                model.setProperty(ChemicalRSSReader.RSS_ITEM_DATE, objectDate);
                model.setProperty(ChemicalRSSReader.RSS_ITEM_LINK, objectLink);
                if (objectDesc != null) model.setProperty(ChemicalRSSReader.RSS_ITEM_DESCRIPTION, objectDesc);
                model.setProperty(ChemicalRSSReader.RSS_ITEM_CREATOR, dcCreator);
                channelSequence.addChemModel(model);
                cmlString = "";
            } else if (local.equals("title")) {
                objectTitle = cData;
            } else if (local.equals("link")) {
                objectLink = cData;
            } else if (local.equals("description")) {
                objectDesc = cData;
            } else if (local.equals("channel")) {
                channelSequence.setProperty(ChemicalRSSReader.RSS_CHANNEL_TITLE, objectTitle);
                channelSequence.setProperty(ChemicalRSSReader.RSS_CHANNEL_WEBSITE, objectLink);
                channelSequence.setProperty(ChemicalRSSReader.RSS_CHANNEL_DESCRIPTION, objectDesc);
                channelSequence.setProperty(ChemicalRSSReader.RSS_CHANNEL_PUBLISHER, dcPublisher);
                channelSequence.setProperty(ChemicalRSSReader.RSS_CHANNEL_CREATOR, dcCreator);
            } else {
                logger.debug("Unparsed RSS element: " + local);
            }
        } else {
            logger.debug("Unparsed element: " + local);
            logger.debug("  uri: " + uri);
        }
        cData = null;
        whiteSpace = null;
    }

    public void startElement(String uri, String local, String raw, Attributes atts) {
        logger.debug("<" + raw + ">");
        if (uri.equals("http://www.xml-cml.org/schema/cml2/core")) {
            if (whiteSpace != null) {
                cmlString += whiteSpace;
            }
            if (cData != null) {
                cmlString += cData;
            }
            if (readdedNamespace) {
                cmlString += toStartTag(raw, atts);
            } else {
                cmlString += toStartTag(raw, atts, uri);
            }
        } else if (local.equals("item") || local.equals("channel")) {
            resetStrings();
        }
        cData = null;
        whiteSpace = null;
    }

    private void resetStrings() {
        objectTitle = "";
        objectDesc = "";
        objectDate = "";
        objectLink = "";
        dcCreator = "";
        dcPublisher = "";
    }

    private String toStartTag(String raw, Attributes atts) {
        return toStartTag(raw, atts, null);
    }

    private String toStartTag(String raw, Attributes atts, String uri) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<");
        buffer.append(raw);
        for (int i = 0; i < atts.getLength(); i++) {
            buffer.append(" ");
            String qName = atts.getQName(i);
            buffer.append(qName);
            buffer.append("=\"");
            String value = atts.getValue(i);
            buffer.append(value);
            buffer.append("\"");
        }
        if (uri != null) {
            buffer.append(" ");
            if (raw.indexOf(":") != -1) {
                buffer.append("xmlns:");
                String namespace = raw.substring(0, raw.indexOf(":"));
                buffer.append(namespace);
            } else {
                buffer.append("xmlns");
            }
            buffer.append("=\"");
            buffer.append(uri);
            buffer.append("\"");
        }
        buffer.append(">");
        return buffer.toString();
    }

    private String toEndTag(String raw) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("</");
        buffer.append(raw);
        buffer.append(">");
        return buffer.toString();
    }
}
