package net.sourceforge.jffmpeg.codecs.video.mpeg4.divx.vlc;

import net.sourceforge.jffmpeg.codecs.utils.VLCTable;

/**
 * Intra picture macro block coded block pattern 
 */
public class IntraMcbpc extends VLCTable {

    public IntraMcbpc() {
        vlcCodes = new long[][] { { 0x1, 1 }, { 0x1, 3 }, { 0x2, 3 }, { 0x3, 3 }, { 0x1, 4 }, { 0x1, 6 }, { 0x2, 6 }, { 0x3, 6 }, { 0x1, 9 } };
        createHighSpeedTable();
    }
}
