package diet.server;

import java.util.Hashtable;

/**
 * This stores the latest status window message sent to each participant.
 * This is to cut down network traffic and also reduce the size and improve the
 * legibility of the log files. 
 * 
 * 
 * @author user
 */
public class LabelManager {

    Conversation c;

    private Hashtable lastReceivedLabels = new Hashtable();

    /**
     * Creates a new instance of LabelManager
     */
    public LabelManager(Conversation c) {
        this.c = c;
    }

    /**
     * Checks whether the status bar of a participant is already displaying a particular String. If it isn't the
     * value is updated.
     * 
     * @param recipient
     * @param window
     * @param text
     * @return whether label is already set to text value
     */
    public boolean isLabelAlreadySetToValue_AndIfNotUpdateIt_IgnoreEnableDisableStatus(Participant recipient, int window, String text) {
        Hashtable pSender = (Hashtable) lastReceivedLabels.get(recipient);
        if (pSender == null) {
            pSender = new Hashtable();
            lastReceivedLabels.put(recipient, pSender);
        }
        LabelManagerLabel labelCurrently = (LabelManagerLabel) pSender.get(window);
        if (labelCurrently == null) {
            pSender.put(window, new LabelManagerLabel(text));
            return false;
        } else if (!labelCurrently.equalsIgnoreLabelAndEnable(text)) {
            pSender.put(window, new LabelManagerLabel(text));
            return false;
        } else {
            return true;
        }
    }

    public boolean isLabelAlreadySetToValueAndEnabledDisabledAndColour_AndIfNotUpdateIt(Participant recipient, int window, String text, boolean isRed, boolean isEnabled) {
        Hashtable pSender = (Hashtable) lastReceivedLabels.get(recipient);
        if (pSender == null) {
            pSender = new Hashtable();
            lastReceivedLabels.put(recipient, pSender);
        }
        LabelManagerLabel labelCurrently = (LabelManagerLabel) pSender.get(window);
        if (labelCurrently == null) {
            pSender.put(window, new LabelManagerLabel(text, isRed, isEnabled));
            return false;
        } else if (!labelCurrently.equalsStringLabelAndEnable(text, isRed, isEnabled)) {
            pSender.put(window, new LabelManagerLabel(text, isRed, isEnabled));
            return false;
        } else {
            return true;
        }
    }

    public class LabelManagerLabel {

        public String label;

        public boolean isRed;

        public boolean isEnabled;

        public boolean hasBeenSetBeforeIgnoringIsRedAndEnableStatus = true;

        public LabelManagerLabel(String s) {
            label = s;
            isRed = false;
            isEnabled = false;
            this.hasBeenSetBeforeIgnoringIsRedAndEnableStatus = true;
        }

        public LabelManagerLabel(String s, boolean red, boolean enabled) {
            label = s;
            isRed = red;
            isEnabled = enabled;
            this.hasBeenSetBeforeIgnoringIsRedAndEnableStatus = false;
        }

        public boolean equalsIgnoreLabelAndEnable(String s) {
            if (s.equals(label)) {
                return true;
            }
            return false;
        }

        public boolean equalsStringLabelAndEnable(String s, boolean red, boolean enabled) {
            if (hasBeenSetBeforeIgnoringIsRedAndEnableStatus) {
                return false;
            }
            if (label.equals(s) && isRed == red && isEnabled == enabled) {
                return true;
            }
            return false;
        }
    }
}
