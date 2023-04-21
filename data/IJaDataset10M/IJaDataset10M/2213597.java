package net.sourceforge.ejb3checker.lib.dt;

/**
 * TODO docme
 *
 * @author foobaamarook
 */
public abstract class ClassStyleProblem extends StyleProblem {

    private final Class<?> clazz;

    protected ClassStyleProblem(final Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException();
        }
        this.clazz = clazz;
    }

    public Class<?> getProblemClass() {
        return clazz;
    }
}
