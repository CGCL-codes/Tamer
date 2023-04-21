package org.opennms.netmgt.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.Collection;
import java.util.List;

@XmlRootElement(name = "mapElements")
public class OnmsMapElementList extends LinkedList<OnmsMapElement> {

    private static final long serialVersionUID = 474241792322520294L;

    public OnmsMapElementList() {
        super();
    }

    public OnmsMapElementList(Collection<? extends OnmsMapElement> c) {
        super(c);
    }

    @XmlElement(name = "mapElement")
    public List<OnmsMapElement> getMapElements() {
        return this;
    }

    public void setMapElements(List<OnmsMapElement> mapElements) {
        clear();
        addAll(mapElements);
    }
}
