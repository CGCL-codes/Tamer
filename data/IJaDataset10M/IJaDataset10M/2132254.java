package com.hack23.cia.service.impl.control.agent.sweden.impl.service;

import com.hack23.cia.model.api.sweden.events.ParliamentYearData;
import com.hack23.cia.model.api.sweden.factory.SwedenModelFactory;
import com.hack23.cia.service.impl.commondao.api.ParliamentYearDAO;
import com.hack23.cia.service.impl.commondao.api.ResourceDAO;
import com.hack23.cia.service.impl.control.agent.sweden.api.ParliamentAgentSupportService;

/**
 * The Class ParliamentAgentSupportServiceImpl.
 */
public class ParliamentAgentSupportServiceImpl extends AbstractAgentSupportServiceImpl implements ParliamentAgentSupportService {

    /**
	 * Instantiates a new parliament agent support service impl.
	 * 
	 * @param resourceDAO the resource dao
	 * @param swedenModelFactory the sweden model factory
	 * @param parliamentYearDAO the parliament year dao
	 */
    public ParliamentAgentSupportServiceImpl(final ResourceDAO resourceDAO, final SwedenModelFactory swedenModelFactory, final ParliamentYearDAO parliamentYearDAO) {
        super(resourceDAO, parliamentYearDAO, swedenModelFactory);
    }

    /**
	 * Generate parliament charts.
	 * 
	 * @param parliamentYearData the parliament year data
	 */
    @Override
    public void generateParliamentCharts(final ParliamentYearData parliamentYearData) {
    }
}
