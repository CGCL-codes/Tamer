package org.xith3d.render.prerender;

import org.xith3d.render.shader.Shader;

/**
 * A state sortable map contains a list of StateSortable items for which an atom
 * may have. The index into this map is the stateType, and the values in the map
 * are StateSortable items.<br>
 * <br>
 * Each RenderBucket has a StateSortableMap and when an atom is assigned to that
 * bucket the atom is interrogated for its sortable states. This is then used in
 * conjuction with the state priority map to sort the atoms properly for
 * rendering,
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus) [code cleaning]
 */
public class StateSortableMap {

    protected StateSortable[] map = new StateSortable[Shader.MAX_SHADER_TYPES];

    protected long[] mapID = new long[Shader.MAX_SHADER_TYPES];

    protected long hash;

    public StateSortableMap() {
    }

    public void calcHash() {
        hash = 0;
        for (int i = 0; i < mapID.length; i++) hash = hash | (mapID[i] << (8 * i));
    }
}
