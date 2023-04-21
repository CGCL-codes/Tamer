package org.stanwood.media.store;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import org.stanwood.media.Controller;
import org.stanwood.media.MediaDirectory;
import org.stanwood.media.model.Episode;
import org.stanwood.media.model.Film;
import org.stanwood.media.model.IEpisode;
import org.stanwood.media.model.IFilm;
import org.stanwood.media.model.ISeason;
import org.stanwood.media.model.IShow;
import org.stanwood.media.model.Mode;
import org.stanwood.media.model.SearchResult;
import org.stanwood.media.model.Show;
import org.stanwood.media.progress.IProgressMonitor;
import org.stanwood.media.setup.MediaDirConfig;

/**
 * This is a fake store that is used for testing of a stores params.
 */
@SuppressWarnings("nls")
public class FakeStore implements IStore {

    /** This is used to test that a param was test */
    private static String testParam1;

    /**
	 * This does nothing as the store does not support it.
	 * @param episodeFile The file the episode is stored in
	 * @param episode The details of the episode
	 */
    @Override
    public void cacheEpisode(File rootMediaDir, File episodeFile, IEpisode episode) {
    }

    /**
	 * This does nothing as the store does not support it.
	 * @param episodeFile The file the episode is stored in
	 * @param season The details of the season
	 */
    @Override
    public void cacheSeason(File rootMediaDir, File episodeFile, ISeason season) {
    }

    /**
	 * This does nothing as the store does not support it.
	 * @param episodeFile The file the episode is stored in
	 * @param show The details of the show
	 */
    @Override
    public void cacheShow(File rootMediaDir, File episodeFile, IShow show) {
    }

    /**
	 * This always returns null as this store does not support reading episodes.
	 * @param episodeFile The file the episode is stored in
	 * @param season The season the episode belongs to.
	 * @param episodeNum The number of the episode to read
	 */
    @Override
    public IEpisode getEpisode(File rootMediaDir, File episodeFile, ISeason season, int episodeNum) {
        return null;
    }

    /**
	 * This always returns null as this store does not support reading episodes.
	 * @param episodeFile The file the episode is stored in
	 * @param show The show the season belongs to.
	 * @param seasonNum The number of the season to read
	 */
    @Override
    public ISeason getSeason(File rootMediaDir, File episodeFile, IShow show, int seasonNum) {
        return null;
    }

    /**
	 * This always returns null as this store does not support reading episodes.
	 * @param episodeFile The file the episode is stored in
	 * @param showId The id of the show to read
	 */
    @Override
    public Show getShow(File rootMediaDir, File episodeFile, String showId) {
        return null;
    }

    /**
	 * This always returns null as this store does not support reading episodes.
	 * @param episodeFile The file the episode is stored in
	 * @param season The season the episode belongs to.
	 * @param specialNumber The number of the special episode to read
	 */
    @Override
    public IEpisode getSpecial(File rootMediaDir, File episodeFile, ISeason season, int specialNumber) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void setParameter(String key, String value) {
        if (key.equalsIgnoreCase("testParam1")) {
            setFakeParam(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getParameter(String key) {
        if (key.equalsIgnoreCase("testParam1")) {
            return getFakeParam();
        }
        return null;
    }

    /**
	 * Used to get the value of the test parameter
	 * @return The value of the test parameter
	 */
    public static String getFakeParam() {
        return testParam1;
    }

    /**
	 * Used to set the value of the test parameter
	 * @param value The value of the test parameter@param value
	 */
    public static void setFakeParam(String value) {
        FakeStore.testParam1 = value;
    }

    /**
	 * This does nothing as this store does not support writing of films
	 * @param filmFile The file the film is stored in
	 * @param film The film details
	 */
    @Override
    public void cacheFilm(File rootMediaDir, File filmFile, IFilm film, Integer part) {
    }

    /**
	 * This does nothing as this store does not support writing of films
	 * @param oldFile The old file
	 * @param newFile The new file
	 */
    @Override
    public void renamedFile(File rootMediaDir, File oldFile, File newFile) {
    }

    /**
	 * This always returns null as this store does not support reading films.
	 * @param filmFile The file the film is stored in
	 * @param filmId The id of the film
	 */
    @Override
    public Film getFilm(File rootMediaDir, File filmFile, String filmId) throws StoreException, MalformedURLException, IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public SearchResult searchMedia(String name, Mode mode, Integer part, MediaDirConfig dirConfig, File mediaFile) throws StoreException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void performedActions(MediaDirectory dir) {
    }

    /** {@inheritDoc} */
    @Override
    public void fileDeleted(MediaDirectory dir, File file) {
    }

    /** {@inheritDoc} */
    @Override
    public Episode getEpisode(MediaDirectory dir, File file) throws StoreException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Film getFilm(MediaDirectory dir, File file) throws StoreException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void init(Controller controller, File nativeDir) throws StoreException {
    }

    /** {@inheritDoc} */
    @Override
    public List<IEpisode> listEpisodes(MediaDirConfig dirConfig, IProgressMonitor monitor) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public List<IFilm> listFilms(MediaDirConfig dirConfig, IProgressMonitor monitor) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void upgrade(MediaDirectory mediaDirectory) {
    }

    /** {@inheritDoc} */
    @Override
    public void fileUpdated(MediaDirectory mediaDirectory, File file) throws StoreException {
    }
}
