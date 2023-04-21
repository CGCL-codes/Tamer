package net.simplemodel.core.generator.internal;

import net.simplemodel.core.generator.*;
import net.simplemodel.core.ast.*;
import org.eclipse.dltk.ast.expressions.Expression;

public class ValidateTemplate implements ITemplate {

    protected static String nl;

    public static synchronized ValidateTemplate create(String lineSeparator) {
        nl = lineSeparator;
        ValidateTemplate result = new ValidateTemplate();
        nl = null;
        return result;
    }

    public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;

    protected final String TEXT_1 = "\tassertNotNull(";

    protected final String TEXT_2 = ");";

    protected final String TEXT_3 = NL + "assertTrue(";

    protected final String TEXT_4 = ");";

    void willNotGenerate() {
        throw new WillNotGenerateException();
    }

    @Override
    public String generate(ITemplateContext argument) {
        final StringBuffer stringBuffer = new StringBuffer();
        IValidateGenerationContext validateGenerationContext = (IValidateGenerationContext) argument.getSubject();
        SMValidateStatement validateStatement = validateGenerationContext.getValidateStatement();
        Expression notNullExpression = argument.getNotNullExpression(validateStatement.getExpression());
        if (notNullExpression != null) {
            stringBuffer.append(TEXT_1);
            stringBuffer.append(argument.generateExpression(notNullExpression, validateGenerationContext.getValueAccessExpression()));
            stringBuffer.append(TEXT_2);
        } else {
            stringBuffer.append(TEXT_3);
            stringBuffer.append(argument.generateExpression(validateStatement.getExpression(), validateGenerationContext.getValueAccessExpression()));
            stringBuffer.append(TEXT_4);
        }
        return stringBuffer.toString();
    }
}
