package com.alibaba.cobar.client.router.support;

import java.util.List;
import com.alibaba.cobar.client.merger.IMerger;

/**
 * @author fujohnwang
 * @since 1.0
 */
public class RoutingResult {

    private List<String> resourceIdentities;

    private IMerger<?, ?> merger;

    /**
     * ����
     */
    private String tableSuffix;

    public String getTableSuffix() {
        return tableSuffix;
    }

    public void setTableSuffix(String tableSuffix) {
        this.tableSuffix = tableSuffix;
    }

    public List<String> getResourceIdentities() {
        return resourceIdentities;
    }

    public void setResourceIdentities(List<String> resourceIdentities) {
        this.resourceIdentities = resourceIdentities;
    }

    public void setMerger(IMerger<?, ?> merger) {
        this.merger = merger;
    }

    public IMerger<?, ?> getMerger() {
        return merger;
    }
}
