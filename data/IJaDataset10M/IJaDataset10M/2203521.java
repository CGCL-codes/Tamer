package edu.gsbme.wasabi.UI.Algorithm.Declaration;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;
import edu.gsbme.wasabi.Algorithm.UIAlgorithm;

public class createXMLArray2DStructure extends UIAlgorithm {

    Element array;

    public createXMLArray2DStructure(Element array) {
        this.array = array;
    }

    @Override
    public Object run(Composite parent, Element viewer) {
        return null;
    }

    @Override
    public Object run() {
        return getArray2D();
    }

    private String[][] getArray2D() {
        String[][] result = new String[0][0];
        return result;
    }
}
