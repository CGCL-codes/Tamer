package org.jcvi.vics.model.user_data.tools;

import org.jcvi.vics.model.tasks.Task;
import org.jcvi.vics.model.user_data.FileNode;
import org.jcvi.vics.model.user_data.Node;

/**
 * Created by IntelliJ IDEA.
 * User: ekelsey
 * Date: Sep 22, 2010
 * Time: 2:07:27 PM
 */
public class Fasta2BsmlResultNode extends FileNode {

    public static final transient String TAG_FASTA2BSML_LIST = "list";

    public static final transient String TAG_FASTA2BSML_BSML = "bsml";

    public static final transient String BASE_OUTPUT_FILENAME = "fasta2bsml";

    protected long hitCount;

    /**
     * default constructor
     */
    public Fasta2BsmlResultNode() {
    }

    public String getSubDirectory() {
        return "Fasta2BsmlResult";
    }

    /**
     * constructor
     *
     * @param owner               - person who owns the node
     * @param task                - task which created this node
     * @param name                - name of the node
     * @param description         - description of the node
     * @param visibility          - visibility of the node to others
     * @param relativeSessionPath - name of the work session this node belongs to
     */
    public Fasta2BsmlResultNode(String owner, Task task, String name, String description, String visibility, String relativeSessionPath) {
        super(owner, task, name, description, visibility, Node.DIRECTORY_DATA_TYPE, relativeSessionPath);
    }

    public String getFilePathByTag(String tag) {
        if (tag.equals(TAG_FASTA2BSML_BSML)) return getFilePath(BASE_OUTPUT_FILENAME + "." + TAG_FASTA2BSML_BSML);
        if (tag.equals(TAG_FASTA2BSML_LIST)) return getFilePath(BASE_OUTPUT_FILENAME + "." + TAG_FASTA2BSML_LIST);
        return null;
    }

    public long getHitCount() {
        return hitCount;
    }

    public void setHitCount(long hitCount) {
        this.hitCount = hitCount;
    }
}
