package org.gyx.elips.config;

import org.gyx.common.sql.SqlAccess;
import org.gyx.common.sql.SqlConnectionProvider;
import org.gyx.elips.ApplicationInitializer;
import java.io.*;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.Vector;

/**
 * @author xm
 */
public class ElipsPropertiesConfig {

    protected static Long VERSION_MAJOR = 1L;

    protected static Long VERSION_MINOR = 7L;

    protected static Long VERSION_BUILD = 0L;

    protected String look = "basic";

    protected String language = "english";

    protected String appliUrl = "http://localhost:8080/Elips/index.jsp";

    protected String uploadDir = "$(TOMCAT)/webapps/Elips/upload";

    protected String tempDir = "c:/temp";

    protected String debugEmail = "NO";

    protected String debugAddress = "admin@company.local";

    protected String printSQL = "NO";

    protected String contactEmail = "admin@company.local";

    protected String contactLabel = "YourCompanyLabel";

    protected String mailSender = "Elips_application";

    protected String appliLabel = "Elips";

    protected String appliSubTitle = "ELectronic Issue Processing Software";

    protected String smtpHost = "EXCHANGE";

    protected String smtpUser = "none";

    protected String smtpPwd = "none";

    protected String emailFormat = "html";

    protected String version = VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_BUILD;

    protected String typeBase = "MySQL";

    protected String companyDatabase = "COMPANY";

    protected String elipsDatabase = "ELIPS_PROD";

    protected String connectionStringMySQL = "jdbc:mysql://localhost/elips_prod?user=elipslog&password=elipspass";

    protected String connectionStringPostgreSQL = "jdbc:postgresql://localhost/elips_prod?user=elipslog&password=elipspass&charset=Cp1251";

    protected String connectionStringSqlServer = "jdbc:jtds:sqlserver://localhost:1433;DatabaseName=ELIPS_PROD;User=elipslog;Password=elipspass";

    protected String connectionStringOracle = "jdbc:oracle:thin:@localhost:1521:schemaName;User=elipslog;Password=elipspass";

    protected String driverStringMySQL = "org.gjt.mm.mysql.Driver";

    protected String driverStringPostgreSQL = "org.postgresql.Driver";

    protected String driverStringSqlServer = "net.sourceforge.jtds.jdbc.Driver";

    protected String driverStringOracle = "oracle.jdbc.driver.OracleDriver";

    protected String configUser = "admin";

    protected String configPwd = "root";

    protected String dateFormat = "yyyy-MM-dd";

    protected String authenticationClass = "ELIPS";

    protected String authenticationDomains = "ELIPS";

    public ElipsPropertiesConfig() {
        this.init();
    }

    /**
	 * @return String
	 */
    public String getAppliLabel() {
        return appliLabel;
    }

    /**
	 * @return String
	 */
    public String getAppliSubTitle() {
        return appliSubTitle;
    }

    /**
	 * @return String
	 */
    public String getAppliUrl() {
        return appliUrl;
    }

    /**
	 * @return String
	 */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
	 * @return String
	 */
    public String getContactLabel() {
        return contactLabel;
    }

    /**
	 * @return String
	 */
    public String getDebugAddress() {
        return debugAddress;
    }

    /**
	 * @return String
	 */
    public String getDebugEmail() {
        return debugEmail;
    }

    /**
	 * @return String
	 */
    public String getLanguage() {
        return language;
    }

    /**
	 * @return String
	 */
    public Vector getLanguages() {
        String aPath = null;
        try {
            aPath = URLDecoder.decode(Class.forName("org.gyx.elips.internationalization.LangMsg").getResource("english.properties").getPath(), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        aPath = (new File(aPath)).getParent();
        Vector languages = new Vector();
        File[] files = new File(aPath).listFiles();
        for (int i = 0; i < files.length; i++) {
            File aFile = files[i];
            String aStr = aFile.getName();
            if (aStr.endsWith(".properties") & (!aStr.startsWith("jsp_"))) {
                int index = aStr.indexOf('.');
                languages.add(aStr.substring(0, index));
            }
            ;
        }
        ;
        return languages;
    }

    /**
	 * @return String
	 */
    public Vector getLooks() {
        String aPath = null;
        try {
            aPath = Class.forName("org.gyx.elips.internationalization.LangMsg").getResource("english.properties").getPath().replaceAll("%20", " ");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ;
        int index = aPath.indexOf("WEB-INF");
        aPath = aPath.substring(0, index) + "images";
        Vector looks = new Vector();
        File[] files = new File(aPath).listFiles();
        for (int i = 0; i < files.length; i++) {
            File aFile = files[i];
            if (!aFile.getName().equals("CVS")) looks.add(aFile.getName());
        }
        ;
        return looks;
    }

    public Vector getTypesBase() {
        Vector typesBase = new Vector();
        typesBase.add("MySQL");
        typesBase.add("PostgreSQL");
        typesBase.add("SqlServer");
        typesBase.add("Oracle");
        return typesBase;
    }

    /**
	 * @return String
	 */
    public String getMailSender() {
        return mailSender;
    }

    /**
	 * @return String
	 */
    public String getPrintSQL() {
        return printSQL;
    }

    /**
	 * @return String
	 */
    public String getTempDir() {
        return tempDir;
    }

    /**
	 * @return String
	 */
    public String getUploadDir() {
        return uploadDir;
    }

    /**
	 * Sets the appliLabel.
	 * @param appliLabel The appliLabel to set
	 */
    public void setAppliLabel(String appliLabel) {
        this.appliLabel = appliLabel;
    }

    /**
	 * Sets the appliSubTitle.
	 * @param appliSubTitle The appliSubTitle to set
	 */
    public void setAppliSubTitle(String appliSubTitle) {
        this.appliSubTitle = appliSubTitle;
    }

    /**
	 * Sets the appliUrl.
	 * @param appliUrl The appliUrl to set
	 */
    public void setAppliUrl(String appliUrl) {
        this.appliUrl = appliUrl;
    }

    /**
	 * Sets the contactEmail.
	 * @param contactEmail The contactEmail to set
	 */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
	 * Sets the contactLabel.
	 * @param contactLabel The contactLabel to set
	 */
    public void setContactLabel(String contactLabel) {
        this.contactLabel = contactLabel;
    }

    /**
	 * Sets the debugAddress.
	 * @param debugAddress The debugAddress to set
	 */
    public void setDebugAddress(String debugAddress) {
        this.debugAddress = debugAddress;
    }

    /**
	 * Sets the debugEmail.
	 * @param debugEmail The debugEmail to set
	 */
    public void setDebugEmail(String debugEmail) {
        this.debugEmail = debugEmail;
    }

    /**
	 * Sets the language.
	 * @param language The language to set
	 */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
	 * Sets the mailSender.
	 * @param mailSender The mailSender to set
	 */
    public void setMailSender(String mailSender) {
        this.mailSender = mailSender;
    }

    /**
	 * Sets the printSQL.
	 * @param printSQL The printSQL to set
	 */
    public void setPrintSQL(String printSQL) {
        this.printSQL = printSQL;
    }

    /**
	 * Sets the tempDir.
	 * @param tempDir The tempDir to set
	 */
    public void setTempDir(String tempDir) {
        this.tempDir = tempDir;
    }

    /**
	 * Sets the uploadDir.
	 * @param uploadDir The uploadDir to set
	 */
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public void init() {
        Properties props = null;
        try {
            props = ApplicationInitializer.readConfigFile("org.gyx.elips.config.ElipsPropertiesConfig", "elips.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
        look = props.getProperty("LOOK").trim();
        language = props.getProperty("LANGUAGE").trim();
        appliUrl = props.getProperty("URL_APPLI").trim();
        uploadDir = props.getProperty("UPLOADDIR").trim();
        tempDir = props.getProperty("TEMP_DIR").trim();
        debugEmail = props.getProperty("EMAIL_SPECIFIC_BOOL").trim();
        debugAddress = props.getProperty("EMAIL_SPECIFIC_ADDR").trim();
        printSQL = props.getProperty("PRINT_SQL").trim();
        contactEmail = props.getProperty("CONTACT_MAIL").trim();
        contactLabel = props.getProperty("CONTACT_LIBELLE").trim();
        mailSender = props.getProperty("MAIL_SENDER").trim();
        appliLabel = props.getProperty("APPLICATION_LABEL").trim();
        appliSubTitle = props.getProperty("APPLICATION_SUB_TITLE").trim();
        smtpHost = props.getProperty("SMTPHOST").trim();
        smtpUser = props.getProperty("SMTPUSER").trim();
        smtpPwd = props.getProperty("SMTPPWD").trim();
        emailFormat = props.getProperty("MAILCONTENTTYPE").trim();
        typeBase = props.getProperty("TYPE_BASE").trim();
        companyDatabase = props.getProperty("COMPANY_DATABASE").trim();
        elipsDatabase = props.getProperty("ELIPS_DATABASE").trim();
        connectionStringMySQL = props.getProperty("CONNECTION_STRING_MySQL").trim();
        connectionStringPostgreSQL = props.getProperty("CONNECTION_STRING_PostgreSQL").trim();
        connectionStringSqlServer = props.getProperty("CONNECTION_STRING_SqlServer").trim();
        connectionStringOracle = props.getProperty("CONNECTION_STRING_Oracle").trim();
        driverStringMySQL = props.getProperty("DRIVER_MySQL").trim();
        driverStringPostgreSQL = props.getProperty("DRIVER_PostgreSQL").trim();
        driverStringSqlServer = props.getProperty("DRIVER_SqlServer").trim();
        driverStringOracle = props.getProperty("DRIVER_Oracle").trim();
        dateFormat = props.getProperty("SQL_DATE_FORMAT").trim();
        configUser = props.getProperty("CONFIGUSER").trim();
        configPwd = props.getProperty("CONFIGPWD").trim();
        authenticationClass = props.getProperty("AUTHENTICATIONCLASS", "ELIPS").trim();
        authenticationDomains = props.getProperty("AUTHENTICATIONDOMAINS", "ELIPS").trim();
    }

    public void configure() {
        saveElipsPropertiesFile();
        createDirs();
        updateDatabase();
    }

    public void updateDatabase() {
        Properties propertiesConfig = null;
        try {
            propertiesConfig = ApplicationInitializer.readConfigFile("org.gyx.elips.config.ElipsPropertiesConfig", "elips.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SqlConnectionProvider aConnectionProvider;
        try {
            aConnectionProvider = new SqlConnectionProvider(propertiesConfig);
            aConnectionProvider.loadDriver();
            SqlAccess theSqlAccess = new SqlAccess(aConnectionProvider);
            theSqlAccess.setPrintSql(true);
            String fileName = URLDecoder.decode(Class.forName("org.gyx.elips.internationalization.LangMsg").getResource("db_upd_" + language + ".sql").getFile(), "ISO-8859-1");
            FileReader aFileReader = new FileReader(fileName);
            StringBuffer strBuf = new StringBuffer();
            theSqlAccess.executeUpdate("DELETE FROM parameter_value");
            int i = aFileReader.read();
            while (i > -1) {
                strBuf.append((char) i);
                if (((char) i) == ';') {
                    theSqlAccess.executeUpdate(strBuf.toString());
                    strBuf = new StringBuffer();
                }
                i = aFileReader.read();
            }
            aFileReader.close();
            theSqlAccess.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void saveElipsPropertiesFile() {
        try {
            String aPath = URLDecoder.decode(Class.forName("org.gyx.elips.config.ElipsPropertiesConfig").getResource("elips.properties").getFile(), "ISO-8859-1");
            File aFile = new File(aPath);
            if (aFile.exists()) aFile.renameTo(new File(aPath + ".old"));
            FileWriter aFileWriter = new FileWriter(new File(aPath));
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("#\n");
            strBuf.append("#\n");
            strBuf.append("# Elips configuration \n");
            strBuf.append("#\n");
            strBuf.append("#\n");
            strBuf.append("\n");
            strBuf.append("#\n");
            strBuf.append("# Application specific\n");
            strBuf.append("#\n");
            strBuf.append("VERSION_APPLICATION=" + version + "\n");
            strBuf.append("#english, french, german...\n");
            strBuf.append("LANGUAGE=" + language + "\n");
            strBuf.append("LOOK=" + look + "\n");
            strBuf.append("\n");
            strBuf.append("#\n");
            strBuf.append("# BD definition\n");
            strBuf.append("#\n");
            strBuf.append("TYPE_BASE=" + typeBase + "\n");
            strBuf.append("\n");
            strBuf.append("DRIVER_MySQL=" + driverStringMySQL + "\n");
            strBuf.append("COMPANY_DATABASE=" + companyDatabase + "\n");
            strBuf.append("ELIPS_DATABASE=" + elipsDatabase + "\n");
            strBuf.append("CONNECTION_STRING_MySQL=" + connectionStringMySQL + "\n");
            strBuf.append("\n");
            strBuf.append("DRIVER_PostgreSQL=" + driverStringPostgreSQL + "\n");
            strBuf.append("CONNECTION_STRING_PostgreSQL=" + connectionStringPostgreSQL + "\n");
            strBuf.append("\n");
            strBuf.append("DRIVER_SqlServer=" + driverStringSqlServer + "\n");
            strBuf.append("CONNECTION_STRING_SqlServer=" + connectionStringSqlServer + "\n");
            strBuf.append("\n");
            strBuf.append("DRIVER_Oracle=" + driverStringOracle + "\n");
            strBuf.append("CONNECTION_STRING_Oracle=" + connectionStringOracle + "\n");
            strBuf.append("\n");
            strBuf.append("SQL_DATE_FORMAT=" + dateFormat + "\n");
            strBuf.append("\n");
            strBuf.append("#\n");
            strBuf.append("# SMTP mail address\n");
            strBuf.append("#\n");
            strBuf.append("SMTPHOST=" + smtpHost + "\n");
            strBuf.append("\n");
            strBuf.append("SMTPUSER=" + smtpUser + "\n");
            strBuf.append("\n");
            strBuf.append("SMTPPWD=" + smtpPwd + "\n");
            strBuf.append("\n");
            strBuf.append("MAILCONTENTTYPE=" + emailFormat + "\n");
            strBuf.append("\n");
            strBuf.append("#\n");
            strBuf.append("# URL of the application (used for instance in mails)\n");
            strBuf.append("#\n");
            strBuf.append("URL_APPLI=" + appliUrl + "\n");
            strBuf.append("\n");
            strBuf.append("#\n");
            strBuf.append("# Directory were are stored  the \" attached strBufs \"  (uploaded strBufs)\n");
            strBuf.append("# The advantage to store them at the same place than Mysql strBufs  \n");
            strBuf.append("# is that we can save everything at the same time.\n");
            strBuf.append("#\n");
            strBuf.append("UPLOADDIR=" + uploadDir + "\n");
            strBuf.append("\n");
            strBuf.append("#\n");
            strBuf.append("# Temp dir\n");
            strBuf.append("# Used for storing temporary uploaded strBufs (eg CVS and Excel)\n");
            strBuf.append("#\n");
            strBuf.append("TEMP_DIR=" + tempDir + "\n");
            strBuf.append("\n");
            strBuf.append("#\n");
            strBuf.append("# EMAIL_SPECIFIC\n");
            strBuf.append("# --------------\n");
            strBuf.append("# Defined  if the  emails must be sent to true senders or if they must sent to a specific sender.\n");
            strBuf.append("# BOOL = 'YES' is the value generally used for the debugging ->   emails are all sent to EMAIL_SPECIFIC_ADDR\n");
            strBuf.append("# BOOl = 'NO' is the normal value : emails will be sent to the real senders.\n");
            strBuf.append("#\n");
            strBuf.append("EMAIL_SPECIFIC_BOOL=" + debugEmail + "\n");
            strBuf.append("EMAIL_SPECIFIC_ADDR=" + debugAddress + "\n");
            strBuf.append("\n");
            strBuf.append("#\n");
            strBuf.append("# PRINT_SQL\n");
            strBuf.append("# ---------\n");
            strBuf.append("# 'YES' ->  requests SQL are printed on the standard output  \n");
            strBuf.append("# 'NO' ->  requests SQL are not printed \n");
            strBuf.append("#\n");
            strBuf.append("PRINT_SQL=" + printSQL + "\n");
            strBuf.append("\n");
            strBuf.append("#\n");
            strBuf.append("# CONTACT (used in  emails, and footer)\n");
            strBuf.append("#\n");
            strBuf.append("CONTACT_MAIL=" + contactEmail + "\n");
            strBuf.append("CONTACT_LIBELLE=" + contactLabel + "\n");
            strBuf.append("\n");
            strBuf.append("#\n");
            strBuf.append("# MAIL_SENDER : name of the sender in automatic mails\n");
            strBuf.append("#\n");
            strBuf.append("MAIL_SENDER=" + mailSender + "\n");
            strBuf.append("\n");
            strBuf.append("#\n");
            strBuf.append("# APPLICATION_LABEL (used in mails)\n");
            strBuf.append("#\n");
            strBuf.append("APPLICATION_LABEL=" + appliLabel + "\n");
            strBuf.append("APPLICATION_SUB_TITLE=" + appliSubTitle + "\n");
            strBuf.append("\n");
            strBuf.append("#\n");
            strBuf.append("# CONFIGURATION AUTHENTIFICATION\n");
            strBuf.append("#\n");
            strBuf.append("CONFIGUSER=" + configUser + "\n");
            strBuf.append("CONFIGPWD=" + configPwd + "\n");
            strBuf.append("# AUTHENTIFICATION CLASS. ENTER ELIPS TO USE REGULAR ELIPS.COMPANY.USER.PASSWORD COLUMN\n");
            strBuf.append("#\n");
            strBuf.append("AUTHENTICATIONCLASS=" + authenticationClass + "\n");
            strBuf.append("# AUTHENTIFICATION DOMAINS TO BE USED WITH NON-ELIPS AUTHENTICATORS\n");
            strBuf.append("#\n");
            strBuf.append("AUTHENTICATIONDOMAINS=" + authenticationDomains + "\n");
            aFileWriter.write(strBuf.toString());
            aFileWriter.flush();
            aFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
        }
    }

    public void createDirs() {
        File aDir = new File(uploadDir);
        if (!aDir.exists()) aDir.mkdir();
        aDir = new File(tempDir);
        if (!aDir.exists()) aDir.mkdir();
    }

    /**
	 * @return String
	 */
    public String getSmtpHost() {
        return smtpHost;
    }

    /**
	 * Sets the smtpHost.
	 * @param smtpHost The smtpHost to set
	 */
    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    /**
	 * @return String 
	 */
    public String getVersion() {
        return version;
    }

    /**
	 * Sets the version.
	 * @param version The version to set
	 */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
	 * @return String
	 */
    public String getConnectionStringMySQL() {
        return connectionStringMySQL;
    }

    /**
	 * Sets the connectionStringMySQL.
	 * @param connectionStringMySQL The connectionStringMySQL to set
	 */
    public void setConnectionStringMySQL(String connectionStringMySQL) {
        this.connectionStringMySQL = connectionStringMySQL;
    }

    /**
	 * @return String
	 */
    public String getConnectionStringPostgreSQL() {
        return connectionStringPostgreSQL;
    }

    /**
	 * Sets the connectionStringPostgreSQL.
	 * @param connectionStringPostgreSQL The connectionStringPostgreSQL to set
	 */
    public void setConnectionStringPostgreSQL(String connectionStringPostgreSQL) {
        this.connectionStringPostgreSQL = connectionStringPostgreSQL;
    }

    /**
	 * @return String
	 */
    public String getLook() {
        return look;
    }

    /**
	 * Sets the look.
	 * @param look The look to set
	 */
    public void setLook(String look) {
        this.look = look;
    }

    /**
	 * @return String
	 */
    public String getTypeBase() {
        return typeBase;
    }

    /**
	 * Sets the typeBase.
	 * @param typeBase The typeBase to set
	 */
    public void setTypeBase(String typeBase) {
        this.typeBase = typeBase;
    }

    /**
	 * @return String
	 */
    public String getConnectionStringSqlServer() {
        return connectionStringSqlServer;
    }

    /**
	 * Sets the connectionStringSqlServer.
	 * @param connectionStringSqlServer The connectionStringSqlServer to set
	 */
    public void setConnectionStringSqlServer(String connectionStringSqlServer) {
        this.connectionStringSqlServer = connectionStringSqlServer;
    }

    /**
	 * @return String
	 */
    public String getConnectionStringOracle() {
        return connectionStringOracle;
    }

    /**
	 * Sets the connectionStringOracle
	 * @param connectionStringOracle The connectionStringSqlServer to set
	 */
    public void setConnectionStringOracle(String connectionStringOracle) {
        this.connectionStringOracle = connectionStringOracle;
    }

    /**
	 * @return Returns the smtpPwd.
	 */
    public String getSmtpPwd() {
        return smtpPwd;
    }

    /**
	 * @param smtpPwd The smtpPwd to set.
	 */
    public void setSmtpPwd(String smtpPwd) {
        this.smtpPwd = smtpPwd;
    }

    /**
	 * @return Returns the smtpUser.
	 */
    public String getSmtpUser() {
        return smtpUser;
    }

    /**
	 * @param smtpUser The smtpUser to set.
	 */
    public void setSmtpUser(String smtpUser) {
        this.smtpUser = smtpUser;
    }

    /**
     * @return Returns the configPwd.
     */
    public String getConfigPwd() {
        return configPwd;
    }

    /**
     * @param configPwd The configPwd to set.
     */
    public void setConfigPwd(String configPwd) {
        this.configPwd = configPwd;
    }

    /**
     * @return Returns the configUser.
     */
    public String getConfigUser() {
        return configUser;
    }

    /**
     * @param configUser The configUser to set.
     */
    public void setConfigUser(String configUser) {
        this.configUser = configUser;
    }

    /**
     * @return Returns the dateFormat.
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * @param dateFormat The dateFormat to set.
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * @return Returns the emailFormat.
     */
    public String getEmailFormat() {
        return emailFormat;
    }

    /**
     * @param emailFormat The emailFormat to set.
     */
    public void setEmailFormat(String emailFormat) {
        this.emailFormat = emailFormat;
    }

    /**
     * @return Returns the driverStringMySQL.
     */
    public String getDriverStringMySQL() {
        return driverStringMySQL;
    }

    /**
     * @param driverStringMySQL The driverStringMySQL to set.
     */
    public void setDriverStringMySQL(String driverStringMySQL) {
        this.driverStringMySQL = driverStringMySQL;
    }

    /**
     * @return Returns the driverStringOracle.
     */
    public String getDriverStringOracle() {
        return driverStringOracle;
    }

    /**
     * @param driverStringOracle The driverStringOracle to set.
     */
    public void setDriverStringOracle(String driverStringOracle) {
        this.driverStringOracle = driverStringOracle;
    }

    /**
     * @return Returns the driverStringPostgreSQL.
     */
    public String getDriverStringPostgreSQL() {
        return driverStringPostgreSQL;
    }

    /**
     * @param driverStringPostgreSQL The driverStringPostgreSQL to set.
     */
    public void setDriverStringPostgreSQL(String driverStringPostgreSQL) {
        this.driverStringPostgreSQL = driverStringPostgreSQL;
    }

    /**
     * @return Returns the driverStringSqlServer.
     */
    public String getDriverStringSqlServer() {
        return driverStringSqlServer;
    }

    /**
     * @param driverStringSqlServer The driverStringSqlServer to set.
     */
    public void setDriverStringSqlServer(String driverStringSqlServer) {
        this.driverStringSqlServer = driverStringSqlServer;
    }

    /**
     * @param theAuthenticationClass to use for password authentication,
     *        default = "elips" if company.user.password should be used to authenticate, 
     *        The class must implement Authentificator interface.
     */
    public void setAuthenticationClass(String theAuthenticationClass) {
        authenticationClass = theAuthenticationClass;
    }

    public String getAuthenticationClass() {
        return authenticationClass;
    }

    /**
     * @param theAuthenticationDomains in comma separated format.
     */
    public void setAuthenticationDomains(String theAuthenticationDomains) {
        authenticationDomains = theAuthenticationDomains;
    }

    public String getAuthenticationDomains() {
        return authenticationDomains;
    }

    /**
	 * @return Returns the companyDatabase.
	 */
    public String getCompanyDatabase() {
        return companyDatabase;
    }

    /**
	 * @param companyDatabase The companyDatabase to set.
	 */
    public void setCompanyDatabase(String companyDatabase) {
        this.companyDatabase = companyDatabase;
    }

    /**
	 * @return Returns the elipsDatabase.
	 */
    public String getElipsDatabase() {
        return elipsDatabase;
    }

    /**
	 * @param elipsDatabase The elipsDatabase to set.
	 */
    public void setElipsDatabase(String elipsDatabase) {
        this.elipsDatabase = elipsDatabase;
    }
}
