package javax.agent.service.directory;

import javax.agent.service.ServiceFailure;

/**
 * The DirectoryFailure is thrown when a directory operation fails
 * due to a problem with the directory service infrastructure.
 *
 * @author A. Spydell
 * @since 1.0
 */
public class DirectoryFailure extends ServiceFailure {

    /** 
    *Create a DirectoryFailure with no message. 
    *
    */
    public DirectoryFailure() {
        super();
    }

    /** 
    * Create a DirectoryFailure with the given message. 
    *
    *
    * @param msg the detail message.
    */
    public DirectoryFailure(String msg) {
        super(msg);
    }

    /**
    * Create a DirectoryFailure with the given message and
    * embedded exception.
    *
    * @param msg the detail message.
    * @param exp the causing exception.
    */
    public DirectoryFailure(String msg, Exception exp) {
        super(msg, exp);
    }
}
