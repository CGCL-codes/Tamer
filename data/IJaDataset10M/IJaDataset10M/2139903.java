package imi.shader.programs;

import imi.shader.ShaderProperty;
import imi.shader.dynamic.GLSLDataType;
import imi.shader.dynamic.GLSLShaderProgram;
import imi.shader.effects.CalculateToLight_Lighting;
import imi.shader.effects.GenerateFragLocalNormal;
import imi.shader.effects.VertexGlobalsAssignment;
import imi.shader.effects.SimpleNdotL_Lighting;
import imi.shader.effects.SingleTextureSample;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.mtgame.WorldManager;

/**
 * This class represents the basic transform and lighting
 * shader
 * @author Ronald E Dahlgren
 */
public class SimpleTNLShader extends GLSLShaderProgram implements Serializable {

    /** Serialization version number **/
    private static final long serialVersionUID = 1l;

    /**
     * Construct a brand new instance!
     * @param wm
     */
    public SimpleTNLShader(WorldManager wm) {
        super(wm, true);
        setProgramName(this.getClass().getSimpleName());
        setProgramDescription("This program performs basic lighting with a single texture");
        addEffect(new VertexGlobalsAssignment());
        addEffect(new SingleTextureSample());
        addEffect(new CalculateToLight_Lighting());
        addEffect(new GenerateFragLocalNormal());
        addEffect(new SimpleNdotL_Lighting());
        try {
            compile();
            setProperty(new ShaderProperty("DiffuseMapIndex", GLSLDataType.GLSL_SAMPLER2D, Integer.valueOf(0)));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Caught " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
