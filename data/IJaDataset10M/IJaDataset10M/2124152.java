package org.opennms.netmgt.eventd.datablock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import org.apache.log4j.Category;
import org.opennms.core.utils.ThreadCategory;

/**
 * <pre>
 * The information read from the eventconf.xml is stored here. It maintains
 *  a map,  keyed with 'EventKey's. 
 * 
 *  It also has an UEI to 'EventKey'list map - this mapping fastens the lookup 
 *  for OpenNMS internal events when different masks are configured for the
 *  same UEI.
 * 
 *  When a lookup is to be done for an 'Event',  
 *  - its 'key' is used to get a lookup,
 *  - if no match is found for the key, UEI is used to lookup the keys that got added for that UEI
 *    and the first best fit in the event map for any of the UEI keys are used
 *  - if there is still no match at this point, all keys in the eventconf are iterated through to
 *    find a match
 *  
 * </pre>
 * 
 * @author <A HREF="mailto:sowmya@opennms.org">Sowmya Nataraj </A>
 * @author <A HREF="http://www.opennms.org">OpenNMS.org </A>
 */
public class EventConfData extends Object {

    /**
     * The map keyed with 'EventKey's
     */
    private LinkedHashMap<EventKey, org.opennms.netmgt.xml.eventconf.Event> m_eventMap;

    private LinkedHashMap<String, List<EventKey>> m_ueiToKeyListMap;

    /**
     * Check whether the event matches the passed key
     * 
     * @return true if the event matches the passed key
     */
    @SuppressWarnings("unchecked")
    private boolean eventMatchesKey(EventKey eventKey, org.opennms.netmgt.xml.event.Event event) {
        boolean maskMatch = true;
        Iterator<String> keysetIter = eventKey.keySet().iterator();
        while (keysetIter.hasNext() && maskMatch) {
            String key = keysetIter.next();
            List maskValues = (List) eventKey.get(key);
            String eventvalue = EventKey.getMaskElementValue(event, key);
            maskMatch = eventValuePassesMaskValue(eventvalue, maskValues);
            if (!maskMatch) {
                return maskMatch;
            }
        }
        return maskMatch;
    }

    /**
     * Check whether the eventvalue passes any of the mask values Mask values
     * ending with a '%' only need to be a substring of the eventvalue for the
     * eventvalue to pass the mask
     * 
     * Enhanced 2005/08/31 to allow regular expression in eventconf.
     * 
     * @return true if the values passes the mask
     */
    protected static boolean eventValuePassesMaskValue(String eventvalue, List<String> maskValues) {
        boolean maskMatch = false;
        Iterator<String> valiter = maskValues.iterator();
        while (valiter.hasNext() && !maskMatch) {
            String keyvalue = valiter.next();
            if (keyvalue != null && eventvalue != null) {
                int len = keyvalue.length();
                if (keyvalue.equals(eventvalue)) {
                    maskMatch = true;
                } else if (keyvalue.charAt(0) == '~') {
                    if (eventvalue.matches(keyvalue.substring(1))) {
                        maskMatch = true;
                    }
                } else if (keyvalue.charAt(len - 1) == '%') {
                    if (eventvalue.startsWith(keyvalue.substring(0, len - 1))) {
                        maskMatch = true;
                    }
                }
            }
        }
        return maskMatch;
    }

    /**
     * Update the uei to keylist map
     */
    private void updateUeiToKeyListMap(EventKey eventKey, org.opennms.netmgt.xml.eventconf.Event event) {
        String eventUei = event.getUei();
        List<EventKey> keylist = m_ueiToKeyListMap.get(eventUei);
        if (keylist == null) {
            keylist = new ArrayList<EventKey>();
            keylist.add(eventKey);
            m_ueiToKeyListMap.put(eventUei, keylist);
        } else {
            if (!keylist.contains(eventKey)) {
                keylist.add(eventKey);
            }
        }
    }

    /**
     * Default constructor - allocate the maps
     */
    public EventConfData() {
        m_eventMap = new LinkedHashMap<EventKey, org.opennms.netmgt.xml.eventconf.Event>();
        m_ueiToKeyListMap = new LinkedHashMap<String, List<EventKey>>();
    }

    /**
     * Add an event - add to the 'EventKey' map using the event mask by default.
     * If the event has snmp information, add using the snmp EID
     * 
     * @param event
     *            the org.opennms.netmgt.xml.eventconf.Event
     */
    public synchronized void put(org.opennms.netmgt.xml.eventconf.Event event) {
        EventKey eventKey = new EventKey(event);
        m_eventMap.put(eventKey, event);
        updateUeiToKeyListMap(eventKey, event);
        org.opennms.netmgt.xml.eventconf.Snmp eventSnmp = event.getSnmp();
        if (eventSnmp != null) {
            String eventEID = eventSnmp.getId();
            if (eventEID != null) {
                EventKey snmpKey = new EventKey();
                snmpKey.put(EventKey.TAG_SNMP_EID, new EventMaskValueList(eventEID));
                m_eventMap.put(snmpKey, event);
                updateUeiToKeyListMap(snmpKey, event);
            }
        }
    }

    /**
     * Add an event with the specified key
     * 
     * @param key
     *            the EventKey for this event
     * @param event
     *            the org.opennms.netmgt.xml.eventconf.Event
     */
    public synchronized void put(EventKey key, org.opennms.netmgt.xml.eventconf.Event event) {
        m_eventMap.put(key, event);
        updateUeiToKeyListMap(key, event);
    }

    /**
     * <pre>
     * Get the right configuration for the event - the eventkey is used first.
     *  If no match is found, the event's uei to keylist is iterated through, and these keys
     *  used to lookup the event map. if still no match is found, all eventconf
     *  keys are iterated through to find a match. The first successful match is returned.
     * 
     *  
     * <EM>
     * NOTE:
     * </EM>
     * The first right config event that the event matches is returned.
     *  The ordering of the configurations is the responsibility of the user
     * </pre>
     * 
     * @param event
     *            the event which is to be looked up
     */
    public synchronized org.opennms.netmgt.xml.eventconf.Event getEvent(org.opennms.netmgt.xml.event.Event event) {
        org.opennms.netmgt.xml.eventconf.Event matchedEvent = null;
        if (matchedEvent == null) {
            Iterator<Entry<EventKey, org.opennms.netmgt.xml.eventconf.Event>> entryIterator = m_eventMap.entrySet().iterator();
            while (entryIterator.hasNext() && (matchedEvent == null)) {
                Entry<EventKey, org.opennms.netmgt.xml.eventconf.Event> entry = entryIterator.next();
                EventKey iterKey = entry.getKey();
                boolean keyMatchFound = eventMatchesKey(iterKey, event);
                if (keyMatchFound) {
                    if (log().isDebugEnabled()) {
                        log().debug("Match found using key: " + iterKey.toString());
                    }
                    matchedEvent = entry.getValue();
                }
            }
        }
        return matchedEvent;
    }

    /**
     * Get the event with the specified uei
     * 
     * @param uei
     *            the uei
     */
    public synchronized org.opennms.netmgt.xml.eventconf.Event getEventByUEI(String uei) {
        EventKey key = new EventKey();
        key.put(EventKey.TAG_UEI, new EventMaskValueList(uei));
        return m_eventMap.get(key);
    }

    /**
     * Clear out the data
     */
    public synchronized void clear() {
        m_eventMap.clear();
        m_ueiToKeyListMap.clear();
    }

    private Category log() {
        return ThreadCategory.getInstance(getClass());
    }
}
