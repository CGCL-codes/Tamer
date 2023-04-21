package clump.language.parsing.statement;

import clump.language.analysis.IEnvironment;
import clump.language.ast.definition.statements.IStatement;
import opala.lexing.LexemeKind;
import opala.lexing.impl.Lexeme;
import opala.parsing.exception.EntryAlreadyBoundException;
import opala.parsing.impl.AbstractSetOfCompilationUnit;

public class StatementUnit extends AbstractSetOfCompilationUnit<IStatement, IEnvironment> {

    public StatementUnit() throws EntryAlreadyBoundException {
        super();
        this.addCompilationUnit(new Lexeme(LexemeKind.OPERATOR, "{"), "FunctionVariableOrBlocStatement");
        this.addCompilationUnit(new Lexeme(LexemeKind.IDENT, "return"), "Return");
        this.addCompilationUnit(new Lexeme(LexemeKind.IDENT, "do"), "DoWhile");
        this.addCompilationUnit(new Lexeme(LexemeKind.IDENT, "while"), "WhileDo");
        this.addCompilationUnit(new Lexeme(LexemeKind.IDENT, "for"), "For");
        this.addCompilationUnit(new Lexeme(LexemeKind.IDENT, "unless"), "UnlessThenElse");
        this.addCompilationUnit(new Lexeme(LexemeKind.IDENT, "if"), "IfThenElse");
        this.addCompilationUnit(new Lexeme(LexemeKind.IDENT, "try"), "TryCatch");
        this.addCompilationUnit(new Lexeme(LexemeKind.IDENT, "throw"), "Throw");
        this.addCompilationUnit(new Lexeme(LexemeKind.IDENT, "switch"), "Switch");
        this.addCompilationUnit(new Lexeme(LexemeKind.IDENT, "native"), "Native", true);
        this.setDefaultCompilationUnit("SingleStatement");
    }
}
