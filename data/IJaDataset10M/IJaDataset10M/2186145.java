package clump.language.parsing.statement;

import clump.language.analysis.IEnvironment;
import clump.language.analysis.impl.VariablesAndEnvironment;
import clump.language.ast.definition.statements.IStatement;
import clump.language.ast.definition.statements.impl.BlocStatement;
import clump.language.ast.exception.AbstractSyntaxTreeError;
import opala.lexing.ILocation;
import opala.lexing.LexemeKind;
import opala.lexing.exception.LexemeNotFoundException;
import opala.parsing.ICompilationUnit;
import opala.parsing.ILanguageSupport;
import opala.parsing.exception.ParsingUnitNotFound;
import opala.scanner.IScanner;
import opala.scanner.exception.ScannerException;

public class BlocStatementUnit implements ICompilationUnit<IStatement, IEnvironment> {

    public IStatement compile(ILanguageSupport support, IScanner scanner, IEnvironment parameter) throws ScannerException, ParsingUnitNotFound, LexemeNotFoundException, AbstractSyntaxTreeError {
        final ILocation location = scanner.scan(LexemeKind.OPERATOR, "{").getLocation();
        BlocStatement blocStatement = new BlocStatement(location);
        IEnvironment newParameter = new VariablesAndEnvironment(parameter);
        while (scanner.currentLexeme().isA(LexemeKind.OPERATOR, "}") == false) {
            IStatement iStatement = IStatement.CAST.perform(support.getUnitByKey("Statement").compile(support, scanner, newParameter));
            blocStatement.append(iStatement);
        }
        scanner.scan();
        if (scanner.currentLexeme().isA(LexemeKind.OPERATOR, ";")) {
            scanner.scan();
        }
        return blocStatement;
    }
}
