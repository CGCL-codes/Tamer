package com.pixelmed.dicom;

import org.dicom4j.dicom.UniqueIdentifiers.support.UniqueIdentifier;

/**
 * <p>The abstract base class of classes that implement individual DICOM SOP Classes or
 * groups of SOP Classes that behave similarly (for example the composite instance
 * Storage SOP Classes).
 * </p>
 * <p>There is no formal or separate abstraction of the DICOM concept of a Service Class per se.
 * </p>
 * <p>Also defines the UID strings that correspond to the individual SOP Classes, as well as
 * various utility methods for testing whether or not a string UID is of a particular family.
 * <p>
 *
 * @author	dclunie
 */
public class SOPClass extends UniqueIdentifier {

    public SOPClass(String aValue, String aName) {
        super(UniqueIdentifier.Types.SOPCLASS, aValue, aName);
    }

    /***/
    public static final String ComputedRadiographyImageStorage = "1.2.840.10008.5.1.4.1.1.1";

    /***/
    public static final String DigitalXRayImageStorageForPresentation = "1.2.840.10008.5.1.4.1.1.1.1";

    /***/
    public static final String DigitalXRayImageStorageForProcessing = "1.2.840.10008.5.1.4.1.1.1.1.1";

    /***/
    public static final String DigitalMammographyXRayImageStorageForPresentation = "1.2.840.10008.5.1.4.1.1.1.2";

    /***/
    public static final String DigitalMammographyXRayImageStorageForProcessing = "1.2.840.10008.5.1.4.1.1.1.2.1";

    /***/
    public static final String DigitalIntraoralXRayImageStorageForPresentation = "1.2.840.10008.5.1.4.1.1.1.3";

    /***/
    public static final String DigitalIntraoralXRayImageStorageForProcessing = "1.2.840.10008.5.1.4.1.1.1.3.1";

    /***/
    public static final String CTImageStorage = "1.2.840.10008.5.1.4.1.1.2";

    /***/
    public static final String EnhancedCTImageStorage = "1.2.840.10008.5.1.4.1.1.2.1";

    /***/
    public static final String UltrasoundMultiframeImageStorageRetired = "1.2.840.10008.5.1.4.1.1.3";

    /***/
    public static final String UltrasoundMultiframeImageStorage = "1.2.840.10008.5.1.4.1.1.3.1";

    /***/
    public static final String MRImageStorage = "1.2.840.10008.5.1.4.1.1.4";

    /***/
    public static final String EnhancedMRImageStorage = "1.2.840.10008.5.1.4.1.1.4.1";

    /***/
    public static final String NuclearMedicineImageStorageRetired = "1.2.840.10008.5.1.4.1.1.5";

    /***/
    public static final String UltrasoundImageStorageRetired = "1.2.840.10008.5.1.4.1.1.6";

    /***/
    public static final String UltrasoundImageStorage = "1.2.840.10008.5.1.4.1.1.6.1";

    /***/
    public static final String SecondaryCaptureImageStorage = "1.2.840.10008.5.1.4.1.1.7";

    /***/
    public static final String MultiframeSingleBitSecondaryCaptureImageStorage = "1.2.840.10008.5.1.4.1.1.7.1";

    /***/
    public static final String MultiframeGrayscaleByteSecondaryCaptureImageStorage = "1.2.840.10008.5.1.4.1.1.7.2";

    /***/
    public static final String MultiframeGrayscaleWordSecondaryCaptureImageStorage = "1.2.840.10008.5.1.4.1.1.7.3";

    /***/
    public static final String MultiframeTrueColorSecondaryCaptureImageStorage = "1.2.840.10008.5.1.4.1.1.7.4";

    /***/
    public static final String XrayAngiographicImageStorage = "1.2.840.10008.5.1.4.1.1.12.1";

    /***/
    public static final String XrayRadioFlouroscopicImageStorage = "1.2.840.10008.5.1.4.1.1.12.2";

    /***/
    public static final String XrayAngiographicBiplaneImageStorage = "1.2.840.10008.5.1.4.1.1.12.3";

    /***/
    public static final String NuclearMedicineImageStorage = "1.2.840.10008.5.1.4.1.1.20";

    /***/
    public static final String VisibleLightDraftImageStorage = "1.2.840.10008.5.1.4.1.1.77.1";

    /***/
    public static final String VisibleLightMultiFrameDraftImageStorage = "1.2.840.10008.5.1.4.1.1.77.2";

    /***/
    public static final String VisibleLightEndoscopicImageStorage = "1.2.840.10008.5.1.4.1.1.77.1.1";

    /***/
    public static final String VideoEndoscopicImageStorage = "1.2.840.10008.5.1.4.1.1.77.1.1.1";

    /***/
    public static final String VisibleLightMicroscopicImageStorage = "1.2.840.10008.5.1.4.1.1.77.1.2";

    /***/
    public static final String VideoMicroscopicImageStorage = "1.2.840.10008.5.1.4.1.1.77.1.2.1";

    /***/
    public static final String VisibleLightSlideCoordinatesMicroscopicImageStorage = "1.2.840.10008.5.1.4.1.1.77.1.3";

    /***/
    public static final String VisibleLightPhotographicImageStorage = "1.2.840.10008.5.1.4.1.1.77.1.4";

    /***/
    public static final String VideoPhotographicImageStorage = "1.2.840.10008.5.1.4.1.1.77.1.4.1";

    /***/
    public static final String PETImageStorage = "1.2.840.10008.5.1.4.1.1.128";

    /***/
    public static final String RTImageStorage = "1.2.840.10008.5.1.4.1.1.481.1";

    /**
	 * @param	sopClassUID	UID of the SOP Class, as a String without trailing zero padding
	 * @return			true if the UID argument matches one of the known standard Image Storage SOP Classes
	 */
    public static final boolean isImageStorage(String sopClassUID) {
        return sopClassUID != null && (sopClassUID.equals(ComputedRadiographyImageStorage) || sopClassUID.equals(DigitalXRayImageStorageForPresentation) || sopClassUID.equals(DigitalXRayImageStorageForProcessing) || sopClassUID.equals(DigitalMammographyXRayImageStorageForPresentation) || sopClassUID.equals(DigitalMammographyXRayImageStorageForProcessing) || sopClassUID.equals(DigitalIntraoralXRayImageStorageForPresentation) || sopClassUID.equals(DigitalIntraoralXRayImageStorageForProcessing) || sopClassUID.equals(CTImageStorage) || sopClassUID.equals(EnhancedCTImageStorage) || sopClassUID.equals(UltrasoundMultiframeImageStorageRetired) || sopClassUID.equals(UltrasoundMultiframeImageStorage) || sopClassUID.equals(MRImageStorage) || sopClassUID.equals(EnhancedMRImageStorage) || sopClassUID.equals(NuclearMedicineImageStorageRetired) || sopClassUID.equals(UltrasoundImageStorageRetired) || sopClassUID.equals(UltrasoundImageStorage) || sopClassUID.equals(SecondaryCaptureImageStorage) || sopClassUID.equals(MultiframeSingleBitSecondaryCaptureImageStorage) || sopClassUID.equals(MultiframeGrayscaleByteSecondaryCaptureImageStorage) || sopClassUID.equals(MultiframeGrayscaleWordSecondaryCaptureImageStorage) || sopClassUID.equals(MultiframeTrueColorSecondaryCaptureImageStorage) || sopClassUID.equals(XrayAngiographicImageStorage) || sopClassUID.equals(XrayRadioFlouroscopicImageStorage) || sopClassUID.equals(XrayAngiographicBiplaneImageStorage) || sopClassUID.equals(NuclearMedicineImageStorage) || sopClassUID.equals(VisibleLightDraftImageStorage) || sopClassUID.equals(VisibleLightMultiFrameDraftImageStorage) || sopClassUID.equals(VisibleLightEndoscopicImageStorage) || sopClassUID.equals(VisibleLightMicroscopicImageStorage) || sopClassUID.equals(VisibleLightSlideCoordinatesMicroscopicImageStorage) || sopClassUID.equals(VisibleLightPhotographicImageStorage) || sopClassUID.equals(PETImageStorage) || sopClassUID.equals(RTImageStorage));
    }

    /***/
    public static final String MediaStorageDirectoryStorage = "1.2.840.10008.1.3.10";

    /**
	 * @param	sopClassUID	UID of the SOP Class, as a String without trailing zero padding
	 * @return			true if the UID argument matches the Media Storage Directory Storage SOP Class (used for the DICOMDIR)
	 */
    public static final boolean isDirectory(String sopClassUID) {
        return sopClassUID != null && (sopClassUID.equals(MediaStorageDirectoryStorage));
    }

    /***/
    public static final String BasicTextSRStorage = "1.2.840.10008.5.1.4.1.1.88.11";

    /***/
    public static final String EnhancedSRStorage = "1.2.840.10008.5.1.4.1.1.88.22";

    /***/
    public static final String ComprehensiveSRStorage = "1.2.840.10008.5.1.4.1.1.88.33";

    /***/
    public static final String MammographyCADSRStorage = "1.2.840.10008.5.1.4.1.1.88.50";

    /***/
    public static final String KeyObjectSelectionDocumentStorage = "1.2.840.10008.5.1.4.1.1.88.59";

    /**
	 * @param	sopClassUID	UID of the SOP Class, as a String without trailing zero padding
	 * @return			true if the UID argument matches one of the known standard generic or specific Structured Report Storage SOP Classes (including Key Object)
	 */
    public static final boolean isStructuredReport(String sopClassUID) {
        return sopClassUID != null && (sopClassUID.equals(BasicTextSRStorage) || sopClassUID.equals(EnhancedSRStorage) || sopClassUID.equals(ComprehensiveSRStorage) || sopClassUID.equals(MammographyCADSRStorage) || sopClassUID.equals(KeyObjectSelectionDocumentStorage));
    }

    /***/
    public static final String GrayscaleSoftcopyPresentationStateStorage = "1.2.840.10008.5.1.4.1.1.11.1";

    /**
	 * @param	sopClassUID	UID of the SOP Class, as a String without trailing zero padding
	 * @return			true if the UID argument matches one of the known standard Presentation State Storage SOP Classes (currently just the Grayscale Softcopy Presentation State Storage SOP Class)
	 */
    public static final boolean isPresentationState(String sopClassUID) {
        return sopClassUID != null && (sopClassUID.equals(GrayscaleSoftcopyPresentationStateStorage));
    }

    /***/
    public static final String TwelveLeadECGStorage = "1.2.840.10008.5.1.4.1.1.9.1.1";

    /***/
    public static final String GeneralECGStorage = "1.2.840.10008.5.1.4.1.1.9.1.2";

    /***/
    public static final String AmbulatoryECGStorage = "1.2.840.10008.5.1.4.1.1.9.1.3";

    /***/
    public static final String HemodynamicWaveformStorage = "1.2.840.10008.5.1.4.1.1.9.2.1";

    /***/
    public static final String CardiacElectrophysiologyWaveformStorage = "1.2.840.10008.5.1.4.1.1.9.3.1";

    /***/
    public static final String BasicVoiceStorage = "1.2.840.10008.5.1.4.1.1.9.4.1";

    /**
	 * @param	sopClassUID	UID of the SOP Class, as a String without trailing zero padding
	 * @return			true if the UID argument matches one of the known standard Waveform Storage SOP Classes
	 */
    public static final boolean isWaveform(String sopClassUID) {
        return sopClassUID != null && (sopClassUID.equals(TwelveLeadECGStorage) || sopClassUID.equals(GeneralECGStorage) || sopClassUID.equals(AmbulatoryECGStorage) || sopClassUID.equals(HemodynamicWaveformStorage) || sopClassUID.equals(CardiacElectrophysiologyWaveformStorage) || sopClassUID.equals(BasicVoiceStorage));
    }

    /***/
    public static final String StandaloneOverlayStorage = "1.2.840.10008.5.1.4.1.1.8";

    /***/
    public static final String StandaloneCurveStorage = "1.2.840.10008.5.1.4.1.1.9";

    /***/
    public static final String StandaloneModalityLUTStorage = "1.2.840.10008.5.1.4.1.1.10";

    /***/
    public static final String StandaloneVOILUTStorage = "1.2.840.10008.5.1.4.1.1.11";

    /***/
    public static final String StandalonePETCurveStorage = "1.2.840.10008.5.1.4.1.1.129";

    /**
	 * @param	sopClassUID	UID of the SOP Class, as a String without trailing zero padding
	 * @return			true if the UID argument matches one of the known standard Standalone Storage SOP Classes (overlay, curve (including PET curve), and LUTs)
	 */
    public static final boolean isStandalone(String sopClassUID) {
        return sopClassUID != null && (sopClassUID.equals(StandaloneOverlayStorage) || sopClassUID.equals(StandaloneCurveStorage) || sopClassUID.equals(StandaloneModalityLUTStorage) || sopClassUID.equals(StandaloneVOILUTStorage) || sopClassUID.equals(StandalonePETCurveStorage));
    }

    /***/
    public static final String RTDoseStorage = "1.2.840.10008.5.1.4.1.1.481.2";

    /***/
    public static final String RTStructureSetStorage = "1.2.840.10008.5.1.4.1.1.481.3";

    /***/
    public static final String RTBeamsTreatmentRecordStorage = "1.2.840.10008.5.1.4.1.1.481.4";

    /***/
    public static final String RTPlanStorage = "1.2.840.10008.5.1.4.1.1.481.5";

    /***/
    public static final String RTBrachyTreatmentRecordStorage = "1.2.840.10008.5.1.4.1.1.481.6";

    /***/
    public static final String RTTreatmentSummaryRecordStorage = "1.2.840.10008.5.1.4.1.1.481.7";

    /**
	 * @param	sopClassUID	UID of the SOP Class, as a String without trailing zero padding
	 * @return			true if the UID argument matches one of the known standard RT non-image Storage SOP Classes (dose, structure set, plan and treatment records)
	 */
    public static final boolean isRadiotherapy(String sopClassUID) {
        return sopClassUID != null && (sopClassUID.equals(RTDoseStorage) || sopClassUID.equals(RTStructureSetStorage) || sopClassUID.equals(RTBeamsTreatmentRecordStorage) || sopClassUID.equals(RTPlanStorage) || sopClassUID.equals(RTBrachyTreatmentRecordStorage) || sopClassUID.equals(RTTreatmentSummaryRecordStorage));
    }

    /***/
    public static final String MRSpectroscopyStorage = "1.2.840.10008.5.1.4.1.1.4.2";

    /**
	 * @param	sopClassUID	UID of the SOP Class, as a String without trailing zero padding
	 * @return			true if the UID argument matches one of the known standard Spectroscopy Storage SOP Classes (currently just the MR Spectroscopy Storage SOP Class)
	 */
    public static final boolean isSpectroscopy(String sopClassUID) {
        return sopClassUID != null && (sopClassUID.equals(MRSpectroscopyStorage));
    }

    /***/
    public static final String RawDataStorage = "1.2.840.10008.5.1.4.1.1.66";

    /**
	 * @param	sopClassUID	UID of the SOP Class, as a String without trailing zero padding
	 * @return			true if the UID argument matches the Raw Data Storage SOP Class
	 */
    public static final boolean isRawData(String sopClassUID) {
        return sopClassUID != null && (sopClassUID.equals(RawDataStorage));
    }

    /**
	 * @param	sopClassUID	UID of the SOP Class, as a String without trailing zero padding
	 * @return			true if the UID argument matches one of the known non-image Storage SOP Classes (directory, SR, presentation state, waveform, standalone, RT, spectroscopy or raw data)
	 */
    public static final boolean isNonImageStorage(String sopClassUID) {
        return isDirectory(sopClassUID) || isStructuredReport(sopClassUID) || isPresentationState(sopClassUID) || isWaveform(sopClassUID) || isStandalone(sopClassUID) || isRadiotherapy(sopClassUID) || isSpectroscopy(sopClassUID) || isRawData(sopClassUID);
    }

    /***/
    public static final String StudyRootQueryRetrieveInformationModelFind = "1.2.840.10008.5.1.4.1.2.2.1";

    /***/
    public static final String StudyRootQueryRetrieveInformationModelMove = "1.2.840.10008.5.1.4.1.2.2.2";
}
