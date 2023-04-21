package com.sourceforge.microframe.state;

public class ErrorState {

    private static ErrorState instance = null;

    private String message;

    private String title;

    private int previousScreen;

    public static ErrorState getInstance() {
        if (instance == null) {
            instance = new ErrorState();
        }
        return instance;
    }

    public String getMessage() {
        return message == null ? "Message unknown" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title == null ? "Error Message" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPreviousScreen() {
        return previousScreen;
    }

    public void setPreviousScreen(int previousScreen) {
        this.previousScreen = previousScreen;
    }

    ErrorState() {
    }
}
