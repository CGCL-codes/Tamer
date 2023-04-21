package alt.viiigine.formats.utils;

/**
 * @author Dmitry S. Vorobiev
 */
public interface LZSArchiveError {

    public static final int E_OK = 0x00000000;

    public static final int E_EOF = 0x00000001;

    public static final int E_OUT_OF_RANGE = 0x00000002;

    public static final int E_NO_MORE_FILES = 0x00000100;

    public static final int E_UNKNOWN = 0xffffffff;
}
