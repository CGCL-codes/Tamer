package de.grogra.imp3d.glsl.material;

import java.util.Iterator;
import javax.media.opengl.GL;
import de.grogra.imp3d.ParallelProjection;
import de.grogra.imp3d.glsl.GLSLDisplay;
import de.grogra.imp3d.glsl.light.LightShaderConfiguration;
import de.grogra.imp3d.glsl.material.channel.GLSLChannelMap;
import de.grogra.imp3d.glsl.material.channel.Result;
import de.grogra.imp3d.glsl.utility.GLSLManagedShader;
import de.grogra.imp3d.glsl.utility.ShaderConfiguration;
import de.grogra.imp3d.shading.RGBAShader;
import de.grogra.ray.physics.Light;

/**
 * This class defines a state for a glsl-Shader, where individual ChannelMaps
 * may register Textures, TmpVariables or UniformVariables. It also may complete
 * a shader by querying all needed input from a GLSLCachedMaterial.
 * 
 * @author shi
 * 
 */
public class MaterialConfiguration extends ShaderConfiguration {

    public static final int IT_NORMAL = 0;

    public static final int IT_POSITION = 1;

    public static final int IT_SHININESS = 2;

    public static final int IT_DIFFUSE = 3;

    public static final int IT_SPECULAR = 4;

    public static final int IT_AMBIENT = 5;

    public static final int IT_EMISSIVE = 6;

    public static final int IT_TRANSPERENCY = 7;

    public static final int IT_DIFFUSE_TRANSPERENCY = 8;

    public static final int IT_TRANSPERENCY_SHININESS = 9;

    public static final int IT_PROLOGUE = 10;

    private boolean shaderAntialiasing = false;

    public boolean isShaderAntialiasing() {
        return shaderAntialiasing;
    }

    String getDerivates() {
        return "vec3 dpdv = normalize(dFdx(pos.xyz) * dFdy(uv.t) - dFdy(pos.xyz) * dFdx(uv.t));\n" + "vec3 dpdu = cross(dpdv, normalize(normal));\n" + "dpdv = cross(normalize(normal), dpdu);\n";
    }

    public String[] completeShader(Result[] input) {
        String s = "#version " + version + "\n" + "#extension GL_ARB_draw_buffers : enable\n";
        s += "uniform float inv_farplane;";
        for (int i = 0; i < uniform.size(); i++) s += uniform.elementAt(i);
        if (uniform.size() > 0) s += "\n";
        for (int i = 0; i < sampler.size(); i++) s += sampler.elementAt(i);
        if (sampler.size() > 0) s += "\n";
        for (int i = 0; i < customSampler.size(); i++) s += customSampler.elementAt(i);
        if (customSampler.size() > 0) s += "\n";
        s += "varying vec2 uv;\n";
        s += "varying vec3 normal;\n";
        s += "varying float depth;\n";
        s += "varying vec4 pos;\n";
        s += "varying vec3 n_pos;\n";
        s += "varying vec3 g_pos;\n\n";
        for (int i = 0; i < constVar.size(); i++) s += constVar.elementAt(i);
        if (constVar.size() > 0) s += "\n";
        Iterator<String> it = funcMap.values().iterator();
        while (it.hasNext()) {
            s += it.next();
        }
        if (funcMap.size() > 0) s += "\n";
        s += "float packToFloat(vec2 val) {\n" + " val = clamp(val, 0.0, 1.0);\n" + " val.x = floor(val.x * 127.5) * 0.125;\n" + " vec2 X = vec2(floor(val.x), fract(val.x));\n" + " return (X.y + ( floor(val.y * 127.5) * 0.0009765625) + (1.0)) * exp2(X.x);\n" + "}\n\n";
        s += "vec2 encodeNormal(vec3 normal){\n" + " float f = sqrt(8.0*normal.z+8.0);\n" + " return normal.xy / f + 0.5;\n" + "}\n\n";
        s += "void main() {\n";
        if (getBit(USE_DERIVATES)) s += getDerivates() + "\n";
        for (int i = 0; i < var.size(); i++) s += " " + var.elementAt(i);
        if (var.size() > 0) s += "\n";
        if (input[IT_PROLOGUE].toString().length() > 0) s += input[IT_PROLOGUE] + "\n";
        s += " vec3 n_normal = normalize(" + input[IT_NORMAL].convert(Result.ET_VEC3) + ");\n";
        s += " if(!gl_FrontFacing) n_normal *= -1.0;\n";
        s += " vec3 emissive = " + input[IT_EMISSIVE].convert(Result.ET_VEC3) + ";\n";
        s += " vec3 diff_transp = " + input[IT_DIFFUSE_TRANSPERENCY].convert(Result.ET_VEC3) + ";\n";
        s += " gl_FragData[0] = vec4(encodeNormal(n_normal), " + input[IT_SHININESS].reduce(Result.ET_FLOAT) + ", " + input[IT_TRANSPERENCY_SHININESS].reduce(Result.ET_FLOAT) + ");\n";
        s += " gl_FragData[1] = vec4(" + input[IT_DIFFUSE].convert(Result.ET_VEC3) + ",packToFloat(emissive.rg));\n";
        s += " gl_FragData[2] = vec4(" + input[IT_SPECULAR].convert(Result.ET_VEC3) + ", packToFloat(vec2(emissive.b, diff_transp.r)));\n";
        s += " gl_FragData[3] = vec4(" + input[IT_TRANSPERENCY].convert(Result.ET_VEC3) + ",packToFloat(diff_transp.gb));\n";
        s += "}";
        String[] code = { s };
        return code;
    }

    /**
	 * Should be implemented by ReferenceKey! using a Interface or such
	 */
    @Override
    protected boolean perInstance() {
        return (referenceKey.getClass() == RGBAShader.class);
    }

    ;

    @Override
    protected void setThisToOther(ShaderConfiguration other) {
        super.setThisToOther(other);
        assert (other instanceof MaterialConfiguration);
        this.shaderAntialiasing = ((MaterialConfiguration) other).shaderAntialiasing;
    }

    @Override
    public ShaderConfiguration clone() {
        MaterialConfiguration sc = new MaterialConfiguration();
        sc.setThisToOther(this);
        return sc;
    }

    ;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (shaderAntialiasing ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        MaterialConfiguration other = (MaterialConfiguration) obj;
        if (shaderAntialiasing != other.shaderAntialiasing) return false;
        return true;
    }

    @Override
    public void set(de.grogra.imp3d.glsl.OpenGLState glState, GLSLDisplay disp, Object obj) {
        assert (obj instanceof Light);
        set(obj);
        this.shaderAntialiasing = disp.isOptionShaderAntialiasing();
    }

    ;

    public GLSLChannelMap getDefaultInputChannel() {
        return new GLSLDefaultInput();
    }

    @Override
    public GLSLManagedShader getShaderByDefaultCollection(GLSLDisplay disp, Object reference) {
        return MaterialCollection.getGLSLManagedObject(reference);
    }

    @Override
    public void setupShader(GL gl, GLSLDisplay disp, int shaderNo) {
        super.setupShader(gl, disp, shaderNo);
        invFarPlaneLoc = gl.glGetUniformLocation(shaderNo, "inv_farplane");
    }

    int invFarPlaneLoc = -1;

    @Override
    public void setupDynamicUniforms(GL gl, GLSLDisplay disp, int shaderNo) {
        super.setupDynamicUniforms(gl, disp, shaderNo);
        gl.glUniform1f(invFarPlaneLoc, 1.f / disp.getView3D().getCamera().getZFar());
    }
}
