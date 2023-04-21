package gg.arkehion.utils;

import gg.arkehion.store.ArkDirConstants;

/**
 * Global Constants for Client and Server. Some are static final, some are
 * static only.
 * 
 * @author Frederic Bregier LGPL
 * 
 */
public class ArConstants {

    /**
     * Time elapse for retry in ms
     */
    public static final long RETRYINMS = 10;

    /**
     * Number of retry before error
     */
    public static final int RETRYNB = 3;

    /**
     * Number of retry before error on Delete
     */
    public static final int RETRYDELNB = 100;

    /**
     * Time elapse for retry in ms on Delete
     */
    public static final long RETRYDELINMS = 50;

    /**
     * Default size for buffers (NIO)
     */
    public static final int BUFFERSIZEDEFAULT = 65536;

    /**
     * Size of one block in the filesystem: could be later in the Legacy
     * definition. <br>
     * JFS2 supports multiple file system block sizes of 512, 1024, 2048, and
     * 4096, with 4 KBytes as standard. <br>
     * EXT3 supports multiple file system block sizes of 1024, 2048 and 4096,
     * with 4 KBytes as standard. <br>
     * NTFS supports sizes of clusters from 512 bytes up to 64 KBytes, with 4
     * KBytes as standard.
     */
    public static final long SIZEBLOCKFS = 4096;

    /**
     * Size of one block in the filesystem (double version): could be later in
     * the Legacy definition.
     */
    public static final double DSIZEBLOCKFS = 4096.0;

    /**
     * Minimum size for block for receive/sending files
     */
    public static final int MINBLOCKSIZE = 1024;

    /**
     * Maximum number of file in list (AEXIST, LIST) : nb*100
     */
    public static final int MAXIMUM_FILELIST = 500;

    /**
     * Default separator in String buffer in case of List
     */
    public static final char separator = '#';

    /**
     * Same as separator in String format
     */
    public static final String sseparator = "#";

    /**
     * Test if ths idUnique is KO
     * 
     * @param idUnique
     * @return True if idUnique is KO, else False
     */
    public static final boolean isIdUniqueKO(long idUnique) {
        return ((idUnique <= ArkDirConstants.invalide_idx) || (idUnique > ArkDirConstants.maximal_idx));
    }
}
