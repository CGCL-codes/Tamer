package net.sourceforge.pebble.api.confirmation;

import javax.servlet.http.HttpServletRequest;

/**
 * Represents an abstraction of the various ways that confirmations can happen.
 *
 * @author    Simon Brown
 */
public interface ConfirmationStrategy {

    /**
   * Called before showing the confirmation page.
   *
   * @param request   the HttpServletRequest used in the confirmation
   */
    public void setupConfirmation(HttpServletRequest request);

    /**
   * Gets the URI of the confirmation page.
   *
   * @return  a URI, relative to the web application root.
   */
    public String getUri();

    /**
   * Called to determine whether confirmation was successful.
   *
   * @param request   the HttpServletRequest used in the confirmation
   * @return  true if the confirmation was successful, false otherwise
   */
    public boolean isConfirmed(HttpServletRequest request);
}
