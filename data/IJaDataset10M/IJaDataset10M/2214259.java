package de.mogwai.kias.convertor;

public class ConvertorException extends Exception {

    public ConvertorException(Exception e) {
        super(e);
    }

    public ConvertorException(String aMessage) {
        super(aMessage);
    }
}
