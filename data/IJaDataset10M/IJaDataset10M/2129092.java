package archstorage.client;

import java.util.LinkedList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 */
public class SaxListParser extends DefaultHandler {

    private List<String> xuids;

    public SaxListParser() {
        xuids = new LinkedList<String>();
    }

    ;

    public List<String> getXUIDs() {
        return xuids;
    }

    @Override
    public void startElement(String string, String string1, String string2, Attributes atrbts) throws SAXException {
        super.startElement(string, string1, string2, atrbts);
        if (atrbts != null) {
            int len = atrbts.getLength();
            for (int i = 0; i < len; i++) {
                if ("id".equals(atrbts.getQName(i))) {
                    xuids.add(atrbts.getValue(i));
                }
            }
        }
    }
}
