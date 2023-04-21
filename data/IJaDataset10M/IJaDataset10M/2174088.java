package org.dasein.cloud.jclouds.terremark.network;

import org.dasein.cloud.network.AbstractNetworkServices;
import org.dasein.cloud.jclouds.terremark.Terremark;
import javax.annotation.Nonnull;

public class TerremarkNetworkServices extends AbstractNetworkServices {

    private Terremark cloud;

    public TerremarkNetworkServices(@Nonnull Terremark cloud) {
        this.cloud = cloud;
    }

    @Override
    @Nonnull
    public PublicIp getIpAddressSupport() {
        return new PublicIp(cloud);
    }
}
