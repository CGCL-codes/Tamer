package starcraft.gameclient.ui;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import test.SimpleTest;
import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

public class TwlSimple extends DesktopArea {

    public static void main(String[] args) {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
            Display.setTitle("TWL Chat Demo");
            Display.setVSyncEnabled(true);
            LWJGLRenderer renderer = new LWJGLRenderer();
            TwlSimple chat = new TwlSimple();
            Label label = new Label("laber");
            label.setTheme("label");
            chat.add(label);
            GUI gui = new GUI(chat, renderer);
            ThemeManager theme = ThemeManager.createThemeManager(TwlSimple.class.getClassLoader().getResource("simple.xml"), renderer);
            chat.setTheme("");
            gui.applyTheme(theme);
            while (!Display.isCloseRequested()) {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                gui.update();
                Display.update();
                SimpleTest.reduceInputLag();
            }
            gui.destroy();
            theme.destroy();
        } catch (Exception ex) {
            SimpleTest.showErrMsg(ex);
        }
        Display.destroy();
    }
}
