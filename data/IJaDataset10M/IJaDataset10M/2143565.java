package net.jixi.pipeline.objecttree;

import net.jixi.objecttree.ObjectTreeNode;
import net.jixi.pipeline.PipelineHandlerBase;

/**
 * DOCUMENT ME!
 *
 * @author $Author: eschnepel $
 * @version $Revision: 1.5 $
 */
public final class Pipeline extends PipelineHandlerBase {

    /** DOCUMENT ME! */
    private String pName;

    /**
     * Creates a new Pipeline object.
     */
    private Pipeline() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ObjectTreeNode build() {
        return new Pipeline();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setPipelineName(final String name) {
        this.pName = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the Name of the Pipeline.
     */
    public String getPipelineName() {
        return pName;
    }
}
