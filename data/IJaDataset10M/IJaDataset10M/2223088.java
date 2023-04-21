package com.googlecode.habano.util;

/**
 * Default implementation of the {@link CpuInfoHandler}.
 */
public class DefaultCpuInfoHandler implements CpuInfoHandler {

    @Override
    public Boolean start() {
        return true;
    }

    @Override
    public Boolean end() {
        return true;
    }

    @Override
    public Boolean handleProcessor(Integer value) {
        return true;
    }

    @Override
    public Boolean handleVendorId(String value) {
        return true;
    }

    @Override
    public Boolean handleCpuFamily(String value) {
        return true;
    }

    @Override
    public Boolean handleModel(String value) {
        return true;
    }

    @Override
    public Boolean handleModelName(String value) {
        return true;
    }

    @Override
    public Boolean handleStepping(String value) {
        return true;
    }

    @Override
    public Boolean handleCpuMhz(Float value) {
        return true;
    }

    @Override
    public Boolean handleCacheSize(Long value) {
        return true;
    }

    @Override
    public Boolean handlePhysicalId(String value) {
        return true;
    }

    @Override
    public Boolean handleSyblings(Integer value) {
        return true;
    }

    @Override
    public Boolean handleCoreId(String value) {
        return true;
    }

    @Override
    public Boolean handleCpuCores(Integer value) {
        return true;
    }

    @Override
    public Boolean handleAcpiId(String value) {
        return true;
    }

    @Override
    public Boolean handleInitialAcpiId(String value) {
        return true;
    }

    @Override
    public Boolean handleFdivBug(Boolean value) {
        return true;
    }

    @Override
    public Boolean handleHltBug(Boolean value) {
        return true;
    }

    @Override
    public Boolean handleF00fBug(Boolean value) {
        return true;
    }

    @Override
    public Boolean handleComaBug(Boolean value) {
        return true;
    }

    @Override
    public Boolean handleFpu(Boolean value) {
        return true;
    }

    @Override
    public Boolean handleFpuException(Boolean value) {
        return true;
    }

    @Override
    public Boolean handleCpuidLevel(String value) {
        return true;
    }

    @Override
    public Boolean handleWp(String value) {
        return true;
    }

    @Override
    public Boolean handleFlags(String[] value) {
        return true;
    }

    @Override
    public Boolean handleBogomips(Float value) {
        return true;
    }

    @Override
    public Boolean handleClfushSize(Integer value) {
        return true;
    }

    @Override
    public Boolean handleCacheAlignment(Integer value) {
        return true;
    }

    @Override
    public Boolean handleAddressSizes(String value) {
        return true;
    }

    @Override
    public Boolean handlePowerManagement(String value) {
        return true;
    }
}
