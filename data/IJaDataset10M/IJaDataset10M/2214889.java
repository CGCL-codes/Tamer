package jj2000.j2k.wavelet;

import jj2000.j2k.*;

/**
 * This class holds the decomposition type to be used in each part of the image;
 * the default one, the component specific ones, the tile default ones and the
 * component-tile specific ones.
 * 
 * <P>
 * The decomposition type identifiers values are the same as in the codestream.
 * 
 * <P>
 * The hierarchy is:<br>
 * - Tile and component specific decomposition<br>
 * - Tile specific default decomposition<br>
 * - Component main default decomposition<br>
 * - Main default decomposition<br>
 * 
 * <P>
 * At the moment tiles are not supported by this class.
 * */
public class WTDecompSpec {

    /**
	 * ID for the dyadic wavelet tree decomposition (also called "Mallat" in
	 * JPEG 2000): 0x00.
	 */
    public static final int WT_DECOMP_DYADIC = 0;

    /**
	 * ID for the SPACL (as defined in JPEG 2000) wavelet tree decomposition (1
	 * level of decomposition in the high bands and some specified number for
	 * the lowest LL band): 0x02.
	 */
    public static final int WT_DECOMP_SPACL = 2;

    /**
	 * ID for the PACKET (as defined in JPEG 2000) wavelet tree decomposition (2
	 * levels of decomposition in the high bands and some specified number for
	 * the lowest LL band): 0x01.
	 */
    public static final int WT_DECOMP_PACKET = 1;

    /** The identifier for "main default" specified decomposition */
    public static final byte DEC_SPEC_MAIN_DEF = 0;

    /** The identifier for "component default" specified decomposition */
    public static final byte DEC_SPEC_COMP_DEF = 1;

    /** The identifier for "tile specific default" specified decomposition */
    public static final byte DEC_SPEC_TILE_DEF = 2;

    /**
	 * The identifier for "tile and component specific" specified decomposition
	 */
    public static final byte DEC_SPEC_TILE_COMP = 3;

    private byte specValType[];

    /** The main default decomposition */
    private int mainDefDecompType;

    /** The main default number of decomposition levels */
    private int mainDefLevels;

    /** The component main default decomposition, for each component. */
    private int compMainDefDecompType[];

    /** The component main default decomposition levels, for each component */
    private int compMainDefLevels[];

    /**
	 * Constructs a new 'WTDecompSpec' for the specified number of components
	 * and tiles, with the given main default decomposition type and number of
	 * levels.
	 * 
	 * <P>
	 * NOTE: The tile specific things are not supported yet
	 * 
	 * @param nc
	 *            The number of components
	 * 
	 * @param nt
	 *            The number of tiles
	 * 
	 * @param dec
	 *            The main default decomposition type
	 * 
	 * @param lev
	 *            The main default number of decomposition levels
	 * 
	 * 
	 * */
    public WTDecompSpec(int nc, int dec, int lev) {
        mainDefDecompType = dec;
        mainDefLevels = lev;
        specValType = new byte[nc];
    }

    /**
	 * Sets the "component main default" decomposition type and number of levels
	 * for the specified component. Both 'dec' and 'lev' can not be negative at
	 * the same time.
	 * 
	 * @param n
	 *            The component index
	 * 
	 * @param dec
	 *            The decomposition type. If negative then the main default is
	 *            used.
	 * 
	 * @param lev
	 *            The number of levels. If negative then the main defaul is
	 *            used.
	 * 
	 * 
	 * */
    public void setMainCompDefDecompType(int n, int dec, int lev) {
        if (dec < 0 && lev < 0) {
            throw new IllegalArgumentException();
        }
        specValType[n] = DEC_SPEC_COMP_DEF;
        if (compMainDefDecompType == null) {
            compMainDefDecompType = new int[specValType.length];
            compMainDefLevels = new int[specValType.length];
        }
        compMainDefDecompType[n] = (dec >= 0) ? dec : mainDefDecompType;
        compMainDefLevels[n] = (lev >= 0) ? lev : mainDefLevels;
        throw new NotImplementedError("Currently, in JJ2000, all components and tiles must have the same " + "decomposition type and number of levels");
    }

    /**
	 * Returns the type of specification for the decomposition in the specified
	 * component and tile. The specification type is one of:
	 * 'DEC_SPEC_MAIN_DEF', 'DEC_SPEC_COMP_DEF', 'DEC_SPEC_TILE_DEF',
	 * 'DEC_SPEC_TILE_COMP'.
	 * 
	 * <P>
	 * NOTE: The tile specific things are not supported yet
	 * 
	 * @param n
	 *            The component index
	 * 
	 * @param t
	 *            The tile index, in raster scan order.
	 * 
	 * @return The specification type for component 'n' and tile 't'.
	 * 
	 * 
	 * */
    public byte getDecSpecType(int n) {
        return specValType[n];
    }

    /**
	 * Returns the main default decomposition type.
	 * 
	 * @return The main default decomposition type.
	 * 
	 * 
	 * */
    public int getMainDefDecompType() {
        return mainDefDecompType;
    }

    /**
	 * Returns the main default decomposition number of levels.
	 * 
	 * @return The main default decomposition number of levels.
	 * 
	 * 
	 * */
    public int getMainDefLevels() {
        return mainDefLevels;
    }

    /**
	 * Returns the decomposition type to be used in component 'n' and tile 't'.
	 * 
	 * <P>
	 * NOTE: The tile specific things are not supported yet
	 * 
	 * @param n
	 *            The component index.
	 * 
	 * @param t
	 *            The tile index, in raster scan order
	 * 
	 * @return The decomposition type to be used.
	 * 
	 * 
	 * */
    public int getDecompType(int n) {
        switch(specValType[n]) {
            case DEC_SPEC_MAIN_DEF:
                return mainDefDecompType;
            case DEC_SPEC_COMP_DEF:
                return compMainDefDecompType[n];
            case DEC_SPEC_TILE_DEF:
                throw new NotImplementedError();
            case DEC_SPEC_TILE_COMP:
                throw new NotImplementedError();
            default:
                throw new Error("Internal JJ2000 error");
        }
    }

    /**
	 * Returns the decomposition number of levels in component 'n' and tile 't'.
	 * 
	 * <P>
	 * NOTE: The tile specific things are not supported yet
	 * 
	 * @param n
	 *            The component index.
	 * 
	 * @param t
	 *            The tile index, in raster scan order
	 * 
	 * @return The decomposition number of levels.
	 * 
	 * 
	 * */
    public int getLevels(int n) {
        switch(specValType[n]) {
            case DEC_SPEC_MAIN_DEF:
                return mainDefLevels;
            case DEC_SPEC_COMP_DEF:
                return compMainDefLevels[n];
            case DEC_SPEC_TILE_DEF:
                throw new NotImplementedError();
            case DEC_SPEC_TILE_COMP:
                throw new NotImplementedError();
            default:
                throw new Error("Internal JJ2000 error");
        }
    }
}
