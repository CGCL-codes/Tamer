package com.liferay.portal.security.auth;

import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.util.HttpUtil;
import com.liferay.util.PwdGenerator;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="OpenSSOAutoLogin.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class OpenSSOAutoLogin implements AutoLogin {

    public String[] login(HttpServletRequest req, HttpServletResponse res) throws AutoLoginException {
        String[] credentials = null;
        try {
            long companyId = PortalUtil.getCompanyId(req);
            if (!PrefsPropsUtil.getBoolean(companyId, PropsUtil.OPEN_SSO_AUTH_ENABLED)) {
                return credentials;
            }
            HttpSession ses = req.getSession();
            String subjectId = (String) ses.getAttribute(WebKeys.OPEN_SSO_LOGIN);
            if (subjectId == null) {
                return credentials;
            }
            Map nameValues = new HashMap();
            String serviceUrl = PrefsPropsUtil.getString(companyId, PropsUtil.OPEN_SSO_SERVICE_URL);
            String url = serviceUrl + "/attributes?subjectid=" + HttpUtil.encodeURL(subjectId);
            URL urlObj = new URL(url);
            URLConnection con = urlObj.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) con.getContent()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if ((parts == null) || (parts.length != 2)) {
                    continue;
                }
                String attrName = null;
                String attrValue = null;
                if (parts[0].endsWith("name")) {
                    attrName = parts[1];
                    line = reader.readLine();
                    if (line == null) {
                        throw new AutoLoginException("Error reading user attributes");
                    }
                    parts = line.split("=");
                    if ((parts == null) || (parts.length != 2) || (!parts[0].endsWith("value"))) {
                        attrValue = null;
                    } else {
                        attrValue = parts[1];
                    }
                    nameValues.put(attrName, attrValue);
                }
            }
            String firstName = (String) nameValues.get("cn");
            String lastName = (String) nameValues.get("sn");
            String screenName = (String) nameValues.get("givenname");
            String emailAddress = (String) nameValues.get("mail");
            User user = null;
            try {
                user = UserLocalServiceUtil.getUserByEmailAddress(companyId, emailAddress);
            } catch (NoSuchUserException nsue) {
                ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
                user = addUser(companyId, firstName, lastName, emailAddress, screenName, themeDisplay.getLocale());
            }
            credentials = new String[3];
            credentials[0] = String.valueOf(user.getUserId());
            credentials[1] = user.getPassword();
            credentials[2] = Boolean.TRUE.toString();
        } catch (Exception e) {
            _log.error(e.getMessage());
        }
        return credentials;
    }

    protected User addUser(long companyId, String firstName, String lastName, String emailAddress, String screenName, Locale locale) throws Exception {
        long creatorUserId = 0;
        boolean autoPassword = false;
        String password1 = PwdGenerator.getPassword();
        String password2 = password1;
        boolean autoScreenName = false;
        String middleName = StringPool.BLANK;
        int prefixId = 0;
        int suffixId = 0;
        boolean male = true;
        int birthdayMonth = Calendar.JANUARY;
        int birthdayDay = 1;
        int birthdayYear = 1970;
        String jobTitle = StringPool.BLANK;
        long[] organizationIds = new long[0];
        boolean sendEmail = false;
        return UserLocalServiceUtil.addUser(creatorUserId, companyId, autoPassword, password1, password2, autoScreenName, screenName, emailAddress, locale, firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, organizationIds, sendEmail);
    }

    private static Log _log = LogFactory.getLog(OpenSSOAutoLogin.class);
}
