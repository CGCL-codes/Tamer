package edu.arsc.fullmetal.server;

/**
 *
 * @author cgranade
 */
public class RobotDriverException extends Exception {

    public RobotDriverException() {
        super();
    }

    public RobotDriverException(String msg) {
        super(msg);
    }

    public RobotDriverException(String msg, Throwable ex) {
        super(msg, ex);
    }

    public RobotDriverException(Throwable ex) {
        super(ex);
    }
}
