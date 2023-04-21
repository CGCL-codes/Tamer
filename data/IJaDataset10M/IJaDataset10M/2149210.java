package de.mogwai.common.web.component.layout;

import javax.faces.component.UIComponent;
import de.mogwai.common.logging.Logger;

/**
 * The GridbagLayout component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:29:23 $
 */
public final class GridBagLayoutUtils {

    public static final int PREFFERED_SIZE = 21;

    public static final String PREFFERED_SIZE_MARKER = "p";

    public static final String INHERITED_SIZE_MARKER = "*";

    private static Logger logger = new Logger(GridBagLayoutUtils.class);

    private GridBagLayoutUtils() {
    }

    public static GridbagLayoutSizeDefinition getLayoutSizeDefinition(String aDefinition) {
        int p = aDefinition.indexOf(":");
        if (p < 0) {
            if (PREFFERED_SIZE_MARKER.equals(aDefinition)) {
                aDefinition = "" + PREFFERED_SIZE;
            }
            return new GridbagLayoutSizeDefinition(null, aDefinition);
        } else {
            String theAlign = aDefinition.substring(0, p);
            String theDefinition = aDefinition.substring(p + 1);
            if (PREFFERED_SIZE_MARKER.equals(theDefinition)) {
                theDefinition = "" + PREFFERED_SIZE;
            }
            return new GridbagLayoutSizeDefinition(theAlign, theDefinition);
        }
    }

    public static GridbagLayoutCellComponent findSurroundingCell(UIComponent aParent) {
        if (aParent instanceof GridbagLayoutCellComponent) {
            return (GridbagLayoutCellComponent) aParent;
        }
        aParent = aParent.getParent();
        if (aParent != null) {
            return findSurroundingCell(aParent);
        }
        return null;
    }

    public static String getSizeForColumn(UIComponent aParent, GridbagLayoutSizeDefinitionVector aCols, int aColumn) {
        GridbagLayoutSizeDefinition theColDef = aCols.get(aColumn);
        String theWidth = theColDef.getSize();
        if (theColDef.isInheritedSize()) {
            GridbagLayoutCellComponent theSurroundingCell = findSurroundingCell(aParent);
            if (theSurroundingCell != null) {
                int theTotalWidth = aCols.getTotalSizeWithoutStar();
                theWidth = "" + (theSurroundingCell.computeWidth() - theTotalWidth);
            } else {
                logger.logWarning("Gridbaglayout verwendet * ohne umgebende Gridbaglayout - Zelle");
            }
        }
        return theWidth;
    }
}
