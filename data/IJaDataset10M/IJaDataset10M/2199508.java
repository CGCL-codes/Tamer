package com.groovytagger.database;

import com.groovytagger.interfaces.CdDataEngineInterface;
import com.groovytagger.utils.GlobalApplicationStatus;

public class DataProvider {

    private CdDataEngineInterface engine;

    private boolean lyricsEnabled;

    private boolean coverEnabled;

    private boolean tagEnabled;

    private boolean storeProxyEnabled;

    private String engineName;

    private boolean deepSearchenabled;

    public DataProvider() {
    }

    public DataProvider(CdDataEngineInterface engine, String engineName, boolean lyricsEnabled, boolean coverEnabled, boolean tagEnabled) {
        this.engine = engine;
        this.lyricsEnabled = lyricsEnabled;
        this.coverEnabled = coverEnabled;
        this.tagEnabled = tagEnabled;
        this.engineName = engineName;
    }

    public void setEngine(CdDataEngineInterface engine) {
        this.engine = engine;
    }

    public CdDataEngineInterface getEngine() {
        return engine;
    }

    public void setLyricsEnabled(boolean lyricsEnabled) {
        this.lyricsEnabled = lyricsEnabled;
    }

    public boolean isLyricsEnabled() {
        return lyricsEnabled;
    }

    public void setCoverEnabled(boolean coverEnabled) {
        this.coverEnabled = coverEnabled;
    }

    public boolean isCoverEnabled() {
        return coverEnabled;
    }

    public void setTagEnabled(boolean tagEnabled) {
        this.tagEnabled = tagEnabled;
    }

    public boolean isTagEnabled() {
        return tagEnabled;
    }

    public void setStoreProxyEnabled(boolean storeProxyEnabled) {
        this.storeProxyEnabled = storeProxyEnabled;
    }

    public boolean isStoreProxyEnabled() {
        return storeProxyEnabled;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    public String getEngineName() {
        return engineName;
    }

    public void setDeepSearchenabled(boolean deepSearchenabled) {
        this.deepSearchenabled = deepSearchenabled;
    }

    public boolean isDeepSearchenabled() {
        return deepSearchenabled;
    }
}
