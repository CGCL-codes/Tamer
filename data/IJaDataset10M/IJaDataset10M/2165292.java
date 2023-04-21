package unbbayes.gui.draw;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class DrawParallelogram extends DrawElement {

    private Point2D.Double position;

    private Point2D.Double size;

    /**
	 * Constructs an DrawParallelogram that will draw the parallelogram 
	 * in the (x,y) position given. The (x,y) position represents the
	 * center of the parallelogram.
	 * @param position The (x,y) position representing the center of the parallelogram.
	 * @param size The width and height of the parallelogram.
	 */
    public DrawParallelogram(Point2D.Double position, Point2D.Double size) {
        this.position = position;
        this.size = size;
    }

    @Override
    public void paint(Graphics2D graphics) {
        GeneralPath parallelogram = new GeneralPath();
        parallelogram.moveTo((float) (position.x - size.x / 2), (float) (position.y));
        parallelogram.lineTo((float) (position.x), (float) (position.y + size.y / 2));
        parallelogram.lineTo((float) (position.x + size.x / 2), (float) (position.y));
        parallelogram.lineTo((float) (position.x), (float) (position.y - size.y / 2));
        parallelogram.lineTo((float) (position.x - size.x / 2), (float) (position.y));
        graphics.setColor(getFillColor());
        graphics.fill(parallelogram);
        if (isSelected()) {
            graphics.setColor(getSelectionColor());
            graphics.setStroke(new BasicStroke(2));
        } else {
            graphics.setColor(getOutlineColor());
        }
        graphics.draw(parallelogram);
        graphics.setStroke(new BasicStroke(1));
        super.paint(graphics);
    }
}
