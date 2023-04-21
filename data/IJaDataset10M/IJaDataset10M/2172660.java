package org.aacc.utils.comm;

/**
 * A message that has a response code, such as 100, 200, 220, 407, etc.
 * 
 * @author Fernando
 */
public class AACCResponse extends AACCMessage {

    public static int MSG_UNKNOWN = 999;

    public static int MSG_INFORMATION = 100;

    public static int MSG_OK = 200;

    public static int MSG_PUSH = 220;

    public static int MSG_BAD_REQUEST = 400;

    public static int MSG_UNAUTHORIZED = 401;

    public static int MSG_NOT_FOUND = 404;

    public static int MSG_NOT_ACCEPTABLE = 406;

    public static int MSG_CONFLICT = 409;

    private int code = MSG_UNKNOWN;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void args2Properties() {
    }

    public void properties2Args() {
    }

    @Override
    public void extractArguments() {
        if (command.length() <= 4) {
            command = "";
        } else {
            command = command.substring(code, 4);
        }
        super.extractArguments();
    }

    @Override
    public String toString() {
        try {
            return String.valueOf(code);
        } catch (Exception e) {
            return "999";
        }
    }
}
