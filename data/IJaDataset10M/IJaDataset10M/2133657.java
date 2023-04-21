package jj2000.j2k;

import jj2000.j2k.util.*;
import java.util.*;

/**
 * This class extends ModuleSpec and is responsible of Integer specifications
 * for each tile-component.
 * 
 * @see ModuleSpec
 * */
public class IntegerSpec extends ModuleSpec {

    /** The largest value of type int */
    protected static int MAX_INT = Integer.MAX_VALUE;

    /**
	 * Constructs a new 'IntegerSpec' for the specified number of tiles and
	 * components and with allowed type of specifications. This constructor is
	 * normally called at decoder side.
	 * 
	 * @param nt
	 *            The number of tiles
	 * 
	 * @param nc
	 *            The number of components
	 * 
	 * @param type
	 *            The type of allowed specifications
	 * */
    public IntegerSpec(int nt, int nc, byte type) {
        super(nt, nc, type);
    }

    /**
	 * Constructs a new 'IntegerSpec' for the specified number of tiles and
	 * components, the allowed specifications type and the ParameterList
	 * instance. This constructor is normally called at encoder side and parse
	 * arguments of specified option.
	 * 
	 * @param nt
	 *            The number of tiles
	 * 
	 * @param nc
	 *            The number of components
	 * 
	 * @param type
	 *            The allowed specifications type
	 * 
	 * @param pl
	 *            The ParameterList instance
	 * 
	 * @param optName
	 *            The name of the option to process
	 * */
    public IntegerSpec(int nt, int nc, byte type, ParameterList pl, String optName) {
        super(nt, nc, type);
        Integer value;
        String param = pl.getParameter(optName);
        if (param == null) {
            param = pl.getDefaultParameterList().getParameter(optName);
            try {
                setDefault(new Integer(param));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Non recognized value for option -" + optName + ": " + param);
            }
            return;
        }
        StringTokenizer stk = new StringTokenizer(param);
        String word;
        byte curSpecType = SPEC_DEF;
        boolean[] tileSpec = null;
        boolean[] compSpec = null;
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
                    try {
                        value = new Integer(word);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Non recognized value for option -" + optName + ": " + word);
                    }
                    if (curSpecType == SPEC_DEF) {
                        setDefault(value);
                    } else if (curSpecType == SPEC_TILE_DEF) {
                        for (int i = tileSpec.length - 1; i >= 0; i--) if (tileSpec[i]) {
                            setTileDef(i, value);
                        }
                    } else if (curSpecType == SPEC_COMP_DEF) {
                        for (int i = compSpec.length - 1; i >= 0; i--) if (compSpec[i]) {
                            setCompDef(i, value);
                        }
                    } else {
                        for (int i = tileSpec.length - 1; i >= 0; i--) {
                            for (int j = compSpec.length - 1; j >= 0; j--) {
                                if (tileSpec[i] && compSpec[j]) {
                                    setTileCompVal(i, j, value);
                                }
                            }
                        }
                    }
                    curSpecType = SPEC_DEF;
                    tileSpec = null;
                    compSpec = null;
                    break;
            }
        }
        if (getDefault() == null) {
            int ndefspec = 0;
            for (int t = nt - 1; t >= 0; t--) {
                for (int c = nc - 1; c >= 0; c--) {
                    if (specValType[t][c] == SPEC_DEF) {
                        ndefspec++;
                    }
                }
            }
            if (ndefspec != 0) {
                param = pl.getDefaultParameterList().getParameter(optName);
                try {
                    setDefault(new Integer(param));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Non recognized value for option -" + optName + ": " + param);
                }
            } else {
                setDefault(getTileCompVal(0, 0));
                switch(specValType[0][0]) {
                    case SPEC_TILE_DEF:
                        for (int c = nc - 1; c >= 0; c--) {
                            if (specValType[0][c] == SPEC_TILE_DEF) specValType[0][c] = SPEC_DEF;
                        }
                        tileDef[0] = null;
                        break;
                    case SPEC_COMP_DEF:
                        for (int t = nt - 1; t >= 0; t--) {
                            if (specValType[t][0] == SPEC_COMP_DEF) specValType[t][0] = SPEC_DEF;
                        }
                        compDef[0] = null;
                        break;
                    case SPEC_TILE_COMP:
                        specValType[0][0] = SPEC_DEF;
                        tileCompVal.put("t0c0", null);
                        break;
                }
            }
        }
    }

    /**
	 * Gets the maximum value of all tile-components.
	 * 
	 * @return The maximum value
	 * */
    public int getMax() {
        int max = ((Integer) def).intValue();
        int tmp;
        for (int t = 0; t < nTiles; t++) {
            for (int c = 0; c < nComp; c++) {
                tmp = ((Integer) getSpec(t, c)).intValue();
                if (max < tmp) max = tmp;
            }
        }
        return max;
    }

    /**
	 * Get the minimum value of all tile-components.
	 * 
	 * @return The minimum value
	 * */
    public int getMin() {
        int min = ((Integer) def).intValue();
        int tmp;
        for (int t = 0; t < nTiles; t++) {
            for (int c = 0; c < nComp; c++) {
                tmp = ((Integer) getSpec(t, c)).intValue();
                if (min > tmp) min = tmp;
            }
        }
        return min;
    }

    /**
	 * Gets the maximum value of each tile for specified component
	 * 
	 * @param c
	 *            The component index
	 * 
	 * @return The maximum value
	 * */
    public int getMaxInComp(int c) {
        int max = 0;
        int tmp;
        for (int t = 0; t < nTiles; t++) {
            tmp = ((Integer) getSpec(t, c)).intValue();
            if (max < tmp) max = tmp;
        }
        return max;
    }

    /**
	 * Gets the minimum value of all tiles for the specified component.
	 * 
	 * @param c
	 *            The component index
	 * 
	 * @return The minimum value
	 * */
    public int getMinInComp(int c) {
        int min = MAX_INT;
        int tmp;
        for (int t = 0; t < nTiles; t++) {
            tmp = ((Integer) getSpec(t, c)).intValue();
            if (min > tmp) min = tmp;
        }
        return min;
    }

    /**
	 * Gets the maximum value of all components in the specified tile.
	 * 
	 * @param t
	 *            The tile index
	 * 
	 * @return The maximum value
	 * */
    public int getMaxInTile(int t) {
        int max = 0;
        int tmp;
        for (int c = 0; c < nComp; c++) {
            tmp = ((Integer) getSpec(t, c)).intValue();
            if (max < tmp) max = tmp;
        }
        return max;
    }

    /**
	 * Gets the minimum value of each component in specified tile
	 * 
	 * @param t
	 *            The tile index
	 * 
	 * @return The minimum value
	 * */
    public int getMinInTile(int t) {
        int min = MAX_INT;
        int tmp;
        for (int c = 0; c < nComp; c++) {
            tmp = ((Integer) getSpec(t, c)).intValue();
            if (min > tmp) min = tmp;
        }
        return min;
    }
}
