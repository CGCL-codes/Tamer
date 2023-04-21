package org.spantus.server.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SpntServletConfig")
public class SpntServletConfig {

    public static final String APPLET_JAR_NAME = "spnt-applet-0.3-SNAPSHOT.jar";

    private String recordServletURL;

    private String audioServletURL;

    private String clientSessionId;

    private String baseURL;

    private String serveraddress;

    private String controlServletURL;

    private String appletjarName = APPLET_JAR_NAME;

    private Integer playPollTimeout = 0;

    private String localeCode;

    private int pollTimeout;

    public String getRecordServletURL() {
        return recordServletURL;
    }

    public void setRecordServletURL(String recordServletURL) {
        this.recordServletURL = recordServletURL;
    }

    public String getAudioServletURL() {
        return this.audioServletURL;
    }

    public void setAudioServletURL(String audioServletURL) {
        this.audioServletURL = audioServletURL;
    }

    public String getControlServletURL() {
        return controlServletURL;
    }

    public void setClientSessionId(String clientSessionId) {
        this.clientSessionId = clientSessionId;
    }

    public String getClientSessionId() {
        return clientSessionId;
    }

    public String getBaseURL() {
        return this.baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public void setServerAddress(String serveraddress) {
        this.serveraddress = serveraddress;
    }

    public String getServerAddress() {
        return serveraddress;
    }

    public String getAudioAppletClass() {
        return "org.spnt.applet.SpntAudioApplet";
    }

    public String getAppletArchives() {
        String appletArchives = getBaseURL() + "/api/content/applet/" + getAppletjarName();
        return appletArchives;
    }

    public String getAppletjarName() {
        return appletjarName;
    }

    public String getHubLocationString() {
        return null;
    }

    public String getRecordAudioFormat() {
        return "LIN16";
    }

    public int getRecordSampleRate() {
        return 8000;
    }

    public boolean getGreenOnEnableInput() {
        return false;
    }

    public boolean getRecordIsLittleEndian() {
        return true;
    }

    public boolean getAllowStopPlaying() {
        return true;
    }

    public boolean getHideAudioButton() {
        return false;
    }

    public boolean getPlayRecordTone() {
        return true;
    }

    public boolean getUseSpeechDetector() {
        return true;
    }

    public int getAppletWidth() {
        return 128;
    }

    public int getAppletHeight() {
        return 128;
    }

    public void setControlServletURL(String controlServletURL) {
        this.controlServletURL = controlServletURL;
    }

    public void setAppletjarName(String appletjarName) {
        this.appletjarName = appletjarName;
    }

    public Integer getPlayPollTimeout() {
        return playPollTimeout;
    }

    public void setPlayPollTimeout(Integer playPollTimeout) {
        this.playPollTimeout = playPollTimeout;
    }

    public int getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(int i) {
        this.pollTimeout = i;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }
}
