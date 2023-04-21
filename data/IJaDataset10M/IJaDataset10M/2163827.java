package org.xebra.scp.media.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xebra.dcm.util.Base64Codec;
import org.xebra.scp.db.exception.KeyNotFoundException;
import org.xebra.scp.db.exception.PersistException;
import org.xebra.scp.db.model.Instance;
import org.xebra.scp.db.model.Series;
import org.xebra.scp.db.model.Study;
import org.xebra.scp.db.model.query.InstanceQuery;
import org.xebra.scp.db.peer.AETitle;
import org.xebra.scp.db.peer.CMOVERequest;
import org.xebra.scp.db.peer.CMOVERequestLoader;
import org.xebra.scp.db.peer.ServiceClassProvider;
import org.xebra.scp.db.persist.Loader;
import org.xebra.scp.db.persist.LoaderFactory;
import org.xebra.scp.db.persist.Persister;
import org.xebra.scp.media.AbstractMediaGenerator;
import org.xebra.scp.tool.DCMException;
import org.xebra.scp.tool.DCMExec;
import org.xebra.scp.util.FileUtility;

/**
 * Generates the study description XML file.
 * 
 * @author Rafael Chargel
 * @version $Revision: 1.21 $
 */
class StudyXMLGenerator extends AbstractMediaGenerator {

    private static final long TIME = 90000;

    private static final long MAX_ATTEMPT = 100000;

    private static final Map<String, Long> TIME_MAP = new HashMap<String, Long>();

    private static final String STUDY_PRE = "study:";

    private static final String STUDY_URI = "org.xebra.xml:1.0";

    private static final String SERIES_PRE = "series:";

    private static final String SERIES_URI = "org.xebra.xml:series:1.0";

    private static final String INSTANCE_PRE = "img:";

    private static final String INSTANCE_URI = "org.xebra.xml:image:1.0";

    private static final String UID_PRE = "uid:";

    private static final String UID_URI = "org.xebra.xml:uid:1.0";

    private static final SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public String getDescription() {
        return "Study Descriptor Generator";
    }

    public boolean isStudyLevelGenerator() {
        return true;
    }

    public void addUID(String uid) {
        boolean alreadyIn = TIME_MAP.containsKey(uid);
        TIME_MAP.put(uid, System.currentTimeMillis());
        if (!alreadyIn) {
            super.addUID(uid);
        }
    }

    public void process(String uid) {
        try {
            waitForStudy(uid);
        } catch (KeyNotFoundException exc) {
            LOGGER.warn(exc.getMessage(), exc);
            return;
        }
        try {
            long time = System.currentTimeMillis();
            Study study = loadStudy(uid);
            LOGGER.info("Study '" + uid + "' for patient '" + study.getMrn() + "' is complete, creating XML Descriptor Document");
            Document doc = createDocument();
            LOGGER.debug("Creating document element");
            Element root = createStudyElement("RetrieveStudyResponse", doc);
            root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:study", STUDY_URI);
            root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:series", SERIES_URI);
            root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:img", INSTANCE_URI);
            root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:uid", UID_URI);
            LOGGER.debug("Loading Study from database");
            setStudy(study, root);
            LOGGER.debug("Study XML Document generation complete");
            File descriptorFile = FileUtility.getDescriptorFile(study.getPeer(), study);
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setAttribute("indent-number", 3);
            Transformer trans = factory.newTransformer();
            trans.setOutputProperty(OutputKeys.MEDIA_TYPE, "xml");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            trans.setOutputProperty(OutputKeys.MEDIA_TYPE, "text/xml");
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(descriptorFile)));
            trans.transform(new DOMSource(doc), new StreamResult(out));
            out.flush();
            out.close();
            LOGGER.debug("Wrote descriptor file: " + descriptorFile.getAbsolutePath());
            study.setStudyDescriptorLocation(descriptorFile.getAbsolutePath());
            if (study.getNumberOfStudyRelatedSeries() == null) {
                try {
                    study.setNumberOfStudyRelatedSeries(getSeriesCount(study.getUid()));
                } catch (Throwable t) {
                }
            }
            if (study.getNumberOfStudyRelatedInstances() == null) {
                try {
                    study.setNumberOfStudyRelatedInstances(getInstanceCount(study.getUid()));
                } catch (Throwable t) {
                }
            }
            Persister<Study> persister = LoaderFactory.getPersister(Study.class);
            persister.persist(study);
            LOGGER.debug("Study descriptor recorded in database: " + uid);
            time = System.currentTimeMillis() - time;
            LOGGER.info("Study descriptor written in " + (time / 1000.0) + " seconds");
            TIME_MAP.remove(uid);
        } catch (TransformerConfigurationException exc) {
            LOGGER.error("Error writing descriptor file", exc);
        } catch (TransformerException exc) {
            LOGGER.error("Error writing descriptor file", exc);
        } catch (IOException exc) {
            LOGGER.error("Error writing descriptor file", exc);
        } catch (KeyNotFoundException exc) {
            LOGGER.error("Could not find data in the requested study", exc);
        } catch (PersistException exc) {
            LOGGER.error("Error reading from or writing to the database", exc);
        } catch (ParserConfigurationException exc) {
            LOGGER.error("Unable to generate document", exc);
        } catch (Throwable t) {
            LOGGER.error("Unknown error occured", t);
        }
        try {
            CMOVERequestLoader loader = new CMOVERequestLoader();
            List<CMOVERequest> requests = loader.loadCMOVERequests(uid, CMOVERequest.Status.PENDING);
            if (requests != null && requests.size() > 0) {
                PendingProcessor proc = new PendingProcessor(requests);
                proc.start();
            }
        } catch (KeyNotFoundException exc) {
            LOGGER.debug("No pending requests");
        } catch (Throwable t) {
            LOGGER.error("Could not process pending requests for study: " + uid, t);
        }
    }

    private void setStudy(Study study, Element root) throws KeyNotFoundException, PersistException {
        LOGGER.debug("Setting patient data");
        Element patient = createStudyElement("PatientData", root);
        patient.setAttribute("mrn", study.getMrn());
        patient.setAttribute("sex", study.getSex());
        Element patientName = createStudyElement("PatientName", patient);
        patientName.setTextContent(study.getPatientName());
        if (study.getBirthDate() != null) {
            Element patientBDay = createStudyElement("BirthDay", patient);
            patientBDay.setTextContent(_dateFormat.format(study.getBirthDate()));
        }
        LOGGER.debug("Setting study data");
        Element studyData = createStudyElement("Study", root);
        Element studyInfo = createStudyElement("StudyInfo", studyData);
        Element studyUID = createUIDElement("StudyInstanceUID", studyInfo);
        studyUID.setAttribute("value", study.getUid());
        if (study.getAccessionNumber() != null) {
            Element element = createStudyElement("AccessionNumber", studyInfo);
            element.setTextContent(study.getAccessionNumber());
        }
        if (study.getStudyDate() != null) {
            Element element = createStudyElement("StudyDate", studyInfo);
            element.setTextContent(_dateFormat.format(study.getStudyDate()));
        }
        if (study.getInstitutionName() != null) {
            Element element = createStudyElement("InstitutionName", studyInfo);
            element.setTextContent(study.getInstitutionName());
        }
        if (study.getInstitutionAddress() != null) {
            Element element = createStudyElement("InstitutionAddress", studyInfo);
            element.setTextContent(study.getInstitutionAddress());
        }
        if (study.getNormalizedInstitutionName() != null) {
            Element element = createStudyElement("NormalizedInstitutionName", studyInfo);
            element.setTextContent(study.getNormalizedInstitutionName());
        }
        if (study.getReferringPhysicianName() != null) {
            Element element = createStudyElement("ReferringPhysician", studyInfo);
            element.setTextContent(study.getReferringPhysicianName());
        }
        if (study.getReadingPhysicianName() != null) {
            Element element = createStudyElement("ReadingPhysician", studyInfo);
            element.setTextContent(study.getReadingPhysicianName());
        }
        if (study.getModalitiesInStudy() != null) {
            Element element = createStudyElement("ModalitiesInStudy", studyInfo);
            element.setTextContent(study.getModalitiesInStudy());
        }
        if (study.getStudyDescription() != null) {
            Element element = createStudyElement("StudyDescription", studyInfo);
            element.setTextContent(study.getStudyDescription());
        }
        Loader<Series> loader = LoaderFactory.getLoader(Series.class);
        List<Series> seriesList = loader.loadByParentUID(study.getUid());
        Collections.sort(seriesList);
        for (Series series : seriesList) {
            setSeries(series, studyData);
        }
        studyData.setAttribute("seriesInStudy", Integer.toString(seriesList.size()));
    }

    private void setSeries(Series series, Element root) throws KeyNotFoundException, PersistException {
        LOGGER.debug("Setting series data");
        Element seriesData = createSeriesElement("Series", root);
        Element seriesInfo = createSeriesElement("SeriesInfo", seriesData);
        Element seriesUID = createUIDElement("SeriesInstanceUID", seriesInfo);
        seriesUID.setAttribute("value", series.getUid());
        if (series.getFrameOfReferenceUid() != null) {
            Element element = createUIDElement("FrameOfReferenceUID", seriesInfo);
            element.setAttribute("value", series.getFrameOfReferenceUid());
        }
        if (series.getSeriesNumber() != null) {
            Element element = createSeriesElement("SeriesNumber", seriesInfo);
            element.setTextContent(series.getSeriesNumber().toString());
        }
        if (series.getSeriesDate() != null) {
            Element element = createSeriesElement("SeriesDate", seriesInfo);
            element.setTextContent(_dateFormat.format(series.getSeriesDate()));
        }
        if (series.getModality() != null) {
            Element element = createSeriesElement("Modality", seriesInfo);
            element.setTextContent(series.getModality());
        }
        if (series.getManufacturerModelName() != null) {
            Element element = createSeriesElement("ManufacturerModelName", seriesInfo);
            element.setTextContent(series.getManufacturerModelName());
        }
        if (series.getStationName() != null) {
            Element element = createSeriesElement("StationName", seriesInfo);
            element.setTextContent(series.getStationName());
        }
        if (series.getDepartmentName() != null) {
            Element element = createSeriesElement("InstitutionalDepartmentName", seriesInfo);
            element.setTextContent(series.getDepartmentName());
        }
        if (series.getPatientPosition() != null) {
            Element element = createSeriesElement("PatientPosition", seriesInfo);
            element.setTextContent(series.getPatientPosition());
        }
        if (series.getPatientOrientation() != null) {
            Element element = createSeriesElement("PatientOrientation", seriesInfo);
            element.setTextContent(series.getPatientOrientation());
        }
        if (series.getBodyPart() != null) {
            Element element = createSeriesElement("BodyPartExamined", seriesInfo);
            element.setTextContent(series.getBodyPart());
        }
        if (series.getKvp() != null) {
            Element element = createSeriesElement("KVP", seriesInfo);
            element.setTextContent(series.getKvp());
        }
        if (series.getExposure() != null) {
            Element element = createSeriesElement("Exposure", seriesInfo);
            element.setTextContent(series.getExposure().toString());
        }
        if (series.getPulseSequenceName() != null) {
            Element element = createSeriesElement("PulseSequenceName", seriesInfo);
            element.setTextContent(series.getPulseSequenceName());
        }
        if (series.getScanOptions() != null) {
            Element element = createSeriesElement("ScanOptions", seriesInfo);
            element.setTextContent(series.getScanOptions());
        }
        if (series.getLaterality() != null) {
            Element element = createSeriesElement("Laterality", seriesInfo);
            element.setTextContent(series.getLaterality());
        }
        if (series.getProtocolName() != null) {
            Element element = createSeriesElement("ProtocolName", seriesInfo);
            element.setTextContent(series.getProtocolName());
        }
        if (series.getPlateType() != null) {
            Element element = createSeriesElement("PlateType", seriesInfo);
            element.setTextContent(series.getPlateType());
        }
        if (series.getPhosphorType() != null) {
            Element element = createSeriesElement("PhosphorType", seriesInfo);
            element.setTextContent(series.getPhosphorType());
        }
        if (series.getCassetteOrientation() != null) {
            Element element = createSeriesElement("CassetteOrientation", seriesInfo);
            element.setTextContent(series.getCassetteOrientation());
        }
        if (series.getCassetteSize() != null) {
            Element element = createSeriesElement("CassetteSize", seriesInfo);
            element.setTextContent(series.getCassetteSize());
        }
        if (series.getSensitivity() != null) {
            Element element = createSeriesElement("Sensitivity", seriesInfo);
            element.setTextContent(series.getSensitivity());
        }
        if (series.getContrastBolusAgent() != null) {
            Element element = createSeriesElement("ContrastBolusAgent", seriesInfo);
            element.setTextContent(series.getContrastBolusAgent());
        }
        if (series.getLastCalibration() != null) {
            Element element = createSeriesElement("LastCalibrationDate", seriesInfo);
            element.setTextContent(_dateFormat.format(series.getLastCalibration()));
        }
        if (series.getSeriesDescription() != null) {
            Element element = createSeriesElement("SeriesDescription", seriesInfo);
            element.setTextContent(series.getSeriesDescription());
        }
        Loader<Instance> loader = LoaderFactory.getLoader(Instance.class);
        List<Instance> instances = loader.loadByParentUID(series.getUid());
        Collections.sort(instances);
        int imgNum = 1;
        for (Instance instance : instances) {
            setInstance(instance, series.getModality(), seriesData, imgNum);
            imgNum++;
        }
        seriesData.setAttribute("instancesInSeries", Integer.toString(instances.size()));
    }

    private void setInstance(Instance instance, String modality, Element root, int imgNum) throws PersistException {
        LOGGER.debug("Setting instance data");
        Element image = createInstanceElement("Image", root);
        image.setAttribute("aspectRatio", Double.toString((double) instance.getMatrixColumns() / (double) instance.getMatrixRows()));
        image.setAttribute("width", Integer.toString(instance.getMatrixColumns()));
        image.setAttribute("height", Integer.toString(instance.getMatrixRows()));
        image.setAttribute("imageNumber", Integer.toString(imgNum));
        Element uid = createUIDElement("SOPInstanceUID", image);
        uid.setAttribute("value", instance.getSOPInstanceUid());
        Element classUID = createUIDElement("SOPClassUID", image);
        classUID.setAttribute("value", instance.getSopClassUid());
        if (instance.getImageDate() != null) {
            Element element = createInstanceElement("ImageDate", image);
            element.setTextContent(_dateFormat.format(instance.getImageDate()));
        }
        if (instance.getImageType() != null) {
            Element element = createInstanceElement("ImageTypeName", image);
            element.setTextContent(instance.getImageType());
        }
        LOGGER.debug("Setting window settings data");
        Element winSettings = createInstanceElement("WindowSettings", image);
        Element winVals = createInstanceElement("WindowValues", winSettings);
        String window = instance.getWindowWidth();
        String level = instance.getWindowCenter();
        String[] wws = null;
        String[] wcs = null;
        if (window != null && level != null) {
            wws = window.split("[^\\d\\.]+");
            wcs = level.split("[^\\d\\.]+");
        } else {
            wcs = new String[] { "256" };
            wws = new String[] { "512" };
        }
        LOGGER.debug("Setting window/level values data");
        for (int i = 0; i < wws.length && i < wcs.length; i++) {
            Element element = createInstanceElement("WindowLevel", winVals);
            if (wws[i] == null || wws[i].equals("")) {
                wws[i] = "512";
            }
            if (wcs[i] == null || wcs[i].equals("")) {
                wcs[i] = "256";
            }
            element.setAttribute("window", wws[i]);
            element.setAttribute("level", wcs[i]);
        }
        LOGGER.debug("Setting window/level values data complete");
        Element rescaleInter = createInstanceElement("RescaleIntercept", winSettings);
        Element rescaleSlope = createInstanceElement("RescaleSlope", winSettings);
        Element padding = createInstanceElement("Padding", winSettings);
        Element signed = createInstanceElement("Signed", winSettings);
        Element inverted = createInstanceElement("Inverted", winSettings);
        Element smallest = createInstanceElement("SmallestImagePixelValue", winSettings);
        Element largest = createInstanceElement("LargestImagePixelValue", winSettings);
        LOGGER.debug("Setting rescale intercept/slope values");
        rescaleInter.setAttribute("value", instance.getRescaleIntercept() == null ? "0.0" : instance.getRescaleIntercept());
        rescaleSlope.setAttribute("value", instance.getRescaleSlope() == null ? "1.0" : instance.getRescaleSlope());
        LOGGER.debug("Setting rescale intercept/slope values complete");
        LOGGER.debug("Setting signed/inverted values");
        boolean signedVal = instance.getPixelRepresentation() != null && instance.getPixelRepresentation().intValue() == 1;
        signed.setAttribute("value", Boolean.toString(signedVal));
        inverted.setAttribute("value", Boolean.toString("MONOCHROME1".equals(instance.getPhotometricInterpretation())));
        LOGGER.debug("Setting signed/inverted values complete");
        LOGGER.debug("Setting padding required");
        padding.setAttribute("required", Boolean.toString(instance.isPadded()));
        if (instance.getPadding() != null) {
            LOGGER.debug("Setting padding value");
            padding.setAttribute("value", instance.getPadding().toString());
        }
        LOGGER.debug("Setting padding values complete");
        LOGGER.debug("Setting smallest/largest pixel values");
        if (instance.getSmallestImagePixelValue() != null) {
            smallest.setAttribute("value", instance.getSmallestImagePixelValue().toString());
        }
        if (instance.getLargestImagePixelValue() != null) {
            largest.setAttribute("value", instance.getLargestImagePixelValue().toString());
        }
        LOGGER.debug("Setting smallest/largest pixel values complete");
        LOGGER.debug("Setting window settings data complete");
        if (instance.getEchoTime() != null) {
            Element element = createInstanceElement("EchoTime", image);
            element.setTextContent(instance.getEchoTime());
        }
        if (instance.getEchoTrainLength() != null) {
            Element element = createInstanceElement("EchoTrainLength", image);
            element.setTextContent(instance.getEchoTrainLength().toString());
        }
        if (instance.getRepetitionTime() != null) {
            Element element = createInstanceElement("RepetitionTime", image);
            element.setTextContent(instance.getRepetitionTime());
        }
        if (instance.getInversionTime() != null) {
            Element element = createInstanceElement("InversionTime", image);
            element.setTextContent(instance.getInversionTime());
        }
        if (instance.getTriggerTime() != null) {
            Element element = createInstanceElement("TriggerTime", image);
            element.setTextContent(instance.getTriggerTime());
        }
        if (instance.getFrameTime() != null) {
            Element element = createInstanceElement("FrameTime", image);
            element.setTextContent(instance.getFrameTime());
        }
        if (instance.getFrameTimeVector() != null) {
            Element element = createInstanceElement("FrameTimeVector", image);
            element.setTextContent(instance.getFrameTimeVector());
        }
        if (instance.getActualFrameDuration() != null) {
            Element element = createInstanceElement("ActualFrameDuration", image);
            element.setTextContent(instance.getActualFrameDuration().toString());
        }
        if (instance.getScanningSequence() != null) {
            Element element = createInstanceElement("ScanningSequence", image);
            element.setTextContent(instance.getScanningSequence());
        }
        if (instance.getSequenceVariant() != null) {
            Element element = createInstanceElement("SequenceVariant", image);
            element.setTextContent(instance.getSequenceVariant());
        }
        if (instance.getMagneticFieldStrength() != null) {
            Element element = createInstanceElement("MagneticFieldStrength", image);
            element.setTextContent(instance.getMagneticFieldStrength());
        }
        if (instance.getPhotometricInterpretation() != null) {
            Element element = createInstanceElement("PhotometricInterpretation", image);
            element.setTextContent(instance.getPhotometricInterpretation());
        }
        if (instance.getPixelAspectRatio() != null) {
            Element element = createInstanceElement("PixelAspectRatio", image);
            element.setTextContent(instance.getPixelAspectRatio());
        }
        if (instance.getPixelSpacing() != null) {
            Element element = createInstanceElement("PixelSpacing", image);
            element.setTextContent(instance.getPixelSpacing());
        }
        if (instance.getPixelRepresentation() != null) {
            Element element = createInstanceElement("PixelRepresentation", image);
            element.setTextContent(instance.getPixelRepresentation().toString());
        }
        if (instance.getImagerPixelSpacing() != null) {
            Element element = createInstanceElement("ImagerPixelSpacing", image);
            element.setTextContent(instance.getImagerPixelSpacing());
        }
        if (instance.getGantryDetectorTilt() != null) {
            Element element = createInstanceElement("GantryDetectorTilt", image);
            element.setTextContent(instance.getGantryDetectorTilt());
        }
        if (instance.getTableHeight() != null) {
            Element element = createInstanceElement("TableHeight", image);
            element.setTextContent(instance.getTableHeight());
        }
        if (instance.getTableTraverse() != null) {
            Element element = createInstanceElement("TableTraverse", image);
            element.setTextContent(instance.getTableTraverse());
        }
        if (instance.getWholeBodyTechnique() != null) {
            Element element = createInstanceElement("WholeBodyTechnique", image);
            element.setTextContent(instance.getWholeBodyTechnique());
        }
        if (instance.getFilterType() != null) {
            Element element = createInstanceElement("FilterType", image);
            element.setTextContent(instance.getFilterType());
        }
        if (instance.getRotationDirection() != null) {
            Element element = createInstanceElement("RotationDirection", image);
            element.setTextContent(instance.getRotationDirection());
        }
        if (instance.getCountsAccumulated() != null) {
            Element element = createInstanceElement("CountsAccumulated", image);
            element.setTextContent(instance.getCountsAccumulated().toString());
        }
        if (instance.getScanLength() != null) {
            Element element = createInstanceElement("ScanLength", image);
            element.setTextContent(instance.getScanLength().toString());
        }
        if (instance.getScanVelocity() != null) {
            Element element = createInstanceElement("ScanVelocity", image);
            element.setTextContent(instance.getScanVelocity());
        }
        LOGGER.debug("Setting image settings data");
        Element imgSettings = createInstanceElement("ImageSettings", image);
        if (instance.getSliceThickness() != null) {
            Element element = createInstanceElement("SliceThickness", imgSettings);
            element.setTextContent(instance.getSliceThickness());
        }
        if (instance.getSliceLocation() != null) {
            Element element = createInstanceElement("SliceLocation", imgSettings);
            element.setTextContent(instance.getSliceLocation());
        }
        if (instance.getSpacingBetweenSlices() != null) {
            Element element = createInstanceElement("SpacingBetweenSlices", imgSettings);
            element.setTextContent(instance.getSpacingBetweenSlices());
        }
        if (instance.getImagePosition() != null) {
            Element element = createInstanceElement("ImagePosition", imgSettings);
            element.setTextContent(instance.getImagePosition());
        }
        if (instance.getImageOrientation() != null) {
            Element element = createInstanceElement("ImageOrientation", imgSettings);
            element.setTextContent(instance.getImageOrientation());
        }
        LOGGER.debug("Setting image settings data complete");
        try {
            byte[] thumb = thumbnail(instance.getThumbnailFileLocation());
            if (thumb != null && thumb.length > 0) {
                Element thumbnail = createInstanceElement("Thumbnail", image);
                thumbnail.setTextContent(Base64Codec.encode(thumb, Base64Codec.LF));
            }
        } catch (IOException exc) {
            LOGGER.error("Could not create thumbnail", exc);
            throw new PersistException("Could not load thumbnail", exc);
        }
    }

    private byte[] thumbnail(String fileName) throws IOException {
        LOGGER.debug("Getting thumbnail");
        if (fileName == null) return null;
        File file = new File(fileName);
        if (!file.exists()) return null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        byte[] buf = new byte[4096];
        int len = -1;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        out.flush();
        out.close();
        in.close();
        return out.toByteArray();
    }

    private Document createDocument() throws ParserConfigurationException {
        LOGGER.debug("Creating document factory");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        LOGGER.debug("Creating document builder");
        DocumentBuilder builder = factory.newDocumentBuilder();
        LOGGER.debug("Creating XML Document");
        return builder.newDocument();
    }

    private void waitForStudy(String uid) throws KeyNotFoundException {
        int attempt = 0;
        LOGGER.info("Waiting for Study Completion: " + uid);
        while (!isStudyComplete(uid)) {
            attempt++;
            if (attempt >= MAX_ATTEMPT) break;
            try {
                Thread.sleep(10000);
            } catch (InterruptedException exc) {
            }
        }
    }

    private boolean isStudyComplete(String uid) throws KeyNotFoundException {
        Study study = loadStudy(uid);
        if (study.getNumberOfStudyRelatedInstances() == null) {
            Long insert = TIME_MAP.get(uid);
            if (insert == null) throw new KeyNotFoundException("No study with UID: " + uid);
            long time = System.currentTimeMillis() - insert.longValue();
            LOGGER.debug("Time since dicovery: " + time);
            return time >= TIME;
        }
        int num = study.getNumberOfStudyRelatedInstances();
        try {
            int count = getInstanceCount(uid);
            return num <= count;
        } catch (Throwable t) {
            LOGGER.error("Error finding instance count", t);
        }
        return false;
    }

    private Element createStudyElement(String name, Node root) {
        Document doc = (root instanceof Document) ? (Document) root : root.getOwnerDocument();
        Element ele = doc.createElementNS(STUDY_URI, STUDY_PRE + name);
        root.appendChild(ele);
        return ele;
    }

    private Element createSeriesElement(String name, Node root) {
        Element ele = root.getOwnerDocument().createElementNS(SERIES_URI, SERIES_PRE + name);
        root.appendChild(ele);
        return ele;
    }

    private Element createInstanceElement(String name, Node root) {
        Element ele = root.getOwnerDocument().createElementNS(INSTANCE_URI, INSTANCE_PRE + name);
        root.appendChild(ele);
        return ele;
    }

    private Element createUIDElement(String name, Node root) {
        Element ele = root.getOwnerDocument().createElementNS(UID_URI, UID_PRE + name);
        root.appendChild(ele);
        return ele;
    }

    private int getSeriesCount(String studyUID) throws Exception {
        Loader<Series> loader = LoaderFactory.getLoader(Series.class);
        List<Series> series = loader.loadByParentUID(studyUID);
        return series.size();
    }

    private int getInstanceCount(String studyUID) throws Exception {
        Loader<Instance> instLoader = LoaderFactory.getLoader(Instance.class);
        InstanceQuery query = new InstanceQuery();
        query.setStudyUids(new String[] { studyUID });
        List<Instance> instances = instLoader.loadByQuery(query);
        return instances.size();
    }
}

class PendingProcessor extends Thread {

    private static final Logger _log = Logger.getLogger(PendingProcessor.class);

    private List<CMOVERequest> requests;

    public PendingProcessor(List<CMOVERequest> requests) {
        this.requests = requests;
    }

    public void run() {
        CMOVERequestLoader loader = new CMOVERequestLoader();
        ServiceClassProvider scp = null;
        try {
            scp = ServiceClassProvider.load();
        } catch (PersistException exc) {
            _log.fatal("Could not load the SCP service");
            return;
        }
        for (CMOVERequest request : this.requests) {
            DCMExec exec = new DCMExec(DCMExec.DEV_NULL);
            request.setFinalizedDate(new java.util.Date());
            try {
                exec.cmove(scp.getAETitle(), request.getMoveAE().getDefaultAETitle(AETitle.MOVE_AE).getAETitle(), request.getStudyUID(), request.getAnonMap());
                request.setStatus(CMOVERequest.Status.COMPLETED);
                request.setMessage("Study has been moved");
            } catch (DCMException exc) {
                _log.error("Could not send study '" + request.getStudyUID() + "' to " + request.getMoveAE().getLocalName(), exc);
                request.setMessage("Could not move study: " + exc.getMessage());
                request.setStatus(CMOVERequest.Status.FAILED);
            }
            try {
                loader.updateRequestStatus(request);
            } catch (PersistException exc) {
                _log.warn("Could not update request: " + request.getTransactionUID(), exc);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException exc) {
            }
        }
    }
}
