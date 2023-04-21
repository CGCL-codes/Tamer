package com.genia.toolbox.basics.exception.technical;

import org.springframework.core.Ordered;

/**
 * default implementation of {@link ExceptionTranslator}.
 */
public class DefaultExceptionTranslator implements Ordered, ExceptionTranslator {

    /**
   * getter for the order property. This property allow to sort the
   * {@link ExceptionTranslator}.
   * 
   * @return the order
   * @see org.springframework.core.Ordered#getOrder()
   */
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    /**
   * returns whether this translater handle the given class of exceptions.
   * 
   * @param exceptionClass
   *          the class of exception
   * @return whether this translater handle the given class of exceptions.
   * @see com.genia.toolbox.basics.exception.technical.IOExceptionTranslator#handle(java.lang.Class)
   */
    public boolean handle(Class<? extends Exception> exceptionClass) {
        return true;
    }

    /**
   * convert an {@link Exception} to a {@link TechnicalException}.
   * 
   * @param exception
   *          the exception to convert
   * @return the converted exception
   * @see com.genia.toolbox.basics.exception.technical.ExceptionTranslator#convertException(java.lang.Exception)
   */
    public TechnicalException convertException(Exception exception) {
        return new TechnicalException(exception);
    }
}
