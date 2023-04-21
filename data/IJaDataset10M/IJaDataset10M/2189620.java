package com.cafe.serve.event.handler;

import org.apache.log4j.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import com.cafe.serve.util.dialogs.ShowOrderInJFrame;

/**
 * @author Raptis Asterios
 * Created first on 09.12.2004 in project ServerThreaded
 */
public class ShowOrderEventHandler extends WindowAdapter implements ActionListener {

    private static Logger logger = Logger.getLogger(ShowOrderEventHandler.class.getName());

    private ShowOrderInJFrame showOrderInJFrame = null;

    /**
     * 
     */
    public ShowOrderEventHandler(ShowOrderInJFrame showOrderInJFrame) {
        this.showOrderInJFrame = showOrderInJFrame;
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof JButton) {
            JButton jbutton = (JButton) source;
            if (jbutton.getName().equals("Getr�nke bereit")) {
                System.out.println("Button wurde gedr�ckt: " + jbutton.getName());
                showOrderInJFrame.setVisible(false);
                showOrderInJFrame.dispose();
            }
        }
    }

    public void windowClosing(WindowEvent we) {
        showOrderInJFrame.setVisible(false);
        showOrderInJFrame.dispose();
    }
}
