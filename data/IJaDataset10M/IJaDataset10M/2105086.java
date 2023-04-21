package EQNLangInterfaces.DisplayInterface;

import EQNLangInterfaces.DisplayInterface.ParamTypes.DisplayElemParams;

/**
 * This is a main data structure, it is what is passed to a Display
 *  to tell it what to paint.
 * GraphicalElements are chained together into a linked list.  Each
 * element contains:
 *  -- a type
 *  -- an object holding the parameters for that type
 *  -- a pointer to the next graphical element in the list
 *  
 *
 * @author SeanHalle@yahoo.com
 */
public class DisplayElement {

    public DisplayElementType type;

    public DisplayElemParams params;

    public int displayElementID;

    public DisplayElement nextElem = null;
}
