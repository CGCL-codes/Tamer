package org.dcm4che2.iod.module.pr;

import java.util.HashMap;
import java.util.Map;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.iod.module.Module;

/**
 * C.10.7 Provide the colour, z-index and other related graphical layer
 * information.
 * 
 * @author Bill Wallace <wayfarer3130@gmail.com>
 * @version $Revision$ $Date$
 * @since July 19, 2007
 */
public class GraphicLayerModule extends Module {

    /** Create a display shutter IOD instance on the given dicom object data. */
    public GraphicLayerModule(DicomObject dcmobj) {
        super(dcmobj);
    }

    /**
	 * Gets a map of String to GraphicLayerModule for all the available graphic
	 * layers.
	 */
    public static Map toGraphicLayerMap(DicomObject dcmobj) {
        DicomElement sq = dcmobj.get(Tag.GraphicLayerSequence);
        if (sq == null || !sq.hasItems()) {
            return null;
        }
        int sqCount = sq.countItems();
        Map<String, GraphicLayerModule> layers = new HashMap<String, GraphicLayerModule>(sqCount);
        for (int i = 0; i < sqCount; i++) {
            GraphicLayerModule glm = new GraphicLayerModule(sq.getDicomObject(i));
            layers.put(glm.getGraphicLayer(), glm);
        }
        return layers;
    }

    /** Gets a string that identifies this layer within this GSPS object */
    public String getGraphicLayer() {
        return dcmobj.getString(Tag.GraphicLayer);
    }

    /**
	 * Get the p-value on the range 0..65535 to display, defaulting to a
	 * mid-gray if absent.
	 */
    public int getGraphicLayerRecommendedDisplayGrayscaleValue() {
        return dcmobj.getInt(Tag.GraphicLayerRecommendedDisplayGrayscaleValue, 32768);
    }

    public int[] getGraphicLayerRecommendedDisplayCIELabValue() {
        return dcmobj.getInts(Tag.GraphicLayerRecommendedDisplayCIELabValue);
    }

    public float[] getFloatLab() {
        return DisplayShutterModule.convertToFloatLab(getGraphicLayerRecommendedDisplayCIELabValue());
    }

    public int getGraphicLayerOrder() {
        return dcmobj.getInt(Tag.GraphicLayerOrder);
    }

    public String getGraphicLayerDescription() {
        return dcmobj.getString(Tag.GraphicLayerDescription);
    }

    /**
	 * Get the recommended RGB value to display this graphic layer in.
	 * 
	 * @deprecated No recommended to be used any longer.
	 * @return the RGB value to display as an integer triplet.
	 */
    @Deprecated
    public int[] getGraphicLayerRecommendedDisplayRGBValueRET() {
        return dcmobj.getInts(Tag.GraphicLayerRecommendedDisplayRGBValueRET);
    }
}
