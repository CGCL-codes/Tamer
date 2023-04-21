package jmotor.core.ioc.converter;

/**
 * Component:
 * Description:
 * Date: 11-8-24
 *
 * @author Andy.Ai
 */
public interface ValueConverter {

    Object convert(Class<?> type, String value);
}
