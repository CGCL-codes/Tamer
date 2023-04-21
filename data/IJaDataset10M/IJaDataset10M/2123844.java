package icreate.db;

public class dbException extends java.lang.Exception {

    /** Holds value of property msg
     */
    private String msg;

    /** Holds value of property obj
     */
    private Object obj;

    /** Blank Constructor
     */
    public dbException() {
    }

    /** Create new instance of Object
     * @param obj New value for obj property
     * @param msg New value for msg property
     */
    public dbException(String msg, Object obj) {
        this.msg = msg;
    }

    /** Return error message
      * @return Error message
      */
    public String toString() {
        return "DB Exception: " + msg;
    }

    /** Return object attached
     * @return Object attached
     */
    public Object getObject() {
        return obj;
    }
}
