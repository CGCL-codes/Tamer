package org.ashleygwinnell.snahs.err;

import java.util.ArrayList;

public class SnahsException extends Exception {

    public ArrayList<String> messages = new ArrayList<String>();

    public boolean kill;

    public boolean showingStackTrace;

    public Exception parentException;

    public SnahsException() {
    }

    public void setKill(boolean kill) {
        this.kill = kill;
    }

    public void setMessage(String message) {
        this.messages.add(message);
    }

    public boolean isKill() {
        return kill;
    }

    public void setParentException(Exception parent) {
        this.parentException = parent;
    }

    public void print() {
        System.err.println("------------");
        System.err.println("SNAHS ERROR: ");
        System.err.println("------------");
        for (int i = 0; i < this.messages.size(); i++) {
            System.err.println(this.messages.get(i));
        }
        if (this.isShowingStackTrace()) {
            this.parentException.printStackTrace();
        }
        System.err.println("------------");
    }

    public void setShowStackTrace(boolean b) {
        this.showingStackTrace = b;
    }

    public boolean isShowingStackTrace() {
        return this.showingStackTrace;
    }
}
