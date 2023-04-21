package com.protomatter.pas.jndi.spi;

import javax.naming.*;
import javax.naming.spi.*;
import com.protomatter.pas.*;
import com.protomatter.pas.jndi.*;

/**
 *  An implementation of the <tt>javax.naming.InitialContextFactory</tt>
 *  that can connect with the JNDI tree in PAS.
 *
 *  @see javax.naming.InitialContextFactory
 */
public class PASInitialContextFactory implements InitialContextFactory {

    /**
   *  Default constructor.
   */
    public PASInitialContextFactory() {
        super();
    }

    /**
   *  Get a context based on the parameters specified in
   *  <tt>environment</tt>.  Currently, the environment is
   *  ignored.
   */
    public Context getInitialContext(java.util.Hashtable environment) throws NamingException {
        return new PASContext();
    }
}
