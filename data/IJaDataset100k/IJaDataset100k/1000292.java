package uk.ac.lkl.expresser.client;

import java.util.HashMap;
import com.google.gwt.xml.client.Element;

/**
 * Supports the use of id and idRef attributes in XML
 * 
 * @author Ken Kahn
 *
 */
public class IdsToObjectsMapping extends HashMap<String, Object> {

    public IdsToObjectsMapping() {
        super();
    }

    public Object getPreviouslyEncounteredObject(Element element) {
        String idRef = element.getAttribute("idRef");
        if (idRef == null) {
            String id = element.getAttribute("id");
            if (id == null) {
                throw new RuntimeException("Element expected to have either id or idRef " + XMLUtilities.nodeToString(element));
            }
            return null;
        } else {
            Object object = get(idRef);
            if (object == null) {
                throw new RuntimeException("Element expected to have an idRef that refered to an earlier id " + XMLUtilities.nodeToString(element));
            } else {
                return object;
            }
        }
    }

    public void encountered(Element element, Object newObject) {
        String id = element.getAttribute("id");
        if (id == null) {
            throw new RuntimeException("Element expected to have either id or idRef " + XMLUtilities.nodeToString(element));
        }
        put(id, newObject);
    }
}
