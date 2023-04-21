package com.liferay.portal.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <a href="CompanySoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by <code>com.liferay.portal.service.http.CompanyServiceSoap</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.http.CompanyServiceSoap
 *
 */
public class CompanySoap implements Serializable {

    public static CompanySoap toSoapModel(Company model) {
        CompanySoap soapModel = new CompanySoap();
        soapModel.setCompanyId(model.getCompanyId());
        soapModel.setAccountId(model.getAccountId());
        soapModel.setWebId(model.getWebId());
        soapModel.setKey(model.getKey());
        soapModel.setVirtualHost(model.getVirtualHost());
        soapModel.setMx(model.getMx());
        soapModel.setLogoId(model.getLogoId());
        return soapModel;
    }

    public static CompanySoap[] toSoapModels(List models) {
        List soapModels = new ArrayList(models.size());
        for (int i = 0; i < models.size(); i++) {
            Company model = (Company) models.get(i);
            soapModels.add(toSoapModel(model));
        }
        return (CompanySoap[]) soapModels.toArray(new CompanySoap[0]);
    }

    public CompanySoap() {
    }

    public long getPrimaryKey() {
        return _companyId;
    }

    public void setPrimaryKey(long pk) {
        setCompanyId(pk);
    }

    public long getCompanyId() {
        return _companyId;
    }

    public void setCompanyId(long companyId) {
        _companyId = companyId;
    }

    public long getAccountId() {
        return _accountId;
    }

    public void setAccountId(long accountId) {
        _accountId = accountId;
    }

    public String getWebId() {
        return _webId;
    }

    public void setWebId(String webId) {
        _webId = webId;
    }

    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        _key = key;
    }

    public String getVirtualHost() {
        return _virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        _virtualHost = virtualHost;
    }

    public String getMx() {
        return _mx;
    }

    public void setMx(String mx) {
        _mx = mx;
    }

    public long getLogoId() {
        return _logoId;
    }

    public void setLogoId(long logoId) {
        _logoId = logoId;
    }

    private long _companyId;

    private long _accountId;

    private String _webId;

    private String _key;

    private String _virtualHost;

    private String _mx;

    private long _logoId;
}
