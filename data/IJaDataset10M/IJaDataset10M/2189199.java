package br.ufal.tci.nexos.arcolive.gui;

import java.awt.Color;
import javax.swing.JDesktopPane;

/**
 * 
 * Desktop.java
 *
 * CLASS DESCRIPTION
 *
 * @see Jan 4, 2008
 *
 * @author <a href="mailto:txithihausen@gmail.com">Ivo Augusto Andrade R Calado</a>.
 * @author <a href="mailto:thiagobrunoms@gmail.com">Thiago Bruno Melo de Sales</a>.
 * @since 0.1
 * @version 0.1
 *
 * <p><b>Revisions:</b>
 *
 * <p><b>yyyymmdd USERNAME:</b>
 * <ul>
 * <li> VERSION
 * </ul>
 */
public class Desktop {

    private static Desktop singletonInstance;

    private JDesktopPane desktopPane;

    private Desktop() {
        this.desktopPane = new JDesktopPane();
        this.setBackground();
    }

    public static Desktop getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new Desktop();
        }
        return singletonInstance;
    }

    private void setBackground() {
        this.desktopPane.setBackground(new Color(21, 153, 251));
    }

    public JDesktopPane getDesktopPane() {
        return this.desktopPane;
    }

    public void setDesktopPane(JDesktopPane desktopPane) {
        this.desktopPane = desktopPane;
    }
}
