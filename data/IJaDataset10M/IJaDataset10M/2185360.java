package it.unipi.miabot.data;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The blob track data type interface.
 * <p>
 * The Miabot camera is able to track up to 8 different color blobs at a 30
 * frame per second speed.
 * <p>
 * This interface allows the user to get a "burst" of tracking packets from the
 * miabot.
 * 
 * @author Luca Benedetti
 */
public interface BlobTrack {

    /**
   * Returns the color map active when the track begun.
   * 
   * @return the color map active when the track begun.
   */
    public BlobColorMap getColorMap();

    /**
   * Returns the time stamp of the track burst.
   * 
   * @return the time stamp of the track burst.
   */
    public long getTimestamp();

    /**
   * Returns the track burst data.
   * <p>
   * A "burst" of data is a time ordered list of "track packets", where each one
   * of these packets is a mapping from color range indexes to sets of
   * recttangles.
   * <p>
   * <strong>Warning:</strong> modifications to the object returned by this
   * method will invalidate the current BlobTrack instance.
   * 
   * @return the track burst data.
   */
    public List<Map<Integer, Set<Rectangle>>> getTrackList();

    /**
   * Returns true if the camera had the auto adjust enabled when the track
   * begun.
   * 
   * @return true if the camera had the auto adjust enabled when the track
   *         begun.
   */
    public boolean hadAutoAdjustEnabled();

    /**
   * Returns true if the camera had the auto white balance enabled when the
   * track begun.
   * 
   * @return true if the camera had the auto white balance enabled when the
   *         track begun.
   */
    public boolean hadAutoWhiteBalanceEnabled();

    /**
   * Returns true if the camera had the light filter enabled when the track
   * begun.
   * 
   * @return true if the camera had the light filter enabled when the track
   *         begun.
   */
    public boolean hadLightFilterEnabled();
}
