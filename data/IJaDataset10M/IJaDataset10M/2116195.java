package edu.nps.moves.kmleditor.types.panels;

import edu.nps.moves.kmleditor.palette.BaseKmlItem;
import edu.nps.moves.kmleditor.palette.KmlItemHolder;
import edu.nps.moves.kmleditor.palette.items.AbstractStyleSelector;
import edu.nps.moves.kmleditor.types.panels.gui.UpRightArrowWrapperPanel;
import javax.swing.border.LineBorder;

/**
 *
 * @author mike
 */
public class AbstractStyleSelectorTypePanel_collapsible extends UpRightArrowWrapperPanel implements KmlItemHolder {

    private AbstractStyleSelector absObj;

    private AbstractStyleSelectorTypePanel pan;

    public AbstractStyleSelectorTypePanel_collapsible() {
        super();
        setBorder(new LineBorder(UpRightArrowWrapperPanel.LINEBORDERCOLOR, 1));
    }

    public BaseKmlItem getDataObject() {
        return absObj;
    }

    public void loadDataObject(BaseKmlItem obj) {
        absObj = (AbstractStyleSelector) obj;
        pan = new AbstractStyleSelectorTypePanel();
        pan.loadDataObject(absObj);
        setContent(pan, "<html><b>metadata");
        if (absObj.hasContent()) setExpanded(true);
    }

    public void unloadInput() throws IllegalArgumentException {
        pan.unloadInput();
    }
}
