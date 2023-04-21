package net.firefly.client.http.aws.filters;

import java.net.MalformedURLException;
import java.net.URL;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

public class AWSSoapResponseXmlFilter extends XMLFilterImpl {

    private static String ITEM_ELEM = "Item";

    private static String SMALL_IMAGE_ELEM = "SmallImage";

    private static String MEDIUM_IMAGE_ELEM = "MediumImage";

    private static String LARGE_IMAGE_ELEM = "LargeImage";

    private static String URL_ELEM = "URL";

    private URL coverUrl;

    private StringBuffer buffer;

    private String currentTag;

    boolean inItem;

    boolean inSmallImage;

    boolean inMediumImage;

    boolean inLargeImage;

    boolean inURL;

    boolean found;

    public AWSSoapResponseXmlFilter() {
        inItem = false;
        inSmallImage = false;
        inMediumImage = false;
        inLargeImage = false;
        inURL = false;
        found = false;
    }

    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        String tagName = qName;
        this.currentTag = tagName;
        super.startElement(uri, localName, qName, attrs);
        if (ITEM_ELEM.equals(tagName)) {
            inItem = true;
        } else if (SMALL_IMAGE_ELEM.equals(tagName)) {
            inSmallImage = true;
            inMediumImage = false;
            inLargeImage = false;
        } else if (MEDIUM_IMAGE_ELEM.equals(tagName)) {
            inSmallImage = false;
            inMediumImage = true;
            inLargeImage = false;
        } else if (LARGE_IMAGE_ELEM.equals(tagName)) {
            inSmallImage = false;
            inMediumImage = false;
            inLargeImage = true;
        } else if (URL_ELEM.equals(tagName)) {
            inURL = true;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        String tagName = qName;
        super.endElement(uri, localName, qName);
        if (!found) {
            if (URL_ELEM.equals(tagName) && (inSmallImage || inMediumImage || inLargeImage)) {
                try {
                    this.coverUrl = new URL(this.buffer.toString().trim());
                } catch (MalformedURLException e) {
                }
                this.buffer = new StringBuffer();
                if (inLargeImage) {
                    found = true;
                }
            }
        }
    }

    public void characters(char buf[], int offset, int length) throws SAXException {
        if (URL_ELEM.equals(this.currentTag)) {
            if (this.buffer == null) {
                this.buffer = new StringBuffer();
            }
            for (int i = offset; i < offset + length; i++) {
                this.buffer.append(buf[i]);
            }
        }
        super.characters(buf, offset, length);
    }

    /**
	 * @return
	 */
    public URL getCoverUrl() {
        return this.coverUrl;
    }
}
