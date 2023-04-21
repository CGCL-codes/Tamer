package net.sf.ediknight;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;
import org.xml.sax.ContentHandler;

/**
 * Defines a factory API that enables applications to configure and
 * obtain a parser.
 * <p>
 * An implementation of the ParserFactory class is NOT guaranteed to
 * be thread safe. It is up to the user application to make sure about the
 * use of the ParserFactory from more than one thread.
 * Alternatively the application can have one instance of the
 * ParserFactory per thread. An application can use the same instance
 * of the factory to obtain one or more instances of the parser
 * provided the instance of the factory isn't being used in more than one
 * thread at a time.
 * <p>
 * The static newInstance method returns a new concrete implementation of
 * this class.
 *
 * @param <E> a format
 *
 * @author Holger Joest
 */
public abstract class ParserFactory<E extends Format> {

    /**
     *
     */
    protected ParserFactory() {
    }

    /**
     * Creates a new instance of a Parser using the currently
     * configured factory parameters.
     *
     * @return the parser
     * @throws ConfigurationException if there is a configuration error
     */
    public abstract Parser<E> newParser() throws ConfigurationException;

    /**
     * Returns if this factory supports the given format.
     *
     * @param format the format
     * @return true if the format is supported
     */
    public final boolean isFormatSupported(String format) {
        FormatDefinition definition = getFormatClass().getAnnotation(FormatDefinition.class);
        if (definition.name().equals(format)) {
            return true;
        }
        return false;
    }

    /**
     * Returns the format class of this parser factory.
     *
     * @return the format class
     */
    protected abstract Class<? extends Format> getFormatClass();

    /**
     * Returns an output content handler.
     *
     * @param out an output stream
     * @return the content handler
     */
    public abstract ContentHandler getContentHandler(OutputStream out);

    /**
     * Obtain all available parser factories.
     *
     * @return an iterator of parser factories
     * @throws ConfigurationException if there's a serious configuration problem
     */
    public static Iterator<ParserFactory<Format>> findParserFactories() throws ConfigurationException {
        return findParserFactories(null);
    }

    /**
     * Obtain all available parser factories.
     *
     * @param properties properties used instead of the system properties
     * @return an iterator of parser factories
     * @throws ConfigurationException if there's a serious configuration problem
     */
    @SuppressWarnings("unchecked")
    public static Iterator<ParserFactory<Format>> findParserFactories(Properties properties) throws ConfigurationException {
        return (Iterator) FactoryFinder.findAll("net.sf.ediknight.ParserFactory", null, properties);
    }

    /**
     * Obtain an instance of a parser factory that handles the specified
     * format.
     *
     * @param format the format to parse
     * @return the parser factory
     * @throws ConfigurationException if there's a serious configuration problem
     */
    public static ParserFactory<Format> findParserFactory(String format) throws ConfigurationException {
        Iterator<ParserFactory<Format>> it = ParserFactory.findParserFactories();
        while (it.hasNext()) {
            ParserFactory<Format> next = it.next();
            if (next.isFormatSupported(format)) {
                return next;
            }
        }
        return null;
    }

    /**
     * Obtain an instance of a parser factory that handles the specified
     * format.
     *
     * @param format the format to parse
     * @param <S> the format type
     * @return the parser factory
     * @throws ConfigurationException if there's a serious configuration problem
     */
    @SuppressWarnings("unchecked")
    public static <S extends Format> ParserFactory<S> findParserFactory(Class<S> format) throws ConfigurationException {
        Class<?> factoryClass = format.getAnnotation(FormatDefinition.class).factory();
        Iterator<ParserFactory<Format>> it = ParserFactory.findParserFactories();
        while (it.hasNext()) {
            ParserFactory<Format> next = it.next();
            if (next.getClass().equals(factoryClass)) {
                return (ParserFactory<S>) next;
            }
        }
        return null;
    }
}
