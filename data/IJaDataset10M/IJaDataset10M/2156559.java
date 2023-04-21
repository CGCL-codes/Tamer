package org.gudy.azureus2.pluginsimpl.local.download;

import java.net.URL;
import org.gudy.azureus2.plugins.download.Download;
import org.gudy.azureus2.plugins.download.DownloadScrapeResult;
import org.gudy.azureus2.core3.tracker.client.*;

public class DownloadScrapeResultImpl implements DownloadScrapeResult {

    protected DownloadImpl download;

    protected TRTrackerScraperResponse response;

    protected DownloadScrapeResultImpl(DownloadImpl _download, TRTrackerScraperResponse _response) {
        download = _download;
        response = _response;
    }

    protected void setContent(TRTrackerScraperResponse _response) {
        response = _response;
    }

    public Download getDownload() {
        return (download);
    }

    public int getResponseType() {
        if (response != null && response.isValid()) {
            return (RT_SUCCESS);
        } else {
            return (RT_ERROR);
        }
    }

    public int getSeedCount() {
        return (response == null ? -1 : response.getSeeds());
    }

    public int getNonSeedCount() {
        return (response == null ? -1 : response.getPeers());
    }

    public long getScrapeStartTime() {
        return (response == null ? -1 : response.getScrapeStartTime());
    }

    public void setNextScrapeStartTime(long nextScrapeStartTime) {
        TRTrackerScraperResponse current_response = getCurrentResponse();
        if (current_response != null) {
            current_response.setNextScrapeStartTime(nextScrapeStartTime);
        }
    }

    public long getNextScrapeStartTime() {
        TRTrackerScraperResponse current_response = getCurrentResponse();
        return (current_response == null ? -1 : current_response.getNextScrapeStartTime());
    }

    public String getStatus() {
        if (response != null) {
            return (response.getStatusString());
        }
        return ("");
    }

    public URL getURL() {
        if (response != null) {
            return (response.getURL());
        }
        return null;
    }

    protected TRTrackerScraperResponse getCurrentResponse() {
        TRTrackerScraperResponse current = download.getDownload().getTrackerScrapeResponse();
        if (current == null) {
            current = response;
        }
        return (current);
    }
}
