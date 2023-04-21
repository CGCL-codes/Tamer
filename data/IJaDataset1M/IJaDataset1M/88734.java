package x360mediaserver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ConfigWeb {

    private static ArrayList<ConfiguratorElement> elements = new ArrayList<ConfiguratorElement>();

    private static int nextConfiguratorElementID = 1;

    public static void start() {
        addElements();
    }

    /**
     * When you modify something in the webpage... it happens here.
     * 
     * @param formdata
     * @param listener
     */
    public static void addElement(String formdata, ConfiguratorListener listener) {
        ConfiguratorElement blah = new ConfiguratorElement(nextConfiguratorElementID++ + ".cgi", formdata, listener);
        elements.add(blah);
    }

    /**
     * This sets up the webpage to configure this....
     */
    public static void addElements() {
        addElement("Music Dir: <input type=\"text\" name=\"musicdir\">", new ConfiguratorListener() {

            public void process(HashMap<String, String> formdata) {
                String musicDir = formdata.get("musicdir");
                if (musicDir != null) {
                    File file = new File(musicDir);
                    if (file.exists() && file.isDirectory()) {
                        Config.addMusicDir(musicDir);
                    }
                }
            }
        });
        addElement("iTunesFile Dir: <input type=\"text\" name=\"itunesfile\">", new ConfiguratorListener() {

            public void process(HashMap<String, String> formdata) {
                String iTunesFile = formdata.get("itunesfile");
                if (iTunesFile != null) {
                    Config.setiTunesFile(iTunesFile);
                }
            }
        });
        addElement("Friendly Name: <input type=\"text\" name=\"friendlyname\">", new ConfiguratorListener() {

            public void process(HashMap<String, String> formdata) {
                String friendlyName = formdata.get("friendlyname");
                if (friendlyName != null) {
                    Config.setFriendlyName(friendlyName);
                }
            }
        });
        addElement("Output Format:" + "<SELECT NAME=\"format\">" + "<OPTION VALUE=\"mp3\">MP3" + "<OPTION VALUE=\"pcm\">PCM" + "</SELECT>", new ConfiguratorListener() {

            public void process(HashMap<String, String> formdata) {
                System.out.println("Doing pcm config");
                String formatString = formdata.get("format");
                if (formatString != null) {
                    debug("formatString !=null");
                    if (formatString.equals("mp3")) Config.setPCMoption(false); else if (formatString.equals("pcm")) Config.setPCMoption(true);
                }
            }
        });
        addElement("Stream Name:<input type=\"text\" name=\"streamname\"><br>Stream URL:<input type=\"text\" name=\"URL\"><br>" + "<SELECT NAME=\"format\">" + "<OPTION VALUE=\"mp3\">MP3" + "<OPTION VALUE=\"wma\">WMA" + "<OPTION VALUE=\"generic\">Generic" + "</SELECT>", new ConfiguratorListener() {

            public void process(HashMap<String, String> formdata) {
                String streamName = formdata.get("streamname");
                String url = formdata.get("URL");
                String formatString = formdata.get("format");
                if (streamName != null && url != null && formatString != null) {
                    try {
                        int format = -1;
                        if (formatString.equals("mp3")) format = 0; else if (formatString.equals("wma")) format = 1; else if (formatString.equals("generic")) format = 2;
                        if (format != -1) {
                            Config.addStream(streamName, url, format);
                        }
                    } catch (Exception urlException) {
                        System.out.println(urlException.toString());
                    }
                }
            }
        });
    }

    private static void debug(String str) {
        System.err.println("ConfigWeb: " + str);
    }
}

interface ConfiguratorListener {

    void process(HashMap<String, String> formdata);
}

class ConfiguratorElement {

    String url;

    String formElement;

    ConfiguratorListener listener;

    /**
     * @param url
     * @param element
     * @param listener
     */
    public ConfiguratorElement(String url, String element, ConfiguratorListener listener) {
        super();
        this.url = url;
        formElement = element;
        this.listener = listener;
    }
}
