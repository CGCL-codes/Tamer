package org.gbif.checklistbank.service.mybatis;

import org.gbif.checklistbank.api.model.Distribution;
import org.gbif.checklistbank.api.service.DistributionService;
import com.google.inject.Inject;

/**
 * Implements a DistributionService using MyBatis.
 */
public class DistributionServiceMyBatis extends NameUsageComponentServiceMyBatis<Distribution, Distribution> implements DistributionService {

    @Inject
    DistributionServiceMyBatis(DistributionMapper distributionMapper) {
        super(distributionMapper);
    }
}
