package com.dukesoftware.cdnavi.exec.template;

public interface IPlayerManager {

    public enum State {

        PLAYING, STOP
    }

    ;

    void play();

    State getState();

    void setStopState();

    void close();

    double getMediaSeconds();
}
