package org.systemsbiology.apmlparser.v2;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.*;
import org.systemsbiology.apml.*;
import org.systemsbiology.apmlparser.v2.datatype.*;
import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;

/**
 * Reads an APML file.
 *
 * The method is a compromise between
 * the elegance of DOM/XmlBeans and the efficiency of stax.  We pull subtrees
 * out of the pepxml file for bits that we care about, and process those with XmlBeans objects
 */
public class APMLReader {

    static Logger _log = Logger.getLogger(APMLReader.class);

    public static final int ELEMENT_UNKNOWN = -1;

    public static final int ELEMENT_APML = 0;

    public static final int ELEMENT_DATA_PROCESSING = 1;

    public static final int ELEMENT_DATA = 2;

    public static final int ELEMENT_SOFTWARE = 3;

    public static final int ELEMENT_PEAK_LISTS = 4;

    public static final int ELEMENT_PEAK_LIST = 5;

    public static final int ELEMENT_FEATURES = 6;

    public static final int ELEMENT_FEATURE = 7;

    public static final int ELEMENT_ALIGNMENT = 8;

    public static final int ELEMENT_FEATURE_SOURCE_LIST = 9;

    public static final int ELEMENT_ALIGNED_FEATURES = 10;

    public static final int ELEMENT_ALIGNED_FEATURE = 11;

    public static final int ELEMENT_CLUSTER_PROFILE = 12;

    public static final int ELEMENT_CLUSTERS = 13;

    public static final int ELEMENT_CLUSTER = 14;

    public static final String[] ELEMENT_NAMES = { "apml", "dataProcessing", "data", "software", "peak_lists", "peak_list", "features", "feature", "alignment", "feature_source_list", "aligned_features", "aligned_feature", "cluster_profile", "clusters", "cluster" };

    protected Map<String, Integer> _elementNameIDMap = null;

    protected XMLEventReader reader = null;

    protected int maxBufferedBytes = 100 * 1024 * 1024;

    protected ResettableStringWriter swriter = null;

    protected boolean validate = true;

    protected XmlOptions xmlOptions = null;

    protected ArrayList<Object> validationErrors = null;

    protected APMLReaderListener readerListener = null;

    protected boolean readSingleScanPeaks = false;

    protected boolean isInitialized = false;

    /**
     * Set the readerListener.
     * @param readerListener A user-provided listening class to handle all events from APMLReader
     */
    public APMLReader(APMLReaderListener readerListener) {
        this.readerListener = readerListener;
    }

    /**
     * Set the readerListener and indicate whether we're validating
     * @param readerListener
     * @param validate
     */
    public APMLReader(APMLReaderListener readerListener, boolean validate) {
        this(readerListener);
        this.validate = validate;
    }

    /**
     * Initialize the xmlOptions, setting validation strategy.  Create the output StringWriter for storing
     * XML in memory
     */
    protected void init() {
        if (validate) {
            validationErrors = new ArrayList();
            xmlOptions = new XmlOptions();
            xmlOptions.setErrorListener(validationErrors);
        } else {
            xmlOptions = new XmlOptions();
        }
        swriter = new ResettableStringWriter(maxBufferedBytes);
        isInitialized = true;
    }

    /**
     * Read in a file, extract modifications and features
     * @param file
     * @throws FileNotFoundException if the input file isn't found
     * @throws XMLStreamException if there's an XML-parsing problem
     * @throws XmlException if there's a schema-related problem with the XML, e.g., an invalid attribute
     */
    public void read(File file) throws FileNotFoundException, XMLStreamException, XmlException {
        _log.debug("Reading APML file " + file.getAbsolutePath());
        if (!isInitialized) init();
        XMLInputFactory inFactory = XMLInputFactory.newInstance();
        FileReader freader = new FileReader(file);
        reader = inFactory.createXMLEventReader(freader);
        while (reader.hasNext()) {
            XMLEvent evt = reader.nextEvent();
            if (evt.isEndDocument()) break;
            if (evt.isStartElement()) {
                StartElement startEvt = evt.asStartElement();
                switch(getElementType(evt)) {
                    case ELEMENT_APML:
                        break;
                    case ELEMENT_DATA_PROCESSING:
                        processDataProcessing(startEvt);
                        break;
                    case ELEMENT_DATA:
                        processData(startEvt);
                        break;
                    case ELEMENT_UNKNOWN:
                        String message = "Unknown start element " + evt.asStartElement().getName();
                        if (validate) throw new XmlException(message);
                        _log.info(message);
                        break;
                }
            }
        }
    }

    /**
     * Process the dataProcessing element that is common to all apml files
     * @param dataProcessingStartElement
     * @throws XMLStreamException
     * @throws XmlException
     */
    protected void processDataProcessing(StartElement dataProcessingStartElement) throws XMLStreamException, XmlException {
        _log.debug("Processing dataProcessing...");
        DataProcessing dataProcessing = new DataProcessing();
        Attribute processingDateAttribute = dataProcessingStartElement.getAttributeByName(new QName("processingDate"));
        if (processingDateAttribute != null) {
            String processingDateString = dataProcessingStartElement.getAttributeByName(new QName("processingDate")).getValue();
            dataProcessing.setProcessingDate(new XmlCalendar(processingDateString));
            _log.debug("\tprocessing date: " + dataProcessing.getProcessingDate());
        }
        List<DataProcessing.Software> softwareList = new ArrayList<DataProcessing.Software>();
        boolean doneReading = false;
        while (reader.hasNext() && !doneReading) {
            XMLEvent evt = reader.nextEvent();
            if (evt.isStartElement()) {
                StartElement startElement = evt.asStartElement();
                switch(getElementType(startElement)) {
                    case ELEMENT_SOFTWARE:
                        String softwareXml = collectElementXmlAsFragment(startElement);
                        DataProcessingType xmlBeansDPT = DataProcessingType.Factory.parse(softwareXml, xmlOptions);
                        conditionalValidate(xmlBeansDPT);
                        SoftwareType xmlBeansSoftware = xmlBeansDPT.getSoftwareArray(0);
                        DataProcessing.Software software = new DataProcessing.Software(xmlBeansSoftware.getName(), xmlBeansSoftware.getType().toString());
                        if (xmlBeansSoftware.getVersion() != null) software.setVersion(xmlBeansSoftware.getVersion());
                        dataProcessing.addSoftware(software);
                        SoftwareType.DataProcessingParam[] dpParams = xmlBeansSoftware.getDataProcessingParamArray();
                        if (dpParams != null && dpParams.length > 0) {
                            for (SoftwareType.DataProcessingParam dpParam : dpParams) {
                                software.addDataProcessingParam(dpParam.getName(), dpParam.getValue());
                            }
                        }
                        _log.debug("\tSoftware.  Name: " + xmlBeansSoftware.getName() + ", type: " + xmlBeansSoftware.getType() + ", version: " + software.getVersion());
                        softwareList.add(software);
                        break;
                    default:
                        String message = "Unexpected start element " + startElement.getName();
                        if (validate) throw new XmlException(message);
                        _log.info(message);
                        break;
                }
            } else if (evt.isEndElement() && getElementType(evt) == ELEMENT_DATA_PROCESSING) doneReading = true;
        }
        readerListener.reportDataProcessing(dataProcessing);
    }

    /**
     * Process the data element, the bulk of the file
     * @param startElement
     * @throws XMLStreamException
     * @throws XmlException
     */
    protected void processData(StartElement startElement) throws XMLStreamException, XmlException {
        _log.debug("Processing data...");
        boolean definedFileType = false;
        boolean doneReading = false;
        while (reader.hasNext() && !doneReading) {
            XMLEvent evt = reader.nextEvent();
            if (evt.isStartElement()) {
                StartElement startEvt = (StartElement) evt;
                switch(getElementType(startEvt)) {
                    case ELEMENT_PEAK_LISTS:
                        if (definedFileType) throw new XMLStreamException("APML files cannot have both peak_lists and alignment");
                        readerListener.setAPMLDataType(APMLReaderListener.DATA_TYPE_PEAK_LISTS);
                        definedFileType = true;
                        processPeakLists(startEvt);
                        break;
                    case ELEMENT_ALIGNMENT:
                        if (definedFileType) throw new XMLStreamException("APML files cannot have both peak_lists and alignment");
                        readerListener.setAPMLDataType(APMLReaderListener.DATA_TYPE_ALIGNMENT);
                        definedFileType = true;
                        processAlignment(startEvt);
                        break;
                    case ELEMENT_CLUSTER_PROFILE:
                        if (!definedFileType) throw new XMLStreamException("cluster_profile must come after peak_lists or alignment");
                        processClusterProfile(startEvt);
                        break;
                    default:
                        String message = "Unknown element " + startEvt.getName();
                        if (validate) throw new XMLStreamException(message);
                        _log.info(message);
                        break;
                }
            } else if (evt.isEndElement() && getElementType(evt) == ELEMENT_DATA) doneReading = true;
        }
        _log.debug("Done processing data");
    }

    /**
     * Process the alignment element
     * @param alignmentStartElement
     * @throws XMLStreamException
     * @throws XmlException
     */
    protected void processAlignment(StartElement alignmentStartElement) throws XMLStreamException, XmlException {
        _log.debug("Processing alignment...");
        AlignmentListener alignmentListener = readerListener.createAlignmentListener();
        boolean doneReading = false;
        while (reader.hasNext() && !doneReading) {
            XMLEvent evt = reader.nextEvent();
            if (evt.isStartElement()) {
                switch(getElementType(evt)) {
                    case ELEMENT_FEATURE_SOURCE_LIST:
                        _log.debug("Processing sources....");
                        String featureSourceListXML = collectElementXml(evt.asStartElement());
                        AlignmentType xbDummyAlignment = AlignmentType.Factory.parse(featureSourceListXML);
                        if (validate) {
                            if (!xbDummyAlignment.validate(xmlOptions)) {
                                boolean ok = false;
                                if (validationErrors.size() == 1) {
                                    XmlValidationError error = (XmlValidationError) validationErrors.get(0);
                                    if (error.getErrorType() == XmlValidationError.INCORRECT_ELEMENT && error.getExpectedQNames() != null && !error.getExpectedQNames().isEmpty() && error.getExpectedQNames().get(0).toString().contains("aligned_features")) {
                                        ok = true;
                                        validationErrors.remove(0);
                                    }
                                }
                                if (!ok) {
                                    for (Object validationError : validationErrors) {
                                        if (((String) validationError).contains("aligned_features")) validationErrors.remove(validationError);
                                    }
                                    reportValidationErrors(xbDummyAlignment);
                                }
                            }
                        }
                        AlignmentType.FeatureSourceList xbFeatureSourceList = xbDummyAlignment.getFeatureSourceList();
                        int expectedNumSources = xbFeatureSourceList.getCount();
                        AlignmentType.FeatureSourceList.Source[] xbSources = xbFeatureSourceList.getSourceArray();
                        int numSources = xbSources == null ? 0 : xbSources.length;
                        if (numSources != expectedNumSources) {
                            String errorMessage = "Expected " + expectedNumSources + " feature sources, got " + numSources;
                            if (validate) throw new XMLStreamException(); else _log.info(errorMessage);
                        }
                        if (xbSources != null) {
                            for (AlignmentType.FeatureSourceList.Source xbSource : xbSources) {
                                FeatureSource featureSource = new FeatureSource(xbSource.getLocation());
                                BigInteger xbSourceId = xbSource.getId();
                                if (xbSourceId != null) featureSource.setId(xbSourceId.intValue());
                                alignmentListener.reportFeatureSource(featureSource);
                            }
                            _log.debug("Got " + xbSources.length + " sources");
                        }
                        break;
                    case ELEMENT_ALIGNED_FEATURES:
                        processAlignedFeatures(evt.asStartElement(), alignmentListener);
                        break;
                    default:
                        String message = "Unknown element " + getElementName(evt) + ", expected feature_source_list or aligned_features";
                        if (validate) throw new XMLStreamException(message);
                        _log.info(message);
                        break;
                }
            } else if (evt.isEndElement() && getElementType(evt) == ELEMENT_ALIGNMENT) doneReading = true;
        }
    }

    /**
     * Process all the aligned_feature elements within the aligned_features element
     * @param alignedFeaturesStartElement
     * @param alignmentListener
     * @throws XMLStreamException
     * @throws XmlException
     */
    protected void processAlignedFeatures(StartElement alignedFeaturesStartElement, AlignmentListener alignmentListener) throws XMLStreamException, XmlException {
        _log.debug("Processing aligned features...");
        int expectedNumAlignedFeatures = Integer.parseInt(getRequiredAttributeValue(alignedFeaturesStartElement, "count"));
        _log.debug("\tCount: " + expectedNumAlignedFeatures);
        boolean doneReading = false;
        int numAlignedFeatures = 0;
        while (reader.hasNext() && !doneReading) {
            XMLEvent evt = reader.nextEvent();
            if (evt.isStartElement()) {
                switch(getElementType(evt)) {
                    case ELEMENT_ALIGNED_FEATURE:
                        long t1 = System.currentTimeMillis();
                        String alignedFeaturesXML = collectElementXml(evt.asStartElement());
                        long t2 = System.currentTimeMillis();
                        _log.debug((t2 - t1) + "ms");
                        AlignedFeaturesType xbDummyAlignedFeatures = AlignedFeaturesType.Factory.parse(alignedFeaturesXML);
                        AlignedFeatureType xbAlignedFeature = xbDummyAlignedFeatures.getAlignedFeatureArray(0);
                        AlignedFeature alignedFeature = new AlignedFeature();
                        alignedFeature.setId(xbAlignedFeature.getId().intValue());
                        alignedFeature.setCoords(processXmlBeansCoordinate(xbAlignedFeature.getCoordinate()));
                        AlignedFeatureType.Ppids xbPpids = xbAlignedFeature.getPpids();
                        if (xbPpids != null) {
                            int expectedNumPpids = xbPpids.getCount().intValue();
                            PpidCollectionType.Ppid[] xbPpidArray = xbPpids.getPpidArray();
                            int numPpids = xbPpidArray == null ? 0 : xbPpidArray.length;
                            if (numPpids != expectedNumPpids) {
                                String errorMessage = "Expected " + expectedNumPpids + " PutativePeptidIds, got " + numPpids;
                                if (validate) throw new XMLStreamException(errorMessage); else _log.info(errorMessage);
                            }
                            if (xbPpidArray != null) {
                                for (PpidCollectionType.Ppid xbPpid : xbPpidArray) alignedFeature.addPpid(processXmlBeansPpid(xbPpid));
                            }
                        }
                        AlignedFeatureType.Features xbFeatures = xbAlignedFeature.getFeatures();
                        int expectedNumFeatures = xbFeatures.getCount().intValue();
                        AlignedFeatureType.Features.Feature[] xbFeatureArray = xbFeatures.getFeatureArray();
                        int numFeatures = xbFeatureArray == null ? 0 : xbFeatureArray.length;
                        if (numFeatures != expectedNumFeatures) {
                            String errorMessage = "Expected " + expectedNumFeatures + " Features, got " + numFeatures;
                            if (validate) throw new XMLStreamException(errorMessage); else _log.info(errorMessage);
                        }
                        if (xbFeatureArray != null) {
                            for (AlignedFeatureType.Features.Feature xbFeature : xbFeatureArray) {
                                Feature feature = processXmlBeansFeature(xbFeature);
                                feature.setSource(xbFeature.getSource());
                                alignedFeature.addFeature(feature);
                            }
                        }
                        alignmentListener.reportAlignedFeature(alignedFeature);
                        numAlignedFeatures++;
                        break;
                    default:
                        String message = "Unknown element " + getElementName(evt) + ", expected aligned_feature";
                        if (validate) throw new XMLStreamException(message);
                        _log.info(message);
                        break;
                }
            } else if (evt.isEndElement() && getElementType(evt) == ELEMENT_ALIGNED_FEATURES) doneReading = true;
        }
        if (numAlignedFeatures != expectedNumAlignedFeatures) {
            String errorMessage = "Expected " + expectedNumAlignedFeatures + " aligned features, got " + numAlignedFeatures;
            if (validate) throw new XMLStreamException(errorMessage); else _log.info(errorMessage);
        }
    }

    /**
     * Process the peak_lists element
     * @param startElement
     * @throws XMLStreamException
     * @throws XmlException
     */
    protected void processPeakLists(StartElement startElement) throws XMLStreamException, XmlException {
        _log.debug("Processing peak lists...");
        int count = Integer.parseInt(getRequiredAttributeValue(startElement, "count"));
        _log.debug("\tcount: " + count);
        boolean doneReading = false;
        while (reader.hasNext() && !doneReading) {
            XMLEvent evt = reader.nextEvent();
            if (evt.isStartElement()) {
                if (getElementType(evt) == ELEMENT_PEAK_LIST) processPeakList((StartElement) evt); else {
                    String message = "Unknown element " + getElementName(evt) + ", expected peak_list";
                    if (validate) throw new XMLStreamException(message);
                    _log.info(message);
                }
            } else if (evt.isEndElement() && getElementType(evt) == ELEMENT_PEAK_LISTS) doneReading = true;
        }
    }

    /**
     * Process the peak_list element, which contains a list of features
     * @param peakListStartElement
     * @throws XMLStreamException
     * @throws XmlException
     */
    protected void processPeakList(StartElement peakListStartElement) throws XMLStreamException, XmlException {
        _log.debug("Processing peak list");
        String sourceURI = getRequiredAttributeValue(peakListStartElement, "source");
        _log.debug("Source file: " + sourceURI);
        XMLEvent evt = reader.nextEvent();
        while (!evt.isStartElement()) evt = reader.nextEvent();
        if (getElementType(evt) != ELEMENT_FEATURES) throw new XMLStreamException("Unknown element " + evt + ", expected features");
        StartElement featuresStartElement = (StartElement) evt;
        int count = Integer.parseInt(getRequiredAttributeValue(featuresStartElement, "count"));
        _log.debug("feature count: " + count);
        PeakListListener peakListListener = readerListener.createPeakListListener(count, sourceURI);
        boolean doneReading = false;
        int numFeatures = 0;
        while (reader.hasNext() && !doneReading) {
            evt = reader.nextEvent();
            if (evt.isStartElement()) {
                if (getElementType(evt) == ELEMENT_FEATURE) {
                    processFeatureXML(this.collectElementXmlAsFragment((StartElement) evt), peakListListener);
                    numFeatures++;
                } else throw new XMLStreamException("Unknown element " + getElementType(evt) + ", expected feature");
            } else if (evt.isEndElement() && getElementType(evt) == ELEMENT_FEATURES) doneReading = true;
        }
        if (numFeatures != count) {
            String logMessage = "Error: " + count + " features were declared, but " + numFeatures + " were found";
            if (validate) throw new XMLStreamException(logMessage); else _log.info(logMessage);
        }
        evt = reader.nextEvent();
        while (!evt.isEndElement()) evt = reader.nextEvent();
        if (getElementType(evt) != ELEMENT_PEAK_LIST) {
            String message = "Unexpected element " + getElementType(evt) + ", expected /peak_list";
            if (validate) throw new XmlException(message);
            _log.info(message);
        }
    }

    /**
     * Process the ClusterProfile element.  Note: this method reports an entire cluster's references at once.
     * All those will be held in memory at the same time.
     * @param startElement
     * @throws XMLStreamException
     * @throws XmlException
     */
    protected void processClusterProfile(StartElement startElement) throws XMLStreamException, XmlException {
        _log.debug("Processing clusterProfile...");
        boolean doneReading = false;
        while (reader.hasNext() && !doneReading) {
            XMLEvent evt = reader.nextEvent();
            if (evt.isStartElement()) {
                if (getElementType(evt) == ELEMENT_CLUSTERS) {
                    StartElement clustersStartElement = (StartElement) evt;
                    int id = Integer.parseInt(clustersStartElement.getAttributeByName(new QName("id")).getValue());
                    Attribute descAttr = clustersStartElement.getAttributeByName(new QName("description"));
                    String description = null;
                    if (descAttr != null) description = descAttr.getValue();
                    _log.debug("Creating ClustersListener");
                    ClustersListener clustersListener = readerListener.createClustersListener(id, description);
                    boolean doneReading2 = false;
                    while (reader.hasNext() && !doneReading2) {
                        evt = reader.nextEvent();
                        if (evt.isStartElement() && getElementType(evt) == ELEMENT_CLUSTER) {
                            String clusterXml = collectElementXmlAsFragment((StartElement) evt);
                            {
                                ClustersType xbClusters = ClustersType.Factory.parse(clusterXml);
                                ClusterType xbCluster = xbClusters.getClusterArray(0);
                                Cluster cluster = new Cluster(xbCluster.getId().intValue(), xbCluster.getClassification(), xbCluster.getRefElement());
                                _log.debug("Reporting cluster, ID=" + cluster.getId());
                                for (ClusterType.Reference xbRef : xbCluster.getReferenceArray()) cluster.addFeatureId(xbRef.getRefElementId().intValue());
                                clustersListener.reportCluster(cluster);
                            }
                        } else if (evt.isEndElement() && getElementType(evt) == ELEMENT_CLUSTERS) doneReading2 = true;
                    }
                } else {
                    String message = "Unknown element " + getElementType(evt) + ", expected clusters";
                    if (validate) throw new XMLStreamException(message);
                    _log.info(message);
                }
            } else if (evt.isEndElement() && getElementType(evt) == ELEMENT_CLUSTER_PROFILE) doneReading = true;
        }
    }

    /**
     * Helper method to throw a decent error if a required att is missing
     * @param startElement
     * @param attrName
     * @return
     * @throws XMLStreamException
     */
    protected String getRequiredAttributeValue(StartElement startElement, String attrName) throws XMLStreamException {
        try {
            return startElement.getAttributeByName(new QName(attrName)).getValue();
        } catch (Exception e) {
            throw new XMLStreamException("Missing required attribute " + attrName + " for element " + getElementName(startElement));
        }
    }

    /**
     * Turns an XmlBeans Ppid into an APML in-memory PutativePeptideId
     * @param xbPpid
     * @return
     */
    public static PutativePeptideId processXmlBeansPpid(PpidCollectionType.Ppid xbPpid) {
        PutativePeptideId ppid = new PutativePeptideId(xbPpid.getPeptideSequence());
        ppid.setMs2ScanNum(xbPpid.getMs2ScanNum());
        ppid.setPrecursorMZ(xbPpid.getPrecursorMz());
        ppid.setTheoreticalMz(xbPpid.getTheoreticalMz());
        BigInteger charge = xbPpid.getCharge();
        if (charge != null) ppid.setCharge(charge.intValue());
        ppid.setPreviousAA(xbPpid.getPreviousAa());
        ppid.setNextAA(xbPpid.getNextAa());
        ppid.setDta(xbPpid.getDta());
        PpidCollectionType.Ppid.Ms1QualityScore[] xmlBeansMs1SSarray = xbPpid.getMs1QualityScoreArray();
        if (xmlBeansMs1SSarray != null && xmlBeansMs1SSarray.length > 0) for (PpidCollectionType.Ppid.Ms1QualityScore xmlBeansMs1SS : xmlBeansMs1SSarray) {
            PutativePeptideId.SearchScore searchScore = new PutativePeptideId.SearchScore(xmlBeansMs1SS.getName(), xmlBeansMs1SS.getValue().getStringValue());
            if (xmlBeansMs1SS.getType() != null) searchScore.setScoreType(xmlBeansMs1SS.getType().getStringValue());
            ppid.addMs1QualityScore(searchScore);
        }
        PpidCollectionType.Ppid.Ms2SearchScore[] xmlBeansMs2SSarray = xbPpid.getMs2SearchScoreArray();
        if (xmlBeansMs2SSarray != null && xmlBeansMs2SSarray.length > 0) for (PpidCollectionType.Ppid.Ms2SearchScore xmlBeansMs2SS : xmlBeansMs2SSarray) {
            PutativePeptideId.SearchScore searchScore = new PutativePeptideId.SearchScore(xmlBeansMs2SS.getName(), xmlBeansMs2SS.getValue().getStringValue());
            if (xmlBeansMs2SS.getType() != null) searchScore.setScoreType(xmlBeansMs2SS.getType().getStringValue());
            ppid.addMs2SearchScore(searchScore);
        }
        PpidCollectionType.Ppid.Proteins xmlBeansProteins = xbPpid.getProteins();
        if (xmlBeansProteins != null) {
            PpidCollectionType.Ppid.Proteins.Protein[] xmlBeansProteinArray = xmlBeansProteins.getProteinArray();
            if (xmlBeansProteinArray != null && xmlBeansProteinArray.length > 0) for (PpidCollectionType.Ppid.Proteins.Protein xmlBeansProtein : xmlBeansProteinArray) ppid.addProteinAccessionNumber(xmlBeansProtein.getAccessionNum());
        }
        PpidCollectionType.Ppid.Modifications xbModifications = xbPpid.getModifications();
        if (xbModifications != null) {
            PpidCollectionType.Ppid.Modifications.Modification[] xbModArray = xbModifications.getModificationArray();
            if (xbModArray != null && xbModArray.length > 0) for (PpidCollectionType.Ppid.Modifications.Modification xbMod : xbModArray) {
                Modification mod = new Modification(xbMod.getPosition().intValue(), xbMod.getValue());
                ppid.addModification(mod);
            }
        }
        return ppid;
    }

    /**
     * Turn an XmlBeans FeatureType into a Feature
     * @param xbFeature
     * @return
     * @throws XMLStreamException
     */
    protected Feature processXmlBeansFeature(FeatureType xbFeature) throws XMLStreamException {
        Feature feature = new Feature();
        feature.setId((xbFeature.getId().intValue()));
        feature.setNumPeaks(xbFeature.getNumPeaks());
        if (xbFeature.getAnnotation() != null) feature.setAnnotation(xbFeature.getAnnotation());
        CoordinateType xmlBeansCoord = xbFeature.getCoordinate();
        Coordinate coord = processXmlBeansCoordinate(xmlBeansCoord);
        feature.setCoord(coord);
        FeatureType.QualityScore[] xmlBeansQualityScores = xbFeature.getQualityScoreArray();
        if (xmlBeansQualityScores != null && xmlBeansQualityScores.length > 0) {
            for (FeatureType.QualityScore xmlBeansQS : xmlBeansQualityScores) {
                Feature.QualityScore qualityScore = new Feature.QualityScore(xmlBeansQS.getScoreName(), xmlBeansQS.getScoreValue().getStringValue());
                if (xmlBeansQS.getScoreType() != null) qualityScore.setScoreType(xmlBeansQS.getScoreType().toString());
                feature.addQualityScore(qualityScore);
            }
        }
        FeatureType.Ppids xbPpids = xbFeature.getPpids();
        if (xbPpids != null) {
            int expectedNumPpids = xbPpids.getCount().intValue();
            PpidCollectionType.Ppid[] xmlBeansPpids = xbPpids.getPpidArray();
            int actualNumPpids = 0;
            if (xmlBeansPpids != null && xmlBeansPpids.length > 0) {
                for (PpidCollectionType.Ppid xbPpid : xmlBeansPpids) {
                    actualNumPpids++;
                    PutativePeptideId ppid = APMLReader.processXmlBeansPpid(xbPpid);
                    feature.addPpid(ppid);
                }
            }
            if (expectedNumPpids != actualNumPpids) {
                String logMessage = "Error: " + expectedNumPpids + " Ppids were declared, but " + actualNumPpids + " were found";
                if (validate) throw new XMLStreamException(logMessage); else _log.info(logMessage);
            }
        }
        FeatureType.MultiScanPeak[] xbMultiScanPeaks = xbFeature.getMultiScanPeakArray();
        if (xbMultiScanPeaks != null && xbMultiScanPeaks.length > 0) {
            for (FeatureType.MultiScanPeak xbMultiScanPeak : xbMultiScanPeaks) {
                int peakOffset = xbMultiScanPeak.getPeakOffset().intValue();
                Coordinate coordinate = processXmlBeansCoordinate(xbMultiScanPeak.getCoordinate());
                MultiScanPeak multiScanPeak = new MultiScanPeak(coordinate, peakOffset);
                if (readSingleScanPeaks) {
                    FeatureType.MultiScanPeak.SingleScanPeak[] xbSSPs = xbMultiScanPeak.getSingleScanPeakArray();
                    if (xbSSPs != null && xbSSPs.length > 0) {
                        for (FeatureType.MultiScanPeak.SingleScanPeak xbSSP : xbSSPs) {
                            MultiScanPeak.SingleScanPeak ssp = new MultiScanPeak.SingleScanPeak(xbSSP.getMz(), xbSSP.getRt(), xbSSP.getScan().intValue(), xbSSP.getIntensity());
                            multiScanPeak.addSingleScanPeak(ssp);
                        }
                    }
                }
                feature.addMultiScanPeak(multiScanPeak);
            }
        }
        return feature;
    }

    /**
     * Process the feature element
     * @param featureXml
     * @param peakListListener
     * @throws XMLStreamException
     * @throws XmlException
     */
    protected void processFeatureXML(String featureXml, PeakListListener peakListListener) throws XMLStreamException, XmlException {
        FeaturesType xmlBeansFeatures = FeaturesType.Factory.parse(featureXml, xmlOptions);
        if (validate) {
            if (!xmlBeansFeatures.validate(xmlOptions)) {
                if (validationErrors.size() == 1 && validationErrors.get(0).toString().contains("count")) validationErrors.remove(0); else {
                    String failureMessage = "Failed to parse XML string: " + xmlBeansFeatures;
                    if (validationErrors.isEmpty()) failureMessage = failureMessage + "\nNo errors to report"; else {
                        for (Object vError : validationErrors) {
                            failureMessage = failureMessage + "\nError: " + vError;
                        }
                    }
                    throw new XmlException(failureMessage);
                }
            }
        }
        FeatureType xmlBeansFeature = xmlBeansFeatures.getFeatureArray(0);
        Feature feature = processXmlBeansFeature(xmlBeansFeature);
        _log.debug(feature);
        peakListListener.reportFeature(feature);
    }

    /**
     * Turns an XmlBeans CoordinateType into an APML in-memory Coordinate
     * @param xbCoord
     * @return
     */
    protected Coordinate processXmlBeansCoordinate(CoordinateType xbCoord) {
        Coordinate coord = new Coordinate();
        coord.setMz((float) xbCoord.getMz());
        coord.setRt(xbCoord.getRt());
        coord.setIntensity(xbCoord.getIntensity());
        coord.setMass((float) xbCoord.getMass());
        coord.setApexIntensity(xbCoord.getApexIntensity());
        BigInteger apexScanBigInt = xbCoord.getApexScan();
        if (apexScanBigInt != null) coord.setApexScan(apexScanBigInt.intValue());
        coord.setBackgroundEstimate(xbCoord.getBackgroundEstimate());
        coord.setMedianEstimate(xbCoord.getMedianEstimate());
        coord.setCharge(xbCoord.getCharge());
        CoordinateType.ScanRange xbScanRange = xbCoord.getScanRange();
        if (xbScanRange != null) {
            coord.setScanRange(new Coordinate.Range<Integer>(xbScanRange.getMin().intValue(), xbScanRange.getMax().intValue()));
            if (xbScanRange.getCount() != null) coord.setScanCount(xbScanRange.getCount().intValue());
        }
        CoordinateType.TimeRange xbTimeRange = xbCoord.getTimeRange();
        if (xbTimeRange != null) coord.setRtRange(new Coordinate.Range<Float>(xbTimeRange.getMin(), xbTimeRange.getMax()));
        CoordinateType.MzRange xbMzRange = xbCoord.getMzRange();
        if (xbMzRange != null) coord.setMzRange(new Coordinate.Range<Float>((float) xbMzRange.getMin(), (float) xbMzRange.getMax()));
        return coord;
    }

    /**
     * Adds xml-fragment tags around the output of collectElementXml
     * @param startElement
     * @return
     * @throws XMLStreamException
     */
    protected String collectElementXmlAsFragment(StartElement startElement) throws XMLStreamException {
        return "<xml-fragment>" + collectElementXml(startElement) + "</xml-fragment>";
    }

    /**
     * Given a start element, collect all the XML for that element and its children.
     * Throws an XMLStreamException if there's an underlying exception in parsing XML,
     * or if the XML exceeds the number of bytes allowed in buffer.
     *
     * @param startElement
     * @return
     * @throws XMLStreamException
     */
    protected String collectElementXml(StartElement startElement) throws XMLStreamException {
        XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
        outFactory.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);
        XMLEventWriter xmlEventWriter = null;
        xmlEventWriter = outFactory.createXMLEventWriter(swriter);
        xmlEventWriter.add(startElement);
        boolean doneReading = false;
        String startElementName = getElementName(startElement);
        while (reader.hasNext() && !doneReading) {
            XMLEvent evt = reader.nextEvent();
            if (evt.isEndElement() && startElementName.equals(getElementName(evt))) doneReading = true;
            xmlEventWriter.add(evt);
        }
        xmlEventWriter.flush();
        xmlEventWriter.close();
        return swriter.reset();
    }

    /**
     * If validate flag is true, then check if the object validated.  If it didn't, throw an XmlException
     * with all relevant information
     * @param beanObject
     * @throws XmlException
     */
    protected void conditionalValidate(XmlObject beanObject) throws XmlException {
        if (!validate) return;
        if (!beanObject.validate(xmlOptions)) {
            reportValidationErrors(beanObject);
        }
    }

    /**
     * Report any errors in the validationErrors list
     * @param beanObject
     * @throws XmlException
     */
    protected void reportValidationErrors(XmlObject beanObject) throws XmlException {
        String failureMessage = "Failed to parse XML string: " + beanObject;
        if (validationErrors.isEmpty()) failureMessage = failureMessage + "\nNo errors to report"; else {
            for (Object vError : validationErrors) {
                failureMessage = failureMessage + "\nError: " + vError;
            }
        }
        throw new XmlException(failureMessage);
    }

    /**
     * Get the name of an event, assuming it's a start or end element.  If it's not, don't call this method
     * @param event
     * @return
     * @throws XMLStreamException
     */
    protected String getElementName(XMLEvent event) throws XMLStreamException {
        String elementName = null;
        if (event.isStartElement()) {
            StartElement elem = (StartElement) event;
            elementName = elem.getName().getLocalPart();
        } else if (event.isEndElement()) {
            EndElement elem = (EndElement) event;
            elementName = elem.getName().getLocalPart();
        } else throw new XMLStreamException("Non-start-or-end element passed to getElementType.  Element: " + event);
        return elementName;
    }

    /**
     * Get the integer type of an event, assuming it's a start or end element.  If it's not, don't call this method,
     * it'll throw an error
     * @param event
     * @return
     * @throws XMLStreamException
     */
    protected int getElementType(XMLEvent event) throws XMLStreamException {
        try {
            return getElementType(getElementName(event));
        } catch (IllegalArgumentException e) {
            throw new XMLStreamException(e);
        }
    }

    /**
     * Look up the integer element type for an element name.  return ELEMENT_UNKNOWN if not found.
     * If the element name map is not initialized, initialize it
     * @param elementName
     * @return
     * @throws IllegalArgumentException
     */
    protected int getElementType(String elementName) throws IllegalArgumentException {
        if (_elementNameIDMap == null) {
            _elementNameIDMap = new HashMap<String, Integer>();
            for (int i = 0; i < ELEMENT_NAMES.length; i++) {
                _elementNameIDMap.put(ELEMENT_NAMES[i], i);
            }
        }
        Integer result = _elementNameIDMap.get(elementName);
        if (result == null) return ELEMENT_UNKNOWN;
        return result;
    }

    /**
     * A Writer that buffers the data in memory as a string. The buffer can be
     * reset at any time and the contents of the buffer will be returned.
     *
     * This code is cribbed from Michael Pilone's freely available implementation at:
     * http://www.devx.com/xml/Article/34037/1954
     */
    private static class ResettableStringWriter extends Writer {

        /**
         * The internal buffer used to store the data written to this stream.
         */
        private StringBuilder mBuffer = new StringBuilder();

        /**
         * The maximum bytes this writer will buffer.
         */
        private int mMaxBytes = 1024;

        /**
         * Constructs the writer which will hold a fixed maximum of bytes. If more
         * bytes are written, an exception will be raised.
         *
         * @param maxBytes
         *          the maximum number of bytes this writer will buffer
         */
        public ResettableStringWriter(int maxBytes) {
            mMaxBytes = maxBytes;
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            mBuffer.append(cbuf, off, len);
            if (mBuffer.length() > mMaxBytes) {
                throw new IOException("Memory buffer overflow");
            }
        }

        /**
         * Resets the stream to a length of zero. Any data written to the stream
         * since the last call to reset will be returned.
         *
         * @return the contents of the stream buffer
         */
        public String reset() {
            String data = mBuffer.toString();
            mBuffer.setLength(0);
            return data;
        }
    }

    public boolean isReadSingleScanPeaks() {
        return readSingleScanPeaks;
    }

    public void setReadSingleScanPeaks(boolean readSingleScanPeaks) {
        this.readSingleScanPeaks = readSingleScanPeaks;
    }

    public int getMaxBufferedBytes() {
        return maxBufferedBytes;
    }

    public void setMaxBufferedBytes(int maxBufferedBytes) {
        this.maxBufferedBytes = maxBufferedBytes;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }
}
