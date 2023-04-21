package rollmadness.formats;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TrackInfoSequence {

    private final ArrayList<TrackInfo> sequence;

    public TrackInfoSequence(URL xml) throws Exception {
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DocumentBuilder bui = fac.newDocumentBuilder();
        Document doc = bui.parse(xml.toString());
        NodeList list = doc.getDocumentElement().getElementsByTagName("track-info");
        LinkedList<TrackInfo> seq = new LinkedList<TrackInfo>();
        for (int i = 0; i < list.getLength(); i++) {
            seq.add(new TrackInfo((Element) list.item(i)));
        }
        sequence = new ArrayList<TrackInfo>(seq);
    }

    public TrackInfo next(TrackInfo current) {
        if (current == null) {
            return sequence.get(0);
        } else {
            int index = sequence.indexOf(current);
            index += 1;
            return index < sequence.size() ? sequence.get(index) : null;
        }
    }
}
