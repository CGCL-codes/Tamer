package pso;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import pso.ui.*;

public final class PsoMain extends Applet {

    private static final int width = pso.common.PsoConstants.PSO_UI_DEFAULT_WIDTH;

    private static final int height = pso.common.PsoConstants.PSO_UI_DEFAULT_HEIGHT;

    private Container container;

    private void go() {
        WizardController c = new WizardController();
        c.init();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        container.setLayout(gbl);
        gbl.addLayoutComponent(c.getView(), gbc);
        container.add(c.getView());
        container.setVisible(true);
    }

    public static void main(String[] args) {
        PsoMain instance = new PsoMain();
        Frame f = new Frame();
        f.setSize(width, height);
        instance.container = f;
        f.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        instance.go();
    }

    private boolean appletIsRunning = false;

    @Override
    public void start() {
        if (!appletIsRunning) {
            appletIsRunning = true;
            go();
        }
    }

    @Override
    public void init() {
        container = this;
    }

    private static final long serialVersionUID = 1L;
}
