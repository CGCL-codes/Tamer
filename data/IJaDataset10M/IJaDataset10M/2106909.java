package com.bix.util.blizfiles.dds;

import java.nio.ByteBuffer;

/**
 * This object represents a DDS color block that contains alpha information.
 * 
 *	@author		squid
 *
 *	@version	1.0.0
 */
class DDSColorBlockAlpha extends DDSColorBlock {

    private int[] alpha = new int[4];

    /**
	 * Instantiate the object from a ByteBuffer.
	 * 
	 * @param bb	The buffer to read the object data from.
	 */
    public DDSColorBlockAlpha(ByteBuffer bb) {
        this.read(bb);
    }

    /**
	 * Read the object from a byte buffer. 
	 * 
	 * @param	bb	The buffer to read the object from.
	 */
    public void read(ByteBuffer bb) {
        for (int i = 0; i < alpha.length; i++) {
            this.alpha[i] = (bb.getShort() & 0xFFFF);
        }
        super.read(bb);
    }

    /**
	 * This color type ALWAYS uses the first algorithm as the normal one.  That
	 * is:
	 * 
	 * color[2] = 2/3 color[0] + 1/3 color[1];
	 * color[3] = 1/3 color[0] + 2/3 color[1];
	 * 
	 * @return true, always
	 */
    protected boolean useAlgorithm1() {
        return true;
    }

    public void decode(int destination[], int offset, int width) {
        DDSColor[] colors = this.getColors();
        for (int y = 0; y < 4; y++, offset += width * 4) {
            int lookup = this.getLookup()[y];
            int alpha = this.alpha[y];
            for (int x = 0; x < 4; x++) {
                DDSColor color = colors[(lookup & masks[x]) >> shift[x]];
                destination[offset + x * 4 + 0] = color.getRed();
                destination[offset + x * 4 + 1] = color.getGreen();
                destination[offset + x * 4 + 2] = color.getBlue();
                int temp = x * 4;
                destination[offset + x * 4 + 3] = (alpha & 0xF << temp) >> temp;
            }
        }
    }
}
