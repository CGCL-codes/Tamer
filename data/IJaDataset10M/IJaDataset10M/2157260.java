package rez.plugins;

import java.io.*;
import java.util.*;
import rez.ElevationParser;
import rez.XPlat;
import rez.utils.Debug;

/**
 * This class will parse a Gtopo30 DEM header.  Results are returned in the parent class parameters
 * except for height values.  Heights are already in a suitable format for the output tilers.</P>
 */
public class ParseTopo extends ElevationParser {

    static final int allReqsMet = 5;

    static final int noGood = -99999999;

    private static final boolean staticDebugOn = Boolean.getBoolean("debugOn");

    boolean debugOn = false;

    FileWriter debugFile;

    /**
     * For Gtopo format we only need to read the header as the tiler can directly read the elevation.
     * If the elevation is also required see the next parseElevation below.
     */
    public void parseElevation(String inputFile, Object returnObject, float heightScale, String dataInfo, boolean invertData, boolean parseHeader, boolean sourceNorthToSouth) {
        readHeader(inputFile);
        this.setRealValues(realValues);
        this.setNbits(nbits);
    }

    /**
     *  Reads a Gtopo ".hdr" file.
     */
    private void readHeader(String inputFile) {
        debugOn = Debug.debugOn;
        double ULXMAP = 1;
        double ULYMAP = 1;
        int reqs = 0;
        String valueType = "int";
        try {
            System.out.println("parseTopo: inputFile: " + inputFile);
            debugFile = new FileWriter("ParseTopoDebug.txt");
            int dotindex = inputFile.lastIndexOf(".");
            int slashindex = 0;
            if (inputFile.indexOf(XPlat.fileSep) != -1) {
                slashindex = inputFile.lastIndexOf(XPlat.fileSep);
            }
            slashindex = inputFile.lastIndexOf("/");
            slashindex = slashindex < 0 ? 0 : slashindex;
            dotindex = dotindex < 0 ? 0 : dotindex;
            String prename = inputFile.substring(0, dotindex);
            System.out.println("parseTopo: inputFile: " + inputFile);
            File dataFile = new File(prename + ".hdr");
            if (!dataFile.exists()) {
                System.out.println("ParseTopo: Could not find file: " + prename + ".hdr");
                throw new IOException("file not found");
            }
            BufferedReader b = new BufferedReader(new FileReader(prename + ".hdr"));
            if (debugOn) debug("read header");
            units = "degrees";
            String line = b.readLine();
            while (line != null) {
                if (debugOn) debug("line: " + line);
                line = line.toLowerCase();
                StringTokenizer bigTokenizer = new StringTokenizer(line, " \n\t\r\"", false);
                if (line.indexOf("ncols") != -1) {
                    if ((xDimension = getValue(bigTokenizer, line, "ncols")) == noGood) {
                        if (debugOn) debug(" Nogood:" + line);
                    } else {
                        reqs++;
                        if (debugOn) debug("xDimension" + xDimension);
                    }
                } else if (line.indexOf("nrows") != -1) {
                    if ((zDimension = getValue(bigTokenizer, line, "nrows")) == noGood) {
                        if (debugOn) debug(" Nogood:" + line);
                    } else {
                        reqs++;
                        if (debugOn) debug("zDimension" + zDimension);
                    }
                } else if (line.indexOf("xdim") != -1) {
                    if ((xSpacing = getdValue(bigTokenizer, line, "xdim")) == noGood) {
                        if (debugOn) debug(" Nogood:" + line);
                    } else {
                        reqs++;
                        if (debugOn) debug("xSpacing" + xSpacing);
                    }
                } else if (line.indexOf("ydim") != -1) {
                    if ((zSpacing = getdValue(bigTokenizer, line, "ydim")) == noGood) {
                        if (debugOn) debug(" Nogood:" + line);
                    } else {
                        reqs++;
                        if (debugOn) debug("zSpacing" + zSpacing);
                    }
                } else if (line.indexOf("ulxmap") != -1) {
                    if ((ULXMAP = getdValue(bigTokenizer, line, "ulxmap")) == noGood) {
                        if (debugOn) debug(" Nogood:" + line);
                    } else {
                        reqs++;
                        if (debugOn) debug("ULXMAP" + ULXMAP);
                    }
                } else if (line.indexOf("ulymap") != -1) {
                    if ((ULYMAP = getdValue(bigTokenizer, line, "ulymap")) == noGood) {
                        if (debugOn) debug(" Nogood:" + line);
                    } else {
                        reqs++;
                        if (debugOn) debug("ULYMAP" + ULYMAP);
                    }
                } else if ((line.indexOf("valuetype") != -1)) {
                    valueType = bigTokenizer.nextToken();
                    valueType = bigTokenizer.nextToken();
                    System.out.println("valueType: " + valueType);
                } else if ((line.indexOf("byteorder") != -1)) {
                    setByteOrder(bigTokenizer.nextToken());
                    setByteOrder(bigTokenizer.nextToken());
                    if (getByteOrder().equals("m")) setByteOrder("msb");
                    if (getByteOrder().equals("l")) setByteOrder("lsb");
                    System.out.println("byteorder: " + line);
                    System.out.println("byteorder: " + getByteOrder());
                } else if (line.indexOf("nbits") != -1) {
                    if ((nbits = getValue(bigTokenizer, line, "nbits")) == noGood) {
                        if (debugOn) debug(" Nogood:" + line);
                    } else {
                        if (debugOn) debug("nbits" + nbits);
                    }
                } else if (line.indexOf("datum") != -1) {
                    bigTokenizer = new StringTokenizer(line, " \n\t\r");
                    datum = bigTokenizer.nextToken();
                    datum = bigTokenizer.nextToken();
                    if (datum.equals("")) {
                        if (debugOn) debug("Datum null in line: " + line);
                    } else {
                        reqs++;
                        if (debugOn) debug("datum" + datum);
                    }
                } else if (line.indexOf("zone") != -1) {
                    bigTokenizer = new StringTokenizer(line, " \n\t\r");
                    projn = bigTokenizer.nextToken();
                    zone = bigTokenizer.nextToken();
                    zone = bigTokenizer.nextToken();
                    reqs++;
                    if (debugOn) debug("projn " + projn);
                } else if (line.indexOf("units") != -1) {
                    bigTokenizer = new StringTokenizer(line, " \n\t\r");
                    units = bigTokenizer.nextToken();
                    units = bigTokenizer.nextToken();
                    reqs++;
                    if (debugOn) debug("units " + units);
                    if (units.toLowerCase().indexOf("meters") != -1) {
                        degrees = false;
                    }
                } else if (line.indexOf("texture") != -1) {
                    bigTokenizer = new StringTokenizer(line, " \n\t\r");
                    texturePath = bigTokenizer.nextToken();
                    texturePath = bigTokenizer.nextToken();
                    reqs++;
                    if (debugOn) debug("texture " + texturePath);
                }
                line = b.readLine();
            }
            geoSystem = "geoSystem [ \"GD\" \"WE\" ]";
            if (debugOn) debug(" xDim:" + xDimension + " zDim " + zDimension + "\n");
            if (debugOn) debug("Requirements satisfied: " + reqs + "\n");
            if (reqs > allReqsMet) reqsMet = true;
            gridx = ULXMAP;
            gridz = ULYMAP;
            yScale = 1;
            units = "degrees";
            if (valueType.toLowerCase().equals("real")) realValues = true;
            this.setOutputFilePath(inputFile);
            this.setRealValues(realValues);
            this.setNbits(nbits);
        } catch (FileNotFoundException fnfe) {
            System.out.println("Cannot find file " + fnfe.getMessage());
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            try {
                debugFile.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    /**
     * Reads the elevation which is returned in an array.
     * The array is row major with each successive row element representing 
     * a value positiong further east than the previouse one.
     *
     */
    public float[] parseElevation(String inputFile, Object returnObject, float heightScale, String Info, boolean invert, boolean sourceNorthToSouth) {
        System.out.println("parse Elevation:inputFile " + inputFile);
        readHeader(inputFile);
        int dotindex = inputFile.lastIndexOf(".");
        int slashindex = 0;
        if (inputFile.indexOf(XPlat.fileSep) != -1) {
            slashindex = inputFile.lastIndexOf(XPlat.fileSep);
        }
        slashindex = inputFile.lastIndexOf("/");
        slashindex = slashindex < 0 ? 0 : slashindex;
        dotindex = dotindex < 0 ? 0 : dotindex;
        String prename = inputFile.substring(0, dotindex);
        float[] heights = null;
        try {
            DataInputStream dataIn = new DataInputStream(new BufferedInputStream(new FileInputStream(prename + ".dem")));
            heights = new float[zDimension * xDimension];
            int htCount = 0;
            boolean breakout = false;
            for (int rowCount = 0; rowCount < zDimension; rowCount++) {
                for (int colCount = 0; colCount < xDimension; colCount++) {
                    try {
                        float value;
                        if (realValues) {
                            if (nbits == 64) {
                                value = (float) dataIn.readDouble();
                            } else value = dataIn.readFloat();
                            System.out.println("float \n");
                        } else {
                            if (nbits == 8) {
                                value = (float) dataIn.readByte();
                            } else if (nbits == 16) {
                                value = (float) dataIn.readShort();
                            } else if (nbits == 32) {
                                value = (float) dataIn.readInt();
                            } else value = (float) dataIn.readInt();
                        }
                        heights[htCount++] = value;
                    } catch (EOFException e) {
                        System.err.println(" ParseTopo, ReadElevation: EOF, eastWestDimension " + xDimension);
                        breakout = true;
                        System.err.println(e);
                        break;
                    } catch (IOException e) {
                        System.err.println(" problem reading file, rowCount " + rowCount + ", column: " + colCount);
                        System.err.println(e);
                        break;
                    }
                }
                if (breakout) {
                    dataIn.close();
                    break;
                }
            }
            dataIn.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Cannot find file " + fnfe.getMessage());
        } catch (IOException e) {
            System.err.println(e);
        }
        return heights;
    }

    /** looks for a substring in the given string and if found will
     * return the following numeric value as an int.
     */
    public int getValue(StringTokenizer tokenizer, String line, String label) {
        String tmpToken;
        int tmp;
        tmpToken = tokenizer.nextToken();
        if (line.indexOf(label) != -1) {
            if (Debug.debugOn) debug("token " + tmpToken);
            tmpToken = tokenizer.nextToken();
            tmp = Integer.parseInt(tmpToken);
            return tmp;
        } else return noGood;
    }

    /** looks for a substring in the given string and if found will
     * return the following numeric value as an int.
     */
    public float getfValue(StringTokenizer tokenizer, String line, String label) {
        String tmpToken;
        float tmp;
        tmpToken = tokenizer.nextToken();
        if (line.indexOf(label) != -1) {
            while (tmpToken.indexOf(label) == -1) {
                tmpToken = tokenizer.nextToken();
            }
            tmpToken = tokenizer.nextToken();
            tmp = Float.valueOf(tmpToken).floatValue();
            return tmp;
        } else return noGood;
    }

    /** looks for a substring in the given string and if found will
     * return the following numeric value as a double.
     */
    public double getdValue(StringTokenizer tokenizer, String line, String label) {
        String tmpToken;
        double tmp;
        tmpToken = tokenizer.nextToken();
        if (line.indexOf(label) != -1) {
            while (tmpToken.indexOf(label) == -1) {
                tmpToken = tokenizer.nextToken();
            }
            tmpToken = tokenizer.nextToken();
            tmp = Double.valueOf(tmpToken).doubleValue();
            return tmp;
        } else return noGood;
    }

    private void debug(String str) {
        if (Debug.debugOn) {
            Debug.debug(str, debugFile);
        }
    }
}
