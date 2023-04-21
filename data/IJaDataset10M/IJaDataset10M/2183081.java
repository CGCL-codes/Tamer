package net.sf.vgap4.projecteditor.figures;

import java.util.logging.Logger;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class MineralsFigure extends Figure {

    private static Logger logger = Logger.getLogger("net.sf.vgap4.projecteditor.figures");

    public static Color brown = new Color(Display.getCurrent(), 204, 102, 0);

    private MineralBarFigure neutronium = new MineralBarFigure(ColorConstants.red, ColorConstants.orange);

    private MineralBarFigure deuterium = new MineralBarFigure(ColorConstants.darkBlue, ColorConstants.blue);

    private MineralBarFigure tritanium = new MineralBarFigure(ColorConstants.darkGreen, ColorConstants.green);

    private MineralBarFigure molybdenum = new MineralBarFigure(brown, ColorConstants.orange);

    public MineralsFigure() {
        GridLayout grid = new GridLayout();
        setLayoutManager(grid);
        grid.horizontalSpacing = 0;
        grid.marginHeight = 0;
        grid.marginWidth = 0;
        grid.numColumns = 1;
        grid.verticalSpacing = 0;
        this.add(neutronium);
        this.add(deuterium);
        this.add(tritanium);
        this.add(molybdenum);
        this.setSize(getPreferredSize());
    }

    public void setNeutronium(int[] mineralData) {
        this.neutronium.setLevels(mineralData[0], mineralData[1], mineralData[2]);
    }

    public void setDuranium(int[] mineralData) {
        this.deuterium.setLevels(mineralData[0], mineralData[1], mineralData[2]);
    }

    public void setTritanium(int[] mineralData) {
        this.tritanium.setLevels(mineralData[0], mineralData[1], mineralData[2]);
    }

    public void setMolybdenum(int[] mineralData) {
        this.molybdenum.setLevels(mineralData[0], mineralData[1], mineralData[2]);
    }

    @Override
    public Dimension getPreferredSize(int hint, int hint2) {
        Dimension barSize = neutronium.getSize();
        barSize.height = (barSize.height + 1) * 4;
        return barSize;
    }
}

class MineralBarFigure extends Figure {

    private static final int BOX_WIDTH = 4;

    private static final int BOX_HEIGHT = 4;

    private static final int MAX_BOX_COUNT = 15;

    private final Color colorOre;

    private Color colorMineral;

    private int maximun = MAX_BOX_COUNT;

    private int ore = 0;

    private int mineral = 0;

    public MineralBarFigure(Color colorOre, Color colorMin) {
        this.colorOre = colorOre;
        this.colorMineral = colorMin;
        this.setSize(getPreferredSize());
    }

    public void setLevels(int maximun, int ore, int mineral) {
        this.maximun = Math.max(MAX_BOX_COUNT, maximun);
        this.ore = ore * MAX_BOX_COUNT / this.maximun;
        this.mineral = mineral * MAX_BOX_COUNT / this.maximun;
        this.repaint();
    }

    @Override
    protected void paintFigure(Graphics graphics) {
        Point location = getLocation();
        Rectangle box = new Rectangle();
        box.width = 3;
        box.height = 3;
        box.y = location.y;
        for (int i = 0; i < MAX_BOX_COUNT; i++) {
            graphics.setBackgroundColor(ColorConstants.lightGray);
            if (i <= (this.mineral + this.ore)) {
                graphics.setBackgroundColor(colorMineral);
            } else {
                if (i <= this.ore) {
                    graphics.setBackgroundColor(colorOre);
                }
            }
            box.x = location.x + i * BOX_WIDTH;
            graphics.fillRectangle(box);
        }
    }

    @Override
    public Dimension getPreferredSize(int hint, int hint2) {
        return new Dimension(BOX_WIDTH * MAX_BOX_COUNT + 2, BOX_HEIGHT);
    }
}
