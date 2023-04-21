package hr.chus.cchat.db.service;

import hr.chus.cchat.model.db.jpa.ServiceProviderKeyword;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public interface ServiceProviderKeywordService {

    public void addServiceProviderKeyword(ServiceProviderKeyword serviceProviderKeyword);

    public void removeServiceProviderKeyword(ServiceProviderKeyword serviceProviderKeyword);

    public ServiceProviderKeyword addOrEditServiceProviderKeyword(ServiceProviderKeyword serviceProviderKeyword);

    public ServiceProviderKeyword getById(Integer id);
}
