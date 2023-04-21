package org.gvsig.remoteClient.wcs.wcs_1_0_0;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.utils.DescribeCoverageTags;
import org.gvsig.remoteClient.utils.EncodingXMLParser;
import org.gvsig.remoteClient.wcs.CoverageOfferingBrief;
import org.gvsig.remoteClient.wcs.WCSProtocolHandler;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author jaume
 *
 */
public class WCSProtocolHandler1_0_0 extends WCSProtocolHandler {

    public WCSProtocolHandler1_0_0() {
        this.version = "1.0.0";
        this.name = "WCS1.0.0";
        this.serviceInfo = new ServiceInformation();
    }

    /**
     * <p>Parse the xml data retrieved from the WCS, it will parse the WCS Capabilities</p>
     *
     */
    public boolean parseCapabilities(File f) {
        int tag;
        EncodingXMLParser parser = null;
        parser = new EncodingXMLParser();
        try {
            parser.setInput(f);
            parser.nextTag();
            if (parser.getEventType() != KXmlParser.END_DOCUMENT) {
                parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.WCS_CAPABILITIES_ROOT1_0_0);
                tag = parser.nextTag();
                while (tag != KXmlParser.END_DOCUMENT) {
                    switch(tag) {
                        case KXmlParser.START_TAG:
                            if (parser.getName().compareTo(CapabilitiesTags.SERVICE) == 0) {
                                parseServiceTag(parser);
                            } else if (parser.getName().compareTo(CapabilitiesTags.CAPABILITY) == 0) {
                                parseCapabilityTag(parser);
                            } else if (parser.getName().compareTo(CapabilitiesTags.WCS_CONTENTMETADATA) == 0) {
                                parseContentMetadataTag(parser);
                            }
                            break;
                        case KXmlParser.END_TAG:
                            break;
                        case KXmlParser.TEXT:
                            if (parser.getName() != null) System.out.println("[TEXT][" + parser.getText().trim() + "]");
                            break;
                    }
                    tag = parser.next();
                }
                parser.require(KXmlParser.END_DOCUMENT, null, null);
            }
        } catch (XmlPullParserException parser_ex) {
            System.out.println(parser_ex.getMessage());
            parser_ex.printStackTrace();
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean parseDescribeCoverage(File f) {
        int tag;
        EncodingXMLParser parser = null;
        parser = new EncodingXMLParser();
        try {
            parser.setInput(f);
            parser.nextTag();
            if (parser.getEventType() != KXmlParser.END_DOCUMENT) {
                parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.COVERAGE_DESCRIPTION);
                tag = parser.nextTag();
                while (tag != KXmlParser.END_DOCUMENT) {
                    switch(tag) {
                        case KXmlParser.START_TAG:
                            if (parser.getName().compareTo(DescribeCoverageTags.COVERAGE_OFFERING) == 0) {
                                WCSCoverage1_0_0 lyr = new WCSCoverage1_0_0();
                                lyr.parse(parser);
                                if (lyr != null) {
                                    layerPool.put(lyr.getName(), lyr);
                                }
                            }
                            break;
                    }
                    tag = parser.next();
                }
                parser.require(KXmlParser.END_DOCUMENT, null, null);
            }
        } catch (XmlPullParserException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * <p>Parses the Capability Tag </p>
     */
    private void parseCapabilityTag(KXmlParser parser) throws IOException, XmlPullParserException {
        int currentTag;
        boolean end = false;
        parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.CAPABILITY);
        currentTag = parser.next();
        while (!end) {
            switch(currentTag) {
                case KXmlParser.START_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.REQUEST) == 0) {
                        parseRequestTag(parser);
                    } else if (parser.getName().compareTo(CapabilitiesTags.EXCEPTION) == 0) {
                        parser.skipSubTree();
                    } else if ((parser.getName().compareTo(CapabilitiesTags.VENDORSPECIFICCAPABILITIES) == 0) || (parser.getName().compareTo(CapabilitiesTags.USERDEFINEDSYMBOLIZATION) == 0)) {
                        parser.skipSubTree();
                    }
                    break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.CAPABILITY) == 0) end = true;
                    break;
                case KXmlParser.TEXT:
                    break;
            }
            currentTag = parser.next();
        }
    }

    /**
     * <p>Parses the Service Information </p>
     */
    private void parseServiceTag(KXmlParser parser) throws IOException, XmlPullParserException {
        int currentTag;
        boolean end = false;
        parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.SERVICE);
        currentTag = parser.next();
        while (!end) {
            switch(currentTag) {
                case KXmlParser.START_TAG:
                    if (parser.getName().compareToIgnoreCase(CapabilitiesTags.NAME) == 0) {
                        serviceInfo.name = parser.nextText();
                    } else if (parser.getName().compareTo(CapabilitiesTags.WCS_LABEL) == 0) {
                        serviceInfo.title = parser.nextText();
                    } else if (parser.getName().compareTo(CapabilitiesTags.WCS_DESCRIPTION) == 0) {
                        serviceInfo.abstr = parser.nextText();
                    } else if (parser.getName().compareTo(CapabilitiesTags.WCS_KEYWORDS) == 0) {
                        parser.skipSubTree();
                    } else if (parser.getName().compareTo(CapabilitiesTags.ACCESSCONSTRAINTS) == 0) {
                        parser.skipSubTree();
                    } else if (parser.getName().compareTo(CapabilitiesTags.FEES) == 0) {
                        parser.skipSubTree();
                    }
                    break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.SERVICE) == 0) end = true;
                    break;
                case KXmlParser.TEXT:
                    break;
            }
            currentTag = parser.next();
        }
    }

    /**
     * <p>Parses the Request tag </p>
     */
    private void parseRequestTag(KXmlParser parser) throws IOException, XmlPullParserException {
        int currentTag;
        boolean end = false;
        parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.REQUEST);
        currentTag = parser.next();
        while (!end) {
            switch(currentTag) {
                case KXmlParser.START_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.GETCAPABILITIES) == 0) {
                        currentTag = parser.nextTag();
                        parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.DCPTYPE);
                        {
                            currentTag = parser.nextTag();
                            if (parser.getName().compareTo(CapabilitiesTags.HTTP) == 0) {
                                currentTag = parser.nextTag();
                                if (parser.getName().compareTo(CapabilitiesTags.GET) == 0) {
                                    currentTag = parser.nextTag();
                                    if (parser.getName().compareTo(CapabilitiesTags.ONLINERESOURCE) == 0) {
                                        String value = new String();
                                        value = parser.getAttributeValue("", CapabilitiesTags.XLINK_HREF);
                                        if (value != null) {
                                            serviceInfo.operations.put(value, null);
                                        }
                                    }
                                }
                            }
                        }
                    } else if (parser.getName().compareTo(CapabilitiesTags.DESCRIBECOVERAGE) == 0) {
                        currentTag = parser.nextTag();
                        parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.DCPTYPE);
                        {
                            currentTag = parser.nextTag();
                            if (parser.getName().compareTo(CapabilitiesTags.HTTP) == 0) {
                                currentTag = parser.nextTag();
                                if (parser.getName().compareTo(CapabilitiesTags.GET) == 0) {
                                    currentTag = parser.nextTag();
                                    if (parser.getName().compareTo(CapabilitiesTags.ONLINERESOURCE) == 0) {
                                        String value = new String();
                                        value = parser.getAttributeValue("", CapabilitiesTags.XLINK_HREF);
                                        if (value != null) {
                                            serviceInfo.operations.put(value, null);
                                        }
                                    }
                                }
                            }
                        }
                    } else if (parser.getName().compareTo(CapabilitiesTags.GETCOVERAGE) == 0) {
                        currentTag = parser.nextTag();
                        parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.DCPTYPE);
                        {
                            currentTag = parser.nextTag();
                            if (parser.getName().compareTo(CapabilitiesTags.HTTP) == 0) {
                                currentTag = parser.nextTag();
                                if (parser.getName().compareTo(CapabilitiesTags.GET) == 0) {
                                    currentTag = parser.nextTag();
                                    if (parser.getName().compareTo(CapabilitiesTags.ONLINERESOURCE) == 0) {
                                        String value = new String();
                                        value = parser.getAttributeValue("", CapabilitiesTags.XLINK_HREF);
                                        if (value != null) {
                                            serviceInfo.operations.put(value, null);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.REQUEST) == 0) end = true;
                    break;
                case KXmlParser.TEXT:
                    break;
            }
            currentTag = parser.next();
        }
    }

    private void parseContentMetadataTag(KXmlParser parser) throws XmlPullParserException, IOException {
        int currentTag;
        boolean end = false;
        parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.WCS_CONTENTMETADATA);
        currentTag = parser.next();
        while (!end) {
            switch(currentTag) {
                case KXmlParser.START_TAG:
                    if (parser.getName().compareTo("wcs:" + CapabilitiesTags.WCS_COVERAGEOFFERINGBRIEF) == 0) {
                        CoverageOfferingBrief cob = new CoverageOfferingBrief();
                        cob.parse(parser);
                        layerPool.put(cob.getName(), cob);
                    }
                    break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.WCS_CONTENTMETADATA) == 0) end = true;
                    break;
                case KXmlParser.TEXT:
                    break;
            }
            currentTag = parser.next();
        }
    }
}
