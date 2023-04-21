package net.sf.telescope.communications;

import java.io.IOException;
import java.net.UnknownHostException;
import net.sf.telescope.communications.client.Client;
import net.sf.telescope.communications.client.audio.AudioListener;
import net.sf.telescope.communications.client.video.VideoListener;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class TelescopeCommunications extends Plugin {

    public static final String PLUGIN_ID = "net.sf.telescope.communications";

    private static TelescopeCommunications plugin;

    /**
	 * The constructor
	 */
    public TelescopeCommunications() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
    public static TelescopeCommunications getDefault() {
        return plugin;
    }

    public boolean start(String audioServer, int audioServerPort, String login) throws UnknownHostException, IOException {
        return Client.getInstance().start(login, audioServer, audioServerPort);
    }

    public void disconnectAudioCall() {
        Client.getInstance().stop();
    }

    public String getActiveAudioContact() {
        return Client.getInstance().getActiveContact();
    }

    public void connectAudioCall(String username) {
        Client.getInstance().audioRequest(username);
    }

    public void connectVideoCall(String username) {
        Client.getInstance().videoRequest(username);
    }

    public void setAudioListener(AudioListener listener) {
        Client.getInstance().setAudioListener(listener);
    }

    public void setVideoListener(VideoListener listener) {
        Client.getInstance().setVideoListener(listener);
    }
}
