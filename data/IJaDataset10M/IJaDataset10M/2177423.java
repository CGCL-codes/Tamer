package ar.uba.dc.rfm.alloy.util;

import java.util.Map;
import ar.uba.dc.rfm.alloy.AlloyVariable;
import ar.uba.dc.rfm.alloy.ast.expressions.AlloyExpression;
import ar.uba.dc.rfm.alloy.ast.expressions.ExprConstant;
import ar.uba.dc.rfm.alloy.ast.expressions.ExprJoin;
import ar.uba.dc.rfm.alloy.ast.expressions.ExprVariable;
import ar.uba.dc.rfm.dynalloy.visualization.DynalloyExecutionTrace;

public class PrefixedExpressionPrinter extends ExpressionPrinter {

    private static class SubstituteFormalParameter extends VarSubstitutor {

        private DynalloyExecutionTrace current_node;

        public SubstituteFormalParameter(DynalloyExecutionTrace current_node) {
            this.current_node = current_node;
        }

        @Override
        protected AlloyExpression getExpr(AlloyVariable variable) {
            return this.current_node.getParameterMap(new ExprVariable(variable));
        }
    }

    private static class PrefixAdder extends VarSubstitutor {

        private String new_prefix;

        public PrefixAdder(String new_prefix) {
            this.new_prefix = new_prefix;
        }

        @Override
        protected AlloyExpression getExpr(AlloyVariable variable) {
            AlloyVariable prefix_variable = new AlloyVariable(new_prefix + variable.getVariableId().getString(), variable.getIndex());
            return ExprVariable.buildExprVariable(prefix_variable);
        }
    }

    private String variablePrefix = "QF.";

    private DynalloyExecutionTrace currentExecutionTrace;

    public PrefixedExpressionPrinter() {
        super();
    }

    public PrefixedExpressionPrinter(String variablePrefix) {
        super();
        setVariablePrefix(variablePrefix);
    }

    public PrefixedExpressionPrinter(DynalloyExecutionTrace currentExecutionTrace) {
        super();
        this.currentExecutionTrace = currentExecutionTrace;
    }

    @Override
    public Object visit(ExprVariable n) {
        ExpressionPrinter exprPrinter = new ExpressionPrinter();
        FormulaPrinter formPrinter = new FormulaPrinter();
        formPrinter.setExpressionVisitor(exprPrinter);
        exprPrinter.setFormulaVisitor(formPrinter);
        AlloyExpression expression = n;
        DynalloyExecutionTrace node = currentExecutionTrace;
        while (node.getParent() != null) {
            SubstituteFormalParameter varSubstitutor = new SubstituteFormalParameter(node);
            expression = (AlloyExpression) expression.accept(varSubstitutor);
            node = node.getParent();
        }
        String expr = null;
        try {
            String expressionStr = (String) expression.accept(exprPrinter);
            Integer.parseInt(expressionStr);
            expr = expressionStr;
        } catch (NumberFormatException ex) {
            PrefixAdder prefixAdder = new PrefixAdder(getVariablePrefix());
            FormulaMutator formulaMutator = new FormulaMutator();
            prefixAdder.setFormulaVisitor(formulaMutator);
            formulaMutator.setExpressionVisitor(prefixAdder);
            AlloyExpression prefixExpr = (AlloyExpression) expression.accept(prefixAdder);
            String prefixExprStr = (String) prefixExpr.accept(exprPrinter);
            expr = prefixExprStr;
        }
        return expr;
    }

    public void setVariablePrefix(String variablePrefix) {
        this.variablePrefix = variablePrefix;
    }

    public String getVariablePrefix() {
        return variablePrefix;
    }
}
