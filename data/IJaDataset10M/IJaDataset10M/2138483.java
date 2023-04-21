package zombieville.engine;

import zombieville.GUI.BackgroundGUI;

/**
 *
 * @author -Gui-
 */
public class Background extends Item {

    public Background(int x, int y, Cenario cenarioPai) {
        super(x, y, cenarioPai);
    }

    @Override
    public void criaItemGui() {
        setItemGui(new BackgroundGUI(this));
        System.out.println("Aqui");
    }
}
