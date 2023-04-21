package com.cell.sound;

/**
 * 表示一段已经缓冲了的声音数据。
 * @author WAZA
 */
public interface ISound {

    public SoundInfo getSoundInfo();

    public void dispose();

    public int getSize();
}
