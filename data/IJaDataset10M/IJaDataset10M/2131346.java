package com.cerny.bugtrack;

import com.cerny.bugtrack.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.framework.EntityQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

@Name("listGenericsList")
public class ListGenericsList extends EntityQuery {

    private static final String[] RESTRICTIONS = { "lower(listGenerics.name) like concat(lower(#{listGenericsList.listGenerics.name}),'%')" };

    private ListGenerics listGenerics = new ListGenerics();

    @Override
    public String getEjbql() {
        return "select listGenerics from ListGenerics listGenerics";
    }

    @Override
    public Integer getMaxResults() {
        return 25;
    }

    public ListGenerics getListGenerics() {
        return listGenerics;
    }

    public List<ValueExpression> getRestrictions() {
        return buildExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    @SuppressWarnings("unchecked")
    protected List<ValueExpression> buildExpressionStrings(List<String> expressionStrings) {
        Expressions expressions = new Expressions();
        List<ValueExpression> restrictionVEs = new ArrayList<ValueExpression>(expressionStrings.size());
        for (String expressionString : expressionStrings) {
            restrictionVEs.add(expressions.createValueExpression(expressionString));
        }
        return restrictionVEs;
    }
}
