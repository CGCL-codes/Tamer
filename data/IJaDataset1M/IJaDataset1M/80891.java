package de.mogwai.common.web.component.layout;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Tag for the gridbag layout component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:29:28 $
 */
@SuppressWarnings("serial")
public class GridbagLayoutSizeDefinitionVector extends Vector<GridbagLayoutSizeDefinition> {

    public GridbagLayoutSizeDefinitionVector(String aDefinition) {
        for (StringTokenizer theSt = new StringTokenizer(aDefinition, ","); theSt.hasMoreTokens(); ) {
            add(GridBagLayoutUtils.getLayoutSizeDefinition(theSt.nextToken()));
        }
    }

    public int getTotalSizeWithoutStar() {
        int theSize = 0;
        for (GridbagLayoutSizeDefinition theDefinition : this) {
            String theWidth = theDefinition.getSize();
            if (!GridBagLayoutUtils.INHERITED_SIZE_MARKER.equals(theWidth)) {
                theSize += Integer.parseInt(theWidth);
            }
        }
        return theSize;
    }
}
