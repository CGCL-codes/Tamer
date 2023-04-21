package org.databene.benerator.maven;

import org.databene.benerator.main.DBSnapshotTool;
import org.databene.commons.Assert;
import org.databene.commons.StringUtil;

/**
 * Creates a database snapshot in DbUnit data file format.<br/>
 * <br/>
 * Created: 09.07.2008 18:50:23
 * @since 0.5.4
 * @author Volker Bergmann
 * @goal dbsnapshot
 */
public class DBSnapshotMojo extends AbstractBeneratorMojo {

    /**
	 * The fully qualified name of the JDBC database driver.
	 * @parameter
	 * @required
	 */
    protected String dbDriver;

    /**
	 * The JDBC database url.
	 * @parameter
	 * @required
	 */
    protected String dbUrl;

    /**
	 * The database user name.
	 * @parameter expression="${user.name}"
	 * @required
	 */
    protected String dbUser;

    /**
	 * The database password.
	 * @parameter expression="${user.name}"
	 */
    protected String dbPassword;

    /**
	 * The database schema to use.
	 * @parameter expression="${user.name}"
	 */
    protected String dbSchema;

    /**
	 * The file format to use in the export file.
	 * Available values: dbunit, sql, xls.
	 * If left blank, dbunit is used.
	 * @parameter expression="${user.name}"
	 */
    protected String snapshotFormat;

    /**
	 * The database dialect to use in a snapshot file of SQL format.
	 * If left blank, the database's own dialect will be used.
	 * @parameter
	 */
    protected String snapshotDialect;

    /**
	 * The file name to use for the snapshot file.
	 * If left blank, 'export' + file type suffix is used.
	 * @parameter
	 */
    protected String snapshotFilename;

    @Override
    protected void setSystemProperties() {
        super.setSystemProperties();
        setSystemProperty("dbDriver", dbDriver);
        setSystemProperty("dbUrl", dbUrl);
        setSystemProperty("dbUser", dbUser);
        setSystemProperty("dbPassword", dbPassword);
        setSystemProperty("dbSchema", dbSchema);
    }

    /**
	 * 'Main' method of the Mojo which calls the DbSnapshotTool using the pom's benerator configuration.
	 */
    public void execute() {
        getLog().info(getClass().getName());
        setSystemProperties();
        Assert.notNull(dbUrl, "dbUrl");
        Assert.notNull(dbDriver, "dbDriver");
        Assert.notNull(dbUser, "dbUser");
        if (StringUtil.isEmpty(snapshotFormat)) snapshotFormat = DBSnapshotTool.DBUNIT_FORMAT;
        if (StringUtil.isEmpty(snapshotFilename)) {
            if (DBSnapshotTool.DBUNIT_FORMAT.equals(snapshotFormat)) snapshotFilename = "snapshot.dbunit.xml"; else snapshotFilename = "snapshot." + snapshotFormat;
        }
        DBSnapshotTool.export(dbUrl, dbDriver, dbSchema, dbUser, dbPassword, snapshotFilename, snapshotFormat, snapshotDialect);
    }
}
