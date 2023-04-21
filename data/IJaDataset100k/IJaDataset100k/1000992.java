package drawer;

import java.nio.FloatBuffer;

abstract class AbstractDynamicVertexSetFactory {

    abstract DynamicVertexSet newVertexSet(float[] array, int mode);

    abstract DynamicVertexSet newVertexSet(FloatBuffer floatBuffer, int mode);

    abstract DynamicVertexSet newVertexSet(IVertexSet vertexSet, int mode);
}
