package org.argouml.core.propertypanels.ui;

import org.argouml.model.Model;

/**
 * @author jrobbins
 * @author jaap.branderhorst
 * @author penyaskito
 */
class UMLInitialValueExpressionModel extends UMLExpressionModel {

    public UMLInitialValueExpressionModel(Object target) {
        super(target, "initialValue");
    }

    /**
     * @return
     * @see org.argouml.uml.ui.UMLExpressionModel2#getExpression()
     */
    @Override
    public Object getExpression() {
        return Model.getFacade().getInitialValue(getTarget());
    }

    @Override
    public Object newExpression(String lang, String body) {
        return Model.getDataTypesFactory().createExpression(lang, body);
    }

    /**
     * @param expr
     * @see org.argouml.uml.ui.UMLExpressionModel2#setExpression(java.lang.Object)
     */
    @Override
    public void setExpression(Object expression) {
        Object target = getTarget();
        assert (expression == null) || Model.getFacade().isAExpression(expression);
        Model.getCoreHelper().setInitialValue(target, null);
        Model.getCoreHelper().setInitialValue(target, expression);
    }
}
