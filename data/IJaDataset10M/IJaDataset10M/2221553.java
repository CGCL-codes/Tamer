package net.sf.freenote.figure;

import net.sf.freenote.directedit.DirectEditable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;

/**
 * 从logic示例中移植的，可支持自动换行，硬换行
 * @author levin
 * @since 2008-1-20 上午10:08:33
 */
public class TextFigure extends Figure implements DirectEditable {

    protected static int DEFAULT_CORNER_SIZE = 10;

    private int cornerSize;

    /**
	 * Constructs an empty BentCornerFigure with default background color of 
	 * ColorConstants.tooltipBackground and default corner size.
	 */
    public TextFigure() {
        setBackgroundColor(ColorConstants.tooltipBackground);
        setForegroundColor(ColorConstants.tooltipForeground);
        setCornerSize(DEFAULT_CORNER_SIZE);
        setBorder(new MarginBorder(DEFAULT_CORNER_SIZE - 3));
        FlowPage flowPage = new FlowPage();
        textFlow = new TextFlow();
        textFlow.setLayoutManager(new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_SOFT));
        flowPage.add(textFlow);
        setLayoutManager(new StackLayout());
        add(flowPage);
    }

    /**
	 * Returns the size, in pixels, that the figure should use to draw its bent corner.
	 * 
	 * @return size of the corner
	 */
    public int getCornerSize() {
        return cornerSize;
    }

    /**
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
    protected void paintFigure(Graphics graphics) {
        Rectangle rect = getBounds().getCopy();
        graphics.translate(getLocation());
        PointList outline = new PointList();
        outline.addPoint(0, 0);
        outline.addPoint(rect.width - cornerSize, 0);
        outline.addPoint(rect.width - 1, cornerSize);
        outline.addPoint(rect.width - 1, rect.height - 1);
        outline.addPoint(0, rect.height - 1);
        graphics.fillPolygon(outline);
        PointList innerLine = new PointList();
        innerLine.addPoint(rect.width - cornerSize - 1, 0);
        innerLine.addPoint(rect.width - cornerSize - 1, cornerSize);
        innerLine.addPoint(rect.width - 1, cornerSize);
        innerLine.addPoint(rect.width - cornerSize - 1, 0);
        innerLine.addPoint(0, 0);
        innerLine.addPoint(0, rect.height - 1);
        innerLine.addPoint(rect.width - 1, rect.height - 1);
        innerLine.addPoint(rect.width - 1, cornerSize);
        graphics.drawPolygon(innerLine);
        graphics.translate(getLocation().getNegated());
    }

    /**
	 * Sets the size of the figure's corner to the given offset.
	 * 
	 * @param newSize the new size to use.
	 */
    public void setCornerSize(int newSize) {
        cornerSize = newSize;
    }

    /** The inner TextFlow **/
    private TextFlow textFlow;

    /**
	 * Returns the text inside the TextFlow.
	 * 
	 * @return the text flow inside the text.
	 */
    public String getDesc() {
        return textFlow.getText();
    }

    /**
	 * Sets the text of the TextFlow to the given value.
	 * 
	 * @param newText the new text value.
	 */
    public void setText(String newText) {
        textFlow.setText(newText);
    }

    public void setDesc(String newText) {
        textFlow.setText(newText);
    }
}
