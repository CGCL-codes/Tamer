package net.sourceforge.cvsgrab.web;

import java.util.Properties;
import org.apache.commons.httpclient.URIException;
import net.sourceforge.cvsgrab.CVSGrab;
import net.sourceforge.cvsgrab.WebBrowser;

/**
 * Support for FishEye 1.2 interfaces to a cvs repository
 *
 * @author <a href="mailto:shinobukawai@users.sourceforge.net">Shinobu Kawai</a>
 * @cvsgrab.created June 7, 2006
 */
public class FishEye1_2Interface extends FishEye1_0Interface {

    public FishEye1_2Interface(CVSGrab grabber) {
        super(grabber);
        setWebInterfaceType("browse");
    }

    public Properties guessWebProperties(String url) {
        Properties properties = new Properties();
        int keywordPosition = url.toLowerCase().indexOf(getWebInterfaceType());
        if (keywordPosition > 0) {
            String versionTag = null;
            String query = null;
            int rootUrlPosition = url.indexOf('/', keywordPosition) + 1;
            int startPackagePath = rootUrlPosition;
            if ('~' == url.charAt(rootUrlPosition)) {
                startPackagePath = url.indexOf('/', rootUrlPosition) + 1;
                int eqPos = url.indexOf('=', rootUrlPosition);
                if ("tag".equals(url.substring(rootUrlPosition + 1, eqPos))) {
                    int endVersionTag = startPackagePath - 1;
                    int commaPos = url.indexOf(',', eqPos);
                    if (commaPos > 0) {
                        endVersionTag = commaPos;
                    }
                    versionTag = url.substring(eqPos + 1, endVersionTag);
                }
            }
            String guessedRootUrl = url.substring(0, rootUrlPosition);
            String guessedPackagePath = url.substring(startPackagePath);
            properties.put(CVSGrab.ROOT_URL_OPTION, guessedRootUrl);
            properties.put(CVSGrab.PACKAGE_PATH_OPTION, guessedPackagePath);
            if (versionTag != null && versionTag.trim().length() > 0) {
                properties.put(CVSGrab.TAG_OPTION, versionTag);
            }
            int queryPos = guessedPackagePath.indexOf('?');
            if (queryPos >= 0) {
                query = guessedPackagePath.substring(queryPos + 1);
                guessedPackagePath = guessedPackagePath.substring(0, queryPos);
            } else {
                query = "%40hideDeletedFiles=Y";
            }
            properties.put(CVSGrab.ROOT_URL_OPTION, guessedRootUrl);
            properties.put(CVSGrab.PACKAGE_PATH_OPTION, guessedPackagePath);
            if (versionTag != null && versionTag.trim().length() > 0) {
                properties.put(CVSGrab.TAG_OPTION, versionTag);
            }
            if (query != null && query.trim().length() > 0) {
                properties.put(CVSGrab.QUERY_PARAMS_OPTION, query);
            }
        }
        return properties;
    }

    /**
     * {@inheritDoc}
     */
    public String getDirectoryUrl(String rootUrl, String directoryName) {
        try {
            String url = WebBrowser.forceFinalSlash(rootUrl);
            if (getVersionTag() != null) {
                url += "~tag=" + getVersionTag() + "/";
            }
            url += WebBrowser.forceFinalSlash(quote(directoryName));
            url = WebBrowser.addQueryParam(url, getQueryParams());
            return url;
        } catch (URIException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Cannot create URI");
        }
    }
}
