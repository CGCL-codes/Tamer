package jaxlib.conversion;

/**
 * Takes an input object and converts it to another object of an implementation specific type.
 * <p>
 * Where possible your code should use the {@link ConvertableType} annotation, rather than implementing
 * {@code Converter} classes. For latter you will have to deal with classpaths and {@link java.util.ServiceLoader}.
 * </p>
 *
 * @see ConverterService
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: Converter.java 2670 2008-11-03 14:54:33Z joerg_wassmer $
 */
public interface Converter<T> {

    public T convert(ConversionEngine engine, Object src) throws Exception;
}
