package jemfis.fs;

import java.io.File;
import org.ogre4j.IOverlay;
import org.ogre4j.ISceneNode;

/** 
 *
 * @author Max Vyaznikov
 */
public class FSObject {

    protected ISceneNode sceneNode;

    protected File file;

    protected IOverlay label;

    public FSObject(ISceneNode sceneNode, File file) {
        this.sceneNode = sceneNode;
        this.file = file;
    }

    public void setLabel(IOverlay label) {
        this.label = label;
    }

    public IOverlay getLabel() {
        return label;
    }

    public ISceneNode getSceneNode() {
        return sceneNode;
    }

    public File getFile() {
        return file;
    }
}
