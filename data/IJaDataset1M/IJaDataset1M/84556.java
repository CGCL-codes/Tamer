package net.sourceforge.trust.wf;

public interface Parseable<T> {

    T parse(String value) throws ParseException;
}
