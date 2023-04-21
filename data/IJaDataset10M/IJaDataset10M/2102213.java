package foa.elements.fo;

import foa.elements.Element;
import java.util.Hashtable;

public class RegionStart extends Region {

    public RegionStart(Hashtable attributes) {
        super("fo:region-start", attributes, "Left");
    }
}
