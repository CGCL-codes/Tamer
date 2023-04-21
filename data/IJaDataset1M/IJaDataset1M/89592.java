package jung.refact;

import java.util.Map;
import java.util.HashMap;
import jung.ext.utils.BasicInterface;
import jung.ext.utils.BasicUtils;

public class MouseModes implements BasicInterface {

    private static final String ID = MouseModes.class.getName();

    private Map modes;

    public MouseModes() {
        modes = new HashMap(5);
    }

    /**
   * The method <code>setMode</code> sets mode information using a 
   * <code>MouseModeName</code> as identifier. Old <code>PeirceMutatorMode</code>
   * information is overwritten. 
   */
    public void setMode(MouseModeName name, MouseMode mode) {
        modes.put(name, mode);
    }

    /**
   * The function <code>getMode</code> returns mode information using a 
   * <code>MouseModeName</code> as identifier. The function will throw
   * an error when the mode isn't set before.
   * @see containsMode
   */
    public MouseMode getMode(MouseModeName name) {
        return (MouseMode) modes.get(name);
    }

    /**
   * The function <code>containsMode</code> returns if a certain mode is
   * already set (at least) once and the function <code>getMode</code> may
   * be called.
   * @see getMode
   */
    public boolean containsMode(MouseModeName name) {
        return modes.containsKey(name);
    }

    public String getName() {
        return BasicUtils.afterDot(ID);
    }

    public String getBaseClass() {
        return ID;
    }

    public String toString() {
        return getName();
    }
}
