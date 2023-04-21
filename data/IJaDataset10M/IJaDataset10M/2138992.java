package cc.sprite;

/**
 * An interface used by the WElement.walk method to traverse the descendents
 * of a given element.
 * 
 * @author Joe Mayer
 */
public interface WElementWalker {

    /**
   * Called by WElement.walk with each child of a given element.  This 
   * method is first called with a parent element and then its descendents
   * a depth first manner.
   * 
   * @see cc.sprite.WElement#walk(WElementWalker)
   * 
   * @param e The next element in the walk.
   */
    public void walk(WElement e);
}
