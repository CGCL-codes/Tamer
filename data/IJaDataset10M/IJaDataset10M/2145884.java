package edu.gsbme.wasabi.UI.Algorithm.FML.Mesh;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;
import edu.gsbme.MMLParser2.FML.VirtualTree.cTreeNodes;
import edu.gsbme.wasabi.Algorithm.UIAlgorithm;
import edu.gsbme.wasabi.UI.Algorithm.data.GenericSearchResult;

public class searchMeshTopologyClass extends UIAlgorithm {

    cTreeNodes class_node;

    public searchMeshTopologyClass(cTreeNodes class_node) {
        this.class_node = class_node;
    }

    @Override
    public Object run(Composite parent, Element viewer) {
        return null;
    }

    @Override
    public Object run() {
        return generate();
    }

    public GenericSearchResult generate() {
        GenericSearchResult r = new GenericSearchResult("", 1);
        r.columns[0] = "data";
        return r;
    }
}
