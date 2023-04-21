package com.genia.toolbox.basics.exception.technical;

/**
 * an interface that allow to automatically convert {@link Exception} to
 * {@link TechnicalException}.
 */
public interface ExceptionTranslator {

    /**
   * returns whether this translater handle the given class of exceptions.
   * 
   * @param exceptionClass
   *          the class of exception
   * @return whether this translater handle the given class of exceptions.
   */
    public abstract boolean handle(Class<? extends Exception> exceptionClass);

    /**
   * convert an {@link Exception} to a {@link TechnicalException}.
   * 
   * @param exception
   *          the exception to convert
   * @return the converted exception
   */
    public abstract TechnicalException convertException(Exception exception);
}
