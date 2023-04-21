package jode.bytecode;

/**
 * This exception is thrown, if the class file has an unknown format.
 * @author Jochen Hoenicke
 */
public class ClassFormatException extends java.io.IOException {

    public ClassFormatException(String detail) {
        super(detail);
    }

    public ClassFormatException() {
        super();
    }
}
