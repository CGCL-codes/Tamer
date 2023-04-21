package com.cincosoft.project.security.parameterList;

import java.util.Arrays;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.framework.EntityQuery;
import com.cincosoft.project.Module;
import com.cincosoft.project.Parameter;

@Name("parameterList")
@Restrict("#{s:hasRole('parameterList')}")
public class ParameterList extends EntityQuery<Parameter> {

    private static final String[] RESTRICTIONS = { "lower(parameter.name) like concat(lower(#{parameterList.parameter.name}),'%')", "lower(parameter.value) like concat(lower(#{parameterList.parameter.value}),'%')", "lower(parameter.description) like concat(lower(#{parameterList.parameter.description}),'%')" };

    private static final String EJBQL = "select parameter from Parameter parameter";

    private Parameter parameter = new Parameter();

    public ParameterList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
    }

    public Parameter getParameter() {
        return parameter;
    }
}
