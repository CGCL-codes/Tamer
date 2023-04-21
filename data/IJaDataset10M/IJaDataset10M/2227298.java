package orbe.gui.map.renderer;

import net.sf.doolin.gui.util.GUIStrings;
import orbe.hex.HXRect;
import orbe.model.OrbeMap;
import org.apache.commons.lang.ObjectUtils;

public abstract class AbstractLayer implements OrbeLayer {

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof AbstractLayer) {
            OrbeLayer item = (OrbeLayer) obj;
            return ObjectUtils.equals(this.getId(), item.getId());
        } else {
            return false;
        }
    }

    protected HXRect getHXMapBounds(OrbeMap map) {
        return new HXRect(0, 0, map.getWidth() - 1, map.getHeight() - 1);
    }

    public String getTitle() {
        return GUIStrings.get("Layer." + getId());
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(getId());
    }

    public final boolean isVisible(OrbeMap map) {
        return map.getSettings().isLayerVisible(getId());
    }
}
