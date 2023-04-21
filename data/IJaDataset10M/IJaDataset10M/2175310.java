package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.plugin.IWPObjectXmlCreator;
import edu.ncssm.iwp.toolkit.xml.*;

public class DObject_TimeXMLCreator implements IWPObjectXmlCreator {

    DObject_Time object;

    public DObject_TimeXMLCreator(DObject_Time object) {
        this.object = object;
    }

    public XMLElement getElement() {
        XMLElement elem = new XMLElement("time");
        elem.addElement(new XMLElement("start", "" + object.getStartTime()));
        elem.addElement(new XMLElement("stop", "" + object.getStopTime()));
        elem.addElement(new XMLElement("change", "" + object.getChange()));
        elem.addElement(new XMLElement("fps", "" + object.getFps()));
        if (object.getUsePreciseCalculations()) {
            elem.addElement(new XMLElement("usePreciseCalculations", "true"));
        }
        return elem;
    }
}
