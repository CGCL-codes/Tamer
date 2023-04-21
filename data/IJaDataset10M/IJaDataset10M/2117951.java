package hotheart.starcraft.units.target;

import hotheart.starcraft.units.Flingy;

public class FlingyTarget extends AbstractTarget {

    Flingy dest;

    int R;

    public FlingyTarget(Flingy u, int radius) {
        dest = u;
        R = radius;
    }

    public FlingyTarget(Flingy u) {
        this(u, 10);
    }

    @Override
    public int getDestinationX() {
        return dest.getPosX();
    }

    @Override
    public int getDestinationY() {
        return dest.getPosY();
    }

    @Override
    public int getDestinationRadius() {
        return R;
    }
}
