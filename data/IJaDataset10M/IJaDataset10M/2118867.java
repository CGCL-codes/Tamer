package net.simplemodel.core.generator.internal;

import net.simplemodel.core.generator.*;
import net.simplemodel.core.ast.*;

public class AttributeImplementationVariableTemplate implements ITemplate {

    protected static String nl;

    public static synchronized AttributeImplementationVariableTemplate create(String lineSeparator) {
        nl = lineSeparator;
        AttributeImplementationVariableTemplate result = new AttributeImplementationVariableTemplate();
        nl = null;
        return result;
    }

    public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;

    protected final String TEXT_1 = "private final ";

    protected final String TEXT_2 = " ";

    protected final String TEXT_3 = " = new ";

    protected final String TEXT_4 = "(";

    protected final String TEXT_5 = ",";

    protected final String TEXT_6 = "){";

    protected final String TEXT_7 = NL;

    protected final String TEXT_8 = "@Override" + NL + "protected void validateNewValue(";

    protected final String TEXT_9 = " ";

    protected final String TEXT_10 = "){";

    protected final String TEXT_11 = NL + "}" + NL + "};";

    protected final String TEXT_12 = "private final ";

    protected final String TEXT_13 = " ";

    protected final String TEXT_14 = " = new ";

    protected final String TEXT_15 = "(this,";

    protected final String TEXT_16 = ",";

    protected final String TEXT_17 = ",";

    protected final String TEXT_18 = ",";

    protected final String TEXT_19 = "){";

    protected final String TEXT_20 = NL;

    protected final String TEXT_21 = "@Override" + NL + "protected void validateNewValue(";

    protected final String TEXT_22 = " ";

    protected final String TEXT_23 = "){";

    protected final String TEXT_24 = NL + "}" + NL + "};";

    protected final String TEXT_25 = "private ";

    protected final String TEXT_26 = " ";

    protected final String TEXT_27 = ";";

    void willNotGenerate() {
        throw new WillNotGenerateException();
    }

    @Override
    public String generate(ITemplateContext argument) {
        final StringBuffer stringBuffer = new StringBuffer();
        IImportBlock importBlock = argument.getImportBlock();
        SMAttributeDeclaration attribute = (SMAttributeDeclaration) argument.getSubject();
        SMTypeDeclaration typeDeclaration = (SMTypeDeclaration) argument.getSubjectAt(1);
        SMOppositeStatement opposite = attribute.getOppositeStatement();
        String valueType = importBlock.imprt(attribute.getType());
        String boxedValueType = importBlock.imprt(Util.boxIfNecessary(attribute.getType()));
        if (attribute.isMultiple()) {
            if (opposite == null) {
                stringBuffer.append(argument.getGeneratedJavadoc(NL));
                stringBuffer.append(TEXT_1);
                stringBuffer.append(importBlock.imprt(argument.getGeneratorContext().getNotifyingListInterface().parameterize(Util.boxIfNecessary(attribute.getType()))));
                stringBuffer.append(TEXT_2);
                stringBuffer.append(argument.getVariableName(attribute));
                stringBuffer.append(TEXT_3);
                stringBuffer.append(importBlock.imprt(argument.getGeneratorContext().getSynchronizedNotifyingListClass().parameterize(Util.boxIfNecessary(attribute.getType()))));
                stringBuffer.append(TEXT_4);
                stringBuffer.append(argument.getAccessReadExpression());
                stringBuffer.append(TEXT_5);
                stringBuffer.append(argument.getAccessWriteExpression());
                stringBuffer.append(TEXT_6);
                stringBuffer.append(TEXT_7);
                stringBuffer.append(argument.getGeneratedJavadoc(NL));
                stringBuffer.append(TEXT_8);
                stringBuffer.append(boxedValueType);
                stringBuffer.append(TEXT_9);
                stringBuffer.append(argument.getVariableName(attribute));
                stringBuffer.append(TEXT_10);
                if (attribute.getBody() != null) {
                    for (Object statement : attribute.getBody().getStatements()) {
                        if (statement instanceof SMValidateStatement) {
                            stringBuffer.append(argument.generateValidateStatement(new ValidateGeneratorContext((SMValidateStatement) statement, argument.getVariableName(attribute))));
                        }
                    }
                }
                stringBuffer.append(TEXT_11);
            } else {
                String attributeAccess = importBlock.imprt(opposite.getType()) + "." + argument.getAttributeDeclarationName(attribute);
                String inverseAttributeAccess = importBlock.imprt(attribute.getType()) + "." + argument.getAttributeDeclarationName(opposite);
                SMTypeReference variableType = argument.getGeneratorContext().getNotifyingListInterface().parameterize(attribute.getType());
                SMTypeReference instanceType;
                if (opposite.isMultiple()) {
                    instanceType = argument.getGeneratorContext().getMultiValueOppositeSynchronizedNotifyingListImpl().parameterize(typeDeclaration.createDeclarationTypeReference(), attribute.getType());
                } else {
                    instanceType = argument.getGeneratorContext().getSingleValueOppositeSynchronizedNotifyingListImpl().parameterize(typeDeclaration.createDeclarationTypeReference(), attribute.getType());
                }
                stringBuffer.append(argument.getGeneratedJavadoc(NL));
                stringBuffer.append(TEXT_12);
                stringBuffer.append(importBlock.imprt(variableType));
                stringBuffer.append(TEXT_13);
                stringBuffer.append(argument.getVariableName(attribute));
                stringBuffer.append(TEXT_14);
                stringBuffer.append(importBlock.imprt(instanceType));
                stringBuffer.append(TEXT_15);
                stringBuffer.append(attributeAccess);
                stringBuffer.append(TEXT_16);
                stringBuffer.append(inverseAttributeAccess);
                stringBuffer.append(TEXT_17);
                stringBuffer.append(argument.getAccessReadExpression());
                stringBuffer.append(TEXT_18);
                stringBuffer.append(argument.getAccessWriteExpression());
                stringBuffer.append(TEXT_19);
                stringBuffer.append(TEXT_20);
                stringBuffer.append(argument.getGeneratedJavadoc(NL));
                stringBuffer.append(TEXT_21);
                stringBuffer.append(attribute.getType());
                stringBuffer.append(TEXT_22);
                stringBuffer.append(argument.getVariableName(attribute));
                stringBuffer.append(TEXT_23);
                if (attribute.getBody() != null) {
                    for (Object statement : attribute.getBody().getStatements()) {
                        if (statement instanceof SMValidateStatement) {
                            stringBuffer.append(argument.generateValidateStatement(new ValidateGeneratorContext((SMValidateStatement) statement, argument.getVariableName(attribute))));
                        }
                    }
                }
                stringBuffer.append(TEXT_24);
            }
        } else {
            stringBuffer.append(argument.getGeneratedJavadoc(NL));
            stringBuffer.append(TEXT_25);
            stringBuffer.append(importBlock.imprt(attribute.getType()));
            stringBuffer.append(TEXT_26);
            stringBuffer.append(argument.getVariableName(attribute));
            stringBuffer.append(TEXT_27);
        }
        return stringBuffer.toString();
    }
}
