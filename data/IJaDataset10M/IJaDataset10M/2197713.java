package org.dasein.cloud.jclouds.cloudsigma.network;

import org.dasein.cloud.jclouds.cloudsigma.CloudSigma;
import org.dasein.cloud.network.AbstractNetworkServices;
import javax.annotation.Nonnull;

public class CSNetworkServices extends AbstractNetworkServices {

    private CloudSigma cloud;

    public CSNetworkServices(@Nonnull CloudSigma cloud) {
        this.cloud = cloud;
    }

    @Override
    @Nonnull
    public StaticIP getIpAddressSupport() {
        return new StaticIP(cloud);
    }
}
