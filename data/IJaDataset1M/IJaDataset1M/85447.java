package gestalt.impl.jogl.extension.picking;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import static gestalt.Gestalt.*;
import gestalt.context.GLContext;
import gestalt.extension.picking.Pickable;
import gestalt.extension.picking.Picker;
import gestalt.impl.jogl.context.JoglGLContext;
import gestalt.input.EventHandler;
import gestalt.util.JoglUtil;
import com.sun.opengl.util.BufferUtil;

public class JoglPicker extends Picker {

    private int _my2DBufferDepth;

    public JoglPicker() {
        _my2DBufferDepth = 20;
    }

    public void draw(GLContext theContext) {
        JoglGLContext myJoglContext = (JoglGLContext) theContext;
        selectOrthoPickables(myJoglContext);
        selectSpatialPickables(myJoglContext);
    }

    protected void selectSpatialPickables(JoglGLContext theRenderContext) {
        GL gl = theRenderContext.gl;
        GLU glu = theRenderContext.glu;
        ByteBuffer directBuffer = BufferUtil.newByteBuffer(128);
        ByteOrder.nativeOrder();
        IntBuffer myIntBuffer = directBuffer.asIntBuffer();
        float myMouseX = theRenderContext.event.mouseX;
        float myMouseY = theRenderContext.event.mouseY;
        if (EventHandler.EVENT_CENTER_MOUSE) {
            myMouseX += theRenderContext.displaycapabilities.width / 2;
            myMouseY += theRenderContext.displaycapabilities.height / 2;
        }
        if (!EventHandler.EVENT_FLIP_MOUSE_Y) {
            myMouseY = theRenderContext.displaycapabilities.height - myMouseY;
        }
        int[] myViewPort = new int[4];
        gl.glGetIntegerv(GL.GL_VIEWPORT, myViewPort, 0);
        gl.glSelectBuffer(myIntBuffer.capacity(), myIntBuffer);
        gl.glRenderMode(GL.GL_SELECT);
        gl.glInitNames();
        gl.glPushName(-1);
        if (theRenderContext.camera != null) {
            theRenderContext.camera.draw(theRenderContext);
        }
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        glu.gluPickMatrix(myMouseX, myMouseY, 2f, 2f, myViewPort, 0);
        if (theRenderContext.camera != null) {
            JoglUtil.gluPerspective(gl, theRenderContext.camera.fovy, (float) (theRenderContext.displaycapabilities.width) / (float) (theRenderContext.displaycapabilities.height), theRenderContext.camera.nearclipping, theRenderContext.camera.farclipping, theRenderContext.camera.frustumoffset);
        }
        gl.glMatrixMode(GL.GL_MODELVIEW);
        drawPickables(bin[PICKING_BIN_3D].getDataRef(), theRenderContext);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPopMatrix();
        int myHits = 0;
        myHits = gl.glRenderMode(GL.GL_RENDER);
        processPickedObjects(bin[PICKING_BIN_3D].getDataRef(), myHits, myIntBuffer);
    }

    protected void selectOrthoPickables(JoglGLContext theRenderContext) {
        GL gl = theRenderContext.gl;
        GLU glu = theRenderContext.glu;
        ByteBuffer directBuffer = BufferUtil.newByteBuffer(128);
        ByteOrder.nativeOrder();
        IntBuffer myIntBuffer = directBuffer.asIntBuffer();
        int myHits = 0;
        int[] myViewPort = new int[4];
        float myMouseX = theRenderContext.event.mouseX;
        float myMouseY = theRenderContext.event.mouseY;
        if (EventHandler.EVENT_CENTER_MOUSE) {
            myMouseX += theRenderContext.displaycapabilities.width / 2;
            myMouseY += theRenderContext.displaycapabilities.height / 2;
        }
        if (!EventHandler.EVENT_FLIP_MOUSE_Y) {
            myMouseY = theRenderContext.displaycapabilities.height - myMouseY;
        }
        gl.glGetIntegerv(GL.GL_VIEWPORT, myViewPort, 0);
        gl.glSelectBuffer(myIntBuffer.capacity(), myIntBuffer);
        gl.glRenderMode(GL.GL_SELECT);
        gl.glInitNames();
        gl.glPushName(-1);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        glu.gluPickMatrix(myMouseX, myMouseY, 2f, 2f, myViewPort, 0);
        if (theRenderContext.camera != null) {
            JoglUtil.gluPerspective(gl, CAMERA_A_HANDY_ANGLE, (float) (theRenderContext.displaycapabilities.width) / (float) (theRenderContext.displaycapabilities.height), theRenderContext.displaycapabilities.height - _my2DBufferDepth, theRenderContext.displaycapabilities.height + _my2DBufferDepth, theRenderContext.camera.frustumoffset);
        }
        gl.glMatrixMode(GL.GL_MODELVIEW);
        drawPickables(bin[PICKING_BIN_2D].getDataRef(), theRenderContext);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPopMatrix();
        myHits = gl.glRenderMode(GL.GL_RENDER);
        processPickedObjects(bin[PICKING_BIN_2D].getDataRef(), myHits, myIntBuffer);
    }

    protected void drawPickables(Pickable[] thePickables, JoglGLContext theContext) {
        if (thePickables != null) {
            for (int i = 0; i < thePickables.length; ++i) {
                if (thePickables[i] != null) {
                    int myName = i + 1;
                    theContext.gl.glLoadName(myName);
                    thePickables[i].pickDraw(theContext);
                }
            }
        }
    }

    private void processPickedObjects(Pickable[] thePickables, int theHits, IntBuffer theIntBuffer) {
        if (thePickables != null) {
            if (theHits > 0) {
                int[] myPickedObjects = new int[thePickables.length];
                int myArrayIndex = 0;
                for (int j = 0; j < theIntBuffer.limit(); j += 4) {
                    int myRefObject = theIntBuffer.get(j + 3);
                    if (myRefObject != 0) {
                        myPickedObjects[myArrayIndex] = myRefObject;
                        myArrayIndex++;
                    }
                }
                myArrayIndex = 0;
                for (int i = 0; i < thePickables.length; ++i) {
                    if (thePickables[i] != null) {
                        boolean isInPickedArray = false;
                        int myPickableIndex = 0;
                        for (int j = 0; j < myPickedObjects.length; j++) {
                            myPickableIndex = myPickedObjects[j];
                            if (myPickableIndex != 0) {
                                myPickableIndex -= 1;
                                if (i == myPickableIndex) {
                                    isInPickedArray = true;
                                }
                            }
                        }
                        if (isInPickedArray) {
                            if (thePickables[i].isPicked()) {
                                thePickables[i].mouseWithin();
                            } else {
                                thePickables[i].mouseEnter();
                            }
                        } else {
                            if (thePickables[i].isPicked()) {
                                thePickables[i].mouseLeave();
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < thePickables.length; ++i) {
                    if (thePickables[i] != null) {
                        if (thePickables[i].isPicked()) {
                            thePickables[i].mouseLeave();
                        }
                    }
                }
            }
        }
    }
}
