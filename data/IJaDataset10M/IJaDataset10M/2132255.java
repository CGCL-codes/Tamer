package org.wwweeeportal.util.convert;

import java.io.*;
import org.springframework.core.convert.*;
import org.springframework.core.convert.converter.*;
import org.wwweeeportal.util.*;

/**
 * This class serves the same purpose as {@link ConversionFailedException}, except exposes the
 * {@linkplain #getConverter() converter} and {@linkplain #getValue() value} which caused it. This class would extend
 * {@link ConversionFailedException} if it weren't final, so instead it {@linkplain #getConversionFailedCause() wraps}
 * it.
 */
public class ConverterInvocationFailedException extends ConversionException {

    /**
   * @see Serializable
   */
    private static final long serialVersionUID = 1L;

    /**
   * The {@link Converter} which caused this exception.
   */
    protected final Converter<?, ?> converter;

    /**
   * The source Object being {@linkplain Converter converted} when this exception occurred.
   */
    protected final Object value;

    /**
   * Construct a new <code>ConverterInvocationFailedException</code>.
   * 
   * @param cfe The {@link ConversionFailedException} which caused this one.
   * @param converter The {@link Converter} which caused this exception.
   * @param value The source Object being {@linkplain Converter converted} when this exception occurred.
   */
    protected ConverterInvocationFailedException(final ConversionFailedException cfe, final Converter<?, ?> converter, final Object value) {
        super((cfe != null) ? (cfe.getClass().getSimpleName() + ": " + getSimpleMessage(cfe)) : null, cfe);
        this.converter = converter;
        this.value = value;
        return;
    }

    /**
   * Get the {@link ConversionFailedException} message without it's {@linkplain ConversionFailedException#getCause()
   * cause} appended, and with a {@linkplain MiscUtil#mkSimpleMessage(String, Throwable) simplified message}. This
   * method is a total hack, used to prevent the exponentially long messages spring generates for deep cause chains.
   * 
   * @param cfe The exception to retrieve the message from.
   * @return The message.
   */
    protected static final String getSimpleMessage(final ConversionFailedException cfe) {
        if (cfe == null) return null;
        String message = cfe.getMessage();
        if (message == null) return null;
        final int primaryMessageTerminatorIndex = message.indexOf("; nested exception is ");
        if (primaryMessageTerminatorIndex >= 0) message = message.substring(0, primaryMessageTerminatorIndex);
        message = MiscUtil.mkSimpleMessage(message, cfe);
        return message;
    }

    @Override
    public String getMessage() {
        return MiscUtil.mkSimpleMessage(super.getMessage(), this);
    }

    /**
   * Get the {@link ConversionFailedException} which caused this one.
   * 
   * @return The cause of this exception.
   * @see #getCause()
   */
    public ConversionFailedException getConversionFailedCause() {
        return (ConversionFailedException) getCause();
    }

    /**
   * Get the {@link Converter} which caused this exception.
   * 
   * @return The converter.
   */
    public Converter<?, ?> getConverter() {
        return converter;
    }

    /**
   * Get the source Object being {@linkplain Converter converted} when this exception occurred.
   * 
   * @return The source Object.
   */
    public Object getValue() {
        return value;
    }

    /**
   * Return the source type we tried to convert the value from.
   * 
   * @return The source type.
   */
    public TypeDescriptor getSourceType() {
        final ConversionFailedException cause = getConversionFailedCause();
        return (cause != null) ? getConversionFailedCause().getSourceType() : null;
    }

    /**
   * Returns the target type we tried to convert the value to.
   * 
   * @return The target type.
   */
    public TypeDescriptor getTargetType() {
        final ConversionFailedException cause = getConversionFailedCause();
        return (cause != null) ? getConversionFailedCause().getTargetType() : null;
    }
}
