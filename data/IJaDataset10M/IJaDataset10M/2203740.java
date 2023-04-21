package cn.com.believer.songyuanframework.openapi.storage.xdrive.object.functional.file;

import java.util.ArrayList;
import java.util.List;
import cn.com.believer.songyuanframework.openapi.storage.xdrive.object.functional.BaseOutput;

/**
 * @author Jimmy
 * 
 */
public class FileReIndexOutput extends BaseOutput {

    /** FileObject list, The reindexed files. */
    private List reindexed;

    /**
     * 
     */
    public FileReIndexOutput() {
        reindexed = new ArrayList();
    }

    /**
     * @return the reindexed
     */
    public List getReindexed() {
        return this.reindexed;
    }

    /**
     * @param reindexed
     *            the reindexed to set
     */
    public void setReindexed(List reindexed) {
        this.reindexed = reindexed;
    }
}
