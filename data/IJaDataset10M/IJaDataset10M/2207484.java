package httpclient.persistence.family;

import httpclient.dao.ItemDao;
import httpclient.domain.HttpProxy;
import org.springframework.beans.factory.annotation.Required;

public class FamilyHibernate {

    private ItemDao<HttpProxy> httpProxyDao;

    private ItemDao<HttpProxy> accountDao;

    public ItemDao<HttpProxy> getAccountDao() {
        return accountDao;
    }

    public ItemDao<HttpProxy> getHttpProxyDao() {
        return httpProxyDao;
    }

    @Required
    public void setHttpProxyDao(ItemDao<HttpProxy> httpProxyDao) {
        this.httpProxyDao = httpProxyDao;
    }

    @Required
    public void setAccountDao(ItemDao<HttpProxy> accountDao) {
        this.accountDao = accountDao;
    }
}
