package org.dengues.designer.ui.editors;

import java.util.List;
import org.dengues.core.process.CompElement;
import org.dengues.core.process.ICompConnection;
import org.dengues.core.process.ICompGefNode;
import org.dengues.core.process.IGefNode;
import org.dengues.designer.ui.process.editors.GEFComponentsEditor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 1 2006-09-29 17:06:40Z qiang.zhang $
 * 
 */
public abstract class AbstractGEFCommand extends Command {

    protected CompElement element;

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "setModified".
     * 
     * @param target
     */
    private void setModified(IGefNode target) {
        if (target instanceof ICompGefNode) {
            ICompGefNode gefNode = (ICompGefNode) target;
            if (gefNode.isExternalNode()) {
                gefNode.setModified(true);
            }
        }
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "setExternalNodeModified".
     * 
     * @param connection
     */
    protected void setModified(ICompConnection connection) {
        setModified(connection.getSource());
        setModified(connection.getTarget());
    }

    @Override
    public abstract void undo();

    @Override
    public abstract void execute();

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "initNewBounds".
     * 
     * @param newBounds
     */
    protected void initNewBounds(Rectangle newBounds) {
        if (element.getCompProcess().isGridEnabled()) {
            int tempVar = newBounds.x / GEFComponentsEditor.getGrid();
            newBounds.x = tempVar * GEFComponentsEditor.getGrid();
            tempVar = newBounds.y / GEFComponentsEditor.getGrid();
            newBounds.y = tempVar * GEFComponentsEditor.getGrid();
        }
    }

    public boolean isContainsRect(Rectangle newBounds) {
        if (element != null) {
            List<? extends CompElement> elements = element.getCompProcess().getElements();
            for (CompElement e : elements) {
                if (e != element && e.getLocation() != null && e.getSize() != null && !e.getSize().equals(Dimension.SINGLETON)) {
                    Rectangle rectangle = new Rectangle(e.getLocation(), e.getSize());
                    if (rectangle.touches(newBounds)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
