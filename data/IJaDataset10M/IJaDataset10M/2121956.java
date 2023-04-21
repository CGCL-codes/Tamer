package be.javamomentum.io.flatfile.errors;

/**
 * Thrown if no match is found for an input file in the re
 */
public class FlatwormInvalidRecordException extends FlatwormException {

    public FlatwormInvalidRecordException(String s) {
        super(s);
    }
}
