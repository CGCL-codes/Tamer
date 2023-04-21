package de.fzi.injectj.model;

import java.util.List;
import de.fzi.injectj.script.model.Type;

/**
 * @author genssler
 * This class is part of the Inject/J (http://injectj.sf.net) project. 
 * Inject/J is free software, available under the terms and conditions
 * of the GNU public license.
 * @inject export name=ExpressionFragment package=lang.weavepoints
 * 
 */
public abstract class ExpressionFragment extends BoundSourceFragment {

    /**
	 * @param parent
	 */
    public ExpressionFragment(Type parent) {
        super(parent);
    }

    /**
	 * Checks wether this expression represents a statement. 
	 * @inject export modifier=query
	 */
    public abstract boolean isStatementFragment();

    public abstract List getSubExpressions();

    /**
	 * @inject export modifier=query
	 * @return
	 */
    public abstract ExpressionFragment negate();
}
