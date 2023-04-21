package org.apache.ddlutils.task;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.CreationParameters;
import org.apache.tools.ant.BuildException;

/**
 * Parses the schema XML files specified for the enclosing task, and creates the corresponding
 * schema in the database.
 * 
 * @version $Revision: 289996 $
 * @ant.task name="writeSchemaToDatabase"
 */
public class WriteSchemaToDatabaseCommand extends DatabaseCommandWithCreationParameters {

    /** Whether to alter or re-set the database if it already exists. */
    private boolean _alterDb = true;

    /** Whether to drop tables and the associated constraints if necessary. */
    private boolean _doDrops = true;

    /**
     * Determines whether to alter the database if it already exists, or re-set it.
     * 
     * @return <code>true</code> if to alter the database
     */
    protected boolean isAlterDatabase() {
        return _alterDb;
    }

    /**
     * Specifies whether DdlUtils shall alter an existing database rather than clearing it and
     * creating it new.
     * 
     * @param alterTheDb <code>true</code> if to alter the database
     * @ant.not-required Per default an existing database is altered
     */
    public void setAlterDatabase(boolean alterTheDb) {
        _alterDb = alterTheDb;
    }

    /**
     * Determines whether to drop tables and the associated constraints if necessary.
     * 
     * @return <code>true</code> if drops shall be performed
     */
    protected boolean isDoDrops() {
        return _doDrops;
    }

    /**
     * Specifies whether tables, external constraints, etc. can be dropped if necessary.
     * Note that this is only relevant when <code>alterDatabase</code> is <code>false</code>.
     * 
     * @param doDrops <code>true</code> if drops shall be performed
     * @ant.not-required Per default database structures are dropped if necessary
     */
    public void setDoDrops(boolean doDrops) {
        _doDrops = doDrops;
    }

    /**
     * {@inheritDoc}
     */
    public void execute(DatabaseTaskBase task, Database model) throws BuildException {
        if (getDataSource() == null) {
            throw new BuildException("No database specified.");
        }
        Platform platform = getPlatform();
        boolean isCaseSensitive = platform.isDelimitedIdentifierModeOn();
        CreationParameters params = getFilteredParameters(model, platform.getName(), isCaseSensitive);
        platform.setScriptModeOn(false);
        platform.setSqlCommentsOn(false);
        try {
            if (isAlterDatabase()) {
                if ((getCatalogPattern() != null) || (getSchemaPattern() != null)) {
                    platform.alterTables(getCatalogPattern(), getSchemaPattern(), null, model, params, true);
                } else {
                    platform.alterTables(model, params, true);
                }
            } else {
                platform.createTables(model, params, _doDrops, true);
            }
            _log.info("Written schema to database");
        } catch (Exception ex) {
            if (isFailOnError()) {
                throw new BuildException(ex);
            } else {
                _log.error(ex);
            }
        }
    }
}
