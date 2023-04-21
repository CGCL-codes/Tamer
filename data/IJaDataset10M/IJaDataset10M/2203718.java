package com.googlecode.sarasvati.rubric.visitor;

import com.googlecode.sarasvati.rubric.lang.RubricExprAnd;
import com.googlecode.sarasvati.rubric.lang.RubricExprNot;
import com.googlecode.sarasvati.rubric.lang.RubricExprOr;
import com.googlecode.sarasvati.rubric.lang.RubricExprSymbol;
import com.googlecode.sarasvati.rubric.lang.RubricStmtDateSymbol;
import com.googlecode.sarasvati.rubric.lang.RubricStmtIf;
import com.googlecode.sarasvati.rubric.lang.RubricStmtRelativeDate;
import com.googlecode.sarasvati.rubric.lang.RubricStmtResult;
import com.googlecode.sarasvati.rubric.lang.RubricStmtStringSymbol;

public interface RubricVisitor {

    void visit(RubricStmtIf ifStmt);

    void visit(RubricStmtStringSymbol stringSymbolStmt);

    void visit(RubricStmtDateSymbol dateSymbolStmt);

    void visit(RubricStmtRelativeDate relativeDateStmt);

    void visit(RubricStmtResult resultStmt);

    void visit(RubricExprAnd andExpr);

    void visit(RubricExprNot notExpr);

    void visit(RubricExprOr orExpr);

    void visit(RubricExprSymbol symbolExpr);
}
