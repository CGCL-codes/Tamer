package httpclient.domain;

import httpclient.constant.Regex;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Id;
import org.apache.log4j.Logger;
import core.Item;

@javax.persistence.Entity
public class Account implements Item {

    public long getId() {
        return id;
    }

    public static final String HTTP_TOKEN = "http://";

    public static final String WWW_DOT_TOKEN = "www.";

    private static Logger logger = Logger.getLogger(Account.class);

    @Id
    private long id;

    private String username;

    private String password;

    private String siteUrl;

    private String resource;

    private Account() {
    }

    public static Builder build() {
        return new Builder();
    }

    public static class Builder {

        private Account accountToBuild;

        private Builder() {
            accountToBuild = new Account();
        }

        public static Builder build() {
            return new Builder();
        }

        public Account get() {
            return accountToBuild;
        }

        public Builder withUsername(String username) {
            accountToBuild.username = username;
            return this;
        }

        public Builder withPassword(String password) {
            accountToBuild.password = password;
            return this;
        }

        /**
		 * Remove the prefix http:// or http://www if exist
		 * 
		 * @param fullUrl
		 * @throws MalformedURLException
		 */
        public Builder withFullUrl(String fullUrl) throws MalformedURLException {
            fullUrl = fullUrl.trim();
            if (fullUrl.substring(0, HTTP_TOKEN.length()).equals(HTTP_TOKEN)) {
                fullUrl = fullUrl.replace(HTTP_TOKEN, "");
            }
            if (fullUrl.substring(0, WWW_DOT_TOKEN.length()).equals(WWW_DOT_TOKEN)) {
                fullUrl = fullUrl.replace(WWW_DOT_TOKEN, "");
            }
            Pattern patternUriWithResource = Pattern.compile(Regex.WEBSITE_WITH_RESOURCE_PATTERN);
            Matcher matcherUriWithResource = patternUriWithResource.matcher(fullUrl);
            if (matcherUriWithResource.find()) {
                logger.debug("matcherUriWithResource found");
                String foundUriWithResource = matcherUriWithResource.group();
                accountToBuild.siteUrl = foundUriWithResource.substring(0, foundUriWithResource.indexOf('/'));
                accountToBuild.resource = foundUriWithResource.substring(foundUriWithResource.indexOf('/'));
            } else {
                logger.debug("matcherUriWithResource not found");
                Pattern patternUriWithoutResource = Pattern.compile(Regex.WEBSITE_WITHOUT_RESOURCE_PATTERN);
                Matcher matcherUriWithoutResource = patternUriWithoutResource.matcher(fullUrl);
                if (matcherUriWithoutResource.find()) {
                    String foundUriWithOutResource = matcherUriWithoutResource.group();
                    accountToBuild.siteUrl = foundUriWithOutResource.replace("/", "");
                    accountToBuild.resource = "";
                } else throw new MalformedURLException(fullUrl + " misses .xxx");
            }
            return this;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public String getResource() {
        return resource;
    }

    /**
	 * Constructs a <code>String</code> with all attributes in name = value
	 * format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
    @Override
    public String toString() {
        final String separator = ";";
        String retValue = "";
        retValue = "Account (username=" + this.username + separator + "password=" + this.password + separator + "siteUrl=" + this.siteUrl + separator + "resource=" + this.resource + ")";
        return retValue;
    }

    public String generateFullUrl() {
        return "http://" + username + ":" + password + "@" + siteUrl + resource;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((resource == null) ? 0 : resource.hashCode());
        result = prime * result + ((siteUrl == null) ? 0 : siteUrl.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Account other = (Account) obj;
        if (password == null) {
            if (other.password != null) return false;
        } else if (!password.equals(other.password)) return false;
        if (resource == null) {
            if (other.resource != null) return false;
        } else if (!resource.equals(other.resource)) return false;
        if (siteUrl == null) {
            if (other.siteUrl != null) return false;
        } else if (!siteUrl.equals(other.siteUrl)) return false;
        if (username == null) {
            if (other.username != null) return false;
        } else if (!username.equals(other.username)) return false;
        return true;
    }

    public String renderObject() {
        return generateFullUrl();
    }
}
