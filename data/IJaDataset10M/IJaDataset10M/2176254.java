package org.mitre.mrald.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;

/**
 *  This class looks for a file located at the location specified in the
 *  config.properties file with property "otherDbProps". If a file is found, the
 *  properties specified are added to the Properties appropriately.
 *
 *@author     jchoyt
 *@created    January 16, 2004
 */
public class OtherDbPropsSubscriber implements JdbcPropertySubscriber {

    /**
     *  Important: The constructor for implementations of this class should add
     *  themselves to the JdbcPropertyPublisher class in this package.
     */
    public OtherDbPropsSubscriber() {
        JdbcPropertyPublisher.addSubscriber(this);
    }

    /**
     *  The implementation of this method should add properties to the
     *  Properties arguement that are to be passed to the JDBC Connection object
     *  via the MraldConnection constructor used in the OutputManager class.<p>
     *
     *  Note that this method is intended to change the Properties object passed
     *  to it by the calling class.
     *
     *@param  msg   This parameter is not needed by this implementation.
     *@param  prop  The Properties object to be passed to the MraldConnection
     *      constructor
     */
    public void notify(HttpServletRequest request, Properties prop) {
        Properties info = new Properties();
        String otherPropFilename = Config.getProperty("otherDbProps");
        if (otherPropFilename == null || otherPropFilename.equals("")) {
            return;
        }
        File otherProps = new File(otherPropFilename);
        try {
            InputStream in = new FileInputStream(otherProps);
            info.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            throw new MraldError(e);
        }
        prop.putAll(info);
    }

    /**
     *  This method is called when the Subscriber is forcefully unsubscribed
     *  from the Publisher it is attached to. This is primarily so the class can
     *  re-subscribe immediately. If this is not necessary, an empty
     *  implementation is recommended.
     */
    public void unsubscribed() {
        JdbcPropertyPublisher.addSubscriber(this);
    }
}
