package uk.ac.ebi.pride.data.controller.impl.Transformer;

import uk.ac.ebi.pride.data.controller.DataAccessUtilities;
import uk.ac.ebi.pride.data.core.*;
import uk.ac.ebi.pride.data.utils.BinaryDataUtils;
import uk.ac.ebi.pride.engine.SearchEngineType;
import uk.ac.ebi.pride.term.CvTermReference;
import uk.ac.ebi.pride.util.NumberUtilities;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PrideXmlTransformer contains a list of static methods which convert pride-jaxb objects to pride inspector core objects
 * <p/>
 * Date: 17-Mar-2010
 * Time: 14:14:12
 */
public class PrideXmlTransformer {

    /**
     * These are a list of default names to be used for the conversion
     */
    private static final String SAMPLE_ID = "sample1";

    private static final String COMMENTS = "comments";

    private static final String COMPLETION_TIME = "completion time";

    private static final String CONTACT_INFO = "contact information";

    private static final String DATA_PROCESSING_ID = "dataprocessing1";

    private static final String SOURCE_FILE_ID = "sourcefile1";

    private static final String SOURCE_FILE_TYPE = "source file type";

    private static final String PROTOCOL_ID = "protocol1";

    private static final int PROCESSING_METHOD_ORDER = 1;

    /**
     * Convert spectrum
     * <p/>
     * Note: supDes, supDataArrayBinary are ignored.
     *
     * @param rawSpec pride xml spectrum
     * @return Spectrum    spectrum
     */
    public static Spectrum transformSpectrum(uk.ac.ebi.pride.jaxb.model.Spectrum rawSpec) {
        Spectrum spectrum = null;
        if (rawSpec != null) {
            uk.ac.ebi.pride.jaxb.model.SpectrumDesc rawSpecDesc = rawSpec.getSpectrumDesc();
            ParamGroup params = null;
            ScanList scanList = null;
            List<Precursor> precursors = null;
            List<BinaryDataArray> dataArr = null;
            int defaultArrLength = -1;
            if (rawSpecDesc != null) {
                params = transformSpectrumParamGroup(rawSpecDesc);
                scanList = transformScanList(rawSpecDesc.getSpectrumSettings());
                precursors = transformPrecursorList(rawSpec.getSpectrumDesc().getPrecursorList());
                BinaryDataArray mz = transformBinaryDataArray(rawSpec.getMzArrayBinary(), CvTermReference.MZ_ARRAY);
                BinaryDataArray inten = transformBinaryDataArray(rawSpec.getIntenArrayBinary(), CvTermReference.INTENSITY_ARRAY);
                dataArr = new ArrayList<BinaryDataArray>();
                dataArr.add(mz);
                dataArr.add(inten);
                defaultArrLength = mz.getDoubleArray().length;
            }
            Integer specId = rawSpec.getId();
            spectrum = new Spectrum(params, specId, null, -1, null, defaultArrLength, dataArr, null, null, scanList, precursors, null, null);
        }
        return spectrum;
    }

    /**
     * transform spectrum's param grouop from pride xml to core data model.
     *
     * @param rawSpecDesc original spectrum description from pride xml.
     * @return ParamGroup   param group in core data model format.
     */
    private static ParamGroup transformSpectrumParamGroup(uk.ac.ebi.pride.jaxb.model.SpectrumDesc rawSpecDesc) {
        ParamGroup params = new ParamGroup();
        uk.ac.ebi.pride.jaxb.model.SpectrumSettings rawSpecSettings = rawSpecDesc.getSpectrumSettings();
        uk.ac.ebi.pride.jaxb.model.AcqSpecification rawActSpec = rawSpecSettings.getAcqSpecification();
        uk.ac.ebi.pride.jaxb.model.SpectrumInstrument rawSpecInstrument = rawSpecSettings.getSpectrumInstrument();
        CvTermReference msLevelCv = CvTermReference.MS_LEVEL;
        params.addCvParam(new CvParam(msLevelCv.getAccession(), msLevelCv.getName(), msLevelCv.getCvLabel(), rawSpecInstrument.getMsLevel() + "", null, null, null));
        CvTermReference massSpecCv = CvTermReference.MASS_SPECTRUM;
        params.addCvParam(new CvParam(massSpecCv.getAccession(), massSpecCv.getName(), massSpecCv.getCvLabel(), null, null, null, null));
        if (rawActSpec != null) {
            params.addCvParam(getSpectrumType(rawActSpec.getSpectrumType()));
        }
        params.addCvParams(transformCvParams(rawSpecInstrument.getCvParam()));
        params.addUserParams(transformUserParams(rawSpecInstrument.getUserParam()));
        List<String> comments = rawSpecDesc.getComments();
        for (String comment : comments) {
            params.addUserParam(new UserParam(COMMENTS, null, comment, null, null, null));
        }
        return params;
    }

    /**
     * create a cv param for spectrum type
     *
     * @param value original value of the spectrum type, map discrete to centroid spectrum, continuous to profile spectrum.
     * @return CvParam cv param in core data model format.
     */
    private static CvParam getSpectrumType(String value) {
        CvParam cvParam = null;
        CvTermReference cvTerm = null;
        if ("discrete".equals(value)) {
            cvTerm = CvTermReference.CENTROID_SPECTRUM;
        } else if ("continuous".equals(value)) {
            cvTerm = CvTermReference.PROFILE_SPECTRUM;
        }
        if (cvTerm != null) {
            cvParam = new CvParam(cvTerm.getAccession(), cvTerm.getName(), cvTerm.getCvLabel(), value, null, null, null);
        }
        return cvParam;
    }

    /**
     * Transform spectrumSetting in pride xml to ScanList
     * <p/>
     * 1. for each acquisition a Scan object will created
     * 2. Method of combination is mapped to ParamGroup of scan list
     * 3. mz range start/stop in SpectrumInstrument are mapped to a scan window.
     *
     * @param rawSpecSettings spectrum setting in pride xml.
     * @return ScanList scan list in core data model.
     */
    private static ScanList transformScanList(uk.ac.ebi.pride.jaxb.model.SpectrumSettings rawSpecSettings) {
        ScanList scanList = null;
        if (rawSpecSettings != null) {
            uk.ac.ebi.pride.jaxb.model.AcqSpecification rawActSpec = rawSpecSettings.getAcqSpecification();
            uk.ac.ebi.pride.jaxb.model.SpectrumInstrument rawSpecInstrument = rawSpecSettings.getSpectrumInstrument();
            ParamGroup params = new ParamGroup();
            List<Scan> scans = new ArrayList<Scan>();
            List<ParamGroup> scanWindows = getScanWindows(rawSpecInstrument);
            if (rawActSpec != null) {
                params.addCvParam(getMethodOfCombination(rawActSpec.getMethodOfCombination()));
                for (uk.ac.ebi.pride.jaxb.model.Aquisition rawAcq : rawActSpec.getAcquisition()) {
                    ParamGroup scanParmaGroup = transformParamGroup(rawAcq);
                    Scan scan = new Scan(null, null, null, null, scanWindows, scanParmaGroup);
                    scans.add(scan);
                }
            } else {
                params.addCvParam(getMethodOfCombination(null));
                Scan scan = new Scan(null, null, null, null, scanWindows, null);
                scans.add(scan);
            }
            scanList = new ScanList(scans, params);
        }
        return scanList;
    }

    /**
     * create a list of scan windows
     *
     * @param rawSpecInstrument spectrum instrument in pride xml.
     * @return List<ParamGroup> a list of param groups represents scan windows.
     */
    private static List<ParamGroup> getScanWindows(uk.ac.ebi.pride.jaxb.model.SpectrumInstrument rawSpecInstrument) {
        List<ParamGroup> scanWindows = null;
        Float mzRangeStartValue = rawSpecInstrument.getMzRangeStart();
        Float mzRangeStopValue = rawSpecInstrument.getMzRangeStop();
        if (mzRangeStartValue != null && mzRangeStopValue != null) {
            CvTermReference mzStartTerm = CvTermReference.SCAN_WINDOW_LOWER_LIMIT;
            CvParam mzRangeStart = new CvParam(mzStartTerm.getAccession(), mzStartTerm.getName(), mzStartTerm.getCvLabel(), mzRangeStartValue.toString(), null, null, null);
            CvTermReference mzStopTerm = CvTermReference.SCAN_WINDOW_UPPER_LIMIT;
            CvParam mzRangeStop = new CvParam(mzStopTerm.getAccession(), mzStopTerm.getName(), mzStopTerm.getCvLabel(), mzRangeStopValue.toString(), null, null, null);
            scanWindows = new ArrayList<ParamGroup>();
            ParamGroup scanWindow = new ParamGroup();
            scanWindow.addCvParam(mzRangeStart);
            scanWindow.addCvParam(mzRangeStop);
            scanWindows.add(scanWindow);
        }
        return scanWindows;
    }

    /**
     * create a cv param for method of combination
     *
     * @param value original value of the method of combination.
     * @return CvParam  cv param in core data model format.
     */
    private static CvParam getMethodOfCombination(String value) {
        CvTermReference cvTerm = CvTermReference.NO_COMBINATION;
        if (value != null && value.toLowerCase().contains("sum")) {
            cvTerm = CvTermReference.SUM_OF_SPECTRA;
        } else {
            value = null;
        }
        return new CvParam(cvTerm.getAccession(), cvTerm.getName(), cvTerm.getCvLabel(), value, null, null, null);
    }

    /**
     * Convert precursor list
     *
     * @param rawPrecursors pride xml precursor list
     * @return List<Precursor> a list of precursors
     */
    public static List<Precursor> transformPrecursorList(uk.ac.ebi.pride.jaxb.model.PrecursorList rawPrecursors) {
        List<Precursor> precursors = null;
        if (rawPrecursors != null) {
            precursors = new ArrayList<Precursor>();
            for (uk.ac.ebi.pride.jaxb.model.Precursor rawPrecursor : rawPrecursors.getPrecursor()) {
                precursors.add(transformPrecursor(rawPrecursor));
            }
        }
        return precursors;
    }

    /**
     * transform precursors from pride xml to core data model
     * <p/>
     * Note: In MzData schema, there are experimentRef, this is not included
     * in pride xml schema.
     *
     * @param rawPrecursor pride xml precursor
     * @return Precursor   precursor
     */
    public static Precursor transformPrecursor(uk.ac.ebi.pride.jaxb.model.Precursor rawPrecursor) {
        Spectrum spectrum = transformSpectrum(rawPrecursor.getSpectrum());
        ParamGroup ionSelection = transformParamGroup(rawPrecursor.getIonSelection());
        List<ParamGroup> ionSelections = null;
        if (ionSelection != null) {
            ionSelections = new ArrayList<ParamGroup>();
            ionSelections.add(ionSelection);
        }
        ParamGroup activation = transformParamGroup(rawPrecursor.getActivation());
        return new Precursor(spectrum, null, null, null, ionSelections, activation);
    }

    /**
     * Transform BinaryDataArray from pride xml to core data model
     *
     * @param rawArr     pride xml binary data array
     * @param binaryType binary type
     * @return BinaryDataArray binary data array
     */
    public static BinaryDataArray transformBinaryDataArray(uk.ac.ebi.pride.jaxb.model.PeakListBinary rawArr, CvTermReference binaryType) {
        uk.ac.ebi.pride.jaxb.model.Data rawData = rawArr.getData();
        byte[] binary = rawData.getValue();
        CvTermReference dataType = "32".equals(rawData.getPrecision()) ? CvTermReference.FLOAT_32_BIT : CvTermReference.FLOAT_64_BIT;
        ByteOrder order = "big".equals(rawData.getEndian()) ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        double[] binaryDoubleArr = BinaryDataUtils.toDoubleArray(binary, dataType, order);
        ParamGroup params = new ParamGroup();
        params.addCvParam(new CvParam(dataType.getAccession(), dataType.getName(), dataType.getCvLabel(), null, null, null, null));
        CvTermReference compressionTerm = CvTermReference.NO_COMPRESSION;
        params.addCvParam(new CvParam(compressionTerm.getAccession(), compressionTerm.getName(), compressionTerm.getCvLabel(), null, null, null, null));
        params.addCvParam(new CvParam(binaryType.getAccession(), binaryType.getName(), binaryType.getCvLabel(), null, null, null, null));
        return new BinaryDataArray(null, binaryDoubleArr, params);
    }

    /**
     * Convert protein identification
     *
     * @param identification pride xml protein identification
     * @return Identification  protein identification
     */
    public static Identification transformIdentification(uk.ac.ebi.pride.jaxb.model.Identification identification) {
        return identification instanceof uk.ac.ebi.pride.jaxb.model.TwoDimensionalIdentification ? transformTwoDimIdent((uk.ac.ebi.pride.jaxb.model.TwoDimensionalIdentification) identification) : transformGelFreeIdent((uk.ac.ebi.pride.jaxb.model.GelFreeIdentification) identification);
    }

    /**
     * Convert two dimensional identification
     * <p/>
     * ToDo: there are code dupliation between transformTwoDimIdent and transformGelFreeIdent
     *
     * @param rawIdent pride xml two dimensional identification
     * @return TwoDimIdentification    two dimentional identification
     */
    public static Identification transformTwoDimIdent(uk.ac.ebi.pride.jaxb.model.TwoDimensionalIdentification rawIdent) {
        Identification ident = null;
        if (rawIdent != null) {
            List<uk.ac.ebi.pride.jaxb.model.PeptideItem> rawPeptides = rawIdent.getPeptideItem();
            List<Peptide> peptides = null;
            if (rawPeptides != null) {
                peptides = new ArrayList<Peptide>();
                for (uk.ac.ebi.pride.jaxb.model.PeptideItem rawPeptide : rawPeptides) {
                    peptides.add(transformPeptide(rawPeptide));
                }
            }
            ParamGroup params = transformParamGroup(rawIdent.getAdditional());
            uk.ac.ebi.pride.jaxb.model.SimpleGel rawGel = rawIdent.getGel();
            Gel gel = null;
            if (rawGel != null) {
                gel = transformGel(rawGel, rawIdent.getGelLocation(), rawIdent.getMolecularWeight(), rawIdent.getPI());
            }
            Double seqConverage = rawIdent.getSequenceCoverage();
            double seqConverageVal = seqConverage == null ? -1 : seqConverage;
            Double threshold = rawIdent.getThreshold();
            double thresholdVal = threshold == null ? -1 : threshold;
            SearchEngineType searchEngine = SearchEngineType.getByName(rawIdent.getSearchEngine());
            Score score = null;
            SearchDataBase searchDataBase = new SearchDataBase(rawIdent.getDatabase(), rawIdent.getDatabaseVersion());
            DBSequence dbSequence = new DBSequence(rawIdent.getAccession(), searchDataBase, rawIdent.getAccessionVersion(), rawIdent.getSpliceIsoform());
            ident = new Identification(params, rawIdent.getId(), null, dbSequence, false, peptides, score, thresholdVal, seqConverageVal, gel);
        }
        return ident;
    }

    /**
     * Convert gel free protein identification
     *
     * @param rawIdent pride xml protein identification
     * @return GelFreeIdentification   gel free identification
     */
    public static Identification transformGelFreeIdent(uk.ac.ebi.pride.jaxb.model.GelFreeIdentification rawIdent) {
        Identification ident = null;
        if (rawIdent != null) {
            List<uk.ac.ebi.pride.jaxb.model.PeptideItem> rawPeptides = rawIdent.getPeptideItem();
            List<Peptide> peptides = null;
            if (rawPeptides != null) {
                peptides = new ArrayList<Peptide>();
                for (uk.ac.ebi.pride.jaxb.model.PeptideItem rawPeptide : rawPeptides) {
                    peptides.add(transformPeptide(rawPeptide));
                }
            }
            ParamGroup params = transformParamGroup(rawIdent.getAdditional());
            Double seqConverage = rawIdent.getSequenceCoverage();
            double seqConverageVal = seqConverage == null ? -1 : seqConverage;
            Double threshold = rawIdent.getThreshold();
            double thresholdVal = threshold == null ? -1 : threshold;
            Score score = null;
            SearchDataBase searchDataBase = new SearchDataBase(rawIdent.getDatabase(), rawIdent.getDatabaseVersion());
            DBSequence dbSequence = new DBSequence(rawIdent.getAccession(), searchDataBase, rawIdent.getAccessionVersion(), rawIdent.getSpliceIsoform());
            return new Identification(params, rawIdent.getId(), null, dbSequence, false, peptides, score, thresholdVal, seqConverageVal, null);
        }
        return ident;
    }

    /**
     * Transform gel from pride xml to core data model.
     *
     * @param rawGel      gel in pride xml format.
     * @param gelLocation gel location in pride xml format.
     * @param mw          molecular weight in pride xml.
     * @param pI          pI in pride xml.
     * @return Gel  gel in core data model.
     */
    private static Gel transformGel(uk.ac.ebi.pride.jaxb.model.SimpleGel rawGel, uk.ac.ebi.pride.jaxb.model.GelLocation gelLocation, Double mw, Double pI) {
        String gelLink = null;
        ParamGroup params = null;
        if (rawGel != null) {
            gelLink = rawGel.getGelLink();
            params = transformParamGroup(rawGel.getAdditional());
        }
        double xCoordinate = -1;
        double yCoordinate = -1;
        if (gelLocation != null) {
            xCoordinate = gelLocation.getXCoordinate();
            yCoordinate = gelLocation.getYCoordinate();
        }
        double molWeight = mw == null ? -1 : mw;
        double pi = pI == null ? -1 : pI;
        return new Gel(params, gelLink, xCoordinate, yCoordinate, molWeight, pi);
    }

    /**
     * Transform peptide from pride xml to core data model.
     *
     * @param rawPeptide peptide in pride xml format.
     * @return Peptide  peptide in core data model.
     */
    public static Peptide transformPeptide(uk.ac.ebi.pride.jaxb.model.PeptideItem rawPeptide) {
        Spectrum spectrum = transformSpectrum(rawPeptide.getSpectrum());
        List<uk.ac.ebi.pride.jaxb.model.ModificationItem> rawMods = rawPeptide.getModificationItem();
        List<Modification> modifications = null;
        if (rawMods != null) {
            modifications = new ArrayList<Modification>();
            for (uk.ac.ebi.pride.jaxb.model.ModificationItem rawMod : rawMods) {
                modifications.add(transformModification(rawMod));
            }
        }
        List<uk.ac.ebi.pride.jaxb.model.FragmentIon> rawFragIons = rawPeptide.getFragmentIon();
        List<FragmentIon> fragmentIons = null;
        if (rawFragIons != null && !rawFragIons.isEmpty()) {
            fragmentIons = new ArrayList<FragmentIon>();
            for (uk.ac.ebi.pride.jaxb.model.FragmentIon rawFrag : rawFragIons) {
                ParamGroup fragIonParams = transformParamGroup(rawFrag);
                fragmentIons.add(new FragmentIon(fragIonParams));
            }
        }
        ParamGroup params = transformParamGroup(rawPeptide.getAdditional());
        int startPos = -1;
        int stopPos = -1;
        BigInteger start = rawPeptide.getStart();
        if (start != null) {
            startPos = start.intValue();
        }
        BigInteger stop = rawPeptide.getEnd();
        if (stop != null) {
            stopPos = stop.intValue();
        }
        PeptideSequence peptideSequence = new PeptideSequence(null, null, rawPeptide.getSequence(), modifications);
        List<PeptideEvidence> peptideEvidences = new ArrayList<PeptideEvidence>();
        PeptideEvidence peptideEvidence = new PeptideEvidence(null, null, startPos, stopPos, false, peptideSequence, null);
        peptideEvidences.add(peptideEvidence);
        int charge = -1;
        double mz = 0.0;
        if (spectrum != null) {
            charge = DataAccessUtilities.getPrecursorCharge(spectrum);
            mz = DataAccessUtilities.getPrecursorMz(spectrum);
        }
        Score score = DataAccessUtilities.getScore(params);
        SpectrumIdentification spectrumIdentification = new SpectrumIdentification(params, null, null, charge, mz, 0.0, 0.0, peptideSequence, -1, false, null, null, peptideEvidences, fragmentIons, score, spectrum, null);
        return new Peptide(peptideEvidence, spectrumIdentification);
    }

    /**
     * Transform modification from pride xml to core data model
     *
     * @param rawMod modification in pride xml format.
     * @return Modification modification in core data model.
     */
    private static Modification transformModification(uk.ac.ebi.pride.jaxb.model.ModificationItem rawMod) {
        List<String> rawMonoDelta = rawMod.getModMonoDelta();
        List<Double> monoDelta = null;
        if (rawMonoDelta != null) {
            monoDelta = new ArrayList<Double>();
            for (String delta : rawMonoDelta) {
                if (NumberUtilities.isNumber(delta)) {
                    monoDelta.add(new Double(delta));
                }
            }
        }
        List<String> rawAvgDelta = rawMod.getModAvgDelta();
        List<Double> avgDelta = null;
        if (rawAvgDelta != null) {
            avgDelta = new ArrayList<Double>();
            for (String delta : rawAvgDelta) {
                if (NumberUtilities.isNumber(delta)) {
                    avgDelta.add(new Double(delta));
                }
            }
        }
        ParamGroup params = transformParamGroup(rawMod.getAdditional());
        int location = -1;
        BigInteger rawLocation = rawMod.getModLocation();
        if (rawLocation != null) {
            location = rawLocation.intValue();
        }
        return new Modification(params, rawMod.getModAccession(), null, location, null, avgDelta, monoDelta, rawMod.getModDatabase(), rawMod.getModDatabaseVersion());
    }

    /**
     * transform protocol from pride xml to core data model.
     *
     * @param rawProt protocol from pride xml.
     * @return Protocol protocol in core data model format.
     */
    public static ExperimentProtocol transformProtocol(uk.ac.ebi.pride.jaxb.model.Protocol rawProt) {
        ExperimentProtocol protocol = null;
        if (rawProt != null) {
            List<uk.ac.ebi.pride.jaxb.model.Param> rawSteps;
            List<ParamGroup> protocolSteps = null;
            uk.ac.ebi.pride.jaxb.model.ProtocolSteps rawProtSteps = rawProt.getProtocolSteps();
            if (rawProtSteps != null) {
                rawSteps = rawProtSteps.getStepDescription();
                if (rawSteps != null) {
                    protocolSteps = new ArrayList<ParamGroup>();
                    for (uk.ac.ebi.pride.jaxb.model.Param rawStep : rawSteps) {
                        protocolSteps.add(transformParamGroup(rawStep));
                    }
                }
            }
            protocol = new ExperimentProtocol(null, PROTOCOL_ID, rawProt.getProtocolName(), protocolSteps);
        }
        return protocol;
    }

    /**
     * Transform a paramgroup from pride xml format to core data model format.
     * <p/>
     * Note: if the paramgroup returned is not null, then it must have a cv param list and
     * a user param list, even they are empty.
     *
     * @param rawParams paramgroup in pride xml format.
     * @return ParamGroup   paramgroup in core data model format.
     */
    public static ParamGroup transformParamGroup(uk.ac.ebi.pride.jaxb.model.Param rawParams) {
        ParamGroup params = null;
        if (rawParams != null) {
            List<CvParam> cvParams = transformCvParams(rawParams.getCvParam());
            List<UserParam> userParams = transformUserParams(rawParams.getUserParam());
            params = new ParamGroup(cvParams, userParams);
        }
        return params;
    }

    /**
     * Convert a list of user params
     *
     * @param rawUserParams pride xml user params
     * @return List<UserParam> a list of user params
     */
    public static List<UserParam> transformUserParams(List<uk.ac.ebi.pride.jaxb.model.UserParam> rawUserParams) {
        List<UserParam> userParams = new ArrayList<UserParam>();
        if (rawUserParams != null) {
            for (uk.ac.ebi.pride.jaxb.model.UserParam rawUserParam : rawUserParams) {
                userParams.add(transformUserParam(rawUserParam));
            }
        }
        return userParams;
    }

    /**
     * Transform a user parameter from pride xml format to core data model format.
     * Note: there is neither data type or unit related information in pride xml format.
     *
     * @param rawUserParam a user parameter in pride xml format.
     * @return UserParam    a user parameter in core data model format.
     */
    public static UserParam transformUserParam(uk.ac.ebi.pride.jaxb.model.UserParam rawUserParam) {
        return new UserParam(rawUserParam.getName(), null, rawUserParam.getValue(), null, null, null);
    }

    /**
     * Convert a list of cv params
     *
     * @param rawCvParams pride xml cv params
     * @return List<CvParam>   a list of cv params
     */
    public static List<CvParam> transformCvParams(List<uk.ac.ebi.pride.jaxb.model.CvParam> rawCvParams) {
        List<CvParam> cvParams = new ArrayList<CvParam>();
        if (rawCvParams != null) {
            for (uk.ac.ebi.pride.jaxb.model.CvParam rawCvParam : rawCvParams) {
                cvParams.add(transformCvParam(rawCvParam));
            }
        }
        return cvParams;
    }

    /**
     * Transform a cv parameter from pride xml format to core data model format.
     * Note: there is no unit related information in pride xml format.
     *
     * @param rawCvParam a cv parameter in pride xml format.
     * @return CvParam  a cv parameter in core data model format
     */
    public static CvParam transformCvParam(uk.ac.ebi.pride.jaxb.model.CvParam rawCvParam) {
        return new CvParam(rawCvParam.getAccession(), rawCvParam.getName(), rawCvParam.getCvLabel(), rawCvParam.getValue(), null, null, null);
    }

    /**
     * Transform a list of cvlookup object from pride Xml format to core data model format.
     *
     * @param rawCvLookups a list of cv lookups in pride xml format.
     * @return List<CVLookup>   a list of cv lookups in core data model format.
     */
    public static List<CVLookup> transformCvLookups(List<uk.ac.ebi.pride.jaxb.model.CvLookup> rawCvLookups) {
        List<CVLookup> cvLookups = null;
        if (rawCvLookups != null) {
            cvLookups = new ArrayList<CVLookup>();
            for (uk.ac.ebi.pride.jaxb.model.CvLookup rawCvLookup : rawCvLookups) {
                CVLookup cvLookup = new CVLookup(rawCvLookup.getCvLabel(), rawCvLookup.getFullName(), rawCvLookup.getVersion(), rawCvLookup.getAddress());
                cvLookups.add(cvLookup);
            }
        }
        return cvLookups;
    }

    /**
     * Transform sample from pride xml format to core data model format
     *
     * @param rawAdmin Admin object in pride xml format, which contains all the details about sample.
     * @return Sample   sample object in core data model format.
     */
    public static Sample transformSample(uk.ac.ebi.pride.jaxb.model.Admin rawAdmin) {
        Sample sample = null;
        if (rawAdmin != null) {
            ParamGroup params = transformParamGroup(rawAdmin.getSampleDescription());
            sample = new Sample(params, SAMPLE_ID, rawAdmin.getSampleName());
        }
        return sample;
    }

    /**
     * Transform an software object from pride xml format to core data model format.
     *
     * @param rawDataProcessing software object in pride xml format.
     * @return Software software object in core data model format.
     */
    public static Software transformSoftware(uk.ac.ebi.pride.jaxb.model.DataProcessing rawDataProcessing) {
        Software software = null;
        if (rawDataProcessing != null) {
            uk.ac.ebi.pride.jaxb.model.Software rawSoftware = rawDataProcessing.getSoftware();
            List<CvParam> cvParams = new ArrayList<CvParam>();
            List<UserParam> userParams = new ArrayList<UserParam>();
            String comments = rawSoftware.getComments();
            if (comments != null) {
                userParams.add(new UserParam(COMMENTS, null, rawSoftware.getComments(), null, null, null));
            }
            XMLGregorianCalendar completionTime = rawSoftware.getCompletionTime();
            if (completionTime != null) {
                userParams.add(new UserParam(COMPLETION_TIME, null, completionTime.toString(), null, null, null));
            }
            software = new Software(new ParamGroup(cvParams, userParams), null, rawSoftware.getName(), null, null, null, rawSoftware.getVersion());
        }
        return software;
    }

    /**
     * Transform an instrument from pride xml to core data model format.
     * <p/>
     * Note: for each instrument object, only three instrument components can exists.
     * multiple analyzer should be spliced into different instrument objecs.
     *
     * @param rawInstrument     instrument object in pride xml format.
     * @param rawDataProcessing data processing object in pride xml format.
     * @return List<Instrument> a list of instrument objects.
     */
    public static List<InstrumentConfiguration> transformInstrument(uk.ac.ebi.pride.jaxb.model.Instrument rawInstrument, uk.ac.ebi.pride.jaxb.model.DataProcessing rawDataProcessing) {
        List<InstrumentConfiguration> instrumentConfigurations = null;
        if (rawInstrument != null) {
            instrumentConfigurations = new ArrayList<InstrumentConfiguration>();
            ParamGroup params = new ParamGroup();
            CvTermReference cvTerm = CvTermReference.INSTRUMENT_MODEL;
            params.addCvParam(new CvParam(cvTerm.getAccession(), cvTerm.getName(), cvTerm.getCvLabel(), rawInstrument.getInstrumentName(), null, null, null));
            Software software = transformSoftware(rawDataProcessing);
            int sourceOrder = 1;
            int detectorOrder = rawInstrument.getAnalyzerList().getAnalyzer().size() + 2;
            List<InstrumentComponent> source = new ArrayList<InstrumentComponent>();
            InstrumentComponent sourceInstrument = transformSource(sourceOrder, rawInstrument.getSource());
            source.add(sourceInstrument);
            List<InstrumentComponent> detector = new ArrayList<InstrumentComponent>();
            InstrumentComponent detectorInstrument = transformDetector(detectorOrder, rawInstrument.getDetector());
            detector.add(detectorInstrument);
            List<uk.ac.ebi.pride.jaxb.model.Param> rawAnalyzers = rawInstrument.getAnalyzerList().getAnalyzer();
            int orderCnt = 2;
            List<InstrumentComponent> analyzer = new ArrayList<InstrumentComponent>();
            for (uk.ac.ebi.pride.jaxb.model.Param rawAnalyzer : rawAnalyzers) {
                InstrumentComponent analyzerInstrument = transformAnalyzer(orderCnt, rawAnalyzer);
                analyzer.add(analyzerInstrument);
                orderCnt++;
            }
            instrumentConfigurations.add(new InstrumentConfiguration(rawInstrument.getInstrumentName(), null, software, source, analyzer, detector, params));
        }
        return instrumentConfigurations;
    }

    /**
     * Transform source from pride xml to core data model format
     *
     * @param order     order of encounter.
     * @param rawSource source in pride xml format.
     * @return InstrumentComponent  source in data model format.
     */
    private static InstrumentComponent transformSource(int order, uk.ac.ebi.pride.jaxb.model.Param rawSource) {
        InstrumentComponent component = null;
        if (rawSource != null) {
            ParamGroup params = transformParamGroup(rawSource);
            component = new InstrumentComponent(order, params);
        }
        return component;
    }

    /**
     * Transform analyzer from pride xml to core data model format
     *
     * @param order       order of encounter.
     * @param rawAnalyzer analyzer in pride xml format.
     * @return InstrumentComponent  analyzer in data model format.
     */
    private static InstrumentComponent transformAnalyzer(int order, uk.ac.ebi.pride.jaxb.model.Param rawAnalyzer) {
        InstrumentComponent component = null;
        if (rawAnalyzer != null) {
            ParamGroup params = transformParamGroup(rawAnalyzer);
            component = new InstrumentComponent(order, params);
        }
        return component;
    }

    /**
     * Transform detector from pride xml to core data model format
     *
     * @param order       order of encounter.
     * @param rawDetector detector in pride xml format.
     * @return InstrumentComponent  detector in data model format.
     */
    private static InstrumentComponent transformDetector(int order, uk.ac.ebi.pride.jaxb.model.Param rawDetector) {
        InstrumentComponent component = null;
        if (rawDetector != null) {
            ParamGroup params = transformParamGroup(rawDetector);
            component = new InstrumentComponent(order, params);
        }
        return component;
    }

    /**
     * Transform data processing from pride xml to core data model format.
     *
     * @param rawDataProcessing data processing in pride xml.
     * @return DataProcessing    data processing in core data model.
     */
    public static DataProcessing transformDataProcessing(uk.ac.ebi.pride.jaxb.model.DataProcessing rawDataProcessing) {
        DataProcessing dataProc = null;
        if (rawDataProcessing != null) {
            Software software = transformSoftware(rawDataProcessing);
            List<ProcessingMethod> procMethods = new ArrayList<ProcessingMethod>();
            uk.ac.ebi.pride.jaxb.model.Param rawProcMethod = rawDataProcessing.getProcessingMethod();
            if (rawProcMethod != null) {
                ParamGroup params = transformParamGroup(rawProcMethod);
                procMethods.add(new ProcessingMethod(PROCESSING_METHOD_ORDER, software, params));
            } else if (software != null) {
                procMethods.add(new ProcessingMethod(PROCESSING_METHOD_ORDER, software, new ParamGroup()));
            }
            dataProc = new DataProcessing(DATA_PROCESSING_ID, procMethods);
        }
        return dataProc;
    }

    /**
     * Transform a list of contacts from pride xml to core data model format
     *
     * @param rawAdmin admin object in pride xml.
     * @return List<ParamGroup> a list of param groups represents contact details.
     */
    public static List<ParamGroup> transformContacts(uk.ac.ebi.pride.jaxb.model.Admin rawAdmin) {
        List<ParamGroup> contact = null;
        if (rawAdmin != null) {
            contact = new ArrayList<ParamGroup>();
            List<uk.ac.ebi.pride.jaxb.model.Contact> rawContacts = rawAdmin.getContact();
            for (uk.ac.ebi.pride.jaxb.model.Contact rawContact : rawContacts) {
                List<CvParam> cvParams = new ArrayList<CvParam>();
                CvTermReference contactName = CvTermReference.CONTACT_NAME;
                cvParams.add(new CvParam(contactName.getAccession(), contactName.getName(), contactName.getCvLabel(), rawContact.getName(), null, null, null));
                CvTermReference contactOrg = CvTermReference.CONTACT_ORG;
                cvParams.add(new CvParam(contactOrg.getAccession(), contactOrg.getName(), contactOrg.getCvLabel(), rawContact.getInstitution(), null, null, null));
                List<UserParam> userParams = null;
                String contactInfo = rawContact.getContactInfo();
                if (contactInfo != null) {
                    userParams = new ArrayList<UserParam>();
                    userParams.add(new UserParam(CONTACT_INFO, null, contactInfo, null, null, null));
                }
                contact.add(new ParamGroup(cvParams, userParams));
            }
        }
        return contact;
    }

    /**
     * Transform a list of contacts from person contact
     *
     * @param rawAdmin admin object in pride xml.
     * @return List<ParamGroup> a list of param groups represents contact details.
     */
    public static List<Person> transformContactToPerson(uk.ac.ebi.pride.jaxb.model.Admin rawAdmin) {
        if (rawAdmin != null) {
            List<Person> persons = new ArrayList<Person>();
            List<uk.ac.ebi.pride.jaxb.model.Contact> rawContacts = rawAdmin.getContact();
            for (uk.ac.ebi.pride.jaxb.model.Contact rawContact : rawContacts) {
                CvTermReference contactTerm = CvTermReference.CONTACT_NAME;
                List<CvParam> cvParams = DataAccessUtilities.getCvParam(rawContact.getName(), contactTerm.getCvLabel(), contactTerm.getAccession(), contactTerm.getName());
                CvTermReference contactOrg = CvTermReference.CONTACT_ORG;
                cvParams.add(new CvParam(contactOrg.getAccession(), contactOrg.getName(), contactOrg.getCvLabel(), rawContact.getInstitution(), null, null, null));
                List<UserParam> userParams = null;
                String contactInfo = rawContact.getContactInfo();
                if (contactInfo != null) {
                    userParams = new ArrayList<UserParam>();
                    userParams.add(new UserParam(CONTACT_INFO, null, contactInfo, null, null, null));
                }
                List<Organization> affiliation = new ArrayList<Organization>();
                affiliation.add(new Organization(null, rawContact.getInstitution(), null, null));
                ParamGroup contact = new ParamGroup(cvParams, userParams);
                Person contactPerson = new Person(contact, null, rawContact.getName(), null, null, null, affiliation, null);
                persons.add(contactPerson);
            }
            return persons;
        }
        return null;
    }

    /**
     * Transform a list of contacts from person contact
     *
     * @param rawAdmin admin object in pride xml.
     * @return List<ParamGroup> a list of param groups represents contact details.
     */
    public static List<Organization> transformContactToOrganization(uk.ac.ebi.pride.jaxb.model.Admin rawAdmin) {
        if (rawAdmin != null) {
            List<Organization> organizations = new ArrayList<Organization>();
            List<uk.ac.ebi.pride.jaxb.model.Contact> rawContacts = rawAdmin.getContact();
            for (uk.ac.ebi.pride.jaxb.model.Contact rawContact : rawContacts) {
                CvTermReference contactOrg = CvTermReference.CONTACT_ORG;
                List<CvParam> cvParams = new ArrayList<CvParam>();
                cvParams.add(new CvParam(contactOrg.getAccession(), contactOrg.getName(), contactOrg.getCvLabel(), rawContact.getInstitution(), null, null, null));
                List<UserParam> userParams = null;
                ParamGroup contact = new ParamGroup(cvParams, userParams);
                Organization contactOrganization = new Organization(contact, null, rawContact.getInstitution(), null, null);
                organizations.add(contactOrganization);
            }
            return organizations;
        }
        return null;
    }

    /**
     * Transform source file from pride xml to core data model
     *
     * @param rawAdmin Admin in pride xml format.
     * @return SourceFile   source file in core data model.
     */
    public static SourceFile transformSourceFile(uk.ac.ebi.pride.jaxb.model.Admin rawAdmin) {
        SourceFile sourceFile = null;
        if (rawAdmin != null) {
            uk.ac.ebi.pride.jaxb.model.SourceFile rawSourceFile = rawAdmin.getSourceFile();
            if (rawSourceFile != null) {
                String rawFileType = rawSourceFile.getFileType();
                ParamGroup params = new ParamGroup();
                params.addUserParam(new UserParam(SOURCE_FILE_TYPE, null, rawFileType, null, null, null));
                sourceFile = new SourceFile(params, SOURCE_FILE_ID, rawSourceFile.getNameOfFile(), rawSourceFile.getPathToFile());
            }
        }
        return sourceFile;
    }

    /**
     * Transform references from pride xml format to core data model format
     *
     * @param rawReferences a list of references in pride xml format.
     * @return List<Reference>  a list of references in core data model foramt.
     */
    public static List<Reference> transformReferences(List<uk.ac.ebi.pride.jaxb.model.Reference> rawReferences) {
        List<Reference> references = null;
        if (rawReferences != null) {
            references = new ArrayList<Reference>();
            for (uk.ac.ebi.pride.jaxb.model.Reference rawReference : rawReferences) {
                references.add(new Reference(transformParamGroup(rawReference.getAdditional()).getCvParams(), transformParamGroup(rawReference.getAdditional()).getUserParams(), null, null, rawReference.getRefLine()));
            }
        }
        return references;
    }

    /**
     * Transform additional params from pride xml to core data model format.
     *
     * @param rawAdditionalParams
     * @return ParamGroup   additional param groups
     */
    public static ParamGroup transformAdditional(uk.ac.ebi.pride.jaxb.model.Param rawAdditionalParams) {
        return transformParamGroup(rawAdditionalParams);
    }
}
