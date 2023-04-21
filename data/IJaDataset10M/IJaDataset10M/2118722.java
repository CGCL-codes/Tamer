package org.openconcerto.erp.core.humanresources.payroll.element;

public class CodeStatutCategorielConventionnelSQLElement extends AbstractCodeSQLElement {

    public CodeStatutCategorielConventionnelSQLElement() {
        super("CODE_STATUT_CAT_CONV", "un code statut catégoriel conventionnel", "codes statuts catégoriels conventionnel");
    }

    @Override
    protected String createCode() {
        return createCodeFromPackage() + ".convention.status.code";
    }
}
