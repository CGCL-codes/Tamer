package com.dcivision.mail.bean;

/**
  EmailError.java

  This class about exception.

  @author      Beyond.Qu
  @company     DCIVision Limited
  @creation date   09/06/2005
  @version     $Revision: 1.1 $
*/
public interface EmailError {

    /**
   * Tests if the error is an inline error.
   * <p>
   * An inline error should be displayed within the same view
   * and not on the error view.
   *
   * @return true if it is an inline error, false otherwise.
   */
    public boolean isInlineError();

    /**
   * Tests if the error has been displayed.
   *
   * @return true if it was displayed, false otherwise.
   */
    public boolean isDisplayed();

    /**
   * Set's the flag that stores if an error has
   * been displayed.
   *
   * @param b true if it was displayed, false otherwise.
   */
    public void setDisplayed(boolean b);

    /**
   * Returns an <tt>String[]</tt> containing the
   * keys to the locale specific error descriptions.
   *
   * @return an array of strings containing the error
   *         description keys.
   */
    public String[] getDescriptions();

    /**
   * Tests if the error has an embedded exception.
   *
   * @return true if error has an embedded exception,
   *         false otherwise.
   */
    public boolean hasException();

    /**
   * Returns a <tt>String</tt> representing the Errors
   * embedded exception stack trace.
   *
   * @return the error's embedded exception stacktrace as
   *         <tt>String</tt>.
   */
    public String getExceptionTrace();
}
