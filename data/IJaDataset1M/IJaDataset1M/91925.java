package trstudio.beansmetric.core.beansmetric.java;

import com.sun.source.tree.VariableTree;
import javax.lang.model.element.Modifier;
import trstudio.beansmetric.core.beansmetric.CompilationUnitElement;
import trstudio.beansmetric.core.beansmetric.FieldElement;
import trstudio.beansmetric.core.metricsource.MetricSource;

/**
 * Attribut Java.
 *
 * @author Sebastien Villemain
 */
public class JavaFieldElement implements FieldElement {

    private final CompilationUnitElement parent;

    private final VariableTree variable;

    public JavaFieldElement(CompilationUnitElement parent, VariableTree variable) {
        this.parent = parent;
        this.variable = variable;
    }

    public CompilationUnitElement getParentElement() {
        return parent;
    }

    public boolean isPublic() {
        return variable.getModifiers().getFlags().contains(Modifier.PUBLIC);
    }

    public boolean isProtected() {
        return variable.getModifiers().getFlags().contains(Modifier.PROTECTED);
    }

    public boolean isPrivate() {
        return variable.getModifiers().getFlags().contains(Modifier.PRIVATE);
    }

    public boolean isAbstract() {
        return variable.getModifiers().getFlags().contains(Modifier.ABSTRACT);
    }

    public boolean isStatic() {
        return variable.getModifiers().getFlags().contains(Modifier.STATIC);
    }

    public String getHandle() {
        return parent.getHandle() + "." + variable.getName().toString();
    }

    public void compute(MetricSource source) {
    }
}
