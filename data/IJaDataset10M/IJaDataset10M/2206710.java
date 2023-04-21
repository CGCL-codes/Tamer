package test.com.ivis.xprocess.util;

import java.util.HashMap;
import java.util.Map;
import com.ivis.xprocess.core.Portfolio;
import com.ivis.xprocess.framework.DataSource;
import com.ivis.xprocess.framework.exceptions.MinimumVersionException;
import com.ivis.xprocess.framework.exceptions.NoRootLogicalContainerException;
import com.ivis.xprocess.framework.impl.DataSourceImpl;
import com.ivis.xprocess.framework.impl.DatasourceDescriptor;
import com.ivis.xprocess.framework.impl.DatasourceDescriptorFactory;
import com.ivis.xprocess.framework.schema.JUnitSchemaSource;
import com.ivis.xprocess.framework.schema.SchemaBuilder;
import com.ivis.xprocess.framework.xml.IPersistenceHelper;
import com.ivis.xprocess.framework.xml.PersistenceHelper;

public class ModelProviderFactory {

    private static Map<String, ModelProvider> modelProviders = new HashMap<String, ModelProvider>();

    private static ModelProviderFactory instance = null;

    public static ModelProvider getModelProvider(String name) {
        if (instance == null) {
            instance = new ModelProviderFactory();
            SchemaBuilder.getInstance().buildSchema(new JUnitSchemaSource());
        }
        if (modelProviders.get(name) == null) {
            ModelProvider modelProvider = instance.new ModelProvider(name);
            modelProviders.put(name, modelProvider);
        }
        return modelProviders.get(name);
    }

    public class ModelProvider {

        private String name;

        protected IPersistenceHelper persistenceHelper;

        private ModelProvider(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public DataSource getDataSource() {
            return this.persistenceHelper.getDataSource();
        }

        public IPersistenceHelper getPersistenceHelper() {
            return persistenceHelper;
        }

        public void saveAndAdd() {
            persistenceHelper.saveAndAddDirtyElements();
        }

        public DataSource reconnectDatasourceJUnitSchemaSource(String directoryName) throws MinimumVersionException, NoRootLogicalContainerException {
            String dataSourceDir = Helper.getExistingTemporaryDirectory(directoryName);
            return initialiseDataSource(directoryName, dataSourceDir);
        }

        public DataSource setupDatasourceJUnitSchemaSource(String directoryName) throws MinimumVersionException, NoRootLogicalContainerException {
            String dataSourceDir = Helper.createTemporaryDirectory(directoryName);
            return initialiseDataSource(directoryName, dataSourceDir);
        }

        public DataSource setupDatasourceJarSchemaSource(String directoryName) throws MinimumVersionException, NoRootLogicalContainerException {
            String dataSourceDir = Helper.createTemporaryDirectory(directoryName);
            return initialiseDataSource(directoryName, dataSourceDir);
        }

        private DataSource initialiseDataSource(String directoryName, String dataSourceDir) throws MinimumVersionException, NoRootLogicalContainerException {
            DatasourceDescriptor dd = DatasourceDescriptorFactory.getManager(Helper.getTempRootDir()).create(dataSourceDir, directoryName, null, null);
            dd.saveAll();
            persistenceHelper = new PersistenceHelper(dataSourceDir, dataSourceDir + DataSourceImpl.LOCAL_DIR, Portfolio.class);
            return persistenceHelper.getDataSource();
        }
    }
}
