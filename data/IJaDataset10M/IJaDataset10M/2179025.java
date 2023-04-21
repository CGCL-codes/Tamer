package net.iTunes;

import com4j.*;

/**
 * IITArtwork Interface
 */
@IID("{D0A6C1F8-BF3D-4CD8-AC47-FE32BDD17257}")
public interface IITArtwork extends Com4jObject {

    /**
     * Delete this piece of artwork from the track.
     */
    @VTID(7)
    void delete();

    /**
     * Replace existing artwork data with new artwork from an image file.
     */
    @VTID(8)
    void setArtworkFromFile(java.lang.String filePath);

    /**
     * Save artwork data to an image file.
     */
    @VTID(9)
    void saveArtworkToFile(java.lang.String filePath);

    /**
     * The format of the artwork.
     */
    @VTID(10)
    net.iTunes.ITArtworkFormat format();

    /**
     * True if the artwork was downloaded by net.iTunes.
     */
    @VTID(11)
    boolean isDownloadedArtwork();
}
