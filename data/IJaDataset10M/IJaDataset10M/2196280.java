#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId};

import ${package}.api.*;
import com.sun.sgs.app.*;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServer
implements Serializable, AppListener, ManagedObject
{
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(MyServer.class.getName());

    public void initialize(Properties props) {
        logger.info("MyServer initialized");
    }

    public ClientSessionListener loggedIn(ClientSession session) {
        logger.log(Level.INFO, String.format("Client logged in: %s${symbol_escape}n", session.getName()));
        return MyServerUser.loggedIn(session);
    }
}