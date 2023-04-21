package org.dasein.cloud.euca.identity;

import org.dasein.cloud.euca.Eucalyptus;
import org.dasein.cloud.identity.AbstractIdentityServices;
import javax.annotation.Nonnull;

public class EucalyptusIdentity extends AbstractIdentityServices {

    private Eucalyptus cloud;

    public EucalyptusIdentity(Eucalyptus cloud) {
        this.cloud = cloud;
    }

    @Override
    @Nonnull
    public IAM getIdentityAndAccessSupport() {
        return new IAM(cloud);
    }

    @Override
    @Nonnull
    public Keypairs getShellKeySupport() {
        return new Keypairs(cloud);
    }
}
