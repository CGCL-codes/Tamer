package org.nees.rbnb;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.MalformedURLException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;

/**
 * DLinkSource takes an image stream from a D-Link DCS-900 camera and puts the
 * JPEG images into an RBNB server with timestamps.
 * 
 * @author Jason P. Hanley
 * @author Lawrence J. Miller
 */
public class DLinkSource extends MJPEGSource {

    /** the URL for the MJPEG video stream */
    private URL mjpegURL;

    /** the maximum frames per second the camera can deliver */
    private static final int MAX_FPS = 20;

    /** start DLinkSource with the given command line arguments */
    public static void main(String[] args) {
        final DLinkSource a = new DLinkSource();
        if (a.parseArgs(args)) {
            a.start();
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                a.stop();
            }
        });
    }

    protected String getCVSVersionString() {
        return ("$LastChangedDate: 2008-04-15 20:12:19 -0400 (Tue, 15 Apr 2008) $\n" + "$LastChangedRevision: 36 $" + "$LastChangedBy: ljmiller.ucsd $" + "$HeadURL: http://oss-dataturbine.googlecode.com/svn/trunk/apps/oss-apps/src/org/nees/rbnb/DLinkSource.java $");
    }

    protected Options setOptions() {
        Options opt = setBaseOptions(new Options());
        opt.addOption("U", true, "username (no default)");
        opt.addOption("P", true, "password  (no default)");
        return opt;
    }

    protected boolean setArgs(CommandLine cmd) {
        if (!setBaseArgs(cmd)) return false;
        if (!cmd.hasOption('A') || cmd.getOptionValue('A') == null) {
            System.err.println("Video camera host name is required.");
            printUsage();
            return false;
        } else {
            hostName = cmd.getOptionValue('A');
        }
        String username = null;
        if (cmd.hasOption('U')) {
            String a = cmd.getOptionValue('U');
            if (a != null) username = a;
        }
        String password = null;
        if (cmd.hasOption('P')) {
            String a = cmd.getOptionValue('P');
            if (a != null) password = a;
        }
        if ((username != null) && (password != null)) {
            Authenticator.setDefault(new PasswordAuthenticator(username, password));
        }
        try {
            mjpegURL = new URL("http://" + getHostName() + "/video.cgi");
        } catch (MalformedURLException e) {
            System.err.println("Unable to construct the camera URL.");
            return false;
        }
        return true;
    }

    protected int getFPS() {
        return MAX_FPS;
    }

    protected URL getMJPEGURL() {
        return mjpegURL;
    }

    /**
     * Create an Anthenticator for URL access when a username and password is
     * required.
     */
    private class PasswordAuthenticator extends Authenticator {

        PasswordAuthentication passwordAuthentication;

        public PasswordAuthenticator(String username, String password) {
            if ((username != null) && (password != null)) {
                passwordAuthentication = new PasswordAuthentication(username, password.toCharArray());
            }
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return passwordAuthentication;
        }
    }
}
