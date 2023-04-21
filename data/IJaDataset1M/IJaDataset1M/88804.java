package com.sts.webmeet.server.util;

import java.io.File;
import com.sts.webmeet.server.PlaybackConstants;

public class Recordings {

    public static String getRecordingDir(String strID) {
        return getRootRecordingDir() + File.separator + strID;
    }

    public static String getRootRecordingDir() {
        return System.getProperty(PlaybackConstants.RECORDINGS_DIR_PROPERTY, PlaybackConstants.RECORDINGS_DIR_DEFAULT);
    }

    public static final String getArchiveForConf(String strConfID) {
        return PlaybackConstants.RECORDING_PREFIX + "_" + strConfID + ".zip";
    }

    public static final String getStreamRelativePath(String strConfID) {
        return strConfID + File.separator + PlaybackConstants.STREAM_FILE_ARCHIVE;
    }
}
