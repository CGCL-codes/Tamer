package org.globus.ftp.exception;

/**
Most exceptions used by ftp package are subclasses of FTPException and inherit its features:

<ul>
<li> exception code can be used to more precisely identify the problem. Exception codes are defined within each exception class (look at the source code). For example, in ClientException, code 8 (ClientException.BAD_MODE) indicates that client refused operation because of bad transfer mode, while code 13 (ClientException.BAD_TYPE) indicates that the same thing was caused by bad transfer type. To programmatically retrieve the exception code, use exception.getCode().

<li> exception nesting can be used to track the root of the exceptions that come from lower software layers. Use getRootCause().
</ul>
 */
public class FTPException extends Exception {

    public static final int UNSPECIFIED = 0;

    protected int code = UNSPECIFIED;

    private static String[] codeExplained = { "Unspecified category." };

    public String getCodeExplanation(int code) {
        if (codeExplained.length > code) return codeExplained[code]; else return "";
    }

    protected Exception cause;

    protected String customMessage;

    public FTPException(int code, String message) {
        super();
        this.code = code;
        customMessage = message;
    }

    public FTPException(int code) {
        this.code = code;
    }

    public void setRootCause(Exception c) {
        this.cause = c;
    }

    /**
       Retrieve the nested lower layer exception.
     */
    public Exception getRootCause() {
        return cause;
    }

    public void setCode(int c) {
        this.code = c;
    }

    public int getCode() {
        return code;
    }

    public void setCustomMessage(String m) {
        customMessage = m;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public String getMessage() {
        StringBuffer buf = new StringBuffer();
        if (code != UNSPECIFIED) {
            buf.append(getCodeExplanation(code));
        }
        if (customMessage != null) {
            buf.append(" Custom message: ");
            buf.append(customMessage);
        }
        if (code != UNSPECIFIED) {
            buf.append(" (error code ").append(String.valueOf(code)).append(")");
        }
        if (cause != null) {
            buf.append(" [Nested exception message: ");
            buf.append(cause.getMessage());
            buf.append("]");
        }
        return buf.toString();
    }

    public String toString() {
        String answer = super.toString();
        if (cause != null && cause != this) {
            answer += " [Nested exception is " + cause.toString() + "]";
        }
        return answer;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(java.io.PrintStream ps) {
        if (cause != null) {
            String superString = super.toString();
            synchronized (ps) {
                ps.print(superString + (superString.endsWith(".") ? "" : ".") + "  Nested exception is ");
                cause.printStackTrace(ps);
            }
        } else {
            super.printStackTrace(ps);
        }
    }

    public void printStackTrace(java.io.PrintWriter pw) {
        if (cause != null) {
            String superString = super.toString();
            synchronized (pw) {
                pw.print(superString + (superString.endsWith(".") ? "" : ".") + "  Nested exception is ");
                cause.printStackTrace(pw);
            }
        } else {
            super.printStackTrace(pw);
        }
    }
}
