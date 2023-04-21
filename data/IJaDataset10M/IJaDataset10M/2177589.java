package net.sourceforge.ejb3checker.libs.style_checker.dt;

import java.lang.reflect.Field;
import net.sourceforge.ejb3checker.libs.style_checker.IStyleCheck;

/**
 * TODO docme
 *
 * @author foobaamarook
 */
public class FieldStyleProblem extends StyleProblem {

    private final Field field;

    public FieldStyleProblem(final IStyleCheck styleCheck, final Class<?> clazz, final Field field) {
        this(styleCheck, clazz, field, styleCheck.getProblemDescription());
    }

    public FieldStyleProblem(final IStyleCheck styleCheck, final Class<?> clazz, final Field field, final String problemMessage) {
        super(styleCheck, clazz, problemMessage);
        if (field == null) {
            throw new NullPointerException();
        }
        this.field = field;
    }

    /**
         * @see net.sourceforge.ejb3checker.libs.style_checker.dt.StyleProblem#createMessage(java.lang.String)
         */
    @Override
    protected String createMessage(final String problemDescription) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Style problem with field ");
        sb.append(field.getName());
        sb.append(" in ");
        sb.append(clazz);
        sb.append(": ");
        sb.append(problemDescription);
        return sb.toString();
    }
}
