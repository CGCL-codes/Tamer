package com.cell.sound;

import java.nio.ByteBuffer;

public abstract class SoundInfo {

    public abstract String getResource();

    /** stereo mono */
    public abstract int getChannels();

    /** 16bit 8bit */
    public abstract int getBitLength();

    /** 44100hz 22050hz */
    public abstract int getFrameRate();

    /** file comment */
    public abstract String getComment();

    /** 
	 * PCM raw data stream <br>
	 * 读取此缓冲区当前位置的字节流，然后该位置递增。 
	 * */
    public abstract ByteBuffer getData();

    /**
	 * if the raw stream has remain data <br>
	 * 该缓冲区是否有新数据。
	 */
    public abstract boolean hasData();

    /**
	 * reset the raw data stream to head<br>
	 * 重设读取的数据流到最开始。
	 */
    public abstract void resetData();

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SoundInfo : " + getResource() + "\n");
        sb.append("\tchannels : " + getChannels() + "\n");
        sb.append("\tbit_length : " + getBitLength() + "\n");
        sb.append("\tframe_rate : " + getFrameRate() + "\n");
        sb.append(getComment());
        return sb.toString();
    }
}
