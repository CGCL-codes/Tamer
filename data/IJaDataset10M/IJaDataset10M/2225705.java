package com.xith3d.sound.drivers.joal;

import com.xith3d.sound.SoundBuffer;
import com.xith3d.sound.BufferFormat;
import java.nio.ByteBuffer;
import net.java.games.joal.AL;

/**
 * Insert package comments here
 * <p>
 * Originally Coded by David Yazel on Sep 7, 2003 at 2:01:07 PM.
 */
public class SoundBufferImpl implements SoundBuffer {

    SoundDriverImpl driver;

    int handle;

    SoundBufferImpl(SoundDriverImpl driver) {
        this.driver = driver;
        int ret[] = new int[1];
        driver.getAL().alGenBuffers(1, ret);
        handle = ret[0];
    }

    public void setData(BufferFormat format, int size, int frequency, ByteBuffer data) {
        int f = 0;
        if (format == BufferFormat.MONO16) f = AL.AL_FORMAT_MONO16; else if (format == BufferFormat.MONO8) f = AL.AL_FORMAT_MONO8; else if (format == BufferFormat.STEREO16) f = AL.AL_FORMAT_STEREO16; else if (format == BufferFormat.STEREO8) f = AL.AL_FORMAT_STEREO8;
        driver.getAL().alBufferData(handle, f, data, size, frequency);
    }
}
