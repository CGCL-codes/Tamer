package org.wejde.muel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * This class contains the members and methods related to
 * the panel containing fields.  For muel, this object
 * resides only within a tabPane.
 * 
 * The methods included are for clearing the fields and
 * retrieving their values.
 *  
 * @author Eric Martin
 * @author Webb Pinner
 *
 */
public class fieldsPane extends JPanel {

    private static final int MAX_COLUMNS = 3;

    /**
	 * Default field delimiter to use
	 */
    private static final String DEFAULTDELIMETER = ",";

    /**
	 * Default field delimiter to use
	 */
    private static final String DEFAULTSEPERATOR = ":";

    /**
	 * Serial Version UID
	 */
    private static final long serialVersionUID = 176821119342327083L;

    /**
	 * Layout manager for the pane.
	 */
    private GridBagLayout gridBag;

    /**
	 * Array of JComponents, representing all of the fields
	 * displayed on the pane.
	 */
    private JComponent[] jComponentArray;

    /**
	 * Array of fields objects, used to create and manage
	 * the behaviors of the JComponent array.
	 */
    private fields[] fieldsArray;

    /**
	 * The delimiter to use when concatenating field
	 * values.
	 */
    private String delim;

    /**
	 * Constructor method.
	 */
    public fieldsPane() {
        super();
        this.setMinimumSize(new Dimension(570, 375));
        this.setSize(this.getMinimumSize());
        this.delim = new String(",");
        gridBag = new GridBagLayout();
        this.setLayout(gridBag);
    }

    /**
	 * Constructor method.
	 * 
	 * @param fieldsArray the array of field objects to use
	 * for creating the JComponent array.
	 */
    public fieldsPane(fields[] fieldsArray) {
        this();
        this.delim = fieldsPane.DEFAULTDELIMETER;
        this.fieldsArray = fieldsArray;
        GridBagConstraints constraints = new GridBagConstraints();
        jComponentArray = new JComponent[fieldsArray.length];
        int curRow = 0;
        int curCol = 0;
        int checkboxCount = fieldTypeCount(fieldsArray, fields.CHECKBOX);
        int textfieldCount = fieldTypeCount(fieldsArray, fields.TEXTFIELD);
        for (int i = 0; i < fieldsArray.length; i++) {
            if (this.fieldsArray[i] != null && this.fieldsArray[i].getFieldType() == fields.CHECKBOX) {
                JCheckBox cb = new JCheckBox(this.fieldsArray[i].getFieldLabel());
                if (fieldsArray[i].getFieldValue()[0] == fields.TRUE) {
                    cb.setSelected(true);
                }
                cb.setToolTipText(this.fieldsArray[i].getFieldTooltip());
                jComponentArray[i] = cb;
                this.buildConstraints(constraints, curCol, curRow, 1, 1, 10, 10);
                constraints.fill = GridBagConstraints.NONE;
                constraints.anchor = GridBagConstraints.NORTHWEST;
                gridBag.setConstraints(jComponentArray[i], constraints);
                this.add(jComponentArray[i]);
                curCol++;
                if (curCol == fieldsPane.MAX_COLUMNS) {
                    curRow++;
                    curCol = 0;
                }
            }
        }
        if (checkboxCount > 0) {
            curRow++;
            curCol = 0;
            JSeparator js1 = new JSeparator(SwingConstants.HORIZONTAL);
            this.buildConstraints(constraints, curCol, curRow, 3, 1, 100, 10);
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.CENTER;
            gridBag.setConstraints(js1, constraints);
            this.add(js1);
            curRow++;
        }
        for (int i = 0; i < fieldsArray.length; i++) {
            if (fieldsArray[i] != null && fieldsArray[i].getFieldType() == fields.TEXTFIELD) {
                JTextField tf = new JTextField(fieldsArray[i].getFieldValue()[0]);
                tf.setSize(8, 1);
                tf.setToolTipText(fieldsArray[i].getFieldTooltip());
                tf.setColumns(12);
                jComponentArray[i] = tf;
                JLabel l = new JLabel(fieldsArray[i].getFieldLabel());
                this.buildConstraints(constraints, curCol, curRow, 1, 1, 10, 10);
                constraints.fill = GridBagConstraints.NONE;
                constraints.anchor = GridBagConstraints.SOUTHWEST;
                gridBag.setConstraints(l, constraints);
                this.add(l);
                this.buildConstraints(constraints, curCol, curRow + 1, 1, 1, 10, 10);
                constraints.fill = GridBagConstraints.NONE;
                constraints.anchor = GridBagConstraints.NORTHWEST;
                gridBag.setConstraints(jComponentArray[i], constraints);
                this.add(jComponentArray[i]);
                curCol++;
                if (curCol == fieldsPane.MAX_COLUMNS) {
                    curRow += 2;
                    curCol = 0;
                }
            }
        }
        if (textfieldCount > 0) {
            curRow += 2;
            curCol = 0;
            JSeparator js2 = new JSeparator(SwingConstants.HORIZONTAL);
            this.buildConstraints(constraints, curCol, curRow, 3, 1, 100, 10);
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.CENTER;
            gridBag.setConstraints(js2, constraints);
            this.add(js2);
            curRow++;
        }
        for (int i = 0; i < fieldsArray.length; i++) {
            if (fieldsArray[i] != null && fieldsArray[i].getFieldType() == fields.COMBOBOX) {
                JComboBox cb = new JComboBox(fieldsArray[i].getFieldValue());
                cb.setToolTipText(fieldsArray[i].getFieldTooltip());
                cb.setEditable(false);
                jComponentArray[i] = cb;
                JLabel l = new JLabel(fieldsArray[i].getFieldLabel());
                this.buildConstraints(constraints, curCol, curRow, 1, 1, 10, 10);
                constraints.fill = GridBagConstraints.NONE;
                constraints.anchor = GridBagConstraints.SOUTHWEST;
                gridBag.setConstraints(l, constraints);
                this.add(l);
                this.buildConstraints(constraints, curCol, curRow + 1, 1, 1, 10, 10);
                constraints.fill = GridBagConstraints.NONE;
                constraints.anchor = GridBagConstraints.NORTHWEST;
                gridBag.setConstraints(jComponentArray[i], constraints);
                this.add(jComponentArray[i]);
                curCol++;
                if (curCol == fieldsPane.MAX_COLUMNS) {
                    curRow += 2;
                    curCol = 0;
                }
            }
        }
        if (checkboxCount == 0 && textfieldCount == 0) {
            curRow += 2;
            curCol = 0;
            JSeparator js1 = new JSeparator(SwingConstants.HORIZONTAL);
            this.buildConstraints(constraints, curCol, curRow, 3, 1, 100, 10);
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.CENTER;
            gridBag.setConstraints(js1, constraints);
            this.add(js1);
        }
    }

    /**
	 * Clears/Resets the fields to their default state.
	 * This behavior is dependent on the clearOnSubmit
	 * flag. 
	 */
    public void clearFields() {
        for (int i = 0; i < this.jComponentArray.length; i++) {
            if (this.fieldsArray[i].isFieldClearOnSubmit()) {
                if (fieldsArray[i].getFieldType() == fields.CHECKBOX) {
                    JCheckBox cb = (JCheckBox) jComponentArray[i];
                    if (fieldsArray[i].getFieldValue()[0] == fields.TRUE) {
                        cb.setSelected(true);
                    } else {
                        cb.setSelected(false);
                    }
                }
                if (fieldsArray[i].getFieldType() == fields.COMBOBOX) {
                    JComboBox cb = (JComboBox) jComponentArray[i];
                    cb.setSelectedIndex(0);
                }
                if (fieldsArray[i].getFieldType() == fields.TEXTFIELD) {
                    JTextField tf = (JTextField) jComponentArray[i];
                    tf.setText(fieldsArray[i].getFieldValue()[0]);
                }
            }
        }
    }

    /**
	 * Return a string containing all of the values and
	 * hidden values from the fields.  The values are
	 * delimited based on the delimiter specified in the
	 * configuration file.
	 *  
	 * @return string representation of the values and
	 * hidden values.
	 */
    public String getFieldValues() {
        String msg = "";
        for (int i = 0; i < this.jComponentArray.length; i++) {
            if (this.fieldsArray[i].getFieldType() == fields.CHECKBOX) {
                JCheckBox cb = (JCheckBox) jComponentArray[i];
                if (cb.isSelected()) {
                    msg += fieldsArray[i].getFieldHiddenValue()[0] + this.delim;
                }
            }
            if (fieldsArray[i].getFieldType() == fields.COMBOBOX) {
                JComboBox cb = (JComboBox) jComponentArray[i];
                if (cb.getSelectedIndex() > 0) {
                    if (!fieldsArray[i].getFieldHiddenValue()[cb.getSelectedIndex()].equals("")) {
                        if (fieldsArray[i].isUseOnlyHidden()) {
                            msg += fieldsArray[i].getFieldHiddenValue()[cb.getSelectedIndex()] + this.delim;
                        } else {
                            msg += fieldsArray[i].getFieldHiddenValue()[cb.getSelectedIndex()] + fieldsPane.DEFAULTSEPERATOR;
                            msg += fieldsArray[i].getFieldValue()[cb.getSelectedIndex()] + this.delim;
                        }
                    } else {
                        msg += fieldsArray[i].getFieldValue()[cb.getSelectedIndex()] + this.delim;
                    }
                }
            }
            if (fieldsArray[i].getFieldType() == fields.TEXTFIELD) {
                JTextField tf = (JTextField) jComponentArray[i];
                if (tf.getText().length() > 0) {
                    if (fieldsArray[i].getFieldHiddenValue()[0].length() > 0) {
                        msg += fieldsArray[i].getFieldHiddenValue()[0] + fieldsPane.DEFAULTSEPERATOR;
                    }
                    msg += tf.getText() + this.delim;
                }
            }
        }
        return msg;
    }

    /**
	 * Sets the delimiter used when collecting the values
	 * from the JComponents.
	 * 
	 * @param delim string containing the field delimiter.
	 */
    public void setDelimiter(String delim) {
        this.delim = delim;
    }

    /**
	 * Get the field delimiter
	 * 
	 * @return the field delimiter as a string.
	 */
    public String getDelimiter() {
        return this.delim;
    }

    /**
	 * Used to build the gridbag layout constraints object.
	 * 
	 * @param gbc the gridbag constraint object
	 * @param gx the x-value in the grid
	 * @param gy the y-value in the grid
	 * @param gw the width of the cell
	 * @param gh the height of the cell
	 * @param wx the x-weight of the cell
	 * @param wy the y-weight of the cell
	 */
    private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy) {
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.weighty = wy;
    }

    private int fieldTypeCount(fields[] fa, short fieldType) {
        int count = 0;
        for (int i = 0; i < fa.length; i++) {
            if (fa[i].getFieldType() == fieldType) count++;
        }
        return count;
    }
}
