package oext.gui;

import javax.swing.UIManager;
import java.awt.*;
import java.io.File;

public class Oext_App {

    boolean packFrame = false;

    String fileName1 = null;

    String fileName2 = null;

    public Oext_App() {
    }

    public void start() {
        MainFrame frame = new MainFrame();
        if (packFrame) frame.pack(); else frame.validate();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        if (fileName1 != null) {
            frame.setFileName1(fileName1);
        }
        if (fileName2 != null) {
            frame.setFileName2(fileName2);
        }
        if ((fileName1 != null) && (fileName2 != null)) {
            frame.construct();
        }
        frame.setVisible(true);
    }

    public static boolean fileExists(String fileName) {
        File testFile = new File(fileName);
        return testFile.exists();
    }

    public static void main(String[] args) {
        Oext_App app = new Oext_App();
        int i = 0;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        while (i < args.length) {
            String arg = args[i];
            if (arg.startsWith("-")) {
            } else if ((app.fileName1 == null) && fileExists(arg)) {
                app.fileName1 = arg;
            } else if ((app.fileName2 == null) && fileExists(arg)) {
                app.fileName2 = arg;
            }
            ++i;
        }
        app.start();
    }
}
