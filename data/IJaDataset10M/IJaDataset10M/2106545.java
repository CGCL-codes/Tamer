package net.firefly.client.gui.swing.other;

import javax.swing.JProgressBar;

public class LabeledProgressBar extends JProgressBar {

    private static final long serialVersionUID = 2734611631719126238L;

    private String additionalLabel;

    private boolean displayPercentage = true;

    public LabeledProgressBar(int min, int max, String additionalLabel, boolean displayPercentage) {
        super(min, max);
        this.additionalLabel = additionalLabel;
        this.displayPercentage = displayPercentage;
    }

    public LabeledProgressBar(int min, int max, String additionalLabel) {
        this(min, max, additionalLabel, true);
    }

    public LabeledProgressBar(int min, int max) {
        this(min, max, null, true);
    }

    public LabeledProgressBar(int min, int max, boolean displayPercentage) {
        this(min, max, null, displayPercentage);
    }

    public String getAdditionalLabel() {
        return additionalLabel;
    }

    public void setAdditionalLabel(String additionalLabel) {
        this.additionalLabel = additionalLabel;
    }

    public boolean isDisplayPercentage() {
        return displayPercentage;
    }

    public void setDisplayPercentage(boolean displayPercentage) {
        this.displayPercentage = displayPercentage;
    }

    public String getString() {
        StringBuffer result = new StringBuffer();
        if (this.additionalLabel != null) {
            result.append(additionalLabel).append(" ");
        }
        if (displayPercentage) {
            result.append((int) (getPercentComplete() * 100)).append(" %");
        }
        setString(result.toString());
        return super.getString();
    }
}
