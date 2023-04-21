package br.ufmg.ubicomp.communication;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import br.ufmg.ubicomp.utils.EntityProperties;
import br.ufmg.ubicomp.utils.RequestUtil;

public class EventServiceCommunication extends CommunicationService {

    public static final String BASE_URL = ConfigurationManager.getInstance().getData("server") + "eventservice/process?operation=";

    public Collection<EntityProperties> listUserEvents(String dgKey) throws Exception {
        String url = BASE_URL + "list&droidGuideUser_key=" + dgKey;
        NodeList entities = RequestUtil.executeUrlDOM(url);
        return getEventObjects(entities);
    }

    private Collection<EntityProperties> getEventObjects(NodeList entities) throws ParseException {
        ArrayList<EntityProperties> events = new ArrayList<EntityProperties>();
        EntityProperties event = null;
        for (int i = 0; i < entities.getLength(); i++) {
            Node entityEntry = entities.item(i);
            if (entityEntry instanceof Element) {
                event = getEntityElement(entityEntry);
                if (event != null) events.add(event);
            }
        }
        return events;
    }

    public Collection<EntityProperties> save(String userKey, String type, String source, String data) throws Exception {
        String url = BASE_URL + "save&";
        url = url + "droidGuideUser_key=" + userKey;
        url = url + "&type=" + type;
        url = url + "&source=" + source;
        url = url + "&data=" + data;
        NodeList entities = RequestUtil.executeUrlDOM(url);
        Collection<EntityProperties> userEvents = getEventObjects(entities);
        return userEvents;
    }

    public Collection<EntityProperties> read(String userKey) throws Exception {
        String url = BASE_URL + "getServerEvents&";
        url = url + "droidGuideUser_key=" + userKey;
        NodeList entities = RequestUtil.executeUrlDOM(url);
        Collection<EntityProperties> userEvents = getEventObjects(entities);
        return userEvents;
    }
}
