package gestalt.demo.processing;

import gestalt.Gestalt;
import gestalt.candidates.rendertotexture.JoglTexCreatorFBO_DepthRGBA;
import gestalt.p5.GestaltPlugIn;
import gestalt.p5.JoglProcessingFrameBufferObject;
import gestalt.shape.Plane;
import gestalt.shape.material.TexturePlugin;
import processing.core.PApplet;

/**
 * @deprecated this sketch seems to contain errors...
 */
public class UsingSketchesOnFBOs extends PApplet {

    private GestaltPlugIn gestalt;

    private Plane _myPlane;

    private static final boolean USE_FBO = true;

    public void setup() {
        System.err.println("### " + getClass().getSimpleName() + " seems to be broken since processing 148++.");
        size(512, 512, OPENGL);
        rectMode(CENTER);
        if (USE_FBO) {
            boolean MAKE_PROCESSING_FRIENDLY = false;
            gestalt = new GestaltPlugIn(this, MAKE_PROCESSING_FRIENDLY);
            gestalt.displaycapabilities().backgroundcolor.set(0.2f);
            final boolean PRESENTATION_MODE = false;
            TexturePlugin myFBO = sketchTexture(this, gestalt, 1024, 768, PRESENTATION_MODE);
            _myPlane = gestalt.drawablefactory().plane();
            _myPlane.material().addPlugin(myFBO);
            _myPlane.setPlaneSizeToTextureSize();
            gestalt.bin(Gestalt.BIN_3D).add(_myPlane);
        }
    }

    private JoglProcessingFrameBufferObject sketchTexture(final PApplet theParent, final GestaltPlugIn theGestalt, final int theWindowWidth, final int theWindowHeight, final boolean thePresentationModeFlag) {
        JoglProcessingFrameBufferObject myFBO = new JoglProcessingFrameBufferObject(theWindowWidth, theWindowHeight, new JoglTexCreatorFBO_DepthRGBA(), theGestalt, theParent, thePresentationModeFlag);
        return myFBO;
    }

    public void draw() {
        background(127, 255, 0);
        fill(255);
        stroke(0);
        strokeWeight(20);
        rect(mouseX, mouseY, 200, 200);
        if (USE_FBO) {
            _myPlane.rotation().x += 0.005f;
            _myPlane.rotation().y += 0.0033f;
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { UsingSketchesOnFBOs.class.getName() });
    }
}
