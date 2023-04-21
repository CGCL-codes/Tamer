package com.tomgibara.mosey.portal;

import java.util.Random;
import com.tomgibara.mosey.coding.GolayCode;

/**
 * Static constants, lookups and methods for rendering and scanning the
 * Moseycode portal symbology.
 * 
 * The dimensions in this class are based on a portal that occupies the
 * unit square. 
 * 
 * @author Tom Gibara
 *
 */
public class PortalSymbology {

    /**
	 * The number of codewords encoded in the symbology.
	 */
    public static final int CODEWORDS = 8;

    /**
	 * The number of bits in one codeword.
	 */
    public static final int CODEWORD_LENGTH = 24;

    /**
	 * The size of a (conceptual) grid around which the coordinates of the
	 * symbology are arranged.
	 */
    public static final int SIZE = 24;

    /**
	 * The separation between successive grid coordinates.
	 */
    public static final float COORD_STEP = 1f / SIZE;

    /**
	 * The offset to centralize a coordinate.
	 */
    public static final float COORD_OFFSET = 1f / (SIZE * 2);

    /**
	 * The number of targets in the symbology.
	 */
    public static final int TARGETS = 4;

    /**
	 * The corner radius of the "quiet area" of the symbology.
	 */
    public static final float CORNER_RADIUS = 3 * COORD_STEP + COORD_OFFSET;

    /**
	 * The number of edge bits (bits used for orienting the symbology).
	 */
    public static final int EDGE_BITS = 8;

    /**
	 * The radius of a bit.
	 */
    public static final float BIT_RADIUS = 0.45f / SIZE;

    /**
	 * Bit locations are based on a pattern of reflections and rotations
	 * over a single octant where each codeword has three bits in each.
	 */
    private static final int BITS_PER_OCTANT = CODEWORD_LENGTH / 8;

    /**
	 * The packed x,y coordinates of each bit in each codeword.
	 */
    private static final float[][] bitCoords;

    /**
	 * The packed x,y coordinates of each target.
	 */
    private static final float[] targetCoords;

    /**
	 * The radii of successive black/white targets.
	 */
    private static final float[] targetRadii;

    /**
	 * There are eight edge bits as the LSB bits of an int.
	 */
    private static final int edgeBits;

    /**
	 * The packed x,y coordinates of each edge bit.
	 */
    private static final float[] edgeBitCoords;

    /**
	 * An 256 element array, the nth element of which is the best guess orientation
	 * based on the 8 LSBs of n being read as an orientation. 
	 * 
	 * Each element consists of:
	 * 
	 * 3 bits : the number of bits in error
	 * 1 bit  : set if symbology has been reflected. 
	 * 2 bits : the edge bit that zeroth edge bit maps to
	 * 
	 */
    private static final int[] orientations;

    /**
	 * The packed x,y coordinates of the corners of the central area into which
	 * a human readable design may be added.
	 */
    private static final float[] decalCoords;

    /**
	 * Masks off the 12 LSBs of an int.
	 */
    private static final int DATAWORD_MASK = (1 << 12) - 1;

    /**
	 * Masks off the 24 LSBs of an int.
	 */
    private static final int CODEWORD_MASK = (1 << 24) - 1;

    /**
	 * Fixed random sequence of bits.
	 */
    private static final int[] CODEWORD_NOISE = new int[CODEWORDS];

    static {
        bitCoords = new float[CODEWORDS][CODEWORD_LENGTH * 2];
        for (int word = 0; word < CODEWORDS; word++) {
            float[] cs = bitCoords[word];
            for (int bit = 0; bit < CODEWORD_LENGTH; bit++) {
                int octant = bit / BITS_PER_OCTANT;
                int steps = bit % BITS_PER_OCTANT;
                int index = (8 * steps + word + octant) % CODEWORD_LENGTH;
                int row = index / 5;
                int column = index % 5;
                if ((octant & 1) == 1) {
                    column += row == 4 ? 6 : 5;
                }
                int x, y;
                int quadrant = bit / (BITS_PER_OCTANT * 2);
                switch(quadrant) {
                    case 0:
                        x = 7 + column;
                        y = 1 + row;
                        break;
                    case 1:
                        x = 22 - row;
                        y = 7 + column;
                        break;
                    case 2:
                        x = 16 - column;
                        y = 22 - row;
                        break;
                    case 3:
                        x = 1 + row;
                        y = 16 - column;
                        break;
                    default:
                        throw new IllegalStateException();
                }
                cs[bit * 2] = x * COORD_STEP + COORD_OFFSET;
                cs[bit * 2 + 1] = y * COORD_STEP + COORD_OFFSET;
            }
        }
    }

    static {
        float d = 3 * COORD_STEP + COORD_OFFSET;
        targetCoords = new float[] { d, d, 1 - d, d, 1 - d, 1 - d, d, 1 - d };
        targetRadii = new float[2];
        targetRadii[0] = COORD_OFFSET + COORD_STEP * 2;
        targetRadii[1] = COORD_OFFSET + COORD_STEP;
    }

    static {
        Random r = new Random(0L);
        for (int i = 0; i < CODEWORD_NOISE.length; i++) {
            CODEWORD_NOISE[i] = r.nextInt() & CODEWORD_MASK;
        }
    }

    static {
        edgeBits = 27;
        edgeBitCoords = new float[] { 11, 5, 12, 5, 18, 11, 18, 12, 12, 18, 11, 18, 5, 12, 5, 11 };
        for (int i = 0; i < edgeBitCoords.length; i++) {
            edgeBitCoords[i] = COORD_OFFSET + COORD_STEP * edgeBitCoords[i];
        }
    }

    static {
        orientations = new int[256];
        final int[] valid = { Integer.parseInt("00011011", 2), Integer.parseInt("01101100", 2), Integer.parseInt("10110001", 2), Integer.parseInt("11000110", 2), Integer.parseInt("11011000", 2), Integer.parseInt("00110110", 2), Integer.parseInt("10001101", 2), Integer.parseInt("01100011", 2) };
        for (int i = 0; i < orientations.length; i++) {
            int minD = 8;
            int best = -1;
            for (int j = 0; j < valid.length; j++) {
                int v = valid[j];
                int d = Integer.bitCount(i ^ v);
                if (d < minD) {
                    minD = d;
                    best = j;
                }
            }
            orientations[i] = best | (minD << 3);
        }
    }

    static {
        decalCoords = new float[] { COORD_STEP * 7, COORD_STEP * 7, COORD_STEP * 17, COORD_STEP * 17 };
    }

    /**
	 * @param codeword the index of a codeword
	 * @return the x, coordinates of each bit in the codeword
	 */
    public static float[] getBitCoords(int codeword) {
        return bitCoords[codeword];
    }

    /**
	 * @return the x,y coordinates of the centers of each target
	 */
    public static float[] getTargetCoords() {
        return targetCoords;
    }

    /**
	 * @return the radii of the alternating black and white circles that combine
	 * to form a target, largest first
	 */
    public static float[] getTargetRadii() {
        return targetRadii;
    }

    /**
	 * @return the pattern of 8 edge bits in the LSBs of an int
	 */
    public static int getEdgeBits() {
        return edgeBits;
    }

    /**
	 * @return the x,y coordinates at which each of the 8 edge bits appear
	 */
    public static float[] getEdgeBitCoords() {
        return edgeBitCoords;
    }

    /**
	 * @return the corners of the interior free area
	 */
    public static float[] getDecalCoords() {
        return decalCoords;
    }

    /**
	 * Encodes the values associated with a portal into an array of codewords
	 * 
	 * @param portal a portal, not null
	 * @return an array of codewords that encode the portal values 
	 */
    public static int[] codewords(Portal portal) {
        int[] codewords = new int[CODEWORDS];
        {
            int chamberId = portal.getChamberId();
            short shortData = portal.getShortData();
            codewords[0] = (chamberId >> 20) & DATAWORD_MASK;
            codewords[1] = (chamberId >> 8) & DATAWORD_MASK;
            codewords[2] = ((chamberId << 4) | ((shortData & 0xffff) >> 12)) & DATAWORD_MASK;
            codewords[3] = shortData & DATAWORD_MASK;
        }
        {
            int intData = portal.getIntData();
            short checksum = portal.getChecksum();
            codewords[4] = (intData >> 20) & DATAWORD_MASK;
            codewords[5] = (intData >> 8) & DATAWORD_MASK;
            codewords[6] = ((intData << 4) | ((checksum & 0xffff) >> 12)) & DATAWORD_MASK;
            codewords[7] = checksum & DATAWORD_MASK;
        }
        for (int i = 0; i < codewords.length; i++) {
            codewords[i] = GolayCode.encode(codewords[i]) ^ CODEWORD_NOISE[i];
        }
        return codewords;
    }

    /**
	 * Decodes an array of codewords into a portal.
	 * 
	 * @param codewords an array of codewords
	 * @return a portal, or null if the codewords could not be decoded correctly
	 */
    public static Portal portal(int[] codewords) {
        for (int i = 0; i < codewords.length; i++) {
            codewords[i] = GolayCode.correctAndDecode(codewords[i] ^ CODEWORD_NOISE[i]);
        }
        int chamberId = (codewords[0] << 20) | (codewords[1] << 8) | (codewords[2] >> 4);
        short shortData = (short) ((codewords[2] << 12) | codewords[3]);
        int intData = (codewords[4] << 20) | (codewords[5] << 8) | (codewords[6] >> 4);
        short checksum = (short) ((codewords[6] << 12) | codewords[7]);
        Portal portal = new Portal(chamberId, shortData, intData);
        return portal.getChecksum() == checksum ? portal : null;
    }

    /**
	 * @param bits an int containing edge bits in its LSBs
	 * @return information about the orientation relative to the supplied edge bits
	 */
    public static int matchOrientation(int bits) {
        return orientations[bits];
    }
}
