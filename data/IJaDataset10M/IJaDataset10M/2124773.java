package edu.upenn.cis.propbank_shen;

/**
   This exception is thrown for processing corrupt
   data in propbank formats.
   @author Scott Cotton
 */
public class CorruptDataException extends Exception {

    public CorruptDataException(String msg) {
        super(msg);
    }
}
