package gestalt.impl.jogl.shape;

import javax.media.opengl.GL;
import gestalt.context.GLContext;
import gestalt.impl.jogl.context.JoglGLContext;
import gestalt.shape.Mesh;
import gestalt.util.JoglUtil;

public class JoglMesh extends Mesh {

    public JoglMesh(float[] theVertices, int theVertexComponents, float[] theColors, int theColorComponents, float[] theTexCoords, int theTexCoordComponents, float[] theNormals, int thePrimitive) {
        super(theVertices, theVertexComponents, theColors, theColorComponents, theTexCoords, theTexCoordComponents, theNormals, thePrimitive);
        material = new JoglMaterial();
        checkDataIntegrity();
    }

    public void setPrimitive(int theGestaltPrimitive) {
        _myPrimitive = JoglUtil.mapGestaltPrimitiveToOpenGLPrimitive(theGestaltPrimitive);
        checkDataIntegrity();
    }

    public int getPrimitive() {
        return JoglUtil.mapOpenGLPrimitiveToGestaltPrimitive(_myPrimitive);
    }

    public void updateData() {
    }

    public boolean checkDataIntegrity() {
        if (_myVertices == null) {
            System.err.print("### WARNING @ JoglMesh / problems with data reference");
            System.err.println("/ vertex data is 'null'");
            return false;
        }
        if (_myNumberOfAtoms * _myNumberOfVertexComponents != _myVertices.length) {
            System.err.print("### WARNING @ JoglMesh / problems with data integrity ");
            System.err.println("/ vertex");
            return false;
        }
        if ((_myColors != null) && (_myColors.length != 0) && (_myColors.length / _myNumberOfColorComponents != _myNumberOfAtoms)) {
            System.err.print("### WARNING @ JoglMesh / problems with data integrity ");
            System.err.println("/ color");
            return false;
        }
        if ((_myTexCoords != null) && (_myTexCoords.length != 0) && (_myTexCoords.length / _myNumberOfTexCoordComponents != _myNumberOfAtoms)) {
            System.err.print("### WARNING @ JoglMesh / problems with data integrity ");
            System.err.println("/ texture coordinates");
            return false;
        }
        if ((_myNormals != null) && (_myNormals.length != 0) && (_myNormals.length / NUMBER_OF_NORMAL_COMPONENTS != _myNumberOfAtoms)) {
            System.err.print("### WARNING @ JoglMesh / problems with data integrity ");
            System.err.println("/ normals");
            return false;
        }
        return true;
    }

    public void draw(final GLContext theRenderContext) {
        final GL gl = ((JoglGLContext) theRenderContext).gl;
        if (material != null) {
            material.begin(theRenderContext);
        }
        gl.glPushMatrix();
        JoglUtil.applyTransform(gl, _myTransformMode, transform, rotation, scale);
        int myNormalIndex = _myDrawStart * NUMBER_OF_NORMAL_COMPONENTS;
        int myTexCoordIndex = _myDrawStart * _myNumberOfTexCoordComponents;
        int myColorIndex = _myDrawStart * _myNumberOfColorComponents;
        int myVertexIndex = _myDrawStart * _myNumberOfVertexComponents;
        gl.glBegin(_myPrimitive);
        for (int i = 0; i < _myDrawLength; i++) {
            if (_myNormals != null && _myNormals.length != 0) {
                gl.glNormal3f(_myNormals[myNormalIndex], _myNormals[myNormalIndex + 1], _myNormals[myNormalIndex + 2]);
                myNormalIndex += NUMBER_OF_NORMAL_COMPONENTS;
            }
            if (_myTexCoords != null && _myTexCoords.length != 0 && material != null && !material.disableTextureCoordinates) {
                if (_myNumberOfTexCoordComponents == 2) {
                    gl.glTexCoord2f(_myTexCoords[myTexCoordIndex], _myTexCoords[myTexCoordIndex + 1]);
                } else if (_myNumberOfTexCoordComponents == 1) {
                    gl.glTexCoord1f(_myTexCoords[myTexCoordIndex]);
                } else if (_myNumberOfTexCoordComponents == 3) {
                    gl.glTexCoord3f(_myTexCoords[myTexCoordIndex], _myTexCoords[myTexCoordIndex + 1], _myTexCoords[myTexCoordIndex + 2]);
                }
                myTexCoordIndex += _myNumberOfTexCoordComponents;
            }
            if (_myColors != null && _myColors.length != 0) {
                if (_myNumberOfColorComponents == 3) {
                    gl.glColor3f(_myColors[myColorIndex], _myColors[myColorIndex + 1], _myColors[myColorIndex + 2]);
                } else if (_myNumberOfColorComponents == 4) {
                    gl.glColor4f(_myColors[myColorIndex], _myColors[myColorIndex + 1], _myColors[myColorIndex + 2], _myColors[myColorIndex + 3]);
                }
                myColorIndex += _myNumberOfColorComponents;
            }
            if (_myNumberOfVertexComponents == 3) {
                gl.glVertex3f(_myVertices[myVertexIndex], _myVertices[myVertexIndex + 1], _myVertices[myVertexIndex + 2]);
            } else if (_myNumberOfVertexComponents == 2) {
                gl.glVertex2f(_myVertices[myVertexIndex], _myVertices[myVertexIndex + 1]);
            } else if (_myNumberOfVertexComponents == 4) {
                gl.glVertex4f(_myVertices[myVertexIndex], _myVertices[myVertexIndex + 1], _myVertices[myVertexIndex + 2], _myVertices[myVertexIndex + 3]);
            }
            myVertexIndex += _myNumberOfVertexComponents;
        }
        gl.glEnd();
        gl.glPopMatrix();
        if (material != null) {
            material.end(theRenderContext);
        }
    }

    public void dispose(GLContext theRenderContext) {
    }
}
