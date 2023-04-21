package ui.framework.components;

import javax.swing.JLabel;

public class ValidationStatusLabel extends JLabel {

    private static final long serialVersionUID = 7046733371473790296L;

    private String[] statusStrings;

    private boolean valid;

    public ValidationStatusLabel(String valid, String invalid) {
        statusStrings = new String[] { valid, invalid };
        super.setText(invalid);
    }

    public void setText() {
        throw new IllegalStateException("ValidationStatusLabel does not support external classes to set its text.  ");
    }

    public void setValid(boolean valid) {
        this.valid = valid;
        super.setText(valid ? statusStrings[0] : statusStrings[1]);
    }

    public boolean getValid() {
        return valid;
    }
}
