package mujmail;

public class MyException extends Exception {

    public static final byte COM_BASE = 1;

    public static final byte COM_IN = COM_BASE + 1;

    public static final byte COM_OUT = COM_BASE + 2;

    public static final byte COM_TIMEOUT = COM_BASE + 3;

    public static final byte COM_HALTED = COM_BASE + 4;

    public static final byte COM_UNKNOWN = COM_BASE + 5;

    public static final byte SYSTEM_BASE = 10;

    public static final byte SYS_OUT_OF_MEMORY = SYSTEM_BASE + 1;

    public static final byte SYS_IMAGE_FAILED = SYSTEM_BASE + 2;

    public static final byte PROTOCOL_BASE = 20;

    public static final byte PROTOCOL_CANNOT_CONNECT = PROTOCOL_BASE + 1;

    public static final byte PROTOCOL_CANNOT_RETRIEVE_BODY = PROTOCOL_BASE + 2;

    public static final byte PROTOCOL_COMMAND_NOT_EXECUTED = PROTOCOL_BASE + 3;

    public static final byte PROTOCOL_CANNOT_DELETE_MAILS = PROTOCOL_BASE + 4;

    public static final byte PROTOCOL_CANNOT_GET_URL = PROTOCOL_BASE + 5;

    public static final byte VARIOUS_BASE = 30;

    public static final byte VARIOUS_BAD_EMAIL = VARIOUS_BASE + 1;

    public static final byte VARIOUS_AB_MULTIPLE_ENTRIES = VARIOUS_BASE + 2;

    public static final byte VARIOUS_DECODE_ILLEGAL_MIME = VARIOUS_BASE + 3;

    public static final byte DB_BASE = 50;

    public static final byte DB_NOSPACE = DB_BASE + 1;

    public static final byte DB_CANNOT_CLEAR = DB_BASE + 2;

    public static final byte DB_CANNOT_SAVE_BODY = DB_BASE + 3;

    public static final byte DB_CANNOT_SAVE_HEADER = DB_BASE + 4;

    public static final byte DB_CANNOT_DEL_HEADER = DB_BASE + 5;

    public static final byte DB_CANNOT_DEL_BODY = DB_BASE + 6;

    public static final byte DB_CANNOT_DEL_MAIL = DB_BASE + 7;

    public static final byte DB_CANNOT_LOAD_BODY = DB_BASE + 8;

    public static final byte DB_CANNOT_LOAD_CONTACT = DB_BASE + 9;

    public static final byte DB_CANNOT_SAVE_CONTACT = DB_BASE + 10;

    public static final byte DB_CANNOT_DEL_CONTACT = DB_BASE + 11;

    public static final byte DB_CANNOT_UPDATE_HEADER = DB_BASE + 12;

    public static final byte DB_CANNOT_LOAD_SETTINGS = DB_BASE + 13;

    public static final byte DB_CANNOT_SAVE_SETTINGS = DB_BASE + 14;

    public static final byte DB_CANNOT_LOAD_ACCOUNTS = DB_BASE + 15;

    public static final byte DB_CANNOT_SAVE_ACCOUNT = DB_BASE + 16;

    public static final byte DB_CANNOT_DELETE_ACCOUNT = DB_BASE + 17;

    public static final byte DB_CANNOT_OPEN_DB = DB_BASE + 18;

    public static final byte DB_CANNOT_LOAD_HEADERS = DB_BASE + 19;

    public static final byte DB_CANNOT_CLOSE_DB = DB_BASE + 20;

    public static final byte DB_CANNOT_SAVE_MSGID = DB_BASE + 21;

    public static final byte DB_CANNOT_LOAD_MSGID = DB_BASE + 22;

    public static final byte DB_CANNOT_DEL_MSGID = DB_BASE + 23;

    public static final byte DB_CANNOT_SAVE = DB_BASE + 24;

    public static final byte DB_CANNOT_DEL = DB_BASE + 25;

    private int errorCode = 0;

    private String details = null;

    public MyException(int errorCode) {
        this.errorCode = errorCode;
    }

    public MyException(int errorCode, String details) {
        this.errorCode = errorCode;
        this.details = details;
    }

    /** 
     * Creates MyException. As detail string uses 
     *   standard string associated with exception suffixed by 
     *  given exception e description.
     * @param errorCode Error code from prepared contants
     * @param e Exception that is add as suffinx into {@link #delails}
     */
    public MyException(int errorCode, Throwable e) {
        this.errorCode = errorCode;
        this.details = Lang.get((short) (Lang.EXP_BASE + errorCode)) + " : " + e.toString();
    }

    public String getDetails() {
        if (details != null) {
            return details;
        }
        return Lang.get((short) (Lang.EXP_BASE + errorCode));
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getDetailsNocode() {
        String result = getDetails();
        if (result.startsWith("100:") || result.startsWith("200:") || result.startsWith("300:")) {
            return result.substring(4);
        }
        return result;
    }
}
