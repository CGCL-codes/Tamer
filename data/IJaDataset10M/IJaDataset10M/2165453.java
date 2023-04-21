package antirashka.map.terrains;

import antirashka.icons.IconManager;
import javax.swing.*;

public final class Wall extends Solid {

    public static final Wall INSTANCE = new Wall();

    private Wall() {
    }

    public Icon getIcon() {
        return IconManager.getInstance().getTerrain3(1, 1);
    }
}
