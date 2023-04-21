package visitpc.mtightvnc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OptionsPanel extends JPanel implements ItemListener {

    public static final String[] ParameterNames = { "Encoding", "Compression level", "JPEG image quality", "Cursor shape updates", "Use CopyRect", "Restricted colors", "Mouse buttons 2 and 3", "View only", "Scale remote cursor", "Share desktop" };

    public static final String[][] ParameterValues = { { "Auto", "Raw", "RRE", "CoRRE", "Hextile", "Zlib", "Tight", "ZRLE" }, { "Default", "1", "2", "3", "4", "5", "6", "7", "8", "9" }, { "JPEG off", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" }, { "Enable", "Ignore", "Disable" }, { "Yes", "No" }, { "Yes", "No" }, { "Normal", "Reversed" }, { "Yes", "No" }, { "No", "50%", "75%", "125%", "150%" }, { "Yes", "No" } };

    final int encodingIndex = 0, compressLevelIndex = 1, jpegQualityIndex = 2, cursorUpdatesIndex = 3, useCopyRectIndex = 4, eightBitColorsIndex = 5, mouseButtonIndex = 6, viewOnlyIndex = 7, scaleCursorIndex = 8, shareDesktopIndex = 9;

    JLabel[] labels = new JLabel[ParameterNames.length];

    JComboBox[] choices = new JComboBox[ParameterNames.length];

    VncViewer viewer;

    public static int DefaultOptions[] = { 0, 0, 7, 0, 0, 1, 0, 1, 0, 1 };

    int preferredEncoding;

    int compressLevel;

    int jpegQuality;

    boolean useCopyRect;

    boolean requestCursorUpdates;

    boolean ignoreCursorUpdates;

    boolean eightBitColors;

    boolean reverseMouseButtons2And3;

    boolean shareDesktop;

    boolean viewOnly;

    int scaleCursor;

    boolean autoScale;

    int scalingFactor;

    GridBagLayout gridbag;

    GridBagConstraints gbc;

    JLabel passwordLabel = new JLabel("Password");

    JPasswordField passwordField = new JPasswordField();

    public OptionsPanel(VncViewer v) {
        viewer = v;
        gridbag = new GridBagLayout();
        setLayout(gridbag);
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gridbag.setConstraints(passwordLabel, gbc);
        add(passwordLabel);
        passwordField.setText(v.readParameter("PASSWORD", false));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(passwordField, gbc);
        add(passwordField);
        for (int i = 0; i < ParameterNames.length; i++) {
            labels[i] = new JLabel(ParameterNames[i]);
            gbc.gridwidth = 1;
            gridbag.setConstraints(labels[i], gbc);
            add(labels[i]);
            choices[i] = new JComboBox();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gridbag.setConstraints(choices[i], gbc);
            add(choices[i]);
            for (int j = 0; j < ParameterValues[i].length; j++) {
                choices[i].addItem(ParameterValues[i][j]);
            }
        }
        for (int i = 0; i < ParameterNames.length; i++) {
            choices[i].addItemListener(this);
        }
        for (int i = 0; i < choices.length; i++) {
            choices[i].setSelectedIndex(OptionsPanel.DefaultOptions[i]);
        }
        for (int i = 0; i < ParameterNames.length; i++) {
            String s = viewer.readParameter(ParameterNames[i], false);
            if (s != null) {
                for (int j = 0; j < ParameterValues[i].length; j++) {
                    if (s.equalsIgnoreCase(ParameterValues[i][j])) {
                        choices[i].setSelectedIndex(j);
                    }
                }
            }
        }
        autoScale = false;
        scalingFactor = 100;
        String s = viewer.readParameter("Scaling Factor", false);
        if (s != null) {
            if (s.equalsIgnoreCase("Auto")) {
                autoScale = true;
            } else {
                if (s.charAt(s.length() - 1) == '%') {
                    s = s.substring(0, s.length() - 1);
                }
                try {
                    scalingFactor = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    scalingFactor = 100;
                }
                if (scalingFactor < 1) {
                    scalingFactor = 1;
                } else if (scalingFactor > 1000) {
                    scalingFactor = 1000;
                }
            }
        }
        setEncodings();
        setColorFormat();
        setOtherOptions();
    }

    public void hidePassword() {
        remove(passwordLabel);
        remove(passwordField);
        validate();
    }

    public void setPassword(String password) {
        passwordField.setText(password);
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    void disableShareDesktop() {
        labels[shareDesktopIndex].setEnabled(false);
        choices[shareDesktopIndex].setEnabled(false);
    }

    void setEncodings() {
        useCopyRect = choices[useCopyRectIndex].getSelectedItem().equals("Yes");
        preferredEncoding = RfbProto.EncodingRaw;
        boolean enableCompressLevel = false;
        if (choices[encodingIndex].getSelectedItem().equals("RRE")) {
            preferredEncoding = RfbProto.EncodingRRE;
        } else if (choices[encodingIndex].getSelectedItem().equals("CoRRE")) {
            preferredEncoding = RfbProto.EncodingCoRRE;
        } else if (choices[encodingIndex].getSelectedItem().equals("Hextile")) {
            preferredEncoding = RfbProto.EncodingHextile;
        } else if (choices[encodingIndex].getSelectedItem().equals("ZRLE")) {
            preferredEncoding = RfbProto.EncodingZRLE;
        } else if (choices[encodingIndex].getSelectedItem().equals("Zlib")) {
            preferredEncoding = RfbProto.EncodingZlib;
            enableCompressLevel = true;
        } else if (choices[encodingIndex].getSelectedItem().equals("Tight")) {
            preferredEncoding = RfbProto.EncodingTight;
            enableCompressLevel = true;
        } else if (choices[encodingIndex].getSelectedItem().equals("Auto")) {
            preferredEncoding = -1;
        }
        try {
            compressLevel = choices[compressLevelIndex].getSelectedIndex();
        } catch (NumberFormatException e) {
            compressLevel = -1;
        }
        if (compressLevel < 1 || compressLevel > 9) {
            compressLevel = -1;
        }
        labels[compressLevelIndex].setEnabled(enableCompressLevel);
        choices[compressLevelIndex].setEnabled(enableCompressLevel);
        try {
            jpegQuality = choices[jpegQualityIndex].getSelectedIndex();
        } catch (NumberFormatException e) {
            jpegQuality = -1;
        }
        if (jpegQuality < 0 || jpegQuality > 9) {
            jpegQuality = -1;
        }
        requestCursorUpdates = !choices[cursorUpdatesIndex].getSelectedItem().equals("Disable");
        if (requestCursorUpdates) {
            ignoreCursorUpdates = choices[cursorUpdatesIndex].getSelectedItem().equals("Ignore");
        }
        viewer.setEncodings();
    }

    void setColorFormat() {
        eightBitColors = choices[eightBitColorsIndex].getSelectedItem().equals("Yes");
        boolean enableJPEG = !eightBitColors;
        labels[jpegQualityIndex].setEnabled(enableJPEG);
        choices[jpegQualityIndex].setEnabled(enableJPEG);
    }

    void setOtherOptions() {
        reverseMouseButtons2And3 = choices[mouseButtonIndex].getSelectedItem().equals("Reversed");
        viewOnly = choices[viewOnlyIndex].getSelectedItem().equals("Yes");
        if (viewer.vc != null) viewer.vc.enableInput(!viewOnly);
        shareDesktop = choices[shareDesktopIndex].getSelectedItem().equals("Yes");
        switch(choices[scaleCursorIndex].getSelectedIndex()) {
            case 0:
                scaleCursor = 0;
                break;
            case 1:
                scaleCursor = 50;
                break;
            case 2:
                scaleCursor = 75;
                break;
            case 3:
                scaleCursor = 125;
                break;
            case 4:
                scaleCursor = 150;
                break;
        }
        if (scaleCursor < 10 || scaleCursor > 500) {
            scaleCursor = 0;
        }
        if (requestCursorUpdates && !ignoreCursorUpdates && !viewOnly) {
            labels[scaleCursorIndex].setEnabled(true);
            choices[scaleCursorIndex].setEnabled(true);
        } else {
            labels[scaleCursorIndex].setEnabled(false);
            choices[scaleCursorIndex].setEnabled(false);
        }
        if (viewer.vc != null) viewer.vc.createSoftCursor();
    }

    public void itemStateChanged(ItemEvent evt) {
        Object source = evt.getSource();
        if (source == choices[encodingIndex] || source == choices[compressLevelIndex] || source == choices[jpegQualityIndex] || source == choices[cursorUpdatesIndex] || source == choices[useCopyRectIndex]) {
            setEncodings();
            if (source == choices[cursorUpdatesIndex]) {
                setOtherOptions();
            }
        } else if (source == choices[eightBitColorsIndex]) {
            setColorFormat();
        } else if (source == choices[mouseButtonIndex] || source == choices[shareDesktopIndex] || source == choices[viewOnlyIndex] || source == choices[scaleCursorIndex]) {
            setOtherOptions();
        }
    }

    public void windowClosing(WindowEvent evt) {
        setVisible(false);
    }

    public void windowActivated(WindowEvent evt) {
    }

    public void windowDeactivated(WindowEvent evt) {
    }

    public void windowOpened(WindowEvent evt) {
    }

    public void windowClosed(WindowEvent evt) {
    }

    public void windowIconified(WindowEvent evt) {
    }

    public void windowDeiconified(WindowEvent evt) {
    }

    public String[] getSelectedOptions() {
        String selectedOptions[] = new String[OptionsPanel.ParameterNames.length];
        for (int i = 0; i < OptionsPanel.ParameterNames.length; i++) {
            int selectedIndex = choices[i].getSelectedIndex();
            selectedOptions[i] = OptionsPanel.ParameterValues[i][selectedIndex];
        }
        return selectedOptions;
    }
}
