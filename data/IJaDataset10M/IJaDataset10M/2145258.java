package com.griddynamics.convergence.monitor.gemstone;

import com.gemstone.gemfire.cache.AttributesFactory;
import com.gemstone.gemfire.cache.CacheLoader;
import com.gemstone.gemfire.cache.CacheLoaderException;
import com.gemstone.gemfire.cache.ExpirationAction;
import com.gemstone.gemfire.cache.ExpirationAttributes;
import com.gemstone.gemfire.cache.LoaderHelper;
import com.gemstone.gemfire.cache.PartitionAttributes;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionAttributes;
import com.gemstone.gemfire.distributed.DistributedMember;
import com.gemstone.gemfire.internal.cache.PartitionAttributesImpl;
import com.gemstone.gemfire.internal.cache.PartitionedRegion;
import com.griddynamics.convergence.utils.gemfire.PartitionAttributesFactoryExt;

public class GemFirePartitionMonitorAgent {

    public static final String SERVICE_REGION = "KEY_OWNERSHIP";

    private PartitionedRegion parentRegion;

    private Region ownershipRegion;

    public GemFirePartitionMonitorAgent(Region parent) {
        this.parentRegion = (PartitionedRegion) parent;
        initRegionAgent();
    }

    public static Region getKeyOwnerMap(Region region) {
        String regionName = region.getName() + "-" + SERVICE_REGION;
        return region.getCache().getRegion(regionName);
    }

    private void initRegionAgent() {
        RegionAttributes attributes = parentRegion.getAttributes();
        PartitionAttributes pattr = attributes.getPartitionAttributes();
        CacheLoader loader = new CacheLoader() {

            public Object load(LoaderHelper helper) throws CacheLoaderException {
                Object key = helper.getKey();
                DistributedMember node = parentRegion.getMemberOwning(key);
                if (node != null) {
                    return node.getHost();
                } else {
                    return null;
                }
            }

            public void close() {
            }
        };
        PartitionAttributesFactoryExt newPAttr = new PartitionAttributesFactoryExt();
        newPAttr.setPartitionResolver(((PartitionAttributesImpl) pattr).getPartitionResolver());
        newPAttr.setTotalNumBuckets(pattr.getTotalNumBuckets());
        AttributesFactory af = new AttributesFactory();
        af.setEntryTimeToLive(new ExpirationAttributes(1, ExpirationAction.DESTROY));
        af.setCacheLoader(loader);
        af.setStatisticsEnabled(true);
        String regionName = parentRegion.getName() + "-" + SERVICE_REGION;
        ownershipRegion = parentRegion.getCache().createRegion(regionName, af.create());
    }
}
