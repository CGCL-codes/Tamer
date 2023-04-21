package org.digitalcure.refactordw.model.wikifiles;

import org.digitalcure.refactordw.entities.AbstractWikiFile;
import org.digitalcure.refactordw.entities.Article;
import org.digitalcure.refactordw.entities.ArticleLink;
import org.digitalcure.refactordw.entities.MediaFile;
import org.digitalcure.refactordw.entities.WikiFileURL;
import org.digitalcure.refactordw.entities.WikiFileURL.URLSeparator;
import org.apache.log4j.Logger;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model containing all scanned Wiki files and the name space model of the
 * DokuWiki.
 * @author Manfred Novotny, Stefan Diener
 * @version 1.1
 * @since 1.2, 03.06.2009
 * @lastChange $Date$ by $Author$
 */
public class WikiFileModel implements PropertyChangeListener {

    /** Logger instance. */
    private static final Logger LOGGER = Logger.getLogger(WikiFileModel.class);

    /**
     * The list of all scanned Wiki files. This contains articles
     * (<code>pages</code>) as well as uploaded documents (<code>media</code>).
     */
    private final List<AbstractWikiFile> fileList;

    /**
     * The list of all articles contained in {@link #fileList}. This list is
     * generated at the first request of the list, so make sure it is in sync
     * with {@link #fileList}.
     */
    private List<Article> articleList;

    /**
     * The list of all media files contained in {@link #fileList}. This list is
     * generated at the first request of the list, so make sure it is in sync
     * with {@link #fileList}.
     */
    private List<MediaFile> mediaFileList;

    /**
     * Map of all scanned Wiki files. Key: absolute file path, value: Wiki file.
     * This map contains articles as well as uploaded documents.
     */
    private final Map<String, AbstractWikiFile> fileMap = new HashMap<String, AbstractWikiFile>();

    /**
     * Map of all scanned Wiki files. Key: Wiki file URL, value: Wiki file. This
     * map contains articles as well as uploaded documents.
     */
    private final Map<String, AbstractWikiFile> urlMap = new HashMap<String, AbstractWikiFile>();

    /**
     * Constructor.
     * @param wikiFiles the list of files contained in the Wiki, never
     *  <code>null</code>
     */
    public WikiFileModel(final List<AbstractWikiFile> wikiFiles) {
        if (wikiFiles == null) {
            throw new IllegalArgumentException("Parameter must not be null");
        }
        fileList = wikiFiles;
    }

    /**
     * Returns the list of Wiki files. This list contains articles as well as
     * media files. Please note that the original list is returned, so the
     * manipulation of the list is strictly forbidden!
     * @return the list of scanned Wiki files
     */
    public List<AbstractWikiFile> getAllFiles() {
        return fileList;
    }

    /**
     * Returns a list of all Wiki articles in the model.
     * @return the list of all articles
     */
    public List<Article> getArticles() {
        if (articleList != null) {
            return articleList;
        }
        articleList = new ArrayList<Article>();
        for (final AbstractWikiFile file : fileList) {
            if (file instanceof Article) {
                articleList.add((Article) file);
            }
        }
        return articleList;
    }

    /**
     * Returns a list of all Wiki media files in the model.
     * @return the list of all media files
     */
    public List<MediaFile> getMediaFiles() {
        if (mediaFileList != null) {
            return mediaFileList;
        }
        mediaFileList = new ArrayList<MediaFile>();
        for (final AbstractWikiFile file : fileList) {
            if (file instanceof MediaFile) {
                mediaFileList.add((MediaFile) file);
            }
        }
        return mediaFileList;
    }

    /**
     * Returns the Wiki file denoted by the given file.
     * @param file the file that determines the Wiki file to return
     * @return the Wiki file that is represented by the given file or
     *  <code>null</code> if the Wiki file cannot be found
     * @throws IllegalArgumentException if the given parameter is
     *  <code>null</code>
     */
    public AbstractWikiFile getByFile(final File file) throws IllegalArgumentException {
        if (file == null) {
            throw new IllegalArgumentException("Parameter 'file' must not be null");
        }
        if (fileMap.isEmpty()) {
            for (final AbstractWikiFile wikiFile : fileList) {
                final String path = wikiFile.getFile().getAbsolutePath();
                fileMap.put(path, wikiFile);
                wikiFile.addPropertyChangeListener("file", this);
            }
        }
        final String filePath = file.getAbsolutePath();
        return fileMap.get(filePath);
    }

    /**
     * Returns the Wiki file denoted by the given {@link ArticleLink}.
     * @param link the link that determines the Wiki file to return
     * @return the Wiki file that is represented by the given link or
     *  <code>null</code> if the Wiki file cannot be found
     * @throws IllegalArgumentException if the given parameter is
     *  <code>null</code>
     */
    public AbstractWikiFile getByLink(final ArticleLink link) throws IllegalArgumentException {
        if (link == null) {
            throw new IllegalArgumentException("Parameter 'link' must not be null");
        }
        if (ArticleLink.isInvalid(link.getOriginalTextLink())) {
            LOGGER.warn("WikiFileModel.getByLink(...): Link " + link.toString() + " is not a valid link. Skipping...");
            return null;
        }
        if (urlMap.isEmpty()) {
            for (final AbstractWikiFile wikiFile : fileList) {
                final String url = wikiFile.getWikiFileURL(URLSeparator.DEFAULT);
                urlMap.put(url, wikiFile);
                wikiFile.addPropertyChangeListener("wikiFileURL", this);
            }
        }
        final String denotedWikiFileURLString = link.getWikiFileURL().getURL(URLSeparator.DEFAULT);
        return urlMap.get(denotedWikiFileURLString);
    }

    /**
     * {@inheritDoc}
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        final Object source = event.getSource();
        final Object oldValue = event.getOldValue();
        final Object newValue = event.getNewValue();
        final String property = event.getPropertyName();
        if (source instanceof AbstractWikiFile) {
            if ("file".equals(property)) {
                final File oldFile = (File) oldValue;
                final File newFile = (File) newValue;
                fileMap.remove(oldFile.getAbsolutePath());
                fileMap.put(newFile.getAbsolutePath(), (AbstractWikiFile) source);
            } else if ("wikiFileURL".equals(property)) {
                final WikiFileURL oldUrl = (WikiFileURL) oldValue;
                final WikiFileURL newUrl = (WikiFileURL) newValue;
                urlMap.remove(oldUrl.getURL(URLSeparator.DEFAULT));
                urlMap.put(newUrl.getURL(URLSeparator.DEFAULT), (AbstractWikiFile) source);
            } else {
                LOGGER.warn("WikiFileModel.propertyChange(...): Unknown property of AbstractWikiFile changed: " + property);
            }
        }
    }
}
