package de.svenjacobs.invertm3u;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Represents a M3U playlist.
 */
public class M3U implements Iterable<String> {

    private static final Logger logger = Logger.getLogger(M3U.class);

    private final Vector<String> items;

    public M3U() {
        this.items = new Vector<String>();
    }

    /***
   * Appends entries from the specified M3U file to this M3U instance.
   * 
   * @param filename
   * @throws java.io.FileNotFoundException
   * @throws java.io.IOException
   */
    public void load(final String filename) throws FileNotFoundException, IOException {
        final FileReader fileReader = new FileReader(filename);
        final BufferedReader buffReader = new BufferedReader(fileReader);
        while (true) {
            String line = buffReader.readLine();
            if (line == null) {
                break;
            }
            line = line.trim();
            if (line.startsWith("#") || StringUtils.isEmpty(line)) {
                continue;
            }
            line = FilenameUtils.separatorsToSystem(line);
            if (line.startsWith("." + File.separator)) {
                line = line.substring(2);
            }
            logger.debug(String.format("Adding \"%s\" to internal representation of source M3U", line));
            if (!items.contains(line)) {
                items.add(line);
            }
        }
    }

    /***
   * Clears the contents of this M3U playlist.
   */
    public void clear() {
        items.clear();
    }

    @Override
    public Iterator<String> iterator() {
        return items.iterator();
    }

    /***
   * Checks if the specified filename is contained in this M3U.
   * 
   * @param filename Filename to search
   * @return true if found
   */
    public boolean contains(final String filename) {
        return items.contains(filename);
    }
}
