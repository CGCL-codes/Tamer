package net.sourceforge.fluxion.api;

import java.net.URI;

/**
 * Thrown by the data source and fluxion service interfaces when an error occurs
 * during access to the data source.
 * 
 * @author Tom Oinn
 */
public class DataSourceException extends Exception {

    private static final long serialVersionUID = 3816306234582165951L;

    private URI dataSource;

    public DataSourceException(String message) {
        super(message);
    }

    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceException(Throwable cause) {
        super(cause);
    }

    public DataSourceException(URI dataSource) {
        super();
        this.dataSource = dataSource;
    }

    public DataSourceException(URI dataSource, String msg) {
        super(msg);
        this.dataSource = dataSource;
    }

    public DataSourceException(URI dataSource, Throwable reason) {
        super(reason);
        this.dataSource = dataSource;
    }

    public DataSourceException(URI dataSource, String msg, Throwable reason) {
        super(msg, reason);
        this.dataSource = dataSource;
    }

    public URI getDataSource() {
        return dataSource;
    }
}
