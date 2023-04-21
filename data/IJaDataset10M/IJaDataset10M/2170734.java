package repast.simphony.agents.designer.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import repast.simphony.agents.AgentBuilderConsts;
import repast.simphony.agents.base.FigureConsts;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif�s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * TODO
 * 
 * 
 * 
 */
public class BehaviorStepFigure extends PropertyOrStepFigure {

    private Rectangle BOUNDS = new Rectangle(0, 0, FigureConsts.DIMENSION_FIGURE_START.width, FigureConsts.DIMENSION_FIGURE_START.height);

    /**
	 * TODO
	 * 
	 */
    public BehaviorStepFigure() {
        super();
        getBounds().width = BOUNDS.width;
        getBounds().height = BOUNDS.height;
    }

    /**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
    @Override
    protected void paintFigure(Graphics g) {
        Rectangle drawRect = getBounds().getCropped(new Insets(1, 1, 0, 2));
        PointList pointList = new PointList();
        pointList.addPoint(drawRect.getTopLeft());
        pointList.addPoint(drawRect.getBottom());
        pointList.addPoint(drawRect.getTopRight());
        paintFlowletFigure(g, pointList);
    }

    /**
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        return new Dimension(FigureConsts.DIMENSION_FIGURE_START.width, FigureConsts.DIMENSION_FIGURE_START.height);
    }

    /**
	 * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
	 */
    public Rectangle getHandleBounds() {
        return getBounds();
    }

    /**
	 * @see repast.simphony.agents.designer.model.AgentPropertyorStepModelPart#getType()
	 */
    @Override
    public String getType() {
        return AgentBuilderConsts.BEHAVIOR;
    }
}
