package com.dmanski.util.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import com.dmanski.util.ui.ImageCache.EIMAGE;

/**
 * Diese Klasse dient dazu einen SplashScreen beim Start der Anwendung 
 * anzuzeigen
 * @author 02.12.2008  Daniel Manski  D.Manski@dmanski.com 
 */
public class MDBSpashScreen extends JWindow {

    private static final long serialVersionUID = 5660673889677245305L;

    private JLabel infoLabel;

    /**
	 * Konstruktor
	 */
    public MDBSpashScreen() {
        super(new JFrame());
        this.setAlwaysOnTop(true);
        init();
    }

    /**
	 * Initialisiert die Komponenten
	 */
    private void init() {
        this.setSize(300, 300);
        this.getContentPane().setLayout(new GridBagLayout());
        JLabel theIconLabel = new JLabel();
        theIconLabel.setSize(new Dimension(600, 22));
        theIconLabel.setIcon(ImageCache.getInstance().get(EIMAGE.LOGO));
        JLabel theHeaderLabel = new JLabel(MDBPropertiesIF.TITLE + " - " + MDBPropertiesIF.DEVELOPER);
        infoLabel = new JLabel(MDBPropertiesIF.VERSION + "  " + MDBPropertiesIF.LASTDATE);
        infoLabel.setSize(new Dimension(600, 22));
        addComponentInLayout(theHeaderLabel, 0, 0, 1, 1, GridBagConstraints.NONE, GridBagConstraints.CENTER, 0., 0., new Insets(0, 0, 0, 0));
        addComponentInLayout(theIconLabel, 0, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER, 1., 1., new Insets(0, 0, 0, 0));
        addComponentInLayout(infoLabel, 0, 2, 1, 1, GridBagConstraints.NONE, GridBagConstraints.CENTER, 0., 0., new Insets(0, 0, 0, 0));
    }

    /**
	 * Zeigt den Splashscreen
	 */
    public void showSplashScreen() {
        int width = this.getWidth();
        int height = this.getHeight();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        this.setBounds(x, y, width, height);
        this.setVisible(true);
    }

    /**
	 * Versteckt den Splashscreen
	 */
    public void hideSplashScreen() {
        this.setVisible(false);
        this.dispose();
    }

    /**
	 * Setzt die uebergebene Componente entsprechend den Constraintparametern
	 * in das Gridbaglayout der uebergebenen Component ein.
	 *
	 * @predcondition Der uebergegeben Container muss ein Gridbaglayout haben!!!
	 * @param aComponentToAdd javax.swing.JComponent
	 * @param aGridX int
	 * @param aGridY int
	 * @param aGridWidth int
	 * @param aGridHeight int
	 * @param aFillConstraint int
	 * @param anAnchorConstraint int
	 * @param aWeightX int
	 * @param aWeightY int
	 * @param anInsets java.awt.Insets
	 */
    private void addComponentInLayout(JComponent aComponentToAdd, int aGridX, int aGridY, int aGridWidth, int aGridHeigth, int aFillConstraint, int anAnchorConstraint, double aWeightX, double aWeightY, Insets anInsets) {
        GridBagConstraints theConstraint = new GridBagConstraints();
        theConstraint.gridx = aGridX;
        theConstraint.gridy = aGridY;
        theConstraint.gridwidth = aGridWidth;
        theConstraint.gridheight = aGridHeigth;
        theConstraint.fill = aFillConstraint;
        theConstraint.anchor = anAnchorConstraint;
        theConstraint.weightx = aWeightX;
        theConstraint.weighty = aWeightY;
        theConstraint.insets = anInsets;
        this.getContentPane().add(aComponentToAdd, theConstraint);
    }

    public JLabel getInfoLabel() {
        return infoLabel;
    }
}
