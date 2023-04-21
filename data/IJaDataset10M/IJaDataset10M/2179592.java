package cn.rui.powermanja.pane.roam.hostile.guardian;

import static cn.rui.powermanja.Powermanja.RAND;
import static cn.rui.powermanja.Powermanja.VIEW_BOUNDS;
import java.awt.Dimension;
import java.awt.Rectangle;
import cn.rui.powermanja.pane.roam.hostile.Guardian;
import cn.rui.powermanja.pane.roam.hostile.guardian.assistant.Naggys;
import cn.rui.powermanja.pane.roam.sprite.Explosion;
import cn.rui.powermanja.pane.roam.stage.GuardianStage;
import cn.rui.powermanja.util.Icon;
import cn.rui.powermanja.util.ResourceReader;
import cn.rui.powermanja.util.Tempo;

public class Pikkiou extends Guardian {

    public static final String RESOURCE = "stage/gardian3.icn";

    private static final Icon[] ICONS = new Icon[32];

    static {
        ResourceReader in = new ResourceReader(RESOURCE);
        for (int i = 0; i < ICONS.length; i++) {
            ICONS[i] = new Icon(in);
        }
        in.close();
    }

    private static final Trajectory[] TRAJECTORY = new Trajectory[] { new Trajectory(32, 100, 0), new Trajectory(0, 400, 1), new Trajectory(32, 100, 0), new Trajectory(16, 400, 1) };

    private Tempo spec;

    public Pikkiou(GuardianStage stage) {
        super(stage, PIKKIOU, ICONS, 15, 3, 75, 0.5f, TRAJECTORY);
        spec = new Tempo(16);
    }

    @Override
    protected boolean takeMove() {
        if (apparition) {
            y += v;
            if (y >= VIEW_BOUNDS.y) {
                y = VIEW_BOUNDS.y;
                apparition = false;
                idx = 0;
                t = trajectories[idx].t;
            }
            if (RAND.nextBoolean() && RAND.nextInt(fullCondition + 1) > condition + fullCondition / 8) {
                Rectangle[] colZones = getIcon().getCollisionZones();
                int i = RAND.nextInt(colZones.length);
                roamPane.addObject(new Explosion(this, 0, x + colZones[i].x + RAND.nextInt(colZones[i].x + 1), y + colZones[i].y + RAND.nextInt(colZones[i].y + 1), 8, -0.5f, 0));
            }
        } else {
            if (--t > 0) {
                if (trajectories[idx].a < 32) {
                    x += DISPLACEX[trajectories[idx].v][trajectories[idx].a];
                    y += DISPLACEY[trajectories[idx].v][trajectories[idx].a];
                    Dimension size = getSize();
                    if (x < VIEW_BOUNDS.x) {
                        x = VIEW_BOUNDS.x;
                        t = 0;
                    } else if (x + size.width > VIEW_BOUNDS.x + VIEW_BOUNDS.width) {
                        x = VIEW_BOUNDS.x + VIEW_BOUNDS.width - size.width;
                        t = 0;
                    }
                    if (y < VIEW_BOUNDS.y) {
                        y = VIEW_BOUNDS.y;
                        t = 0;
                    } else if (y + size.height > VIEW_BOUNDS.y + VIEW_BOUNDS.height) {
                        y = VIEW_BOUNDS.y + VIEW_BOUNDS.height - size.height;
                        t = 0;
                    }
                }
                switch(trajectories[idx].a) {
                    case 0:
                    case 16:
                    case 32:
                        {
                            if (tempo.tick()) {
                                iconIdx = (iconIdx + 1) % icons.length;
                            }
                            break;
                        }
                }
            } else {
                idx = (idx + 1) % trajectories.length;
                t = trajectories[idx].t;
            }
        }
        return false;
    }

    @Override
    protected void takeFire() {
        super.takeFire();
        if (spec.tick()) {
            stage.addHostile(new Naggys(this));
        }
    }
}
