package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.StaticPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import java.util.*;

/**
 * Maintains an index of all static pages
 *
 * @author    Simon Brown
 */
public class StaticPageIndex {

    private static final Log log = LogFactory.getLog(StaticPageIndex.class);

    private static final String PAGES_INDEX_DIRECTORY_NAME = "pages";

    private static final String NAME_TO_ID_INDEX_FILE_NAME = "name.index";

    private static final String LOCK_FILE_NAME = "pages.lock";

    private static final int MAXIMUM_LOCK_ATTEMPTS = 3;

    /** the owning blog */
    private Blog blog;

    /** the collection of all static pages */
    private Map<String, String> index = new HashMap<String, String>();

    private int lockAttempts = 0;

    public StaticPageIndex(Blog blog) {
        this.blog = blog;
        File indexDirectory = new File(blog.getIndexesDirectory(), PAGES_INDEX_DIRECTORY_NAME);
        if (!indexDirectory.exists()) {
            indexDirectory.mkdirs();
        }
        readIndex();
    }

    /**
   * Indexes one or more blog entries.
   *
   * @param staticPages   a List of Page instances
   */
    public synchronized void reindex(Collection<StaticPage> staticPages) {
        if (lock()) {
            index = new HashMap<String, String>();
            for (StaticPage staticPage : staticPages) {
                index.put(staticPage.getName(), staticPage.getId());
            }
            writeIndex();
            unlock();
        }
    }

    /**
   * Indexes a single page.
   *
   * @param staticPage    a Page instance
   */
    public synchronized void index(StaticPage staticPage) {
        if (lock()) {
            readIndex();
            Iterator it = index.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                String value = index.get(key);
                if (value.equals(staticPage.getId())) {
                    it.remove();
                }
            }
            index.put(staticPage.getName(), staticPage.getId());
            writeIndex();
            unlock();
        } else {
            if (lockAttempts <= MAXIMUM_LOCK_ATTEMPTS) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
                index(staticPage);
            } else {
                blog.error("Could not index static page - try <a href=\"utilities.secureaction?action=buildIndexes\">rebuilding the indexes</a>.");
            }
        }
    }

    /**
   * Unindexes a single page.
   *
   * @param staticPage    a Page instance
   */
    public synchronized void unindex(StaticPage staticPage) {
        if (lock()) {
            readIndex();
            index.remove(staticPage.getName());
            writeIndex();
            unlock();
        } else {
            if (lockAttempts <= MAXIMUM_LOCK_ATTEMPTS) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
                unindex(staticPage);
            } else {
                blog.reindexStaticPages();
            }
        }
    }

    /**
   * Helper method to load the index.
   */
    private void readIndex() {
        log.info("Reading index from disk");
        File indexFile = getIndexFile();
        if (indexFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(indexFile));
                String indexEntry = reader.readLine();
                while (indexEntry != null) {
                    String[] parts = indexEntry.split("=");
                    index.put(parts[0], parts[1]);
                    indexEntry = reader.readLine();
                }
                reader.close();
            } catch (Exception e) {
                log.error("Error while reading index", e);
            }
        }
    }

    /**
   * Helper method to write out the index to disk.
   */
    private void writeIndex() {
        try {
            File indexFile = getIndexFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile));
            for (String name : index.keySet()) {
                writer.write(name + "=" + index.get(name));
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.error("Error while writing index", e);
        }
    }

    /**
   * Gets the page ID for the specified named page.
   *
   * @param name    a String
   * @return  a String instance, or null if no page exists
   *          with the specified name
   */
    public String getStaticPage(String name) {
        return index.get(name);
    }

    /**
   * Gets the list of static page IDs.
   *
   * @return    a List<String>
   */
    public List<String> getStaticPages() {
        return new LinkedList<String>(index.values());
    }

    /**
   * Determines whether a page with the specified permalink exists.
   *
   * @param name   the name as a String
   * @return  true if the page exists, false otherwise
   */
    public boolean contains(String name) {
        return index.containsKey(name);
    }

    /**
   * Gets the number of static pages.
   *
   * @return  an int
   */
    public int getNumberOfStaticPages() {
        return index.size();
    }

    private File getIndexFile() {
        File indexDirectory = new File(blog.getIndexesDirectory(), PAGES_INDEX_DIRECTORY_NAME);
        return new File(indexDirectory, NAME_TO_ID_INDEX_FILE_NAME);
    }

    private boolean lock() {
        File lockFile = new File(blog.getIndexesDirectory(), LOCK_FILE_NAME);
        boolean success = false;
        try {
            success = lockFile.createNewFile();
            if (!success) {
                lockAttempts++;
            }
        } catch (IOException ioe) {
            log.warn("Error while creating lock file", ioe);
        }
        return success;
    }

    private void unlock() {
        File lockFile = new File(blog.getIndexesDirectory(), LOCK_FILE_NAME);
        lockFile.delete();
        lockAttempts = 0;
    }
}
