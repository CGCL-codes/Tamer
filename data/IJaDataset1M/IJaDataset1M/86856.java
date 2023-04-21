package com.cell.bms.oal;

import net.java.games.joal.AL;
import net.java.games.sound3d.AudioSystem3D;
import net.java.games.sound3d.Source;

public class JALPlayer implements Comparable<JALPlayer> {

    final AL al;

    private final int[] source;

    private int playing_buffer_id = -1;

    public long last_bind_time = 0;

    public JALPlayer(AL al) throws Exception {
        this.al = al;
        synchronized (al) {
            int[] source = new int[1];
            al.alGenSources(1, source, 0);
            if (al.alGetError() != AL.AL_NO_ERROR) {
                throw new Exception("Error generating OpenAL source !");
            }
            this.source = source;
            float[] sourcePos = { 0.0f, 0.0f, 0.0f };
            float[] sourceVel = { 0.0f, 0.0f, 0.0f };
            al.alSourcefv(source[0], AL.AL_POSITION, sourcePos, 0);
            al.alSourcefv(source[0], AL.AL_VELOCITY, sourceVel, 0);
            al.alSourcef(source[0], AL.AL_PITCH, 1.0f);
            al.alSourcef(source[0], AL.AL_GAIN, 1.0f);
            al.alSourcei(source[0], AL.AL_LOOPING, 0);
        }
    }

    public void bindBuffer(JALSound sound) {
        if (source != null) {
            al.alSourcei(source[0], AL.AL_BUFFER, sound.buffer[0]);
            if (al.alGetError() != AL.AL_NO_ERROR) {
                System.err.println("Error setting up OpenAL source : " + sound.sound_name);
                return;
            }
            playing_buffer_id = sound.buffer[0];
            last_bind_time = System.currentTimeMillis();
        }
    }

    public boolean isBindBuffer(JALSound sound) {
        if (source != null && playing_buffer_id == sound.buffer[0]) {
            return true;
        }
        return false;
    }

    public boolean isFree() {
        if (source != null) {
            int state[] = new int[1];
            al.alGetSourcei(source[0], AL.AL_SOURCE_STATE, state, 0);
            return state[0] != AL.AL_PLAYING;
        }
        return false;
    }

    public void play() {
        if (source != null) {
            al.alSourcePlay(source[0]);
        }
    }

    public void pause() {
        if (source != null) {
            al.alSourcePause(source[0]);
        }
    }

    public void stop() {
        if (source != null) {
            al.alSourceStop(source[0]);
        }
    }

    public void dispose() {
        if (source != null) {
            al.alDeleteSources(1, source, 0);
            System.out.println("alDeleteSources : ");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        dispose();
    }

    @Override
    public int compareTo(JALPlayer o) {
        return (int) (this.last_bind_time - o.last_bind_time);
    }
}
