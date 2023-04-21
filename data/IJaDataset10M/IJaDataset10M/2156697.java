package clump.language.parsing.statement;

import clump.language.analysis.IEnvironment;
import clump.language.analysis.IPackageImportation;
import clump.language.ast.common.impl.VariableIdentifier;
import clump.language.ast.common.impl.VariableSpecification;
import clump.language.ast.definition.expression.IExpression;
import clump.language.ast.definition.expression.impl.FunctionExpression;
import clump.language.ast.definition.statements.IStatement;
import clump.language.ast.definition.statements.impl.Statement;
import clump.language.ast.definition.statements.impl.VariableSetup;
import clump.language.ast.exception.AbstractSyntaxTreeError;
import clump.language.ast.specification.entity.IFunctionSpecification;
import clump.language.ast.specification.expression.impl.FunctionSpecification;
import clump.language.ast.specification.method.IMethodSpecification;
import clump.language.parsing.expression.FunctionExpressionUnit;
import opala.lexing.ILocation;
import opala.lexing.LexemeKind;
import opala.lexing.exception.LexemeNotFoundException;
import opala.parsing.ICompilationUnit;
import opala.parsing.ILanguageSupport;
import opala.parsing.exception.ParsingUnitNotFound;
import opala.scanner.impl.CheckPointScanner;
import opala.scanner.IScanner;
import opala.scanner.exception.ScannerException;
import opala.utils.Pair;
import java.util.ArrayList;

public class SingleStatementUnit implements ICompilationUnit<IStatement, IEnvironment> {

    public IStatement compile(ILanguageSupport support, IScanner scanner, IEnvironment parameter) throws ScannerException, ParsingUnitNotFound, LexemeNotFoundException, AbstractSyntaxTreeError {
        final ILocation location = scanner.currentLexeme().getLocation();
        CheckPointScanner checkPoint = CheckPointScanner.newInstance(scanner);
        try {
            IStatement statement = IStatement.CAST.perform(support.getUnitByKey("ObjectOrVariableSetup").compile(support, scanner, parameter));
            scanner.scan(LexemeKind.OPERATOR, ";");
            checkPoint.commit();
            return statement;
        } catch (LexemeNotFoundException exn) {
            checkPoint.rollback();
        }
        checkPoint = CheckPointScanner.newInstance(scanner);
        try {
            IExpression expression = IExpression.CAST.perform(support.getUnitByKey("Expression").compile(support, scanner, parameter));
            scanner.scan(LexemeKind.OPERATOR, ";");
            checkPoint.commit();
            return new Statement(location, expression);
        } catch (LexemeNotFoundException exn) {
            checkPoint.rollback();
        }
        final IMethodSpecification specification = IMethodSpecification.CAST.perform(support.getUnitByKey("MethodSpecification").compile(support, scanner, parameter));
        final FunctionSpecification funSpec = new FunctionSpecification(new ArrayList<IPackageImportation>(), parameter.getPackageDefinition(), specification);
        final FunctionExpression funExpr = FunctionExpressionUnit.CAST.perform(support.getUnitByKey("FunctionExpression").compile(support, scanner, new Pair<IFunctionSpecification, IEnvironment>(funSpec, parameter)));
        final VariableIdentifier variableIdentifier = new VariableIdentifier(specification.getInternalName(), location);
        final VariableSpecification specification1 = new VariableSpecification(funSpec, variableIdentifier);
        if (scanner.currentLexeme().isA(LexemeKind.OPERATOR, ";")) {
            scanner.scan();
        }
        return new VariableSetup(location, specification1, funExpr);
    }
}
