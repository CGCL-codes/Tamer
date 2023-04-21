package au.gov.qld.dnr.dss.v1.init;

/**
 * Directory Repository exception.
 */
public class DirectoryRepositoryException extends RepositoryException {

    public static final int DIRECTORY_DOES_NOT_EXIST = 1000;

    public static final int DIRECTORY_READ_ACCESS_DENIED = 1100;

    public static final int DIRECTORY_WRITE_ACCESS_DENIED = 1150;

    public static final int DIRECTORY_IS_A_FILE = 1200;

    public static final int CANNOT_READ_DIRECTORY = 1300;

    public static final int CANNOT_WRITE_DIRECTORY = 1400;

    public static final int ALREADY_EXISTS = 1500;

    public static final int MAKE_DIRECTORY_FAILED = 1600;

    Object source = null;

    Object extra = null;

    /**
     * A repository exception.
     *
     * @param type the exception type.
     */
    public DirectoryRepositoryException(int type) {
        this(type, null);
    }

    public DirectoryRepositoryException(int type, Object source) {
        this(type, source, null);
    }

    public DirectoryRepositoryException(int type, Object source, Object extra) {
        super(type);
        this.source = source;
        this.extra = extra;
    }

    public Object getSource() {
        return source;
    }

    public Object getExtra() {
        return extra;
    }
}
