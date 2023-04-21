package xuniversewizard.gui.param;

import org.w3c.dom.Node;
import xutools.text.TextManager;

/**
 * An int build parameter.
 * 
 * @author Tobias Weigel
 * @date 25.03.2009
 * 
 */
public class IntParameter extends Parameter {

    public IntParameter(TextManager textManager, String section, String name, int level, Node node) {
        super(textManager, section, name, level);
        this.type = ParameterType.INT;
    }
}
