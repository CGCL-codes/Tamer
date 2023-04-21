package org.spantus.android.dto;

import java.io.File;
import java.net.URL;
import org.spantus.android.audio.RecordState;

public class SpantusAudioCtx {

    private Float sampleRate = 8000F;

    private String spantusServer = "spantus.cloudfoundry.com";

    private URL spantusServerCorpus;

    private RecordState recordState = RecordState.STOP;

    private Boolean isPlayabe = false;

    private Boolean isOnline = false;

    private Boolean isPlaying = false;

    private Boolean isRecording = false;

    private Boolean isListening = false;

    private Boolean audioFailure = false;

    private Boolean destroyed = false;

    private Boolean repollOnTimeout = true;

    private String sessionId;

    private Boolean allowStopPlaying;

    private Boolean useSpeechDetector;

    private URL recordUrl;

    private URL playUrl;

    private Long lastTimestamp;

    private String subDirName = "spantus";

    private String lastFileName = "tmp.wav";

    private int maxLengthInSamples;

    private File workingDir;

    private URL trainUrl;

    private URL recognizedUrl;

    public RecordState getRecordState() {
        return recordState;
    }

    public void setRecordState(RecordState recordState) {
        this.recordState = recordState;
    }

    @Override
    public String toString() {
        return "SpntAudioContext [recordState=" + recordState + "]";
    }

    public Boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public Boolean getIsRecording() {
        return isRecording;
    }

    public void setIsRecording(Boolean isRecording) {
        this.isRecording = isRecording;
    }

    public Boolean getIsListening() {
        return isListening;
    }

    public void setIsListening(Boolean isListening) {
        this.isListening = isListening;
    }

    public Boolean getAudioFailure() {
        return audioFailure;
    }

    public void setAudioFailure(Boolean audioFailure) {
        this.audioFailure = audioFailure;
    }

    public Boolean getAllowStopPlaying() {
        return allowStopPlaying;
    }

    public void setAllowStopPlaying(Boolean allowStopPlaying) {
        this.allowStopPlaying = allowStopPlaying;
    }

    public Boolean getUseSpeechDetector() {
        return useSpeechDetector;
    }

    public void setUseSpeechDetector(Boolean useSpeechDetector) {
        this.useSpeechDetector = useSpeechDetector;
    }

    public URL getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(URL recordUrl) {
        this.recordUrl = recordUrl;
    }

    public URL getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(URL playUrl) {
        this.playUrl = playUrl;
    }

    public Long getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(Long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public String getSpantusServer() {
        return spantusServer;
    }

    public void setSpantusServer(String spantusServer) {
        this.spantusServer = spantusServer;
    }

    public URL getSpantusServerCorpus() {
        return spantusServerCorpus;
    }

    public void setSpantusServerCorpus(URL spantusServerCorpus) {
        this.spantusServerCorpus = spantusServerCorpus;
    }

    public Boolean getDestroyed() {
        return destroyed;
    }

    public void setDestroyed(Boolean destroyed) {
        this.destroyed = destroyed;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Boolean getRepollOnTimeout() {
        return repollOnTimeout;
    }

    public void setRepollOnTimeout(Boolean repollOnTimeout) {
        this.repollOnTimeout = repollOnTimeout;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOffline) {
        this.isOnline = isOffline;
    }

    public Boolean getIsPlayabe() {
        return isPlayabe;
    }

    public void setIsPlayabe(Boolean isPlayabe) {
        this.isPlayabe = isPlayabe;
    }

    public String getSubDirName() {
        return subDirName;
    }

    public void setSubDirName(String subDirName) {
        this.subDirName = subDirName;
    }

    public String getLastFileName() {
        return lastFileName;
    }

    public void setLastFileName(String lastFileName) {
        this.lastFileName = lastFileName;
    }

    public Float getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(Float sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setMaxLengthInSamples(int maxLengthInSamples) {
        this.maxLengthInSamples = maxLengthInSamples;
    }

    public int getMaxLengthInSamples() {
        return maxLengthInSamples;
    }

    public int getSampleSizeInBits() {
        return 16;
    }

    public File getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(File workingDir) {
        this.workingDir = workingDir;
    }

    public void setTrainUrl(URL trainUrl) {
        this.trainUrl = trainUrl;
    }

    public URL getTrainUrl() {
        return trainUrl;
    }

    public void setRecognizedUrl(URL recognizedUrl) {
        this.recognizedUrl = recognizedUrl;
    }

    public URL getRecognizedUrl() {
        return recognizedUrl;
    }
}
