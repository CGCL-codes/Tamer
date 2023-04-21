package se.sics.tasim.tacscm.atp;

import java.awt.BorderLayout;
import javax.swing.JApplet;

/**
 */
public class SupplierApplet extends JApplet {

    private SupplierViewer sup = new SupplierViewer();

    public SupplierApplet() {
        getContentPane().add(sup, BorderLayout.CENTER);
        getContentPane().add(sup.getButtonPanel(), BorderLayout.SOUTH);
    }

    public void start() {
        sup.start();
    }

    public void stop() {
        sup.stop();
    }
}
