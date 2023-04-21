package org.dcm4chex.service;

import java.beans.PropertyEditor;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.management.ObjectName;
import javax.sql.DataSource;
import org.dcm4che.dict.UIDs;
import org.dcm4che.net.AcceptorPolicy;
import org.dcm4che.net.DcmServiceRegistry;
import org.dcm4cheri.util.StringUtils;
import org.dcm4chex.service.util.AETsEditor;
import org.dcm4chex.service.util.ConfigurationException;
import org.dcm4chex.archive.util.EJBHomeFactory;

/**
 * @jmx.mbean extends="org.jboss.system.ServiceMBean"
 * 
 * @author Gunter.Zeilinger@tiani.com
 * @version $Revision: 1025 $ $Date: 2004-03-02 18:55:32 -0500 (Tue, 02 Mar 2004) $
 * @since 03.08.2003
 */
public class StoreScpService extends AbstractScpService implements org.dcm4chex.service.StoreScpServiceMBean {

    private static final String[] STORAGE_AS = { UIDs.ComputedRadiographyImageStorage, UIDs.DigitalXRayImageStorageForPresentation, UIDs.DigitalXRayImageStorageForProcessing, UIDs.DigitalMammographyXRayImageStorageForPresentation, UIDs.DigitalMammographyXRayImageStorageForProcessing, UIDs.DigitalIntraoralXRayImageStorageForPresentation, UIDs.DigitalIntraoralXRayImageStorageForProcessing, UIDs.CTImageStorage, UIDs.UltrasoundMultiframeImageStorageRetired, UIDs.UltrasoundMultiframeImageStorage, UIDs.MRImageStorage, UIDs.EnhancedMRImageStorage, UIDs.MRSpectroscopyStorage, UIDs.NuclearMedicineImageStorageRetired, UIDs.UltrasoundImageStorageRetired, UIDs.UltrasoundImageStorage, UIDs.SecondaryCaptureImageStorage, UIDs.MultiframeSingleBitSecondaryCaptureImageStorage, UIDs.MultiframeGrayscaleByteSecondaryCaptureImageStorage, UIDs.MultiframeGrayscaleWordSecondaryCaptureImageStorage, UIDs.MultiframeColorSecondaryCaptureImageStorage, UIDs.HardcopyGrayscaleImageStorage, UIDs.HardcopyColorImageStorage, UIDs.StandaloneOverlayStorage, UIDs.StandaloneCurveStorage, UIDs.TwelveLeadECGWaveformStorage, UIDs.GeneralECGWaveformStorage, UIDs.AmbulatoryECGWaveformStorage, UIDs.HemodynamicWaveformStorage, UIDs.CardiacElectrophysiologyWaveformStorage, UIDs.BasicVoiceAudioWaveformStorage, UIDs.StandaloneModalityLUTStorage, UIDs.StandaloneVOILUTStorage, UIDs.GrayscaleSoftcopyPresentationStateStorage, UIDs.XRayAngiographicImageStorage, UIDs.XRayRadiofluoroscopicImageStorage, UIDs.XRayAngiographicBiPlaneImageStorageRetired, UIDs.NuclearMedicineImageStorage, UIDs.RawDataStorage, UIDs.VLImageStorageRetired, UIDs.VLMultiframeImageStorageRetired, UIDs.VLEndoscopicImageStorage, UIDs.VLMicroscopicImageStorage, UIDs.VLSlideCoordinatesMicroscopicImageStorage, UIDs.VLPhotographicImageStorage, UIDs.BasicTextSR, UIDs.EnhancedSR, UIDs.ComprehensiveSR, UIDs.MammographyCADSR, UIDs.KeyObjectSelectionDocument, UIDs.PositronEmissionTomographyImageStorage, UIDs.StandalonePETCurveStorage, UIDs.RTImageStorage, UIDs.RTDoseStorage, UIDs.RTStructureSetStorage, UIDs.RTBeamsTreatmentRecordStorage, UIDs.RTPlanStorage, UIDs.RTBrachyTreatmentRecordStorage, UIDs.RTTreatmentSummaryRecordStorage };

    private DataSourceFactory dsf = new DataSourceFactory(log);

    private int acTimeout = 5000;

    private String retrieveAETs;

    private Set retrieveAETSet;

    private String[] callingAETs;

    private String computedRadiographyImageStorage;

    private String digitalXRayImageStorageForPresentation;

    private String digitalXRayImageStorageForProcessing;

    private String digitalMammographyXRayImageStorageForPresentation;

    private String digitalMammographyXRayImageStorageForProcessing;

    private String digitalIntraoralXRayImageStorageForPresentation;

    private String digitalIntraoralXRayImageStorageForProcessing;

    private String ctImageStorage;

    private String ultrasoundMultiframeImageStorageRetired;

    private String ultrasoundMultiframeImageStorage;

    private String mrImageStorage;

    private String enhancedMRImageStorage;

    private String mrSpectroscopyStorage;

    private String nuclearMedicineImageStorageRetired;

    private String ultrasoundImageStorageRetired;

    private String ultrasoundImageStorage;

    private String secondaryCaptureImageStorage;

    private String multiframeSingleBitSecondaryCaptureImageStorage;

    private String multiframeGrayscaleByteSecondaryCaptureImageStorage;

    private String multiframeGrayscaleWordSecondaryCaptureImageStorage;

    private String multiframeColorSecondaryCaptureImageStorage;

    private String hardcopyGrayscaleImageStorage;

    private String hardcopyColorImageStorage;

    private String standaloneOverlayStorage;

    private String standaloneCurveStorage;

    private String twelveLeadECGWaveformStorage;

    private String generalECGWaveformStorage;

    private String ambulatoryECGWaveformStorage;

    private String hemodynamicWaveformStorage;

    private String cardiacElectrophysiologyWaveformStorage;

    private String basicVoiceAudioWaveformStorage;

    private String standaloneModalityLUTStorage;

    private String standaloneVOILUTStorage;

    private String grayscaleSoftcopyPresentationStateStorage;

    private String xRayAngiographicImageStorage;

    private String xRayRadiofluoroscopicImageStorage;

    private String xRayAngiographicBiPlaneImageStorageRetired;

    private String nuclearMedicineImageStorage;

    private String rawDataStorage;

    private String vlImageStorageRetired;

    private String vlMultiframeImageStorageRetired;

    private String vlEndoscopicImageStorage;

    private String vlMicroscopicImageStorage;

    private String vlSlideCoordinatesMicroscopicImageStorage;

    private String vlPhotographicImageStorage;

    private String basicTextSR;

    private String enhancedSR;

    private String comprehensiveSR;

    private String mammographyCADSR;

    private String keyObjectSelectionDocument;

    private String positronEmissionTomographyImageStorage;

    private String standalonePETCurveStorage;

    private String rtImageStorage;

    private String rtDoseStorage;

    private String rtStructureSetStorage;

    private String rtBeamsTreatmentRecordStorage;

    private String rtPlanStorage;

    private String rtTreatmentSummaryRecordStorage;

    private String storageCommitmentPushModel;

    private StoreScp scp = new StoreScp(this);

    private StgCmtScp stgCmtScp = new StgCmtScp(this);

    /**
     * @jmx.managed-attribute
     */
    public ObjectName getDcmServerName() {
        return dcmServerName;
    }

    /**
     * @jmx.managed-attribute
     */
    public void setDcmServerName(ObjectName dcmServerName) {
        this.dcmServerName = dcmServerName;
    }

    DataSource getDS() throws ConfigurationException {
        return dsf.getDataSource();
    }

    /**
     * @jmx.managed-attribute
     */
    public String getDataSource() {
        return dsf.getJNDIName();
    }

    /**
     * @jmx.managed-attribute
     */
    public void setDataSource(String jndiName) {
        dsf.setJNDIName(jndiName);
    }

    /**
     * @jmx.managed-attribute
     */
    public String getEjbProviderURL() {
        return EJBHomeFactory.getEjbProviderURL();
    }

    /**
     * @jmx.managed-attribute
     */
    public void setEjbProviderURL(String ejbProviderURL) {
        EJBHomeFactory.setEjbProviderURL(ejbProviderURL);
    }

    /**
     * @jmx.managed-attribute
     */
    public String getMaskWarningAsSuccessForCallingAETs() {
        return scp.getMaskWarningAsSuccessForCallingAETs();
    }

    /**
     * @jmx.managed-attribute
     */
    public void setMaskWarningAsSuccessForCallingAETs(String aets) {
        scp.setMaskWarningAsSuccessForCallingAETs(aets);
    }

    /**
     * @jmx.managed-attribute
     */
    public String getStorageDirs() {
        return scp.getStorageDirs();
    }

    /**
     * @jmx.managed-attribute
     */
    public void setStorageDirs(String dirs) throws IOException {
        scp.setStorageDirs(dirs);
    }

    /**
     * @jmx.managed-attribute
     */
    public String getForwardAETs() {
        return scp.getForwardAETs();
    }

    /**
     * @jmx.managed-attribute
     */
    public void setForwardAETs(String aets) {
        scp.setForwardAETs(aets);
    }

    /**
     * @jmx.managed-attribute
     */
    public int getForwardPriority() {
        return scp.getForwardPriority();
    }

    /**
     * @jmx.managed-attribute
     */
    public void setForwardPriority(int forwardPriority) {
        scp.setForwardPriority(forwardPriority);
    }

    /**
     * @jmx.managed-attribute
     */
    public final int getUpdateDatabaseMaxRetries() {
        return scp.getUpdateDatabaseMaxRetries();
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setUpdateDatabaseMaxRetries(int updateDatabaseMaxRetries) {
        scp.setUpdateDatabaseMaxRetries(updateDatabaseMaxRetries);
    }

    /**
     * @jmx.managed-attribute
     */
    public final int getAcTimeout() {
        return acTimeout;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setAcTimeout(int acTimeout) {
        this.acTimeout = acTimeout;
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getRetrieveAETs() {
        return retrieveAETs;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setRetrieveAETs(String aets) {
        if (aets == null) {
            throw new NullPointerException();
        }
        String[] array = StringUtils.split(aets, '\\');
        if (array.length == 0) {
            throw new IllegalArgumentException("aets=" + aets);
        }
        this.retrieveAETSet = Collections.unmodifiableSet(new HashSet(Arrays.asList(array)));
        this.retrieveAETs = aets;
    }

    final Set getRetrieveAETSet() {
        return retrieveAETSet;
    }

    /**
     * @jmx.managed-attribute
     */
    public String getCallingAETs() {
        PropertyEditor pe = new AETsEditor();
        pe.setValue(callingAETs);
        return pe.getAsText();
    }

    /**
     * @jmx.managed-attribute
     */
    public void setCallingAETs(String newCallingAETs) {
        PropertyEditor pe = new AETsEditor();
        pe.setAsText(newCallingAETs);
        callingAETs = (String[]) pe.getValue();
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getAmbulatoryECGWaveformStorage() {
        return ambulatoryECGWaveformStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setAmbulatoryECGWaveformStorage(String ambulatoryECGWaveformStorage) {
        this.ambulatoryECGWaveformStorage = ambulatoryECGWaveformStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getBasicTextSR() {
        return basicTextSR;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setBasicTextSR(String basicTextSR) {
        this.basicTextSR = basicTextSR;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getBasicVoiceAudioWaveformStorage() {
        return basicVoiceAudioWaveformStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setBasicVoiceAudioWaveformStorage(String basicVoiceAudioWaveformStorage) {
        this.basicVoiceAudioWaveformStorage = basicVoiceAudioWaveformStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getCardiacElectrophysiologyWaveformStorage() {
        return cardiacElectrophysiologyWaveformStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setCardiacElectrophysiologyWaveformStorage(String cardiacElectrophysiologyWaveformStorage) {
        this.cardiacElectrophysiologyWaveformStorage = cardiacElectrophysiologyWaveformStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getComprehensiveSR() {
        return comprehensiveSR;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setComprehensiveSR(String comprehensiveSR) {
        this.comprehensiveSR = comprehensiveSR;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getComputedRadiographyImageStorage() {
        return computedRadiographyImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setComputedRadiographyImageStorage(String computedRadiographyImageStorage) {
        this.computedRadiographyImageStorage = computedRadiographyImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getCtImageStorage() {
        return ctImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setCtImageStorage(String ctImageStorage) {
        this.ctImageStorage = ctImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getDigitalIntraoralXRayImageStorageForPresentation() {
        return digitalIntraoralXRayImageStorageForPresentation;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setDigitalIntraoralXRayImageStorageForPresentation(String digitalIntraoralXRayImageStorageForPresentation) {
        this.digitalIntraoralXRayImageStorageForPresentation = digitalIntraoralXRayImageStorageForPresentation;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getDigitalIntraoralXRayImageStorageForProcessing() {
        return digitalIntraoralXRayImageStorageForProcessing;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setDigitalIntraoralXRayImageStorageForProcessing(String digitalIntraoralXRayImageStorageForProcessing) {
        this.digitalIntraoralXRayImageStorageForProcessing = digitalIntraoralXRayImageStorageForProcessing;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getDigitalMammographyXRayImageStorageForPresentation() {
        return digitalMammographyXRayImageStorageForPresentation;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setDigitalMammographyXRayImageStorageForPresentation(String digitalMammographyXRayImageStorageForPresentation) {
        this.digitalMammographyXRayImageStorageForPresentation = digitalMammographyXRayImageStorageForPresentation;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getDigitalMammographyXRayImageStorageForProcessing() {
        return digitalMammographyXRayImageStorageForProcessing;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setDigitalMammographyXRayImageStorageForProcessing(String digitalMammographyXRayImageStorageForProcessing) {
        this.digitalMammographyXRayImageStorageForProcessing = digitalMammographyXRayImageStorageForProcessing;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getDigitalXRayImageStorageForPresentation() {
        return digitalXRayImageStorageForPresentation;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setDigitalXRayImageStorageForPresentation(String digitalXRayImageStorageForPresentation) {
        this.digitalXRayImageStorageForPresentation = digitalXRayImageStorageForPresentation;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getDigitalXRayImageStorageForProcessing() {
        return digitalXRayImageStorageForProcessing;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setDigitalXRayImageStorageForProcessing(String digitalXRayImageStorageForProcessing) {
        this.digitalXRayImageStorageForProcessing = digitalXRayImageStorageForProcessing;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getEnhancedMRImageStorage() {
        return enhancedMRImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setEnhancedMRImageStorage(String enhancedMRImageStorage) {
        this.enhancedMRImageStorage = enhancedMRImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getEnhancedSR() {
        return enhancedSR;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setEnhancedSR(String enhancedSR) {
        this.enhancedSR = enhancedSR;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getGeneralECGWaveformStorage() {
        return generalECGWaveformStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setGeneralECGWaveformStorage(String generalECGWaveformStorage) {
        this.generalECGWaveformStorage = generalECGWaveformStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getGrayscaleSoftcopyPresentationStateStorage() {
        return grayscaleSoftcopyPresentationStateStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setGrayscaleSoftcopyPresentationStateStorage(String grayscaleSoftcopyPresentationStateStorage) {
        this.grayscaleSoftcopyPresentationStateStorage = grayscaleSoftcopyPresentationStateStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getHardcopyColorImageStorage() {
        return hardcopyColorImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setHardcopyColorImageStorage(String hardcopyColorImageStorage) {
        this.hardcopyColorImageStorage = hardcopyColorImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getHardcopyGrayscaleImageStorage() {
        return hardcopyGrayscaleImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setHardcopyGrayscaleImageStorage(String hardcopyGrayscaleImageStorage) {
        this.hardcopyGrayscaleImageStorage = hardcopyGrayscaleImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getHemodynamicWaveformStorage() {
        return hemodynamicWaveformStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setHemodynamicWaveformStorage(String hemodynamicWaveformStorage) {
        this.hemodynamicWaveformStorage = hemodynamicWaveformStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getKeyObjectSelectionDocument() {
        return keyObjectSelectionDocument;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setKeyObjectSelectionDocument(String keyObjectSelectionDocument) {
        this.keyObjectSelectionDocument = keyObjectSelectionDocument;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getMammographyCADSR() {
        return mammographyCADSR;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setMammographyCADSR(String mammographyCADSR) {
        this.mammographyCADSR = mammographyCADSR;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getMrImageStorage() {
        return mrImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setMrImageStorage(String mrImageStorage) {
        this.mrImageStorage = mrImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getMrSpectroscopyStorage() {
        return mrSpectroscopyStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setMrSpectroscopyStorage(String mrSpectroscopyStorage) {
        this.mrSpectroscopyStorage = mrSpectroscopyStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getMultiframeColorSecondaryCaptureImageStorage() {
        return multiframeColorSecondaryCaptureImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setMultiframeColorSecondaryCaptureImageStorage(String multiframeColorSecondaryCaptureImageStorage) {
        this.multiframeColorSecondaryCaptureImageStorage = multiframeColorSecondaryCaptureImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getMultiframeGrayscaleByteSecondaryCaptureImageStorage() {
        return multiframeGrayscaleByteSecondaryCaptureImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setMultiframeGrayscaleByteSecondaryCaptureImageStorage(String multiframeGrayscaleByteSecondaryCaptureImageStorage) {
        this.multiframeGrayscaleByteSecondaryCaptureImageStorage = multiframeGrayscaleByteSecondaryCaptureImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getMultiframeGrayscaleWordSecondaryCaptureImageStorage() {
        return multiframeGrayscaleWordSecondaryCaptureImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setMultiframeGrayscaleWordSecondaryCaptureImageStorage(String multiframeGrayscaleWordSecondaryCaptureImageStorage) {
        this.multiframeGrayscaleWordSecondaryCaptureImageStorage = multiframeGrayscaleWordSecondaryCaptureImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getMultiframeSingleBitSecondaryCaptureImageStorage() {
        return multiframeSingleBitSecondaryCaptureImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setMultiframeSingleBitSecondaryCaptureImageStorage(String multiframeSingleBitSecondaryCaptureImageStorage) {
        this.multiframeSingleBitSecondaryCaptureImageStorage = multiframeSingleBitSecondaryCaptureImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getNuclearMedicineImageStorage() {
        return nuclearMedicineImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setNuclearMedicineImageStorage(String nuclearMedicineImageStorage) {
        this.nuclearMedicineImageStorage = nuclearMedicineImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getNuclearMedicineImageStorageRetired() {
        return nuclearMedicineImageStorageRetired;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setNuclearMedicineImageStorageRetired(String nuclearMedicineImageStorageRetired) {
        this.nuclearMedicineImageStorageRetired = nuclearMedicineImageStorageRetired;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getPositronEmissionTomographyImageStorage() {
        return positronEmissionTomographyImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setPositronEmissionTomographyImageStorage(String positronEmissionTomographyImageStorage) {
        this.positronEmissionTomographyImageStorage = positronEmissionTomographyImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getRawDataStorage() {
        return rawDataStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setRawDataStorage(String rawDataStorage) {
        this.rawDataStorage = rawDataStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getRtBeamsTreatmentRecordStorage() {
        return rtBeamsTreatmentRecordStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setRtBeamsTreatmentRecordStorage(String rtBeamsTreatmentRecordStorage) {
        this.rtBeamsTreatmentRecordStorage = rtBeamsTreatmentRecordStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getRtDoseStorage() {
        return rtDoseStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setRtDoseStorage(String rtDoseStorage) {
        this.rtDoseStorage = rtDoseStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getRtImageStorage() {
        return rtImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setRtImageStorage(String rtImageStorage) {
        this.rtImageStorage = rtImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getRtPlanStorage() {
        return rtPlanStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setRtPlanStorage(String rtPlanStorage) {
        this.rtPlanStorage = rtPlanStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getRtStructureSetStorage() {
        return rtStructureSetStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setRtStructureSetStorage(String rtStructureSetStorage) {
        this.rtStructureSetStorage = rtStructureSetStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getRtTreatmentSummaryRecordStorage() {
        return rtTreatmentSummaryRecordStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setRtTreatmentSummaryRecordStorage(String rtTreatmentSummaryRecordStorage) {
        this.rtTreatmentSummaryRecordStorage = rtTreatmentSummaryRecordStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getSecondaryCaptureImageStorage() {
        return secondaryCaptureImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setSecondaryCaptureImageStorage(String secondaryCaptureImageStorage) {
        this.secondaryCaptureImageStorage = secondaryCaptureImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getStandaloneCurveStorage() {
        return standaloneCurveStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setStandaloneCurveStorage(String standaloneCurveStorage) {
        this.standaloneCurveStorage = standaloneCurveStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getStandaloneModalityLUTStorage() {
        return standaloneModalityLUTStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setStandaloneModalityLUTStorage(String standaloneModalityLUTStorage) {
        this.standaloneModalityLUTStorage = standaloneModalityLUTStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getStandaloneOverlayStorage() {
        return standaloneOverlayStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setStandaloneOverlayStorage(String standaloneOverlayStorage) {
        this.standaloneOverlayStorage = standaloneOverlayStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getStandalonePETCurveStorage() {
        return standalonePETCurveStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setStandalonePETCurveStorage(String standalonePETCurveStorage) {
        this.standalonePETCurveStorage = standalonePETCurveStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getStandaloneVOILUTStorage() {
        return standaloneVOILUTStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setStandaloneVOILUTStorage(String standaloneVOILUTStorage) {
        this.standaloneVOILUTStorage = standaloneVOILUTStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getTwelveLeadECGWaveformStorage() {
        return twelveLeadECGWaveformStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setTwelveLeadECGWaveformStorage(String twelveLeadECGWaveformStorage) {
        this.twelveLeadECGWaveformStorage = twelveLeadECGWaveformStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getUltrasoundImageStorage() {
        return ultrasoundImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setUltrasoundImageStorage(String ultrasoundImageStorage) {
        this.ultrasoundImageStorage = ultrasoundImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getUltrasoundImageStorageRetired() {
        return ultrasoundImageStorageRetired;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setUltrasoundImageStorageRetired(String ultrasoundImageStorageRetired) {
        this.ultrasoundImageStorageRetired = ultrasoundImageStorageRetired;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getUltrasoundMultiframeImageStorage() {
        return ultrasoundMultiframeImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setUltrasoundMultiframeImageStorage(String ultrasoundMultiframeImageStorage) {
        this.ultrasoundMultiframeImageStorage = ultrasoundMultiframeImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getUltrasoundMultiframeImageStorageRetired() {
        return ultrasoundMultiframeImageStorageRetired;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setUltrasoundMultiframeImageStorageRetired(String ultrasoundMultiframeImageStorageRetired) {
        this.ultrasoundMultiframeImageStorageRetired = ultrasoundMultiframeImageStorageRetired;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getVlEndoscopicImageStorage() {
        return vlEndoscopicImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setVlEndoscopicImageStorage(String vlEndoscopicImageStorage) {
        this.vlEndoscopicImageStorage = vlEndoscopicImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getVlImageStorageRetired() {
        return vlImageStorageRetired;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setVlImageStorageRetired(String vlImageStorageRetired) {
        this.vlImageStorageRetired = vlImageStorageRetired;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getVlMicroscopicImageStorage() {
        return vlMicroscopicImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setVlMicroscopicImageStorage(String vlMicroscopicImageStorage) {
        this.vlMicroscopicImageStorage = vlMicroscopicImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getVlMultiframeImageStorageRetired() {
        return vlMultiframeImageStorageRetired;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setVlMultiframeImageStorageRetired(String vlMultiframeImageStorageRetired) {
        this.vlMultiframeImageStorageRetired = vlMultiframeImageStorageRetired;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getVlPhotographicImageStorage() {
        return vlPhotographicImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setVlPhotographicImageStorage(String vlPhotographicImageStorage) {
        this.vlPhotographicImageStorage = vlPhotographicImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getVlSlideCoordinatesMicroscopicImageStorage() {
        return vlSlideCoordinatesMicroscopicImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setVlSlideCoordinatesMicroscopicImageStorage(String vlSlideCoordinatesMicroscopicImageStorage) {
        this.vlSlideCoordinatesMicroscopicImageStorage = vlSlideCoordinatesMicroscopicImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getXRayAngiographicBiPlaneImageStorageRetired() {
        return xRayAngiographicBiPlaneImageStorageRetired;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setXRayAngiographicBiPlaneImageStorageRetired(String rayAngiographicBiPlaneImageStorageRetired) {
        xRayAngiographicBiPlaneImageStorageRetired = rayAngiographicBiPlaneImageStorageRetired;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getXRayAngiographicImageStorage() {
        return xRayAngiographicImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setXRayAngiographicImageStorage(String rayAngiographicImageStorage) {
        xRayAngiographicImageStorage = rayAngiographicImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public final String getXRayRadiofluoroscopicImageStorage() {
        return xRayRadiofluoroscopicImageStorage;
    }

    /**
     * @jmx.managed-attribute
     */
    public final void setXRayRadiofluoroscopicImageStorage(String rayRadiofluoroscopicImageStorage) {
        xRayRadiofluoroscopicImageStorage = rayRadiofluoroscopicImageStorage;
        if (getState() == STARTED) {
            updatePolicy();
        }
    }

    /**
     * @jmx.managed-attribute
     */
    public String getStorageCommitmentPushModel() {
        return storageCommitmentPushModel;
    }

    /**
     * @jmx.managed-attribute
     */
    public void setStorageCommitmentPushModel(String storageCommitmentPushModel) {
        this.storageCommitmentPushModel = storageCommitmentPushModel;
    }

    protected void startService() throws Exception {
        scp.checkReadyToStart();
        super.startService();
    }

    protected void bindDcmServices(DcmServiceRegistry services) {
        for (int i = 0; i < STORAGE_AS.length; ++i) {
            services.bind(STORAGE_AS[i], scp);
        }
        services.bind(UIDs.StorageCommitmentPushModel, stgCmtScp);
        dcmHandler.addAssociationListener(scp);
    }

    protected void unbindDcmServices(DcmServiceRegistry services) {
        for (int i = 0; i < STORAGE_AS.length; ++i) {
            services.unbind(STORAGE_AS[i]);
        }
        services.unbind(UIDs.StorageCommitmentPushModel);
        dcmHandler.removeAssociationListener(scp);
    }

    protected AcceptorPolicy getAcceptorPolicy() {
        AcceptorPolicy policy = asf.newAcceptorPolicy();
        policy.setCallingAETs(callingAETs);
        putPresContext(policy, UIDs.ComputedRadiographyImageStorage, computedRadiographyImageStorage);
        putPresContext(policy, UIDs.DigitalXRayImageStorageForPresentation, digitalXRayImageStorageForPresentation);
        putPresContext(policy, UIDs.DigitalXRayImageStorageForProcessing, digitalXRayImageStorageForProcessing);
        putPresContext(policy, UIDs.DigitalMammographyXRayImageStorageForPresentation, digitalMammographyXRayImageStorageForPresentation);
        putPresContext(policy, UIDs.DigitalMammographyXRayImageStorageForProcessing, digitalMammographyXRayImageStorageForProcessing);
        putPresContext(policy, UIDs.DigitalIntraoralXRayImageStorageForPresentation, digitalIntraoralXRayImageStorageForPresentation);
        putPresContext(policy, UIDs.DigitalIntraoralXRayImageStorageForProcessing, digitalIntraoralXRayImageStorageForProcessing);
        putPresContext(policy, UIDs.CTImageStorage, ctImageStorage);
        putPresContext(policy, UIDs.UltrasoundMultiframeImageStorageRetired, ultrasoundMultiframeImageStorageRetired);
        putPresContext(policy, UIDs.UltrasoundMultiframeImageStorage, ultrasoundMultiframeImageStorage);
        putPresContext(policy, UIDs.MRImageStorage, mrImageStorage);
        putPresContext(policy, UIDs.EnhancedMRImageStorage, enhancedMRImageStorage);
        putPresContext(policy, UIDs.MRSpectroscopyStorage, mrSpectroscopyStorage);
        putPresContext(policy, UIDs.NuclearMedicineImageStorageRetired, nuclearMedicineImageStorageRetired);
        putPresContext(policy, UIDs.UltrasoundImageStorageRetired, ultrasoundImageStorageRetired);
        putPresContext(policy, UIDs.UltrasoundImageStorage, ultrasoundImageStorage);
        putPresContext(policy, UIDs.SecondaryCaptureImageStorage, secondaryCaptureImageStorage);
        putPresContext(policy, UIDs.MultiframeSingleBitSecondaryCaptureImageStorage, multiframeSingleBitSecondaryCaptureImageStorage);
        putPresContext(policy, UIDs.MultiframeGrayscaleByteSecondaryCaptureImageStorage, multiframeGrayscaleByteSecondaryCaptureImageStorage);
        putPresContext(policy, UIDs.MultiframeGrayscaleWordSecondaryCaptureImageStorage, multiframeGrayscaleWordSecondaryCaptureImageStorage);
        putPresContext(policy, UIDs.MultiframeColorSecondaryCaptureImageStorage, multiframeColorSecondaryCaptureImageStorage);
        putPresContext(policy, UIDs.HardcopyGrayscaleImageStorage, hardcopyGrayscaleImageStorage);
        putPresContext(policy, UIDs.HardcopyColorImageStorage, hardcopyColorImageStorage);
        putPresContext(policy, UIDs.StandaloneOverlayStorage, standaloneOverlayStorage);
        putPresContext(policy, UIDs.StandaloneCurveStorage, standaloneCurveStorage);
        putPresContext(policy, UIDs.TwelveLeadECGWaveformStorage, twelveLeadECGWaveformStorage);
        putPresContext(policy, UIDs.GeneralECGWaveformStorage, generalECGWaveformStorage);
        putPresContext(policy, UIDs.AmbulatoryECGWaveformStorage, ambulatoryECGWaveformStorage);
        putPresContext(policy, UIDs.HemodynamicWaveformStorage, hemodynamicWaveformStorage);
        putPresContext(policy, UIDs.RTBrachyTreatmentRecordStorage, cardiacElectrophysiologyWaveformStorage);
        putPresContext(policy, UIDs.BasicVoiceAudioWaveformStorage, basicVoiceAudioWaveformStorage);
        putPresContext(policy, UIDs.StandaloneModalityLUTStorage, standaloneModalityLUTStorage);
        putPresContext(policy, UIDs.StandaloneVOILUTStorage, standaloneVOILUTStorage);
        putPresContext(policy, UIDs.GrayscaleSoftcopyPresentationStateStorage, grayscaleSoftcopyPresentationStateStorage);
        putPresContext(policy, UIDs.XRayAngiographicImageStorage, xRayAngiographicImageStorage);
        putPresContext(policy, UIDs.XRayRadiofluoroscopicImageStorage, xRayRadiofluoroscopicImageStorage);
        putPresContext(policy, UIDs.XRayAngiographicBiPlaneImageStorageRetired, xRayAngiographicBiPlaneImageStorageRetired);
        putPresContext(policy, UIDs.NuclearMedicineImageStorage, nuclearMedicineImageStorage);
        putPresContext(policy, UIDs.RTBrachyTreatmentRecordStorage, rawDataStorage);
        putPresContext(policy, UIDs.VLImageStorageRetired, vlImageStorageRetired);
        putPresContext(policy, UIDs.VLMultiframeImageStorageRetired, vlMultiframeImageStorageRetired);
        putPresContext(policy, UIDs.VLEndoscopicImageStorage, vlEndoscopicImageStorage);
        putPresContext(policy, UIDs.VLMicroscopicImageStorage, vlMicroscopicImageStorage);
        putPresContext(policy, UIDs.VLSlideCoordinatesMicroscopicImageStorage, vlSlideCoordinatesMicroscopicImageStorage);
        putPresContext(policy, UIDs.VLPhotographicImageStorage, vlPhotographicImageStorage);
        putPresContext(policy, UIDs.BasicTextSR, basicTextSR);
        putPresContext(policy, UIDs.EnhancedSR, enhancedSR);
        putPresContext(policy, UIDs.ComprehensiveSR, comprehensiveSR);
        putPresContext(policy, UIDs.MammographyCADSR, mammographyCADSR);
        putPresContext(policy, UIDs.KeyObjectSelectionDocument, keyObjectSelectionDocument);
        putPresContext(policy, UIDs.PositronEmissionTomographyImageStorage, positronEmissionTomographyImageStorage);
        putPresContext(policy, UIDs.StandalonePETCurveStorage, standalonePETCurveStorage);
        putPresContext(policy, UIDs.RTImageStorage, rtImageStorage);
        putPresContext(policy, UIDs.RTDoseStorage, rtDoseStorage);
        putPresContext(policy, UIDs.RTStructureSetStorage, rtStructureSetStorage);
        putPresContext(policy, UIDs.RTBeamsTreatmentRecordStorage, rtBeamsTreatmentRecordStorage);
        putPresContext(policy, UIDs.RTPlanStorage, rtPlanStorage);
        putPresContext(policy, UIDs.RTTreatmentSummaryRecordStorage, rtTreatmentSummaryRecordStorage);
        putPresContext(policy, UIDs.StorageCommitmentPushModel, storageCommitmentPushModel);
        return policy;
    }
}
