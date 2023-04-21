package uk.co.caprica.vlcj.radio.service.indymedia;

import java.util.Collections;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import uk.co.caprica.vlcj.radio.model.Directory;
import uk.co.caprica.vlcj.radio.model.DirectoryEntry;

/**
 * Implementation of a directory.
 */
@Root(name = "directory")
public class IndymediaDirectory implements Directory {

    /**
   * Collection of directory entries.
   */
    @ElementList(entry = "entry", inline = true)
    private List<IndymediaDirectoryEntry> entries;

    /**
   * Default constructor (required for XML binding).
   */
    public IndymediaDirectory() {
    }

    /**
   * Create a directory.
   * 
   * @param entries directory entries.
   */
    public IndymediaDirectory(List<IndymediaDirectoryEntry> entries) {
        this.entries = Collections.unmodifiableList(entries);
    }

    @Override
    public List<? extends DirectoryEntry> entries() {
        return entries;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(4096);
        for (int i = 0; i < entries.size(); i++) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(entries.get(i));
        }
        return sb.toString();
    }
}
