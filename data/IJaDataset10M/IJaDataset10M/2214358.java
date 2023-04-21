package com.safi.workshop.sqlexplorer.wizard;

import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import com.safi.workshop.sqlexplorer.dbproduct.Alias;
import com.safi.workshop.sqlexplorer.dbproduct.ManagedDriver;

public class DBConnectionWizard extends Wizard {

    private DBConnectionWizardNamePage namePage = new DBConnectionWizardNamePage();

    private DBConnectionWizardAuthPage authPage = new DBConnectionWizardAuthPage();

    private DBConnectionWizardHostPage hostPage = new DBConnectionWizardHostPage();

    private DBConnectionWizardSchemaPage schemaPage = new DBConnectionWizardSchemaPage();

    private DBConnectionWizardPropertiesPage propertiesPage = new DBConnectionWizardPropertiesPage();

    private DBConnectionWizardSummaryPage summaryPage = new DBConnectionWizardSummaryPage();

    private Alias alias;

    private ManagedDriver driver;

    private Properties properties;

    private String user;

    private String password;

    private String url;

    private boolean authRequired;

    private boolean done = false;

    private String connectionName;

    public DBConnectionWizard() {
    }

    public String getTitle() {
        return "Database Connection Wizard";
    }

    public String getMessage() {
        return "Create new database connection";
    }

    @Override
    public void addPages() {
        addPage(namePage);
        addPage(hostPage);
        addPage(authPage);
        addPage(schemaPage);
        addPage(propertiesPage);
        addPage(summaryPage);
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        DBConnectionWizardPage newPage = null;
        if (page == null || page == namePage) newPage = hostPage;
        done = true;
        if (page == hostPage) newPage = authPage;
        if (page == authPage) newPage = schemaPage;
        if (page == schemaPage) newPage = propertiesPage;
        if (page == propertiesPage) newPage = summaryPage;
        return newPage;
    }

    @Override
    public boolean canFinish() {
        return done;
    }

    @Override
    public boolean performFinish() {
        url = constructUrl();
        Properties props = propertiesPage.getProperties();
        if (properties == null) properties = props; else {
            properties.clear();
            properties.putAll(props);
        }
        if (getAuthPage().isAuthRequired()) {
            user = getAuthPage().getUsername();
            password = getAuthPage().getPassword();
        } else {
            user = "";
            password = "";
        }
        connectionName = getNamePage().getConnectionName();
        return true;
    }

    public String constructUrl() {
        String guideUrl = getDriver().getDriver().getGuideUrl();
        if (StringUtils.isBlank(guideUrl)) return null;
        String host = getHostPage().getHost();
        String port = getHostPage().getPort();
        String user = getAuthPage().getUsername();
        String password = getAuthPage().getPassword();
        String schema = getSchemaPage().getSchema();
        String testUrl = guideUrl.replace("[host]", host == null ? "" : host);
        testUrl = testUrl.replace("[port]", StringUtils.isBlank(port) ? "" : ":" + port);
        testUrl = testUrl.replace("[user]", StringUtils.isBlank(user) ? "" : user);
        testUrl = testUrl.replace("[password]", StringUtils.isBlank(password) ? "" : password);
        testUrl = testUrl.replace("[schema]", schema == null ? "" : schema);
        return testUrl;
    }

    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    public ManagedDriver getDriver() {
        return driver;
    }

    public void setDriver(ManagedDriver driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DBConnectionWizardNamePage getNamePage() {
        return namePage;
    }

    public DBConnectionWizardAuthPage getAuthPage() {
        return authPage;
    }

    public DBConnectionWizardHostPage getHostPage() {
        return hostPage;
    }

    public DBConnectionWizardSchemaPage getSchemaPage() {
        return schemaPage;
    }

    public DBConnectionWizardPropertiesPage getPropertiesPage() {
        return propertiesPage;
    }

    public DBConnectionWizardSummaryPage getSummaryPage() {
        return summaryPage;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuthRequired() {
        return authRequired;
    }

    public void setAuthRequired(boolean authRequired) {
        this.authRequired = authRequired;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String text) {
        connectionName = text;
    }
}
