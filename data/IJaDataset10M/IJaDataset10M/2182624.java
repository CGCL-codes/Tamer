package adventure.placeable.trigger;

import java.util.StringTokenizer;
import adventure.placeable.Item;
import adventure.placeable.ItemImp;
import player.Player;
import adventure.room.RoomManager;
import adventure.room.RoomManagerEdit;

/**
 *
 * @author Michael Hanns
 *
 */
public class ResultSpawnItem extends TriggerResult implements Cloneable {

    private Item i;

    public ResultSpawnItem(String result, Item i) {
        super(result);
        this.i = i;
    }

    public ResultSpawnItem(String result, int uses, Item i) {
        super(result, uses);
        this.i = i;
    }

    public ResultSpawnItem() {
        super();
        i = new ItemImp();
    }

    public void setItem(Item item) {
        i = item;
    }

    public int getItemID() {
        return i.getID();
    }

    @Override
    public String getDescription() {
        if (super.limitedUses()) {
            String desc = super.resultText + ", uses: " + super.usesRemaining() + " / SPAWNITEM " + i.getName();
            return desc;
        } else {
            String desc = super.resultText + " / SPAWNITEM " + i.getName();
            return desc;
        }
    }

    @Override
    public String executeResult(RoomManager r, Player p, String in, boolean previousFailed) {
        if (allConditionsMet(r, p, in, previousFailed)) {
            ((RoomManagerEdit) r).addItemToRoom(i.cloneThis(), p.x(), p.y(), p.z());
            useOnce();
            return replaceKeywords(p);
        } else {
            return "";
        }
    }

    @Override
    public String getType() {
        return "SPAWNITEM";
    }

    @Override
    public String replaceKeywords(Player p) {
        return resultText.replace("[PLAYER]", p.getName()).replace("[ITEM]", i.getName()).replace("[USES]", super.usesRemaining() + "");
    }
}
