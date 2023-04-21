package org.geogrid.aist.tsukubagama.filter.impl;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dyuproject.openid.OpenIdUser;
import com.dyuproject.openid.RelyingParty;
import com.dyuproject.openid.ext.AxSchemaExtension;
import com.dyuproject.openid.ext.SRegExtension;
import com.dyuproject.util.http.UrlEncodedParameterMap;
import org.geogrid.aist.tsukubagama.filter.TsukubaGamaRelyingPartyAdapter;
import org.geogrid.aist.tsukubagama.filter.TsukubaGamaWhiteList;

public final class DyuprojectRelyingPartyAdapterImpl implements TsukubaGamaRelyingPartyAdapter {

    private TsukubaGamaWhiteList wh = null;

    public DyuprojectRelyingPartyAdapterImpl() {
    }

    public void setWhiteList(TsukubaGamaWhiteList wh) {
        this.wh = wh;
    }

    public void initializeRelyingParty() {
        RelyingParty.getInstance().addListener(new RelyingParty.Listener() {

            public void onPreAuthenticate(OpenIdUser user, HttpServletRequest request, UrlEncodedParameterMap params) {
                System.err.println("pre-authenticate user: " + user.getClaimedId());
                if ((wh != null) && (!wh.check(user.getOpenIdServer()))) {
                    throw new RuntimeException("Distrusted OpenID Provider: \"" + user.getOpenIdServer() + "\"");
                }
            }

            public void onDiscovery(OpenIdUser user, HttpServletRequest request) {
            }

            public void onAuthenticate(OpenIdUser user, HttpServletRequest request) {
            }

            public void onAccess(OpenIdUser user, HttpServletRequest request) {
            }
        });
    }

    public String getClaimedId(HttpServletRequest request, HttpServletResponse response) {
        OpenIdUser user = (OpenIdUser) request.getAttribute(OpenIdUser.ATTR_NAME);
        if (user == null) {
            return null;
        }
        return user.getClaimedId();
    }

    public void invalidateOpenId(HttpServletRequest request, HttpServletResponse response) {
        try {
            RelyingParty.getInstance().invalidate(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAttribute(String key, HttpServletRequest request) {
        OpenIdUser user = (OpenIdUser) request.getAttribute(OpenIdUser.ATTR_NAME);
        if (user == null) {
            return null;
        }
        Map<String, String> info = (Map) user.getAttribute("info");
        if (info == null) {
            return null;
        }
        return info.get(key);
    }
}
