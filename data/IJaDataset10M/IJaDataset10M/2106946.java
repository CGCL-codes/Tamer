package fr.crnan.videso3d.formats.images;

import fr.crnan.videso3d.VidesoGLCanvas;
import gov.nasa.worldwindx.examples.util.SurfaceImageEditor;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.render.SurfaceImage;

/**
 * 
 * @author Bruno Spyckerelle
 * @version 0.1.2
 */
public class EditableSurfaceImage extends SurfaceImage {

    private String name;

    private SurfaceImageEditor editor;

    public EditableSurfaceImage(String name, Object imageSource, Sector sector, VidesoGLCanvas wwd) {
        super(imageSource, sector);
        this.editor = new SurfaceImageEditor(wwd, this);
        this.editor.setArmed(false);
        this.setName(name);
    }

    public EditableSurfaceImage(String name, SurfaceImage si, VidesoGLCanvas wwd) {
        this(name, si.getImageSource(), si.getSector(), wwd);
    }

    public SurfaceImageEditor getEditor() {
        return this.editor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
