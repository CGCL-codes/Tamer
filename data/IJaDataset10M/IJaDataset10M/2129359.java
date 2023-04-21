package com.funambol.syncclient.google.panels;

import java.util.Hashtable;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.border.*;
import javax.swing.SwingConstants;
import com.funambol.syncclient.google.utils.Constants;
import com.funambol.syncclient.google.utils.Language;
import com.funambol.syncclient.google.MainWindow;
import javax.swing.JTextField;

/**
 * The Synchronization settings window.
 *
 *  @authorPaulo Fernandes
 */
public class RecoverPanel extends JDialog implements ActionListener, Constants {

    private Hashtable htFields = new Hashtable();

    private MainWindow mainWindow;

    private boolean modal = false;

    private JButton btRemoteSet;

    private JCheckBox cbContacts;

    private JCheckBox cbCalendars;

    private JPanel jpOver;

    private JPanel jpUp;

    private JPanel jpInterval;

    private JPanel jpDown;

    private JButton btOk;

    private JButton btCancel;

    private JLabel jLabel2;

    private JRadioButton server2Client;

    private JRadioButton client2Server;

    private Hashtable htContactValues = null;

    private Hashtable htSyncmlValues = null;

    /** Creates new form JDialog */
    public RecoverPanel(MainWindow mainWindow, boolean modal) {
        super(mainWindow, modal);
        this.mainWindow = mainWindow;
        this.modal = modal;
        htContactValues = mainWindow.getXmlContactValues();
        htSyncmlValues = mainWindow.getSyncmlValues();
        initComponents();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        int xcb = 10;
        int wcb = 80;
        int hcb = 23;
        int ybt = 235;
        int wbt = 88;
        int hbt = 24;
        int xlab = 18;
        int wlab = 200;
        int hlab = 21;
        int xtext = 120;
        int wtext = 50;
        int htext = 21;
        int y = 26;
        int dy = 24;
        jpOver = new JPanel();
        jpInterval = new JPanel();
        cbContacts = new JCheckBox();
        cbCalendars = new JCheckBox();
        btRemoteSet = new JButton();
        jpUp = new JPanel();
        jpDown = new JPanel();
        btOk = new JButton();
        btCancel = new JButton();
        jLabel2 = new JLabel();
        server2Client = new JRadioButton();
        client2Server = new JRadioButton();
        getContentPane().setLayout(null);
        setTitle(Language.getMessage(Language.LABEL_GOOGLE_RECOVER));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(310, 270);
        setLocationRelativeTo(null);
        setFont(font);
        setName("syncSetDialog");
        setResizable(false);
        jpOver.setLayout(null);
        jpOver.setBackground(Color.WHITE);
        jpOver.setForeground(Color.WHITE);
        jpOver.setBounds(0, 0, 310, 270);
        getContentPane().add(jpOver);
        jpUp.setLayout(null);
        jpUp.setBounds(8, 16, 290, 90);
        jpUp.setBackground(Color.WHITE);
        jpUp.setBorder(new TitledBorder(new LineBorder(new Color(204, 204, 204), 1, true), Language.getMessage(Language.LABEL_TITLE_REPLACE), 1, 0, font, new Color(0, 51, 204)));
        jpUp.setName("syncSet");
        jpInterval.setLayout(null);
        jpInterval.setBounds(8, 120, 290, 90);
        jpInterval.setBackground(Color.WHITE);
        jpInterval.setBorder(new TitledBorder(new LineBorder(new Color(204, 204, 204), 1, true), Language.getMessage(Language.LABEL_SYNC_INTERVAL), 1, 0, font, new Color(0, 51, 204)));
        jpInterval.setName("commSet");
        server2Client.setSelected(true);
        server2Client.setText(Language.getMessage(Language.LABEL_GOOGLE_RECOVER_S2C));
        server2Client.setFont(font);
        server2Client.setBackground(Color.WHITE);
        server2Client.setBounds(20, 20, 250, 20);
        jpUp.add(server2Client);
        client2Server.setSelected(false);
        client2Server.setText(Language.getMessage(Language.LABEL_GOOGLE_RECOVER_C2S));
        client2Server.setFont(font);
        client2Server.setBackground(Color.WHITE);
        client2Server.setBounds(20, 60, 250, 20);
        ButtonGroup group = new ButtonGroup();
        group.add(server2Client);
        group.add(client2Server);
        jpUp.add(server2Client);
        jpUp.add(client2Server);
        cbContacts.setText(Language.getMessage(Language.LABEL_SYNCSET_CONTACTS));
        cbContacts.setName("cbContacts");
        cbContacts.setFont(font);
        cbContacts.setBackground(Color.WHITE);
        cbContacts.setBounds(20, 22, wcb, hcb);
        cbContacts.setSelected(true);
        cbContacts.setVerticalAlignment(SwingConstants.CENTER);
        cbContacts.requestFocusInWindow();
        jpDown.add(cbContacts);
        cbCalendars.setText(Language.getMessage(Language.LABEL_SYNCSET_CALENDARS));
        cbCalendars.setName("cbCalendars");
        cbCalendars.setFont(font);
        cbCalendars.setBackground(Color.WHITE);
        cbCalendars.setBounds(120, 22, wcb, hcb);
        cbCalendars.setSelected(true);
        cbCalendars.setVerticalAlignment(SwingConstants.CENTER);
        cbCalendars.setEnabled(false);
        jpDown.add(cbCalendars);
        jpOver.add(jpUp);
        jpDown.setLayout(null);
        jpDown.setBackground(Color.WHITE);
        jpDown.setBounds(8, 110, 290, 80);
        jpDown.setBorder(new TitledBorder(new LineBorder(new Color(204, 204, 204), 1, true), Language.getMessage(Language.LABEL_SYNCSET_OTHER), 1, 0, font, new Color(0, 51, 204)));
        jpOver.add(jpDown);
        btOk.setText(Language.getMessage(Language.BT_OK));
        btOk.setActionCommand("OK");
        btOk.setName("btOk");
        btOk.setFont(font);
        btOk.setBounds(112, 200, wbt, hbt);
        btOk.addActionListener(this);
        jpOver.add(btOk);
        btCancel.setText(Language.getMessage(Language.BT_CANCEL));
        btCancel.setActionCommand("CANCEL");
        btCancel.setName("btCancel");
        btCancel.setBounds(210, 200, wbt, hbt);
        btCancel.setFont(font);
        btCancel.addActionListener(this);
        jpOver.add(btCancel);
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("OK")) {
            if (cbContacts.isSelected()) {
                if (client2Server.isSelected()) {
                    htContactValues.put(PARAM_SYNCMODE, Constants.REFRESH_FROM_CLIENT);
                } else if (server2Client.isSelected()) {
                    htContactValues.put(PARAM_SYNCMODE, Constants.REFRESH_FROM_SERVER);
                }
            }
            removeAll();
            setVisible(false);
            mainWindow.writeSyncMethod(htContactValues);
        } else if (evt.getActionCommand().equals("CANCEL")) {
            removeAll();
            setVisible(false);
        }
    }
}
