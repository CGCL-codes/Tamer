package blue.components.lines;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import electric.xml.Element;
import electric.xml.Elements;

public class LineList extends ArrayList implements Serializable {

    public static LineList loadFromXML(Element data) {
        LineList retVal = new LineList();
        Elements nodes = data.getElements();
        while (nodes.hasMoreElements()) {
            Element node = nodes.next();
            retVal.addLine(Line.loadFromXML(node));
        }
        return retVal;
    }

    public Element saveAsXML() {
        Element retVal = new Element("lines");
        for (Iterator iter = this.iterator(); iter.hasNext(); ) {
            Line line = (Line) iter.next();
            retVal.addElement(line.saveAsXML());
        }
        return retVal;
    }

    public Line getLine(int index) {
        return (Line) this.get(index);
    }

    public boolean addLine(Line line) {
        return this.add(line);
    }

    public String toString() {
        return "";
    }
}
