package pl.olek.textmash;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import pl.olek.textmash.menu.MenuBuilder;

/**
 * 
 * @author anaszko
 *
 */
public class WindowsSupport extends Support {

    Font font = new Font("Courier New", Font.PLAIN, 13);

    public WindowsSupport() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public int positionDialogButton(int amount, int index) {
        return index;
    }

    @Override
    public KeyStroke getRedoKey() {
        return MenuBuilder.getStdKeyStroke(KeyEvent.VK_Y, MenuBuilder.NONE);
    }

    @Override
    public String getTitle(String name) {
        return String.format("%s - TextMash", name);
    }

    @Override
    public URL getResource(String name) {
        return Support.class.getClassLoader().getResource(name);
    }

    @Override
    public String getId() {
        return "win";
    }

    @Override
    public String getUsername() {
        return System.getenv("USERNAME");
    }
}
