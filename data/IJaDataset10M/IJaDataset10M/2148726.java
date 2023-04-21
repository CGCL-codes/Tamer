package org.nomadpim.core.util.text;

public interface IParser<T> {

    T parse(String value) throws ParseException;
}
