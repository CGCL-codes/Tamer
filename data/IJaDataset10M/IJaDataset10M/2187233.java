package be.derycke.pieter.com;

/**
 *
 * @author Pieter De Rycke
 */
@SuppressWarnings("serial")
public class COMException extends Exception {

    public static final long S_OK = 0x00000000L;

    public static final long E_ABORT = 0x80004004L;

    public static final long E_ACCESSDENIED = 0x80070005L;

    public static final long E_FAIL = 0x80004005L;

    public static final long E_HANDLE = 0x80070006L;

    public static final long E_INVALIDARG = 0x80070057L;

    public static final long E_NOINTERFACE = 0x80004002L;

    public static final long E_NOTIMPL = 0x80004001L;

    public static final long E_OUTOFMEMORY = 0x8007000EL;

    public static final long E_POINTER = 0x80004003L;

    public static final long E_UNEXPECTED = 0x8000FFFFL;

    public static final int FACILITY_WIN32 = 7;

    public static final int FACILITY_WINDOWS = 8;

    public static final int FACILITY_WPD = 42;

    private int hresult;

    public COMException(String message, int hresult) {
        super(message);
        this.hresult = hresult;
    }

    public COMException(int hresult) {
        this.hresult = hresult;
    }

    public long getHresult() {
        return hresult & 0xffffffffl;
    }

    public int getSeverityCode() {
        return (hresult >> 31) & 0x1;
    }

    public int getFacilityCode() {
        return (hresult >> 16) & 0x1fff;
    }

    public int getErrorCode() {
        return hresult & 0xffff;
    }

    public String toString() {
        return this.getClass().getCanonicalName() + ": " + getMessage() + " (0x" + Long.toHexString(getHresult()) + ")";
    }
}
