package de.kajoa.util.dsurlextract;

import javax.sql.DataSource;

public interface DataSourceUrlExtractorIF {

    public boolean supportsDataSource(DataSource ds);

    public String extractUrl(DataSource ds);
}
