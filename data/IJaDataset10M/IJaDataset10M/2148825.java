package ru.satseqsys.parser;

public interface IParser<T> {

    public boolean matches(String message);

    public T parse() throws Exception;
}
