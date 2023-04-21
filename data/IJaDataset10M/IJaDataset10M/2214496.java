package wotlas.libs.graphics2D.menu;

/** Interface of a Menu2D listener. 
 *
 * @author Aldiss
 * @see wotlas.libs.graphics2D.menu.Menu2DEvent
 */
public interface Menu2DListener {

    /** Method called when an item has been clicked on an item who is not a menu link.
     *  @param e menu event generated.
     */
    public void menuItemClicked(Menu2DEvent e);
}
