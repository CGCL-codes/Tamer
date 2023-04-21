package eu.popeye.middleware.dataSharing.distributedDataManagement.ReplicationScheme;

import eu.popeye.middleware.groupmanagement.membership.Member;

public class ReplicationResult {

    public ReplicationResult() {
    }

    public ReplicationResult(boolean doReplicate, Member host) {
        this.replicate = doReplicate;
        this.host = host;
    }

    public boolean replicate;

    public Member host;
}
