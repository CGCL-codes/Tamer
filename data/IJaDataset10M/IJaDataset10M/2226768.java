package org.unicolet.axl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created:
 * User: unicoletti
 * Date: 2:13:13 AM Oct 28, 2005
 */
public abstract class Symbol {

    Log log = LogFactory.getLog(getClass());

    protected Layer layer;

    protected String transparency = "1.0";

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public String getTransparency() {
        return transparency;
    }

    public void setTransparency(String transparency) {
        this.transparency = transparency;
    }

    public int getTransparencyAsInt() {
        int t = 0;
        try {
            t = layer.getMap().getFile().getNumberFormat().parse(transparency).intValue();
        } catch (Throwable T) {
            T.printStackTrace();
        }
        return t;
    }
}
