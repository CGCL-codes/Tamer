package geovista.matrix;

import geovista.matrix.scatterplot.ScatterPlotMatrixElement;
import geovista.matrix.treemap.TreeMapMatrixElement;

public class TreemapAndScatterplotMatrix extends BiPlotMatrix {

    public TreemapAndScatterplotMatrix() {
        super();
        this.setElementClass1(ScatterPlotMatrixElement.class);
        this.setElementClass2(TreeMapMatrixElement.class);
    }
}
