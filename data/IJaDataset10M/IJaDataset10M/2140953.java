package ftp2me;

/**
 * This exception is thrown when an error occures while
 * communicating with a server. These errors are higher
 * level than IOExceptions. For example when a server sends
 * an unexpected error code, or some other reply that cannot
 * be understood by ftp2me. If you cut the internet cable
 * while transferring a file, an IOException is more likely
 * to occur.
 *
 */
public class FtpProtocolException extends Exception {

    FtpProtocolException(String string) {
        super(string);
    }
}
