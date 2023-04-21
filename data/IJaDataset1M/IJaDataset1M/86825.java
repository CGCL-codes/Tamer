package com.cirnoworks.cas;

/**
 * @author cloudee
 * 
 */
public interface SoundManagerFactory {

    SoundManager createManager(int bgChannels, int seChannels);

    void releaseManager(SoundManager manager);

    void reset();

    void shutdown();
}
