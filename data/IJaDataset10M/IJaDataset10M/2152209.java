package com.netflexitysolutions.amazonws.s3;

import java.util.List;

public class S3AccessControlPolicy {

    private S3Owner owner;

    private List<S3Grant> acl;

    public S3AccessControlPolicy(S3Owner owner, List<S3Grant> acl) {
        this.owner = owner;
        this.acl = acl;
    }

    public S3Owner getOwner() {
        return owner;
    }

    public void setOwner(S3Owner owner) {
        this.owner = owner;
    }

    public List<S3Grant> getAcl() {
        return acl;
    }

    public void setAcl(List<S3Grant> acl) {
        this.acl = acl;
    }
}
