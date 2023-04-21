package edu.uiuc.ncsa.myproxy.oa4mp.server.storage.sql;

import edu.uiuc.ncsa.security.storage.sql.internals.ColumnDescriptorEntry;
import edu.uiuc.ncsa.security.storage.sql.internals.Table;
import static java.sql.Types.*;

/**
 * <p>Created by Jeff Gaynor<br>
 * on May 26, 2011 at  9:41:59 AM
 */
public class ClientApprovalTable extends Table {

    /**
     * The schema and prefix are not part of the table's information, actually, but are needed to
     * create its fully qualified name in context. Hence they must be supplied.
     *
     * @param schema
     * @param tablenamePrefix
     */
    public ClientApprovalTable(String schema, String tablenamePrefix, String tablename) {
        super(schema, tablenamePrefix, tablename);
    }

    @Override
    public void createColumnDescriptors() {
        getColumnDescriptor().add(new ColumnDescriptorEntry(getKey(), LONGVARCHAR, false, true));
        getColumnDescriptor().add(new ColumnDescriptorEntry(getApprover(), LONGVARCHAR));
        getColumnDescriptor().add(new ColumnDescriptorEntry(getApprovalTS(), TIMESTAMP));
        getColumnDescriptor().add(new ColumnDescriptorEntry(getApproved(), BOOLEAN));
    }

    String key = "oauth_consumer_key";

    String approved = "approved";

    String approvalTS = "approval_ts";

    String approver = "approver";

    public String getApprovalTS() {
        return approvalTS;
    }

    public void setApprovalTS(String approvalTS) {
        this.approvalTS = approvalTS;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
