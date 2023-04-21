package com.icteam.fiji.security.permission;

import com.icteam.fiji.ServerServiceLocator;
import com.icteam.fiji.ServiceLocationException;
import com.icteam.fiji.ServiceLocator;
import com.icteam.fiji.command.CommandPermission;
import com.icteam.fiji.manager.AccessManager;
import com.icteam.fiji.model.Funz;
import com.icteam.fiji.model.TipPerm;
import com.icteam.fiji.security.FIJIPrincipal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Map;
import java.util.Set;

public class SocGeoPermission extends DefaultPermission {

    public static final Log logger = LogFactory.getLog(EntBsnsPermission.class);

    private AccessManager permissionManager;

    private Long m_CEntBsns = null;

    public SocGeoPermission(TipPerm p_tipPerm) {
        super(p_tipPerm, false);
    }

    public void setCEntBsns(Long p_CEntBsns) {
        m_CEntBsns = p_CEntBsns;
    }

    public void setFunzs(Set<Funz> p_funzs) {
    }

    public void setParams(Map<String, String> p_params) {
    }

    protected boolean doImpliesCommandPermission(CommandPermission permission) {
        FIJIPrincipal principal = getPrincipal();
        if (principal == null) {
            logger.info("Null principal");
            return false;
        }
        if (!(permission instanceof SocGeoPermission)) return false;
        SocGeoPermission socGeoPermission = (SocGeoPermission) permission;
        final boolean result = socGeoPermission.m_CEntBsns != null && permissionManager.checkSocGeoAccess(principal.getName(), socGeoPermission.m_CEntBsns);
        logger.debug(new StringBuilder().append("Owned permission  {").append(getName()).append(": principal has access to the following areas ").append(principal.getGeoPaths()).append("} implies access to ent_bsns(").append(socGeoPermission.m_CEntBsns).append(")? ").append(result).toString());
        return result;
    }

    protected boolean doImpliesUserPermission(CommandPermission permission) {
        return permission instanceof SocGeoPermission;
    }

    protected void initialize() {
        try {
            ServiceLocator svcLocator = ServerServiceLocator.getInstance();
            permissionManager = svcLocator.getService(AccessManager.class);
        } catch (ServiceLocationException e) {
            logger.debug(e);
        }
    }
}
