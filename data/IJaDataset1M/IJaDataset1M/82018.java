package net.sourceforge.magex.transfer.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *  MapInformation stores all the map's meta information.
 */
public class MapInformation {

    /** Map full name */
    public String name;

    /** X-coordinate of the top-left map corner */
    public int boundWest;

    /** Y-coordinate of the top-left map corner */
    public int boundSouth;

    /** X-coordinate of the bottom-right map corner */
    public int boundEast;

    /** Y-coordinate of the bottom-right map corner */
    public int boundNorth;

    /** Number of zoom levels */
    public byte zoomLevels;

    /** Rectangle horizontal sizes for each zoom level */
    public int[] rectangleSizesX;

    /** Rectangle vertical sizes for each zoom level */
    public int[] rectangleSizesY;

    /** If true, map is in RMS, if false map is in JAR */
    public boolean isInRMS;

    /** The total data size of the whole map */
    int fileSize;

    /** Bitmap of present indexes */
    int indexesPresent;

    /** Constructor -- empty */
    public MapInformation() {
    }

    /** Constructor -- reads all the information directly from a record.
	 *
	 *  @param in the input stream from which to read the data
	 *  @param isInRMS if true, the map is marked as stored in RMS
	 */
    public MapInformation(DataInputStream in, boolean isInRMS) throws IOException {
        in.readInt();
        this.name = in.readUTF();
        this.isInRMS = isInRMS;
        this.boundWest = in.readInt();
        this.boundSouth = in.readInt();
        this.boundEast = in.readInt();
        this.boundNorth = in.readInt();
        this.zoomLevels = in.readByte();
        this.rectangleSizesX = new int[zoomLevels];
        this.rectangleSizesY = new int[zoomLevels];
        for (int i = 0; i < zoomLevels; ++i) {
            this.rectangleSizesX[i] = in.readInt();
            this.rectangleSizesY[i] = in.readInt();
        }
        this.fileSize = in.readInt();
        this.indexesPresent = in.readInt();
    }

    /**
	 * Serializes a MapInformation instance into a data outputstream
	 * @param out - the data output stream where MapInformation will be serialized
	 * @throws java.io.IOException
	 */
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(0);
        out.flush();
        out.writeUTF(this.name);
        out.flush();
        out.writeInt(boundWest);
        out.flush();
        out.writeInt(boundSouth);
        out.flush();
        out.writeInt(boundEast);
        out.flush();
        out.writeInt(boundNorth);
        out.flush();
        out.writeByte(zoomLevels);
        out.flush();
        for (int i = 0; i < zoomLevels; ++i) {
            out.writeInt(rectangleSizesX[i]);
            out.flush();
            out.writeInt(rectangleSizesY[i]);
            out.flush();
        }
        out.writeInt(fileSize);
        out.flush();
        out.writeInt(indexesPresent);
        out.flush();
    }
}
