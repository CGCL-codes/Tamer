package com.genia.toolbox.basics.exception.technical;

import com.genia.toolbox.basics.manager.ExceptionManager;

/**
 * {@link ExceptionTranslator} for {@link WrapperException}.
 */
public class WrapperExceptionTranslator extends AbstractExceptionTranslator {

    /**
   * the {@link ExceptionManager} to use.
   */
    private ExceptionManager exceptionManager;

    /**
   * constructor.
   */
    protected WrapperExceptionTranslator() {
        super(WrapperException.class);
    }

    /**
   * convert an {@link WrapperException} to a {@link TechnicalException}.
   * 
   * @param exception
   *          the exception to convert
   * @return the converted exception
   * @see com.genia.toolbox.basics.exception.technical.IOExceptionTranslater#convertException(java.lang.Exception)
   */
    public TechnicalException convertException(Exception exception) {
        Exception causeException = null;
        do {
            causeException = (Exception) exception.getCause();
        } while (causeException instanceof WrapperException);
        return getExceptionManager().convertException(causeException);
    }

    /**
   * getter for the exceptionManager property.
   * 
   * @return the exceptionManager
   */
    public ExceptionManager getExceptionManager() {
        return exceptionManager;
    }

    /**
   * setter for the exceptionManager property.
   * 
   * @param exceptionManager
   *          the exceptionManager to set
   */
    public void setExceptionManager(ExceptionManager exceptionManager) {
        this.exceptionManager = exceptionManager;
    }
}
