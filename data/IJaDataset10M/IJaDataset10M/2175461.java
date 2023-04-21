package org.gnu.amSpacks;

public class DefaultLogger implements ILogger {

    public void log(String message) {
        System.out.println(message);
    }
}
