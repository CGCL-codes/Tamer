package test;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.skin.*;
import test.check.SampleFrame;

public class MultipleSkins {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                SubstanceLookAndFeel.setSkin(new BusinessBlackSteelSkin());
                JFrame.setDefaultLookAndFeelDecorated(true);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd = ge.getDefaultScreenDevice();
                GraphicsConfiguration gc = gd.getDefaultConfiguration();
                Rectangle screenBounds = gc.getBounds();
                screenBounds.x = 0;
                screenBounds.y = 0;
                Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
                Rectangle maxBounds = new Rectangle(screenBounds.x + screenInsets.left, screenBounds.y + screenInsets.top, screenBounds.width - ((screenInsets.left + screenInsets.right)), screenBounds.height - ((screenInsets.top + screenInsets.bottom)));
                SubstanceSkin[][] skinGrid = new SubstanceSkin[][] { { new AutumnSkin(), new BusinessSkin(), new BusinessBlueSteelSkin(), new BusinessBlackSteelSkin() }, { new NebulaSkin(), new CremeSkin(), new CremeCoffeeSkin(), new ModerateSkin() }, { new OfficeSilver2007Skin(), new SaharaSkin(), new MistAquaSkin(), new NebulaBrickWallSkin() }, { new RavenSkin(), new GraphiteSkin(), new GraphiteGlassSkin(), new MagellanSkin() } };
                int x = maxBounds.x;
                for (int i = 0; i < 4; i++) {
                    int y = maxBounds.y;
                    for (int j = 0; j < 3; j++) {
                        SampleFrame skinFrame = new SampleFrame();
                        skinFrame.getRootPane().putClientProperty(SubstanceLookAndFeel.SKIN_PROPERTY, skinGrid[i][j]);
                        SwingUtilities.updateComponentTreeUI(skinFrame);
                        skinFrame.setBounds(x, y, maxBounds.width / 4, maxBounds.height / 3);
                        skinFrame.setVisible(true);
                        skinFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        y += maxBounds.height / 3;
                    }
                    x += maxBounds.width / 4;
                }
            }
        });
    }
}
