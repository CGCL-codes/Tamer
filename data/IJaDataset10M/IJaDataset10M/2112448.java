package drawer;

import java.nio.FloatBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

class DynamicVertexSetFactory extends AbstractDynamicVertexSetFactory {

    private AbstractDynamicVertexSetFactory delegate;

    static boolean multiDrawSupported;

    DynamicVertexSetFactory() {
        final GL gl = GLU.getCurrentGL();
        DynamicVertexSetFactory.multiDrawSupported = gl.isFunctionAvailable("glMultiDrawArrays");
        if ((gl.isExtensionAvailable("GL_ARB_vertex_buffer_object") || gl.isExtensionAvailable("GL_EXT_vertex_buffer_object")) && (gl.isFunctionAvailable("glBindBufferARB") || gl.isFunctionAvailable("glBindBuffer")) && (gl.isFunctionAvailable("glBufferDataARB") || gl.isFunctionAvailable("glBufferData")) && (gl.isFunctionAvailable("glDeleteBuffersARB") || gl.isFunctionAvailable("glDeleteBuffers")) && (gl.isFunctionAvailable("glGenBuffersARB") || gl.isFunctionAvailable("glGenBuffers"))) delegate = new DynamicVertexBufferObjectFactory(); else if (gl.isExtensionAvailable("GL_EXT_vertex_array") && gl.isFunctionAvailable("glColorPointer") && gl.isFunctionAvailable("glDrawArrays") && gl.isFunctionAvailable("glDrawElements") && gl.isFunctionAvailable("glDrawRangeElements") && gl.isFunctionAvailable("glIndexPointer") && gl.isFunctionAvailable("glNormalPointer") && gl.isFunctionAvailable("glTexCoordPointer") && gl.isFunctionAvailable("glVertexPointer")) delegate = new VertexArrayFactory(); else delegate = new DynamicDefaultVertexSetFactory();
    }

    @Override
    DynamicVertexSet newVertexSet(float[] array, int mode) {
        return (delegate.newVertexSet(array, mode));
    }

    @Override
    DynamicVertexSet newVertexSet(FloatBuffer floatBuffer, int mode) {
        return (delegate.newVertexSet(floatBuffer, mode));
    }

    @Override
    DynamicVertexSet newVertexSet(IVertexSet vertexSet, int mode) {
        return (delegate.newVertexSet(vertexSet, mode));
    }
}
