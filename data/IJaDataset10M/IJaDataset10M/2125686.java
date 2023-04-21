package tufts.vue;

import tufts.vue.gui.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;

public class ProviderListCellRenderer extends DefaultListCellRenderer {

    private edu.tufts.vue.dsm.OsidFactory factory = null;

    private final Icon myComputerIcon = VueResources.getImageIcon("dataSourceMyComputer");

    private final Icon savedResourcesIcon = VueResources.getImageIcon("dataSourceSavedResources");

    private final Icon remoteIcon = VueResources.getImageIcon("dataSourceRemote");

    private final Icon checkedIcon = VueResources.getImageIcon("addLibrary.checkMarkIcon");

    private final ImageIcon waitIcon = VueResources.getImageIcon("waitIcon");

    private final Icon rssIcon = VueResources.getImageIcon("dataSourceRSS");

    private static String LOADING = VueResources.getString("addLibrary.loading.label");

    private static String MY_COMPUTER = VueResources.getString("addLibrary.mycomputer.label");

    private static String MY_SAVED_CONTENT = "My Saved Content";

    private JPanel mRow = new JPanel();

    private JPanel mLabelPanel = new JPanel();

    private JLabel mLabel = new DefaultListCellRenderer();

    private JLabel mIconLabel = new JLabel();

    private JLabel waitLabel;

    private JLabel checkedLabel;

    private Component blankArea;

    private Border DividerBorder = new MatteBorder(1, 0, 0, 0, Color.gray);

    private Border EmptyDividerBorder = new EmptyBorder(1, 0, 0, 0);

    private Color AlternateRowColor = VueResources.getColor("gui.dataSourceList.alternateRowColor", 237, 243, 253);

    private Vector checklist = new Vector(50);

    private int waitingMode = -1;

    public ProviderListCellRenderer() {
        try {
            factory = edu.tufts.vue.dsm.impl.VueOsidFactory.getInstance();
        } catch (Throwable t) {
        }
        checkedLabel = new JLabel();
        checkedLabel.setIcon(checkedIcon);
        mRow.setLayout(new BoxLayout(mRow, BoxLayout.X_AXIS));
        mRow.setOpaque(true);
        mLabel.setMinimumSize(new Dimension(5, mLabel.getHeight()));
        mLabel.setPreferredSize(new Dimension(5, mLabel.getHeight()));
        mLabelPanel.setLayout(new BorderLayout());
        mLabelPanel.add(mLabel, BorderLayout.CENTER);
        waitLabel = new JLabel();
        waitLabel.setDoubleBuffered(true);
        waitLabel.setIcon(waitIcon);
        blankArea = Box.createRigidArea(new Dimension(16, 16));
        mRow.add(Box.createHorizontalStrut(GUI.WidgetInsets.left));
        mRow.add(blankArea);
        mRow.add(Box.createHorizontalStrut(GUI.WidgetInsets.right));
        mRow.add(mLabelPanel);
        mRow.add(Box.createHorizontalStrut(GUI.WidgetInsets.left));
        mRow.add(mIconLabel);
        mRow.add(Box.createHorizontalStrut(GUI.WidgetInsets.left));
    }

    public void setChecked(int index) {
        checklist.set(index, new Boolean(true));
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean cellHasFocus) {
        Color bg;
        if (selected) {
            bg = GUI.getTextHighlightColor();
        } else {
            if (index % 2 == 0) bg = list.getBackground(); else bg = AlternateRowColor;
        }
        if (checklist.size() == 0 || checklist.size() <= index) checklist.add(index, new Boolean(false));
        if (((Boolean) checklist.get(index)).booleanValue() && mRow.getComponent(1) != waitLabel) {
            mRow.remove(1);
            mRow.add(checkedLabel, 1);
        }
        if (!((Boolean) checklist.get(index)).booleanValue() && mRow.getComponent(1) != waitLabel) {
            mRow.remove(1);
            mRow.add(blankArea, 1);
        }
        mRow.setBackground(bg);
        waitLabel.setBackground(bg);
        mLabelPanel.setBackground(bg);
        mLabel.setBackground(bg);
        mIconLabel.setBackground(bg);
        String displayName = null;
        if (value instanceof org.osid.provider.Provider) {
            org.osid.provider.Provider provider = (org.osid.provider.Provider) value;
            displayName = provider.getDisplayName();
            try {
                org.osid.shared.PropertiesIterator propertiesIterator = provider.getProperties();
                while (propertiesIterator.hasNextProperties()) {
                    org.osid.shared.Properties props = propertiesIterator.nextProperties();
                    org.osid.shared.ObjectIterator objectIterator = props.getKeys();
                    while (objectIterator.hasNextObject()) {
                        String key = (String) objectIterator.nextObject();
                        try {
                            if (key.equals("icon16x16")) {
                                String path = factory.getResourcePath((String) props.getProperty(key));
                                mIconLabel.setIcon(new javax.swing.ImageIcon(path));
                                break;
                            }
                        } catch (Throwable t) {
                            mIconLabel.setIcon(remoteIcon);
                            break;
                        }
                    }
                }
            } catch (Throwable t) {
            }
            mRow.setBorder(EmptyDividerBorder);
        } else if (value instanceof String) {
            String s = (String) value;
            mRow.setBorder(DividerBorder);
            displayName = s;
            if (s.equals(MY_COMPUTER)) {
                mIconLabel.setIcon(myComputerIcon);
            } else if (s.equals(MY_SAVED_CONTENT)) {
                mIconLabel.setIcon(savedResourcesIcon);
            } else if (s.startsWith("FTP")) {
                mIconLabel.setIcon(remoteIcon);
            } else if (s.startsWith("RSS")) {
                mIconLabel.setIcon(rssIcon);
            } else if (s.equals(LOADING)) {
                displayName = LOADING;
                mIconLabel.setIcon(null);
            } else {
                mIconLabel.setIcon(null);
            }
        } else {
            mRow.setBorder(DividerBorder);
        }
        if (selected) mLabel.setForeground(Color.black); else mLabel.setForeground(Color.black);
        mLabel.setText(displayName);
        return mRow;
    }

    public void invokeWaitingMode(int index) {
        waitingMode = index;
    }

    public void endWaitingMode() {
        waitingMode = -1;
    }

    public void clearAllChecked() {
        for (int i = 0; i < checklist.size(); i++) checklist.set(i, new Boolean(false));
    }
}
