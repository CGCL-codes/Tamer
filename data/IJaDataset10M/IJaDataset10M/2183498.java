package antirashka.map;

import antirashka.ballistics.IWeapon;
import javax.swing.*;
import java.io.Serializable;

public interface IItem extends Serializable {

    int LAYER_EARTH = 0;

    int LAYER_STANDING = 1;

    boolean canBeSteppedBy(IItem who);

    boolean canBePushedBy(IItem who);

    boolean isTransparent();

    boolean canPassThrough(IWeapon weapon);

    boolean canBeShotBy(IWeapon weapon);

    Icon getIcon();

    int getLayer();
}
