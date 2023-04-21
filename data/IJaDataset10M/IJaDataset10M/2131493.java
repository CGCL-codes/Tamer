package diet.task.tangram2D1M;

import java.awt.Graphics2D;

public abstract class Slot {

    public static final int clientImageBorder = 3;

    public static final int clientWidth = 96;

    public static final int clientHeight = 126;

    public static final int serverImageBorder = 1;

    public static final int serverWidth = 48;

    public static final int serverHeight = 63;

    public int x;

    public int y;

    public int slotID;

    public Tangram tangram = null;

    public boolean enabled;

    public Slot(Tangram img, int slotID, int x, int y) {
        this.x = x;
        this.y = y;
        this.slotID = slotID;
        this.tangram = img;
        enabled = false;
    }

    public Slot(Tangram img, int x, int y) {
        this.x = x;
        this.y = y;
        this.slotID = -1;
        this.tangram = img;
    }

    public abstract void draw(Graphics2D g2, char slotType);

    public boolean isEmpty() {
        return tangram == null;
    }
}
