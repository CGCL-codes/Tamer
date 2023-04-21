package opennlp.ccg.grammar;

import opennlp.ccg.unify.*;
import opennlp.ccg.synsem.*;
import java.util.*;

/**
 * Backward composition, e.g. Y\Z X\Y => X\Z
 * 
 * @author Jason Baldridge
 * @version $Revision: 1.3 $, $Date: 2009/07/17 04:23:30 $
 */
public class BackwardComposition extends AbstractCompositionRule {

    private static final long serialVersionUID = -937944882697380690L;

    public BackwardComposition() {
        this(true);
    }

    public BackwardComposition(boolean isHarmonic) {
        _isHarmonic = isHarmonic;
        if (isHarmonic) {
            _name = "<B";
            _functorSlash = new Slash('\\', "^");
            _argSlash = new Slash('\\', "^");
        } else {
            _name = "<Bx";
            _functorSlash = new Slash('\\', "x");
            _argSlash = new Slash('/', "x");
        }
        _functorSlash.setAbility("active");
    }

    public List<Category> applyRule(Category[] inputs) throws UnifyFailure {
        if (inputs.length != 2) {
            throw new UnifyFailure();
        }
        return apply(inputs[1], inputs[0]);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Y").append(_argSlash.toString()).append("Z ").append("X\\Y => X").append(_argSlash.toString()).append("Z");
        return sb.toString();
    }
}
