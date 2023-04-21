package rvsn00p.viewer;

import rvsn00p.util.rv.RvParameters;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * RvSnooperInputDialog
 *
 * Creates a popup input dialog box so that users can enter
 * a URL to open a log file from.
 *
 * @author �rjan Lundberg <lundberg@home.se>
 */
public class RvSnooperRvTransportInputDialog extends RvSnooperDialog {

    private JTextField _tService = new JTextField();

    private JTextField _tNetwork = new JTextField();

    private JTextField _tDaemon = new JTextField();

    private JTextField _tSubject = new JTextField();

    private boolean isOK;

    /**
     * Retrieves tibco transport information from the user.
     * param jframe the frame where the dialog will be loaded from.
     * param title the title of the dialog box.
     */
    public RvSnooperRvTransportInputDialog(JFrame jframe, String title, RvParameters defaultParameters) {
        super(jframe, title, true);
        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());
        JPanel main = new JPanel();
        main.setLayout(new GridLayout(8, 0));
        createInputField("Service:", defaultParameters.getService(), _tService, main);
        createInputField("Daemon:", defaultParameters.getDaemon(), _tDaemon, main);
        createInputField("Subject:", defaultParameters.getSubject(), _tSubject, main);
        createInputField("Network:", defaultParameters.getNetwork(), _tNetwork, main);
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    hide();
                    setOK(true);
                }
            }
        });
        JButton ok = new JButton("Ok");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                hide();
                setOK(true);
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                hide();
                setOK(false);
            }
        });
        bottom.add(ok);
        bottom.add(cancel);
        getContentPane().add(main, BorderLayout.CENTER);
        getContentPane().add(bottom, BorderLayout.SOUTH);
        pack();
        centerWindow(this);
        show();
    }

    public rvsn00p.util.rv.RvParameters getRvParameters() {
        RvParameters p = new RvParameters();
        p.setDeamon(_tDaemon.getText());
        p.setNetwork(_tNetwork.getText());
        p.setService(_tService.getText());
        p.setSubject(_tSubject.getText());
        return p;
    }

    public boolean isOK() {
        return isOK;
    }

    protected void createInputField(String name, String defaultText, JTextField tf, JPanel addTo) {
        JLabel jl = new JLabel(name);
        addTo.add(jl);
        tf.setText(defaultText);
        addTo.add(tf);
        jl.setLabelFor(tf);
    }

    protected void setOK(boolean OK) {
        isOK = OK;
    }
}
