package org.databene.mad4db;

import java.util.regex.Pattern;
import org.databene.commons.ConnectFailedException;
import org.databene.commons.ImportFailedException;
import org.databene.commons.version.VersionNumber;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.Database;
import org.databene.jdbacl.model.jdbc.JDBCMetaDataUtil;

/**
 * Encapsulates all configuration settings for comparison of database objects.<br/><br/>
 * Created: 20.05.2011 19:55:38
 * @since 0.1
 * @author Volker Bergmann
 */
public class ComparisonConfig {

    private String environment1;

    private String environment2;

    private Pattern tableExclusionPattern;

    private boolean metaDataCached;

    private boolean ignoringIndexes;

    private boolean ignoringSequences;

    private int defectCountLimit;

    private VersionNumber appVersion;

    private Database metaData1;

    private Database metaData2;

    public ComparisonConfig(String environment1, String environment2, String exclusionPattern) {
        this.environment1 = environment1;
        this.environment2 = environment2;
        setTableExclusionPattern(exclusionPattern);
        this.metaDataCached = false;
        this.ignoringIndexes = false;
        this.ignoringSequences = false;
        this.defectCountLimit = 0;
        this.appVersion = null;
    }

    public String getEnvironment1() {
        return environment1;
    }

    public void setEnvironment1(String environment1) {
        this.environment1 = environment1;
    }

    public String getEnvironment2() {
        return environment2;
    }

    public void setEnvironment2(String environment2) {
        this.environment2 = environment2;
    }

    public String getTableExclusionPattern() {
        return (tableExclusionPattern != null ? tableExclusionPattern.pattern() : null);
    }

    public void setTableExclusionPattern(String tableExclusionPattern) {
        this.tableExclusionPattern = (tableExclusionPattern != null ? Pattern.compile(tableExclusionPattern) : null);
    }

    public boolean isMetaDataCached() {
        return metaDataCached;
    }

    public void setMetaDataCached(boolean metaDataCached) {
        this.metaDataCached = metaDataCached;
    }

    public boolean acceptTable(DBTable table) {
        return (tableExclusionPattern == null || !this.tableExclusionPattern.matcher(table.getName()).matches());
    }

    public Database getMetaData1() throws ConnectFailedException, ImportFailedException {
        if (metaData1 == null) metaData1 = fetchMetaData(environment1);
        return metaData1;
    }

    public Database getMetaData2() throws ConnectFailedException, ImportFailedException {
        if (metaData2 == null) metaData2 = fetchMetaData(environment2);
        return metaData2;
    }

    public boolean isIgnoringIndexes() {
        return ignoringIndexes;
    }

    public void setIgnoringIndexes(boolean ignoringIndexes) {
        this.ignoringIndexes = ignoringIndexes;
    }

    public boolean isIgnoringSequences() {
        return ignoringSequences;
    }

    public void setIgnoringSequences(boolean ignoringSequences) {
        this.ignoringSequences = ignoringSequences;
    }

    public int getDefectCountLimit() {
        return defectCountLimit;
    }

    public void setDefectCountLimit(int defectCountLimit) {
        this.defectCountLimit = defectCountLimit;
    }

    public VersionNumber getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(VersionNumber appVersion) {
        this.appVersion = appVersion;
    }

    protected Database fetchMetaData(String environment) throws ConnectFailedException, ImportFailedException {
        return JDBCMetaDataUtil.getMetaData(environment, true, true, true, true, ".*", getTableExclusionPattern(), false, metaDataCached);
    }
}
