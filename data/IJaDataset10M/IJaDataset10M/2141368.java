package com.completex.objective.components.persistency.meta.adapter;

import com.completex.objective.components.log.Log;
import com.completex.objective.components.persistency.ColumnType;
import com.completex.objective.components.persistency.JavaToMetaType;
import com.completex.objective.components.persistency.MetaColumn;
import com.completex.objective.components.persistency.MetaTable;
import com.completex.objective.components.persistency.UserDefinedTypeMetaModel;
import com.completex.objective.components.persistency.meta.MetaModel;
import com.completex.objective.components.persistency.meta.ModelLoader;
import com.completex.objective.components.persistency.meta.ModelLoaderPlugin;
import com.completex.objective.components.persistency.meta.impl.DatabaseModelLoaderImpl;
import com.completex.objective.components.persistency.meta.impl.FileModelLoaderImpl;
import com.completex.objective.components.persistency.meta.impl.NullModelLoaderImpl;
import com.completex.objective.components.persistency.meta.impl.UdtBaseModelLoaderPlugin;
import com.completex.objective.components.sdl.reader.SdlReader;
import com.completex.objective.components.sdl.reader.impl.SdlReaderImpl;
import com.completex.objective.util.ExceptionHandlerImpl;
import com.completex.objective.util.PropertyMap;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Gennady Krizhevsky
 */
public class ModelLoaderAdapter implements ModelLoader {

    private ModelLoader internalFileModelLoader;

    private ModelLoader externalFileModelLoader = new NullModelLoaderImpl();

    private JavaToMetaType javaToMetaType;

    private MetaColumn.GeneratorStruct creationDateStruct;

    private MetaColumn.GeneratorStruct lastUpdatedStruct;

    private Log logger;

    private static final SdlReader sdlReader = new SdlReaderImpl();

    private Map plugins = new LinkedHashMap();

    public ModelLoaderAdapter(ModelLoader internalFileModelLoader, ModelLoader externalFileModelLoader, JavaToMetaType javaToMetaType, Log logger) {
        this(internalFileModelLoader, externalFileModelLoader, javaToMetaType, null, null, logger);
    }

    public ModelLoaderAdapter(ModelLoader internalFileModelLoader, ModelLoader externalFileModelLoader, JavaToMetaType javaToMetaType, MetaColumn.GeneratorStruct creationDateStruct, MetaColumn.GeneratorStruct lastUpdatedStruct, Log logger) {
        ExceptionHandlerImpl.assertNotNull("logger", logger);
        ExceptionHandlerImpl.assertNotNull("internalFileModelLoader", internalFileModelLoader);
        this.logger = logger;
        this.internalFileModelLoader = internalFileModelLoader;
        this.internalFileModelLoader.setJavaToMetaType(javaToMetaType);
        this.creationDateStruct = creationDateStruct;
        this.lastUpdatedStruct = lastUpdatedStruct;
        if (externalFileModelLoader != null) {
            this.externalFileModelLoader = externalFileModelLoader;
            this.externalFileModelLoader.setJavaToMetaType(javaToMetaType);
        }
        setJavaToMetaType(javaToMetaType);
    }

    protected Log getLogger() {
        return logger;
    }

    public void setupPlugins(Map map, Connection connection) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        PropertyMap configPropertyMap = PropertyMap.toPropertyMap(map);
        for (Iterator it = configPropertyMap.keySet().iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            PropertyMap config = configPropertyMap.getPropertyMap(key, true);
            PropertyMap loaderConfig = config.getPropertyMap("loader");
            if (loaderConfig != null) {
                String classString = loaderConfig.getProperty("class", true);
                Class loaderClass = Class.forName(classString);
                ModelLoaderPlugin loaderPlugin = (ModelLoaderPlugin) loaderClass.newInstance();
                this.registerPlugin(loaderPlugin);
                PropertyMap pluginConfig = loaderConfig.getPropertyMap("config");
                if (pluginConfig != null) {
                    loaderPlugin.configure(pluginConfig);
                    if (loaderPlugin.needsConnection()) {
                        loaderPlugin.setConnection(connection);
                    }
                }
            }
        }
    }

    private void loadPluginsData(MetaModel model) throws Exception {
        if (plugins != null) {
            ModelLoaderPlugin plugin = (ModelLoaderPlugin) plugins.get(UdtBaseModelLoaderPlugin.PLUGIN_KEY);
            if (plugin != null) {
                UserDefinedTypeMetaModel pluginModel = plugin.load(new UserDefinedTypeMetaModel(model.getFilter()));
                model.setUserDefinedTypeMetaModel(pluginModel);
            }
        }
    }

    private MetaModel loadModel() throws Exception {
        MetaModel model = internalFileModelLoader.load();
        if (externalFileModelLoader != null) {
            model = externalFileModelLoader.load(model);
        }
        for (Iterator it = model.tableIterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            MetaTable table = (MetaTable) entry.getValue();
            for (int i = 0; i < table.size(); i++) {
                MetaColumn column = table.getColumn(i);
                if (lastUpdatedStruct != null) {
                    column.setLastUpdatedNames(lastUpdatedStruct);
                }
                if (creationDateStruct != null) {
                    column.setCreationDateNames(creationDateStruct);
                }
            }
        }
        return model;
    }

    public MetaModel load() throws Exception {
        return load(null);
    }

    public MetaModel load(MetaModel model) throws Exception {
        model = loadModel();
        loadPluginsData(model);
        return model;
    }

    public void registerPlugin(ModelLoaderPlugin plugin) {
        String key = plugin.getPluginKey();
        if (plugins.containsKey(key)) {
            throw new RuntimeException("There is already storer plugin with key " + key);
        }
        plugins.put(key, plugin);
        plugin.setJavaToMetaType(javaToMetaType);
    }

    public void unregisterPlugin(String key) {
        plugins.remove(key);
    }

    public JavaToMetaType getJavaToMetaType() {
        return javaToMetaType;
    }

    public void setJavaToMetaType(JavaToMetaType javaToMetaType) {
        this.javaToMetaType = javaToMetaType;
    }

    public static class InternalFileModelLoaderAdapter extends FileModelLoaderImpl {

        public InternalFileModelLoaderAdapter(String descriptorPath, String filterPattern) {
            super(descriptorPath, filterPattern);
        }

        public InternalFileModelLoaderAdapter(Reader reader, String filterPattern) {
            super(reader, filterPattern);
        }

        protected MetaModel toModel(MetaModel model, Map modelMap) {
            model.fromInternalMap(modelMap);
            return model;
        }

        protected Map readModelMap(Reader reader) throws IOException {
            return (Map) sdlReader.read(reader);
        }

        public void setJavaToMetaType(JavaToMetaType javaToMetaType) {
        }
    }

    public static class ExternalFileModelLoaderAdapter extends FileModelLoaderImpl {

        public ExternalFileModelLoaderAdapter(String descriptorPath, String filterPattern) {
            super(descriptorPath, filterPattern);
        }

        public ExternalFileModelLoaderAdapter(Reader reader, String filterPattern) {
            super(reader, filterPattern);
        }

        protected MetaModel toModel(MetaModel model, Map modelMap) {
            model.fromExternalMap(modelMap);
            return model;
        }

        protected Map readModelMap(Reader reader) throws IOException {
            return (Map) sdlReader.read(reader);
        }

        public void setJavaToMetaType(JavaToMetaType javaToMetaType) {
        }
    }

    public static class DatabaseModelLoaderAdapter extends DatabaseModelLoaderImpl {

        private Connection connection;

        private JavaToMetaType javaToMetaType;

        private Log logger;

        public DatabaseModelLoaderAdapter(String schema, String catalog, String filterPattern, Connection connection, Log logger) {
            this(schema, catalog, filterPattern, connection, null, logger);
        }

        DatabaseModelLoaderAdapter(String schema, String catalog, String filterPattern, Connection connection, JavaToMetaType javaToMetaType, Log logger) {
            super(schema, catalog, filterPattern);
            this.logger = logger;
            this.connection = connection;
            this.javaToMetaType = javaToMetaType;
        }

        public void setJavaToMetaType(JavaToMetaType javaToMetaType) {
            this.javaToMetaType = javaToMetaType;
        }

        protected Connection getConnection() throws SQLException {
            return connection;
        }

        protected ColumnType columnType(String dataType, int columnSize, int decimalDigits, boolean required) {
            return javaToMetaType.dataType(dataType, columnSize, decimalDigits, required);
        }

        protected void debug(String message) {
            logger.debug(message);
        }

        protected void info(String message) {
            logger.info(message);
        }
    }
}
