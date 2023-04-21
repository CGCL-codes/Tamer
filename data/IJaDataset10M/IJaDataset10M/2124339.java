package com.tensegrity.palowebviewer.modules.paloclient.client;

/**
 * This class represents default cube view. Now, it has filled only selected elements on server side. 
 * It constructs right axises on client side. 
 * There is no sense of many operations, such as getPath(), so, UnsupportedOperationException will be thrown for them.   
 * @author dmol
 *
 */
public class XDefaultView extends XView {

    private XElement[] selectedElements;

    public XDefaultView() {
    }

    public XDefaultView(XElement[] selectedElements) {
        super("default-view", "default-view", null);
        this.selectedElements = selectedElements;
    }

    public XAxis[] getAxises() {
        if (super.getAxises() == null) {
            XCube cube = (XCube) getParent();
            if (cube == null) {
                throw new IllegalStateException("parent cube should be set before accessing axes");
            }
            XAxis[] axises = new XAxis[3];
            XDimension[] dimensions = cube.getDimensions();
            axises[0] = createRowAxis(dimensions);
            axises[1] = createColAxis(dimensions);
            axises[2] = createSelAxis(dimensions);
            super.setAxises(axises);
        }
        return super.getAxises();
    }

    private XAxis createSelAxis(XDimension[] dimensions) {
        int length = dimensions.length - 2;
        XDimension[] dims = new XDimension[length];
        for (int i = 2; i < dimensions.length; i++) {
            dims[i - 2] = dimensions[i];
        }
        XAxis r = new XAxis(XAxis.SELECTED, dims, new XSubset[length], new XElementPath[0]);
        r.setSelectedElements(selectedElements);
        return r;
    }

    private XAxis createColAxis(XDimension[] dimensions) {
        XAxis r = new XAxis(XAxis.COLUMNS, new XDimension[] { dimensions[1] }, new XSubset[1], new XElementPath[0]);
        return r;
    }

    private XAxis createRowAxis(XDimension[] dimensions) {
        XAxis r = new XAxis(XAxis.ROWS, new XDimension[] { dimensions[0] }, new XSubset[1], new XElementPath[0]);
        return r;
    }

    public void setAxises(XAxis[] axises) {
        throw new UnsupportedOperationException();
    }
}
