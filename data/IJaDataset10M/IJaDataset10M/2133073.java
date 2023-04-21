package uk.ac.ebi.pride.gui.task.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.HttpURLConnection;
import java.util.*;

/**
 * Connect to PRIDE server for reviewer download.
 * <p/>
 * User: rwang
 * Date: 04-Aug-2010
 * Time: 13:51:20
 */
public class OpenReviewerConnectionTask extends AbstractConnectPrideTask {

    private static final Logger logger = LoggerFactory.getLogger(OpenReviewerConnectionTask.class);

    private String user;

    private String password;

    private Set<Comparable> accessions;

    public OpenReviewerConnectionTask(String user, String password) {
        this(user, password, null);
    }

    public OpenReviewerConnectionTask(String user, String password, Collection<Comparable> accessions) {
        this.user = user;
        this.password = password;
        if (accessions != null) {
            this.accessions = new HashSet<Comparable>();
            this.accessions.addAll(accessions);
        }
        String msg = "Download PRIDE Experiment";
        this.setName(msg);
        this.setDescription(msg);
    }

    @Override
    protected List<Map<String, String>> doInBackground() throws Exception {
        HttpURLConnection connection = connect();
        List<Map<String, String>> metadata = null;
        try {
            if (connection != null) {
                initMetaDataDownload(connection, accessions, user, password);
                metadata = downloadMetaData(connection);
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }
        } catch (InterruptedException ex) {
            logger.warn("Download session has been cancelled");
        }
        return metadata;
    }
}
