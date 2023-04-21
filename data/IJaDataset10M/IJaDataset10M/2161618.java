package xdoclet.modules.hibernate;

import java.util.Collection;
import xjavadoc.XClass;
import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xdoclet.tagshandler.PackageTagsHandler;
import xdoclet.util.Translator;

/**
 * Generate a SessionFactory facade that is capable of configuring Hibernate inline, as well as provide a convenient
 * method of switching later to other SessionFactory implementations.
 *
 * @author        Brian Topping (topping@bill2.com)
 * @created       August 8, 2003
 * @version       $Revision: 1.8 $
 * @ant.element   name="factoryclass" display-name="Factory class generator"
 *      parent="xdoclet.modules.hibernate.HibernateDocletTask" @ xdoclet.merge-file file="hibernate-factory-custom.xdt"
 *      relates-to="HibernateFactory.java" description="A Java unparsed entity or XDoclet template file, for custom
 *      elements to be included in the generated HibernateFactory.java"
 */
public class FactoryClassSubTask extends TemplateSubTask implements HibernateProperties {

    /**
     * Default template to use for hibernate files
     */
    private static String DEFAULT_TEMPLATE_FILE = "resources/hibernate-factory.xdt";

    private static String GENERATED_FILE_NAME = "HibernateFactory.java";

    private String dataSource;

    private String dialect;

    private String driver;

    private String username;

    private String password;

    private String factoryClass;

    private boolean useJndiFactory = false;

    private String jndiName;

    private String jdbcUrl;

    private String poolSize;

    /**
     * Constructor for the HibernateSubTask object
     */
    public FactoryClassSubTask() {
        setSubTaskName("factoryclass");
        setHavingClassTag("hibernate.class");
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getDialect() {
        return dialect;
    }

    public String getDriver() {
        return driver;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getFactoryClass() {
        return factoryClass;
    }

    public boolean isUseJndiFactory() {
        return useJndiFactory;
    }

    public Collection getJndiProperties() {
        throw new UnsupportedOperationException("FactoryClassSubTask does not have a jndiProperties attribute.");
    }

    /**
	 * We don't provide/have/use other mappings here for now.
	 * @see xdoclet.modules.hibernate.HibernateProperties#getOtherMappings()
	 */
    public Collection getOtherMappings() {
        throw new UnsupportedOperationException("FactoryClassSubTask doesn't have other mappings");
    }

    public Collection getOtherProperties() {
        throw new UnsupportedOperationException("FactoryClassSubTask does not have an otherProperties attribute.");
    }

    public String getTransactionManagerStrategy() {
        throw new UnsupportedOperationException("FactoryClassSubTask does not have a transactionManagerStrategy attribute.");
    }

    public String getUserTransactionName() {
        throw new UnsupportedOperationException("FactoryClassSubTask does not have a userTransactionName attribute.");
    }

    public String getUserName() {
        return username;
    }

    public boolean getUseOuterJoin() {
        throw new UnsupportedOperationException("FactoryClassSubTask does not have a useOuterJoin attribute.");
    }

    public boolean getShowSql() {
        throw new UnsupportedOperationException("FactoryClassSubTask does not have a showSql attribute.");
    }

    public String getJndiName() {
        return jndiName;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getPoolSize() {
        return poolSize;
    }

    /**
     * @param jndiName
     * @ant.not-required
     */
    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    /**
     * @param jdbcUrl
     */
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    /**
     * @param poolSize
     */
    public void setPoolSize(String poolSize) {
        this.poolSize = poolSize;
    }

    /**
     * The data source name to be generated into the factory
     *
     * @param dataSource
     */
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * The Hibernate DB dialect to be generated into the factory
     *
     * @param dialect
     */
    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    /**
     * The driver class name to be generated into the factory
     *
     * @param driver
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * The password to be generated into the factory
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * The username to be generated into the factory
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * The fully qualified class name of the generated factory
     *
     * @param factoryClass
     */
    public void setFactoryClass(String factoryClass) {
        this.factoryClass = factoryClass;
    }

    /**
     * @param useJndiFactory
     * @ant.not-required      No. Default is false.
     */
    public void setUseJndiFactory(boolean useJndiFactory) {
        this.useJndiFactory = useJndiFactory;
    }

    public void validateOptions() throws XDocletException {
        super.validateOptions();
        if (getFactoryClass() == null) {
            throw new XDocletException(Translator.getString(XDocletModulesHibernateMessages.class, XDocletModulesHibernateMessages.FACTORY_NAME_REQUIRED));
        }
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    public void execute() throws XDocletException {
        setDestinationFile(PackageTagsHandler.packageNameAsPathFor(factoryClass) + ".java");
        startProcess();
    }

    /**
     * Gets the GeneratedFileName attribute of the EntityCmpSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException {
        return PackageTagsHandler.packageNameAsPathFor(factoryClass) + ".java";
    }

    /**
     * Called when the engine is started
     *
     * @exception XDocletException  Thrown in case of problem
     */
    protected void engineStarted() throws XDocletException {
        System.out.println(Translator.getString(XDocletModulesHibernateMessages.class, XDocletModulesHibernateMessages.GENERATING_HIBERNATE_FACTORY_CLASS));
    }
}
