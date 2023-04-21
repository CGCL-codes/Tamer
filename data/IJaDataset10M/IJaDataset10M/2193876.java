package com.javapda.c328r.command;

/**
 * 
Snapshot Picture 01h
Preview Picture 02h
JPEG Preview Picture 05h
 *
 */
public abstract class ComediaPictureType {

    public static final int SNAPSHOT = 0x01;

    public static final int PREVIEW = 0x02;

    public static final int JPEG_PREVIEW = 0x05;
}
