package uk.ac.bath.domains.prey;

import java.util.Vector;
import uk.ac.bath.base.MachineIF;
import uk.ac.bath.util.Tweakable;
import uk.ac.bath.util.TweakableInt;

public class HunterCell extends BasicCell {

    public static final int nIn = 14;

    public static final int nOut = 3;

    private MyPoint diff = new MyPoint();

    public static Vector<Tweakable> tweaks = new Vector<Tweakable>();

    public static TweakableInt tickCost = new TweakableInt(tweaks, 0, 200, 10, "TickCost");

    public static TweakableInt moveCost = new TweakableInt(tweaks, 0, 200, 10, "MoveCost");

    public static TweakableInt initFitness = new TweakableInt(tweaks, 1, 2000, 800, "InitialFitness");

    double res = 60;

    Integer preySector;

    int preyScannerResolution = 60;

    private boolean captured = false;

    Integer homeSector;

    public HunterCell(MachineIF machine, MyPoint point, String tag) {
        super(machine, point, tag, initFitness.doubleValue());
    }

    public void preMachineTick(MachineIF machine) {
        if (collided) {
            machine.setIn(0, 1);
        } else {
            machine.setIn(0, -1);
        }
        if (captured) {
            machine.setIn(1, 1);
        } else {
            machine.setIn(1, -1);
        }
        for (int i = 0; i < 6; i++) {
            if (preySector != null && i == preySector) {
                machine.setIn(2 + i, 1);
            } else {
                machine.setIn(2 + i, -1);
            }
            if (homeSector != null && i == homeSector) {
                machine.setIn(8 + i, 1);
            } else {
                machine.setIn(8 + i, -1);
            }
        }
    }

    public void postMachineTick(MachineIF machine) {
        fitness -= moveCost.doubleValue() * vel;
        float left = machine.out(0);
        float right = machine.out(1);
        vel = machine.out(2);
        float dang = (left - right) * 0.2f;
        dir.rotateRads(dang);
        fitness -= tickCost.doubleValue();
    }

    void capturePrey() {
        captured = true;
    }

    void devourPrey() {
        captured = false;
    }

    boolean hasCaptured() {
        return captured;
    }

    void setPreyScanner(MyPoint prey) {
        if (prey == null) {
            preySector = null;
            return;
        }
        MyPoint.diff((MyPoint) point, prey, diff);
        double ang1 = diff.getAngle();
        double myang = dir.getAngle();
        if (diff != null) {
            double ang = ang1 - myang;
            double angDeg = (180.0 * ang / Math.PI);
            if (angDeg < 0) {
                angDeg += 360;
            }
            preySector = (int) (angDeg / preyScannerResolution);
        } else {
            preySector = null;
        }
    }

    void setHomeScanner(MyPoint home) {
        if (home == null) {
            homeSector = null;
            return;
        }
        MyPoint.diff((MyPoint) point, home, diff);
        double ang1 = diff.getAngle();
        double myang = dir.getAngle();
        if (diff != null) {
            double ang = ang1 - myang;
            double angDeg = (180.0 * ang / Math.PI);
            if (angDeg < 0) {
                angDeg += 360;
            }
            homeSector = (int) (angDeg / preyScannerResolution);
        } else {
            homeSector = null;
        }
    }
}
