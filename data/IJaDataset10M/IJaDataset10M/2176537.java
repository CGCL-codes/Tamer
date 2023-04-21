package clump.language.parsing.method;

import clump.language.analysis.IEnvironment;
import clump.language.analysis.impl.VariablesAndEnvironment;
import clump.language.ast.common.IEntitySpecification;
import clump.language.ast.common.IVariableIdentifier;
import clump.language.ast.common.IVariableSpecification;
import clump.language.ast.common.impl.AnonymousVariableIdentifier;
import clump.language.ast.common.impl.EntitySpecification;
import clump.language.ast.common.impl.VariableIdentifier;
import clump.language.ast.common.impl.VariableSpecification;
import clump.language.ast.definition.expression.impl.BinOpExpression;
import clump.language.ast.exception.AbstractSyntaxTreeError;
import clump.language.ast.specification.expression.IEntityInstance;
import clump.language.ast.specification.expression.impl.VariableEntityInstance;
import clump.language.ast.specification.method.IMethodSpecification;
import clump.language.ast.specification.method.impl.MethodSpecification;
import clump.message.MessageProvider;
import opala.lexing.ILexeme;
import opala.lexing.ILocation;
import opala.lexing.LexemeKind;
import opala.lexing.exception.LexemeNotFoundException;
import opala.lexing.impl.ListBasedTokenizer;
import opala.parsing.ICompilationUnit;
import opala.parsing.ILanguageSupport;
import opala.parsing.exception.ParsingUnitNotFound;
import opala.scanner.IScanner;
import opala.scanner.exception.ScannerException;
import java.util.ArrayList;
import java.util.List;

public class MethodSpecificationUnit implements ICompilationUnit<IMethodSpecification, IEnvironment> {

    public IMethodSpecification compile(ILanguageSupport support, IScanner scanner, IEnvironment parameter) throws ScannerException, ParsingUnitNotFound, LexemeNotFoundException, AbstractSyntaxTreeError {
        final opala.scanner.impl.CheckPointScanner checkPointScanner = opala.scanner.impl.CheckPointScanner.newInstance(scanner);
        IEntityInstance result;
        try {
            result = IEntityInstance.CAST.perform(support.getUnitByKey("EntityImplementation").compile(support, scanner, parameter));
        } finally {
            checkPointScanner.commit();
        }
        final IEntitySpecification specification;
        final String internalName;
        final List<IVariableSpecification> parameters = new ArrayList<IVariableSpecification>();
        final List<IEntityInstance> exceptions = new ArrayList<IEntityInstance>();
        final ILocation location = scanner.currentLexeme().getLocation();
        if (scanner.currentLexeme().isA(LexemeKind.OPERATOR, "(")) {
            scanner.scan(LexemeKind.OPERATOR, "(");
            ILexeme lexeme = scanner.scan(LexemeKind.OPERATOR);
            if (BinOpExpression.hasOperator(lexeme.getValue()) == false) {
                throw new ParsingUnitNotFound(MessageProvider.getMessage("undefined.operator", new Object[] { lexeme.getValue() })).setLocation(lexeme.getLocation());
            }
            specification = new EntitySpecification(location, lexeme.getValue());
            internalName = BinOpExpression.getInternalName(specification.getName());
            scanner.scan(LexemeKind.OPERATOR, ")");
        } else {
            specification = IEntitySpecification.CAST.perform(support.getUnitByKey("EntitySpecification").compile(support, scanner, parameter));
            internalName = specification.getName();
        }
        final VariablesAndEnvironment environment;
        if (parameter instanceof VariablesAndEnvironment) {
            environment = (VariablesAndEnvironment) parameter;
        } else {
            environment = new VariablesAndEnvironment(parameter);
        }
        if (specification.getGenerics().length > 0) {
            for (VariableEntityInstance variableEntityInstance : specification.getGenerics()) {
                environment.addTypeVariable(variableEntityInstance);
            }
            final IScanner memoryScanner = scanner.buildWith(new ListBasedTokenizer(checkPointScanner.getScannedLexeme()));
            result = IEntityInstance.CAST.perform(support.getUnitByKey("EntityImplementation").compile(support, memoryScanner, parameter));
        }
        boolean isFirst = true;
        scanner.scan(LexemeKind.OPERATOR, "(");
        while (scanner.currentLexeme().isA(LexemeKind.OPERATOR, ")") == false) {
            if (isFirst) {
                isFirst = false;
            } else {
                scanner.scan(LexemeKind.OPERATOR, ",");
            }
            final IEntityInstance parameterType = IEntityInstance.CAST.perform(support.getUnitByKey("EntityImplementation").compile(support, scanner, environment));
            final IVariableIdentifier parameterName;
            if (scanner.currentLexeme().isA(LexemeKind.IDENT)) {
                final ILexeme iLexeme = scanner.scan();
                parameterName = new VariableIdentifier(iLexeme.getValue(), iLexeme.getLocation());
            } else {
                parameterName = new AnonymousVariableIdentifier(scanner.currentLexeme().getLocation());
            }
            parameters.add(new VariableSpecification(parameterType, parameterName));
        }
        scanner.scan();
        if (scanner.currentLexeme().isA(LexemeKind.IDENT, "throws")) {
            do {
                scanner.scan();
                exceptions.add(IEntityInstance.CAST.perform(support.getUnitByKey("EntityImplementation").compile(support, scanner, parameter)));
            } while (scanner.currentLexeme().isA(LexemeKind.OPERATOR, ","));
        }
        return new MethodSpecification(environment.getCurrentEntitySpecification(), location, specification, result, internalName, parameters, exceptions);
    }
}
