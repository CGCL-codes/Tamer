package uk.ac.ebi.pride.data.controller.impl.ControllerImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.data.controller.DataAccessException;
import uk.ac.ebi.pride.data.controller.DataAccessMode;
import uk.ac.ebi.pride.data.controller.cache.CacheCategory;
import uk.ac.ebi.pride.data.controller.cache.impl.PeakCacheBuilder;
import uk.ac.ebi.pride.data.controller.impl.Transformer.PeakTransformer;
import uk.ac.ebi.pride.data.core.*;
import uk.ac.ebi.pride.data.io.file.PeakUnmarshallerAdaptor;
import uk.ac.ebi.pride.data.utils.Constants;
import uk.ac.ebi.pride.data.utils.MD5Utils;
import uk.ac.ebi.pride.tools.dta_parser.DtaFile;
import uk.ac.ebi.pride.tools.jmzreader.JMzReader;
import uk.ac.ebi.pride.tools.jmzreader.JMzReaderException;
import uk.ac.ebi.pride.tools.mgf_parser.MgfFile;
import uk.ac.ebi.pride.tools.ms2_parser.Ms2File;
import uk.ac.ebi.pride.tools.pkl_parser.PklFile;
import java.io.File;
import java.lang.reflect.Constructor;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Peak Controller keep the information from different Peak files. It support mgf, ms2, pkl,
 * and DTa files. This files only contain the name of the file, the
 *
 * User: yperez
 * Date: 3/15/12
 * Time: 10:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class PeakControllerImpl extends CachedDataAccessController {

    private static final Logger logger = LoggerFactory.getLogger(PeakControllerImpl.class);

    /**
     * Reader for getting information from jmzReader file
     */
    private PeakUnmarshallerAdaptor unmarshaller;

    /**
     * Construct a data access controller using a given mzML file
     *
     * @param file jmzReader file
     * @throws uk.ac.ebi.pride.data.controller.DataAccessException data access exception
     */
    public PeakControllerImpl(File file) throws DataAccessException {
        this(file, null);
    }

    /**
     * Construct a data access controller using a given mzML file and data access mode
     *
     * @param file jmzReader file
     * @param mode data access mode
     * @throws DataAccessException data access exception
     */
    public PeakControllerImpl(File file, DataAccessMode mode) throws DataAccessException {
        super(file, mode);
        initialize();
    }

    /**
     * Initialize the data access controller
     *
     * @throws DataAccessException data access exception
     */
    private void initialize() throws DataAccessException {
        File file = (File) this.getSource();
        JMzReader um = null;
        if (isValidFormat(file) != null) {
            try {
                if (isValidFormat(file) == MgfFile.class) {
                    um = new MgfFile(file);
                }
                if (isValidFormat(file) == DtaFile.class) {
                    um = new DtaFile(file);
                }
                if (isValidFormat(file) == PklFile.class) {
                    um = new PklFile(file);
                }
                if (isValidFormat(file) == Ms2File.class) {
                    um = new Ms2File(file);
                }
            } catch (JMzReaderException e) {
                String msg = "Error while trying to initialize the Peak file";
                logger.error(msg, e);
                throw new DataAccessException(msg, e);
            }
        }
        unmarshaller = new PeakUnmarshallerAdaptor(um);
        this.setName(file.getName());
        this.setType(DataAccessController.Type.XML_FILE);
        this.setContentCategories(DataAccessController.ContentCategory.SPECTRUM, DataAccessController.ContentCategory.CHROMATOGRAM, DataAccessController.ContentCategory.SAMPLE, DataAccessController.ContentCategory.INSTRUMENT, DataAccessController.ContentCategory.SOFTWARE, DataAccessController.ContentCategory.DATA_PROCESSING);
        setCacheBuilder(new PeakCacheBuilder(this));
        populateCache();
    }

    /**
     * Get the backend data reader
     *
     * @return MzMLUnmarshallerAdaptor mzML reader
     */
    public PeakUnmarshallerAdaptor getUnmarshaller() {
        return unmarshaller;
    }

    /**
     * Get the unique id for this data access controller
     * It generates a MD5 hash using the absolute path of the file
     * This will guarantee the same id if the file path is the same
     *
     * @return String  unique id
     */
    @Override
    public String getUid() {
        String uid = super.getUid();
        if (uid == null) {
            File file = (File) this.getSource();
            try {
                uid = MD5Utils.generateHash(file.getAbsolutePath());
            } catch (NoSuchAlgorithmException e) {
                String msg = "Failed to generate unique id for mzML file";
                logger.error(msg, e);
            }
        }
        return uid;
    }

    /**
     * Get a list of person contacts
     *
     * @return List<Person>    list of persons
     * @throws DataAccessException data access exception
     */
    @Override
    public List<Person> getPersonContacts() throws DataAccessException {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public List<Organization> getOrganizationContacts() throws DataAccessException {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public List<SourceFile> getSourceFiles() throws DataAccessException {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public ParamGroup getFileContent() throws DataAccessException {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public List<Software> getSoftwares() throws DataAccessException {
        throw new UnsupportedOperationException("This method is not supported");
    }

    /**
     * Get a list of scan settings by checking the cache first
     *
     * @return List<ScanSetting>   a list of scan settings
     * @throws DataAccessException data access exception
     */
    @Override
    public List<ScanSetting> getScanSettings() throws DataAccessException {
        throw new UnsupportedOperationException("This method is not supported");
    }

    /**
     * Get a list of instrument configurations by checking the cache first
     *
     * @return List<Instrumentconfiguration>   a list of instrument configurations
     * @throws DataAccessException data access exception
     */
    @Override
    public List<InstrumentConfiguration> getInstrumentConfigurations() throws DataAccessException {
        throw new UnsupportedOperationException("This method is not supported");
    }

    /**
     * Get a list of data processings by checking the cache first
     *
     * @return List<DataProcessing>    a list of data processings
     * @throws DataAccessException data access exception
     */
    @Override
    public List<DataProcessing> getDataProcessings() throws DataAccessException {
        throw new UnsupportedOperationException("This method is not supported");
    }

    /**
     * Get additional details, mzML don't have this kind of information
     *
     * @return ParamGroup  param group
     * @throws DataAccessException data access exception
     */
    @Override
    public ParamGroup getAdditional() throws DataAccessException {
        throw new UnsupportedOperationException("This method is not supported");
    }

    /**
     * Get spectrum using a spectrum id, gives the option to choose whether to use cache.
     * This implementation provides a way of by passing the cache.
     *
     * @param id       spectrum id
     * @param useCache true means to use cache
     * @return Spectrum spectrum object
     * @throws DataAccessException data access exception
     */
    @Override
    Spectrum getSpectrumById(Comparable id, boolean useCache) throws DataAccessException {
        Spectrum spectrum = super.getSpectrumById(id, useCache);
        if (spectrum == null) {
            uk.ac.ebi.pride.tools.jmzreader.model.Spectrum rawSpec = null;
            try {
                rawSpec = unmarshaller.getSpectrumById(id.toString());
                spectrum = PeakTransformer.transformSpectrum(rawSpec);
                if (useCache) {
                    getCache().store(CacheCategory.SPECTRUM, id, spectrum);
                }
            } catch (JMzReaderException ex) {
                logger.error("Get spectrum by id", ex);
                throw new DataAccessException("Exception while trying to read Spectrum using Spectrum ID", ex);
            }
        }
        return spectrum;
    }

    /**
     * Get chromatogram using a chromatogram id, gives the option to choose whether to use cache.
     * This implementation provides a way of by passing the cache.
     *
     * @param id       chromatogram id
     * @param useCache true means to use cache
     * @return Chromatogram chromatogram object
     * @throws DataAccessException data access exception
     */
    @Override
    public Chromatogram getChromatogramById(Comparable id, boolean useCache) throws DataAccessException {
        throw new UnsupportedOperationException("This method is not supported");
    }

    /**
     * Close data access controller by resetting the data reader first
     */
    @Override
    public void close() {
        unmarshaller = null;
        super.close();
    }

    @Override
    public ExperimentMetaData getExperimentMetaData() throws DataAccessException {
        ExperimentMetaData metaData = super.getExperimentMetaData();
        if (metaData == null) {
            String id = null;
            String accession = null;
            String version = null;
            List<SourceFile> sourceFileList = null;
            List<Person> personList = null;
            List<Organization> organizationList = null;
            List<Sample> samples = null;
            List<Software> softwares = null;
            ParamGroup fileContent = null;
            metaData = new ExperimentMetaData(fileContent, id, accession, version, null, samples, softwares, personList, sourceFileList, null, organizationList, null, null, null, null);
        }
        return metaData;
    }

    @Override
    public MzGraphMetaData getMzGraphMetaData() {
        return null;
    }

    @Override
    public IdentificationMetaData getIdentificationMetaData() {
        return null;
    }

    /**
     * Check a file is mzML file
     *
     * @param file input file
     * @return boolean true means mzML
     */
    public static Class isValidFormat(File file) {
        boolean valid = false;
        String filename = file.getName().toLowerCase();
        if (filename.endsWith(Constants.DTA_EXT)) {
            return DtaFile.class;
        } else if (filename.endsWith(Constants.MGF_EXT)) {
            return MgfFile.class;
        } else if (filename.endsWith(Constants.MS2_EXT)) {
            return Ms2File.class;
        } else if (filename.endsWith(Constants.PKL_EXT)) {
            return PklFile.class;
        }
        return null;
    }

    /**
     * The only file format that not contain any MetaData are the pure peak files.
     *
     * @return
     * @throws DataAccessException
     */
    @Override
    public boolean hasMetaDataInformation() {
        return false;
    }
}
