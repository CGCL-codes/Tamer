package com.cube42.util.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.MessageFormat;

/**
 * Class for sending feedback into the Tukati system
 *
 * @author  Matt Paulin
 * @version $Id: HttpFeedback.java,v 1.3 2003/03/12 00:28:07 zer0wing Exp $
 */
public class HttpFeedback {

    /**
     * The URL to use to provided feedback that the applet has started
     */
    private static final String APPLET_STARTED_URL = "http://vno.tukati.com/tracking/{0}/JavaRun?GUID=";

    /**
     * The URL is cancelled
     */
    private static final String APPLET_CANCELLED_URL = "http://vno.tukati.com/tracking/{0}/JavaCanceled";

    /**
     * The URL to use to when the applet encounteres an error
     */
    private static final String APPLET_ERROR_URL = "http://vno.tukati.com/tracking/{0}/JavaError?ErrorString=<{1}>";

    /**
     * The URL to use when the customer installs the redistributor
     */
    private static final String REDISTRIBUTOR_INSTALLED_URL = "http://vno.tukati.com/tracking/{0}/JavaOked";

    /**
     * The customer ID of the customer
     */
    private String customerID;

    /**
     * Constructs the HttpFeedback
     *
     * @param   customerID  The customerID
     */
    public HttpFeedback(String customerID) {
        this.customerID = customerID;
    }

    /**
     * Method called to send feedback that the applet was started
     */
    public void appletStarted() {
        try {
            String message = MessageFormat.format(HttpFeedback.APPLET_STARTED_URL, new Object[] { URLEncoder.encode(this.customerID, "UTF-8") });
            message = message + "{}";
            URL url = new URL(message);
            URLConnection con = url.openConnection();
            con.connect();
            con.getHeaderField(0);
        } catch (MalformedURLException e) {
            System.out.println("Applet error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Applet error " + e.getMessage());
        }
    }

    /**
     * Method called to send feedback that the applet is cancelled
     */
    public void appletCancelled() {
        try {
            String message = MessageFormat.format(HttpFeedback.APPLET_CANCELLED_URL, new Object[] { URLEncoder.encode(this.customerID, "UTF-8") });
            URL url = new URL(message);
            URLConnection con = url.openConnection();
            con.connect();
            con.getHeaderField(0);
        } catch (MalformedURLException e) {
            System.out.println("Applet error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Applet error " + e.getMessage());
        }
    }

    /**
     * Method called to send feedback that the applet encountered an
     * error
     *
     * @param   error   The error encountered
     */
    public void appletError(String error) {
        try {
            String message = MessageFormat.format(HttpFeedback.APPLET_ERROR_URL, new Object[] { URLEncoder.encode(this.customerID, "UTF-8"), URLEncoder.encode(error, "UTF-8") });
            URL url = new URL(message);
            URLConnection con = url.openConnection();
            con.connect();
            con.getHeaderField(0);
        } catch (MalformedURLException e) {
            System.out.println("Applet error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Applet error " + e.getMessage());
        }
    }

    /**
     * Method called when the redistributor is installed
     */
    public void redistributorInstalled() {
        try {
            String message = MessageFormat.format(HttpFeedback.REDISTRIBUTOR_INSTALLED_URL, new Object[] { URLEncoder.encode(this.customerID, "UTF-8") });
            URL url = new URL(message);
            URLConnection con = url.openConnection();
            con.connect();
            con.getHeaderField(0);
        } catch (MalformedURLException e) {
            System.out.println("Applet error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Applet error " + e.getMessage());
        }
    }
}
