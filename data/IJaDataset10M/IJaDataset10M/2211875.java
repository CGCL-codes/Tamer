package at.jku.semwiq.mediator.registry.monitor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.jku.rdfstats.RDFStatsModelFactory;
import at.jku.semwiq.mediator.registry.DataSourceRegistry;
import at.jku.semwiq.mediator.registry.model.DataSource;
import at.jku.semwiq.mediator.registry.model.RDFStatsUpdatableModelExt;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileUtils;

/**
 * @author dorgon, Andreas Langegger, al@jku.at
 *
 */
public abstract class RemoteUpdateWorkerBase extends UpdateWorkerBase {

    private static final Logger log = LoggerFactory.getLogger(RemoteUpdateWorkerBase.class);

    public static final int CONNECT_TIMEOUT = 5000;

    public static final int READ_TIMEOUT = 5000;

    /** use this gap in order to outweight timing differences between servers
	 * newer documents must be at least TIMING_GAP ms newer in order to be downloaded => fewer unnecessary downloads if times overlap
	 */
    public static final int TIMING_GAP = 60000;

    /**
	 * constructor
	 */
    public RemoteUpdateWorkerBase(DataSource ds, DataSourceMonitorImpl monitor, DataSourceRegistry reg) {
        super(ds, monitor, reg);
    }

    public void checkAndDownload(String statsUrl, RDFStatsUpdatableModelExt stats, Date lastDownload, boolean onlyIfNewer) throws DataSourceMonitorException {
        if (log.isInfoEnabled()) log.info("Checking if update required for statistics of " + ds + "...");
        HttpURLConnection urlConnection;
        try {
            URL url = new URL(statsUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            int statusCode = urlConnection.getResponseCode();
            if (statusCode / 100 != 2) {
                String msg = urlConnection.getResponseMessage();
                throw new DataSourceMonitorException(statsUrl + " returned HTTP " + statusCode + (msg != null ? msg : "") + ".");
            }
        } catch (Exception e) {
            throw new DataSourceMonitorException("Failed to connect to " + statsUrl + ".", e);
        }
        long lastModified = urlConnection.getLastModified();
        boolean newer = lastDownload == null || lastModified == 0 || lastModified - TIMING_GAP > lastDownload.getTime();
        if (newer || !onlyIfNewer) {
            Model newStats = retrieveModelData(urlConnection, ds);
            Date retrievedTimestamp = Calendar.getInstance().getTime();
            Date modifiedTimestamp = (urlConnection.getLastModified() > 0) ? new Date(urlConnection.getLastModified()) : null;
            if (log.isInfoEnabled()) log.info("Attempt to import up-to-date " + ((modifiedTimestamp != null) ? "(from " + modifiedTimestamp + ") " : "") + "statistics for " + ds + ".");
            try {
                if (stats.updateFrom(RDFStatsModelFactory.create(newStats), onlyIfNewer)) stats.setLastDownload(ds.getSPARQLEndpointURL(), retrievedTimestamp);
            } catch (Exception e) {
                throw new DataSourceMonitorException("Failed to import statistics and set last download for " + ds + ".", e);
            }
        } else {
            if (log.isInfoEnabled()) log.info("Statistics for " + ds + " are up-to-date" + ((lastDownload != null) ? " (" + lastDownload + ")" : ""));
        }
    }

    /**
	 * retrieve Model data from HTTP connection for data source ds
	 * 
	 * @param conn
	 * @param ds
	 * @return
	 * @throws DataSourceMonitorException 
	 */
    private Model retrieveModelData(HttpURLConnection conn, DataSource ds) throws DataSourceMonitorException {
        int size = conn.getContentLength();
        String url = conn.getURL().toString();
        Matcher m = Pattern.compile("^([^\\?]*)(?:\\?.*)?$").matcher(url);
        String cleanFileUrl = url;
        if (m.find()) cleanFileUrl = m.group(1);
        if (cleanFileUrl.endsWith(".zip")) {
            log.info("Downloading and extracting remote statistics for " + ds + " from <" + url + "> (" + (size / 1024) + "kb)...");
            Model model = ModelFactory.createDefaultModel();
            try {
                BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                ZipInputStream zipIn = new ZipInputStream(in);
                try {
                    ZipEntry entry = zipIn.getNextEntry();
                    if (entry == null) log.error("Failed to extract data from obviously empty zip archive at <" + url + ">."); else {
                        log.debug("Loading ZIP file entry '" + entry.getName() + "'...");
                        String lang = FileUtils.guessLang(cleanFileUrl.substring(0, cleanFileUrl.length() - 4));
                        model.read(zipIn, null, lang);
                    }
                } finally {
                    zipIn.close();
                }
            } catch (IOException e) {
                log.error("Decompression of <" + url + "> failed." + e);
            }
            return model;
        } else if (cleanFileUrl.endsWith(".tar.gz")) throw new DataSourceMonitorException("Decompression of .tar.gz not yet implemented."); else if (cleanFileUrl.endsWith(".tar.bz2")) throw new DataSourceMonitorException("Decompression of .tar.bz2 not yet implemented."); else {
            log.info("Downloading statistics for " + ds + " from <" + url + "> (" + (size / 1024) + "kb)...");
            conn.disconnect();
            Model model = ModelFactory.createDefaultModel();
            model.read(url);
            return model;
        }
    }
}
