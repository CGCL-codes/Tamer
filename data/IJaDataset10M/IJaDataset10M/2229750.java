package org.slasoi.businessManager.common.dao;

import java.util.List;
import org.slasoi.businessManager.common.model.EmSpServices;

public interface ServiceDAO extends AbstractHibernateDAO<EmSpServices, Long> {

    public abstract List<EmSpServices> findByFilter(String filter);

    public abstract List<EmSpServices> getServicesByParty(Long partyId);

    public abstract EmSpServices getServiceBySlatId(String txSlatid);

    public abstract List<EmSpServices> findByParty(Long partyId, String filter);
}
