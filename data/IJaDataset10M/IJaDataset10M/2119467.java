package net.sf.astroinfo;

import net.sf.astroinfo.pdb.*;
import java.io.*;
import java.util.*;

public class CatalogRecord {

    public static final int DESCRIPTIONSIZE = 32;

    public static final double DARKOBJECT = -128.0;

    public static final int TYPE_UNKNOWN = 0;

    public static final int TYPE_ARTIFACT = TYPE_UNKNOWN + 1;

    public static final int TYPE_STAR = 1 << 4;

    public static final int TYPE_SINGLESTAR = TYPE_STAR + 1;

    public static final int TYPE_DOUBLESTAR = TYPE_STAR + 2;

    public static final int TYPE_MULTIPLESTAR = TYPE_STAR + 3;

    public static final int TYPE_CLUSTER = 2 << 4;

    public static final int TYPE_OPENCLUSTER = TYPE_CLUSTER + 1;

    public static final int TYPE_GLOBULARCLUSTER = TYPE_CLUSTER + 2;

    public static final int TYPE_ASTERISM = TYPE_CLUSTER + 3;

    public static final int TYPE_NEBULA = 3 << 4;

    public static final int TYPE_EMISSIONNEBULA = TYPE_NEBULA + 1;

    public static final int TYPE_REFLECTIONNEBULA = TYPE_NEBULA + 2;

    public static final int TYPE_DARKNEBULA = TYPE_NEBULA + 3;

    public static final int TYPE_PLANETARYNEBULA = TYPE_NEBULA + 4;

    public static final int TYPE_DIFFUSENEBULA = TYPE_NEBULA + 5;

    public static final int TYPE_OPENCLUSTERWITHNEBULOSITY = TYPE_NEBULA + 6;

    public static final int TYPE_GALAXY = 4 << 4;

    public static final int TYPE_COMPACTGALAXY = TYPE_GALAXY + 1;

    public static final int TYPE_DWARFGALAXY = TYPE_GALAXY + 2;

    public static final int TYPE_ELLIPTICALGALAXY = TYPE_GALAXY + 3;

    public static final int TYPE_IRREGULARGALAXY = TYPE_GALAXY + 4;

    public static final int TYPE_PECULIARGALAXY = TYPE_GALAXY + 5;

    public static final int TYPE_RINGGALAXY = TYPE_GALAXY + 6;

    public static final int TYPE_POLARRINGGALAXY = TYPE_GALAXY + 7;

    public static final int TYPE_SPIRALGALAXY = TYPE_GALAXY + 8;

    public static final int TYPE_BARREDSPIRALGALAXY = TYPE_GALAXY + 9;

    public static final int TYPE_DIVERSE = 5 << 4;

    public static final int TYPE_SUPERNOVAREMNANT = TYPE_DIVERSE;

    String description;

    double ra;

    double dec;

    double magnitude;

    double sizex;

    double sizey;

    String oclass;

    int type;

    int index;

    double distance;

    public CatalogRecord(int i) {
        index = i;
    }

    public String toString() {
        return "[" + description + ": (" + (ra * 12 / Math.PI) + "h),(" + (dec * 180 / Math.PI) + "°)," + magnitude + "," + sizex + "x" + sizey + "]" + "{" + type + "}," + distance;
    }

    public int getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public double parseAngle(StringTokenizer st) {
        String token = st.nextToken();
        boolean negative = false;
        if (token.charAt(0) == '-') {
            negative = true;
            token = token.substring(1);
        }
        double angle = Integer.parseInt(token);
        if (Catalog.noSeconds) {
            angle += Double.valueOf(st.nextToken()).doubleValue() / 60.0;
        } else {
            angle += Integer.parseInt(st.nextToken()) / 60.0;
            angle += Double.valueOf(st.nextToken()).doubleValue() / 3600.0;
        }
        if (negative) angle = -angle;
        return angle;
    }

    public void parseV2(String line) {
        StringTokenizer st = new StringTokenizer(line, "\t", false);
        String token;
        description = st.nextToken();
        if (description.length() >= DESCRIPTIONSIZE) description = description.substring(0, DESCRIPTIONSIZE - 1);
        if (description.equals("--")) description = "";
        ra = parseAngle(st) * (Math.PI / 12.0);
        if (ra >= Math.PI) ra -= 2 * Math.PI;
        dec = parseAngle(st) * (Math.PI / 180.0);
        token = st.nextToken();
        if (token.equals("--")) magnitude = 127.99; else if (token.equals("d")) magnitude = DARKOBJECT; else magnitude = Double.valueOf(token).doubleValue();
        token = st.nextToken();
        if (token.equals("--")) sizex = sizey = 0; else {
            int delim = token.indexOf('x');
            if (delim < 0) delim = token.indexOf('/');
            if (delim < 0) sizex = sizey = Double.valueOf(token).doubleValue(); else {
                sizex = Double.valueOf(token.substring(0, delim)).doubleValue();
                sizey = Double.valueOf(token.substring(delim + 1)).doubleValue();
            }
        }
        type = -1;
        if (st.hasMoreTokens()) {
            token = st.nextToken();
            if (!token.equals("--")) type = Integer.parseInt(token);
        }
        distance = -1.;
        if (st.hasMoreTokens()) {
            token = st.nextToken();
            if (!token.equals("--")) distance = Double.parseDouble(token);
        }
    }

    public double parseDouble(String field) {
        if (field.equals("$")) return Double.NaN; else return Double.parseDouble(field);
    }

    public int parseInt(String field, int undef) {
        if (field.equals("$")) return undef; else return Integer.parseInt(field);
    }

    public String parseString(String field) {
        if (field.equals("$")) return null; else return field;
    }

    public void parse(String prefix, String line) {
        StringTokenizer st = new StringTokenizer(line, "\t", false);
        String token;
        String constellation, catNr;
        int dummy;
        double posang, dist;
        String link, name = null;
        catNr = st.nextToken();
        constellation = st.nextToken();
        type = parseInt(st.nextToken(), 8);
        oclass = st.nextToken();
        magnitude = parseDouble(st.nextToken());
        ra = parseDouble(st.nextToken()) * (Math.PI / 180.0);
        if (ra >= Math.PI) ra -= 2 * Math.PI;
        dec = parseDouble(st.nextToken()) * (Math.PI / 180.0);
        dist = parseDouble(st.nextToken());
        sizex = parseDouble(st.nextToken());
        sizey = parseDouble(st.nextToken());
        posang = parseDouble(st.nextToken());
        link = parseString(st.nextToken());
        if (st.hasMoreTokens()) name = parseString(st.nextToken());
        distance = -1;
        if (st.hasMoreTokens()) distance = parseDouble(st.nextToken());
        description = prefix + catNr;
        if (name != null) description += " " + name;
        if (description.length() >= DESCRIPTIONSIZE) description = description.substring(0, DESCRIPTIONSIZE - 1);
    }

    public void write(PalmDataOutputStream out) throws IOException {
        if (sizex > 3270.0 || sizey > 3270.0 || magnitude > 128.0 || sizex < 0 || sizey < 0 || magnitude < -128.0) throw new IllegalArgumentException(toString());
        out.writeInt((int) (long) (ra * (1L << 31) / Math.PI));
        out.writeInt((int) (long) (dec * (1L << 31) / Math.PI));
        out.writeShort(Double.isNaN(magnitude) ? Short.MAX_VALUE : (int) (magnitude * 256));
        int sx = (int) (Double.isNaN(sizex) ? 0 : sizex * 10);
        int sy = (int) (Double.isNaN(sizey) ? sx : sizey * 10);
        out.writeShort(sx);
        out.writeShort(sy);
        out.writePackedString(description, false);
    }

    public void read(PalmDataInputStream in) throws IOException {
        ra = Math.PI / (1L << 31) * in.readInt();
        dec = Math.PI / (1L << 31) * in.readInt();
        magnitude = in.readShort() / 256.0;
        sizex = in.readShort() / 10.0;
        sizey = in.readShort() / 10.0;
        description = in.readPackedString();
    }
}
