package org.anuta.imdb;

import java.io.IOException;
import java.util.List;
import org.anuta.imdb.beans.MovieRating;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IMDBAccess {

    private IMDBManager manager;

    private IMDBDownloader downloader;

    private IMDBRatingParser parser;

    private static final Log log = LogFactory.getLog(IMDBAccess.class);

    private static final String UNRATED = "unrated";

    public IMDBManager getManager() {
        return manager;
    }

    public void setManager(IMDBManager manager) {
        this.manager = manager;
    }

    public IMDBDownloader getDownloader() {
        return downloader;
    }

    public void setDownloader(IMDBDownloader downloader) {
        this.downloader = downloader;
    }

    public void startup() {
        try {
            if (log.isDebugEnabled()) log.debug("Starting up");
            int result = getDownloader().download();
            if (result == IMDBDownloader.RESULT_OK) {
                getManager().clean();
                long id = 0;
                getParser().init();
                MovieRating r;
                do {
                    r = getParser().getNextRating();
                    if (r != null) {
                        r.setId(++id);
                        getManager().saveMovieRating(r);
                        if (id % 1000 == 0) if (log.isInfoEnabled()) log.info("Processed " + id + " ratings...");
                    }
                } while (r != null);
                getParser().destroy();
            } else {
                if (log.isDebugEnabled()) log.debug("Result was not ok, no new ratings will be used");
            }
        } catch (IOException e) {
            if (log.isErrorEnabled()) log.error("Error update imdb database", e);
        }
    }

    public void shutdown() {
        getManager().shutdown();
    }

    public MovieRating getRating(String title, String subtitle, int year) {
        if (log.isDebugEnabled()) log.debug("Looking for title/subtitle/year");
        List ratings = getManager().getMovieRating(title, subtitle, year);
        if (ratings.size() == 0) {
            if (log.isDebugEnabled()) log.debug("Nothing found. Return " + UNRATED);
            return null;
        }
        MovieRating mr = (MovieRating) ratings.get(0);
        if (log.isDebugEnabled()) log.debug("Using rating: " + mr);
        return mr;
    }

    public IMDBRatingParser getParser() {
        return parser;
    }

    public void setParser(IMDBRatingParser parser) {
        this.parser = parser;
    }
}
