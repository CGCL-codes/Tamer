package com.hifiremote.jp1;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * The Class LearnedSignalTableModel.
 */
public class LearnedSignalTableModel extends JP1TableModel<LearnedSignal> {

    /**
   * Instantiates a new learned signal table model.
   */
    public LearnedSignalTableModel() {
        deviceEditor.setClickCountToStart(RMConstants.ClickCountToStart);
    }

    /**
   * Sets the.
   * 
   * @param remoteConfig
   *          the remote config
   */
    public void set(RemoteConfiguration remoteConfig) {
        this.remoteConfig = remoteConfig;
        if (remoteConfig != null) {
            colorEditor = new RMColorEditor(remoteConfig.getOwner());
            deviceComboBox.setModel(new DefaultComboBoxModel(remoteConfig.getRemote().getDeviceButtons()));
            keyRenderer.setRemote(remoteConfig.getRemote());
            keyEditor.setRemote(remoteConfig.getRemote());
            setData(remoteConfig.getLearnedSignals());
        }
    }

    /** The Constant colNames. */
    private static final String[] colNames = { "#", "<html>Device<br>Button</html>", "Key", "Notes", "Size", "Freq.", "Protocol", "Device", "<html>Sub<br>Device</html>", "OBC", "Hex Cmd", "Misc", "<html>Size &amp<br>Color</html>" };

    public int getColumnCount() {
        int count = colNames.length - 1;
        if (remoteConfig != null && remoteConfig.allowHighlighting()) {
            ++count;
        }
        return count;
    }

    @Override
    public String getColumnName(int col) {
        return colNames[col];
    }

    /** The Constant colPrototypeNames. */
    private static final String[] colPrototypeNames = { " 00 ", "__VCR/DVD__", "_xshift-VCR/DVD_", "A longish comment or note", "1024", "99999", "Protocol", "Device", "Device", "OBC", "Hex Cmd", "Miscellaneous", "Color_" };

    @Override
    public String getColumnPrototypeName(int col) {
        return colPrototypeNames[col];
    }

    @Override
    public boolean isColumnWidthFixed(int col) {
        if (col == 3 || col == 6 || col == 11) {
            return false;
        } else {
            return true;
        }
    }

    /** The Constant colClasses. */
    private static final Class<?>[] colClasses = { Integer.class, DeviceButton.class, Integer.class, String.class, Integer.class, Integer.class, String.class, Integer.class, Integer.class, Integer.class, String.class, String.class, Color.class };

    @Override
    public Class<?> getColumnClass(int col) {
        return colClasses[col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return (col > 0 && col < 4) || col == 12;
    }

    public Object getValueAt(int row, int column) {
        LearnedSignal l = getRow(row);
        UnpackLearned ul = l.getUnpackLearned();
        ArrayList<LearnedSignalDecode> da = l.getDecodes();
        int numDecodes = 0;
        int decodeIndex = 0;
        LearnedSignalDecode decode = null;
        if (da != null) {
            numDecodes = da.size();
            if (numDecodes > 1) {
                for (int i = 0; i < da.size(); ++i) {
                    decode = da.get(i);
                    if (decode.ignore) {
                        numDecodes--;
                    } else {
                        decodeIndex = i;
                    }
                }
            }
        }
        if (numDecodes != 1 && (column > 6 && column < 12 && (column != 11 || ul.error.isEmpty()))) {
            return null;
        }
        if (numDecodes == 1 && column > 5 && column < 12) {
            decode = da.get(decodeIndex);
        }
        switch(column) {
            case 0:
                return new Integer(row + 1);
            case 1:
                return remoteConfig.getRemote().getDeviceButton(l.getDeviceButtonIndex());
            case 2:
                return new Integer(l.getKeyCode());
            case 3:
                return l.getNotes();
            case 4:
                return l.getData().length();
            case 5:
                return new Integer(ul.frequency);
            case 6:
                if (numDecodes == 0) {
                    return "** None **";
                }
                if (numDecodes > 1) {
                    return "** Multiple **";
                }
                return decode.protocolName;
            case 7:
                return new Integer(decode.device);
            case 8:
                if (decode.subDevice == -1) {
                    return null;
                }
                return new Integer(decode.subDevice);
            case 9:
                return new Integer(decode.obc);
            case 10:
                return Hex.toString(decode.hex);
            case 11:
                String message = ul.error.isEmpty() ? "" : "Malformed signal: " + ul.error;
                if (numDecodes == 1) {
                    message += ul.error.isEmpty() ? "" : "; ";
                    message += decode.miscMessage;
                }
                return message;
            case 12:
                return l.getHighlight();
        }
        return null;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        LearnedSignal l = getRow(row);
        switch(col) {
            case 1:
                {
                    l.setDeviceButtonIndex(((DeviceButton) value).getButtonIndex());
                    break;
                }
            case 2:
                l.setKeyCode(((Integer) value).shortValue());
                break;
            case 3:
                l.setNotes((String) value);
                break;
            case 12:
                l.setHighlight((Color) value);
                break;
        }
        propertyChangeSupport.firePropertyChange(col == 12 ? "highlight" : "data", null, null);
    }

    @Override
    public TableCellRenderer getColumnRenderer(int col) {
        if (col == 0) {
            return new RowNumberRenderer();
        } else if (col == 2) {
            return keyRenderer;
        } else if (col == 12) {
            return colorRenderer;
        }
        return null;
    }

    @Override
    public TableCellEditor getColumnEditor(int col) {
        if (col == 1) {
            DefaultCellEditor e = new DefaultCellEditor(deviceComboBox);
            e.setClickCountToStart(RMConstants.ClickCountToStart);
            return e;
        } else if (col == 2) {
            return keyEditor;
        } else if (col == 3) {
            return noteEditor;
        } else if (col == 12) {
            return colorEditor;
        }
        return null;
    }

    /** The remote config. */
    private RemoteConfiguration remoteConfig = null;

    /** The device combo box. */
    private JComboBox deviceComboBox = new JComboBox();

    private DefaultCellEditor deviceEditor = new DefaultCellEditor(deviceComboBox);

    /** The key renderer. */
    private KeyCodeRenderer keyRenderer = new KeyCodeRenderer();

    /** The key editor. */
    private KeyEditor keyEditor = new KeyEditor();

    private SelectAllCellEditor noteEditor = new SelectAllCellEditor();

    private RMColorEditor colorEditor = null;

    private RMColorRenderer colorRenderer = new RMColorRenderer();
}
