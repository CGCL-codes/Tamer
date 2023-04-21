package org.unicode.unihan.parser.field;

/**
 *
 * @author Andrew
 */
public interface FieldParser<E> {

    public boolean isValid(String s);

    public E parse(String s);
}
