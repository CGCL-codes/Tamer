package com.entelience.objects.mspatch;

import java.io.Serializable;

/**
 * Bean - return type for patchInformation
 */
public class ProductInfo implements Serializable {

    public ProductInfo() {
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFixedInServicePack(ServicePackInfo fixedInServicePack) {
        this.fixedInServicePack = fixedInServicePack;
    }

    public ServicePackInfo getFixedInServicePack() {
        return fixedInServicePack;
    }

    public void setMinimumSupportedServicePack(ServicePackInfo minimumSupportedServicePack) {
        this.minimumSupportedServicePack = minimumSupportedServicePack;
    }

    public ServicePackInfo getMinimumSupportedServicePack() {
        return minimumSupportedServicePack;
    }

    public void setCurrentServicePack(ServicePackInfo currentServicePack) {
        this.currentServicePack = currentServicePack;
    }

    public ServicePackInfo getCurrentServicePack() {
        return currentServicePack;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setAvailableServicePacks(ServicePackInfo[] avServicePacks) {
        if (avServicePacks == null) this.availableServicePacks = null; else this.availableServicePacks = (ServicePackInfo[]) avServicePacks.clone();
    }

    public ServicePackInfo[] getAvailableServicePacks() {
        if (availableServicePacks == null) return null;
        return (ServicePackInfo[]) availableServicePacks.clone();
    }

    private int productId;

    private String name;

    private ServicePackInfo fixedInServicePack;

    private ServicePackInfo minimumSupportedServicePack;

    private ServicePackInfo currentServicePack;

    private String currentVersion;

    private ServicePackInfo[] availableServicePacks;
}
