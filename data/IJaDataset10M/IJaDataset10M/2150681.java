package org.happy.commons.patterns.overloaded_constructor;

import java.util.Hashtable;
import org.happy.commons.patterns.version.Version_1x0;

/**
 * the context contains all parameters for the constructor, thus if the parent
 * constructor calls the super constructor it stores all parameters in the
 * context and calls the super constructor.
 * 
 * @author Andreas Hollmann, Eugen Lofing, Wjatscheslaw Stoljarski
 * 
 */
public class Context_1x0Impl implements Context_1x0, Version_1x0<Float> {

    private Hashtable<String, Object> parameters = new Hashtable<String, Object>();

    private boolean isConstructorMethodUsed = false;

    private int inheritanceDepth = 0;

    public Context_1x0Impl() {
    }

    public boolean isConstructorMethodUsed() {
        return isConstructorMethodUsed;
    }

    public void setConstructorMethodUsed(boolean isConstructorMethodUsed) {
        this.isConstructorMethodUsed = isConstructorMethodUsed;
    }

    public int getInheritanceDepth() {
        return inheritanceDepth;
    }

    public void setInheritanceDepth(int inheritanceDepth) {
        this.inheritanceDepth = inheritanceDepth;
    }

    @SuppressWarnings("unchecked")
    public void setInheritanceDepth(Class c) {
        this.inheritanceDepth = OverloadedConstructor_1x0.solveClassInheritanceDepth(c);
    }

    public Hashtable<String, Object> getParameters() {
        return parameters;
    }

    public Float getVersion() {
        return 1F;
    }
}
