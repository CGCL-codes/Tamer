package us.harward.commons.xml.saxbp.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.stream.events.StartElement;
import us.harward.commons.xml.saxbp.annotations.SAXBPHandler;

@SAXBPHandler
public class StAXAddressHandler {

    private final Collection<StartElement> addresses = new LinkedList<StartElement>();

    @XmlElement(namespace = "http://us.harward.xmlns/rolodex", name = "address")
    public void address(final StartElement se) {
        addresses.add(se);
    }

    public Collection<StartElement> getAddressEvents() {
        return Collections.unmodifiableCollection(addresses);
    }

    public void reset() {
        addresses.clear();
    }
}
