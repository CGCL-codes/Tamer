package org.abettor.leaf4e.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.abettor.leaf4e.Activator;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * LEAF工程向导页
 * @author shawn
 *
 */
public class LeafProjectNewWizardPage extends WizardPage {

    private static List<String> dbTypeList = new ArrayList<String>();

    private static Map<String, DbModal> dbModalMap = new HashMap<String, DbModal>();

    private ILog logger = Activator.getDefault().getLog();

    private Text project;

    private Combo runtime;

    private Combo dbDriver;

    private Text dbUrl;

    private Text dbUser;

    private Text dbPass;

    private Combo dbDialect;

    private Text logPath;

    private ModifyListener modifyListener = new ModifyListener() {

        @Override
        public void modifyText(ModifyEvent e) {
            dialogChanged();
        }
    };

    static {
        DbModal m = new DbModal();
        m.dialect = "org.hibernate.dialect.MySQLDialect";
        m.driver = "com.mysql.jdbc.Driver";
        m.url = "jdbc:mysql://localhost:3306/db_name";
        dbModalMap.put("MySQL", m);
        dbTypeList.add("MySQL");
        m = new DbModal();
        m.dialect = "org.hibernate.dialect.PostgreSQLDialect";
        m.driver = "org.postgresql.Driver";
        m.url = "jdbc:postgresql://localhost:5432/db_name";
        dbModalMap.put("PostgreSQL", m);
        dbTypeList.add("PostgreSQL");
        m = new DbModal();
        m.dialect = "org.hibernate.dialect.OracleDialect";
        m.driver = "oracle.jdbc.driver.OracleDriver";
        m.url = "jdbc:oracle:thin:@localhost:1521:sid_name";
        dbModalMap.put("Oracle", m);
        dbTypeList.add("Oracle");
        m = new DbModal();
        m.dialect = "org.hibernate.dialect.SQLServerDialect";
        m.driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
        m.url = "jdbc:microsoft:sqlserver://localhost:1433";
        dbModalMap.put("SQLServer", m);
        dbTypeList.add("SQLServer");
        m = new DbModal();
        m.dialect = "org.hibernate.dialect.SybaseDialect";
        m.driver = "com.sybase.jdbcx.SybDriver";
        m.url = "jdbc:sybase:Tds:localhost:5000?ServiceName=db_name";
        dbModalMap.put("Sybase", m);
        dbTypeList.add("Sybase");
        dbModalMap.put("Others", null);
        dbTypeList.add("Others");
    }

    public LeafProjectNewWizardPage(ISelection selection) {
        super("LEAF Project Wizard");
        setTitle("New LEAF Project");
    }

    @Override
    public void createControl(Composite parent) {
        String[] driverItems = new String[dbTypeList.size() - 1];
        String[] dialectItems = new String[dbTypeList.size() - 1];
        for (int i = 0; i < driverItems.length; i++) {
            DbModal m = dbModalMap.get(dbTypeList.get(i));
            driverItems[i] = m.driver;
            dialectItems[i] = m.dialect;
        }
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayoutData(gridData);
        container.setLayout(gridLayout);
        GridData gridDataGrp = new GridData();
        gridDataGrp.grabExcessHorizontalSpace = true;
        gridDataGrp.horizontalAlignment = GridData.FILL;
        GridLayout gridLayoutGrp = new GridLayout();
        gridLayoutGrp.makeColumnsEqualWidth = false;
        gridLayoutGrp.numColumns = 2;
        Group projectGrp = new Group(container, SWT.NONE);
        projectGrp.setLayoutData(gridDataGrp);
        projectGrp.setLayout(gridLayoutGrp);
        projectGrp.setText("Project");
        GridData gridDataProject = new GridData();
        gridDataProject.grabExcessHorizontalSpace = true;
        gridDataProject.horizontalAlignment = GridData.FILL;
        Label projectLabel = new Label(projectGrp, SWT.NONE);
        projectLabel.setText("Project name:");
        project = new Text(projectGrp, SWT.BORDER);
        project.setLayoutData(gridDataProject);
        project.addModifyListener(modifyListener);
        GridData gridDataRuntime = new GridData();
        gridDataRuntime.grabExcessHorizontalSpace = true;
        gridDataRuntime.horizontalAlignment = GridData.FILL;
        Label runtimeLabel = new Label(projectGrp, SWT.NONE);
        runtimeLabel.setText("Runtime environment:");
        runtime = new Combo(projectGrp, SWT.READ_ONLY);
        runtime.setLayoutData(gridDataRuntime);
        String[] runtimeItems = getRuntimes();
        runtime.setItems(runtimeItems);
        runtime.addModifyListener(modifyListener);
        gridDataGrp = new GridData();
        gridDataGrp.grabExcessHorizontalSpace = true;
        gridDataGrp.horizontalAlignment = GridData.FILL;
        gridLayoutGrp = new GridLayout();
        gridLayoutGrp.makeColumnsEqualWidth = false;
        gridLayoutGrp.numColumns = 2;
        Group dbGrp = new Group(container, SWT.NONE);
        dbGrp.setLayoutData(gridDataGrp);
        dbGrp.setLayout(gridLayoutGrp);
        dbGrp.setText("Database");
        GridData gridDataType = new GridData();
        gridDataType.grabExcessHorizontalSpace = true;
        gridDataType.horizontalAlignment = GridData.FILL;
        Label typeLabel = new Label(dbGrp, SWT.NONE);
        typeLabel.setText("Database type:");
        String[] typeItems = new String[dbTypeList.size()];
        dbTypeList.toArray(typeItems);
        final Combo dbType = new Combo(dbGrp, SWT.READ_ONLY);
        dbType.setLayoutData(gridDataType);
        dbType.setItems(typeItems);
        dbType.setText(typeItems[typeItems.length - 1]);
        dbType.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                DbModal m = dbModalMap.get(dbType.getText());
                if (m == null) {
                    dbDialect.setText("");
                    dbDriver.setText("");
                    dbUrl.setText("");
                } else {
                    dbDialect.setText(m.dialect);
                    dbDriver.setText(m.driver);
                    dbUrl.setText(m.url);
                }
            }
        });
        GridData gridDataDriver = new GridData();
        gridDataDriver.grabExcessHorizontalSpace = true;
        gridDataDriver.horizontalAlignment = GridData.FILL;
        Label driverLabel = new Label(dbGrp, SWT.NONE);
        driverLabel.setText("JDBC driver class:");
        dbDriver = new Combo(dbGrp, SWT.NONE);
        dbDriver.setLayoutData(gridDataProject);
        dbDriver.setItems(driverItems);
        dbDriver.addModifyListener(modifyListener);
        GridData gridDataDialect = new GridData();
        gridDataDialect.grabExcessHorizontalSpace = true;
        gridDataDialect.horizontalAlignment = GridData.FILL;
        Label dialectLabel = new Label(dbGrp, SWT.NONE);
        dialectLabel.setText("Dialect class:");
        dbDialect = new Combo(dbGrp, SWT.NONE);
        dbDialect.setLayoutData(gridDataDialect);
        dbDialect.setItems(dialectItems);
        dbDialect.addModifyListener(modifyListener);
        GridData gridDataUrl = new GridData();
        gridDataUrl.grabExcessHorizontalSpace = true;
        gridDataUrl.horizontalAlignment = GridData.FILL;
        Label urlLabel = new Label(dbGrp, SWT.NONE);
        urlLabel.setText("JDBC connection URL:");
        dbUrl = new Text(dbGrp, SWT.BORDER);
        dbUrl.setLayoutData(gridDataUrl);
        dbUrl.addModifyListener(modifyListener);
        GridData gridDataUser = new GridData();
        gridDataUser.grabExcessHorizontalSpace = true;
        gridDataUser.horizontalAlignment = GridData.FILL;
        Label userLabel = new Label(dbGrp, SWT.NONE);
        userLabel.setText("User name:");
        dbUser = new Text(dbGrp, SWT.BORDER);
        dbUser.setLayoutData(gridDataUser);
        dbUser.addModifyListener(modifyListener);
        GridData gridDataPass = new GridData();
        gridDataPass.grabExcessHorizontalSpace = true;
        gridDataPass.horizontalAlignment = GridData.FILL;
        Label passLabel = new Label(dbGrp, SWT.NONE);
        passLabel.setText("Password:");
        dbPass = new Text(dbGrp, SWT.BORDER);
        dbPass.setLayoutData(gridDataPass);
        dbPass.addModifyListener(modifyListener);
        gridDataGrp = new GridData();
        gridDataGrp.grabExcessHorizontalSpace = true;
        gridDataGrp.horizontalAlignment = GridData.FILL;
        gridLayoutGrp = new GridLayout();
        gridLayoutGrp.makeColumnsEqualWidth = false;
        gridLayoutGrp.numColumns = 2;
        Group logGrp = new Group(container, SWT.NONE);
        logGrp.setLayoutData(gridDataGrp);
        logGrp.setLayout(gridLayoutGrp);
        logGrp.setText("Log file");
        GridData gridDataLog = new GridData();
        gridDataLog.grabExcessHorizontalSpace = true;
        gridDataLog.horizontalAlignment = GridData.FILL;
        Label logLabel = new Label(logGrp, SWT.NONE);
        logLabel.setText("Log file path:");
        logPath = new Text(logGrp, SWT.BORDER);
        logPath.setLayoutData(gridDataLog);
        logPath.addModifyListener(modifyListener);
        setControl(container);
    }

    private void dialogChanged() {
        String tmp = getProject();
        if (tmp == null || tmp.isEmpty()) {
            updateStatus("Enter the project name.");
            return;
        } else if (tmp.indexOf("/") >= 0 || tmp.endsWith(" ")) {
            updateStatus("Invalid project name.");
            return;
        }
        tmp = getRuntime();
        if (tmp == null || tmp.isEmpty()) {
            updateStatus("Choose a runtime environment.");
            return;
        }
        tmp = getDbDriver();
        if (tmp == null || tmp.isEmpty()) {
            updateStatus("Choose a JDBC driver class name.");
            return;
        }
        tmp = getDbUrl();
        if (tmp == null || tmp.isEmpty()) {
            updateStatus("Enter the JDBC connection URL.");
            return;
        }
        tmp = getDbUser();
        if (tmp == null || tmp.isEmpty()) {
            updateStatus("Enter the user name to connect to the database.");
            return;
        }
        tmp = getDbDialect();
        if (tmp == null || tmp.isEmpty()) {
            updateStatus("Choose a JDBC dialect class for the database.");
            return;
        }
        tmp = getLogPath();
        if (tmp == null || tmp.isEmpty()) {
            updateStatus("Enter the full path for the log file.");
            return;
        }
        updateStatus(null);
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    @SuppressWarnings("unchecked")
    private String[] getRuntimes() {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        String path = root.getLocation().toFile().getAbsolutePath();
        path += "/.metadata/.plugins/org.eclipse.wst.server.core/servers.xml";
        path = path.replaceAll("/", File.separator);
        File file = new File(path);
        if (!file.exists()) {
            return new String[0];
        }
        Set<String> set = new HashSet<String>();
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(file);
            Element servers = doc.getRootElement();
            List<Element> serverList = servers.getChildren("server");
            for (Element server : serverList) {
                String val = server.getAttributeValue("runtime-id");
                set.add(val);
            }
        } catch (Exception e) {
            Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Reading runtime environment error", e);
            logger.log(status);
            return new String[0];
        }
        String[] items = new String[set.size()];
        set.toArray(items);
        return items;
    }

    public String getProject() {
        return project.getText();
    }

    public String getRuntime() {
        return runtime.getText();
    }

    public String getDbDriver() {
        return dbDriver.getText();
    }

    public String getDbUrl() {
        return dbUrl.getText();
    }

    public String getDbUser() {
        return dbUser.getText();
    }

    public String getDbPassword() {
        String pass = dbPass.getText();
        if (pass == null) {
            pass = "";
        }
        return pass;
    }

    public String getDbDialect() {
        return dbDialect.getText();
    }

    public String getLogPath() {
        return logPath.getText();
    }

    private static class DbModal {

        public String driver;

        public String url;

        public String dialect;
    }
}
