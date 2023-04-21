package vademecum.visualizer.scattermatrix;

import java.awt.Rectangle;
import vademecum.ui.visualizer.vgraphics.AbstractInteraction;
import vademecum.visualizer.D2.scatter.ScatterPlot2D;

public class ScatterMatrixFormater {

    int dimensions = 0;

    int numScatterPlots = 0;

    int mWidth = 0;

    int mHeight = 0;

    public ScatterMatrixFormater() {
    }

    public ScatterMatrixFormater(int dim) {
        dimensions = dim;
    }

    public void setDimensions(int dim) {
        dimensions = dim;
    }

    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public Rectangle calcBounds(int currentBlock, int dim, Rectangle space) {
        Rectangle r = new Rectangle();
        int row = currentBlock / dim;
        int col = currentBlock % dim;
        int blockwidth = space.width / dim;
        int blockheight = space.height / dim;
        int x = col * blockwidth;
        int y = row * blockheight;
        r.width = blockwidth;
        r.height = blockheight;
        r.x = x;
        r.y = y;
        return r;
    }

    public void adjustScatterPlot(ScatterPlot2D scatter2d) {
        Rectangle xpr = new Rectangle(0, 0, mWidth, mHeight);
        int no = numScatterPlots;
        scatter2d.setBounds(calcBounds(no, dimensions, xpr));
        numScatterPlots++;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }
}
