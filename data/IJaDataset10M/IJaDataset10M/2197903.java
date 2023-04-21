package org.jrcaf.model.datasources;

import java.util.List;
import org.jrcaf.core.registry.IPluginXMLAndParameterNameStrings;

/**
 *  Datasource registry interface
 */
public interface IDatasourceRegistry extends IPluginXMLAndParameterNameStrings {

    /**Extension-Point id for datasources*/
    public static final String MODEL_DATASOURCE_EXTENSION_POINT = "org.jrcaf.core.datasource";

    /**
    * @param aDatasource The IDatasource.
    * @return Returns the DataSourceDefinition
    */
    public abstract DatasourceDefinition getDatasourceDefinitionFor(IDatasource aDatasource);

    /**
    * @return All datasource definitions.
    */
    public abstract List<DatasourceDefinition> getDatasourceDefinitions();

    /**
    * @param aId The id of the datasource.
    * @return Returns the datasource with the given id.
    * @category Getter
    */
    public abstract DatasourceDefinition getDatasourceFor(String aId);
}
