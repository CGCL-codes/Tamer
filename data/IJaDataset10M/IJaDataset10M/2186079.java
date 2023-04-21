package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.border.*;
import manager.IManager;
import manager.Manager;

/**
 * The Class CalibraOra.
 * 
 * @author Giulio Presazzi
 */
public class CalibraOra extends JFrame {

    private ActionListener listener;

    private String sessione;

    /** The Constant ESEGUI. */
    public static final String ESEGUI = "ESEGUI";

    /** The Constant MODIFICA. */
    public static final String MODIFICA = "MODIFICA";

    /**
	 * Instantiates a new calibra ora.
	 * 
	 * @param manager
	 *            the manager
	 * @param sessione
	 *            the sessione
	 */
    public CalibraOra(IManager manager, String sessione) {
        this.sessione = sessione;
        this.manager = manager;
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                calibrationActionPerformed();
            }
        };
        initComponents();
    }

    private void calibrationActionPerformed() {
        if (sessione.equalsIgnoreCase("ESEGUI")) {
            FaiParlare nf = new FaiParlare(manager);
            dispose();
        }
        if (sessione.equalsIgnoreCase("MODIFICA")) {
            dispose();
        }
    }

    private void annullaActionPerformed(ActionEvent actionEvent) {
        dispose();
    }

    private void connettiActionPerformed(ActionEvent actionEvent) {
        if (manager.wiiConnected() == false) {
            Object[] options = { "Ok", "Annulla" };
            int valu = JOptionPane.showOptionDialog(null, "Assicurarsi che il Bluetooth sia attivo", "CONNESSIONE BLUETOOTH", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (valu == 0) {
                manager.connect();
                timer = new Timer();
                count = 0;
                timer.schedule(new RemindTask(), 6000);
            }
        } else manager.calibra(listener);
    }

    private void initComponents() {
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        testoTextPane = new JTextPane();
        Alert = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        setVisible(true);
        setBackground(new Color(189, 204, 249));
        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setBackground(new Color(189, 204, 249));
            dialogPane.setLayout(new BorderLayout());
            {
                contentPanel.setBackground(new Color(189, 204, 249));
                contentPanel.setLayout(new BorderLayout());
                if (manager.wiiConnected() == false) testoTextPane.setText("Prima di poter modificare o usare questo cartellone è necessario impostare l'area di lavoro.\nSe si desidera impostare ora l'area di lavoro accendere il WiiMote e premere i bottoni 1 e 2, poi cliccare ok."); else testoTextPane.setText("Prima di poter modificare o usare questo cartellone è necessario impostare l'area di lavoro premere ok.");
                testoTextPane.setFont(new Font("Cambria", Font.PLAIN, 21));
                testoTextPane.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                testoTextPane.setBackground(new Color(189, 204, 249));
                testoTextPane.setEditable(false);
                contentPanel.add(testoTextPane, BorderLayout.CENTER);
                Alert.setText("Questo è un cartellone non è ancora stato calibrato!");
                Alert.setFont(new Font("Cambria", Font.BOLD, 21));
                contentPanel.add(Alert, BorderLayout.NORTH);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setBackground(new Color(189, 204, 249));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[] { 0, 85, 80 };
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[] { 1.0, 0.0, 0.0 };
                okButton.setText("OK");
                okButton.setFont(new Font("Cambria", Font.PLAIN, 24));
                okButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent actionEvent) {
                        connettiActionPerformed(actionEvent);
                    }
                });
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));
                cancelButton.setText("Cancel");
                cancelButton.setFont(new Font("Cambria", Font.PLAIN, 24));
                cancelButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent actionEvent) {
                        annullaActionPerformed(actionEvent);
                    }
                });
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
    }

    private JPanel dialogPane;

    private JPanel contentPanel;

    private JTextPane testoTextPane;

    private JLabel Alert;

    private JPanel buttonBar;

    private JButton okButton;

    private JButton cancelButton;

    private IManager manager;

    private Integer count = 0;

    private Timer timer;

    /**
	 * The Class RemindTask.
	 */
    class RemindTask extends TimerTask {

        public void run() {
            if (manager.wiiConnected()) {
                manager.calibra(listener);
            } else if (count < 10) {
                timer.schedule(new RemindTask(), 3000);
                count++;
            }
        }
    }
}
