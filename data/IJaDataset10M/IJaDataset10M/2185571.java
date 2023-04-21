package net.sf.table4gwt.client.renderer.cell.formatter;

public interface Formatter<T> {

    T parse(String s);

    String format(T obj);
}
