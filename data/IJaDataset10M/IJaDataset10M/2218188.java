package cn.rui.powermanja.pane.roam.vessel.gun;

import cn.rui.powermanja.pane.roam.Bullet;
import cn.rui.powermanja.pane.roam.Vessel;
import cn.rui.powermanja.pane.roam.bullet.StraightBullet;
import cn.rui.powermanja.pane.roam.vessel.Gun;

public class StraightGun extends Gun {

    protected int aj;

    protected int v;

    protected int iconTempo;

    /**
	 * Construct a straight gun with owner, type, framePeak, condition, arrNo,
	 * angle adjustment, and velocity.
	 */
    public StraightGun(Vessel vessel, int type, int iconTempo, int collision, int arrNo, int aj, int v) {
        super(vessel, type, collision, arrNo);
        this.iconTempo = iconTempo;
        this.arrNo = arrNo;
        this.aj = aj;
        this.v = v;
    }

    @Override
    public Bullet getBullet() {
        return new StraightBullet(vessel, type, iconTempo, collision, vessel.getIcon().getArrows()[arrNo], aj, v);
    }
}
