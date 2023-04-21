package com.esri.gpt.catalog.harvest.jobs;

import com.esri.gpt.framework.request.HybridCriteria;

/**
 * Harvest job hybryd criteria.
 * @see HjRequest
 */
public class HjCriteria extends HybridCriteria<HjActionCriteria, HjQueryCriteria> {

    /**
 * Creates instance of the criteria.
 */
    public HjCriteria() {
        super(new HjActionCriteria(), new HjQueryCriteria());
    }
}
