package net.sourceforge.jffmpeg.codecs.audio.mpeg.mp3.data;

import net.sourceforge.jffmpeg.codecs.utils.VLCTable;

/**
 *
 */
public class HuffmanCodes extends VLCTable {

    protected long[] codes;

    protected long[] codesSize;

    protected int xsize;

    protected int[] huff_code_table;

    protected void generateVLCCodes() {
        vlcCodes = new long[codes.length][2];
        for (int i = 0; i < vlcCodes.length; i++) {
            vlcCodes[i][0] = codes[i];
            vlcCodes[i][1] = codesSize[i];
        }
        huff_code_table = new int[xsize * xsize];
        int j = 0;
        for (int x = 0; x < xsize; x++) {
            for (int y = 0; y < xsize; y++) {
                huff_code_table[j++] = (x << 4) | y;
            }
        }
        createHighSpeedTable();
    }

    /** Creates a new instance of HuffmanCodes */
    public HuffmanCodes() {
    }

    public int[] getHuffCodeTable() {
        return huff_code_table;
    }

    public static final HuffmanCodes[] getHuffmanCodes() {
        return new HuffmanCodes[] { new HuffmanCodesNull(), new HuffmanCodes1(), new HuffmanCodes2(), new HuffmanCodes3(), new HuffmanCodes5(), new HuffmanCodes6(), new HuffmanCodes7(), new HuffmanCodes8(), new HuffmanCodes9(), new HuffmanCodes10(), new HuffmanCodes11(), new HuffmanCodes12(), new HuffmanCodes13(), new HuffmanCodes15(), new HuffmanCodes16(), new HuffmanCodes24() };
    }
}
