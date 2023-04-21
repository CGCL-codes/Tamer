package com.warserver;

/**
 * This is used by com.warserver.server.cmds.FileTransfer to hold a chunk of a file.
 *
 * @author  Kurt Olsen
 * @version 1.0
 *
 * @see com.warserver.server.cmds.FileTransfer
 * @see com.warserver.client.FileTransfer
 */
public class FTPacket implements java.io.Serializable {

    public byte buffer[];

    public int bufsize;

    public FTPacket(byte buffer[], int bufsize) {
        this.buffer = buffer;
        this.bufsize = bufsize;
    }
}
