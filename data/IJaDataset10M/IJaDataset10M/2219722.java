package com.frinika.project;

import java.util.List;
import java.util.Observer;
import java.util.Observable;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComponent;
import uk.org.toot.swingui.audioui.serverui.*;
import com.frinika.global.FrinikaConfig;
import com.frinika.toot.javasoundmultiplexed.*;
import com.frinika.tootX.LatencyTesterPanel;
import uk.org.toot.audio.server.AbstractAudioServer;
import uk.org.toot.audio.server.AudioClient;
import uk.org.toot.audio.server.AudioServer;
import uk.org.toot.audio.server.AudioServerConfiguration;
import uk.org.toot.audio.server.AudioServerServices;
import uk.org.toot.audio.server.ExtendedAudioServer;
import uk.org.toot.audio.server.IOAudioProcess;
import uk.org.toot.audio.server.MultiIOJavaSoundAudioServer;
import uk.org.toot.audio.server.SwitchedAudioClient;
import static com.frinika.localization.CurrentLocale.getMessage;

public class FrinikaAudioSystem {

    private static SwitchedAudioClient mixerSwitch = new SwitchedAudioClient();

    private static FrinikaAudioServer audioServer;

    private static AudioServer realAudioServer;

    private static AudioServerConfiguration serverConfig;

    private static Object thief = null;

    private static int bufferSize;

    private static IOAudioProcess defaultOut;

    private static JFrame configureFrame;

    /**
         * Skip seeing audio outputs if neccesary (e.g. standalone wav rendering)
         */
    public static boolean usePhysicalAudioOutput = true;

    public static FrinikaAudioServer getAudioServer() {
        if (audioServer != null) return audioServer;
        try {
            System.setProperty("jjack.client.name", "Frinika");
            boolean multiplexIO = FrinikaConfig.MULTIPLEXED_AUDIO;
            if (!multiplexIO) {
                realAudioServer = new MultiIOJavaSoundAudioServer();
            } else {
                System.out.println(" WARNING USING EXPERIMENTAL MULTIPLEXED AUDIO SERVER ");
                MultiplexedJavaSoundAudioServer s = new MultiplexedJavaSoundAudioServer();
                realAudioServer = s;
                configureMultiplexed(s);
            }
            audioServer = new FrinikaAudioServer(realAudioServer);
            serverConfig = AudioServerServices.createServerConfiguration(realAudioServer);
            serverConfig.addObserver(new Observer() {

                public void update(Observable obs, Object obj) {
                    saveServerConfig();
                }
            });
            loadServerConfigPost();
            bufferSize = audioServer.createAudioBuffer("dummy").getSampleCount();
            audioServer.setClient(mixerSwitch);
            return audioServer;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(" Too frightened to carry on !!!");
            System.exit(-1);
            return null;
        }
    }

    /**
	 * This is intended for Step 1 of the SplashScreen audio server setup. This
	 * called before the audio server has been created
	 * 
	 * Create AudioServer instance.
	 * 
	 * If one already exists it is a mistake.
	 * 
	 * @return the audio server instance
	 */
    public static AudioServer getAudioServerInit() {
        assert (realAudioServer == null);
        try {
            System.setProperty("jjack.client.name", "Frinika");
            boolean multiplexIO = FrinikaConfig.MULTIPLEXED_AUDIO;
            if (!multiplexIO) {
                realAudioServer = new MultiIOJavaSoundAudioServer();
            } else {
                System.out.println(" WARNING USING EXPERIMENTAL MULTIPLEXED AUDIO SERVER ");
                MultiplexedJavaSoundAudioServer s = new MultiplexedJavaSoundAudioServer();
                realAudioServer = s;
            }
            audioServer = new FrinikaAudioServer(realAudioServer);
            serverConfig = AudioServerServices.createServerConfiguration(realAudioServer);
            serverConfig.addObserver(new Observer() {

                public void update(Observable obs, Object obj) {
                    saveServerConfig();
                }
            });
            return realAudioServer;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(" Too frightened to carry on !!!");
            System.exit(-1);
            return null;
        }
    }

    /**
	 * step 2
	 * 
	 */
    public static void intitIO() {
        loadServerConfigPost();
        bufferSize = audioServer.createAudioBuffer("dummy").getSampleCount();
        audioServer.setClient(mixerSwitch);
    }

    private static void configureMultiplexed(MultiplexedJavaSoundAudioServer s) {
        List<String> list = s.getOutDeviceList();
        Object a[] = new Object[list.size()];
        a = list.toArray(a);
        String configKey = ((ExtendedAudioServer) realAudioServer).getConfigKey();
        String configDev = null;
        configDev = FrinikaConfig.getProperty(configKey + ".outputDevice");
        System.out.println(configDev);
        Object selectedValue = null;
        for (Object ae : a) {
            if (ae.equals(configDev)) {
                selectedValue = ae;
                break;
            }
        }
        if (selectedValue == null) {
            selectedValue = JOptionPane.showInputDialog(null, getMessage("setup.select_audio_output"), "output", JOptionPane.INFORMATION_MESSAGE, null, a, a[0]);
            System.out.println("|" + configDev + "|" + selectedValue + "|");
            FrinikaConfig.setProperty(configKey + ".outputDevice", (String) selectedValue);
            FrinikaConfig.store();
        }
        s.setOutDevice((String) selectedValue);
        list = s.getInDeviceList();
        list.add(0, "NONE");
        a = new Object[list.size()];
        a = list.toArray(a);
        configDev = FrinikaConfig.getProperty(configKey + ".inputDevice");
        selectedValue = null;
        for (Object ae : a) {
            if (ae.equals(configDev)) {
                selectedValue = ae;
                break;
            }
        }
        if (selectedValue == null) {
            selectedValue = JOptionPane.showInputDialog(null, getMessage("setup.select_audio_input"), "input", JOptionPane.INFORMATION_MESSAGE, null, a, a[0]);
            FrinikaConfig.setProperty(configKey + ".inputDevice", (String) selectedValue);
            FrinikaConfig.store();
        }
        if (!((String) selectedValue).equals("NONE")) s.setInDevice((String) selectedValue);
    }

    public static String configureServerOutput() {
        if (usePhysicalAudioOutput == false) return null;
        List<String> list = realAudioServer.getAvailableOutputNames();
        String outDev = null;
        String configKey = ((ExtendedAudioServer) realAudioServer).getConfigKey();
        if (!list.isEmpty()) {
            Object a[] = new Object[list.size()];
            a = list.toArray(a);
            String configDev = null;
            configDev = FrinikaConfig.getProperty(configKey + ".output");
            System.out.println(configKey + "=" + configDev);
            if (list.size() > 1) {
                if (configDev != null) {
                    for (String s : list) {
                        if (s.equals(configDev)) {
                            outDev = s;
                        }
                    }
                }
                if (outDev == null) {
                    outDev = (String) JOptionPane.showInputDialog(null, getMessage("setup.select_audio_output"), "Output", JOptionPane.INFORMATION_MESSAGE, null, a, a[0]);
                }
            } else {
                outDev = list.get(0);
            }
        }
        FrinikaConfig.setProperty(configKey + ".output", outDev);
        return outDev;
    }

    /**
	 * 
	 * sets a new mixer.
	 * 
	 * @param mixer
	 *            new client for the server
	 * @return true if success. (fail if it is stolen)
	 */
    public static boolean installClient(AudioClient mixer) {
        if (thief != null) {
            return false;
        }
        mixerSwitch.installClient(mixer);
        return true;
    }

    /**
	 * revert to previous mixer
	 * 
	 */
    public static void revertMixer() {
        if (thief != null) {
            return;
        }
        mixerSwitch.revertClient();
    }

    public static IOAudioProcess audioOutputDialog(JFrame frame, String prompt) throws Exception {
        List<String> list = audioServer.getAvailableOutputNames();
        Object a[] = new Object[list.size()];
        a = list.toArray(a);
        Object selectedValue = JOptionPane.showInputDialog(frame, getMessage("setup.select_audio_output"), prompt, JOptionPane.INFORMATION_MESSAGE, null, a, a[0]);
        if (selectedValue == null) return null;
        IOAudioProcess o = audioServer.openAudioOutput((String) selectedValue, "output");
        if (defaultOut == null) defaultOut = o;
        return o;
    }

    public static IOAudioProcess getDefaultOutput(JFrame frame) {
        if (defaultOut == null) {
            try {
                audioOutputDialog(frame, getMessage("setup.select_default_output"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultOut;
    }

    public static IOAudioProcess audioInputDialog(JFrame frame, String prompt) throws Exception {
        List<String> list = audioServer.getAvailableInputNames();
        Object a[] = new Object[list.size()];
        a = list.toArray(a);
        Object selectedValue = JOptionPane.showInputDialog(frame, getMessage("setup.select_audio_input"), prompt, JOptionPane.INFORMATION_MESSAGE, null, a, a[0]);
        return audioServer.openAudioInput((String) selectedValue, "input");
    }

    /**
	 * 
	 * Allow user to play with server parameters.
	 * 
	 * Actually redundant because JavaSound is getFramePos is accurate
	 * 
	 */
    public static void latencyMeasureSet() {
        JFrame frame = new JFrame();
        frame.setTitle("Latency Measure/Set");
        long tootTotalLatency = ((AbstractAudioServer) realAudioServer).getTotalLatencyFrames();
        System.out.println(" Server latency = " + tootTotalLatency);
        JPanel panel = new JPanel();
        LatencyTesterPanel lpanel = new LatencyTesterPanel(frame);
        panel.add(lpanel);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
	 * This method allows you to use a measured round trip latency to estimate
	 * the extra latency not estimated directly by the audioServer.
	 * 
	 * This will estimate the hardware latency so the getTotalLatency will
	 * return a correct value even if you change audioServer bufer sizes.
	 * 
	 * @param frames
	 *            measured total round trip latency.
	 */
    public static void setTotalLatency(int frames) {
        throw new UnsupportedOperationException();
    }

    /**
	 * 
	 * Allow user to play with server parameters.
	 * 
	 */
    public static void configure() {
        if (configureFrame != null) {
            configureFrame.setVisible(true);
            return;
        }
        final JComponent ui = AudioServerUIServices.createServerUI(realAudioServer, serverConfig);
        if (ui == null) return;
        configureFrame = new JFrame();
        configureFrame.setAlwaysOnTop(true);
        configureFrame.setContentPane(ui);
        configureFrame.pack();
        configureFrame.setVisible(true);
    }

    /**
	 * 
	 * @return sample rate of the audioServer
	 */
    public static double getSampleRate() {
        return audioServer.getSampleRate();
    }

    private static void errorMessage(String msg) {
        try {
            throw new Exception(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, msg, "Frinika Message", JOptionPane.ERROR_MESSAGE);
    }

    /**
	 * This allows you to have sole ownership of the server. Whilst stolen it is
	 * not possible for any one else to use the server. The thief must return it
	 * as sone as possible. This is intended to thwart anything that tries to be
	 * helpful by automatically switching clients
	 * 
	 * @param thief
	 * @return the audioserver instance or null if the method fails.
	 */
    static AudioServer stealAudioServer(Object thief, AudioClient client) {
        if (FrinikaAudioSystem.thief != null) {
            errorMessage(" server has already been stolen by " + thief);
            return null;
        }
        installClient(client);
        FrinikaAudioSystem.thief = thief;
        return audioServer;
    }

    /**
	 * return the audio server for general use. check no one cheats by
	 * pretending they stole it !!!
	 * 
	 * @param thief
	 */
    static void returnAudioServer(Object thief) {
        if (thief != FrinikaAudioSystem.thief) {
            errorMessage(" attempt to prented to be audio server thief by " + thief + "  real thief was " + FrinikaAudioSystem.thief);
            return;
        }
        FrinikaAudioSystem.thief = null;
        mixerSwitch.revertClient();
    }

    public static int getAudioBufferSize() {
        return bufferSize;
    }

    public static void loadServerConfigPost() {
        serverConfig.applyProperties(FrinikaConfig.getProperties());
    }

    public static void saveServerConfig() {
        serverConfig.mergeInto(FrinikaConfig.getProperties());
        FrinikaConfig.store();
    }

    /**
	 * Called on exit. Put code in here to clse on any devices .
	 */
    public static void close() {
    }
}
