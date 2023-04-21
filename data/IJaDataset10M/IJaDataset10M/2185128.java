package gg.arkehion.store;

import gg.arkehion.exceptions.ArUnvalidIndexException;
import java.io.File;

/**
 * @author frederic
 * 
 */
public interface ArkDirFunctionInterface {

    /**
     * Constructs the subpath
     * 
     * @param idUnique
     * @return the String for the subPath /a/b/c
     * @throws ArUnvalidIndexException
     */
    public String idUniqueTosubPath(long idUnique) throws ArUnvalidIndexException;

    /**
     * Constructs the paths
     * 
     * @param idUnique
     * @return the array of String
     *         (GlobalPath,Basename,PathwoBasename,AbstractPath)
     * @throws ArUnvalidIndexException
     */
    public String[] idUniqueToStrings(long idUnique) throws ArUnvalidIndexException;

    /**
     * Constructs the IDs from path
     * 
     * @param path
     * @return the array of IDs corresponding to the path
     * @throws ArUnvalidIndexException
     */
    public long[] pathToIdUnique(String path) throws ArUnvalidIndexException;

    /**
     * Compute size according to the Size of Block
     * 
     * @param size
     * @return the new size closest to the number of bytes as a integer number
     *         of blocks
     */
    public long sizeAsBlockFs(long size);

    /**
     * Get the Global, Used and Free spaces in KB
     * 
     * @param legacy
     * @param storage
     * @return the global and used and free space in KB in double[] or null in
     *         error
     */
    public double[] getGlobalUsedAndFreeSpaces(ArkLegacyInterface legacy, String storage);

    /**
     * Get the Used space in KB, the number of directories and the number of
     * files
     * 
     * @param legacy
     * @param storage
     * @return the used space in KB, the number of directories and the number of
     *         files
     */
    public double[] getUsedSpaceNbDirNbFile(ArkLegacyInterface legacy, String storage);

    /**
     * Get the list of Files under the path.
     * 
     * the out files will contain triplet of index lid sid did of all files
     * under the path. If more than maxInFile files are found, more files with
     * extension .number will be generated.
     * 
     * @param legacy
     * @param out
     *            out filepath, where extension .0, .1, .2, ... will be
     *            generated
     * @param maxInFile
     *            maximum number of reference in a out file
     * @param path
     * @param refTime
     *            referenced Time (in ms) as only files with modification time >
     *            refTime will be listed
     * @return the number of output files generated or -1 if an error occurs
     */
    public long getListOfFiles(ArkLegacyInterface legacy, File out, long maxInFile, String path, long refTime);

    /**
     * Get the list of Files under the path.
     * 
     * the out file will contain triplet of index lid sid did of all files under
     * the path and the associated Mark (MD5 or SHA1/xxx or CRC32 or ADLER32).
     * If more than maxInFile files are found, more files with extension .number
     * will be generated.
     * 
     * @param legacy
     * @param out
     *            out filepath, where extension .0, .1, .2, ... will be
     *            generated
     * @param maxInFile
     *            maximum number of reference in a out file
     * @param path
     * @param refTime
     *            referenced Time (in ms) as only files with modification time >
     *            refTime will be listed
     * @return the number of output files generated or -1 if an error occurs
     */
    public long getListOfFilesMark(ArkLegacyInterface legacy, File out, long maxInFile, String path, long refTime);

    /**
     * Get the list of Files under the path while checking the list according to a given List as
     * from getListOfFilesMark.
     * 
     * The out file will contain triplet of index lid sid did of all files under
     * the path and the original Mark to check with and the associated Mark (MD5 or SHA1/xxx or CRC32 or ADLER32).
     * If more than maxInFile files are found, more files with extension .number
     * will be generated.<br>
     * <br>
     * The out file will contain all elements from the given list while,<br>
     * if one is missing, the second Mark will be accordingly replaced by NotFound,<br> 
     * if one is mismatching, the second Mark will be accordingly replaced by NotMatchingDKey,<br>
     * if one is incorrectly given on source, the second Mark will be accordingly replaced by NotCorrectIndex,<br>
     * if one is not in the given Legacy, the second Mark will be accordingly replaced by NotSameLegacy.
     * 
     * @param legacy
     * @param in
     *            in filepath, where extension .0, .1, .2, ... will be
     *            generated from 0 to (inNb-1). Note for correctness, the "in" list should be
     *           ordered (Legacy/Storage/DocumentId in that order).
     * @param inNb
     * 				the number of files for "in"
     * @param out
     *            out filepath, where extension .0, .1, .2, ... will be
     *            generated
     * @param maxInFile
     *            maximum number of reference in a out file
     * @param path
     * @param refTime
     *            referenced Time (in ms) as only files with modification time >
     *            refTime will be listed
     * @return the number of output files generated or -1 if an error occurs
     */
    public long checkFilesMark(ArkLegacyInterface legacy, File in, long inNb, File out, long maxInFile, String path, long refTime);

    /**
     * Delete the list of Files under the path while checking the list according to a given List
     * as from getListOfFilesMark.
     * 
     * The out file will contain triplet of index lid sid did of all files under
     * the path and the original Mark to check on and a status.
     * If more than maxInFile files are found, more files with extension .number
     * will be generated.<br>
     * <br>
     * The out file will contain all elements from the given list while,<br>
     * if one is missing, the status will be NotFound,<br> 
     * if one is mismatching, the status will be NotMatchingDKey,<br>
     * if one is incorrectly given on source, the status will be NotCorrectIndex,<br>
     * if one is not in the given Legacy, the status will be NotSameLegacy, <br>
     * finally if it is ok and deleted, the status will be Deleted.

     * @param legacy
     * @param in
     *            in filepath, where extension .0, .1, .2, ... will be
     *            generated from 0 to (inNb-1).
     * @param inNb
     * 				the number of files for "in"
     * @param out
     *            out filepath, where extension .0, .1, .2, ... will be
     *            generated
     * @param maxInFile
     *            maximum number of reference in a out file
     * @param path
     * @param refTime
     *            referenced Time (in ms) as only files with modification time >
     *            refTime will be listed
     * @return the number of output files generated or -1 if an error occurs
     */
    public long deleteFilesMark(ArkLegacyInterface legacy, File in, long inNb, File out, long maxInFile, String path, long refTime);
}
