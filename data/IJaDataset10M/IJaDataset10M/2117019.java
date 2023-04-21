package ArianneEditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.logging.Level;
import ArianneUtil.LogHandler;

public class TextDisplayRuleDialog extends JDialog {

    JPanel panel1 = new JPanel();

    BorderLayout borderLayout1 = new BorderLayout();

    JPanel mainPanel = new JPanel();

    JLabel textLabel = new JLabel();

    JButton okButton = new JButton();

    JButton annullaButton = new JButton();

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    Frame father;

    JTextField Precision = new JTextField();

    String formatter = null;

    boolean logEnabled = false;

    public TextDisplayRuleDialog(Frame frame, String title, boolean modal, boolean le) {
        super(frame, title, modal);
        logEnabled = le;
        father = frame;
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
            LogHandler.log(ex.getMessage(), Level.INFO, "LOG_MSG", isLoggingEnabled());
        }
    }

    public boolean isLoggingEnabled() {
        return logEnabled;
    }

    public void init(String form) {
        Precision.setText(form);
    }

    private void jbInit() throws Exception {
        panel1.setLayout(borderLayout1);
        mainPanel.setBorder(BorderFactory.createEtchedBorder());
        mainPanel.setLayout(gridBagLayout1);
        textLabel.setMaximumSize(new Dimension(57, 15));
        textLabel.setMinimumSize(new Dimension(57, 15));
        textLabel.setPreferredSize(new Dimension(57, 15));
        textLabel.setText("Precision:");
        Precision.setMaximumSize(new Dimension(95, 100));
        Precision.setMinimumSize(new Dimension(95, 20));
        Precision.setPreferredSize(new Dimension(95, 20));
        okButton.setText("OK");
        okButton.setMaximumSize(new Dimension(100, 20));
        okButton.setMinimumSize(new Dimension(100, 20));
        okButton.setPreferredSize(new Dimension(100, 20));
        annullaButton.setMaximumSize(new Dimension(100, 20));
        annullaButton.setMinimumSize(new Dimension(100, 20));
        annullaButton.setPreferredSize(new Dimension(100, 20));
        okButton.addActionListener(new TextDisplayRuleDialog_okButton_actionAdapter(this));
        annullaButton.setText("Annulla");
        annullaButton.addActionListener(new TextDisplayRuleDialog_annullaButton_actionAdapter(this));
        getContentPane().add(panel1);
        panel1.add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(textLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(Precision, new GridBagConstraints(1, 0, 2, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(okButton, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
        mainPanel.add(annullaButton, new GridBagConstraints(2, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
    }

    public String getFormatter() {
        return formatter;
    }

    void okButton_actionPerformed(ActionEvent e) {
        this.formatter = Precision.getText();
        this.dispose();
    }

    void annullaButton_actionPerformed(ActionEvent e) {
        this.dispose();
    }
}

class TextDisplayRuleDialog_okButton_actionAdapter implements java.awt.event.ActionListener {

    TextDisplayRuleDialog adaptee;

    TextDisplayRuleDialog_okButton_actionAdapter(TextDisplayRuleDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.okButton_actionPerformed(e);
    }
}

class TextDisplayRuleDialog_annullaButton_actionAdapter implements java.awt.event.ActionListener {

    TextDisplayRuleDialog adaptee;

    TextDisplayRuleDialog_annullaButton_actionAdapter(TextDisplayRuleDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.annullaButton_actionPerformed(e);
    }
}
