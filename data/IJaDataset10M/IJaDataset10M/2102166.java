package cloudspace.ui.applet;

import java.lang.reflect.Constructor;

public class CloudResult {

    Class<? extends Throwable> ex = null;

    String exMessage = null;

    String[] results = null;

    public CloudResult(Class<? extends Throwable> ex, String exMessage) {
        this.ex = ex;
        this.exMessage = exMessage;
    }

    public CloudResult(String... results) {
        this.results = results;
    }

    public Throwable getException() {
        return new RuntimeException(exMessage);
    }

    public Throwable getException(Class<?>... canThrow) {
        try {
            Constructor<? extends Throwable> messageConstructor = ex.getConstructor(String.class);
            return messageConstructor.newInstance(exMessage);
        } catch (Exception e) {
            return new RuntimeException(exMessage);
        }
    }

    public boolean isError() {
        return ex != null;
    }

    public String[] getResults() {
        return results;
    }

    public String getResult(int i) {
        if (results.length <= i || i < 0) return null;
        return results[i];
    }
}
