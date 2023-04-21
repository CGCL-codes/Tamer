package wizard.com.panels;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ConnectionWizardPanel3a extends JPanel {

    private javax.swing.JLabel anotherBlankSpace = new javax.swing.JLabel();

    private javax.swing.JLabel yetAnotherBlankSpace1 = new javax.swing.JLabel();

    JTextArea northText = new javax.swing.JTextArea();

    JPanel jPanel1 = new javax.swing.JPanel();

    JLabel blankSpace = new javax.swing.JLabel();

    SpinnerNumberModel rpModel = new SpinnerNumberModel(1000, 0, 100000, 1);

    JSpinner refreshPeriod = new JSpinner(rpModel);

    private JPanel contentPanel = new JPanel();

    private JPanel titlePanel = new javax.swing.JPanel();

    JLabel textLabel = new javax.swing.JLabel();

    JLabel iconLabel = new javax.swing.JLabel();

    BorderLayout borderLayout1 = new java.awt.BorderLayout();

    BorderLayout borderLayout2 = new java.awt.BorderLayout();

    BorderLayout borderLayout3 = new java.awt.BorderLayout();

    Font serifFont = new Font("MS Sans Serif", Font.BOLD, 14);

    EmptyBorder emptyBorder1 = new EmptyBorder(new Insets(10, 10, 10, 10));

    EmptyBorder emptyBorder2 = new EmptyBorder(new Insets(10, 10, 10, 10));

    String welcomeText = "";

    JPanel jPanel2 = new JPanel();

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    JLabel rpLabel = new JLabel();

    JLabel iconLabel3 = new JLabel();

    BorderLayout borderLayout4 = new BorderLayout();

    JPanel centerPanel = new JPanel();

    GridBagLayout gridBagLayout2 = new GridBagLayout();

    public ConnectionWizardPanel3a() {
        super();
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addCheckBoxActionListener(ActionListener l) {
    }

    public int getRefreshValue() {
        return ((Integer) rpModel.getValue()).intValue();
    }

    private ImageIcon getImageIcon() {
        return null;
    }

    public void this_actionPerformed(ActionEvent e) {
    }

    private void jbInit() throws Exception {
        this.setMinimumSize(new Dimension(560, 348));
        this.setPreferredSize(new Dimension(560, 348));
        contentPanel.setLayout(borderLayout1);
        welcomeText = "Il reperimento dati avverr� in modalit� 'Polling', ovvero con interrogazione periodica diretta del DBMS. Immettere il periodo di interrogazione (in msec).";
        northText.setBackground(new Color(236, 233, 216));
        northText.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 11));
        northText.setMinimumSize(new Dimension(415, 80));
        northText.setPreferredSize(new Dimension(415, 80));
        northText.setText(welcomeText);
        northText.setLineWrap(true);
        northText.setWrapStyleWord(true);
        refreshPeriod.setMaximumSize(new Dimension(200, 200));
        refreshPeriod.setMinimumSize(new Dimension(95, 24));
        refreshPeriod.setPreferredSize(new Dimension(100, 24));
        refreshPeriod.setToolTipText("Periodo di refresh espresso in millisecondi");
        jPanel2.setLayout(gridBagLayout1);
        rpLabel.setMaximumSize(new Dimension(124, 24));
        rpLabel.setMinimumSize(new Dimension(124, 24));
        rpLabel.setPreferredSize(new Dimension(124, 2));
        rpLabel.setText("Periodo di refresh (msec):");
        iconLabel3.setIcon(new ImageIcon(ConnectionWizardPanel3a.class.getResource("clouds.jpg")));
        centerPanel.setLayout(borderLayout4);
        jPanel2.setMinimumSize(new Dimension(387, 34));
        jPanel2.setPreferredSize(new Dimension(392, 34));
        contentPanel.add(northText, java.awt.BorderLayout.NORTH);
        jPanel1.setLayout(gridBagLayout2);
        contentPanel.add(jPanel1, java.awt.BorderLayout.CENTER);
        contentPanel.setBorder(emptyBorder2);
        ImageIcon icon = getImageIcon();
        setLayout(borderLayout2);
        titlePanel.setLayout(borderLayout3);
        titlePanel.setBackground(Color.gray);
        textLabel.setBackground(Color.gray);
        textLabel.setFont(serifFont);
        textLabel.setText("Modalit� di Connessione");
        textLabel.setBorder(emptyBorder1);
        textLabel.setOpaque(true);
        iconLabel.setBackground(Color.gray);
        if (icon != null) iconLabel.setIcon(icon);
        titlePanel.add(textLabel, BorderLayout.CENTER);
        titlePanel.add(iconLabel, BorderLayout.EAST);
        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.WEST);
        jPanel2.add(refreshPeriod, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 0, 5, 158), 0, 0));
        centerPanel.add(iconLabel3, java.awt.BorderLayout.WEST);
        centerPanel.add(contentPanel, java.awt.BorderLayout.CENTER);
        jPanel1.add(blankSpace, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 415, 51));
        jPanel1.add(anotherBlankSpace, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 415, 51));
        jPanel1.add(yetAnotherBlankSpace1, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 0), 415, 51));
        jPanel1.add(jPanel2, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel2.add(rpLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        this.addCheckBoxActionListener(new ConnectionWizardPanel3a_this_checkBoxActionAdapter(this));
    }
}

class ConnectionWizardPanel3a_this_checkBoxActionAdapter implements ActionListener {

    private ConnectionWizardPanel3a adaptee;

    ConnectionWizardPanel3a_this_checkBoxActionAdapter(ConnectionWizardPanel3a adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.this_actionPerformed(e);
    }
}
