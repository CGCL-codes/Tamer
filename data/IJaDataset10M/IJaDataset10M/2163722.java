package org.icefaces.application.showcase.view.bean.examples.component.progressBar;

/**
 * <p>The OutputProgressModel class stores properties that are used to dynamically
 * change the state of the respective inputProgressBar component</p>
 *
 * @see org.icefaces.application.showcase.view.bean.examples.component.progressBar.OutputProgressController
 * @since 1.7
 */
public class OutputProgressModel {

    private boolean intermediatMode;

    private String labelPosition = "embed";

    private String progressMessage;

    private boolean progressMessageEnabled;

    private String completedMessage;

    private boolean completedMessageEnabled;

    private boolean pogressStarted;

    private int percentComplete;

    public boolean isIntermediatMode() {
        return intermediatMode;
    }

    public void setIntermediatMode(boolean intermediatMode) {
        this.intermediatMode = intermediatMode;
    }

    public String getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }

    /**
     * Gets the Progress message. If the progressMessageEnabled attribute is
     * set to false an empty String is returned.  This ensure that the component
     * will use its default percent value as the progress message.
     *
     * @return current value of progress bar if progressMessageEnabled is true,
     *         otherwise an empty string is returned.
     */
    public String getProgressMessage() {
        if (progressMessageEnabled) {
            return progressMessage;
        } else {
            return "";
        }
    }

    public void setProgressMessage(String progressMessage) {
        this.progressMessage = progressMessage;
    }

    /**
     * Gets the Progress completed message. If the completedMessageEnabled attribute is
     * set to false an empty String is returned.  This ensure that the component
     * will use its default completed message instead of the last entered
     * custom message.
     *
     * @return current value of progress bar if progressMessageEnabled is true,
     *         otherwise an empty string is returned.
     */
    public String getCompletedMessage() {
        if (completedMessageEnabled) {
            return completedMessage;
        } else {
            return "";
        }
    }

    public void setCompletedMessage(String completedMessage) {
        this.completedMessage = completedMessage;
    }

    public boolean isPogressStarted() {
        return pogressStarted;
    }

    public void setPogressStarted(boolean pogressStarted) {
        this.pogressStarted = pogressStarted;
    }

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    public boolean isProgressMessageEnabled() {
        return progressMessageEnabled;
    }

    public void setProgressMessageEnabled(boolean progressMessageEnabled) {
        this.progressMessageEnabled = progressMessageEnabled;
    }

    public boolean isCompletedMessageEnabled() {
        return completedMessageEnabled;
    }

    public void setCompletedMessageEnabled(boolean completedMessageEnabled) {
        this.completedMessageEnabled = completedMessageEnabled;
    }
}
