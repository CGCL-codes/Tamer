package cn.rui.powermanja.pane.roam.hostile.guardian;

import static cn.rui.powermanja.util.Audio.GUN;
import cn.rui.powermanja.pane.roam.hostile.Guardian;
import cn.rui.powermanja.pane.roam.hostile.LargeHostile;
import cn.rui.powermanja.util.Icon;
import cn.rui.powermanja.util.ResourceReader;
import cn.rui.powermanja.util.Tempo;

public abstract class Assistant extends LargeHostile {

    public static final String RESOURCE = "image/assistants.icn";

    protected static final Icon[][] ICONS = new Icon[8][32];

    static {
        ResourceReader in = new ResourceReader(RESOURCE);
        for (int i = 0; i < ICONS.length; i++) {
            for (int j = 0; j < ICONS[i].length; j++) {
                ICONS[i][j] = new Icon(in);
            }
        }
        in.close();
    }

    public static final int SHURIKY = 0;

    public static final int NAGGYS = 1;

    public static final int GOZUKY = 2;

    public static final int SOUKEE = 3;

    public static final int QUIBOULY = 4;

    public static final int TOURNADEE = 5;

    public static final int SAAKEY = 6;

    public static final int SAAKAMIN = 7;

    public static final String[] NAMES = { "SHURIKY", "NAGGYS", "GOZUKY", "SOUKEE", "QUIBOULY", "TOURNADEE", "SAAKEY", "SAAKAMIN" };

    protected float v;

    protected Tempo tempo;

    protected Guardian guardian;

    public Assistant(Guardian guardian, int type, int iconIdx, int iconTempo, float x, float y, int fireTempo, int collision, int condition, float v) {
        super(guardian.getStage(), NAMES[type], ICONS[type], iconIdx, x, y, fireTempo, collision, condition);
        this.guardian = guardian;
        this.v = v;
        tempo = new Tempo(iconTempo);
        GUN.play();
    }
}
