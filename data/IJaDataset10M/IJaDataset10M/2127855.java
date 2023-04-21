package realcix20.guis.utils;

import java.text.DecimalFormat;
import java.util.Calendar;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;
import realcix20.classes.basic.Column;
import realcix20.classes.plugins.CurrencyPlugin;
import realcix20.guis.components.DetailList;
import realcix20.guis.components.DetailText;
import realcix20.guis.components.DisplayOnlyText;
import realcix20.guis.components.JDatePicker;
import realcix20.guis.components.JMonthPicker;
import realcix20.guis.components.JTimeStamp;
import realcix20.guis.components.PasswordEdit;
import realcix20.guis.components.XrCalculator;
import realcix20.utils.GlobalValueManager;

public class ComponentManager {

    private ComponentManager() {
    }

    public static Object getValue(JComponent component) {
        Object value = null;
        if (component instanceof JComboBox) {
            JComboBox comboBox = (JComboBox) component;
            if ((component instanceof JDatePicker) || (component instanceof JTimeStamp)) {
                value = comboBox.getSelectedItem();
            } else if (component instanceof JMonthPicker) {
                value = ((JMonthPicker) comboBox).getSelectedMonth();
            } else {
                if (comboBox.getSelectedItem() instanceof Item) {
                    Item item = (Item) comboBox.getSelectedItem();
                    if (item != null) {
                        value = item.getFactValue();
                    } else value = null;
                } else {
                    if (comboBox.getSelectedItem() != null) value = comboBox.getSelectedItem().toString(); else value = null;
                }
            }
        } else if (component instanceof JTextField) {
            JTextField textField = (JTextField) component;
            if (component instanceof JFormattedTextField) {
                JFormattedTextField ftf = (JFormattedTextField) textField;
                if (ftf.getFormatter() instanceof NumberFormatter) {
                    NumberFormatter nf = (NumberFormatter) ftf.getFormatter();
                    try {
                        value = nf.stringToValue(ftf.getText());
                    } catch (Exception e) {
                    }
                } else {
                    value = ((JFormattedTextField) textField).getValue();
                }
            } else value = textField.getText().trim();
        } else if (component instanceof JTextArea) {
            JTextArea textArea = (JTextArea) component;
            value = textArea.getText().trim();
        } else if (component instanceof DisplayOnlyText) {
            DisplayOnlyText displayOnlyText = (DisplayOnlyText) component;
            value = displayOnlyText.getValue();
        } else if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            value = label.getText();
        } else if (component instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) component;
            value = new Boolean(checkBox.isSelected());
        } else if (component instanceof DetailText) {
            DetailText detailText = (DetailText) component;
            value = detailText.getValue();
        } else if (component instanceof DetailList) {
            DetailList detailList = (DetailList) component;
            value = detailList.getValue();
        } else if (component instanceof XrCalculator) {
            XrCalculator xrCalculator = (XrCalculator) component;
            value = xrCalculator.getValue();
        } else if (component instanceof PasswordEdit) {
            PasswordEdit passwordEdit = (PasswordEdit) component;
            value = passwordEdit.getValue();
        }
        return value;
    }

    public static JComponent getComponent(Column column) {
        JComponent component = null;
        switch(column.getInputType()) {
            case 1:
            case 2:
            case 7:
                JComboBox comboBox = new JComboBox();
                if (column.getInputType() == 2) comboBox.setEditable(true); else comboBox.setEditable(false);
                component = comboBox;
                break;
            case 3:
                JTextField textField = new JTextField();
                component = textField;
                break;
            case 31:
                DetailText detailText = new DetailText();
                component = detailText;
                break;
            case 81:
            case 87:
                DetailList detailList = new DetailList(column);
                detailList.setEditabled(false);
                component = detailList;
                break;
            case 83:
                XrCalculator xrCalculator = new XrCalculator(column);
                component = xrCalculator;
                break;
            case 82:
                detailList = new DetailList(column);
                detailList.setEditabled(true);
                component = detailList;
                break;
            case 4:
                PasswordEdit passwordEdit = new PasswordEdit(column);
                component = passwordEdit;
                break;
            case 5:
                JDatePicker datePicker = new JDatePicker();
                component = datePicker;
                break;
            case 6:
                JCheckBox checkBox = new JCheckBox();
                component = checkBox;
                break;
            case 9:
                JTimeStamp timeStamp = new JTimeStamp();
                component = timeStamp;
                break;
            case 10:
                JMonthPicker monthPicker = new JMonthPicker();
                component = monthPicker;
                break;
            case 11:
            case 12:
                CurrencyPlugin cp = CurrencyPlugin.getInstance();
                StringBuffer sb = new StringBuffer("###");
                sb.append(cp.getSepor() + "###");
                DecimalFormat df = new DecimalFormat(sb.toString());
                JFormattedTextField formattedTextField = new JFormattedTextField(df);
                component = formattedTextField;
                break;
            case 16:
                cp = CurrencyPlugin.getInstance();
                sb = new StringBuffer("###");
                sb.append(cp.getSepor() + "###");
                int fraction = Integer.parseInt(GlobalValueManager.getValue("APPLIATION.DEFFRACTION"));
                if (fraction > 0) {
                    sb.append(cp.getFpoint());
                }
                for (int i = 1; i <= fraction; i++) {
                    sb.append("0");
                }
                df = new DecimalFormat(sb.toString());
                formattedTextField = new JFormattedTextField(df);
                component = formattedTextField;
                break;
            case 21:
            case 22:
            case 23:
            case 24:
                JLabel displayLabel = new JLabel();
                component = displayLabel;
                break;
            case 101:
                DisplayOnlyText displayOnlyText = new DisplayOnlyText(column);
                component = displayOnlyText;
                break;
            default:
                JLabel label = new JLabel("Not Supported!");
                component = label;
                break;
        }
        return component;
    }

    public static void setValue(Object value, JComponent component) {
        if (component instanceof JComboBox) {
            if (component instanceof JMonthPicker) {
                if (value != null) {
                    int year = Integer.parseInt(value.toString().substring(0, 4));
                    int month = Integer.parseInt(value.toString().substring(4, 6));
                    StringBuffer sb = new StringBuffer(year + "");
                    if (month < 10) {
                        sb.append("0" + month);
                    } else {
                        sb.append(month);
                    }
                    ((JMonthPicker) component).setSelectedItem(sb.toString());
                }
            } else if (component instanceof JDatePicker) {
                if (value != null) {
                    if (value instanceof java.util.Date) {
                        java.util.Date date = (java.util.Date) value;
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH) + 1;
                        int day = cal.get(Calendar.DATE);
                        StringBuffer sb = new StringBuffer(year + "-");
                        if (month < 10) {
                            sb.append("0" + month + "-");
                        } else {
                            sb.append(month + "-");
                        }
                        if (day < 10) {
                            sb.append("0" + day);
                        } else {
                            sb.append(day);
                        }
                        ((JDatePicker) component).setSelectedItem(sb.toString());
                    } else {
                        ((JDatePicker) component).setSelectedItem(value);
                    }
                }
            } else if (component instanceof JTimeStamp) {
                if (value != null) {
                    java.util.Date date;
                    if (value instanceof java.util.Date) date = (java.util.Date) value; else date = new java.util.Date(value.toString());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH) + 1;
                    int day = cal.get(Calendar.DATE);
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int minute = cal.get(Calendar.MINUTE);
                    int second = cal.get(Calendar.SECOND);
                    StringBuffer sb = new StringBuffer(year + "-");
                    if (month < 10) {
                        sb.append("0" + month + "-");
                    } else {
                        sb.append(month + "-");
                    }
                    if (day < 10) {
                        sb.append("0" + day);
                    } else {
                        sb.append(day);
                    }
                    if (hour < 10) {
                        sb.append(" 0" + hour + ":");
                    } else {
                        sb.append(" " + hour + ":");
                    }
                    if (minute < 10) {
                        sb.append("0" + minute + ":");
                    } else {
                        sb.append(minute + ":");
                    }
                    if (second < 10) {
                        sb.append("0" + second);
                    } else {
                        sb.append(second);
                    }
                    ((JTimeStamp) component).setSelectedItem(sb.toString());
                }
            } else {
                if (value != null) {
                    for (int i = 0; i < ((JComboBox) component).getItemCount(); i++) {
                        Item item = (Item) ((JComboBox) component).getItemAt(i);
                        if (item.getFactValue() != null) {
                            if (item.getFactValue().equals(value)) {
                                ((JComboBox) component).setSelectedItem(item);
                                break;
                            }
                        }
                    }
                }
            }
        } else if (component instanceof JTextField) {
            if (component instanceof JPasswordField) ((JPasswordField) component).setText("realcix"); else if (component instanceof JFormattedTextField) {
                try {
                    ((JFormattedTextField) component).setValue(value);
                } catch (Exception e) {
                }
            } else {
                if (value != null) ((JTextField) component).setText(value.toString());
            }
        } else if (component instanceof JTextArea) {
            if (value != null) {
                ((JTextArea) component).setText(value.toString());
            }
        } else if (component instanceof JCheckBox) {
            if (value != null) ((JCheckBox) component).setSelected(((Boolean) value).booleanValue());
        } else if (component instanceof DisplayOnlyText) {
            if (value != null) {
                ((DisplayOnlyText) component).setValue(value);
            }
        } else if (component instanceof JLabel) {
            if (value != null) ((JLabel) component).setText(value.toString());
        } else if (component instanceof DetailText) {
            if (value != null) {
                ((DetailText) component).setValue(value.toString());
            }
        } else if (component instanceof DetailList) {
            if (value != null) {
                ((DetailList) component).setValue(value);
            }
        } else if (component instanceof XrCalculator) {
            if (value != null) {
                ((XrCalculator) component).setValue(value);
            }
        } else if (component instanceof PasswordEdit) {
            if (value != null) {
                ((PasswordEdit) component).setValue(value);
            }
        }
    }
}
