package rez.plugins;

import java.io.*;
import java.util.*;
import java.lang.Math.*;
import rez.ElevationParser;
import rez.XPlat;
import rez.utils.Debug;

/**
 * Same as ParseEG but treats everything as being in units of degrees.
 * Parses a VRML97 ElevationGrid or a GeoVRML GeoElevationGrid.  Results are returned
 * in the parent class parameters except for height values.  Heights are written
 * row-by-row to a height file in dem format to keep internal memory requirements
 * to a minimum.</P>
 */
public class ParseEGDegrees extends ElevationParser {

    static final int noGood = -99999999;

    static final int ELGRID = 0;

    static final int HEIGHT = 1;

    static final int CREASE = 2;

    static final int XDIM = 3;

    static final int XSPACE = 4;

    static final int ZDIM = 5;

    static final int ZSPACE = 6;

    static final int DONOTHING = 99;

    static final int NOTINGRID = -1;

    static final int INGRID = 0;

    static final int allReqsMet = 5;

    FileWriter debugFile;

    File heightOutputFile;

    private String nullOrigin = "geoGridOrigin \"0 0 0\"";

    float[] heights = null;

    boolean loadHeights = false;

    String tmpString;

    double height = 0;

    boolean geoElevationGrid;

    boolean debugOn = true;

    String outputFilePat = "";

    /**
     * Parses a VRML ElevationGrid or a GeoVRML GeoElevationGrid.
     * Since there is no rewuirement for the "header" information to come before the
     * height grid we cannot easilly just parse the header when only header info is required
     * because we may have to read through all the height values just to get to it.
     * Hence this code presently parses everyting each time it is called.
     */
    public void parseElevation(String inputFile, Object returnObject, float heightScale, String dataInfo, boolean invertData, boolean parseHeader, boolean sourceNorthToSouth) {
        String solid = "solid FALSE";
        int reqs = 0;
        debugOn = Debug.debugOn;
        int state;
        String line;
        boolean headerFound = false;
        DataOutputStream outFile = null;
        try {
            int dotindex = inputFile.lastIndexOf(".");
            int slashindex = 0;
            if (inputFile.indexOf(XPlat.fileSep) != -1) {
                slashindex = inputFile.lastIndexOf(XPlat.fileSep);
            }
            slashindex = inputFile.lastIndexOf("/");
            slashindex = slashindex < 0 ? 0 : slashindex;
            dotindex = dotindex < 0 ? 0 : dotindex;
            String prename = inputFile.substring(0, dotindex);
            StringTokenizer tokenizer;
            StringTokenizer bigTokenizer;
            BufferedReader d = new BufferedReader(new FileReader(inputFile));
            debugFile = new FileWriter("ParseEGdebug.txt");
            debug("Parsing input file:" + inputFile);
            System.out.println("Parsing input file:" + inputFile);
            debug(" from start ");
            boolean inProtoHeader = false;
            geoElevationGrid = false;
            line = d.readLine();
            if (line.indexOf("PROTO") != -1) {
                inProtoHeader = true;
                if (line.indexOf("GeoElevationGrid") != -1) geoElevationGrid = true;
            }
            state = NOTINGRID;
            boolean foundGrid = false;
            if (line.indexOf("ElevationGrid") != -1) foundGrid = true;
            while ((line != null) && (!foundGrid || inProtoHeader)) {
                if (line.indexOf("PROTO") != -1) inProtoHeader = true;
                if ((line.indexOf("]") != -1) && inProtoHeader) inProtoHeader = false;
                if (line.indexOf("GeoElevationGrid") != -1) geoElevationGrid = true;
                if (line.indexOf("ElevationGrid") != -1) foundGrid = true;
                line = d.readLine();
            }
            headerFound = false;
            state = ELGRID;
            while (true) {
                bigTokenizer = new StringTokenizer(line, " [],]\n\t\r\"");
                if (line.indexOf("height") != -1) {
                    debug("starting height line (s)");
                    state = HEIGHT;
                    reqs++;
                } else if (line.indexOf("geoSystem") != -1) {
                    headerFound = true;
                    state = DONOTHING;
                    geoSystem = line.trim();
                } else if (line.indexOf("geoGridOrigin") != -1) {
                    headerFound = true;
                    state = DONOTHING;
                    geoGridOrigin = line.trim();
                    debug("geoGridOrigin" + geoGridOrigin);
                } else if (line.indexOf("yScale") != -1) {
                    state = DONOTHING;
                    yScale = getfValue(bigTokenizer, line.trim(), "yScale");
                } else if (line.indexOf("solid") != -1) {
                    headerFound = true;
                    state = DONOTHING;
                    solid = line.trim();
                    reqs++;
                } else if (line.indexOf("creaseAngle") != -1) {
                    state = CREASE;
                    debug("crease");
                    headerFound = true;
                } else if (line.indexOf("xDimension") != -1) {
                    debug("XDim");
                    headerFound = true;
                    state = XDIM;
                    reqs++;
                } else if (line.indexOf("xSpacing") != -1) {
                    debug("XSpace");
                    headerFound = true;
                    state = XSPACE;
                    reqs++;
                } else if (line.indexOf("zDimension") != -1) {
                    debug("zDim");
                    headerFound = true;
                    state = ZDIM;
                    reqs++;
                } else if (line.indexOf("zSpacing") != -1) {
                    debug("zSpace");
                    headerFound = true;
                    reqs++;
                    state = ZSPACE;
                }
                if (state != NOTINGRID) {
                    int expectedNumValues = xDimension * zDimension;
                    if (loadHeights) heights = new float[expectedNumValues];
                    switch(state) {
                        case 1:
                            outFile = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(prename + ".dem")));
                            System.out.println(" Intermediate output File:" + prename + ".dem");
                            outputFilePath = prename + ".dem";
                            int htcount = 0;
                            int rowcount = 0;
                            String tmpToken = "";
                            debug("found the parameters now parse heights");
                            for (; ; ) {
                                int count = bigTokenizer.countTokens();
                                for (int i = 0; i < count; i++) {
                                    tmpToken = bigTokenizer.nextToken();
                                    if (tmpToken.equals("]")) break;
                                    if (!(tmpToken.equals("height") || tmpToken.equals("[") || tmpToken.equals("\t") || tmpToken.equals("\n") || tmpToken.equals(",") || tmpToken.equals(" "))) {
                                        try {
                                            int tmp = (int) (Float.valueOf(tmpToken).floatValue());
                                            if (loadHeights) {
                                                heights[htcount] = tmp;
                                            } else {
                                                outFile.writeShort(tmp);
                                            }
                                            htcount++;
                                        } catch (NumberFormatException e) {
                                            System.out.println("number format exception" + e.getMessage());
                                            if (htcount++ < expectedNumValues) {
                                                System.out.println("Too few height values: " + htcount + " " + e.getMessage());
                                            } else {
                                                outFile.close();
                                                state = NOTINGRID;
                                            }
                                            break;
                                        }
                                    }
                                }
                                if (tmpToken.equals("]")) {
                                    state = NOTINGRID;
                                    outFile.close();
                                    break;
                                }
                                line = d.readLine();
                                if (line == null) break;
                                bigTokenizer = new StringTokenizer(line, " [,]\n\t\r\"", true);
                            }
                            reqs++;
                            break;
                        case 2:
                            if ((creaseAngle = getfValue(bigTokenizer, line, "creaseAngle")) == noGood) debug(" Nogoodx:" + line);
                            break;
                        case 3:
                            debug("doing xDimension");
                            if ((xDimension = getValue(bigTokenizer, line, "xDimension")) == noGood) debug(" Nogoodx:" + line);
                            debug("xDimension" + xDimension);
                            break;
                        case 4:
                            if ((xSpacing = getfValue(bigTokenizer, line, "xSpacing")) == noGood) debug(" Nogoodx:" + line);
                            debug("xSpacing" + xSpacing);
                            break;
                        case 5:
                            if ((zDimension = getValue(bigTokenizer, line, "zDimension")) == noGood) debug(" Nogoodx:" + line);
                            debug("zDimension" + zDimension);
                            break;
                        case 6:
                            if ((zSpacing = getfValue(bigTokenizer, line, "zSpacing")) == noGood) {
                                debug(" Nogood z spacing:" + line);
                                debug(" Setting z spacing to xSpacing");
                                zSpacing = xSpacing;
                            }
                            debug("zSpacing" + zSpacing);
                            break;
                    }
                }
                line = d.readLine();
                if (line == null) {
                    if (reqs > allReqsMet) {
                        reqsMet = true;
                        break;
                    }
                    break;
                }
                if (reqs > allReqsMet) {
                    reqsMet = true;
                    break;
                }
            }
            super.reqsMet = true;
            if (!geoElevationGrid) {
                geoGridOrigin = nullOrigin;
            }
            bigTokenizer = new StringTokenizer(geoGridOrigin, " [],]\n\t\r\"");
            int count = bigTokenizer.countTokens();
            String tmpToken = bigTokenizer.nextToken();
            if (debugOn) debug(" ParseEG:geoGridOrigin: " + geoGridOrigin + " token:" + tmpToken);
            tmpToken = bigTokenizer.nextToken();
            if (debugOn) debug(" ParseEG:geoGridOrigin: " + geoGridOrigin + " gridx token:" + tmpToken);
            super.gridx = Float.valueOf(tmpToken).floatValue();
            tmpToken = bigTokenizer.nextToken();
            if (debugOn) debug(" ParseEG:gridz token:" + tmpToken);
            super.gridz = Float.valueOf(tmpToken).floatValue();
            super.reqsMet = true;
            super.units = "degrees";
            this.setRealValues(false);
            this.setNbits(16);
            this.setOutputFilePath(outputFilePath);
            d.close();
            outFile.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("ParseEG: Cannot find file " + fnfe.getMessage());
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
     * Parses a VRML ElevationGrid or a GeoVRML GeoElevationGrid.
     * Also returns the height values in an array.
     */
    public float[] parseElevation(String inputFile, Object returnObject, float heightScale, String Info, boolean invert, boolean sourceNorthToSouth) {
        loadHeights = true;
        parseElevation(inputFile, returnObject, heightScale, Info, false, false, sourceNorthToSouth);
        loadHeights = false;
        return (heights);
    }

    private String buildString(BufferedReader reader) throws IOException {
        String str;
        String line;
        line = reader.readLine();
        str = "";
        while (line != null) {
            str = str + line + "\n";
            line = reader.readLine();
        }
        reader.close();
        return (str);
    }

    private void debug(String str) {
        if (debugOn) {
            System.out.println(str);
            try {
                debugFile.write(str + "\n");
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    /** looks for a substring in the given string and if found will
     * return the following numeric value as an int.
     */
    public int getValue(StringTokenizer tokenizer, String line, String label) {
        String tmpToken;
        int tmp;
        tmpToken = tokenizer.nextToken();
        if (line.indexOf(label) != -1) {
            while (tmpToken.indexOf(label) == -1) {
                tmpToken = tokenizer.nextToken();
            }
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
}
