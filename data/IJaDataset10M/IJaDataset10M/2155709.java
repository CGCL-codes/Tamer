package prototype;

import net.sf.buildbox.devmodel.VcsLocation;
import net.sf.buildbox.devmodel.model.Contact;

public class BatchProxy {

    private VcsLocation project;

    private Contact contact;

    private String revision;

    private String batchId;

    public void setVcsLocationContext(VcsLocation project) {
        this.project = project;
    }

    public void setContactContext(Contact contact) {
        this.contact = contact;
    }

    public void setRevisionContext(String revision) {
        this.revision = revision;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    /**
     * creates or gets existing job from this batch, by activity name
     *
     * @param activity
     * @return
     */
    public JobProxy innerJob(String activity) {
        return null;
    }

    /**
     * - does not become part of the batch
     * - if such a job does not exist, it is scheduled (as placeholder or replaceable job)
     *
     * @param activity
     * @return
     */
    public JobProxy outerJob(String activity) {
        return null;
    }

    public String getRevisionContext() {
        return revision;
    }
}
