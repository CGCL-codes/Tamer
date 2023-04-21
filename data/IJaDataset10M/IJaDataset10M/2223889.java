package tudresden.ocl.codegen.decl.treegen.analysis;

import tudresden.ocl.codegen.decl.treegen.node.*;

public interface Analysis extends Switch {

    Object getIn(Node node);

    void setIn(Node node, Object in);

    Object getOut(Node node);

    void setOut(Node node, Object out);

    void caseStart(Start node);

    void caseAUnionQueryExpression(AUnionQueryExpression node);

    void caseAIntersectQueryExpression(AIntersectQueryExpression node);

    void caseAExceptQueryExpression(AExceptQueryExpression node);

    void caseAQuerySpecQueryExpression(AQuerySpecQueryExpression node);

    void caseAEmptyQueryExpression(AEmptyQueryExpression node);

    void caseASelectClause(ASelectClause node);

    void caseADistinctSetQuantifier(ADistinctSetQuantifier node);

    void caseAAllSetQuantifier(AAllSetQuantifier node);

    void caseAEmptySetQuantifier(AEmptySetQuantifier node);

    void caseAAsteriskSelectList(AAsteriskSelectList node);

    void caseAFunctionSelectList(AFunctionSelectList node);

    void caseASubListSelectList(ASubListSelectList node);

    void caseAEmptySelectList(AEmptySelectList node);

    void caseAStringSelectSubListItem(AStringSelectSubListItem node);

    void caseAColumnSelectSubListItem(AColumnSelectSubListItem node);

    void caseAEmptySelectSubListItem(AEmptySelectSubListItem node);

    void caseAColumn(AColumn node);

    void caseAFromClause(AFromClause node);

    void caseATableNameTableReference(ATableNameTableReference node);

    void caseADerivedTableTableReference(ADerivedTableTableReference node);

    void caseAEmptyTableReference(AEmptyTableReference node);

    void caseAWhereClause(AWhereClause node);

    void caseAAndBooleanExpression(AAndBooleanExpression node);

    void caseAOrBooleanExpression(AOrBooleanExpression node);

    void caseANotBooleanExpression(ANotBooleanExpression node);

    void caseANullBooleanExpression(ANullBooleanExpression node);

    void caseAInBooleanExpression(AInBooleanExpression node);

    void caseAExistsBooleanExpression(AExistsBooleanExpression node);

    void caseARExpBooleanExpression(ARExpBooleanExpression node);

    void caseATrueBooleanExpression(ATrueBooleanExpression node);

    void caseAFalseBooleanExpression(AFalseBooleanExpression node);

    void caseAEmptyBooleanExpression(AEmptyBooleanExpression node);

    void caseAEqRelationalExpression(AEqRelationalExpression node);

    void caseANeqRelationalExpression(ANeqRelationalExpression node);

    void caseAGtRelationalExpression(AGtRelationalExpression node);

    void caseALtRelationalExpression(ALtRelationalExpression node);

    void caseAGteqRelationalExpression(AGteqRelationalExpression node);

    void caseALteqRelationalExpression(ALteqRelationalExpression node);

    void caseANumExpRelationalExpression(ANumExpRelationalExpression node);

    void caseAEmptyRelationalExpression(AEmptyRelationalExpression node);

    void caseAMultNumericExpression(AMultNumericExpression node);

    void caseADivNumericExpression(ADivNumericExpression node);

    void caseAPlusNumericExpression(APlusNumericExpression node);

    void caseAMinusNumericExpression(AMinusNumericExpression node);

    void caseAValueNumericExpression(AValueNumericExpression node);

    void caseAEmptyNumericExpression(AEmptyNumericExpression node);

    void caseANumericUnaryExpression(ANumericUnaryExpression node);

    void caseAStringUnaryExpression(AStringUnaryExpression node);

    void caseAColumnUnaryExpression(AColumnUnaryExpression node);

    void caseAFunctionUnaryExpression(AFunctionUnaryExpression node);

    void caseABooleanUnaryExpression(ABooleanUnaryExpression node);

    void caseAParUnaryExpression(AParUnaryExpression node);

    void caseAQueryUnaryExpression(AQueryUnaryExpression node);

    void caseAMinusUnaryExpression(AMinusUnaryExpression node);

    void caseAEmptyUnaryExpression(AEmptyUnaryExpression node);

    void caseTNewLine(TNewLine node);

    void caseTBlank(TBlank node);

    void caseTTab(TTab node);

    void caseTUnion(TUnion node);

    void caseTIntersect(TIntersect node);

    void caseTExcept(TExcept node);

    void caseTAll(TAll node);

    void caseTTrue(TTrue node);

    void caseTFalse(TFalse node);

    void caseTAsterisk(TAsterisk node);

    void caseTIdentifier(TIdentifier node);

    void caseTNumericValue(TNumericValue node);

    void caseEOF(EOF node);
}
