package fr.amille.animebrowser.view;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import fr.amille.animebrowser.control.MainControl;
import fr.amille.animebrowser.model.Configuration;
import fr.amille.animebrowser.model.language.Language;
import fr.amille.animebrowser.model.language.listener.LanguageListener;

/**
 * 
 * @author amille
 */
@SuppressWarnings("serial")
public class MainView extends JFrame implements LanguageListener {

    public MainView() {
        this.setPreferredSize(new Dimension(1024, 768));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(Language.getInstance().get("FrameTitle"));
        final Image icon = Toolkit.getDefaultToolkit().getImage(Configuration.getInstance().getVariables().get("Button") + "icon.png");
        this.setIconImage(icon);
        SwingUtilities.updateComponentTreeUI(this);
        this.addWindowListener(MainControl.getInstance().getControlViewPrincipal());
    }

    @Override
    public void languageChange() {
        this.setTitle(Language.getInstance().get("FrameTitle"));
    }

    @SuppressWarnings("unused")
    private void lookFeel() {
        this.windowsLookAndFeel();
    }

    private void windowsLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
}
