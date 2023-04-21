package gestalt.demo.processing;

import gestalt.Gestalt;
import gestalt.candidates.JoglFrameBufferCopy;
import gestalt.model.Model;
import gestalt.model.ModelData;
import gestalt.model.ModelLoaderOBJ;
import gestalt.p5.GestaltPlugIn;
import gestalt.shape.Mesh;
import gestalt.shape.material.TexturePlugin;
import gestalt.texture.bitmap.IntegerBitmap;
import gestalt.util.CameraMover;
import data.Resource;
import processing.core.PApplet;

/** @todo
 * there are still some issues. for example lighting doesn t properly work yet.
 */
public class UsingModelAsSketchCanvas extends PApplet {

    private GestaltPlugIn gestalt;

    private Model _myModel;

    public void setup() {
        size(1024, 512, OPENGL);
        ellipseMode(CENTER);
        smooth();
        gestalt = new GestaltPlugIn(this);
        gestalt.framesetup().depthbufferclearing = false;
        gestalt.framesetup().colorbufferclearing = false;
        TexturePlugin myTexture = gestalt.drawablefactory().texture();
        myTexture.load(IntegerBitmap.getDefaultImageBitmap(width, height));
        ModelData myModelData = ModelLoaderOBJ.getModelData(Resource.getStream("demo/common/weirdobject.obj"));
        Mesh myModelMesh = gestalt.drawablefactory().mesh(true, myModelData.vertices, 3, myModelData.vertexColors, 4, myModelData.texCoordinates, 2, myModelData.normals, myModelData.primitive);
        _myModel = gestalt.drawablefactory().model(myModelData, myModelMesh);
        _myModel.mesh().material().addPlugin(myTexture);
        myTexture.setWrapMode(Gestalt.TEXTURE_WRAPMODE_CLAMP);
        myTexture.scale().y = 1;
        _myModel.mesh().material().lit = true;
        _myModel.mesh().material().transparent = false;
        gestalt.bin(Gestalt.BIN_3D).add(_myModel);
        gestalt.camera().setMode(Gestalt.CAMERA_MODE_LOOK_AT);
        gestalt.camera().position().set(150, 100, 450);
        gestalt.light().enable = true;
        gestalt.light().setPositionRef(gestalt.camera().position());
        JoglFrameBufferCopy myFrameBufferCopy = new JoglFrameBufferCopy(myTexture);
        myFrameBufferCopy.backgroundcolor.set(0.2f, 1);
        myFrameBufferCopy.width = width;
        myFrameBufferCopy.height = height;
        gestalt.bin(Gestalt.BIN_3D_SETUP).add(myFrameBufferCopy);
    }

    public void draw() {
        background(0, 255, 127);
        fill(255, 255);
        stroke(0, 255);
        strokeWeight(20);
        ellipse(mouseX, mouseY, 200, 200);
        final float myDeltaTime = 1 / 30f;
        CameraMover.handleKeyEvent(gestalt.camera(), gestalt.event(), myDeltaTime);
        gestalt.camera().side(myDeltaTime * 20);
        if (gestalt.event().mouseDown) {
            if (gestalt.event().mouseButton == Gestalt.MOUSEBUTTON_LEFT) {
                gestalt.enable();
            } else {
                gestalt.disable();
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { UsingModelAsSketchCanvas.class.getName() });
    }
}
