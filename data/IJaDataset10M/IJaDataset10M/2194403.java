package org.easyrec.soap.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easyrec.model.core.TenantConfigVO;
import org.easyrec.model.core.TenantVO;
import org.easyrec.service.core.impl.TenantServiceImpl;
import org.easyrec.soap.service.AuthenticationService;
import org.easyrec.store.dao.core.AuthenticationDAO;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Properties;

/**
 * <DESCRIPTION>
 * <p/>
 * <p><b>Company:&nbsp;</b>
 * SAT, Research Studios Austria</p>
 * <p/>
 * <p><b>Copyright:&nbsp;</b>
 * (c) 2007</p>
 * <p/>
 * <p><b>last modified:</b><br/>
 * $Author: pmarschik $<br/>
 * $Date: 2011-02-11 11:48:48 +0100 (Fr, 11 Feb 2011) $<br/>
 * $Revision: 17667 $</p>
 *
 * @author Stephan Zavrel
 */
public class ReferrerAuthenticationServiceImpl implements AuthenticationService {

    private TenantServiceImpl tenantService;

    private AuthenticationDAO authenticationDAO;

    private final Log logger = LogFactory.getLog(this.getClass());

    public ReferrerAuthenticationServiceImpl(TenantServiceImpl tenantService) {
        this.tenantService = tenantService;
        this.authenticationDAO = tenantService.getAuthenticationDAO();
    }

    public Integer authenticateTenant(String tenant, HttpServletRequest request) {
        String accessDomain = request.getHeader("Referer");
        if (logger.isDebugEnabled()) logger.debug("Trying to authenticate Referer '" + accessDomain + "' for tenant '" + tenant + "'");
        TenantVO tenantVO = getTenantByStringId(tenant);
        if ((tenantVO != null) && (accessDomain != null)) {
            List<String> domainURLs = authenticationDAO.getDomainURLsForTenant(tenantVO.getId());
            for (String URL : domainURLs) {
                if (accessDomain.contains(URL)) return tenantVO.getId();
            }
        }
        return null;
    }

    public boolean authenticateTenant(Integer tenantId, HttpServletRequest request) {
        String accessDomain = request.getHeader("Referer");
        if (logger.isDebugEnabled()) logger.debug("Trying to authenticate Referer '" + accessDomain + "' for tenantId '" + tenantId + "'");
        if (accessDomain != null) {
            List<String> domainURLs = authenticationDAO.getDomainURLsForTenant(tenantId);
            for (String URL : domainURLs) {
                if (accessDomain.contains(URL)) return true;
            }
        }
        return false;
    }

    public List<Integer> authenticateDomain(String accessDomain) {
        return authenticationDAO.getTenantsForDomainURL(accessDomain);
    }

    public int deactivateTenant(TenantVO tenant) {
        return tenantService.deactivateTenant(tenant);
    }

    public TenantVO getTenantById(Integer tenantId) {
        return tenantService.getTenantById(tenantId);
    }

    public TenantVO getTenantByStringId(String stringId) {
        return tenantService.getTenantByStringId(stringId);
    }

    public int insertTenantWithTypes(TenantVO tenant, TenantConfigVO tenantConfig) {
        return tenantService.insertTenantWithTypes(tenant, tenantConfig);
    }

    public List<TenantVO> getAllTenants() {
        return tenantService.getAllTenants();
    }

    public boolean removeTenantWithTypes(TenantVO tenant) {
        return tenantService.removeTenantWithTypes(tenant);
    }

    public Properties getTenantConfig(Integer tenantId) {
        return tenantService.getTenantConfig(tenantId);
    }

    public int storeTenantConfig(Integer tenantId, Properties tenantConfig) {
        return tenantService.storeTenantConfig(tenantId, tenantConfig);
    }
}
