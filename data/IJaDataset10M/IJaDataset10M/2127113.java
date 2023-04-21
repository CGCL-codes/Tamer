package org.globus.mds;

import org.globus.common.ChainedException;

/** An Exception class for MDS interactions.

  <p><b> USING THE MDS EXEPTION CLASS </b>
  <blockquote>
  
  The intention of the MDS Exeption class is to provide a simple
  exeption mechanism for MDS related methods. This includes:

  <ol>
  <li> failure to conect to the MDS due to timeout or other problems,
  <li> failure during MDS queries,
  <li> ... .
  </ol>

  In case the Exeption is thrown by another underlaying library, it is
  the task of the user to properly deal with the exceptions. Such
  methods and exceptions can be found in JNDI and Java Networking classes.

  </blockquote>

  <p><b> Important Network related Exceptions</b>
  <blockquote>

  ...
  
  </blockquote>




  <p><b> Important JNDI Exceptions </b>
  <blockquote>
  
  ...

  </blockquote>

  <p><b> Example</b>
  <blockquote>

  See ... MDS.java

  </blockquote>
  **/
public class MDSException extends ChainedException {

    private String ldapMessage;

    public MDSException(String mdsMessage, String ldapMessage) {
        super(mdsMessage);
        this.ldapMessage = ldapMessage;
    }

    public MDSException(String detail, Throwable exception) {
        super(detail, exception);
    }

    public String getLdapMessage() {
        return ldapMessage;
    }
}
