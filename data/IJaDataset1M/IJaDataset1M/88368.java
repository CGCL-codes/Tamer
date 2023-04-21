package jj2000.j2k.entropy;

import jj2000.j2k.util.*;
import jj2000.j2k.*;
import java.util.*;

/**
 * This class extends ModuleSpec class for code-blocks sizes holding purposes.
 * 
 * <P>
 * It stores the size a of code-block.
 * */
public class CBlkSizeSpec extends ModuleSpec {

    /** Name of the option */
    private static final String optName = "Cblksiz";

    /** The maximum code-block width */
    private int maxCBlkWidth = 0;

    /** The maximum code-block height */
    private int maxCBlkHeight = 0;

    /**
	 * Creates a new CBlkSizeSpec object for the specified number of tiles and
	 * components.
	 * 
	 * @param nt
	 *            The number of tiles
	 * 
	 * @param nc
	 *            The number of components
	 * 
	 * @param type
	 *            the type of the specification module i.e. tile specific,
	 *            component specific or both.
	 * */
    public CBlkSizeSpec(int nt, int nc, byte type) {
        super(nt, nc, type);
    }

    /**
	 * Creates a new CBlkSizeSpec object for the specified number of tiles and
	 * components and the ParameterList instance.
	 * 
	 * @param nt
	 *            The number of tiles
	 * 
	 * @param nc
	 *            The number of components
	 * 
	 * @param type
	 *            the type of the specification module i.e. tile specific,
	 *            component specific or both.
	 * 
	 * @param imgsrc
	 *            The image source (used to get the image size)
	 * 
	 * @param pl
	 *            The ParameterList instance
	 * */
    public CBlkSizeSpec(int nt, int nc, byte type, ParameterList pl) {
        super(nt, nc, type);
        boolean firstVal = true;
        String param = pl.getParameter(optName);
        StringTokenizer stk = new StringTokenizer(param);
        byte curSpecType = SPEC_DEF;
        boolean[] tileSpec = null;
        boolean[] compSpec = null;
        int ci, ti;
        String word = null;
        String errMsg = null;
        while (stk.hasMoreTokens()) {
            word = stk.nextToken();
            switch(word.charAt(0)) {
                case 't':
                    tileSpec = parseIdx(word, nTiles);
                    if (curSpecType == SPEC_COMP_DEF) {
                        curSpecType = SPEC_TILE_COMP;
                    } else {
                        curSpecType = SPEC_TILE_DEF;
                    }
                    break;
                case 'c':
                    compSpec = parseIdx(word, nComp);
                    if (curSpecType == SPEC_TILE_DEF) {
                        curSpecType = SPEC_TILE_COMP;
                    } else {
                        curSpecType = SPEC_COMP_DEF;
                    }
                    break;
                default:
                    if (!Character.isDigit(word.charAt(0))) {
                        errMsg = "Bad construction for parameter: " + word;
                        throw new IllegalArgumentException(errMsg);
                    }
                    Integer dim[] = new Integer[2];
                    try {
                        dim[0] = new Integer(word);
                        if (dim[0].intValue() > StdEntropyCoderOptions.MAX_CB_DIM) {
                            errMsg = "'" + optName + "' option : the code-block's width cannot be greater than " + StdEntropyCoderOptions.MAX_CB_DIM;
                            throw new IllegalArgumentException(errMsg);
                        }
                        if (dim[0].intValue() < StdEntropyCoderOptions.MIN_CB_DIM) {
                            errMsg = "'" + optName + "' option : the code-block's width cannot be less than " + StdEntropyCoderOptions.MIN_CB_DIM;
                            throw new IllegalArgumentException(errMsg);
                        }
                        if (dim[0].intValue() != (1 << MathUtil.log2(dim[0].intValue()))) {
                            errMsg = "'" + optName + "' option : the code-block's width must be a power of 2";
                            throw new IllegalArgumentException(errMsg);
                        }
                    } catch (NumberFormatException e) {
                        errMsg = "'" + optName + "' option : the code-block's width could not be parsed.";
                        throw new IllegalArgumentException(errMsg);
                    }
                    try {
                        word = stk.nextToken();
                    } catch (NoSuchElementException e) {
                        errMsg = "'" + optName + "' option : could not parse the code-block's height";
                        throw new IllegalArgumentException(errMsg);
                    }
                    try {
                        dim[1] = new Integer(word);
                        if (dim[1].intValue() > StdEntropyCoderOptions.MAX_CB_DIM) {
                            errMsg = "'" + optName + "' option : the code-block's height cannot be greater than " + StdEntropyCoderOptions.MAX_CB_DIM;
                            throw new IllegalArgumentException(errMsg);
                        }
                        if (dim[1].intValue() < StdEntropyCoderOptions.MIN_CB_DIM) {
                            errMsg = "'" + optName + "' option : the code-block's height cannot be less than " + StdEntropyCoderOptions.MIN_CB_DIM;
                            throw new IllegalArgumentException(errMsg);
                        }
                        if (dim[1].intValue() != (1 << MathUtil.log2(dim[1].intValue()))) {
                            errMsg = "'" + optName + "' option : the code-block's height must be a power of 2";
                            throw new IllegalArgumentException(errMsg);
                        }
                        if (dim[0].intValue() * dim[1].intValue() > StdEntropyCoderOptions.MAX_CB_AREA) {
                            errMsg = "'" + optName + "' option : The code-block's area (i.e. width*height) " + "cannot be greater than " + StdEntropyCoderOptions.MAX_CB_AREA;
                            throw new IllegalArgumentException(errMsg);
                        }
                    } catch (NumberFormatException e) {
                        errMsg = "'" + optName + "' option : the code-block's height could not be parsed.";
                        throw new IllegalArgumentException(errMsg);
                    }
                    if (dim[0].intValue() > maxCBlkWidth) {
                        maxCBlkWidth = dim[0].intValue();
                    }
                    if (dim[1].intValue() > maxCBlkHeight) {
                        maxCBlkHeight = dim[1].intValue();
                    }
                    if (firstVal) {
                        setDefault(dim);
                        firstVal = false;
                    }
                    switch(curSpecType) {
                        case SPEC_DEF:
                            setDefault(dim);
                            break;
                        case SPEC_TILE_DEF:
                            for (ti = tileSpec.length - 1; ti >= 0; ti--) {
                                if (tileSpec[ti]) {
                                    setTileDef(ti, dim);
                                }
                            }
                            break;
                        case SPEC_COMP_DEF:
                            for (ci = compSpec.length - 1; ci >= 0; ci--) {
                                if (compSpec[ci]) {
                                    setCompDef(ci, dim);
                                }
                            }
                            break;
                        default:
                            for (ti = tileSpec.length - 1; ti >= 0; ti--) {
                                for (ci = compSpec.length - 1; ci >= 0; ci--) {
                                    if (tileSpec[ti] && compSpec[ci]) {
                                        setTileCompVal(ti, ci, dim);
                                    }
                                }
                            }
                            break;
                    }
            }
        }
    }

    /**
	 * Returns the maximum code-block's width
	 * 
	 */
    public int getMaxCBlkWidth() {
        return maxCBlkWidth;
    }

    /**
	 * Returns the maximum code-block's height
	 * 
	 */
    public int getMaxCBlkHeight() {
        return maxCBlkHeight;
    }

    /**
	 * Returns the code-block width :
	 * 
	 * <ul>
	 * <li>for the specified tile/component</li>
	 * <li>for the specified tile</li>
	 * <li>for the specified component</li>
	 * <li>default value</li>
	 * </ul>
	 * 
	 * The value returned depends on the value of the variable 'type' which can
	 * take the following values :<br>
	 * 
	 * <ul>
	 * <li>SPEC_DEF -> Default value is returned. t and c values are ignored</li>
	 * <li>SPEC_COMP_DEF -> Component default value is returned. t value is
	 * ignored</li>
	 * <li>SPEC_TILE_DEF -> Tile default value is returned. c value is ignored</li>
	 * <li>SPEC_TILE_COMP -> Tile/Component value is returned.</li>
	 * </ul>
	 * 
	 * @param type
	 *            The type of the value we want to be returned
	 * 
	 * @param t
	 *            The tile index
	 * 
	 * @param c
	 *            the component index
	 * 
	 * @return The code-block width for the specified tile and component
	 * */
    public int getCBlkWidth(byte type, int t, int c) {
        Integer dim[] = null;
        switch(type) {
            case SPEC_DEF:
                dim = (Integer[]) getDefault();
                break;
            case SPEC_COMP_DEF:
                dim = (Integer[]) getCompDef(c);
                break;
            case SPEC_TILE_DEF:
                dim = (Integer[]) getTileDef(t);
                break;
            case SPEC_TILE_COMP:
                dim = (Integer[]) getTileCompVal(t, c);
        }
        return dim[0].intValue();
    }

    /**
	 * Returns the code-block height:
	 * 
	 * <ul>
	 * <li>for the specified tile/component</li>
	 * <li>for the specified tile</li>
	 * <li>for the specified component</li>
	 * <li>default value</li>
	 * </ul>
	 * 
	 * The value returned depends on the value of the variable 'type' which can
	 * take the following values :
	 * 
	 * <ul>
	 * <li>SPEC_DEF -> Default value is returned. t and c values are ignored</li>
	 * <li>SPEC_COMP_DEF -> Component default value is returned. t value is
	 * ignored</li>
	 * <li>SPEC_TILE_DEF -> Tile default value is returned. c value is ignored</li>
	 * <li>SPEC_TILE_COMP -> Tile/Component value is returned.</li>
	 * </ul>
	 * 
	 * @param type
	 *            The type of the value we want to be returned
	 * 
	 * @param t
	 *            The tile index
	 * 
	 * @param c
	 *            the component index
	 * 
	 * @return The code-block height for the specified tile and component
	 * */
    public int getCBlkHeight(byte type, int t, int c) {
        Integer dim[] = null;
        switch(type) {
            case SPEC_DEF:
                dim = (Integer[]) getDefault();
                break;
            case SPEC_COMP_DEF:
                dim = (Integer[]) getCompDef(c);
                break;
            case SPEC_TILE_DEF:
                dim = (Integer[]) getTileDef(t);
                break;
            case SPEC_TILE_COMP:
                dim = (Integer[]) getTileCompVal(t, c);
        }
        return dim[1].intValue();
    }

    /**
	 * Sets default value for this module
	 * 
	 * @param value
	 *            Default value
	 * */
    @Override
    public void setDefault(Object value) {
        super.setDefault(value);
        storeHighestDims((Integer[]) value);
    }

    /**
	 * Sets default value for specified tile and specValType tag if allowed by
	 * its priority.
	 * 
	 * @param c
	 *            Tile index.
	 * 
	 * @param value
	 *            Tile's default value
	 * */
    @Override
    public void setTileDef(int t, Object value) {
        super.setTileDef(t, value);
        storeHighestDims((Integer[]) value);
    }

    /**
	 * Sets default value for specified component and specValType tag if allowed
	 * by its priority.
	 * 
	 * @param c
	 *            Component index
	 * 
	 * @param value
	 *            Component's default value
	 * */
    @Override
    public void setCompDef(int c, Object value) {
        super.setCompDef(c, value);
        storeHighestDims((Integer[]) value);
    }

    /**
	 * Sets value for specified tile-component.
	 * 
	 * @param t
	 *            Tie index
	 * 
	 * @param c
	 *            Component index
	 * 
	 * @param value
	 *            Tile-component's value
	 * */
    @Override
    public void setTileCompVal(int t, int c, Object value) {
        super.setTileCompVal(t, c, value);
        storeHighestDims((Integer[]) value);
    }

    /**
	 * Stores the highest code-block width and height
	 * 
	 * @param dim
	 *            The 2 elements array that contains the code-block width and
	 *            height.
	 * */
    private void storeHighestDims(Integer[] dim) {
        if (dim[0].intValue() > maxCBlkWidth) {
            maxCBlkWidth = dim[0].intValue();
        }
        if (dim[1].intValue() > maxCBlkHeight) {
            maxCBlkHeight = dim[1].intValue();
        }
    }
}
