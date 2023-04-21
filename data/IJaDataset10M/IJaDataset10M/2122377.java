package info.port4.bbsp2.event;

import java.util.EventObject;
import info.port4.bbsp2.Mask;
import info.port4.bbsp2.Layer;

/**
 * @see info.port4.bbsp2.canvas.Art
 * @author <a href="mailto:harumanx@geocities.co.jp">MIYABE Tatsuhiko</a>
 * @version $Id: LayerEvent.java,v 1.1 2002/06/01 15:12:21 harumanx Exp $
 */
public class LayerEvent extends EventObject {

    private Mask clip;

    public LayerEvent(Object source, Mask clip) {
        super(source);
        this.clip = clip;
    }

    public Mask getClip() {
        return this.clip;
    }

    public Layer getLayer() {
        return (Layer) getSource();
    }
}
