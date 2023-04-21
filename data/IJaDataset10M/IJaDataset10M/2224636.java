package com.liferay.portal.security.auth;

import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.security.ldap.PortalLDAPUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.PwdGenerator;
import com.liferay.util.ldap.LDAPUtil;
import edu.yale.its.tp.cas.client.filter.CASFilter;
import java.util.Properties;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="CASAutoLogin.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 * @author Jorge Ferrer
 *
 */
public class CASAutoLogin implements AutoLogin {

    public String[] login(HttpServletRequest req, HttpServletResponse res) throws AutoLoginException {
        try {
            String[] credentials = null;
            long companyId = PortalUtil.getCompanyId(req);
            if (!PrefsPropsUtil.getBoolean(companyId, PropsUtil.CAS_AUTH_ENABLED)) {
                return credentials;
            }
            HttpSession ses = req.getSession();
            String screenName = (String) ses.getAttribute(CASFilter.CAS_FILTER_USER);
            if (screenName != null) {
                User user = null;
                try {
                    user = UserLocalServiceUtil.getUserByScreenName(companyId, screenName);
                } catch (NoSuchUserException nsue) {
                    if (PrefsPropsUtil.getBoolean(companyId, PropsUtil.CAS_IMPORT_FROM_LDAP)) {
                        user = addUser(companyId, screenName);
                    } else {
                        throw nsue;
                    }
                }
                credentials = new String[3];
                credentials[0] = String.valueOf(user.getUserId());
                credentials[1] = user.getPassword();
                credentials[2] = Boolean.TRUE.toString();
            }
            return credentials;
        } catch (Exception e) {
            throw new AutoLoginException(e);
        }
    }

    protected User addUser(long companyId, String screenName) throws PortalException, SystemException {
        try {
            Properties env = new Properties();
            String baseProviderURL = PrefsPropsUtil.getString(companyId, PropsUtil.LDAP_BASE_PROVIDER_URL);
            String baseDN = PrefsPropsUtil.getString(companyId, PropsUtil.LDAP_BASE_DN);
            env.put(Context.INITIAL_CONTEXT_FACTORY, PrefsPropsUtil.getString(companyId, PropsUtil.LDAP_FACTORY_INITIAL));
            env.put(Context.PROVIDER_URL, LDAPUtil.getFullProviderURL(baseProviderURL, baseDN));
            env.put(Context.SECURITY_PRINCIPAL, PrefsPropsUtil.getString(companyId, PropsUtil.LDAP_SECURITY_PRINCIPAL));
            env.put(Context.SECURITY_CREDENTIALS, PrefsPropsUtil.getString(companyId, PropsUtil.LDAP_SECURITY_CREDENTIALS));
            LdapContext ctx = null;
            try {
                ctx = new InitialLdapContext(env, null);
            } catch (Exception e) {
                if (_log.isDebugEnabled()) {
                    _log.debug("Failed to bind to the LDAP server", e);
                }
                throw new SystemException(e);
            }
            String filter = PrefsPropsUtil.getString(companyId, PropsUtil.LDAP_AUTH_SEARCH_FILTER);
            if (_log.isDebugEnabled()) {
                _log.debug("Search filter before transformation " + filter);
            }
            filter = StringUtil.replace(filter, new String[] { "@company_id@", "@email_address@", "@screen_name@" }, new String[] { String.valueOf(companyId), StringPool.BLANK, screenName });
            if (_log.isDebugEnabled()) {
                _log.debug("Search filter after transformation " + filter);
            }
            SearchControls cons = new SearchControls(SearchControls.SUBTREE_SCOPE, 1, 0, null, false, false);
            NamingEnumeration enu = ctx.search(StringPool.BLANK, filter, cons);
            if (enu.hasMore()) {
                if (_log.isDebugEnabled()) {
                    _log.debug("Search filter returned at least one result");
                }
                Binding binding = (Binding) enu.next();
                Attributes attrs = ctx.getAttributes(binding.getName());
                Attribute emailAddressAttr = attrs.get("mail");
                String emailAddress = StringPool.BLANK;
                if (emailAddressAttr != null) {
                    emailAddress = emailAddressAttr.get().toString();
                }
                return PortalLDAPUtil.importLDAPUser(companyId, ctx, attrs, emailAddress, screenName, PwdGenerator.getPassword(), true);
            } else {
                throw new NoSuchUserException("User " + screenName + " was not found in the LDAP server");
            }
        } catch (Exception e) {
            _log.error("Problem accessing LDAP server ", e);
            throw new SystemException("Problem accessign LDAP server " + e.getMessage());
        }
    }

    private static Log _log = LogFactory.getLog(CASAutoLogin.class);
}
