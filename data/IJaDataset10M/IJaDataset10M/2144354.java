package org.jcrpg.threed.scene.side;

import java.util.ArrayList;
import org.jcrpg.threed.scene.model.Model;
import org.jcrpg.util.HashUtil;

/**
 * coordinate hash altered objects
 * @author pali
 *
 */
public class RenderedHashAlteredSide extends RenderedSide {

    public boolean scaleFix = false;

    Model[][] alteredObjectLists;

    Model[][] alteredSteepObjectLists;

    Model[] returnModels = null;

    /**
	 * @param objects Objects always rendered.
	 */
    public RenderedHashAlteredSide(Model[] objects, Model[][] alteredObjectLists, Model[][] alteredSteepObjectLists) {
        super(objects);
        this.alteredObjectLists = alteredObjectLists;
        this.alteredSteepObjectLists = alteredSteepObjectLists;
        type = RS_HASHALTERED;
    }

    public RenderedHashAlteredSide(Model[] objects, Model[][] alteredObjectLists) {
        super(objects);
        this.alteredObjectLists = alteredObjectLists;
        this.alteredSteepObjectLists = alteredObjectLists;
        type = RS_HASHALTERED;
    }

    public RenderedHashAlteredSide(Model[] objects, Model[][] alteredObjectLists, Model[][] alteredSteepObjectLists, boolean scaleFix) {
        super(objects);
        this.alteredObjectLists = alteredObjectLists;
        this.scaleFix = scaleFix;
        type = RS_HASHALTERED;
    }

    public Model[] getRenderedModels(int x, int y, int z, boolean steep) {
        ArrayList<Model> models = new ArrayList<Model>();
        Model[][] lists = steep ? alteredSteepObjectLists : alteredObjectLists;
        for (int i = 0; i < lists.length; i++) {
            Model[] objectList = lists[i];
            int p = HashUtil.mix(x + i, y, z) % objectList.length;
            models.add(objectList[p]);
        }
        returnModels = models.toArray(new Model[0]);
        return returnModels;
    }

    public float scale(int x, int y, int z) {
        if (scaleFix) return 1f;
        return 1f + (HashUtil.mix(x, y, z) % 100) * 0.003f;
    }
}
