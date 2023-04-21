package ontool.app.designer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ontool.app.modelview.ModelView;
import ontool.app.modelview.PortHolderView;
import ontool.model.PortHolderModel;
import shapetool.DrawArea;
import shapetool.Entity;

/**
 * Abstract link creator.
 * 
 * @author <a href="mailto:asrgomes@dca.fee.unicamp.br">Antonio S. R. Gomes</a> 
 * @version $Revision: 1.1 $
 */
abstract class LinkCreator extends Creator {

    protected PortHolderModel sourcePlace;

    protected PortHolderModel destinationPlace;

    private int x, y;

    protected List nodes = new ArrayList();

    private Entity sourceEntity;

    private void preCreate(MouseEvent e, Entity en) {
        if (nodes.isEmpty() && sourceEntity == en) {
            return;
        }
        destinationPlace = (PortHolderModel) ((ModelView) en).getModel();
        create(e);
        app.setSelectedModel(model);
    }

    /**
	 * @see shapetool.DrawAreaObserver#mousePressed(shapetool.DrawArea, java.awt.event.MouseEvent, shapetool.Entity)
	 */
    public boolean mousePressed(DrawArea src, MouseEvent e, Entity en) {
        if (!nodes.isEmpty()) {
            if (en instanceof PortHolderView) {
                preCreate(e, en);
            }
            return true;
        }
        if (en != null) {
            if (en instanceof PortHolderView) {
                sourceEntity = en;
                sourcePlace = (PortHolderModel) ((ModelView) en).getModel();
            }
        } else {
            deactivate();
        }
        return true;
    }

    /**
	 * @see shapetool.DrawAreaObserver#mouseDragged(shapetool.DrawArea, java.awt.event.MouseEvent, java.util.List)
	 */
    public boolean mouseDragged(DrawArea src, MouseEvent e, java.util.List ens) {
        x = e.getX();
        y = e.getY();
        src.repaint();
        return true;
    }

    /**
	 * @see shapetool.DrawAreaObserverAdapter#mouseMoved(shapetool.DrawArea, java.awt.event.MouseEvent, shapetool.Entity)
	 */
    public boolean mouseMoved(DrawArea src, MouseEvent e, Entity en) {
        if (sourcePlace != null) {
            x = e.getX();
            y = e.getY();
            src.repaint();
        }
        return true;
    }

    /**
	 * @see shapetool.DrawAreaObserver#mouseReleased(shapetool.DrawArea, java.awt.event.MouseEvent, shapetool.Entity)
	 */
    public boolean mouseReleased(DrawArea src, MouseEvent e, Entity en) {
        if (en != null) {
            if (destinationPlace == null && en instanceof PortHolderView) {
                preCreate(e, en);
            }
        } else {
            if (e.isControlDown()) {
                nodes.add(new Point(drawArea.adjustGrid(e.getX()), drawArea.adjustGrid(e.getY())));
                return false;
            }
        }
        deactivate();
        return true;
    }

    /**
	 * @see ontool.app.designer.Creator#deactivate()
	 */
    public void deactivate() {
        super.deactivate();
        sourcePlace = null;
        destinationPlace = null;
        nodes.clear();
    }

    /**
	 * @see ontool.app.designer.Creator#activate()
	 */
    public void activate() {
        super.activate();
        nodes.clear();
    }

    /**
	 * @see shapetool.DrawAreaObserver#paintLast(shapetool.DrawArea, java.awt.Graphics2D)
	 */
    public boolean paintLast(DrawArea src, Graphics2D g) {
        if (sourcePlace != null && destinationPlace == null) {
            g.setColor(Color.red);
            int _x = sourcePlace.getX();
            int _y = sourcePlace.getY();
            for (Iterator i = nodes.iterator(); i.hasNext(); ) {
                Point p = (Point) i.next();
                g.drawLine(_x, _y, p.x, p.y);
                _x = p.x;
                _y = p.y;
            }
            g.drawLine(_x, _y, x, y);
        }
        return true;
    }
}
