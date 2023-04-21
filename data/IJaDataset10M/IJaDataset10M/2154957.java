package com.ibatis.sqlmap.engine.builder.xml;

import com.ibatis.common.resources.Resources;
import com.ibatis.common.xml.Nodelet;
import com.ibatis.common.xml.NodeletParser;
import com.ibatis.common.xml.NodeletUtils;
import com.ibatis.common.beans.ClassInfo;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapException;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;
import com.ibatis.sqlmap.engine.accessplan.AccessPlanFactory;
import com.ibatis.sqlmap.engine.cache.CacheModel;
import com.ibatis.sqlmap.engine.cache.fifo.FifoCacheController;
import com.ibatis.sqlmap.engine.cache.lru.LruCacheController;
import com.ibatis.sqlmap.engine.cache.memory.MemoryCacheController;
import com.ibatis.sqlmap.engine.datasource.DataSourceFactory;
import com.ibatis.sqlmap.engine.datasource.DbcpDataSourceFactory;
import com.ibatis.sqlmap.engine.datasource.JndiDataSourceFactory;
import com.ibatis.sqlmap.engine.datasource.SimpleDataSourceFactory;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactory;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.transaction.TransactionConfig;
import com.ibatis.sqlmap.engine.transaction.TransactionManager;
import com.ibatis.sqlmap.engine.transaction.external.ExternalTransactionConfig;
import com.ibatis.sqlmap.engine.transaction.jdbc.JdbcTransactionConfig;
import com.ibatis.sqlmap.engine.transaction.jta.JtaTransactionConfig;
import com.ibatis.sqlmap.engine.type.*;
import org.w3c.dom.Node;
import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.Properties;

public class SqlMapConfigParser extends BaseParser {

    protected final NodeletParser parser = new NodeletParser();

    private boolean usingStreams;

    public SqlMapConfigParser() {
        this(null, null);
    }

    public SqlMapConfigParser(XmlConverter sqlMapConfigConv, XmlConverter sqlMapConv) {
        super(new Variables());
        parser.setValidation(true);
        parser.setEntityResolver(new SqlMapClasspathEntityResolver());
        vars.sqlMapConfigConv = sqlMapConfigConv;
        vars.sqlMapConv = sqlMapConv;
        vars.delegate = new SqlMapExecutorDelegate();
        vars.typeHandlerFactory = vars.delegate.getTypeHandlerFactory();
        vars.client = new SqlMapClientImpl(vars.delegate);
        registerDefaultTypeAliases();
        addSqlMapConfigNodelets();
        addGlobalPropNodelets();
        addSettingsNodelets();
        addTypeAliasNodelets();
        addTypeHandlerNodelets();
        addTransactionManagerNodelets();
        addSqlMapNodelets();
        addResultObjectFactoryNodelets();
    }

    public SqlMapClient parse(Reader reader, Properties props) {
        vars.properties = props;
        return parse(reader);
    }

    public SqlMapClient parse(Reader reader) {
        try {
            if (vars.sqlMapConfigConv != null) {
                reader = vars.sqlMapConfigConv.convertXml(reader);
            }
            usingStreams = false;
            parser.parse(reader);
            return vars.client;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred.  Cause: " + e, e);
        }
    }

    public SqlMapClient parse(InputStream inputStream, Properties props) {
        vars.properties = props;
        return parse(inputStream);
    }

    public SqlMapClient parse(InputStream inputStream) {
        try {
            if (vars.sqlMapConfigConv != null) {
                inputStream = vars.sqlMapConfigConv.convertXml(inputStream);
            }
            usingStreams = true;
            parser.parse(inputStream);
            return vars.client;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred.  Cause: " + e, e);
        }
    }

    private void addSqlMapConfigNodelets() {
        parser.addNodelet("/sqlMapConfig/end()", new Nodelet() {

            public void process(Node node) throws Exception {
                Iterator cacheNames = vars.client.getDelegate().getCacheModelNames();
                while (cacheNames.hasNext()) {
                    String cacheName = (String) cacheNames.next();
                    CacheModel cacheModel = vars.client.getDelegate().getCacheModel(cacheName);
                    Iterator statementNames = cacheModel.getFlushTriggerStatementNames();
                    while (statementNames.hasNext()) {
                        String statementName = (String) statementNames.next();
                        MappedStatement statement = vars.client.getDelegate().getMappedStatement(statementName);
                        if (statement != null) {
                            statement.addExecuteListener(cacheModel);
                        } else {
                            throw new RuntimeException("Could not find statement named '" + statementName + "' for use as a flush trigger for the cache model named '" + cacheName + "'.");
                        }
                    }
                }
            }
        });
    }

    private void addGlobalPropNodelets() {
        parser.addNodelet("/sqlMapConfig/properties", new Nodelet() {

            public void process(Node node) throws Exception {
                vars.errorCtx.setActivity("loading global properties");
                Properties attributes = NodeletUtils.parseAttributes(node, vars.properties);
                String resource = attributes.getProperty("resource");
                String url = attributes.getProperty("url");
                try {
                    Properties props = null;
                    if (resource != null) {
                        vars.errorCtx.setResource(resource);
                        props = Resources.getResourceAsProperties(resource);
                    } else if (url != null) {
                        vars.errorCtx.setResource(url);
                        props = Resources.getUrlAsProperties(url);
                    } else {
                        throw new RuntimeException("The " + "properties" + " element requires either a resource or a url attribute.");
                    }
                    if (vars.properties == null) {
                        vars.properties = props;
                    } else {
                        props.putAll(vars.properties);
                        vars.properties = props;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error loading properties.  Cause: " + e);
                }
            }
        });
    }

    private void addSettingsNodelets() {
        parser.addNodelet("/sqlMapConfig/settings", new Nodelet() {

            public void process(Node node) throws Exception {
                vars.errorCtx.setActivity("loading settings properties");
                Properties attributes = NodeletUtils.parseAttributes(node, vars.properties);
                String classInfoCacheEnabledAttr = attributes.getProperty("classInfoCacheEnabled");
                boolean classInfoCacheEnabled = (classInfoCacheEnabledAttr == null || "true".equals(classInfoCacheEnabledAttr));
                ClassInfo.setCacheEnabled(classInfoCacheEnabled);
                String lazyLoadingEnabledAttr = attributes.getProperty("lazyLoadingEnabled");
                boolean lazyLoadingEnabled = (lazyLoadingEnabledAttr == null || "true".equals(lazyLoadingEnabledAttr));
                vars.client.getDelegate().setLazyLoadingEnabled(lazyLoadingEnabled);
                String statementCachingEnabledAttr = attributes.getProperty("statementCachingEnabled");
                boolean statementCachingEnabled = (statementCachingEnabledAttr == null || "true".equals(statementCachingEnabledAttr));
                vars.client.getDelegate().setStatementCacheEnabled(statementCachingEnabled);
                String cacheModelsEnabledAttr = attributes.getProperty("cacheModelsEnabled");
                boolean cacheModelsEnabled = (cacheModelsEnabledAttr == null || "true".equals(cacheModelsEnabledAttr));
                vars.client.getDelegate().setCacheModelsEnabled(cacheModelsEnabled);
                String enhancementEnabledAttr = attributes.getProperty("enhancementEnabled");
                boolean enhancementEnabled = (enhancementEnabledAttr == null || "true".equals(enhancementEnabledAttr));
                try {
                    enhancementEnabled = enhancementEnabled && Resources.classForName("net.sf.cglib.proxy.InvocationHandler") != null;
                } catch (ClassNotFoundException e) {
                    enhancementEnabled = false;
                }
                vars.client.getDelegate().setEnhancementEnabled(enhancementEnabled);
                String useStatementNamespacesAttr = attributes.getProperty("useStatementNamespaces");
                vars.useStatementNamespaces = ("true".equals(useStatementNamespacesAttr));
                String maxTransactions = attributes.getProperty("maxTransactions");
                if (maxTransactions != null && Integer.parseInt(maxTransactions) > 0) {
                    vars.client.getDelegate().setMaxTransactions(Integer.parseInt(maxTransactions));
                }
                String maxRequests = attributes.getProperty("maxRequests");
                if (maxRequests != null && Integer.parseInt(maxRequests) > 0) {
                    vars.client.getDelegate().setMaxRequests(Integer.parseInt(maxRequests));
                }
                String maxSessions = attributes.getProperty("maxSessions");
                if (maxSessions != null && Integer.parseInt(maxSessions) > 0) {
                    vars.client.getDelegate().setMaxSessions(Integer.parseInt(maxSessions));
                }
                AccessPlanFactory.setBytecodeEnhancementEnabled(vars.client.getDelegate().isEnhancementEnabled());
                String defaultStatementTimeout = attributes.getProperty("defaultStatementTimeout");
                if (defaultStatementTimeout != null) {
                    try {
                        Integer defaultTimeout = Integer.valueOf(defaultStatementTimeout);
                        vars.defaultStatementTimeout = defaultTimeout;
                    } catch (NumberFormatException e) {
                        throw new SqlMapException("Specified defaultStatementTimeout is not a valid integer");
                    }
                }
            }
        });
    }

    private void addTypeAliasNodelets() {
        parser.addNodelet("/sqlMapConfig/typeAlias", new Nodelet() {

            public void process(Node node) throws Exception {
                Properties prop = NodeletUtils.parseAttributes(node, vars.properties);
                String alias = prop.getProperty("alias");
                String type = prop.getProperty("type");
                vars.typeHandlerFactory.putTypeAlias(alias, type);
            }
        });
    }

    private void addTypeHandlerNodelets() {
        parser.addNodelet("/sqlMapConfig/typeHandler", new Nodelet() {

            public void process(Node node) throws Exception {
                vars.errorCtx.setActivity("building a building custom type handler");
                try {
                    TypeHandlerFactory typeHandlerFactory = vars.client.getDelegate().getTypeHandlerFactory();
                    Properties prop = NodeletUtils.parseAttributes(node, vars.properties);
                    String jdbcType = prop.getProperty("jdbcType");
                    String javaType = prop.getProperty("javaType");
                    String callback = prop.getProperty("callback");
                    callback = typeHandlerFactory.resolveAlias(callback);
                    javaType = typeHandlerFactory.resolveAlias(javaType);
                    vars.errorCtx.setMoreInfo("Check the callback attribute '" + callback + "' (must be a classname).");
                    TypeHandler typeHandler;
                    Object impl = Resources.classForName(callback).newInstance();
                    if (impl instanceof TypeHandlerCallback) {
                        typeHandler = new CustomTypeHandler((TypeHandlerCallback) impl);
                    } else if (impl instanceof TypeHandler) {
                        typeHandler = (TypeHandler) impl;
                    } else {
                        throw new RuntimeException("The class '' is not a valid implementation of TypeHandler or TypeHandlerCallback");
                    }
                    vars.errorCtx.setMoreInfo("Check the javaType attribute '" + javaType + "' (must be a classname) or the jdbcType '" + jdbcType + "' (must be a JDBC type name).");
                    if (jdbcType != null && jdbcType.length() > 0) {
                        typeHandlerFactory.register(Resources.classForName(javaType), jdbcType, typeHandler);
                    } else {
                        typeHandlerFactory.register(Resources.classForName(javaType), typeHandler);
                    }
                } catch (Exception e) {
                    throw new SqlMapException("Error registering occurred.  Cause: " + e, e);
                }
                vars.errorCtx.setMoreInfo(null);
                vars.errorCtx.setObjectId(null);
            }
        });
    }

    private void addTransactionManagerNodelets() {
        parser.addNodelet("/sqlMapConfig/transactionManager/end()", new Nodelet() {

            public void process(Node node) throws Exception {
                vars.errorCtx.setActivity("configuring the transaction manager");
                Properties attributes = NodeletUtils.parseAttributes(node, vars.properties);
                String type = attributes.getProperty("type");
                type = vars.typeHandlerFactory.resolveAlias(type);
                TransactionManager txManager = null;
                try {
                    vars.errorCtx.setMoreInfo("Check the transaction manager type or class.");
                    TransactionConfig config = (TransactionConfig) Resources.instantiate(type);
                    config.setDataSource(vars.dataSource);
                    config.setMaximumConcurrentTransactions(vars.client.getDelegate().getMaxTransactions());
                    vars.errorCtx.setMoreInfo("Check the transactio nmanager properties or configuration.");
                    config.initialize(vars.txProps);
                    vars.errorCtx.setMoreInfo(null);
                    txManager = new TransactionManager(config);
                    txManager.setForceCommit("true".equals(attributes.getProperty("commitRequired")));
                } catch (Exception e) {
                    if (e instanceof SqlMapException) {
                        throw (SqlMapException) e;
                    } else {
                        throw new SqlMapException("Error initializing TransactionManager.  Could not instantiate TransactionConfig.  Cause: " + e, e);
                    }
                }
                vars.client.getDelegate().setTxManager(txManager);
            }
        });
        parser.addNodelet("/sqlMapConfig/transactionManager/property", new Nodelet() {

            public void process(Node node) throws Exception {
                Properties attributes = NodeletUtils.parseAttributes(node, vars.properties);
                String name = attributes.getProperty("name");
                String value = NodeletUtils.parsePropertyTokens(attributes.getProperty("value"), vars.properties);
                vars.txProps.setProperty(name, value);
            }
        });
        parser.addNodelet("/sqlMapConfig/transactionManager/dataSource", new Nodelet() {

            public void process(Node node) throws Exception {
                vars.dsProps = new Properties();
            }
        });
        parser.addNodelet("/sqlMapConfig/transactionManager/dataSource/end()", new Nodelet() {

            public void process(Node node) throws Exception {
                vars.errorCtx.setActivity("configuring the data source");
                Properties attributes = NodeletUtils.parseAttributes(node, vars.properties);
                String type = attributes.getProperty("type");
                type = vars.typeHandlerFactory.resolveAlias(type);
                try {
                    vars.errorCtx.setMoreInfo("Check the data source type or class.");
                    DataSourceFactory dsFactory = (DataSourceFactory) Resources.instantiate(type);
                    vars.errorCtx.setMoreInfo("Check the data source properties or configuration.");
                    dsFactory.initialize(vars.dsProps);
                    vars.dataSource = dsFactory.getDataSource();
                    vars.errorCtx.setMoreInfo(null);
                } catch (Exception e) {
                    if (e instanceof SqlMapException) {
                        throw (SqlMapException) e;
                    } else {
                        throw new SqlMapException("Error initializing DataSource.  Could not instantiate DataSourceFactory.  Cause: " + e, e);
                    }
                }
            }
        });
        parser.addNodelet("/sqlMapConfig/transactionManager/dataSource/property", new Nodelet() {

            public void process(Node node) throws Exception {
                Properties attributes = NodeletUtils.parseAttributes(node, vars.properties);
                String name = attributes.getProperty("name");
                String value = NodeletUtils.parsePropertyTokens(attributes.getProperty("value"), vars.properties);
                vars.dsProps.setProperty(name, value);
            }
        });
    }

    protected void addSqlMapNodelets() {
        parser.addNodelet("/sqlMapConfig/sqlMap", new Nodelet() {

            public void process(Node node) throws Exception {
                vars.errorCtx.setActivity("loading the SQL Map resource");
                Properties attributes = NodeletUtils.parseAttributes(node, vars.properties);
                String resource = attributes.getProperty("resource");
                String url = attributes.getProperty("url");
                if (usingStreams) {
                    InputStream inputStream = null;
                    if (resource != null) {
                        vars.errorCtx.setResource(resource);
                        inputStream = Resources.getResourceAsStream(resource);
                    } else if (url != null) {
                        vars.errorCtx.setResource(url);
                        inputStream = Resources.getUrlAsStream(url);
                    } else {
                        throw new SqlMapException("The <sqlMap> element requires either a resource or a url attribute.");
                    }
                    if (vars.sqlMapConv != null) {
                        inputStream = vars.sqlMapConv.convertXml(inputStream);
                    }
                    new SqlMapParser(vars).parse(inputStream);
                } else {
                    Reader reader = null;
                    if (resource != null) {
                        vars.errorCtx.setResource(resource);
                        reader = Resources.getResourceAsReader(resource);
                    } else if (url != null) {
                        vars.errorCtx.setResource(url);
                        reader = Resources.getUrlAsReader(url);
                    } else {
                        throw new SqlMapException("The <sqlMap> element requires either a resource or a url attribute.");
                    }
                    if (vars.sqlMapConv != null) {
                        reader = vars.sqlMapConv.convertXml(reader);
                    }
                    new SqlMapParser(vars).parse(reader);
                }
            }
        });
    }

    private void addResultObjectFactoryNodelets() {
        parser.addNodelet("/sqlMapConfig/resultObjectFactory", new Nodelet() {

            public void process(Node node) throws Exception {
                vars.errorCtx.setActivity("configuring the Result Object Factory");
                Properties attributes = NodeletUtils.parseAttributes(node, vars.properties);
                String type = attributes.getProperty("type");
                ResultObjectFactory rof;
                try {
                    rof = (ResultObjectFactory) Resources.instantiate(type);
                    vars.delegate.setResultObjectFactory(rof);
                } catch (Exception e) {
                    throw new SqlMapException("Error instantiating resultObjectFactory: " + type, e);
                }
            }
        });
        parser.addNodelet("/sqlMapConfig/resultObjectFactory/property", new Nodelet() {

            public void process(Node node) throws Exception {
                Properties attributes = NodeletUtils.parseAttributes(node, vars.properties);
                String name = attributes.getProperty("name");
                String value = NodeletUtils.parsePropertyTokens(attributes.getProperty("value"), vars.properties);
                vars.delegate.getResultObjectFactory().setProperty(name, value);
            }
        });
    }

    private void registerDefaultTypeAliases() {
        vars.typeHandlerFactory.putTypeAlias("JDBC", JdbcTransactionConfig.class.getName());
        vars.typeHandlerFactory.putTypeAlias("JTA", JtaTransactionConfig.class.getName());
        vars.typeHandlerFactory.putTypeAlias("EXTERNAL", ExternalTransactionConfig.class.getName());
        vars.typeHandlerFactory.putTypeAlias("SIMPLE", SimpleDataSourceFactory.class.getName());
        vars.typeHandlerFactory.putTypeAlias("DBCP", DbcpDataSourceFactory.class.getName());
        vars.typeHandlerFactory.putTypeAlias("JNDI", JndiDataSourceFactory.class.getName());
        vars.typeHandlerFactory.putTypeAlias("FIFO", FifoCacheController.class.getName());
        vars.typeHandlerFactory.putTypeAlias("LRU", LruCacheController.class.getName());
        vars.typeHandlerFactory.putTypeAlias("MEMORY", MemoryCacheController.class.getName());
        vars.typeHandlerFactory.putTypeAlias("OSCACHE", "com.ibatis.sqlmap.engine.cache.oscache.OSCacheController");
        vars.typeHandlerFactory.putTypeAlias("dom", DomTypeMarker.class.getName());
        vars.typeHandlerFactory.putTypeAlias("domCollection", DomCollectionTypeMarker.class.getName());
        vars.typeHandlerFactory.putTypeAlias("xml", XmlTypeMarker.class.getName());
        vars.typeHandlerFactory.putTypeAlias("xmlCollection", XmlCollectionTypeMarker.class.getName());
    }
}
