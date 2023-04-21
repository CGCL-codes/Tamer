package com.liferay.portlet.admin.ejb;

/**
 * <a href="AdminConfigHBM.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.15 $
 *
 */
public class AdminConfigHBM {

    protected AdminConfigHBM() {
    }

    protected AdminConfigHBM(String configId) {
        _configId = configId;
    }

    protected AdminConfigHBM(String configId, String companyId, String type, String name, String config) {
        _configId = configId;
        _companyId = companyId;
        _type = type;
        _name = name;
        _config = config;
    }

    public String getPrimaryKey() {
        return _configId;
    }

    protected void setPrimaryKey(String pk) {
        _configId = pk;
    }

    protected String getConfigId() {
        return _configId;
    }

    protected void setConfigId(String configId) {
        _configId = configId;
    }

    protected String getCompanyId() {
        return _companyId;
    }

    protected void setCompanyId(String companyId) {
        _companyId = companyId;
    }

    protected String getType() {
        return _type;
    }

    protected void setType(String type) {
        _type = type;
    }

    protected String getName() {
        return _name;
    }

    protected void setName(String name) {
        _name = name;
    }

    protected String getConfig() {
        return _config;
    }

    protected void setConfig(String config) {
        _config = config;
    }

    private String _configId;

    private String _companyId;

    private String _type;

    private String _name;

    private String _config;
}
