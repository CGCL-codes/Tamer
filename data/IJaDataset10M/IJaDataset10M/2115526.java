package mipt.gui.data.choice;

import java.awt.Component;
import mipt.data.Data;
import mipt.data.choice.DataManager;
import mipt.gui.ComponentOwner;

/**
 * Object that contains other objects, designed for:
 *   view of data of some set (getComponent()),
 *   adding and removing data from that set (getModel()),
 *   choosing single Data objects from that set (getChooser()),
 *   notifing listeners about single selection events (getSelector()),
 *   notifing listeners about "open" events (double-clicks) (getActor()).
  * Because component (that is laid out on some container) does not often
 *  coinside with top-level component (that receive events)
 *  we also has getEventComponent() that is needed to show popup menu.
 *  If mouse listener added to it wants to determine data by MouseEvent point
 *    (and not use selected data) we also has getDataAt(x, y)
 *  You can also add focus or mouse motion listener to getEventComponent()
*/
public interface DataChoiceManager extends DataManager, ComponentOwner, AbstractDataChoiceManager {

    /**
	 * Return top-level component that can generate events (which is often not getComponent()!)
	 * Often used to add JPopupMenu (and approapriate MouseListener)
	 * @param listener java.awt.event.MouseListener
	 */
    Component getEventComponent();

    /**
	 * Listener added in this.addMouseListener() can call this this e.getX(), e.getY() args
	 */
    Data getDataAt(int x, int y);

    /**
	 * 
	 * @return mipt.gui.data.choice.DataChoiceModel
	 */
    DataChoiceModel getModel();

    /**
	 * 
	 * @return mipt.gui.data.choice.DataChooser
	 */
    DataChooser getChooser();
}
