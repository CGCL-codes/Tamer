package netgest.bo.xwc.framework.spy;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;

@SuppressWarnings("deprecation")
public class XUISpyMethodBinding extends MethodBinding {

    MethodBinding methodBinding;

    public XUISpyMethodBinding(MethodBinding m) {
        methodBinding = m;
    }

    public boolean equals(Object obj) {
        return methodBinding.equals(obj);
    }

    public String getExpressionString() {
        return methodBinding.getExpressionString();
    }

    public Class<?> getType(FacesContext arg0) throws MethodNotFoundException {
        return methodBinding.getType(arg0);
    }

    public int hashCode() {
        return methodBinding.hashCode();
    }

    public Object invoke(FacesContext arg0, Object[] arg1) throws EvaluationException, MethodNotFoundException {
        return methodBinding.invoke(arg0, arg1);
    }

    public String toString() {
        return methodBinding.toString();
    }
}
