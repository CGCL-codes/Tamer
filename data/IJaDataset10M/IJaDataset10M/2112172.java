package net.sourceforge.tile3d.view.writeFile.main.model;

import net.sourceforge.tile3d.view.writeFile.core.model.X3dTile;
import vrml.field.SFVec2f;

/**
 * @author tuannguyen
 *
 */
public class FloorBoxTile extends BoxTileBase {

    SFVec2f m_vec2fSize;

    public FloorBoxTile(X3dTile tile, float width, float length) {
        super();
        init(tile, width, length);
    }

    /**
	 * @return Returns the vec2fSize.
	 */
    public SFVec2f getVec2fSize() {
        return m_vec2fSize;
    }

    /**
	 * @param p_vec2fSize
	 *            The vec2fSize to set.
	 */
    public void setVec2fSize(SFVec2f p_vec2fSize) {
        m_vec2fSize = p_vec2fSize;
    }

    @Override
    public void init(X3dTile p_tile, float p_width, float p_length) {
        float x = p_width / p_tile.getWidth();
        float y = p_length / p_tile.getLength();
        m_vec2fScale = new SFVec2f(x, y);
        m_vec2fSize = new SFVec2f();
        float x1 = p_length;
        float x2 = p_width;
        m_vec2fSize.setValue(x1, x2);
    }
}
