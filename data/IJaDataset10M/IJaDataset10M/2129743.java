package tw.bennu.feeler.main.lcanorus;

import java.awt.Point;
import java.util.Random;
import java.util.UUID;
import tw.bennu.feeler.apps.packet.lcanorus.FightAppsPacket;
import tw.bennu.feeler.apps.packet.lcanorus.HoldAppsPacket;
import tw.bennu.feeler.main.core.EntityLifecycle;

/**
 * 實作為一個兢技場裡作戰的戰士，每個戰士模擬為一個 feeler 的 entity，<br/>
 * 戰士與戰士過於接近時會 Circus 會觸發彼此的交戰，模擬 entity 的 "上線" 訊息。<br/>
 * 
 * @author muchu
 * 
 */
public class LcanorusWarrior implements ICircusWarrior {

    private Point location = new Point(250, 250);

    private Random rand = new Random();

    private EntityLifecycle heart = null;

    @Override
    public void killWarrior() {
        this.getHeart().stopLifecycle();
    }

    /**
	 * 取得戰士位置
	 * 
	 * @return
	 */
    @Override
    public Point getLocation() {
        return location;
    }

    /**
	 * 觸發戰士進行移動，以傳入的 range 和 0 為移動範圍。
	 * 
	 * @param xrange
	 * @param yrange
	 */
    @Override
    public void changeLocation(int xrange, int yrange) {
        int xstep = 5 - rand.nextInt(11);
        int ystep = 5 - rand.nextInt(11);
        int xtarget = location.x + xstep;
        int ytarget = location.y + ystep;
        if (xtarget > xrange) {
            xtarget = xrange;
        }
        if (ytarget > yrange) {
            ytarget = yrange;
        }
        if (xtarget < 0) {
            xtarget = 0;
        }
        if (ytarget < 0) {
            ytarget = 0;
        }
        location.setLocation(xtarget, ytarget);
    }

    @Override
    public void commandWarrior(String cmd) {
        System.out.println("執行 " + cmd);
    }

    @Override
    public void fightTo(ICircusWarrior enemy) {
        UUID srcEntityUUID = this.getHeart().getEntityUUID();
        UUID destEntityUUID = enemy.getHeart().getEntityUUID();
        this.getHeart().getUtilServ().sendAppsPacket(srcEntityUUID, destEntityUUID, new FightAppsPacket(this.toString() + " fight to " + enemy.toString()));
    }

    @Override
    public void holdTo(ICircusWarrior enemy) {
        UUID srcEntityUUID = this.getHeart().getEntityUUID();
        UUID destEntityUUID = enemy.getHeart().getEntityUUID();
        this.getHeart().getUtilServ().sendAppsPacket(srcEntityUUID, destEntityUUID, new HoldAppsPacket(this.toString() + " hold to " + enemy.toString()));
    }

    @Override
    public String toString() {
        return "( " + location.x + " , " + location.y + " )";
    }

    @Override
    public EntityLifecycle getHeart() {
        return heart;
    }

    public void setHeart(EntityLifecycle heart) {
        this.heart = heart;
    }
}
