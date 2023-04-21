package org.ocd.alert;

public interface IWorkInProgressAlert {

    /**
   * Update the Title of the Alert
   * @param pTitle
   */
    public void setTitle(String pTitle);

    /**
   * Update the Message of the Alert
   * @param pMessage
   */
    public void setMessage(String pMessage);

    /**
   * Dispose of the Alert
   */
    public void dispose();
}
