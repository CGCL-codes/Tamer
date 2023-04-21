package net.sf.jdpa.cg.model;

/**
 * @author Andreas Nilsson
 */
public class CastBuilder {

    private Expression expression;

    public CastBuilder(Expression expression) {
        if (expression == null) {
            throw new IllegalArgumentException("Argument [expression] can't be null");
        } else {
            this.expression = expression;
        }
    }

    public Cast to(Class type) {
        return to(type == null ? null : type.getName());
    }

    public Cast to(String className) {
        if (className == null) {
            throw new IllegalArgumentException("Argument [className] cannot be null");
        } else {
            return new Cast(className, expression);
        }
    }
}
