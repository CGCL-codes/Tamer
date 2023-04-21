package lablog.gui.comp;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

@SuppressWarnings("serial")
public class TimeEditField extends JFormattedTextField {

    private SimpleDateFormat sdf;

    public TimeEditField() {
        super();
        MaskFormatter mf = null;
        try {
            mf = new MaskFormatter("##:##:##");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DefaultFormatterFactory dff = new DefaultFormatterFactory(mf);
        sdf = new SimpleDateFormat("HH:mm:ss");
        this.setToolTipText("Enter time in format hh:mm:ss");
        this.setFormatterFactory(dff);
        this.setInputVerifier(new SpecialDateInputVerifier());
        this.addFocusListener(new TimeFieldFocusListener(this));
    }

    @Override
    public void setText(String t) throws IllegalArgumentException {
        if (checkTime(t)) super.setText(t); else throw new IllegalArgumentException("Invalid date string, enter time in format hh:mm:ss: " + t);
    }

    public void setTime(Date date) {
        if (date != null) setText(sdf.format(date)); else setText("  :  :  ");
    }

    public Date getTime() {
        Date result = null;
        String timeStr = getText();
        if (timeStr != null && !timeStr.isEmpty() && !timeStr.contains("  :  :  ")) {
            try {
                result = sdf.parse(timeStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean checkTime(String input) {
        boolean result = true;
        if (input != null) {
            if (!input.isEmpty() && !input.contains("  :  :  ")) {
                String sub = input.substring(0, 2);
                if (sub.matches("^[\\p{Digit}]{1,2}$")) {
                    if (Integer.valueOf(sub) < 0 || Integer.valueOf(sub) > 24) result = false;
                } else {
                    result = false;
                }
                sub = input.substring(3, 5);
                if (sub.matches("^[\\p{Digit}]{1,2}$")) {
                    if (Integer.valueOf(sub) < 0 || Integer.valueOf(sub) > 59) result = false;
                } else {
                    result = false;
                }
                sub = input.substring(6, 8);
                if (sub.matches("^[\\p{Digit}]{1,2}$")) {
                    if (Integer.valueOf(sub) < 0 || Integer.valueOf(sub) > 59) result = false;
                } else {
                    result = false;
                }
            }
        }
        return result;
    }

    private final class SpecialDateInputVerifier extends javax.swing.InputVerifier {

        public SpecialDateInputVerifier() {
            super();
        }

        public boolean verify(javax.swing.JComponent input) {
            javax.swing.JTextField jTF = (javax.swing.JTextField) input;
            String sInput = jTF.getText();
            return checkTime(sInput);
        }

        public boolean shouldYieldFocus(javax.swing.JComponent input) {
            if (!verify(input)) {
                input.setForeground(java.awt.Color.RED);
                return false;
            } else {
                input.setForeground(java.awt.Color.BLACK);
                return true;
            }
        }
    }

    private final class TimeFieldFocusListener implements FocusListener {

        private TimeEditField field;

        public TimeFieldFocusListener(TimeEditField field) {
            super();
            this.field = field;
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (field.getText() == null || field.getText().isEmpty()) {
                field.setText("  :  :  ");
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
        }
    }
}
