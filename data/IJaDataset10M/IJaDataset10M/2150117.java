package edu.sdsc.cleos;

import org.nees.rbnb.MJPEGSource2;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.MalformedURLException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;

/**
 * AxisSource
 * @author Jason P. Hanley
 * @author Terry E. Weymouth
 * @author Lawrence J. Miller
 * 
 * adopted from org.nees.buffalo.video.AxisSource by Jason T. Hanley
 * 
 * AxisSource - takes an image stream from an axis box and put the
 * JPEG images into RBNB with timestamps. 
 * @see org.nees.buffalo.axis.AxisSource
 */
public class IQEyeSource extends MJPEGSource2 {

    private static final int DEFAULT_CAMERA_FPS = 30;

    private int cameraFPS = DEFAULT_CAMERA_FPS;

    private double cameraFreqyency = 0.2;

    private static final int DEFAULT_CAMERA_NUMBER = 1;

    private int cameraNumber = DEFAULT_CAMERA_NUMBER;

    /** the URL for the MJPEG video stream */
    private URL mjpegURL;

    private double cameraFrequency = 0.2;

    /** the maximum frames per second the camera can deliver */
    public static void main(String[] args) {
        final IQEyeSource a = new IQEyeSource();
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
        return ("$LastChangedDate: 2008-04-15 20:12:19 -0400 (Tue, 15 Apr 2008) $\n" + "$LastChangedRevision: 36 $" + "$LastChangedBy: ljmiller.ucsd $" + "$HeadURL: http://oss-dataturbine.googlecode.com/svn/trunk/apps/oss-apps/src/edu/sdsc/cleos/IQEyeSource.java $");
    }

    protected URL getMJPEGURL() {
        return mjpegURL;
    }

    protected int getFPS() {
        return this.cameraFPS;
    }

    protected Options setOptions() {
        Options opt = setBaseOptions(new Options());
        opt.addOption("U", true, "username (no default)");
        opt.addOption("P", true, "password  (no default)");
        opt.addOption("f", true, "frame rate, HZ, *30");
        opt.addOption("n", true, "camera number *" + DEFAULT_CAMERA_NUMBER);
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
        if (cmd.hasOption('f')) {
            String a = cmd.getOptionValue('f');
            if (a != null) try {
                Integer i = new Integer(a);
                int value = i.intValue();
                cameraFPS = value;
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter a numeric value for frame rate (-f option). " + a + " is not valid!");
                return false;
            }
        }
        if (cmd.hasOption('n')) {
            String a = cmd.getOptionValue('n');
            if (a != null) try {
                Integer i = new Integer(a);
                int value = i.intValue();
                cameraNumber = value;
            } catch (NumberFormatException nfe) {
                System.out.println("Please ensure to enter a numeric value for -n option. " + a + " is not valid!");
                return false;
            }
        }
        try {
            String cameraURLString = "http://" + getHostName() + "/now.jpg";
            cameraURLString += "?snap=spuchn=" + Double.toString(cameraFrequency);
            mjpegURL = new URL(cameraURLString);
            System.out.println(cameraURLString);
        } catch (MalformedURLException e) {
            System.err.println("Unable to construct the camera URL.");
            return false;
        }
        return true;
    }

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
