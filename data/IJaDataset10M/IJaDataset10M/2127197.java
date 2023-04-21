package creatureAI_API.interfaces;

import java.util.List;
import creatureAI_API.supportInterfaces.RItem;
import creatureAI_API.supportInterfaces.RLivingSpace;

public interface SensoryInterface {

    public int[] getGPS();

    public List<RItem> getItemsInRoom();

    /**
	 * Gets an item from the room and adds it to creature.
	 * @author Matthew Cummings
	 * @date Dec 28, 2009
	 * @param itemID
	 * @return : True if Item was added successfully, false if creature can't carry any more or if item not found.
	 */
    public boolean pickUpItem(int itemID);

    public RLivingSpace getLivingSpace();

    public RItem searchRoom();
}
