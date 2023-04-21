package de.fraunhofer.isst.axbench.operations.checker.solver.formula;

/**
 * @author smann
 * @version 0.9.0
 * @since 0.9.0
 *
 */
public interface IFormulaVisitor {

    public Object visitAxlFormula(AxlFormula axlFormula);

    public Object visitAxlFormulaAnd(AxlFormulaAnd axlFormula);

    public Object visitAxlFormulaBiimp(AxlFormulaBiimp axlFormula);

    public Object visitAxlFormulaFalse(AxlFormulaFalse axlFormula);

    public Object visitAxlFormulaImp(AxlFormulaImp axlFormula);

    public Object visitAxlFormulaNot(AxlFormulaNot axlFormula);

    public Object visitAxlFormulaOr(AxlFormulaOr axlFormula);

    public Object visitAxlFormulaTrue(AxlFormulaTrue axlFormula);

    public Object visitAxlFormulaVar(AxlFormulaVar axlFormula);

    public Object visitAxlFormulaXor(AxlFormulaXor axlFormula);
}
