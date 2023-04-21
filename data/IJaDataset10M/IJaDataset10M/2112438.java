package xdoclet.modules.bea.wls.ejb;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.bea.wls.Version;
import xdoclet.modules.ejb.EjbDocletTask.EjbSpecVersion;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * This task can generate deployment descriptors for WLS 6.0, 6.1, 7.0 and 8.1. The destinationFile attribute is ignored
 * because this subtask creates multiple deployment descriptor files.
 *
 * @author               <a href="mailto:aslak.nospam@users.sf.net">Aslak Helles�y </a>
 * @author               <a href="mailto:jerome.bernard@xtremejava.com">Jerome Bernard</a>
 * @created              Sept 11, 2001
 * @ant.element          display-name="WebLogic Server" name="weblogic" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.30 $
 * @xdoclet.merge-file   file="weblogic-cmp-rdbms-beans.xml" relates-to="weblogic-cmp-rdbms-jar.xml" description="An XML
 *      unparsed entity containing weblogic-rdbms-bean for any CMP entity beans not processed by XDoclet."
 * @xdoclet.merge-file   file="weblogic-cmp-rdbms-relationships.xml" relates-to="weblogic-cmp-rdbms-jar.xml"
 *      description="An XML unparsed entity containing weblogic-rdbms-relation for any CMR relationships of CMP entity
 *      beans not processed by XDoclet."
 * @xdoclet.merge-file   file="weblogic-enterprise-beans.xml" relates-to="weblogic-ejb-jar.xml" description="An XML
 *      unparsed entity containing weblogic-enterprise-bean elements for any beans not processed by XDoclet."
 * @xdoclet.merge-file   file="weblogic-security-role-assignment.xml" relates-to="weblogic-ejb-jar.xml" description="An
 *      XML unparsed entity containing security-role-assignment elements."
 * @xdoclet.merge-file   file="weblogic-run-as-role-assignment.xml" relates-to="weblogic-ejb-jar.xml" description="An
 *      XML unparsed entity containing run-as-role-assignment elements." *
 */
public class WebLogicSubTask extends AbstractEjbDeploymentDescriptorSubTask {

    private static final String WEBLOGIC_DEFAULT_TEMPLATE_FILE = "resources/weblogic-ejb-jar-xml.xdt";

    private static final String WEBLOGIC_DD_FILE_NAME = "weblogic-ejb-jar.xml";

    private static final String WEBLOGIC_DD_PUBLICID_61 = "-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB//EN";

    private static final String WEBLOGIC_DD_PUBLICID_70 = "-//BEA Systems, Inc.//DTD WebLogic 7.0.0 EJB//EN";

    private static final String WEBLOGIC_DD_PUBLICID_81 = "-//BEA Systems, Inc.//DTD WebLogic 8.1.0 EJB//EN";

    private static final String WEBLOGIC_DD_SYSTEMID_61 = "http://www.bea.com/servers/wls600/dtd/weblogic-ejb-jar.dtd";

    private static final String WEBLOGIC_DD_SYSTEMID_70 = "http://www.bea.com/servers/wls700/dtd/weblogic-ejb-jar.dtd";

    private static final String WEBLOGIC_DD_SYSTEMID_81 = "http://www.bea.com/servers/wls810/dtd/weblogic-ejb-jar.dtd";

    private static final String WEBLOGIC_DTD_FILE_NAME_61 = "resources/weblogic600-ejb-jar.dtd";

    private static final String WEBLOGIC_DTD_FILE_NAME_70 = "resources/weblogic700-ejb-jar.dtd";

    private static final String WEBLOGIC_DTD_FILE_NAME_81 = "resources/weblogic810-ejb-jar.dtd";

    private static final String WEBLOGIC_CMP_DEFAULT_TEMPLATE_FILE = "resources/weblogic-cmp-rdbms-jar-xml.xdt";

    private static final String WEBLOGIC_CMP_DD_FILE_NAME = "weblogic-cmp-rdbms-jar.xml";

    private static final String WEBLOGIC_CMP11_PUBLICID_61 = "-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB 1.1 RDBMS Persistence//EN";

    private static final String WEBLOGIC_CMP11_PUBLICID_70 = "-//BEA Systems, Inc.//DTD WebLogic 7.0.0 EJB 1.1 RDBMS Persistence//EN";

    private static final String WEBLOGIC_CMP20_PUBLICID_61 = "-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB RDBMS Persistence//EN";

    private static final String WEBLOGIC_CMP20_PUBLICID_70 = "-//BEA Systems, Inc.//DTD WebLogic 7.0.0 EJB RDBMS Persistence//EN";

    private static final String WEBLOGIC_CMP20_PUBLICID_81 = "-//BEA Systems, Inc.//DTD WebLogic 8.1.0 EJB RDBMS Persistence//EN";

    private static final String WEBLOGIC_CMP11_SYSTEMID_61 = "http://www.bea.com/servers/wls600/dtd/weblogic-rdbms11-persistence-600.dtd";

    private static final String WEBLOGIC_CMP11_SYSTEMID_70 = "http://www.bea.com/servers/wls700/dtd/weblogic-rdbms11-persistence-700.dtd";

    private static final String WEBLOGIC_CMP20_SYSTEMID_61 = "http://www.bea.com/servers/wls600/dtd/weblogic-rdbms20-persistence-600.dtd";

    private static final String WEBLOGIC_CMP20_SYSTEMID_70 = "http://www.bea.com/servers/wls700/dtd/weblogic-rdbms20-persistence-700.dtd";

    private static final String WEBLOGIC_CMP20_SYSTEMID_81 = "http://www.bea.com/servers/wls810/dtd/weblogic-rdbms20-persistence-810.dtd";

    private static final String WEBLOGIC_CMP11_DTD_FILE_NAME_61 = "resources/weblogic-rdbms11-persistence-600.dtd";

    private static final String WEBLOGIC_CMP11_DTD_FILE_NAME_70 = "resources/weblogic-rdbms11-persistence-700.dtd";

    private static final String WEBLOGIC_CMP20_DTD_FILE_NAME_61 = "resources/weblogic-rdbms20-persistence-600.dtd";

    private static final String WEBLOGIC_CMP20_DTD_FILE_NAME_70 = "resources/weblogic-rdbms20-persistence-700.dtd";

    private static final String WEBLOGIC_CMP20_DTD_FILE_NAME_81 = "resources/weblogic-rdbms20-persistence-810.dtd";

    private static final String DEFAULT_PERSISTENCE = "weblogic";

    private String version = Version.VERSION_6_1;

    private String dataSource = "";

    private String poolName = "";

    private String createTables = "";

    private String persistence = DEFAULT_PERSISTENCE;

    private String validateDbSchemaWith = "";

    private String databaseType = "";

    private boolean orderDatabaseOperations = true;

    private boolean enableBatchOperations = true;

    private boolean enableBeanClassRedeploy = false;

    private URL ejbJarXml = null;

    private URL cmpRdbmsJarXml = null;

    public WebLogicSubTask() {
        ejbJarXml = getClass().getResource(WEBLOGIC_DEFAULT_TEMPLATE_FILE);
        cmpRdbmsJarXml = getClass().getResource(WEBLOGIC_CMP_DEFAULT_TEMPLATE_FILE);
    }

    /**
     * Gets the database type specified in the weblogic deployment descriptor. This is a WLS 7.0 and higher feature.
     * Possible values: DB2 INFORMIX ORACLE SQL_SERVER SYBASE POINTBASE
     *
     * @return   DatabaseType
     */
    public String getDatabaseType() {
        return databaseType;
    }

    /**
     * Gets the Datasource attribute of the WebLogicSubTask object
     *
     * @return   The Datasource value
     */
    public String getDatasource() {
        return dataSource;
    }

    public String getPoolname() {
        return poolName;
    }

    /**
     * Gets the Version attribute of the WebLogicSubTask object
     *
     * @return   The Version value
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the Createtables attribute of the WebLogicSubTask object
     *
     * @return   The Createtables value
     */
    public String getCreatetables() {
        return createTables;
    }

    public String getPersistence() {
        return persistence;
    }

    public String getValidateDbSchemaWith() {
        return validateDbSchemaWith;
    }

    public String getOrderDatabaseOperations() {
        return orderDatabaseOperations ? "True" : "False";
    }

    public String getEnableBatchOperations() {
        return enableBatchOperations ? "True" : "False";
    }

    public String getEnableBeanClassRedeploy() {
        return enableBeanClassRedeploy ? "True" : "False";
    }

    /**
     * Sets the template file for generation of weblogic-ejb-jar.xml.
     *
     * @param templateFile
     * @exception XDocletException
     * @see                         xdoclet.TemplateSubTask#setTemplateFile(java.io.File)
     */
    public void setTemplateFile(File templateFile) throws XDocletException {
        if (templateFile.exists()) {
            try {
                ejbJarXml = templateFile.toURL();
            } catch (MalformedURLException e) {
                throw new XDocletException(e.getMessage());
            }
        } else {
            throw new XDocletException("Couldn't find template for weblogic-ejb-jar.xml: " + templateFile.getAbsolutePath());
        }
    }

    /**
     * Sets the template file for generation of weblogic-cmp-rdbms-jar.xml.
     *
     * @param templateFile          the file name (real file!) of the template
     * @exception XDocletException
     * @ant.not-required            Yes if its a nested <template/> element.
     */
    public void setCmpTemplateFile(File templateFile) throws XDocletException {
        if (templateFile.exists()) {
            try {
                cmpRdbmsJarXml = templateFile.toURL();
            } catch (MalformedURLException e) {
                throw new XDocletException(e.getMessage());
            }
        } else {
            throw new XDocletException("Couldn't find template: " + templateFile.getAbsolutePath());
        }
    }

    /**
     * This flag is used for EJB container to delay all of the database operations in a transaction until the commit
     * time, automatically sort the database dependency between the operations, and send these operations to database in
     * such a way to avoid any database constrain errors. (Such as FK constrain). To turn off order-database-operations,
     * both order-database-operations and enable-batch-operations has to set to "false". Valid values are "true",
     * "True", "false" or "False" Used in: weblogic-rdbms-jar Since: WLS 8.1
     *
     * @param flag
     * @ant.not-required   No. Default is "true"
     */
    public void setOrderDatabaseOperations(boolean flag) {
        this.orderDatabaseOperations = flag;
    }

    /**
     * This flag is used for EJB container to perform batch operations. If this tag is set to true, the
     * order-database-operations will be set to true automatically, and EJB container will delay all of the database
     * operations in a transaction until the commit time. Valid values are "true", "True", "false" or "False" Used in:
     * weblogic-rdbms-jar Since: WLS 8.1
     *
     * @param flag
     * @ant.not-required   No. Default is "true"
     */
    public void setEnableBatchOperations(boolean flag) {
        this.enableBatchOperations = flag;
    }

    /**
     * This allows the EJB implementation class to be redeployed without redeploying the entire EJB module. Used in:
     * weblogic-ejb-jar Since: WLS 8.1
     *
     * @param flag
     * @ant.not-required   No. Default is "false"
     */
    public void setEnableBeanClassRedeploy(boolean flag) {
        this.enableBeanClassRedeploy = flag;
    }

    /**
     * Sets the database type specified in the weblogic-cmp-rdbms-jar.xml deployment descriptor. This is a WLS 7.0 and
     * higher feature. Possible values: DB2 INFORMIX ORACLE SQL_SERVER SYBASE POINTBASE
     *
     * @param databaseType
     * @ant.not-required    No, only used with 7.0 upwards, and optional even then.
     */
    public void setDatabaseType(DatabaseTypes databaseType) {
        this.databaseType = databaseType.getValue();
    }

    /**
     * Specifies a default value for the pool-name element in the CMP descriptor, to use if no weblogic.pool-name tag
     * appears on a bean (only applies when ejbspec=1.1)
     *
     * @param s
     * @ant.not-required
     */
    public void setPoolname(String s) {
        poolName = s;
    }

    /**
     * Specifies a default value for the data-source-name element in the CMP descriptor, to use if no
     * weblogic.data-source-name tag appears on a bean.
     *
     * @param dataSource
     * @ant.not-required
     */
    public void setDatasource(String dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Sets the target WebLogic version to generate for. Possible values are 6.1, 7.0 and 8.1
     *
     * @param version      The new Version value
     * @ant.not-required   No, default is 6.1
     */
    public void setVersion(Version version) {
        this.version = version.getValue();
    }

    /**
     * Sets the persistence type to use. Useful if you're using a different persistence manager like MVCSoft
     *
     * @param persistence
     * @ant.not-required   No, default is "weblogic"
     */
    public void setPersistence(String persistence) {
        this.persistence = persistence;
    }

    /**
     * If "True" or "CreateOnly", then at deployment time if there is no Table in the Database for a CMP Bean, the
     * Container will attempt to CREATE the Table (based on information found in the deployment files and in the Bean
     * Class). Valid values are "True" and "False" for WLS Servers &lt; Version 8.1 and "CreateOnly", "DropAndCreate",
     * "DropAndCreateAlways", "AlterOrCreate" and "Disabled" for WLS Servers &gt;= 8.1.
     *
     * @param flag         The new Createtables value
     * @ant.not-required   No, default is "False" for WLS Servers &lt; Version 8.1 and "Disabled" for WLS Servers &gt;=
     *      8.1
     */
    public void setCreatetables(CreateTablesType flag) {
        this.createTables = flag.getValue();
    }

    /**
     * The CMP subsystem checks that beans have been mapped to a valid database schema at deployment time. A value of
     * 'MetaData' means that JDBC metadata is used to validate the schema. A value of 'TableQuery' means that tables are
     * queried directly to ascertain that they have the schema expected by the CMP runtime.
     *
     * @param type
     * @ant.not-required
     */
    public void setValidateDbSchemaWith(ValidateDbSchemaWithTypes type) {
        validateDbSchemaWith = type.getValue();
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException {
        CreateTablesType tblType = new CreateTablesType();
        String versionStr = getVersion();
        int index = tblType.indexOfValue(getCreatetables());
        int index_lo_8_1 = tblType.indexOfValue(CreateTablesType.TRUE);
        int index_hi_8_1 = tblType.indexOfValue(CreateTablesType.ALTER_OR_CREATE);
        if (versionStr.compareTo(Version.VERSION_8_1) < 0) {
            if (index > index_lo_8_1) {
                throw new XDocletException("Argument for createtables must be one of (" + CreateTablesType.TRUE + "|" + CreateTablesType.FALSE + ") if version attribute is < 8.1");
            }
        } else {
            if ((index <= index_lo_8_1) || (index > index_hi_8_1)) {
                throw new XDocletException("Argument for createtables must be one of (" + CreateTablesType.DISABLED + "|" + CreateTablesType.CREATE_ONLY + "|" + CreateTablesType.DROP_AND_CREATE + "|" + CreateTablesType.DROP_AND_CREATE_ALWAYS + "|" + CreateTablesType.ALTER_OR_CREATE + ") if version attribute is >= 8.1");
            }
        }
    }

    /**
     * @exception XDocletException
     * @see                         xdoclet.SubTask#execute()
     */
    public void execute() throws XDocletException {
        setDestinationFile(WEBLOGIC_DD_FILE_NAME);
        setTemplateURL(ejbJarXml);
        if (getVersion().equals(Version.VERSION_6_1) || getVersion().equals(Version.VERSION_6_0)) {
            setPublicId(WEBLOGIC_DD_PUBLICID_61);
            setSystemId(WEBLOGIC_DD_SYSTEMID_61);
            setDtdURL(getClass().getResource(WEBLOGIC_DTD_FILE_NAME_61));
        } else if (getVersion().equals(Version.VERSION_7_0)) {
            setPublicId(WEBLOGIC_DD_PUBLICID_70);
            setSystemId(WEBLOGIC_DD_SYSTEMID_70);
            setDtdURL(getClass().getResource(WEBLOGIC_DTD_FILE_NAME_70));
        } else {
            setPublicId(WEBLOGIC_DD_PUBLICID_81);
            setSystemId(WEBLOGIC_DD_SYSTEMID_81);
            setDtdURL(getClass().getResource(WEBLOGIC_DTD_FILE_NAME_81));
        }
        startProcess();
        if (atLeastOneCmpEntityBeanExists()) {
            if (DEFAULT_PERSISTENCE.equals(getPersistence())) {
                setDestinationFile(WEBLOGIC_CMP_DD_FILE_NAME);
                setTemplateURL(cmpRdbmsJarXml);
                String specVers = (String) getContext().getConfigParam("EjbSpec");
                if (specVers.equals(EjbSpecVersion.EJB_2_1)) {
                    specVers = EjbSpecVersion.EJB_2_0;
                    LogUtil.getLog(getClass(), "execute").warn("EJB spec version 2.1 unknown for WLS subtask. Falling back to 2.0");
                }
                if (specVers.equals(EjbSpecVersion.EJB_1_1)) {
                    if (getVersion().equals(Version.VERSION_6_1) || getVersion().equals(Version.VERSION_6_0)) {
                        setPublicId(WEBLOGIC_CMP11_PUBLICID_61);
                        setSystemId(WEBLOGIC_CMP11_SYSTEMID_61);
                        setDtdURL(getClass().getResource(WEBLOGIC_CMP11_DTD_FILE_NAME_61));
                    } else {
                        setPublicId(WEBLOGIC_CMP11_PUBLICID_70);
                        setSystemId(WEBLOGIC_CMP11_SYSTEMID_70);
                        setDtdURL(getClass().getResource(WEBLOGIC_CMP11_DTD_FILE_NAME_70));
                    }
                } else if (specVers.equals(EjbSpecVersion.EJB_2_0)) {
                    if (getVersion().equals(Version.VERSION_6_1) || getVersion().equals(Version.VERSION_6_0)) {
                        setPublicId(WEBLOGIC_CMP20_PUBLICID_61);
                        setSystemId(WEBLOGIC_CMP20_SYSTEMID_61);
                        setDtdURL(getClass().getResource(WEBLOGIC_CMP20_DTD_FILE_NAME_61));
                    } else if (getVersion().equals(Version.VERSION_7_0)) {
                        setPublicId(WEBLOGIC_CMP20_PUBLICID_70);
                        setSystemId(WEBLOGIC_CMP20_SYSTEMID_70);
                        setDtdURL(getClass().getResource(WEBLOGIC_CMP20_DTD_FILE_NAME_70));
                    } else {
                        setPublicId(WEBLOGIC_CMP20_PUBLICID_81);
                        setSystemId(WEBLOGIC_CMP20_SYSTEMID_81);
                        setDtdURL(getClass().getResource(WEBLOGIC_CMP20_DTD_FILE_NAME_81));
                    }
                } else {
                    throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.UNSUPPORTED_EJB_SPEC, new String[] { getContext().getConfigParam("EjbSpec").toString() }));
                }
            } else {
                LogUtil.getLog(getClass(), "execute").warn(Translator.getString(XDocletModulesBeaWlsEjbMessages.class, XDocletModulesBeaWlsEjbMessages.NON_WEBLOGIC_PERSISTENCE, new String[] { getPersistence() }));
            }
            startProcess();
        }
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    protected void engineStarted() throws XDocletException {
        if (getDestinationFile().equals(WEBLOGIC_DD_FILE_NAME)) {
            System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[] { WEBLOGIC_DD_FILE_NAME }));
        } else if (getDestinationFile().equals(WEBLOGIC_CMP_DD_FILE_NAME)) {
            System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[] { WEBLOGIC_CMP_DD_FILE_NAME }));
        }
    }

    /**
     * Legal values of the validate-db-schema-with in weblogic-rdbms20-persistence-600.dtd
     *
     * @created   17. juni 2002
     */
    public static class ValidateDbSchemaWithTypes extends org.apache.tools.ant.types.EnumeratedAttribute {

        public static final String META_DATA = "MetaData";

        public static final String TABLE_QUERY = "TableQuery";

        /**
         * Gets the Values attribute of the ValidateDbSchemaWithTypes object
         *
         * @return   The Values value
         */
        public java.lang.String[] getValues() {
            return (new java.lang.String[] { META_DATA, TABLE_QUERY });
        }
    }

    /**
     * Legal values of the database-type in weblogic-rdbms20-persistence-700.dtd
     *
     * @created   02 april 2003
     */
    public static class DatabaseTypes extends org.apache.tools.ant.types.EnumeratedAttribute {

        public static final String DB2 = "DB2";

        public static final String INFORMIX = "INFORMIX";

        public static final String ORACLE = "ORACLE";

        public static final String SQL_SERVER = "SQL_SERVER";

        public static final String SYBASE = "SYBASE";

        public static final String POINTBASE = "POINTBASE";

        /**
         * Gets the Values attribute of the DatabaseTypes object
         *
         * @return   The Values value
         */
        public java.lang.String[] getValues() {
            return (new java.lang.String[] { DB2, INFORMIX, ORACLE, SQL_SERVER, SYBASE, POINTBASE });
        }
    }

    /**
     * @created   30 july 2004
     */
    public static class CreateTablesType extends org.apache.tools.ant.types.EnumeratedAttribute {

        public static final String FALSE = "False";

        public static final String TRUE = "True";

        public static final String DISABLED = "Disabled";

        public static final String CREATE_ONLY = "CreateOnly";

        public static final String DROP_AND_CREATE = "DropAndCreate";

        public static final String DROP_AND_CREATE_ALWAYS = "DropAndCreateAlways";

        public static final String ALTER_OR_CREATE = "AlterOrCreate";

        /**
         * Gets the Values attribute of the createTables object
         *
         * @return   The Values value
         */
        public java.lang.String[] getValues() {
            return (new java.lang.String[] { FALSE, TRUE, DISABLED, CREATE_ONLY, DROP_AND_CREATE, DROP_AND_CREATE_ALWAYS, ALTER_OR_CREATE });
        }
    }
}
