package eln.client.nbentry;

import java.awt.*;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.*;
import eln.util.*;
import java.awt.event.*;
import eln.client.event.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class NameDescriptionControllerPanel extends JPanel implements ClearRequestListener {

    GridBagLayout MainNameDispGridBagLayout = new GridBagLayout();

    JLabel mNameLabel = new JLabel();

    JLabel mDescLabel = new JLabel();

    JTextField mNameTextEntry = new JTextField(30);

    JTextField mDescTextEntry = new JTextField(30);

    StatusChangeListener mStatusListener;

    String langCode;

    String encoding = "UTF-8";

    public NameDescriptionControllerPanel() {
        this("");
    }

    public NameDescriptionControllerPanel(String lang) {
        langCode = lang;
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        Locale currentLocale = Locale.getDefault();
        if ((langCode != null) && (langCode.length() > 0)) currentLocale = new Locale(langCode);
        ResourceBundle labels = ResourceBundle.getBundle("LabelsBundle", currentLocale);
        String font = labels.getString("font");
        String nameL = labels.getString("name");
        String descL = labels.getString("description");
        String encod = labels.getString("encoding");
        if (encod != null && encod.length() > 0) encoding = encod;
        if ((font == null) || (font.length() == 0)) font = "Dialog";
        int fontSize = 12;
        try {
            String fontSizeString = labels.getString("fontsize");
            fontSize = new Integer(fontSizeString).intValue();
        } catch (Exception e) {
            fontSize = 12;
        }
        Font ndFont = new Font(font, Font.PLAIN, fontSize);
        Font labelFont = new Font(font, Font.BOLD, fontSize);
        mNameLabel.setText(nameL);
        mNameLabel.setToolTipText("Enter the name of the new item.");
        mNameLabel.setFont(labelFont);
        mNameTextEntry.setText("");
        mNameTextEntry.setBackground(Color.white);
        mNameTextEntry.addKeyListener(new NameDescriptionControllerPanel_NameTextField_keyAdapter(this));
        mNameTextEntry.setFont(ndFont);
        mDescLabel.setText(descL);
        mDescLabel.setToolTipText("Enter the description of the new item.");
        mDescLabel.setFont(labelFont);
        mDescTextEntry.setText("");
        mDescTextEntry.setBackground(Color.white);
        mDescTextEntry.setFont(ndFont);
        this.setLayout(MainNameDispGridBagLayout);
        this.add(mNameLabel, new GridBagConstraintsEMSL(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(mNameTextEntry, new GridBagConstraintsEMSL(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        this.add(mDescLabel, new GridBagConstraintsEMSL(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(mDescTextEntry, new GridBagConstraintsEMSL(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    }

    public boolean requiredEntrysAreFull() {
        return (mNameTextEntry.getText().length() > 0);
    }

    public String getName() {
        String text = mNameTextEntry.getText();
        try {
            text = new String(text.getBytes(encoding));
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return text;
    }

    public String getDescription() {
        String text = mDescTextEntry.getText();
        try {
            text = new String(text.getBytes(encoding));
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return text;
    }

    public void setName(String name) {
        mNameTextEntry.setText(name);
    }

    public void setDescription(String desc) {
        mDescTextEntry.setText(desc);
    }

    public void clearPerformed(ClearRequestEvent evt) {
        mNameTextEntry.setText("");
        mDescTextEntry.setText("");
    }

    public synchronized void addStatusListener(StatusChangeListener sCL) {
        mStatusListener = sCL;
    }

    public synchronized void removeStatusListener(StatusChangeListener sCL) {
        mStatusListener = null;
    }

    void NameTextField_keyTyped(KeyEvent e) {
        consumeBadChars(e);
    }

    /**
  *  Trap any of the "bad" characters that we don't want
  *  in the usernames or passwords
  */
    private void consumeBadChars(KeyEvent evt) {
        char fromKeyboard = evt.getKeyChar();
        if ((fromKeyboard == ',') || (fromKeyboard == '*') || (fromKeyboard == ';') || (fromKeyboard == '`') || (fromKeyboard == '/') || (fromKeyboard == '\\') || (fromKeyboard == '"') || (fromKeyboard == '^') || (fromKeyboard == '~')) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }
    }
}

class NameDescriptionControllerPanel_NameTextField_keyAdapter extends java.awt.event.KeyAdapter {

    NameDescriptionControllerPanel adaptee;

    NameDescriptionControllerPanel_NameTextField_keyAdapter(NameDescriptionControllerPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void keyTyped(KeyEvent e) {
        adaptee.NameTextField_keyTyped(e);
    }
}
