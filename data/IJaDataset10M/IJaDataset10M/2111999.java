package fr.soleil.bensikin.models;

import javax.swing.JOptionPane;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.tools.Messages;

public class ImageWriteValueTableModel extends ImageReadValueTableModel {

    protected boolean canSet;

    public ImageWriteValueTableModel(Object value, int data_type) {
        super(value, data_type);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            if (isValidValue(aValue.toString())) {
                if (value != null) {
                    switch(data_type) {
                        case TangoConst.Tango_DEV_UCHAR:
                        case TangoConst.Tango_DEV_CHAR:
                            canSet = true;
                            ((Byte[][]) value)[rowIndex][columnIndex] = new Byte(Double.valueOf(aValue.toString()).byteValue());
                            break;
                        case TangoConst.Tango_DEV_STATE:
                        case TangoConst.Tango_DEV_ULONG:
                        case TangoConst.Tango_DEV_LONG:
                            canSet = true;
                            ((Integer[][]) value)[rowIndex][columnIndex] = new Integer(Double.valueOf(aValue.toString()).intValue());
                            break;
                        case TangoConst.Tango_DEV_USHORT:
                        case TangoConst.Tango_DEV_SHORT:
                            canSet = true;
                            ((Short[][]) value)[rowIndex][columnIndex] = new Short(Double.valueOf(aValue.toString()).shortValue());
                            break;
                        case TangoConst.Tango_DEV_FLOAT:
                            canSet = true;
                            ((Float[][]) value)[rowIndex][columnIndex] = Float.valueOf(aValue.toString());
                            break;
                        case TangoConst.Tango_DEV_DOUBLE:
                            canSet = true;
                            ((Double[][]) value)[rowIndex][columnIndex] = Double.valueOf(aValue.toString());
                            break;
                        case TangoConst.Tango_DEV_STRING:
                            canSet = true;
                            ((String[][]) value)[rowIndex][columnIndex] = aValue.toString();
                            break;
                        case TangoConst.Tango_DEV_BOOLEAN:
                            canSet = true;
                            ((Boolean[][]) value)[rowIndex][columnIndex] = new Boolean("true".equalsIgnoreCase(aValue.toString()));
                            break;
                    }
                    super.fireTableDataChanged();
                }
            } else {
                String title = Messages.getMessage("MODIFY_SNAPSHOT_INVALID_VALUE_TITLE");
                String msg = Messages.getMessage("MODIFY_SNAPSHOT_INVALID_VALUE_MESSAGE");
                JOptionPane.showMessageDialog(BensikinFrame.getInstance(), msg, title, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    private boolean isValidValue(String val) {
        switch(data_type) {
            case TangoConst.Tango_DEV_STRING:
                return true;
            case TangoConst.Tango_DEV_BOOLEAN:
                if ("true".equalsIgnoreCase(val.trim()) || "false".equalsIgnoreCase(val.trim())) {
                    return true;
                }
                return false;
            case TangoConst.Tango_DEV_UCHAR:
            case TangoConst.Tango_DEV_CHAR:
                try {
                    Byte.parseByte(val);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            case TangoConst.Tango_DEV_STATE:
            case TangoConst.Tango_DEV_ULONG:
            case TangoConst.Tango_DEV_LONG:
                try {
                    Integer.parseInt(val);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            case TangoConst.Tango_DEV_USHORT:
            case TangoConst.Tango_DEV_SHORT:
                try {
                    Short.parseShort(val);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            case TangoConst.Tango_DEV_FLOAT:
                try {
                    Float.parseFloat(val);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            case TangoConst.Tango_DEV_DOUBLE:
                try {
                    Double.parseDouble(val);
                    return true;
                } catch (Exception e) {
                    return false;
                }
        }
        return false;
    }

    /**
	 * Sets all the elements of the table to a specific value
	 * 
	 * @param value
	 *            the value
	 * @return <code>true</code> if the value is valid and every element is set
	 *         to this value, <code>false></code> otherwise
	 */
    public boolean setAll(String value) {
        if (isValidValue(value)) {
            for (int i = 0; i < this.getRowCount(); i++) {
                for (int j = 0; j < this.getRowCount(); j++) {
                    this.setValueAt(value, i, j);
                }
            }
            fireTableDataChanged();
        } else {
            String title = Messages.getMessage("MODIFY_SNAPSHOT_INVALID_VALUE_TITLE");
            String msg = Messages.getMessage("MODIFY_SNAPSHOT_INVALID_VALUE_MESSAGE");
            JOptionPane.showMessageDialog(BensikinFrame.getInstance(), msg, title, JOptionPane.ERROR_MESSAGE);
            canSet = false;
        }
        return canSet;
    }

    public boolean canSet() {
        return canSet;
    }
}
