package com.frinika.sequencer.gui;

import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import com.frinika.sequencer.model.util.TimeUtils;

/**
 * JSpinner extended to display and edit time-values of the form "bar.beat:tick".
 * 
 * @author Jens Gulden
 */
public class TickSpinner extends JSpinnerDraggable implements CaretListener {

    public TickSpinner() {
        super();
        init();
    }

    public TickSpinner(TickSpinnerModel model) {
        super(model);
        init();
    }

    public TickSpinner(TimeFormat format, long value, TimeUtils timeUtils) {
        this(format, value, false, timeUtils);
    }

    public TickSpinner(TimeFormat format, long value, boolean allowNegative, TimeUtils timeUtils) {
        this(new TickSpinnerModel(format, value, allowNegative, timeUtils));
    }

    public TickSpinner(TimeFormat format, TimeUtils timeUtils) {
        this(format, 0, timeUtils);
    }

    public Object getNextValue() {
        return super.getNextValue();
    }

    private void init() {
        JFormattedTextField ftf = (JFormattedTextField) ((JSpinner.DefaultEditor) this.getEditor()).getTextField();
        ftf.setColumns(((TickSpinnerModel) getModel()).format.textFieldSize);
        ftf.setFormatterFactory(new JFormattedTextField.AbstractFormatterFactory() {

            @Override
            public AbstractFormatter getFormatter(JFormattedTextField tf) {
                return new JFormattedTextField.AbstractFormatter() {

                    @Override
                    public Object stringToValue(String text) throws ParseException {
                        return ((TickSpinnerModel) getModel()).stringToTicks(text);
                    }

                    @Override
                    public String valueToString(Object value) throws ParseException {
                        return ((TickSpinnerModel) getModel()).ticksToString((Long) value);
                    }
                };
            }
        });
        ftf.addCaretListener(this);
    }

    @Override
    public void commitEdit() throws ParseException {
        final JTextField textField = ((JSpinner.DefaultEditor) this.getEditor()).getTextField();
        final int pos = textField.getCaretPosition();
        super.commitEdit();
        (new Thread() {

            public void run() {
                try {
                    textField.setCaretPosition(pos);
                } catch (IllegalArgumentException ie) {
                    ie.printStackTrace();
                }
            }
        }).start();
    }

    public void caretUpdate(CaretEvent e) {
        TickSpinnerModel model = (TickSpinnerModel) getModel();
        JTextField textField = ((JSpinner.DefaultEditor) this.getEditor()).getTextField();
        int pos = textField.getCaretPosition();
        model.updateStepSize(textField.getText(), pos);
    }
}
