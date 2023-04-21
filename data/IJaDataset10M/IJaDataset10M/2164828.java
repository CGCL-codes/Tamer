package org.gvsig.remoteClient.wms;

import java.io.IOException;
import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * <p>Defines a OGC style. Theme that describes the appeareance of certain layer.</p>
 *
 */
public abstract class WMSStyle {

    private String name;

    private String title;

    private String styleAbstract;

    private org.gvsig.remoteClient.wms.WMSStyle.LegendURL legendURL;

    /**
     * <p>Parses the STYLE tag in the WMS capabilities, filling the WMSStyle object
     * loading the data in memory to be easily accesed</p>
     *
     */
    public abstract void parse(KXmlParser parser) throws IOException, XmlPullParserException;

    /**
     * Parses the legendURL tag.
     * @param parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    protected void parseLegendURL(KXmlParser parser) throws IOException, XmlPullParserException {
        int currentTag;
        boolean end = false;
        parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.LEGENDURL);
        String value = new String();
        legendURL = new LegendURL();
        value = parser.getAttributeValue("", CapabilitiesTags.WIDTH);
        if (value != null) {
            legendURL.width = Integer.parseInt(value);
        }
        value = parser.getAttributeValue("", CapabilitiesTags.HEIGHT);
        if (value != null) {
            legendURL.height = Integer.parseInt(value);
        }
        currentTag = parser.nextTag();
        while (!end) {
            switch(currentTag) {
                case KXmlParser.START_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.FORMAT) == 0) {
                        legendURL.format = parser.nextText();
                    } else if (parser.getName().compareTo(CapabilitiesTags.ONLINERESOURCE) == 0) {
                        value = parser.getAttributeValue("", CapabilitiesTags.XLINK_TYPE);
                        if (value != null) legendURL.onlineResource_type = value;
                        value = parser.getAttributeValue("", CapabilitiesTags.XLINK_HREF);
                        if (value != null) legendURL.onlineResource_href = value;
                    }
                    break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.LEGENDURL) == 0) end = true;
                    break;
                case KXmlParser.TEXT:
                    break;
            }
            if (!end) {
                currentTag = parser.next();
            }
        }
        parser.require(KXmlParser.END_TAG, null, CapabilitiesTags.LEGENDURL);
    }

    /**
     * gets the LegendURL OnlineResource type
     */
    public String getLegendURLOnlineResourceType() {
        if (legendURL != null) {
            return legendURL.onlineResource_type;
        } else {
            return null;
        }
    }

    /**
     * gets the LegendURL OnlineResource href
     */
    public String getLegendURLOnlineResourceHRef() {
        if (legendURL != null) {
            return legendURL.onlineResource_href;
        } else {
            return null;
        }
    }

    public String getLegendURLFormat() {
        if (legendURL != null) {
            return legendURL.format;
        } else {
            return null;
        }
    }

    public int getLegendURLWidth() {
        if (legendURL != null) {
            return legendURL.width;
        }
        return 0;
    }

    public int getLegendURLHeight() {
        if (legendURL != null) {
            return legendURL.height;
        }
        return 0;
    }

    /**
     * sets LegendURL
     */
    protected void setLegendURL(LegendURL legendURL) {
        this.legendURL = legendURL;
    }

    /**
     * <p>gets the style name</p>
     *
     * @return style name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>sets the style name.</p>
     *
     * @param _name
     */
    public void setName(String _name) {
        name = _name;
    }

    /**
     * <p>gets the style title</p>
     *
     *
     * @return style title
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>Sets style title</p>
     *
     *
     * @param _title
     */
    public void setTitle(String _title) {
        title = _title.trim();
    }

    /**
     * <p>gets style abstract</p>
     *
     *
     * @return style abstract
     */
    public String getAbstract() {
        return styleAbstract;
    }

    /**
     * <p>sets style abstract</p>
     *
     *
     * @param _abstract, style abstract
     */
    public void setAbstract(String _abstract) {
        styleAbstract = _abstract;
    }

    /**
     * <p>Inner class describing the Legend URL defined for styles in the OGC specifications in WMS</p>
     *
     */
    protected class LegendURL {

        public LegendURL() {
            width = 0;
            height = 0;
            format = new String();
            onlineResource_type = new String();
            onlineResource_href = new String();
        }

        public int width;

        public int height;

        public String format;

        public String onlineResource_type;

        public String onlineResource_href;
    }
}
