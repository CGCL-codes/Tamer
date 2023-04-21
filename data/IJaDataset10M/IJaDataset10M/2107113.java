package jonelo.jacksum.ui;

/**
 * This exception is thrown if the Meta Information has been generated
 * by a later version of Jacksum.
 */
public class MetaInfoVersionException extends Exception {

    MetaInfoVersionException(String s) {
        super(s);
    }
}
