package co.edu.unal.ungrid.image.dicom.display.event;

import co.edu.unal.ungrid.image.dicom.core.AttributeList;
import co.edu.unal.ungrid.image.dicom.core.SpectroscopyVolumeLocalization;
import co.edu.unal.ungrid.image.dicom.event.Event;
import co.edu.unal.ungrid.image.dicom.event.EventContext;
import co.edu.unal.ungrid.image.dicom.geometry.GeometryOfVolume;

/**
 * 
 */
public class SourceSpectrumSelectionChangeEvent extends Event {

    /**
	 * @uml.property name="srcSpectra" multiplicity="(0 -1)" dimension="2"
	 */
    private float[][] srcSpectra;

    /**
	 * @uml.property name="nSrcSpectra"
	 */
    private int nSrcSpectra;

    /**
	 * @uml.property name="index"
	 */
    private int index;

    /**
	 * @uml.property name="sortOrder"
	 */
    private int[] sortOrder;

    /**
	 * @uml.property name="attributeList"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
    private AttributeList attributeList;

    /**
	 * @uml.property name="spectroscopyGeometry"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
    private GeometryOfVolume spectroscopyGeometry;

    /**
	 * @uml.property name="spectroscopyVolumeLocalization"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
    private SpectroscopyVolumeLocalization spectroscopyVolumeLocalization;

    /**
	 * @param eventContext
	 * @param srcSpectra
	 * @param nSrcSpectra
	 * @param sortOrder
	 * @param index
	 * @param attributeList
	 * @param spectroscopyGeometry
	 * @param spectroscopyVolumeLocalization
	 */
    public SourceSpectrumSelectionChangeEvent(EventContext eventContext, float[][] srcSpectra, int nSrcSpectra, int[] sortOrder, int index, AttributeList attributeList, GeometryOfVolume spectroscopyGeometry, SpectroscopyVolumeLocalization spectroscopyVolumeLocalization) {
        super(eventContext);
        this.srcSpectra = srcSpectra;
        this.nSrcSpectra = nSrcSpectra;
        this.sortOrder = sortOrder;
        this.index = index;
        this.attributeList = attributeList;
        this.spectroscopyGeometry = spectroscopyGeometry;
        this.spectroscopyVolumeLocalization = spectroscopyVolumeLocalization;
    }

    /***/
    public float[][] getSourceSpectra() {
        return srcSpectra;
    }

    /***/
    public int getNumberOfSourceSpectra() {
        return nSrcSpectra;
    }

    /**
	 * @uml.property name="sortOrder"
	 */
    public int[] getSortOrder() {
        return sortOrder;
    }

    /**
	 * @uml.property name="index"
	 */
    public int getIndex() {
        return index;
    }

    /**
	 * @uml.property name="attributeList"
	 */
    public AttributeList getAttributeList() {
        return attributeList;
    }

    /***/
    public GeometryOfVolume getGeometryOfVolume() {
        return spectroscopyGeometry;
    }

    /**
	 * @uml.property name="spectroscopyVolumeLocalization"
	 */
    public SpectroscopyVolumeLocalization getSpectroscopyVolumeLocalization() {
        return spectroscopyVolumeLocalization;
    }
}
