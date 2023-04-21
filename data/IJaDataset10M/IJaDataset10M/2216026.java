package gnos.index;

import java.util.*;
import java.io.*;

/** FrameIndex contains the Frame and Segment file index 
 *
 * <p>Note that attributes which are <i>not</i> {@link Attribute#isStored() stored} are
 * <i>not</i> available in documents retrieved from the index, e.g. with {@link
 * Hits#doc(int)}, {@link Searcher#doc(int)} or {@link IndexReader#frame(int)}.
 */
public class Category implements Serializable {

    public File dir;

    public String header;

    public Category(File dir, String header) throws IOException {
        if (!dir.exists()) {
            dir.mkdir();
        }
        this.header = header;
    }
}
