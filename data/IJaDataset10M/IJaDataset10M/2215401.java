package scheme4j.parser;

import scheme4j.environment.EnvironmentManager;
import scheme4j.environment.IEnvironment;

public class ScmBody extends ScmObject {

    /** costruttore che deve essere utilizzato esclusivamente da javacc */
    public ScmBody(int id) {
        super(id);
    }

    /** costruttore che deve essere utilizzato esclusivamente da javacc */
    public ScmBody(SchemeParser p, int id) {
        super(p, id);
    }

    public boolean needsEnvironment() {
        return true;
    }

    /**
	 * Definition()* Sequence()
	 */
    public ScmObject eval() {
        IEnvironment env = EnvironmentManager.getCurrentEnvironment();
        env.setTotalEvaluatingElements(jjtGetNumChildren());
        ScmObject current = null;
        for (int i = env.getEvaluatedElementCount(); i < jjtGetNumChildren(); i++) {
            current = ((ScmObject) jjtGetChild(i)).evaluate();
            if (current == null) return null;
            env.addEvaluatedElement(current);
        }
        return current;
    }
}
