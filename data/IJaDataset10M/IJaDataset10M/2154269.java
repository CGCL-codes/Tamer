package org.quelea.video;

import com.sun.jna.Native;
import java.awt.Canvas;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.quelea.Application;
import org.quelea.languages.LabelGrabber;
import org.quelea.utils.LoggerUtils;

/**
 * Factory class responsible for creating the out of process video players and
 * providing remote objects to control them.
 * @author Michael
 */
public class RemotePlayerFactory {

    private static final Logger LOGGER = LoggerUtils.getLogger();

    private static int portCounter = 5555;

    /**
     * Get a remote embedded player, pointing to an embedded player in another
     * VM.
     * @param canvas the canvas ID (got using JNA) that the other player should
     * use.
     * @return the remote embedded player.
     */
    public static RemotePlayer getEmbeddedRemotePlayer(Canvas canvas) {
        try {
            long drawable = Native.getComponentID(canvas);
            StreamWrapper wrapper = startSecondPlayerJVM(OutOfProcessEmbeddedPlayer.class, Long.toString(drawable));
            final RemotePlayer player = new RemotePlayer(wrapper);
            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    player.close();
                }
            });
            return player;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Video failed", ex);
            JOptionPane.showMessageDialog(Application.get().getMainWindow(), LabelGrabber.INSTANCE.getLabel("video.error"), LabelGrabber.INSTANCE.getLabel("video.error.title"), JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    /**
     * Get the next available port to use for the next headless player.
     * @return the next available port number.
     */
    public static int getNextPort() {
        return portCounter;
    }

    /**
     * Get a headless remote player interface to a player in a separate VM.
     * @return a headless remote player interface to a player in a separate VM.
     */
    public static RemotePlayer getHeadlessRemotePlayer() {
        try {
            StreamWrapper wrapper = startSecondPlayerJVM(OutOfProcessHeadlessPlayer.class, Integer.toString(portCounter++));
            final RemotePlayer player = new RemotePlayer(wrapper);
            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    player.close();
                }
            });
            return player;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Video failed", ex);
            JOptionPane.showMessageDialog(Application.get().getMainWindow(), LabelGrabber.INSTANCE.getLabel("video.error"), LabelGrabber.INSTANCE.getLabel("video.error.title"), JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    /**
     * Start a second JVM to control an out of process player.
     * @param clazz the type of out of process player we're launching (must be
     * a class with a main method.)
     * @param option any options to pass to VLCJ.
     * @return a stream wrapper object for controlling the second VM.
     * @throws IOException if something goes wrong.
     */
    private static StreamWrapper startSecondPlayerJVM(Class<? extends OutOfProcessPlayer> clazz, String option) throws IOException {
        String separator = System.getProperty("file.separator");
        String classpath = System.getProperty("java.class.path");
        String path = System.getProperty("java.home") + separator + "bin" + separator + "java";
        ProcessBuilder processBuilder = new ProcessBuilder(path, "-cp", classpath, "-Djna.library.path=" + System.getProperty("jna.library.path"), clazz.getName(), option);
        Process process = processBuilder.start();
        return new StreamWrapper(process.getInputStream(), process.getOutputStream());
    }
}
