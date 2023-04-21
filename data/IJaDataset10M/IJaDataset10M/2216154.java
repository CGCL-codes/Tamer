package rez.plugins.compactBSP;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.zip.*;
import java.lang.Math.*;
import rez.GridTile;
import rez.RezTileFW;
import rez.RezTileFW.RootTile;
import rez.SampledTileRow;
import rez.SamplingParams;
import rez.XPlat;
import rez.TileArray;
import rez.World;
import rez.utils.Debug;
import rez.utils.TextNumber;
import rez.utils.Compressor;
import rez.ModifiableText;
import imageutils.Image2PixelTexture;

/**
 * An implementation of a standard VRML ElevationGrid subdivision tiler which creates
 * trees in a compact binary tree format.  Quadtree output was not implemented because it requires more files to
 * be downloaded for each LOD change.  It is compact because the tree nodes are
 * in the same file as the tile nodes thus saving hugely on the number of files that have to be
 * doesnloaded over the internet.</BR>
 *
 * Note: Output units are determined and handled by the user and the Rez, RezTileFW classes by adjusting offsets, translations and spacing.
 *
 */
public class CompactVRMLTile extends RezTileFW {

    private static final boolean staticDebugOn = false;

    private static final double minFloat = Double.MIN_VALUE;

    private static final String lineSep = System.getProperty("line.separator");

    private boolean debugOn = false;

    private int minTileCells = 30;

    private int maxTileCells = 120;

    private double xzScale = 1;

    private float creaseAngle;

    private String tmpString;

    private String startEG;

    private String startEG2;

    private String startEG3;

    private String endEG;

    private FileWriter debugFile;

    private boolean outputDataNorthToSouth = false;

    private boolean sourceDataNorthToSouth = false;

    private boolean bsp = true;

    private double tmpz;

    private double tmpx;

    private double xbboxSize;

    private double zbboxSize;

    private String fileSep = XPlat.fileSep;

    private String fileExt;

    private Compressor compressor = new Compressor();

    private DataInputStream elevationInput[];

    private GridTile srcTileDescriptions[] = null;

    private int numSrcTiles;

    private int tileNumber = 0;

    private String outputDirString;

    private TileArray sourceTileArray;

    private static int numRowDivisions = 1;

    private static int numColDivisions = 1;

    private static int nextNumRowDivisions = 1;

    private static int nextNumColDivisions = 1;

    private int startRow = 0;

    private int endRow = 1;

    private int startColumn = 0;

    private int endColumn = 1;

    private boolean widthFirst = true;

    private BufferedWriter outputFile;

    private Image2PixelTexture i2pix;

    private boolean colourPerPolygon = true;

    /**
     *
     * <p><dt><b>Summary :</b></dt></p>
     * <dd>
     * Set the characteristics that may be queried by client programs (like Rez).
     *
     * </dd>
     */
    protected void setCharacteristics() {
        bTree = true;
        justBinary = true;
        outputAny = true;
        regularTile = true;
    }

    /**
     *
     * <p><dt><b>Summary :</b></dt></p>
     * <dd>
     * Initialises the class.
     * @param world attributes of the entire output model
     * @param sourceTileArray  Array of one or more height grids
     * @param sourceTile
     * @param compressionOn tile is to be compressed if true
     * </dd>
     */
    protected void setTilerValues(World world, TileArray sourceTileArray, GridTile sourceTile, boolean compressionOn) {
        try {
            System.out.println("CompactVRMLTile: xSpacing: " + getxSpacing() + " gridx: " + gridx);
            ModifiableText modifiableText = new ModText();
            startEG = modifiableText.startTile;
            startEG2 = modifiableText.tile2;
            startEG3 = modifiableText.tile3;
            endEG = modifiableText.endTile;
            fileSep = XPlat.fileSep;
            debugOn = Debug.debugOn;
            sourceDataNorthToSouth = sourceTile.northToSouth;
            fileSep = XPlat.fileSep;
            debugOn = Debug.debugOn;
            fileExt = ".wrl";
            if (compressionOn) fileExt = ".wrz";
            this.sourceTileArray = sourceTileArray;
            numSrcTiles = sourceTileArray.getLength();
            elevationInput = new DataInputStream[numSrcTiles];
            srcTileDescriptions = new GridTile[numSrcTiles];
            outputDirString = destinationDirectory + "tiles" + fileSep;
            xzScale = world.xzScale;
            rangeScale = world.getDetailScale();
            if (staticDebugOn) {
                File dirname = new File(outputDirString);
                if (!dirname.exists()) if (!dirname.mkdirs()) {
                    System.out.println("Tile: Could not create directory: " + outputDirString);
                }
                debugFile = new FileWriter(outputDirString + "Tiledebug.txt");
                Debug.debug("Tile: xSpacing:" + getxSpacing(), debugFile);
                Debug.debug("fileName is: " + prename);
                Debug.debug(" tile:end setvals");
                debugFile.close();
                if (sourceTileArray.tiles[0].xDimension < sourceTileArray.tiles[0].zDimension) widthFirst = false;
            }
        } catch (IOException e) {
            System.err.println("Error setting tiler values: " + e);
        }
    }

    /**
     * Opens the input geometry for reading - done at the start of a level and for each input file
     */
    protected void openInputGeometry(GridTile elevation, int level) {
        try {
            srcTileDescriptions[tileNumber] = elevation;
            File heightFile = new File(elevation.path);
            if (!heightFile.exists()) {
                System.out.println("Tile: Could not find file: " + elevation.path);
            }
            elevationInput[tileNumber] = new DataInputStream(new BufferedInputStream(new FileInputStream(heightFile)));
            File dirname = new File(outputDirString + Integer.toString(level));
            if (!dirname.exists()) if (!dirname.mkdirs()) {
                System.out.println("Tile: Could not create directory: " + outputDirString + Integer.toString(level));
            }
        } catch (IOException e) {
            System.err.println("Error opening input geometry" + e);
        }
        if (bsp) {
            if (widthFirst) {
                numRowDivisions = initialNumRowDivisions << (level / 2);
                numColDivisions = initialNumColDivisions << ((level + 1) / 2);
                nextNumRowDivisions = (level) % 2 + 1;
                nextNumColDivisions = (level + 1) % 2 + 1;
            } else {
                numRowDivisions = initialNumRowDivisions << ((level + 1) / 2);
                numColDivisions = initialNumColDivisions << (level / 2);
                nextNumRowDivisions = (level + 1) % 2 + 1;
                nextNumColDivisions = (level) % 2 + 1;
            }
        } else {
            numColDivisions = initialNumColDivisions << level;
            numRowDivisions = numColDivisions;
            nextNumColDivisions = 2;
            nextNumRowDivisions = nextNumColDivisions;
        }
        startRow = 0;
        endRow = 1;
        startColumn = 0;
        endColumn = 1;
    }

    /**
     * Sets the source tile.
     * A framework method that must be overriden by tilers that may need to work with multiple src tiles
     * at a time. Splitter tilers (such as this one) can ignore this: the combiner ones need it.
     */
    protected void setSource(String sourceTilePath, int srcTileNumber) {
        tileNumber = srcTileNumber;
        System.out.println("Tile: setSource: " + sourceTilePath);
        if (elevationInput[tileNumber] == null) {
            try {
                File heightFile = new File(sourceTilePath);
                if (!heightFile.exists()) {
                    System.out.println("Tile: Could not find file: " + sourceTilePath);
                }
                elevationInput[tileNumber] = new DataInputStream(new BufferedInputStream(new FileInputStream(heightFile)));
            } catch (IOException e) {
                System.err.println("Error setting source path for elevation" + e);
            }
        }
    }

    /**
     * returns a row of heights from the input geometry
     */
    protected void getRow(float[] rowHtArray, int xDimension, float heightScale) {
        try {
            float value = 0f;
            boolean realValues = srcTileDescriptions[tileNumber].getRealValues();
            int nbits = srcTileDescriptions[tileNumber].getNbits();
            for (int htcount = 0; htcount < xDimension; htcount++) {
                try {
                    if (realValues) {
                        if (nbits == 32) {
                            if (srcTileDescriptions[tileNumber].getByteOrder().indexOf("lsb") != -1) {
                                System.out.println("swapping 32bts ");
                                byte d = elevationInput[tileNumber].readByte();
                                byte c = elevationInput[tileNumber].readByte();
                                byte b = elevationInput[tileNumber].readByte();
                                byte a = elevationInput[tileNumber].readByte();
                                int lValue = ((a & 0xff) << 24) | ((b & 0xff) << 16) | ((c & 0xff) << 8) | (d & 0xff);
                                value = (float) lValue;
                            } else {
                                value = elevationInput[tileNumber].readFloat();
                            }
                        } else {
                            System.out.print("double: ");
                            value = (float) elevationInput[tileNumber].readDouble();
                        }
                    } else {
                        if (nbits == 8) {
                            byte byteValue = elevationInput[tileNumber].readByte();
                            value = (float) byteValue;
                        } else if (nbits == 16) {
                            short shortValue = elevationInput[tileNumber].readShort();
                            value = (float) shortValue;
                        } else {
                            value = (float) elevationInput[tileNumber].readInt();
                        }
                    }
                    rowHtArray[htcount] = value * heightScale;
                } catch (EOFException e) {
                    System.err.println("Error reading elevation" + e);
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading elevation (getRow) " + e);
        }
    }

    /**
     * simply reads (skips) a row of heights from the input geometry
     */
    protected void skipRow(int xDimension) {
        try {
            boolean realValues = srcTileDescriptions[tileNumber].getRealValues();
            int nbits = srcTileDescriptions[tileNumber].getNbits();
            for (int htcount = 0; htcount < xDimension; htcount++) {
                if (realValues) {
                    if (nbits == 32) {
                        elevationInput[tileNumber].readFloat();
                    } else {
                        elevationInput[tileNumber].readDouble();
                    }
                } else {
                    if (nbits == 8) {
                        elevationInput[tileNumber].readByte();
                    } else if (nbits == 16) {
                        elevationInput[tileNumber].readShort();
                    } else {
                        elevationInput[tileNumber].readInt();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading elevation (skip row)" + e);
        }
    }

    /**
     * closes the input geometry reference
     */
    protected void close(int tileNumber) {
        try {
            if (elevationInput[tileNumber] != null) elevationInput[tileNumber].close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * closes the input geometry reference
     */
    protected void close() {
        try {
            if (elevationInput[tileNumber] != null) {
                elevationInput[tileNumber].close();
                elevationInput[tileNumber] = null;
            }
        } catch (IOException e) {
            System.err.println("Error closing elevation input " + e);
        }
    }

    protected String tileNamingConvention(GridTile tileDescription, String dirString, String prename, int level) {
        tileDescription.path = dirString + "tiles" + fileSep + level + fileSep + prename + Integer.toString(outputTileArrayRow) + "-" + Integer.toString(outputTileArrayColumn) + ".wrl";
        tileDescription.texturePath = dirString + "images" + fileSep + level + fileSep + prename + Integer.toString(outputTileArrayRow) + "-" + Integer.toString(outputTileArrayColumn) + ".jpg";
        return (tileDescription.path);
    }

    protected String InlineTileNamingConvention(GridTile tileDescription, String prename, int level) {
        return (prename);
    }

    /**
     * This creates the output tile as a file.
     */
    protected Object newTile(GridTile tileDescription, String dirString, String prename, int level) {
        try {
            if (outputTileArrayRow == 0) {
                startRow = 0;
                endRow = nextNumRowDivisions - 1;
            } else {
                if (outputTileArrayColumn == 0) {
                    startRow += nextNumRowDivisions;
                    endRow += nextNumRowDivisions;
                }
            }
            if (outputTileArrayColumn == 0) {
                startColumn = 0;
                endColumn = nextNumColDivisions - 1;
            } else {
                startColumn += nextNumColDivisions;
                endColumn += nextNumColDivisions;
            }
            outputDataNorthToSouth = tileDescription.northToSouth;
            tileNamingConvention(tileDescription, dirString, prename, level);
            if (staticDebugOn) System.out.println("Tile: open file: " + tileDescription.path);
            return (new BufferedWriter(new FileWriter(tileDescription.path)));
        } catch (IOException e) {
            System.err.println("Error creating new tile" + e);
        }
        return null;
    }

    /** Tiles have a generic structure: a beginning, middle and end.  Each of these parts may have:</BR>
     *  1. a common part which can be set in the modifiable text properties
     *  (and is thereafter constant for each tile generated in a session) and </BR>
     *  2. a variable part - which varies with each tile's height values, position, etc and is therefore
     *  constucted by the set/write menthods in this class.
     */
    protected void setStartOfTile(GridTile tileDescription, Object outputTile, int level) {
        BufferedWriter outFile = (BufferedWriter) outputTile;
        try {
            if ((level == finalTreeLevel) || (!isSubdivide(outputTileArrayColumn))) {
                Debug.debug("creating leaf node");
                tmpString = startEG2 + lineSep;
            } else {
                tmpString = startEG + lineSep;
            }
            outFile.write(tmpString, 0, tmpString.length());
            xbboxSize = ((double) (tileDescription.xDimension - 1)) * tileDescription.getxSpacing();
            zbboxSize = ((double) (tileDescription.zDimension - 1)) * tileDescription.getzSpacing();
            String suffix = new String(Integer.toString(outputTileArrayRow) + "-" + Integer.toString(outputTileArrayColumn));
            if ((level != finalTreeLevel) && (isSubdivide(outputTileArrayColumn))) {
                Debug.debug("CompactVRMLTile: xcolumns  " + tileDescription.xDimension);
                Debug.debug(": rows  " + tileDescription.zDimension);
                Debug.debug("column: " + outputTileArrayColumn + "row: " + outputTileArrayRow + "xbb: " + xbboxSize + "," + " zbb " + zbboxSize);
                Debug.debug("  CompactVRMLTile: xTranslation: " + tileDescription.gridx + " zTranslation " + tileDescription.gridz);
                Debug.debug("  CompactVRMLTile: xTranslation: " + gridx + " zTranslation " + gridz);
                Debug.debug("level " + level + " endLevel " + finalTreeLevel);
                Debug.debug("calling subtree");
                if (bsp) {
                    if ((level & 1) == 1) {
                        buildInlineTree(level, tileDescription.gridx, xbboxSize, 0, tileDescription.gridz, zbboxSize, startRow, endRow, startColumn, endColumn, suffix, outFile);
                    } else {
                        buildInlineTree(level, tileDescription.gridx, xbboxSize, 0, tileDescription.gridz, zbboxSize, startRow, endRow, startColumn, endColumn, suffix, outFile);
                    }
                } else {
                    buildInlineTree(level, tileDescription.gridx, xbboxSize, 0, tileDescription.gridz, zbboxSize, startRow, endRow, startColumn, endColumn, suffix, outFile);
                }
            } else {
                Debug.debug("Tile: calling leaf");
                buildLeaf(level, tileDescription.gridx, xbboxSize, 0, tileDescription.gridz, zbboxSize, suffix, outFile);
            }
            tmpString = "Shape { " + lineSep + "appearance Appearance { material Material { ambientIntensity 0.8} texture ImageTexture {" + lineSep;
            outFile.write(tmpString, 0, tmpString.length());
            tmpString = "url \"" + "../../images/" + level + "/" + prename + outputTileArrayRow + "-" + outputTileArrayColumn + ".jpg\"" + startEG3;
            outFile.write(tmpString, 0, tmpString.length());
            tmpString = "geometry ElevationGrid { " + lineSep + "creaseAngle " + Float.toString(creaseAngle) + lineSep;
            outFile.write(tmpString, 0, tmpString.length());
            if (solid) tmpString = "solid TRUE" + lineSep; else tmpString = "solid FALSE" + lineSep;
            outFile.write(tmpString, 0, tmpString.length());
            if (colourPerPolygon) {
                String urlString = "images/" + level + "/" + prename + outputTileArrayRow + "-" + outputTileArrayColumn + ".jpg";
                try {
                    i2pix = new Image2PixelTexture(urlString);
                    String color = i2pix.GetPixelColor(urlString, true, true, tileDescription.zDimension, tileDescription.xDimension);
                    tmpString = "color Color { color  " + color + "}" + lineSep;
                    outFile.write(tmpString, 0, tmpString.length());
                } catch (java.lang.NoClassDefFoundError e) {
                    colourPerPolygon = false;
                }
            }
            tmpString = "xDimension " + Integer.toString(tileDescription.xDimension) + lineSep;
            outFile.write(tmpString, 0, tmpString.length());
            String sep = "";
            tmpString = "xSpacing " + sep + Float.toString((float) tileDescription.getxSpacing()) + sep + lineSep;
            outFile.write(tmpString, 0, tmpString.length());
            tmpString = "zDimension " + Integer.toString(tileDescription.zDimension) + lineSep;
            outFile.write(tmpString, 0, tmpString.length());
            tmpString = "zSpacing " + sep + Float.toString((float) tileDescription.getzSpacing()) + sep + lineSep + "height [ " + lineSep;
            outFile.write(tmpString, 0, tmpString.length());
        } catch (IOException e) {
            System.err.println("Error writing start of tile" + e);
        }
    }

    /**
     * Writes the next height value of the elevation.  It is provided with the height x,z indexes.
     */
    protected void writeHeightValue(float height, GridTile tileDescription, Object outputTile, int tilex, int tilez) {
        outputFile = (BufferedWriter) outputTile;
        try {
            tmpString = textNumber.shortestNumber(height);
            outputFile.write(tmpString, 0, tmpString.length());
        } catch (IOException e) {
            System.err.println("Error writing height value" + e);
        }
    }

    /**
     * Writes the next height value of the elevation.  It is provided with the height x,z
     * indexes and has a sequential index for convenience too.
     */
    protected void writeHeightValue(float height, GridTile tileDescription, Object outputTile, int tilex, int tilez, int index) {
    }

    /**
     * Writes whatever is needed to end the elevation, closes the file and does other housekeeping
     */
    protected void setEndOfTile(int level, GridTile tileDescription, Object outputTile, RootTile rootTile, boolean compressFlag) {
        BufferedWriter outFile = (BufferedWriter) outputTile;
        try {
            if ((level != finalTreeLevel) && (isSubdivide(outputTileArrayColumn))) {
                outFile.write(endEG, 0, endEG.length());
            } else {
                tmpString = "]}" + lineSep + "}}" + lineSep + "]}" + lineSep;
                outFile.write(tmpString, 0, tmpString.length());
            }
            outFile.close();
            if (compressFlag) {
                if (level == 0) {
                    rootTile.path = rootTile.path.substring(0, rootTile.path.length() - 1) + "z";
                }
                compressor.gzipCompress(tileDescription.path);
            }
        } catch (IOException e) {
            System.err.println("Error ending the tile" + e);
        }
    }

    /**
     * This method is a constructor for the root tile.
     */
    protected RootTile setRootTile(double xbase, double ybase, double zbase, double xSize, double zSize, String path) {
        return (new RootTile(xbase, ybase, zbase, xSize, zSize, path));
    }

    /**
     * This creates the subtile as a file.
     */
    private BufferedWriter newTileFile(String filename) {
        try {
            return (new BufferedWriter(new FileWriter(filename + ".wrl")));
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }

    private void buildInlineTree(int level, double xTranslation, double xbboxSize, double height, double zTranslation, double zbboxSize, int startRow, int endRow, int startColumn, int endColumn, String suffix, BufferedWriter outfile) {
        try {
            Debug.debug("Tree: xbb " + xbboxSize + "zbb " + zbboxSize + " level " + level);
            Debug.debug("Tree: startRow " + startRow + "endRow " + endRow + " startColumn " + endColumn + " endColumn " + startColumn);
            Debug.debug("build:Tree: xbb " + xbboxSize + "zbb " + zbboxSize + " level " + level);
            Debug.debug("build:Tree: xtrans " + xTranslation + "ztrans " + zTranslation + " level " + level);
            tmpString = " Group { children [ LOD { " + lineSep;
            tmpString = tmpString + "center " + (float) (xbboxSize / 2.0 + xTranslation) + " " + height + " " + (float) (zbboxSize / 2.0 + zTranslation) + lineSep + " range [" + (float) ((xbboxSize + zbboxSize) / 2.0 * rangeScale) + "]" + lineSep + "level [ Group { children [" + lineSep;
            outfile.write(tmpString, 0, tmpString.length());
            int n = 1;
            for (int zrow = startRow; zrow <= endRow; zrow++) {
                for (int xcolumn = startColumn; xcolumn <= endColumn; xcolumn++) {
                    Debug.debug(" zrow " + zrow + "xcol " + xcolumn);
                    tmpString = "Inline {url \"../../tiles/" + (level + 1) + "/" + prename + zrow + "-" + xcolumn + fileExt + "\"}" + lineSep;
                    outfile.write(tmpString, 0, tmpString.length());
                    n++;
                }
            }
            tmpString = "]}" + lineSep;
            outfile.write(tmpString, 0, tmpString.length());
            tmpString = "Group { children [ Transform { " + " translation " + (float) (xTranslation) + " " + height + " " + (float) (zTranslation) + " children " + lineSep;
            outfile.write(tmpString, 0, tmpString.length());
            Debug.debug("buildInlineTree end");
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void buildLeaf(int level, double xTranslation, double xbboxSize, double height, double zTranslation, double zbboxSize, String suffix, BufferedWriter outfile) {
        try {
            Debug.debug("leaf:Tree: xbb " + xbboxSize + "zbb " + zbboxSize + " level " + level);
            Debug.debug("build:Tree: xtans " + xTranslation + "ztrans " + zTranslation + " level " + level);
            tmpString = "#" + lineSep + "# This is a leaf" + lineSep + "Collision { children [ Transform {";
            outfile.write(tmpString, 0, tmpString.length());
            tmpString = " translation " + (float) (xTranslation) + " " + height + " " + (float) (zTranslation) + " children " + lineSep;
            outfile.write(tmpString, 0, tmpString.length());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void debug(String str) {
        if (debugOn) {
            System.out.println(str);
            try {
                debugFile.write(str + lineSep);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}
