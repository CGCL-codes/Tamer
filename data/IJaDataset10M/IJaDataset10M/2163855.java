package md.ui.graphics;

import md.ui.MainWindow;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;

public class RootFigure extends Figure {

    private int gridSize = 40;

    public RootFigure() {
        setLayoutManager(new XYLayout());
        setFocusTraversable(true);
    }

    public int getSnapSize() {
        return gridSize / 4;
    }

    public void paint(Graphics graphics) {
        if (MainWindow.getSettingsManager().getStore().getBoolean("showGrid")) {
            graphics.pushState();
            graphics.setForegroundColor(ColorConstants.gray);
            FigureUtilities.paintGrid(graphics, this, new Point(0, 0), gridSize, gridSize);
            graphics.restoreState();
        }
        super.paint(graphics);
    }
}
