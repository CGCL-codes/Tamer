package com.realtime.crossfire.jxclient.server.crossfire;

import org.jetbrains.annotations.NotNull;

/**
 * Listener to be notified of updated face information.
 * @author Andreas Kirschbaum
 */
public interface CrossfireUpdateFaceListener {

    /**
     * Notifies that face information has been received from the Crossfire
     * server.
     * @param faceNum the face ID
     * @param faceSetNum the face set
     * @param packet the packet data; must not be changed
     * @param pos the starting position into <code>data</code>
     * @param len the length in bytes in <code>data</code>
     */
    void updateFace(int faceNum, int faceSetNum, @NotNull byte[] packet, int pos, int len);
}
