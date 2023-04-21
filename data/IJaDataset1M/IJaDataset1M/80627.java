package net.sf.javascribe.generator.context.processor;

import java.util.HashMap;
import java.util.List;
import net.sf.javascribe.ProcessingException;
import net.sf.javascribe.appdef.wrapper.configuration.ModelConfigurationWrapper;
import net.sf.javascribe.appdef.wrapper.model.DataSourceWrapper;
import net.sf.javascribe.appdef.wrapper.model.ModelWrapper;
import net.sf.javascribe.designs.resource.DefaultDataSourceObjectNameResolver;
import net.sf.javascribe.generator.accessor.DataSourceManagerTypeAccessor;
import net.sf.javascribe.generator.accessor.ModelTierTypesAccessor;
import net.sf.javascribe.generator.accessor.ProcessorTypesAccessor;

/**
 * @author User
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ModelProcessorContext extends AbstractModelTierProcessorContext implements ComponentProcessorContext {

    HashMap files = null;

    ModelWrapper model = null;

    ModelConfigurationWrapper config = null;

    public String getProperty(String name) {
        return config.getProperty(name);
    }

    public ModelWrapper getModel() {
        return model;
    }

    public ModelProcessorContext(ModelWrapper m) {
        files = new HashMap();
        model = m;
        resolver = new DefaultDataSourceObjectNameResolver();
    }

    public List getDataSourceNames() {
        return model.getDataSourceNames();
    }

    public DataSourceProcessorContext getDataSourceProcessorContext(String dsName) throws ProcessingException {
        DataSourceWrapper ds = null;
        DataSourceProcessorContext ret = null;
        DataSourceManagerTypeAccessor mgr = null;
        ModelTierTypesAccessor modelTypes = null;
        modelTypes = getProcessorTypesAccessor().getModelTypes(ProcessorTypesAccessor.DEFAULT_DEFINITION);
        ds = model.getDataSource(dsName);
        ret = new DataSourceProcessorContext(ds);
        ret.setTemplateManager(this.templates);
        ret.setProcessorTypesAccessor(types);
        mgr = modelTypes.getDataSourceManager(ds.getDataSourceName());
        if (mgr == null) throw new ProcessingException("Datasource '" + ds.getDataSourceManagerName() + "' is not defined in application environment description.");
        ret.globalObjects = globalObjects;
        ret.config = config.getDataSourceConfig(dsName);
        ret.dataSourceManager = mgr;
        ret.applicationPlatform = applicationPlatform;
        ret.resolver = resolver;
        return ret;
    }
}
