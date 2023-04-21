package gov.lanl.archive;

import gov.lanl.arc.heritrixImpl.ARCFileWriter;
import gov.lanl.archive.wrappers.ARCWrapper;
import gov.lanl.archive.wrappers.TapeWrapper;
import gov.lanl.xmltape.SingleTapeReader;
import gov.lanl.xmltape.SingleTapeWriter;
import java.io.File;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Wrapper class for Adore Archive IO Operations
 * 
 * @author Ryan Chute <rchute@lanl.gov>
 */
public class ArchiveIO {

    private ArchiveConfig config;

    static Logger log = Logger.getLogger(ArchiveIO.class.getName());

    ARCWrapper aw;

    TapeWrapper tw;

    /**
     * Constructor
     * @param _config
     *        Initialized ArchiveConfig Object
     */
    public ArchiveIO(ArchiveConfig _config) {
        this.config = _config;
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        Properties props = config.getProperties();
        aw = new ARCWrapper(props);
        tw = new TapeWrapper(props);
    }

    /**
     * Gets a SingleTapeWriter for the specified tapename; 
     * Provided a name, will prepend XMLtape Storage Path & Extension
     * @param tapename
     *        Name of tape to be created, minus extension
     * @return
     *        SingleTapeWriter, used to write XMLtape records
     * @throws ArchiveException
     */
    public SingleTapeWriter openSingleTapeWriter(String tapename) throws ArchiveException {
        return openSingleTapeWriter(tapename, null);
    }

    /**
     * Gets a SingleTapeWriter for the specified tapename and storageDir; 
     * provided a name, will prepend XMLtape Storage Path & Extension
     * @param tapename
     *        Name of tape to be created, minus extension
     * @param storageDir
     *        Path to XMLtape Storage Directory
     * @return
     *        SingleTapeWriter, used to write XMLtape records
     * @throws ArchiveException
     */
    public SingleTapeWriter openSingleTapeWriter(String tapename, String storageDir) throws ArchiveException {
        try {
            if (storageDir == null) return tw.openTapeWriter(tapename); else return tw.openTapeWriter(tapename, storageDir);
        } catch (Exception ex) {
            throw new ArchiveException("error in openSingleTapeWriter " + tapename, ex);
        }
    }

    /**
     * Gets a SingleTapeReader for specified tapename, uses default storage/index dirs.
     * @param tapename
     *        Name of tape to be read, minus extension
     * @return
     *        SingleTapeReader, used to read specified tape
     * @throws ArchiveException
     */
    public SingleTapeReader openSingleTapeReader(String tapename) throws ArchiveException {
        try {
            SingleTapeReader reader = tw.openTapeReader(tapename);
            return reader;
        } catch (Exception ex) {
            throw new ArchiveException("error in openSingleTapeReader " + tapename, ex);
        }
    }

    /**
     * Gets a SingleTapeReader for specified tapename, uses defined storage/index dirs.
     * @param tapename
     *        Name of tape to be read, minus extension
     * @param storageDir
     *        Path to XMLtape Storage Directory
     * @param indexDir
     *        Path to XMLtape Index Directory
     * @return
     *        SingleTapeReader, used to read specified tape
     * @throws ArchiveException
     */
    public SingleTapeReader openSingleTapeReader(String tapename, String storageDir, String indexDir) throws ArchiveException {
        try {
            if (storageDir == null || indexDir == null) return tw.openTapeReader(tapename); else return tw.openTapeReader(tapename, storageDir, indexDir);
        } catch (Exception ex) {
            throw new ArchiveException("error in openSingleTapeReader " + tapename, ex);
        }
    }

    /**
     * Gets an ARCFileWriter for specified arcname, uses default storage dir. 
     * Provided a name, will prepend ARCfile Storage Path & Extension
     * @param arcname
     *        Name of arcfile to be created, minus extension
     * @return
     *        ARCFileWriter, used to write datastreams to specifed arcfile
     * @throws ArchiveException
     */
    public ARCFileWriter openARCFileWriter(String arcname) throws ArchiveException {
        String arcfileDir = config.getARCStoredDirectory() + File.separator;
        return openARCFileWriter(arcname, arcfileDir);
    }

    /**
     * Gets an ARCFileWriter for specified arcname; provided a name,
     * will prepend ARCfile Storage Path & Extension
     * @param arcname
     *        Name of ARCfile to be created, minus extension
     * @param storageDir
     *        Path to ARCfile Storage Directory
     * @return
     *        ARCFileWriter, used to write datastreams to specifed arcfile
     * @throws ArchiveException
     */
    public ARCFileWriter openARCFileWriter(String arcname, String storageDir) throws ArchiveException {
        try {
            File dir = new File(storageDir);
            if (!dir.exists()) dir.mkdirs();
            boolean compression = Boolean.valueOf(config.getARCCompression()).booleanValue();
            String arcfileDir = (storageDir.endsWith(File.separator)) ? storageDir : storageDir + File.separator;
            String arcfileName = arcname + ".arc";
            ARCFileWriter arcwriter = new ARCFileWriter(arcfileDir, arcfileName, compression);
            return arcwriter;
        } catch (Exception ex) {
            throw new ArchiveException("error in openArcFileWriter " + arcname, ex);
        }
    }
}
