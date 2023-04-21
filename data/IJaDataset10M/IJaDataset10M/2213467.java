package edu.asu.quadriga.impl.elements;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import edu.asu.quadriga.interfaces.elements.IPlace;

public class Place extends Concept implements IPlace {

    public static class Adapter extends XmlAdapter<Place, IPlace> {

        public IPlace unmarshal(Place v) {
            return v;
        }

        public Place marshal(IPlace v) {
            return (Place) v;
        }
    }
}
