package wizworld.navigate.awt;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import wizworld.awt.*;
import wizworld.navigate.database.TableNotFoundException;
import wizworld.navigate.database.Tables;
import wizworld.navigate.geo.*;
import wizworld.navigate.stream.*;
import wizworld.util.Resources;

/** Edits a tidal stream record
 * @author (c) Stephen Denham 2003
 * @version 0.1
 * @version 5.0.2 - SD - Mac settings.
 */
public final class TidalStreamRecordDialog implements ActionListener, KeyListener, DocumentListener {

    /** Parent frame */
    private GuiFrame parent;

    /** The dialog */
    private ComponentDialog dialog;

    /** Waypoint position */
    private Cartesian positionValue;

    /** Button for invoking position editing dialog */
    private JButton positionButton;

    /** Position text field */
    private JTextField positionField;

    /** Set & drift values */
    private JTextField setDriftValues[];

    /** Set & drift components */
    private Vector<JTextField> setDriftComponents = new Vector<JTextField>(0);

    /** Title */
    private String title;

    /** Dialog result */
    private boolean result = false;

    /** Key pressed */
    private int keyCode;

    /** Constructor
   * @param	parent	Parent frame
   * @param	title	Window title
   * @param   tidalStreamRecord	Tidal stream record to edit
   */
    public TidalStreamRecordDialog(GuiFrame parent, String title, TidalStreamRecord tidalStreamRecord) {
        this.title = title;
        this.parent = parent;
        this.dialog = new ComponentDialog(this.parent, this.title);
        GridBagConstraints constraints = this.dialog.panel.getConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        dialog.panel.setConstraints(constraints);
        try {
            this.positionValue = new Cartesian(new Latitude(tidalStreamRecord.getPosition().getLatitude()), new Longitude(tidalStreamRecord.getPosition().getLongitude()));
        } catch (AngleException ex) {
            new MessageDialog(parent, Resources.getString("Error"), Resources.getString("CannotCalculate"));
            return;
        }
        JComboBox portField = new RecordChoice(Tables.PORTS, tidalStreamRecord.getPortName());
        JLabel portLabel = null;
        try {
            portLabel = new JLabel((Tables.getTable(Tables.TIDAL_STREAMS)).getColumnName(TidalStreamTable.TIDAL_STREAM_PORT_COLUMN), JLabel.RIGHT);
        } catch (TableNotFoundException ex) {
            new MessageDialog(parent, Resources.getString("Error"), Resources.getString("CannotLoadTable"));
            return;
        }
        this.dialog.panel.addItem(portLabel, 0, 0);
        this.dialog.panel.addItem(portField, 1, 0);
        this.positionField = new JTextField(positionValue.toString());
        this.positionField.setEditable(false);
        this.dialog.panel.addItem(this.positionField, 2, 0);
        this.positionButton = new EllipsisButton();
        this.positionButton.addActionListener(this);
        this.positionButton.addKeyListener(this);
        constraints.fill = GridBagConstraints.NONE;
        this.dialog.panel.setConstraints(constraints);
        this.dialog.panel.addItem(this.positionButton, 3, 0);
        constraints.fill = GridBagConstraints.BOTH;
        this.dialog.panel.setConstraints(constraints);
        JTextField descriptionField = new JTextField(new StringBuffer(tidalStreamRecord.getDescription()).toString());
        JLabel descriptionLabel = new JLabel();
        try {
            descriptionLabel = new JLabel((Tables.getTable(Tables.TIDAL_STREAMS)).getColumnName(TidalStreamTable.TIDAL_STREAM_DESCRIPTION_COLUMN), Label.RIGHT);
        } catch (TableNotFoundException ex) {
            new MessageDialog(parent, Resources.getString("Error"), Resources.getString("CannotLoadTable"));
            return;
        }
        this.dialog.panel.addItem(descriptionLabel, 0, 1);
        this.dialog.panel.addItem(descriptionField, 1, 1);
        double setDriftAttributes[][] = tidalStreamRecord.getSetDrift();
        JLabel setDriftLabels[] = new JLabel[0];
        try {
            setDriftLabels = new JLabel[TidalStreamTable.SET_AND_DRIFT_ENTRIES];
            setDriftValues = new JTextField[TidalStreamTable.SET_AND_DRIFT_ENTRIES];
            String label = "";
            for (int i = 0, row = 2, column = 0; i < TidalStreamTable.SET_AND_DRIFT_ENTRIES; i++, row++) {
                if (i == TidalStreamTable.SET_AND_DRIFT_ENTRIES / 2) {
                    row = 1;
                    column = 2;
                }
                label = (Tables.getTable(Tables.TIDAL_STREAMS)).getColumnName(TidalStreamTable.TIDAL_STREAM_SET_DRIFT_COLUMN + i);
                if (i > 0) {
                    label = Integer.toString(-1 * (TidalStreamTable.SET_AND_DRIFT_ENTRIES / 2 - i));
                    if (i > TidalStreamTable.SET_AND_DRIFT_ENTRIES / 2) {
                        label = "+" + label;
                    }
                }
                setDriftLabels[i] = new JLabel(label);
                setDriftValues[i] = new JTextField((new SetDriftAttribute(setDriftAttributes[i])).toString());
                setDriftValues[i].getDocument().addDocumentListener(this);
                setDriftValues[i].getDocument().putProperty("id", i);
                if (i < TidalStreamTable.SET_AND_DRIFT_ENTRIES / 2) {
                    setDriftLabels[i].setHorizontalAlignment(JLabel.RIGHT);
                    this.dialog.panel.addItem(setDriftLabels[i], column, row);
                    this.dialog.panel.addItem(setDriftValues[i], column + 1, row);
                } else {
                    setDriftLabels[i].setHorizontalAlignment(JLabel.LEFT);
                    this.dialog.panel.addItem(setDriftLabels[i], column + 1, row);
                    dialog.panel.addItem(setDriftValues[i], column, row);
                }
            }
        } catch (TableNotFoundException te) {
            new MessageDialog(parent, Resources.getString("Error"), Resources.getString("CannotLoadTable"));
            return;
        } catch (SetDriftAttributeException ex) {
            new MessageDialog(parent, Resources.getString("Error"), Resources.getString("CannotCalculate"));
            return;
        }
        this.dialog.display();
        if (this.dialog.getResult() == ComponentDialog.TRUE) {
            try {
                tidalStreamRecord.setPortName(portField.getSelectedItem().toString());
                tidalStreamRecord.setPosition(this.positionValue);
                tidalStreamRecord.setDescription(descriptionField.getText());
                double setDrift[][] = new double[TidalStreamTable.SET_AND_DRIFT_ENTRIES][SetDriftAttribute.SET_DRIFT_VALUES];
                SetDriftAttribute sd = new SetDriftAttribute();
                for (int i = 0; i < TidalStreamTable.SET_AND_DRIFT_ENTRIES; i++) {
                    sd.toSetDriftAttribute(setDriftValues[i].getText());
                    setDrift[i] = sd.getSetDriftAttribute();
                    sd = new SetDriftAttribute();
                }
                tidalStreamRecord.setSetDrift(setDrift);
            } catch (SetDriftAttributeException ex1) {
                new MessageDialog(parent, Resources.getString("Error"), Resources.getString("CannotCalculate"));
                this.result = false;
            }
            this.result = true;
        } else {
            this.result = false;
        }
    }

    /** Invoke position dialog */
    private void getPosition() {
        try {
            new CartesianDialog(this.parent, this.title, this.positionValue);
            this.positionField.setText(this.positionValue.toString());
        } catch (AngleException e) {
            new MessageDialog(parent, Resources.getString("Error"), Resources.getString("CannotCalculate"));
            return;
        }
    }

    /** Get the dialog result
   * @return	True if OK'd, else false
   */
    public boolean getResult() {
        return this.result;
    }

    /** Dialog position button
   * @param	ae	Action event
   */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == positionButton) {
            this.getPosition();
        }
    }

    /** Enter key on position button
   * @param e Key event
   */
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == positionButton && e.getKeyChar() == KeyEvent.VK_ENTER && this.keyCode == KeyEvent.VK_ENTER) {
            this.getPosition();
        }
    }

    public void keyPressed(KeyEvent e) {
        this.keyCode = e.getKeyCode();
    }

    public void keyReleased(KeyEvent e) {
    }

    /** Set & drift field changed
   * @param e Text changed event
   */
    private void textValueChanged(DocumentEvent e) {
        SetDriftAttribute sd = new SetDriftAttribute();
        JTextField textField = this.setDriftValues[(Integer) e.getDocument().getProperty("id")];
        String text = textField.getText();
        int insertionPoint = textField.getCaretPosition();
        try {
            sd.toSetDriftAttribute(text);
            if (this.setDriftComponents.contains(textField)) {
                this.setDriftComponents.removeElement(textField);
            }
            if (this.setDriftComponents.isEmpty()) {
                this.dialog.setValidComponents(true);
            }
        } catch (SetDriftAttributeException ex) {
            if (!this.setDriftComponents.contains(textField)) {
                this.setDriftComponents.addElement(textField);
            }
            textField.setCaretPosition(insertionPoint);
            this.dialog.setValidComponents(false);
        }
    }

    public void changedUpdate(DocumentEvent e) {
        textValueChanged(e);
    }

    public void insertUpdate(DocumentEvent e) {
        textValueChanged(e);
    }

    public void removeUpdate(DocumentEvent e) {
        textValueChanged(e);
    }
}
