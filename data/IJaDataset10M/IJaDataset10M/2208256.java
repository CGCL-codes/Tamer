package com.hack23.cia.service.impl.admin.agent.sweden.impl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.hack23.cia.model.api.common.ResourceType;
import com.hack23.cia.model.impl.sweden.ParliamentYear;
import com.hack23.cia.service.impl.admin.agent.sweden.api.ParliamentYearAgentSupportService;
import com.hack23.cia.service.impl.commondao.api.ParliamentYearDAO;
import com.hack23.cia.service.impl.commondao.api.ResourceDAO;

/**
 * The Class ParliamentYearAgentSupportServiceImpl.
 */
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ParliamentYearAgentSupportServiceImpl extends AbstractAgentSupportServiceImpl implements ParliamentYearAgentSupportService {

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(ParliamentYearAgentSupportServiceImpl.class);

    /** The parliament year dao. */
    private final ParliamentYearDAO parliamentYearDAO;

    /**
	 * Instantiates a new parliament year agent support service impl.
	 * 
	 * @param resourceDAO the resource dao
	 * @param parliamentYearDAO the parliament year dao
	 */
    public ParliamentYearAgentSupportServiceImpl(final ResourceDAO resourceDAO, final ParliamentYearDAO parliamentYearDAO) {
        super(resourceDAO);
        this.parliamentYearDAO = parliamentYearDAO;
    }

    @Override
    public final void addIfNotExist(final ParliamentYear parliamentYear) {
        for (ParliamentYear paYear : parliamentYearDAO.getAllImplementations()) {
            if (paYear.getShortCode().equals(parliamentYear.getShortCode())) {
                return;
            }
        }
        parliamentYear.setResourceType(ResourceType.ApplicationData);
        this.parliamentYearDAO.save(parliamentYear);
        logger.info("Imported ParliamentYear:" + parliamentYear.getShortCode());
    }

    @Override
    public final void generateParliamentYearCharts() {
    }
}
