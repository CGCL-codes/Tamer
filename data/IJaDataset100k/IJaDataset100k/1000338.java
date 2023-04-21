package superweapons;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import core.Unit;

public abstract class SuperWeapon extends Unit {

    public SuperWeapon(Dimension effectSize, ImageIcon img) {
        super(null, 1, img, effectSize, 0);
    }

    public void onActivate() {
    }

    public boolean isAimed() {
        return true;
    }
}
