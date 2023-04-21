package edu.gsbme.wasabi.UI.Algorithm.FML;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import edu.gsbme.MMLParser2.FML.FMLFrame;
import edu.gsbme.MMLParser2.Vocabulary.Attributes;
import edu.gsbme.wasabi.Algorithm.UIAlgorithm;
import edu.gsbme.wasabi.UI.Algorithm.data.GenericSearchDataStructure;
import edu.gsbme.wasabi.UI.Algorithm.data.GenericSearchResult;

/**
 * Generate a control point list search result stored in GenericSearchResult data structure
 * @author David
 *
 */
public class searchControlPoints extends UIAlgorithm {

    Element cell;

    public searchControlPoints(Element cell) {
        this.cell = cell;
    }

    @Override
    public Object run(Composite parent, Element viewer) {
        return null;
    }

    @Override
    public Object run() {
        return generateTable();
    }

    private GenericSearchResult generateTable() {
        GenericSearchResult search = new GenericSearchResult("", 4);
        search.setColumn(0, "x");
        search.setColumn(1, "y");
        search.setColumn(2, "z");
        search.setColumn(3, "weight");
        NodeList list = FMLFrame.returnControlPointList(cell);
        for (int i = 0; i < list.getLength(); i++) {
            Element temp = (Element) list.item(i);
            GenericSearchDataStructure ds = search.createResultEntry();
            ds.data[0] = temp.getAttribute(Attributes.x.toString());
            ds.data[1] = temp.getAttribute(Attributes.y.toString());
            ds.data[2] = temp.getAttribute(Attributes.z.toString());
            ds.data[3] = temp.getAttribute(Attributes.weight.toString());
            ds.reference = temp;
        }
        return search;
    }
}
