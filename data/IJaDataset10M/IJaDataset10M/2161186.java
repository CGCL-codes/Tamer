package mp3.extras.customJComps;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import mp3.NewJFrame;
import mp3.services.GestorBusqueda;
import mp3.services.Service;
import mp3.services.ServiceSetter;
import mp3.services.UIUpdater;

/**
 *
 * @author user
 */
public class GestorBusquedaMonitor extends JDialog implements Runnable, Service {

    private static final long serialVersionUID = 1987891243L;

    private int millis = 3000;

    private boolean initialized = false;

    private JLabel dirText;

    private JLabel numberText;

    private JPanel panel;

    private Timer timer;

    public GestorBusquedaMonitor(NewJFrame owner) {
        super(owner);
    }

    private void init() {
        ((UIUpdater) ServiceSetter.getServiceSetter().getServiceByName(UIUpdater.class.getName())).compAddToList(this);
        setTitle(java.util.ResourceBundle.getBundle("Bundle").getString("GestorBusquedaMonitor.title"));
        JPanel over = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        dirText = new JLabel(java.util.ResourceBundle.getBundle("Bundle").getString("GestorBusquedaMonitor.dir.text"));
        numberText = new JLabel(java.util.ResourceBundle.getBundle("Bundle").getString("GestorBusquedaMonitor.number.text"));
        JButton but = new JButton(java.util.ResourceBundle.getBundle("Bundle").getString("GestorBusquedaMonitor.buttonAbort.text"));
        but.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((GestorBusqueda) ServiceSetter.getServiceSetter().getServiceByName(GestorBusqueda.class.getName(), ((NewJFrame) getOwner()).jList1)).pararBusqueda();
            }
        });
        panel = new JPanel(new GridLayout(2, 1));
        add(over);
        over.add(panel, BorderLayout.CENTER);
        over.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(but);
        panel.add(dirText);
        panel.add(numberText);
        setPreferredSize(new Dimension(600, 100));
        initialized = true;
    }

    @Override
    public void run() {
        timer = new Timer(true);
        String dirMessage = java.util.ResourceBundle.getBundle("Bundle").getString("GestorBusquedaMonitor.dir.text");
        String numberMessage = java.util.ResourceBundle.getBundle("Bundle").getString("GestorBusquedaMonitor.number.text");
        GestorBusqueda gbusq = (GestorBusqueda) ServiceSetter.getServiceSetter().getServiceByName(GestorBusqueda.class.getName(), ((NewJFrame) getOwner()).jList1);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(GestorBusquedaMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!gbusq.finalizado) {
            if (!initialized) {
                try {
                    SwingUtilities.invokeAndWait(new SafeMonitorCreation());
                } catch (InterruptedException ex) {
                    Logger.getLogger(GestorBusquedaMonitor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(GestorBusquedaMonitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                setVisible(true);
            }
            timer.scheduleAtFixedRate(new TimedMessage(gbusq, dirMessage, numberMessage), 0, 500);
        } else {
            Logger.getLogger(GestorBusquedaMonitor.class.getName()).fine("search finished. No need to show up");
        }
    }

    @Override
    public String getServiceName() {
        return GestorBusquedaMonitor.class.getName();
    }

    private class TimedMessage extends TimerTask {

        private GestorBusqueda gb;

        private String dirMessage;

        private String numberMessage;

        public TimedMessage(GestorBusqueda g, String dMess, String nMessage) {
            gb = g;
            dirMessage = dMess;
            numberMessage = nMessage;
        }

        @Override
        public void run() {
            if (dirText != null && numberText != null) {
                dirText.setText(dirMessage + " " + gb.getLastSearchingDir().getAbsolutePath());
                numberText.setText(numberMessage + " " + gb.getNumberOfFileAnalized());
            }
            if (gb.finalizado) {
                cancel();
                GestorBusquedaMonitor.this.setVisible(false);
            }
        }
    }

    private class SafeMonitorCreation implements Runnable {

        @Override
        public void run() {
            GestorBusquedaMonitor.this.init();
            pack();
            setVisible(true);
        }
    }
}
