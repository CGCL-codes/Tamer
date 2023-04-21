package com.sen.imageoperation;

public class OperationHelper {

    private static boolean sNeedAlbumUpdate = false;

    private static boolean sNeedImagesUpdate = false;

    private static boolean sNeedTotalUpdate = false;

    private static boolean sAlbumWasDeleted = false;

    public static boolean getFlagAlbumUpdate() {
        return sNeedAlbumUpdate;
    }

    public static void setFlagAlbumUpdate(boolean flag) {
        sNeedAlbumUpdate = flag;
    }

    public static boolean getFlagImagesUpdate() {
        return sNeedImagesUpdate;
    }

    public static void setFlagImagesUpdate(boolean flag) {
        sNeedImagesUpdate = flag;
    }

    public static boolean getFlagTotalUpdate() {
        return sNeedTotalUpdate;
    }

    public static void setFlagTotalUpdate(boolean flag) {
        sNeedTotalUpdate = flag;
    }

    public static void setFlagAlbumDeleted(boolean flag) {
        sAlbumWasDeleted = flag;
    }

    public static boolean getFlagAlbumDeleted() {
        return sAlbumWasDeleted;
    }
}
