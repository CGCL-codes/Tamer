package uk.ac.ebi.pride.data.controller.impl.ControllerImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.jmzidml.model.mzidml.*;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;
import uk.ac.ebi.pride.data.Tuple;
import uk.ac.ebi.pride.data.controller.DataAccessException;
import uk.ac.ebi.pride.data.controller.DataAccessMode;
import uk.ac.ebi.pride.data.controller.DataAccessUtilities;
import uk.ac.ebi.pride.data.controller.cache.CacheCategory;
import uk.ac.ebi.pride.data.controller.cache.impl.MzIdentMLCacheBuilder;
import uk.ac.ebi.pride.data.controller.impl.Transformer.MzIdentMLTransformer;
import uk.ac.ebi.pride.data.core.*;
import uk.ac.ebi.pride.data.core.CvParam;
import uk.ac.ebi.pride.data.core.Organization;
import uk.ac.ebi.pride.data.core.Peptide;
import uk.ac.ebi.pride.data.core.Person;
import uk.ac.ebi.pride.data.core.Provider;
import uk.ac.ebi.pride.data.core.Sample;
import uk.ac.ebi.pride.data.core.SourceFile;
import uk.ac.ebi.pride.data.core.SpectraData;
import uk.ac.ebi.pride.data.core.SpectrumIdentificationProtocol;
import uk.ac.ebi.pride.data.io.file.MzIdentMLUnmarshallerAdaptor;
import uk.ac.ebi.pride.data.utils.MD5Utils;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The MzIdentMLControllerImpl is the controller that retrieve the information from
 * the mzidentml files. It have support for Experiment Metadata (Global metadata),
 * also it have information about the IdentificationMetadata. The MzGraphMetaData is not
 * supported for this files because they not contains information about spectrums. The controller
 * support the mzidentml schema version 1.1.
 * <p/>
 * User: yperez
 * Date: 19/09/11
 * Time: 16:08
 */
public class MzIdentMLControllerImpl extends CachedDataAccessController {

    private static final Logger logger = LoggerFactory.getLogger(MzIdentMLControllerImpl.class);

    private MzIdentMLUnmarshallerAdaptor unmarshaller = null;

    private static Pattern mzIdentMLHeaderPattern = Pattern.compile("^(<\\?xml [^>]*>\\s*(<!--[^>]*-->\\s*)*){0,1}<(MzIdentML)|(indexedmzIdentML) xmlns=.*", Pattern.MULTILINE);

    /**
     * The constructor used by Default the CACHE_AND_SOURCE mode, it
     * means retrieve information from cache first,
     * if didn't find anything,then read from data source directly.
     *
     * @param file
     * @throws DataAccessException
     */
    public MzIdentMLControllerImpl(File file) throws DataAccessException {
        this(file, null);
    }

    /**
     * Default Constructor extends the CacheDataAccessController
     *
     * @param file mzidentml file
     * @param mode if the the mode is using Cache or retrieving from Source
     * @throws DataAccessException
     */
    public MzIdentMLControllerImpl(File file, DataAccessMode mode) throws DataAccessException {
        super(file, mode);
        initialize();
    }

    /**
     * This function initialize all the Categories in which the Controller
     * used the Cache System. In this case it wil be use cache for PROTEIN,
     * PEPTIDE, SAMPLE and SOFTWARE.
     *
     * @throws DataAccessException
     */
    protected void initialize() throws DataAccessException {
        File file = (File) getSource();
        MzIdentMLUnmarshaller um = new MzIdentMLUnmarshaller(file);
        unmarshaller = new MzIdentMLUnmarshallerAdaptor(um);
        this.setName(file.getName());
        this.setType(Type.XML_FILE);
        this.setContentCategories(ContentCategory.PROTEIN, ContentCategory.PEPTIDE, ContentCategory.SAMPLE, ContentCategory.SOFTWARE);
        setCacheBuilder(new MzIdentMLCacheBuilder(this));
        populateCache();
    }

    /**
     * Return the mzidentml unmarshall adaptor to be used by the CacheBuilder
     * Implementation.
     *
     * @return MzIdentMLUnmarshallerAdaptor
     */
    public MzIdentMLUnmarshallerAdaptor getUnmarshaller() {
        return unmarshaller;
    }

    /**
     * Get the unique id of the data access controller
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
                String msg = "Failed to generate unique id for mzIdentML file";
                logger.error(msg, e);
            }
        }
        return uid;
    }

    /**
     * Get a list of cv lookup objects.
     *
     * @return List<CVLookup>   a list of cvlookup objects.
     * @throws DataAccessException
     */
    @Override
    public List<CVLookup> getCvLookups() throws DataAccessException {
        return MzIdentMLTransformer.transformCVList(unmarshaller.getCvList());
    }

    /**
     * Get a list of source files.
     *
     * @return List<SourceFile> a list of source file objects.
     * @throws uk.ac.ebi.pride.data.controller.DataAccessException
     *
     */
    @Override
    public List<SourceFile> getSourceFiles() throws DataAccessException {
        List<SourceFile> sourceFiles;
        try {
            sourceFiles = MzIdentMLTransformer.transformToSourceFile(unmarshaller.getSourceFiles());
        } catch (Exception ex) {
            throw new DataAccessException("Failed to retrieve source files", ex);
        }
        return sourceFiles;
    }

    /**
     * Get a list of Organization Contacts
     *
     * @return List<Organization> A List of Organizations
     * @throws DataAccessException
     */
    @Override
    public List<Organization> getOrganizationContacts() throws DataAccessException {
        List<Organization> organizationList;
        try {
            organizationList = MzIdentMLTransformer.transformToOrganization(unmarshaller.getOrganizationContacts());
        } catch (Exception ex) {
            throw new DataAccessException("Failed to retrieve organization contacts", ex);
        }
        return organizationList;
    }

    /**
     * Get a list of Person Contacts
     *
     * @return List<Person> A list of Persons
     * @throws DataAccessException
     */
    @Override
    public List<Person> getPersonContacts() throws DataAccessException {
        List<Person> personList;
        try {
            personList = MzIdentMLTransformer.transformToPerson(unmarshaller.getPersonContacts());
        } catch (Exception ex) {
            throw new DataAccessException("Failed to retrieve person contacts", ex);
        }
        return personList;
    }

    /**
     * Get a list of samples
     *
     * @return List<Sample> a list of sample objects.
     * @throws DataAccessException
     */
    @Override
    public List<Sample> getSamples() throws DataAccessException {
        ExperimentMetaData metaData = super.getExperimentMetaData();
        if (metaData == null) {
            try {
                return MzIdentMLTransformer.transformToSample(unmarshaller.getSampleList());
            } catch (Exception ex) {
                throw new DataAccessException("Failed to retrieve samples", ex);
            }
        } else {
            return metaData.getSampleList();
        }
    }

    /**
     * Get provider of the experiment
     *
     * @return Provider    data provider
     * @throws DataAccessException data access exception
     */
    @Override
    public Provider getProvider() throws DataAccessException {
        ExperimentMetaData metaData = super.getExperimentMetaData();
        if (metaData == null) {
            return MzIdentMLTransformer.transformToProvider(unmarshaller.getProvider());
        }
        return metaData.getProvider();
    }

    /**
     * Get a list of softwares
     *
     * @return List<Software>   a list of software objects.
     * @throws DataAccessException data access exception
     */
    @Override
    public List<Software> getSoftwares() throws DataAccessException {
        ExperimentMetaData metaData = super.getExperimentMetaData();
        if (metaData == null) {
            try {
                return MzIdentMLTransformer.transformToSoftware(unmarshaller.getSoftwares());
            } catch (Exception ex) {
                throw new DataAccessException("Failed to retrieve software", ex);
            }
        } else {
            return metaData.getSoftwares();
        }
    }

    /**
     * Get a list of references
     *
     * @return List<Reference>  a list of reference objects
     * @throws DataAccessException data access exception
     */
    public List<Reference> getReferences() throws DataAccessException {
        List<Reference> refs;
        try {
            refs = MzIdentMLTransformer.transformToReference(unmarshaller.getReferences());
        } catch (Exception ex) {
            throw new DataAccessException("Failed to retrieve references", ex);
        }
        return refs;
    }

    /**
     * Protocol is not supported by mzidentml files. In this case ExperimentProtocol is
     * related with Sample Experimental Protocol, this concept comes from PRIDE XML Files
     * and it is not included in any field on mzidentml.
     *
     * @return ExperimentProtocol protocol object.
     * @throws DataAccessException data access exception
     */
    public ExperimentProtocol getProtocol() throws DataAccessException {
        throw new UnsupportedOperationException("This method is not supported");
    }

    /**
     * Additional is a concept that comes from PRIDE XML Files. In the mzidentml
     * all the concepts of the Additional comes inside different objects.
     * This function construct an Additional Object a relation of
     * creationDate, Original Spectra Data Files and finally the Original software
     * that provide the mzidentml file.
     *
     * @return ParamGroup   a group of cv parameters and user parameters.
     * @throws DataAccessException
     *
     */
    @Override
    public ParamGroup getAdditional() throws DataAccessException {
        ParamGroup additionals = null;
        Provider provider = getProvider();
        Date date = unmarshaller.getCreationDate();
        List<SpectraData> spectraDataList = getSpectraDataFiles();
        if ((provider != null && provider.getSoftware() != null) || date != null || spectraDataList != null) {
            additionals = new ParamGroup();
            if (provider != null && provider.getSoftware() != null) additionals.addCvParams(provider.getSoftware().getCvParams());
            if (unmarshaller.getCreationDate() != null) {
                additionals.addCvParam(MzIdentMLTransformer.transformDateToCvParam(unmarshaller.getCreationDate()));
            }
            if (spectraDataList != null) {
                Set<CvParam> cvParamList = new HashSet<CvParam>();
                for (SpectraData spectraData : spectraDataList) {
                    cvParamList.add(spectraData.getSpectrumIdFormat());
                    cvParamList.add(spectraData.getFileFormat());
                }
                List<CvParam> list = new ArrayList<CvParam>(cvParamList);
                additionals.addCvParams(list);
            }
        }
        return additionals;
    }

    /**
     * The mzidentml do not support Quatitation Data
     * @return
     * @throws DataAccessException
     */
    @Override
    public boolean hasQuantData() throws DataAccessException {
        return false;
    }

    /**
     * Get meta data related to this experiment
     *
     * @return MetaData meta data object
     * @throws DataAccessException data access exception
     */
    @Override
    public ExperimentMetaData getExperimentMetaData() throws DataAccessException {
        ExperimentMetaData metaData = super.getExperimentMetaData();
        if (metaData == null) {
            try {
                String accession = unmarshaller.getMzIdentMLId();
                String version = unmarshaller.getMzIdentMLVersion();
                List<SourceFile> sources = getSourceFiles();
                List<Sample> samples = getSamples();
                List<Software> softwares = getSoftwares();
                List<Person> persons = getPersonContacts();
                List<Organization> organizations = getOrganizationContacts();
                ParamGroup additional = getAdditional();
                String title = unmarshaller.getMzIdentMLName();
                String shortLabel = null;
                ExperimentProtocol protocol = null;
                List<Reference> references = getReferences();
                Provider provider = getProvider();
                Date creationDate = unmarshaller.getCreationDate();
                metaData = new ExperimentMetaData(additional, accession, title, version, shortLabel, samples, softwares, persons, sources, provider, organizations, references, creationDate, null, protocol);
                getCache().store(CacheCategory.EXPERIMENT_METADATA, metaData);
            } catch (Exception ex) {
                throw new DataAccessException("Failed to retrieve meta data", ex);
            }
        }
        return metaData;
    }

    /**
     * The spectrum IdentificationProtocol is the Set of parameters Related with
     * the Spectrum Identification Process in terms of Search Engines, Databases,
     * Enzymes, Modifications and Database Filters, etc
     * @return List<SpectrumIdentificationProtocol> A List of Spectrum Identification Protocols
     * @throws DataAccessException
     */
    @Override
    public List<SpectrumIdentificationProtocol> getSpectrumIdentificationProtocol() throws DataAccessException {
        IdentificationMetaData identificationMetaData = super.getIdentificationMetaData();
        if (identificationMetaData == null) {
            return MzIdentMLTransformer.transformToSpectrumIdentificationProtocol(unmarshaller.getSpectrumIdentificationProtcol());
        }
        return identificationMetaData.getSpectrumIdentificationProtocolList();
    }

    /**
     * The Protein Protocol is a relation of different Software and Processing Steps with
     * the Identified Proteins.
     * @return Protocol Protein Protocol
     * @throws DataAccessException
     */
    @Override
    public Protocol getProteinDetectionProtocol() throws DataAccessException {
        IdentificationMetaData identificationMetaData = super.getIdentificationMetaData();
        if (identificationMetaData == null) {
            return MzIdentMLTransformer.transformToProteinDetectionProtocol(unmarshaller.getProteinDetectionProtocol());
        }
        return identificationMetaData.getProteinDetectionProtocol();
    }

    /**
     * Get the List of Databases used in the Experiment
     *
     * @return List<SearchDataBase> List of SearchDatabases
     * @throws DataAccessException
     */
    @Override
    public List<SearchDataBase> getSearchDataBases() throws DataAccessException {
        IdentificationMetaData identificationMetaData = super.getIdentificationMetaData();
        if (identificationMetaData == null) {
            return MzIdentMLTransformer.transformToSearchDataBase(unmarshaller.getSearchDatabases());
        }
        return identificationMetaData.getSearchDataBaseList();
    }

    /**
     * The IdentificationMetadata is a Combination of SpectrumIdentificationProtocol,
     * a Protein Protocol and finally the Databases used in the Experiment.
     *
     * @return IdentificationMetadata the metadata related with the identification process
     * @throws DataAccessException
     */
    @Override
    public IdentificationMetaData getIdentificationMetaData() throws DataAccessException {
        IdentificationMetaData metaData = super.getIdentificationMetaData();
        if (metaData == null) {
            List<SpectrumIdentificationProtocol> spectrumIdentificationProtocolList = getSpectrumIdentificationProtocol();
            Protocol proteinDetectionProtocol = getProteinDetectionProtocol();
            List<SearchDataBase> searchDataBaseList = getSearchDataBases();
            metaData = new IdentificationMetaData(null, null, spectrumIdentificationProtocolList, proteinDetectionProtocol, searchDataBaseList);
        }
        return metaData;
    }

    /**
     * Get the List of File Spectras that the Mzidentml use to identified peptides
     *
     * @return
     * @throws DataAccessException
     */
    @Override
    public List<SpectraData> getSpectraDataFiles() throws DataAccessException {
        ExperimentMetaData metaData = super.getExperimentMetaData();
        if (metaData == null) {
            return MzIdentMLTransformer.transformToSpectraData(unmarshaller.getSpectraData());
        }
        return metaData.getSpectraDataList();
    }

    /**
     * MzidemtML files do not support Spectra MetaData. It is supported by MzML and
     * PRIDE Objects, also by other file Formats.
     * @return
     * @throws DataAccessException
     */
    @Override
    public MzGraphMetaData getMzGraphMetaData() throws DataAccessException {
        throw new UnsupportedOperationException("This method is not supported");
    }

    /**
     * The MzGraphMetadata is not supported by mzidentml.
     *
     * @return
     * @throws DataAccessException
     */
    @Override
    public boolean hasSpectrum() throws DataAccessException {
        return false;
    }

    /**
     * Get identification using a identification id, gives the option to choose whether to use cache.
     * This implementation provides a way of by passing the cache.
     *
     * @param id       identification id
     * @param useCache true means to use cache
     * @return Identification identification object
     * @throws DataAccessException data access exception
     */
    @Override
    public Identification getIdentificationById(Comparable id, boolean useCache) throws DataAccessException {
        Identification ident = super.getIdentificationById(id, useCache);
        if (ident == null) {
            logger.debug("Get new identification from file: {}", id);
            try {
                ident = MzIdentMLTransformer.transformToIdentification(unmarshaller.getIdentificationById(id), unmarshaller.getFragmentationTable());
                List<Peptide> peptides = MzIdentMLTransformer.transformToPeptideIdentifications(unmarshaller.getPeptideHypothesisbyID(id), unmarshaller.getFragmentationTable());
                ident.setPeptides(peptides);
                if (useCache && ident != null) {
                    getCache().store(CacheCategory.IDENTIFICATION, id, ident);
                    for (Peptide peptide : ident.getPeptides()) {
                        getCache().store(CacheCategory.PEPTIDE, new Tuple<Comparable, Comparable>(id, peptide.getSpectrumIdentification().getId()), peptide);
                        Spectrum spectrum = peptide.getSpectrum();
                        if (spectrum != null) {
                            getCache().store(CacheCategory.PRECURSOR_CHARGE, spectrum.getId(), DataAccessUtilities.getPrecursorCharge(spectrum));
                            getCache().store(CacheCategory.PRECURSOR_MZ, spectrum.getId(), DataAccessUtilities.getPrecursorMz(spectrum));
                        }
                    }
                }
            } catch (Exception ex) {
                throw new DataAccessException("Failed to retrieve identification: " + id, ex);
            }
        }
        return ident;
    }

    /**
     * Get peptide using a given identification id and a given peptide index
     *
     * @param index    peptide index
     * @param useCache whether to use cache
     * @return Peptide  peptide
     * @throws DataAccessException exception while getting peptide
     */
    @Override
    public Peptide getPeptideByIndex(Comparable identId, Comparable index, boolean useCache) throws DataAccessException {
        Peptide peptide = super.getPeptideByIndex(identId, index, useCache);
        if (peptide == null) {
            logger.debug("Get new peptide from file: {}", index);
            Identification ident = null;
            try {
                ident = MzIdentMLTransformer.transformToIdentification(unmarshaller.getIdentificationById(identId), unmarshaller.getFragmentationTable());
                List<Peptide> peptides = MzIdentMLTransformer.transformToPeptideIdentifications(unmarshaller.getPeptideHypothesisbyID(identId), unmarshaller.getFragmentationTable());
                ident.setPeptides(peptides);
                peptide = ident.getPeptides().get(Integer.parseInt(index.toString()));
                if (useCache && peptide != null) {
                    getCache().store(CacheCategory.PEPTIDE, new Tuple<Comparable, Comparable>(identId, index), peptide);
                    Spectrum spectrum = peptide.getSpectrum();
                    if (spectrum != null) {
                        getCache().store(CacheCategory.PRECURSOR_CHARGE, spectrum.getId(), DataAccessUtilities.getPrecursorCharge(spectrum));
                        getCache().store(CacheCategory.PRECURSOR_MZ, spectrum.getId(), DataAccessUtilities.getPrecursorMz(spectrum));
                    }
                }
            } catch (JAXBException e) {
                throw new DataAccessException("Failed to retrieve peptide identification: " + identId + " " + index, e);
            }
        }
        return peptide;
    }

    /**
     * Get the number of peptides.
     *
     * @return int  the number of peptides.
     * @throws DataAccessException data access exception.
     */
    @Override
    public int getNumberOfPeptides() throws DataAccessException {
        int num;
        try {
            num = unmarshaller.getNumIdentifiedPeptides();
        } catch (Exception ex) {
            throw new DataAccessException("Failed to retrieve number of peptides", ex);
        }
        return num;
    }

    @Override
    public void close() {
        unmarshaller = null;
        super.close();
    }

    /**
     * Check a file is MZIdentML XML file
     *
     * @param file input file
     * @return boolean true means MZIdentML XML
     */
    public static boolean isValidFormat(File file) {
        boolean valid = false;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                content.append(reader.readLine());
            }
            Matcher matcher = mzIdentMLHeaderPattern.matcher(content);
            valid = matcher.find();
        } catch (Exception e) {
            logger.error("Failed to read file", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return valid;
    }
}
