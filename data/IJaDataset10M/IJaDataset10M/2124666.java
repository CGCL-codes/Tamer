package cn.rui.powermanja.pane.roam.sprite;

import java.awt.Point;
import cn.rui.powermanja.pane.roam.Sprite;
import cn.rui.powermanja.util.Icon;
import cn.rui.powermanja.util.ResourceReader;
import cn.rui.powermanja.util.Tempo;
import static cn.rui.powermanja.util.Audio.*;
import static cn.rui.powermanja.Powermanja.*;

public class Explosion extends Sprite {

    public static final String RESOURCE = "image/explosions.icn";

    public static final int MINI = 0;

    public static final int NORM = 1;

    public static final int BIG = 2;

    public static final int TINY = 3;

    private static final Icon[][] ICONS = { new Icon[32], new Icon[32], new Icon[32], new Icon[8] };

    static {
        ResourceReader in = new ResourceReader(RESOURCE);
        for (int i = 0; i < ICONS.length; i++) {
            for (int j = 0; j < ICONS[i].length; j++) {
                ICONS[i][j] = new Icon(in);
            }
        }
        in.close();
    }

    private int v;

    private int a = 8;

    private int timer = 0;

    private Tempo tempo = new Tempo(6);

    private int type;

    public Explosion(Sprite sponsor, int type, float v) {
        super(sponsor, ICONS[type], 0);
        Point pos = sponsor.getCenter();
        Point ct = getIcon().getCenter();
        x = pos.x - ct.x;
        y = pos.y - ct.y;
        this.type = type;
        this.v = (int) v;
    }

    public Explosion(Sprite sponsor, int type, float x, float y, float a, float v, int timer) {
        super(sponsor, ICONS[type], 0, x, y);
        this.timer = timer;
        this.type = type;
        this.v = (int) v;
        this.a = (int) a;
    }

    @Override
    public boolean run(boolean blocked) {
        if (!blocked) {
            x += DISPLACEX[v][a];
            y += DISPLACEY[v][a];
            if (timer-- == 0) {
                if (type < EXPLOSION.length) EXPLOSION[type][RAND.nextInt(EXPLOSION[type].length)].play();
            }
        }
        if (timer <= 0) {
            if (tempo.tick()) {
                iconIdx = (iconIdx + 1) % icons.length;
                if (iconIdx == 0 & !blocked) return true;
            }
            drawIcon();
        }
        return false;
    }
}
