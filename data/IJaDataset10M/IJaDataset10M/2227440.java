package org.gvsig.gpe.writer;

import org.gvsig.gpe.containers.Layer;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public abstract class GPELayerWithNameTest extends GPEWriterBaseTest {

    private String layerId = "l1";

    private String layerName = "Layer";

    private String layerDescription = "Layer with bbox Test";

    private String srs = "EPSG:23030";

    public void readObjects() {
        Layer[] layers = getLayers();
        assertEquals(layers.length, 1);
        Layer layer = layers[0];
        assertEquals(layer.getName(), layerName);
    }

    public void writeObjects() {
        getWriterHandler().initialize();
        getWriterHandler().startLayer(layerId, null, layerName, layerDescription, srs);
        getWriterHandler().endLayer();
        getWriterHandler().close();
    }
}
