package cogitant.base;

/**	Exception thrown when a IO operation fails. */
public class ExceptionIO extends Exception {

    public static final int NOTYPE = 0;

    public static final int OPEN = 1;

    public static final int CREATE = 2;

    public static final int READ = 3;

    public static final int WRITE = 4;

    public static final int FORMAT = 5;

    public static final int HEADER = 6;

    public static final int CONNECT = 7;

    public static final int ENDOFSTREAM = 8;

    public static final int CHECK = 9;

    protected String m_streamname;

    protected long m_line;

    public ExceptionIO(int code, String streamname, long line, String what) {
        super(code, what);
        m_streamname = streamname;
        m_line = line;
    }

    public String streamname() {
        return m_streamname;
    }

    public long line() {
        return m_line;
    }

    public String toString() {
        String result = m_streamname + ":";
        if (m_line != 0) result = result + m_line + ": "; else result = result + " ";
        if (code() == NOTYPE) result += "Unknown error."; else if (code() == OPEN) result += "Open error."; else if (code() == CREATE) result += "Create error."; else if (code() == READ) result += "Read error."; else if (code() == WRITE) result += "Write error."; else if (code() == FORMAT) result += "Format error."; else if (code() == HEADER) result += "Unknown header."; else if (code() == CONNECT) result += "Connect error."; else if (code() == ENDOFSTREAM) result += "Premature end of stream."; else if (code() == CHECK) result += "Check error.";
        return result;
    }
}
