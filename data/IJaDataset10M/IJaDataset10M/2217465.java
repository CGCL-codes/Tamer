package org.informaticisenzafrontiere.openstaff.persistence.iface;

import java.util.List;
import org.informaticisenzafrontiere.openstaff.model.Parameter;

public interface ParameterDao {

    public void insertParameter(Parameter parameter);

    public void deleteParameter(Parameter parameter);

    public void updateParameter(Parameter parameter);

    public List<Parameter> getParameters();

    public List<Parameter> getParametersByIdRole(Integer idRole);

    public List<Parameter> getParametersByProject(String codeProject);

    public List<Parameter> getParametersProperty();
}
