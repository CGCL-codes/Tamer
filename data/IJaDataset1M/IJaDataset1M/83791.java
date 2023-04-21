package com.aragost.araspect.evaluators;

import com.aragost.araspect.EvaluationException;
import com.aragost.araspect.PathElement;
import java.util.Map;

/**
 * @author aragost
 */
public abstract class GenericMapEvaluator extends MapEvaluator {

    protected GenericMapEvaluator(String id, String getterName) {
        this.id = id;
        this.getterName = getterName;
    }

    /**
   * @see Evaluator#id()
   */
    public String id() {
        return id;
    }

    /**
   * @see Evaluator#eval(Object, PathElement)
   */
    public Object eval(Object arg, PathElement elm) throws EvaluationException {
        return super.eval(getMap(arg), elm);
    }

    /**
   * DOCUMENT ME!
   * 
   * @param key DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
    public PathElement createPathElement(String key) {
        return new PathElement(id(), key);
    }

    /**
   * @see Evaluator#prettyPrint(PathElement)
   */
    public String prettyPrint(PathElement elm) {
        return getterName + "(" + elm.getSecondPart() + ")";
    }

    /**
   * Override to obtain a Map from the passed in object.
   * @param object DOCUMENT ME!
   * @return DOCUMENT ME!
   */
    protected abstract Map getMap(Object object);

    private String id;

    private String getterName;
}
