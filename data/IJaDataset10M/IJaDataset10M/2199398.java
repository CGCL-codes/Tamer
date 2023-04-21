package com.jogamp.graph.curve.opengl;

import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLUniformData;
import jogamp.graph.curve.opengl.RenderStateImpl;
import jogamp.graph.curve.opengl.shader.UniformNames;
import com.jogamp.common.os.Platform;
import com.jogamp.graph.geom.Vertex;
import com.jogamp.opengl.util.PMVMatrix;
import com.jogamp.opengl.util.glsl.ShaderState;

public abstract class RenderState {

    public static RenderState createRenderState(ShaderState st, Vertex.Factory<? extends Vertex> pointFactory) {
        return new RenderStateImpl(st, pointFactory);
    }

    public static RenderState createRenderState(ShaderState st, Vertex.Factory<? extends Vertex> pointFactory, PMVMatrix pmvMatrix) {
        return new RenderStateImpl(st, pointFactory, pmvMatrix);
    }

    protected final ShaderState st;

    protected final Vertex.Factory<? extends Vertex> vertexFactory;

    protected final PMVMatrix pmvMatrix;

    protected final GLUniformData gcu_PMVMatrix;

    protected RenderState(ShaderState st, Vertex.Factory<? extends Vertex> vertexFactory, PMVMatrix pmvMatrix) {
        this.st = st;
        this.vertexFactory = vertexFactory;
        this.pmvMatrix = pmvMatrix;
        this.gcu_PMVMatrix = new GLUniformData(UniformNames.gcu_PMVMatrix, 4, 4, pmvMatrix.glGetPMvMatrixf());
        st.ownUniform(gcu_PMVMatrix);
    }

    public final ShaderState getShaderState() {
        return st;
    }

    public final Vertex.Factory<? extends Vertex> getVertexFactory() {
        return vertexFactory;
    }

    public final PMVMatrix pmvMatrix() {
        return pmvMatrix;
    }

    public final GLUniformData getPMVMatrix() {
        return gcu_PMVMatrix;
    }

    public void destroy(GL2ES2 gl) {
        st.destroy(gl);
    }

    public abstract GLUniformData getWeight();

    public abstract GLUniformData getAlpha();

    public abstract GLUniformData getColorStatic();

    public final RenderState attachTo(GL2ES2 gl) {
        return (RenderState) gl.getContext().attachObject(RenderState.class.getName(), this);
    }

    public final boolean detachFrom(GL2ES2 gl) {
        RenderState _rs = (RenderState) gl.getContext().getAttachedObject(RenderState.class.getName());
        if (_rs == this) {
            gl.getContext().detachObject(RenderState.class.getName());
            return true;
        }
        return false;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (null == sb) {
            sb = new StringBuilder();
        }
        sb.append("RenderState[");
        st.toString(sb).append(Platform.getNewline());
        sb.append("]");
        return sb;
    }

    public String toString() {
        return toString(null).toString();
    }
}
