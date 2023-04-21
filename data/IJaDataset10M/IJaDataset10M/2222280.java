package org.verus.ngl.sl.bprocess.technicalprocessing;

/**
 *
 * @author root
 */
public interface ComplexVolume {

    public long getCountByVolumeId(Integer volumeId, Integer libraryId, String databaseId);

    public int getVolumeId(Integer volumeId, Integer libraryId, String databaseId);
}
