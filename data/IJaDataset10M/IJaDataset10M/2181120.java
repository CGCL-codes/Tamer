package org.vramework.commons.datatypes.converters;

import java.util.Locale;
import org.vramework.commons.datatypes.converters.exceptions.ConverterAlreadyRegisteredException;
import org.vramework.commons.datatypes.converters.exceptions.ConverterIllegalTypeException;
import org.vramework.commons.datatypes.converters.exceptions.ConverterNotRegisteredException;

/**
 * Registry for {@link IConverter}s. <br />
 * Implementors should choose an efficient way to look up converters, e.g. NOT a use as String representation and its
 * hashcode to build a hashmap. (String concatenation and String::hashCode() are expensive). A better way would be to
 * use an identity hashmap of identity hashmaps where the first level contains the source type and the second level the
 * target type as key.<br />
 * <strong>Note:</strong> One converter can be used for several type conversions. E.g.: The same converter for
 * {@link java.lang.Number} to {@link java.lang.String} and for {@link java.math.BigDecimal} to {@link java.lang.String}
 * . <br />
 * It is left to implementors if the lookup ({@link #getConverter(Class, Class, boolean)}) finds only exactly matching
 * converters or also converters whose {@link IConverter#getSourceType()} is assignable from the sourceClass. (In other
 * words: The converter lookup works for subclasses, too.) <br />
 * An exact matching lookup is more efficient because can e.g. be done via the identity hashmap mechanism descibed
 * above.
 * 
 * @see IConverter
 * @author tmahring
 */
public interface IConverterRegistry {

    /**
   * Registers the converter. <br />
   * 
   * @param converter
   * @param throwIfAlreadyRegisterd
   *          - true: Throw exception if converter already registered.
   * @throws ConverterAlreadyRegisteredException
   *           if converter already registered and #throwIfAlreadyRegisterd == true .
   */
    void registerConverter(IConverter converter, boolean throwIfAlreadyRegisterd) throws ConverterAlreadyRegisteredException;

    /**
   * Registers the converter like {@link #registerConverter(IConverter, boolean)} but registers it for the passed
   * classes. <br />
   * Remeber (see class comment): Convertes can be used for several classes.
   * 
   * @param converter
   * @param sourceType
   *          The source type the converter will handle.
   * @param targetType
   *          The target type the converter will handle.
   * @param throwIfAlreadyRegisterd
   *          - true: Throw exception if converter already registered.
   * @throws ConverterAlreadyRegisteredException
   *           if converter already registered and #throwIfAlreadyRegisterd == true .
   * @throws ConverterIllegalTypeException
   *           if the sourceType is not {@link IConverter#getSourceType()} or subclass or if targetType is not
   *           {@link IConverter#getTargetType()} or subclass.
   */
    void registerConverter(IConverter converter, Class<?> sourceType, Class<?> targetType, boolean throwIfAlreadyRegisterd) throws ConverterAlreadyRegisteredException, ConverterIllegalTypeException;

    /**
   * Unregisters the converter
   * 
   * @param converter
   * @param throwIfNotFound
   *          - true: Throw exception if converter not registered.
   * @throws ConverterNotRegisteredException
   *           if #throwIfNotFound == true and converter not registered
   */
    void deregisterConverter(IConverter converter, boolean throwIfNotFound) throws ConverterNotRegisteredException;

    /**
   * Returns the converter matching the passed classes.
   * 
   * @param <T>
   * @param sourceClass
   * @param targetClass
   * @param throwIfNotFound
   *          true: Throw exception if converter not registered.
   * @return The matching converter.
   */
    <T> IConverter getConverter(Class<?> sourceClass, Class<T> targetClass, boolean throwIfNotFound);

    /**
   * Converts the passed object to the passed type. Convenience for (@link #getConverter(Class, Class, boolean)) and
   * {@link IConverter#convert(Object)}.
   * 
   * @param <T>
   * @param toConvert
   * @param targetType
   * @param locale 
   * @param throwIfNotFound
   *          true: Throw exception if converter not registered.
   * @return The converted object or null if the converter is not found and #throwIfNotFound == false.
   * @throws ConverterIllegalTypeException
   *           see {@link IConverter#convert(Object)}
   * @throws ConverterNotRegisteredException
   *           if #throwIfNotFound == true and converter not registered
   */
    <T> T convert(Object toConvert, Class<T> targetType, Locale locale, boolean throwIfNotFound) throws ConverterIllegalTypeException, ConverterNotRegisteredException;

    /**
   * Same as {@link #convert(Object, Class, Locale, boolean)} with a null Locale.
   * 
   * @param <T>
   * @param toConvert
   * @param targetType
   * @param throwIfNotFound
   * @return The converted object.
   * @throws ConverterIllegalTypeException
   * @throws ConverterNotRegisteredException
   */
    <T> T convert(Object toConvert, Class<T> targetType, boolean throwIfNotFound) throws ConverterIllegalTypeException, ConverterNotRegisteredException;

    /**
   * Registers default converters.
   */
    void registerDefaultConverters();
}
