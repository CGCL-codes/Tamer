package imi.shader;

import imi.utils.Cosmic;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.jdesktop.mtgame.WorldManager;

/**
 * This class controls creation of shader programs. This is necessary in order
 * to have all shader creation occur in one code location, as well as making
 * sure that the default shaders are only compiled once.
 * @author Ronald E Dahlgren
 */
public class ShaderFactory {

    /**
     * Control factory behavior
     */
    public enum CreationalBehavior {

        Normal, NoRenderStates
    }

    /** Logger ref **/
    private static final Logger logger = Logger.getLogger(ShaderFactory.class.getName());

    /** Collection of Prototypes **/
    private final Map<Class, AbstractShaderProgram> m_prototypes = new HashMap<Class, AbstractShaderProgram>();

    /** WorldManager, needed for creating shader programs **/
    private WorldManager m_wm = null;

    /**
     * Construct a new ShaderFactory
     */
    public ShaderFactory() {
        m_wm = Cosmic.getWorldManager();
    }

    public AbstractShaderProgram newShader(Class shaderType) {
        return this.newShader(shaderType, CreationalBehavior.Normal);
    }

    public AbstractShaderProgram newShader(Class<? extends AbstractShaderProgram> shaderType, CreationalBehavior mode) {
        AbstractShaderProgram result = null;
        synchronized (m_prototypes) {
            result = m_prototypes.get(shaderType);
        }
        if (result == null) {
            try {
                Constructor cons = shaderType.getConstructor(WorldManager.class);
                result = (AbstractShaderProgram) cons.newInstance(m_wm);
                synchronized (m_prototypes) {
                    m_prototypes.put(shaderType, result);
                }
            } catch (Exception ex) {
                throw new RuntimeException("Could not instantiate " + shaderType, ex);
            }
        }
        result = result.duplicate();
        return result;
    }
}
