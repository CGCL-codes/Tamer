package net.sourceforge.jffmpeg.codecs.video.mpeg4.divx.vlc;

import net.sourceforge.jffmpeg.codecs.utils.VLCTable;

/**
 * Intra picture macro block coded block pattern 
 */
public class dcChrominanceVlc extends VLCTable {

    public dcChrominanceVlc() {
        vlcCodes = new long[][] { { 3, 2 }, { 2, 2 }, { 1, 2 }, { 1, 3 }, { 1, 4 }, { 1, 5 }, { 1, 6 }, { 1, 7 }, { 1, 8 }, { 1, 9 }, { 1, 10 }, { 1, 11 }, { 1, 12 } };
        createHighSpeedTable();
    }
}
