package imi.avatarloaders.xml;

import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.CombinerFunctionAlpha;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.image.Texture.WrapMode;
import com.jme.scene.state.CullState.Face;
import com.jme.scene.state.MaterialState.ColorMaterial;
import imi.repository.MaterialRepoComponent;
import imi.repository.RRL;
import imi.repository.Repository;
import imi.scene.PMatrix;
import imi.scene.polygonmodel.PMeshMaterial;
import imi.scene.polygonmodel.PMeshMaterial.TransparencyType;
import imi.scene.polygonmodel.TextureMaterialProperties;
import imi.shader.AbstractShaderProgram;
import imi.shader.NoSuchPropertyException;
import imi.shader.ShaderProperty;
import imi.shader.ShaderUtils;
import imi.shader.dynamic.GLSLDataType;
import imi.shader.dynamic.GLSLShaderEffect;
import imi.shader.dynamic.GLSLShaderProgram;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.mtgame.WorldManager;

/**
 * XML related helper calls
 * @author Lou Hayt
 */
public class DOMUtils {

    /**
     * Construct a new material from the provided DOM
     * @param xmlMat A non-null DOM to use
     * @param wm WorldManager ref
     * @param componentType The class of the module owning this
     */
    public static PMeshMaterial createMeshMaterial(xmlMaterial xmlMat, WorldManager wm) {
        PMeshMaterial mat = new PMeshMaterial(xmlMat.getName());
        applyMeshMaterialDOM(mat, xmlMat, wm);
        return mat;
    }

    private static void applyMeshMaterialDOM(PMeshMaterial mat, xmlMaterial xmlMat, WorldManager wm) {
        int counter = 0;
        for (xmlTextureAttributes texAttr : xmlMat.getTextures()) {
            mat.setTexture(createTextureMaterialProperties(texAttr), counter);
            counter++;
        }
        PMeshMaterial.MaterialStateProperties materialStateProps = new PMeshMaterial.MaterialStateProperties();
        materialStateProps.setShininess(xmlMat.getShininess());
        if (xmlMat.getColorMaterial() != null) materialStateProps.setColorMaterial(ColorMaterial.valueOf(xmlMat.getColorMaterial()));
        if (xmlMat.getDiffuseColor() != null) materialStateProps.setDiffuse(xmlMat.getDiffuseColor().getColorRGBA());
        if (xmlMat.getAmbientColor() != null) materialStateProps.setAmbient(xmlMat.getAmbientColor().getColorRGBA());
        if (xmlMat.getEmissiveColor() != null) materialStateProps.setEmissive(xmlMat.getEmissiveColor().getColorRGBA());
        if (xmlMat.getSpecularColor() != null) materialStateProps.setSpecular(xmlMat.getSpecularColor().getColorRGBA());
        mat.setMaterialStateProperties(materialStateProps);
        counter = 0;
        mat.clearShaders();
        for (xmlShader shaderDOM : xmlMat.getShaders()) {
            AbstractShaderProgram shader = createShader(shaderDOM.getProgram(), wm);
            applyMeshMaterialShaderProperties(shaderDOM.getProperties(), shader);
            mat.addShader(shader);
            counter++;
        }
        mat.setCullFace(Face.valueOf(xmlMat.getCullFace()));
        if (xmlMat.getName() != null) mat.setName(xmlMat.getName());
        if (xmlMat.getAlphaState() == 0) mat.setTransparencyType(TransparencyType.NO_TRANSPARENCY); else if (xmlMat.getAlphaState() == 1) mat.setTransparencyType(TransparencyType.ALPHA_ENABLED);
    }

    /**
     * Apply the provided property list to the provided shader
     * @param properties
     * @param shader
     */
    private static void applyMeshMaterialShaderProperties(List<xmlShaderProperty> properties, AbstractShaderProgram shader) {
        for (xmlShaderProperty xmlProp : properties) {
            ShaderProperty prop = createShaderProperty(xmlProp);
            try {
                shader.setProperty(prop);
            } catch (NoSuchPropertyException ex) {
                Logger.getLogger(PMeshMaterial.class.getName()).log(Level.WARNING, "Unknown property read from XML file! : " + ex.getMessage());
            }
        }
    }

    private static TextureMaterialProperties createTextureMaterialProperties(xmlTextureAttributes texAttr) {
        TextureMaterialProperties tmp = new TextureMaterialProperties();
        applyTextureAttributesDOM(tmp, texAttr);
        return tmp;
    }

    public static xmlTextureAttributes generateTexturePropertiesDOM(TextureMaterialProperties tmp) {
        xmlTextureAttributes result = new xmlTextureAttributes();
        if (tmp.getImageLocation() != null) result.setRelativePath(tmp.getImageLocation().getRelativePath().toString()); else result.setRelativePath(null);
        result.setTextureUnit(tmp.getTextureUnit());
        result.setWrapS(tmp.getWrapS().toString());
        result.setWrapT(tmp.getWrapT().toString());
        result.setAlphaCombiner(tmp.getAlphaCombineMode().toString());
        result.setMinificationFilter(tmp.getMinFilter().toString());
        result.setMagnificationFilter(tmp.getMagFilter().toString());
        result.setAnisotropicValue(tmp.getAnistotropicValue());
        result.setTextureApplyMode(tmp.getApplyMode().toString());
        return result;
    }

    private static void applyTextureAttributesDOM(TextureMaterialProperties tmp, xmlTextureAttributes texAttr) {
        try {
            tmp.setImageLocation(new RRL(texAttr.getRelativePath()));
            tmp.setTextureUnit(texAttr.getTextureUnit());
            tmp.setWrapS(WrapMode.valueOf(texAttr.getWrapS()));
            tmp.setWrapT(WrapMode.valueOf(texAttr.getWrapT()));
            tmp.setAlphaCombineMode(CombinerFunctionAlpha.valueOf(texAttr.getAlphaCombiner()));
            tmp.setMinFilter(MinificationFilter.valueOf(texAttr.getMinificationFilter()));
            tmp.setMagFilter(MagnificationFilter.valueOf(texAttr.getMagnificationFilter()));
            tmp.setAnistotropicValue(texAttr.getAnisotropicValue());
            tmp.setApplyMode(ApplyMode.valueOf(texAttr.getTextureApplyMode()));
        } catch (Exception ex) {
            Logger.getLogger(TextureMaterialProperties.class.getName()).log(Level.SEVERE, "Error applying DOM! - " + ex.getMessage());
        }
    }

    public static ShaderProperty createShaderProperty(xmlShaderProperty xmlProp) {
        ShaderProperty sp = new ShaderProperty(xmlProp.getPropertyName());
        applyShaderPropertyDOM(sp, xmlProp);
        return sp;
    }

    public static xmlShaderProperty generateShaderPropertyDOM(ShaderProperty sp) {
        xmlShaderProperty result = new xmlShaderProperty();
        result.setPropertyName(sp.getName());
        result.setType(sp.getType().toString());
        if (sp.getType().getJavaType() == float[].class) {
            StringBuilder sb = new StringBuilder();
            for (float f : (float[]) sp.getValue()) sb.append(" " + f);
            result.setValue(sb.toString());
        } else if (sp.getType().getJavaType() == int[].class) {
            StringBuilder sb = new StringBuilder();
            for (int i : (int[]) sp.getValue()) sb.append(" " + i);
            result.setValue(sb.toString());
        } else result.setValue(sp.getValue().toString());
        return result;
    }

    private static void applyShaderPropertyDOM(ShaderProperty sp, xmlShaderProperty xmlProp) {
        GLSLDataType type = GLSLDataType.valueOf(xmlProp.getType());
        sp.setType(type);
        sp.setValue(ShaderUtils.parseStringValue(xmlProp.getValue(), type));
    }

    /**
     * Factory method to aid in shader reconstitution.
     * @param shaderDOM
     * @return
     */
    public static AbstractShaderProgram createShader(xmlShaderProgram shaderDOM, WorldManager wm) {
        AbstractShaderProgram result = null;
        if (shaderDOM.getDefaultProgramName() != null) {
            MaterialRepoComponent matRepo = Repository.get().getRepositoryComponent(MaterialRepoComponent.class);
            try {
                Class classz = Class.forName(shaderDOM.getDefaultProgramName());
                result = matRepo.newShader(classz);
            } catch (Exception ex) {
                Logger.getLogger("DOMUtils").log(Level.SEVERE, "Error recreating default shader:", ex);
            }
        } else {
            GLSLShaderProgram program = new GLSLShaderProgram(wm);
            for (String effectName : shaderDOM.getListOfEffects()) {
                try {
                    Class classz = Class.forName("imi.shader.effects." + effectName);
                    Constructor ctor = classz.getConstructor();
                    program.addEffect((GLSLShaderEffect) ctor.newInstance());
                } catch (Exception ex) {
                    Logger.getLogger("DOMUtils").log(Level.SEVERE, "Error creating shader effect!", ex);
                }
            }
            try {
                program.compile();
            } catch (Exception ex) {
                Logger.getLogger("DOMUtils").log(Level.SEVERE, "Error compiling shader!", ex);
            }
            result = program;
        }
        return result;
    }

    /**
     * Build and retrieve the shader program dom representation.
     * @return
     */
    public static xmlShaderProgram generateShaderProgramDOM(AbstractShaderProgram shader) {
        xmlShaderProgram result = new xmlShaderProgram();
        if (!(shader instanceof GLSLShaderProgram)) result.setDefaultProgramName(shader.getClass().getName()); else {
            GLSLShaderProgram GLSLShader = (GLSLShaderProgram) shader;
            Constructor defaultCtor = null;
            GLSLShaderProgram vanillaInstance = null;
            try {
                defaultCtor = GLSLShader.getClass().getConstructor(WorldManager.class);
                vanillaInstance = (GLSLShaderProgram) defaultCtor.newInstance(WorldManager.getDefaultWorldManager());
            } catch (NoSuchMethodException ex) {
            } catch (SecurityException ex) {
                Logger.getLogger(GLSLShaderProgram.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(GLSLShaderProgram.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(GLSLShaderProgram.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(GLSLShaderProgram.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(GLSLShaderProgram.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
            if (GLSLShader.containsSameEffectsAs(vanillaInstance)) result.setDefaultProgramName(GLSLShader.getClass().getName()); else constructManualShaderDOM(GLSLShader, result);
        }
        return result;
    }

    public static xmlShaderProgram generateShaderProgramDOMDerrived(AbstractShaderProgram shader) {
        xmlShaderProgram result = new xmlShaderProgram();
        result.setDefaultProgramName(shader.getClass().getName());
        return result;
    }

    /**
     * This method fills out the provided shader program dom with the
     * appropriate list of constituent effects.
     * @param shaderDOM
     */
    private static void constructManualShaderDOM(GLSLShaderProgram shader, xmlShaderProgram shaderDOM) {
        shaderDOM.setDefaultProgramName(null);
        for (GLSLShaderEffect effect : shader.getEffects()) shaderDOM.addEffect(effect.getClass().getSimpleName());
    }

    public static void setMatrixDOM(PMatrix mat, xmlMatrix matDOM) {
        float[] matrix = new float[16];
        mat.getFloatArray(matrix);
        xmlFloatRow row = new xmlFloatRow();
        row.setValues(matrix[0], matrix[1], matrix[2], matrix[3]);
        matDOM.setRowOne(row);
        row = new xmlFloatRow();
        row.setValues(matrix[4], matrix[5], matrix[6], matrix[7]);
        matDOM.setRowTwo(row);
        row = new xmlFloatRow();
        row.setValues(matrix[8], matrix[9], matrix[10], matrix[11]);
        matDOM.setRowThree(row);
        row = new xmlFloatRow();
        row.setValues(matrix[12], matrix[13], matrix[14], matrix[15]);
        matDOM.setRowFour(row);
    }

    public static void getMatrixFromDOM(PMatrix mat, xmlMatrix matDOM) {
        float[] fArray = new float[16];
        fArray[0] = matDOM.getRowOne().x;
        fArray[1] = matDOM.getRowOne().y;
        fArray[2] = matDOM.getRowOne().z;
        fArray[3] = matDOM.getRowOne().w;
        fArray[4] = matDOM.getRowTwo().x;
        fArray[5] = matDOM.getRowTwo().y;
        fArray[6] = matDOM.getRowTwo().z;
        fArray[7] = matDOM.getRowTwo().w;
        fArray[8] = matDOM.getRowThree().x;
        fArray[9] = matDOM.getRowThree().y;
        fArray[10] = matDOM.getRowThree().z;
        fArray[11] = matDOM.getRowThree().w;
        fArray[12] = matDOM.getRowFour().x;
        fArray[13] = matDOM.getRowFour().y;
        fArray[14] = matDOM.getRowFour().z;
        fArray[15] = matDOM.getRowFour().w;
        mat.set(fArray);
    }
}
