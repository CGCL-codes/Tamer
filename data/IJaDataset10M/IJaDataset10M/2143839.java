package uk.gov.dti.og.fox.security;

import uk.gov.dti.og.fox.FoxResponse;

/**
 * Structure for communicating authentication information
 */
public interface AuthenticationDescriptor {

    /**
   * @return a string representation of the session an authentication action operated on
   */
    public String getSessionId();

    /**
   * @return result code from an authentication action, e.g 'VALID', 'SUCCESS', 'ERROR'
   */
    public String getCode();

    /**
   * @return a detailed description/message of the result from an authentication action
   */
    public String getMessage();

    /**
   * @return a response associated with an authentication action that could be used for overriding the default response
   */
    public FoxResponse getFoxResponse();

    /**
   * @return the login id of an authenticated user
   */
    public String getSessionLoginId();
}
