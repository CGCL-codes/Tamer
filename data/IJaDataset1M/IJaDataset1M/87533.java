package uk.ac.ebi.pride.tools.converter.dao;

import uk.ac.ebi.pride.jaxb.model.Spectrum;
import uk.ac.ebi.pride.tools.converter.report.model.*;
import uk.ac.ebi.pride.tools.converter.utils.InvalidFormatException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

/**
 * This is the main interface for format-specific parsing. Each implementation is responsible to support as much of
 * desired functionality as possible. It is appreciated that not all formats will make the requested data items
 * available. In such cases, the methods should return null primitives and empty collections.
 * <p/>
 * If information is available and the methods are expected to return Param types, it is valid that the implementations
 * return UserParam objects for terms where the CvParam cannot be explicitely set at runtime. It will be the
 * responsibility of the user to inspect the report file generated and make certain that the information is correct
 * and, if possible, convert the UserParam data into the appropriate CvParams. In any case, the report formats will
 * undergo a validation step where missing or incorrect information will be flagged to the user before the full
 * parsing into PRIDE XML is executed.
 * </p>
 * The DAO must report all possible protein-to-peptide assignments. External tools will be available to update
 * the report file based on specific protein inference algorithms.
 * </p
 * >Note to all DAO implementers: DAO Implementing classes must extend the AbstractDAOImpl class and override all
 * methods defined in that class. All methods declared in AbstractDAOImpl will throw an UnsupportedOperationException,
 * unless documented otherwise.
 */
public interface DAO {

    public void setConfiguration(Properties props);

    public Properties getConfiguration();

    /**
     * MANDATORY - Must return some experiment title. In case no title
     * is provided by the search enginge's result file, a default title
     * should be returned.
     */
    public String getExperimentTitle() throws InvalidFormatException;

    /**
     * OPTIONAL - Should return null in case this option is not supported
     * by the search engine.
     */
    public String getExperimentShortLabel();

    /**
     * MANDATORY - As a minimal requirement the date of search and the original
     * MS data file format should be set.
     */
    public Param getExperimentParams() throws InvalidFormatException;

    /**
     * OPTIONAL - Should return null in case it's not supported by the search engine.
     */
    public String getSampleName();

    /**
     * OPTIONAL - Should return null in case it's not supported by the search engine.
     */
    public String getSampleComment();

    /**
     * MANDATORY - As a minimal requirement the sample's species should be returned.
     */
    public Param getSampleParams() throws InvalidFormatException;

    /**
     * MANDATORY - Represents the input file for the conversion.
     */
    public SourceFile getSourceFile() throws InvalidFormatException;

    /**
     * OPTIONAL - Should return null in case it's not supported by the search engine.
     */
    public Collection<Contact> getContacts();

    /**
     * OPTIONAL - Should return null in case it's not supported by the search engine.
     */
    public InstrumentDescription getInstrument();

    /**
     * MANDATORY - The search engine's name and version.
     */
    public Software getSoftware() throws InvalidFormatException;

    /**
     * OPTIONAL - Should return null in case it's not supported by the search engine.
     */
    public Param getProcessingMethod();

    /**
     * OPTIONAL - Should return null in case it's not supported by the search engine.
     */
    public Protocol getProtocol();

    /**
     * OPTIONAL - Should return null in case it's not supported by the search engine.
     */
    public Collection<Reference> getReferences();

    /**
     * MANDATORY - these will be written to the FASTA attributes and will be used
     * in the FASTA section if there are multiple sequence files, the search database
     * name will be a string-delimited concatenation of all the names. Idem for version.
     */
    public String getSearchDatabaseName() throws InvalidFormatException;

    /**
     * MANDATORY - see getSearchDatabaseName
     */
    public String getSearchDatabaseVersion() throws InvalidFormatException;

    /**
     * MANDATORY - Should return a collection of PTMs representing all PTMs that are used
     * in this search. Collection can be empty but not null.
     */
    public Collection<PTM> getPTMs() throws InvalidFormatException;

    /**
     * MANDATORY - Should return a collection of DatabaseMappings that contain all search database names and versions
     * used in this search. Collection can be empty but not null.
     */
    public Collection<DatabaseMapping> getDatabaseMappings() throws InvalidFormatException;

    /**
     * MANDATORY - Must return a valid SearchResultIdenfier object
     */
    public SearchResultIdentifier getSearchResultIdentifier() throws InvalidFormatException;

    /**
     * MANDATORY - Must return a non-null list of all CV lookups used by the DAO
     */
    public Collection<CV> getCvLookup() throws InvalidFormatException;

    /**
     * MANDATORY - Must returns a count of the number of spectra. if onlyIdentified is true, return only count
     * of identified spectra. If false, return count of all spectra.
     */
    public int getSpectrumCount(boolean onlyIdentified) throws InvalidFormatException;

    /**
     * returns an iterator for spectra in the source file - if onlyIdentified is true, return only identified
     * spectra. If false, return all spectra
     */
    public Iterator<Spectrum> getSpectrumIterator(boolean onlyIdentified) throws InvalidFormatException;

    /**
     * must return -1 if no spectrum ref found
     */
    public int getSpectrumReferenceForPeptideUID(String peptideUID) throws InvalidFormatException;

    /**
     * This function provides random access to the identifications.
     * It is only used in scan mode!
     *
     * @param identificationUID
     * @return
     */
    public Identification getIdentificationByUID(String identificationUID) throws InvalidFormatException;

    /**
     * This method will return an iterator that will return individual identification objects.
     * In prescanMode the complete Identification and Peptide objects should be returned
     * <b>without<b> the peptide's fragment ion annotation. Peptide items have to contain
     * all the PTMs. <br>
     * In conversionMode (= !prescanMode) Peptide and Protein objects should <b>NOT</b> contain
     * any additional parameters and peptidePTMs should <b>NOT</b> be included. Peptide
     * FragmentIon annotations are mandatory (if applicable) in scanMode.
     * The identification iterator may return null for an identification
     */
    public Iterator<Identification> getIdentificationIterator(boolean prescanMode) throws InvalidFormatException;

    /**
     * Sets the external spectrum file to the given filename. This function
     * is ignored by DAOs that do not require external spectrum files.
     * @param filename
     */
    public void setExternalSpectrumFile(String filename);
}
