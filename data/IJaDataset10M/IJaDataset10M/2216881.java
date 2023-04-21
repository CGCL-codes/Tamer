package edu.gsbme.wasabi.UI.Algorithm.Imports;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import edu.gsbme.MMLParser2.CommonParserLib.MMLDeclaration;
import edu.gsbme.MMLParser2.Vocabulary.Attributes;
import edu.gsbme.wasabi.Algorithm.UIAlgorithm;
import edu.gsbme.wasabi.UI.Algorithm.data.GenericSearchDataStructure;
import edu.gsbme.wasabi.UI.Algorithm.data.GenericSearchResult;

/**
 * Generate a import equation list search result stored in GenericSearchResult data structure
 * @author David
 *
 */
public class searchImportEquationList extends UIAlgorithm {

    Element parent;

    public searchImportEquationList(Element parent) {
        this.parent = parent;
    }

    @Override
    public Object run(Composite parent, Element viewer) {
        return null;
    }

    @Override
    public Object run() {
        return generateImportList();
    }

    private GenericSearchResult generateImportList() {
        GenericSearchResult search = new GenericSearchResult("", 2);
        search.setColumn(0, "ref");
        search.setColumn(1, "Local Mapping");
        NodeList list = MMLDeclaration.returnEquationList(parent);
        for (int i = 0; i < list.getLength(); i++) {
            Element temp = (Element) list.item(i);
            GenericSearchDataStructure s = search.createResultEntry();
            s.data[0] = temp.getAttribute(Attributes.ref.toString());
            s.data[1] = temp.getAttribute(Attributes.local_mapping.toString());
            s.reference = temp;
        }
        return search;
    }
}
