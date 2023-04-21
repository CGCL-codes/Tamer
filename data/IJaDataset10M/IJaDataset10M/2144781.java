package com.liferay.portal.ejb;

/**
 * <a href="LayerHBMUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.2 $
 *
 */
public class LayerHBMUtil {

    public static com.liferay.portal.model.Layer model(LayerHBM layerHBM) {
        com.liferay.portal.model.Layer layer = LayerPool.get(layerHBM.getPrimaryKey());
        if (layer == null) {
            layer = new com.liferay.portal.model.Layer(layerHBM.getLayerId(), layerHBM.getSkinId(), layerHBM.getHref(), layerHBM.getHrefHover(), layerHBM.getBackground(), layerHBM.getForeground(), layerHBM.getNegAlert(), layerHBM.getPosAlert());
            LayerPool.put(layer.getPrimaryKey(), layer);
        }
        return layer;
    }
}
