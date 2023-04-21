package org.oclc.da.ndiipp.packager.pvt;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import org.oclc.da.exceptions.DAException;
import org.oclc.da.exceptions.DAExceptionCodes;
import org.oclc.da.logging.Logger;
import org.oclc.da.ndiipp.common.CallerIdentity;
import org.oclc.da.ndiipp.common.DataObject;
import org.oclc.da.ndiipp.dcmetadata.DCElementConst;
import org.oclc.da.ndiipp.dcmetadata.DCElementSetNames;
import org.oclc.da.ndiipp.dcmetadata.DCMetadata;
import org.oclc.da.ndiipp.dcmetadata.DCMetadataMaker;
import org.oclc.da.ndiipp.packagecontainer.PackageContainer;
import org.oclc.da.ndiipp.packagecontainer.PackageContainerConst;
import org.oclc.da.ndiipp.packager.PackageBuilder;
import org.oclc.da.ndiipp.packager.PackageDetails;
import org.oclc.da.ndiipp.packager.PackageResults;
import org.oclc.da.ndiipp.spider.FileInfo;
import org.oclc.da.ndiipp.spider.Link;
import org.oclc.da.utils.ContentType;
import org.oclc.da.utils.IOUtils;
import org.oclc.da.utils.ZipFileCreator;

/**
 * HAndSPackageBuilder
 *
 * This is a class which implements PackageBuilder interface
 * implements the build method. The build method builds Hub
 * and Spoke package
 *  
 * @author SR
 * @version 1.0, 
 * @created 01/18/2007
 */
public class HAndSPackageBuilder implements PackageBuilder {

    /** DA */
    private static final String HS = "HS";

    /** Package Type */
    private static final String HUB_AND_SPOKE = "HubAndSpoke";

    /** Not ready extension for DA batches. This extension prevents the batch
     * copy process from picking up batches that have not yet been submitted.
     * */
    private static final String BATCH_NOTREADY = "notready";

    /** Ready extension for DA batches. This extension indicates that 
	 * the batch is ready to be copied.
     * */
    private static final String BATCH_READY = "ready";

    /** Zip extension
    * */
    private static final String ZIP = "zip";

    /** Package Details instance */
    private PackageDetails m_pkgDetails;

    /** Caller identity instance */
    private CallerIdentity m_identity;

    /** String to store Inst Symbol */
    private String m_strInstSymbol;

    /** String to store BatchNumber */
    private String m_strBatchNumber;

    /** String to store Batch Path */
    private String m_strBatchPath;

    /** String to store final Batch Path */
    private String m_strFinalBatchPath;

    /** String to store tmp mets file loc */
    private String m_tmpMetsFileLoc;

    /** DC metadata for current object. */
    private HAndSObjectInfo m_objectInfo;

    /** DC metadata for current object. */
    private DCMetadata m_dcMeta;

    private DCMetadataMaker m_metadataMaker;

    /** Current object count. */
    private int m_objCount = 0;

    /** Current object count. */
    private int m_fileCount = 0;

    /** Current object ID. */
    private String m_objID;

    /** Vector to store FileInfo for the current object. */
    private Vector<FileInfo> m_vfileInfo;

    /** A temporary directory for heritrix to store the spider results in. */
    private PackageDir packageDir = null;

    /** SIPInfo instance */
    private SIPInfo m_sipInfo;

    /** PackageMETSManager instance */
    private HubAndSpokeMETSManager m_metsManager;

    /** ZipFileCreator instance */
    private ZipFileCreator m_zipFileCreator;

    /** Logger instance. */
    private static final Logger logger = Logger.newInstance();

    /**
    * Initialize the package builder.
    * <p>
    * @param identity	The identity of the user for whom the package is 
    *                  being created.
    * @param details   The details of the package being created.	
	 * @throws DAException 
    */
    public void init(CallerIdentity identity, PackageDetails details) throws DAException {
        m_objectInfo = new HAndSObjectInfo();
        m_identity = identity;
        m_pkgDetails = details;
        m_strInstSymbol = details.getInstSymbol();
        DataObject pInfo = m_pkgDetails.getPackageInfo();
        String harvestLoc = (String) pInfo.getAttr(PackageContainerConst.HARVEST_LOC);
        m_objectInfo.setHarvestLoc(harvestLoc);
        m_metadataMaker = new DCMetadataMaker();
        m_strFinalBatchPath = "";
    }

    /**
     * Start building the package.
	 * @throws DAException 
     */
    public void startBuild() throws DAException {
        m_strBatchNumber = getBatchNumber(m_identity, m_strInstSymbol);
        packageDir = new PackageDir(HUB_AND_SPOKE, m_strInstSymbol);
        String strBatchLoc = packageDir.getDirectory().getAbsolutePath();
        m_strBatchPath = strBatchLoc + File.separator + m_strBatchNumber + "." + BATCH_NOTREADY + "." + ZIP;
        System.out.println("In HAndSPackageBuilder m_strBatchPath:" + m_strBatchPath);
        m_sipInfo = new SIPInfo(m_strBatchPath);
        m_tmpMetsFileLoc = m_sipInfo.getTmpMetsFileLoc();
        createTmpFolder();
        m_metsManager = new HubAndSpokeMETSManager(m_sipInfo);
        m_zipFileCreator = new ZipFileCreator(m_strBatchPath, m_strBatchNumber);
        m_zipFileCreator.init();
    }

    /**
     * Add a new object to the package.
     * <p>
     * @param dcMetadata    The metadata for the new object. 
	 * @throws DAException 
     */
    public void newObject(DCMetadata dcMetadata) throws DAException {
        m_vfileInfo = new Vector<FileInfo>();
        this.m_dcMeta = dcMetadata;
        m_objCount++;
        m_objID = Integer.toString(m_objCount);
        String objFolderId = m_sipInfo.getHubAndSpokeObjectId(m_objID);
        m_objectInfo.setObjectId(objFolderId);
        ArrayList<DataObject> elements = m_metadataMaker.getMatchingElements(dcMetadata, DCElementSetNames.DCTITLE);
        DataObject element = elements.get(0);
        m_objectInfo.setTitle(element.getAttr(DCElementConst.VALUE).toString());
        m_vfileInfo.clear();
    }

    /**
     * Adds file to batch folder
     * The package helper will get the harvest location from package
     * info and get the array of file info. Then this method will be
     * called to add the file in the batch folder
     * @param fileInfo FileInfo
     * @param links link	Array of links. Empty or <code>null</code> means no links.
	 * @throws DAException 
     */
    public void addContent(FileInfo fileInfo, Link[] links) throws DAException {
        m_fileCount++;
        if (m_fileCount == 1) {
            String mimeType = fileInfo.getMIMEType();
            System.out.println("Entering file 1 with content type:" + mimeType);
            if (mimeType.equals(ContentType.CONTENT_TYPE_HTML)) {
                m_objectInfo.setObjectType(MetsXmlConst.WEBSITE);
            }
        }
        if (m_dcMeta == null) {
            DAException ex = new DAException(DAExceptionCodes.MISSING_STEP, new String[] { "newObject()", "addContent()" });
            logger.log(this, "addContent", "checking DC metadata", ex);
            throw ex;
        }
        m_vfileInfo.add(fileInfo);
        copyFile(fileInfo);
    }

    /**
     * Creates the object level mets file
     * <p>
	 * @throws DAException 
     */
    public void endObject() throws DAException {
        String dcMetadata = m_metadataMaker.objectsToXML(m_dcMeta);
        String fileLoc = m_metsManager.createObjectMets(m_objectInfo, m_vfileInfo, dcMetadata);
        if (fileLoc != null && (!(fileLoc.equals("")))) {
            System.out.println("Before adding the mets file to zip:" + fileLoc);
            m_zipFileCreator.addFile(new File(fileLoc), null);
        }
    }

    /**
     * Renames the batch to ".ready"
	 * @throws DAException 
     */
    public void endBuild() throws DAException {
        renameBatchDirectory();
        deleteDir(new File(m_tmpMetsFileLoc));
    }

    /**
     * Get the current results of the build. Typically this method is called 
     * after the <code>endBuild</code> call. 
     * <p>
     * @return	The current results of the packaging.
     */
    public PackageResults getResults() {
        PackageResults pkgResult = new PackageResults(m_strFinalBatchPath);
        return pkgResult;
    }

    /**
     * Delete an existing package.
     * <p>
	 * @throws DAException 
     */
    public void delete() throws DAException {
        deletePkg(true);
    }

    /**
	 * @see org.oclc.da.ndiipp.packager.PackageBuilder#close()
	 */
    public void close() throws DAException {
        if (m_zipFileCreator != null) {
            m_zipFileCreator.close();
        }
    }

    /**
     * Gets the next sequence number and creates a Batch number
     * <p>
     * @param identity Caller Identity  
     * @param a_strInstSymbol Inst Symbol  
     * @return  Batch Number
     * @throws DAException on error
     */
    private String getBatchNumber(CallerIdentity identity, String a_strInstSymbol) throws DAException {
        String strBatchNumber = "";
        long seqNum = 0;
        seqNum = (long) (Math.random() * 1000000000);
        strBatchNumber = HS + String.valueOf(seqNum) + a_strInstSymbol;
        return strBatchNumber;
    }

    /**
     * Create the directory specified. Creates all necessary subdirs.
     * <p>
     * @param dir	The directory to create.
	 * @return <code>File</code>
	 * @throws DAException 
     */
    private File createDir(File dir) throws DAException {
        if ((dir.exists()) && (dir.isDirectory())) {
            return dir;
        }
        if (!dir.mkdirs()) {
            DAException ex = new DAException(DAExceptionCodes.ERROR_CREATING, new String[] { dir.toString() });
            logger.log(this, "createDir", null, ex);
            throw ex;
        }
        return dir;
    }

    private boolean deleteDir(File dir) {
        if ((dir.exists()) && (dir.isDirectory())) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * Create the tmp folder.
     * <p>
     * @return	The file representing the tmp location for mets file.
	 * @throws DAException 
     */
    private File createTmpFolder() throws DAException {
        File tmpFolder = new File(m_tmpMetsFileLoc);
        System.out.println("In HAndSPackageBuilder m_sipInfo.getTmpMetsFileLoc():" + m_sipInfo.getTmpMetsFileLoc());
        return createDir(tmpFolder);
    }

    /**
     * Move the file to the object's content folder.
     * <p>
     * @param fileInfo  The file to move.
	 * @throws DAException 
     */
    private void copyFile(FileInfo fileInfo) throws DAException {
        File source = fileInfo.getFileLocation();
        m_zipFileCreator.addFile(source, m_objID);
    }

    /**
     * Move the file to the object's content folder.
     * <p>
	 * @throws DAException 
     */
    private void renameBatchDirectory() throws DAException {
        File notReadyBatch = new File(m_strBatchPath);
        String parentDir = notReadyBatch.getParent();
        m_strFinalBatchPath = parentDir + File.separator + m_strBatchNumber + "." + BATCH_READY + "." + ZIP;
        ;
        File ready = new File(m_strFinalBatchPath);
        if (!notReadyBatch.renameTo(ready)) {
            DAException ex = new DAException(DAExceptionCodes.IO_ERROR, new String[] { ready.toString() });
            logger.log(this, "renameBatchDirectory", null, ex);
            throw ex;
        }
    }

    /**
     * Delete an existing package.
     * <p>
     * @param delHarvest    Indicates whether or not to delete the associated
     *                      harvest also.
	 * @throws DAException 
     */
    public void deletePkg(boolean delHarvest) throws DAException {
        File batchFolder = findBatchFolder();
        if (batchFolder != null) {
            IOUtils.delDir(batchFolder);
        }
        if (delHarvest) {
            File harvestFolder = getFolder(false);
            if (harvestFolder != null) {
                IOUtils.delDir(harvestFolder);
            }
        }
    }

    /**
     * Find the actual batch folder for this package. Due to batch processing
     * the exact folder name stored in the package info. may no longer match
     * the folder name on disk. 
     * <p>
     * @return	The batch folder for this package or <code>null</code>
     *          if the package does not exist.
     *          
     */
    private File findBatchFolder() {
        File folder = getFolder(true);
        if (folder != null) {
            if (folder.isDirectory()) {
                return folder;
            } else {
                File parentFolder = folder.getParentFile();
                if (parentFolder != null) {
                    String noExt = removeExt(folder);
                    File[] files = parentFolder.listFiles();
                    for (int index = 0; index < files.length; index++) {
                        if (files[index].isDirectory() && (removeExt(files[index]).equals(noExt))) {
                            return files[index];
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get the location of the package or harvest. 
     * <p>
     * @param pkg <code>true</code> means get the package location, 
     *            <code>false</code> means get the harvest location.
     * @return	The specified folder for this package or <code>null</code>
     *          if the location cannot be determined.
     */
    private File getFolder(boolean pkg) {
        String locAttr = (pkg) ? PackageContainerConst.HS_PACKAGE_LOC : PackageContainerConst.HARVEST_LOC;
        PackageContainer pkgc = m_pkgDetails.getPackageInfo();
        String loc = (String) pkgc.getAttr(locAttr);
        return (loc != null) ? new File(loc) : null;
    }

    /**
     * Get the file name (or directory name) with extension removed.
     * <p>
     * @param file  A file to remove extension from.
     * @return	The file name with extension removed.
     */
    private String removeExt(File file) {
        String name = file.getName();
        int ext = name.lastIndexOf(".");
        if (ext != -1) {
            name = name.substring(0, ext);
        }
        return name;
    }

    /**Main (empty)
	 * @param args
	 */
    public static void main(String[] args) {
    }

    /**Empty
	 * test method
	 * @throws DAException 
	 */
    public void test() throws DAException {
    }
}
