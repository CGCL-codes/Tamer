package uqdsd.infosec;

import uqdsd.infosec.model.ProgressListener;

public class StdOutProgressListener implements ProgressListener {

    private String messagePrefix;

    private String messageNote;

    public StdOutProgressListener(String message) {
        super();
        messagePrefix = message;
    }

    public void progressAction(int percent) {
        if (percent >= 100) {
            System.out.println(messagePrefix + " - Done");
        } else {
            System.out.println(messagePrefix + " - " + messageNote + "(" + percent + "%)");
        }
    }

    public void note(String message) {
        messageNote = message;
        System.out.println(messagePrefix + " - " + messageNote);
    }
}
