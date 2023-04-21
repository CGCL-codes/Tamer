package org.modss.facilitator.shared.repository;

/**
 * Repository Exception.
 */
public class RepositoryException extends Exception {

    public static final int UNSPECIFIED = 500;

    public static final int REPOSITORY_DOES_NOT_EXIST = 1000;

    public static final int REPOSITORY_READ_ACCESS_DENIED = 1100;

    public static final int REPOSITORY_WRITE_ACCESS_DENIED = 1150;

    public static final int EXISTS_BUT_IS_NOT_A_REPOSITORY = 1200;

    public static final int REPOSITORY_CANNOT_READ_CONTENTS = 1300;

    public static final int REPOSITORY_CANNOT_WRITE_CONTENTS = 1400;

    public static final int REPOSITORY_ALREADY_EXISTS = 1500;

    public static final int REPOSITORY_CREATE_FAILED = 1600;

    public static final int DATA_STREAM_CREATE_FAILED = 1700;

    public static final int DATA_STREAM_OPEN_FAILED = 1800;

    public static final int CANNOT_GENERATE_URL = 3000;

    int type;

    Object source = null;

    /**
     * A repository exception.
     *
     * @param type the exception type.
     * @param msg a message.
     */
    public RepositoryException(int type, String msg) {
        this(type, msg, null);
    }

    /**
     * A repository exception.
     *
     * @param type the exception type.
     * @param msg a message.
     * @param source the source of the exception.
     */
    public RepositoryException(int type, String msg, Object source) {
        super(msg);
        this.type = type;
        this.source = source;
    }

    /**
     * Provide the source object associated with this exception.
     *
     * @return the source object.
     */
    public Object getSource() {
        return source;
    }

    /**
     * Provide the type of this exception.
     *
     * @return the type of exception.
     */
    public int getType() {
        return type;
    }
}
