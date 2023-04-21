package org.gvsig.fmap.drivers.gpe.reader;

import java.util.Map;
import org.gvsig.fmap.drivers.gpe.exceptions.GPELayerException;
import org.gvsig.fmap.drivers.gpe.model.GPEFeature;
import com.iver.cit.gvsig.fmap.layers.FLayer;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class AddFeatureToLayerException extends GPELayerException {

    private static final long serialVersionUID = -1081746144520645516L;

    private GPEFeature feature = null;

    public AddFeatureToLayerException(FLayer layer, GPEFeature feature, Throwable exception) {
        super(layer, exception);
        this.feature = feature;
        initialize();
    }

    /**
	 * Initialize the properties
	 */
    private void initialize() {
        messageKey = "gpe_gvsig_addFeatureToLayer_error";
        formatString = "Error adding the feature with id %(featureId)" + " to the layer %(layerName)";
        code = serialVersionUID;
    }

    protected Map values() {
        Map map = super.values();
        map.put("featureID", feature.getId());
        return map;
    }
}
