package br.ufal.ic.nexos.arcolive.tests;

import java.io.IOException;
import java.net.MalformedURLException;
import javax.media.Manager;
import javax.media.NoDataSourceException;
import javax.media.protocol.DataSource;

/**
 * CLASSNAME.java
 *
 * CLASS DESCRIPTION
 *
 * @see CLASSNAME
 *
 * @author <a href="mailto:felipe@labpesquisas.tci.ufal.br">Felipe Barros Pontes</a>.
 * @author <a href="mailto:leandro@labpesquisas.tci.ufal.br">Leandro Melo de Sales</a>.
 * @since 0.1
 * @version 0.1
 *
 * <p><b>Revisions:</b>
 *
 * <p><b>yyyymmdd USERNAME:</b>
 * <ul>
 * <li> VERSION
 * </ul>
 */
public class ArCoLIVEDatasource {

    private DataSource datasource;

    public ArCoLIVEDatasource() {
    }

    public DataSource getDataSource() {
        return this.datasource;
    }

    public void connect() throws IOException {
        this.datasource.connect();
    }

    public void disconnect() {
        this.datasource.disconnect();
    }

    public String getContentType() {
        return this.datasource.getContentType();
    }

    public void setLocator(javax.media.MediaLocator source) {
        try {
            this.datasource.setLocator(new javax.media.MediaLocator(source.getURL()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setDatasource(DataSource ds) {
        this.datasource = ds;
    }

    public void start() throws IOException {
        this.datasource.start();
    }

    public void stop() throws IOException {
        this.datasource.stop();
    }
}
