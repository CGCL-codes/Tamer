package org.tanso.fountain.router.httpproxy;

import java.util.*;
import org.tanso.fountain.router.util.AppItem;
import org.tanso.fountain.router.util.RoutingTable;

/**
 * The UrlSubstitute class is used to substitute the original URL from HTTP  Request <br />
 * To substibute the original URL,you need to parse the application name from the original URL,then look up the routing table<br \> 
 * which is passed in the constructor function.<br \>
 * @author Song Huanhuan
 */
public class UrlSubstitute {

    /**
	 * routingTable is getted from the constructor as param.
	 */
    private RoutingTable routingTable;

    /**
	 * the constructor of Urlsubstitute with the param routintgTable 
	 * 
	 */
    public UrlSubstitute(RoutingTable routingTable) {
        this.routingTable = routingTable;
    }

    /**
	 * the getSubstituteUrl method is used to substitute the original URL according the application from the URL.
	 * @return the substituted URL
	 */
    public String getSubstituteUrl(String orgUrl) {
        String other = null;
        int urlLen = orgUrl.length();
        String httpHeader = orgUrl.substring(0, 4);
        int index = 0, i, startIndex = 0, endIndex = 0;
        String appName = null;
        String SubstitutteUrl = null;
        @SuppressWarnings("unused") String port = null;
        if ((httpHeader.toLowerCase()).equals(new String("http"))) index = 7; else index = 0;
        String subOrgUrl = orgUrl.substring(index);
        startIndex = subOrgUrl.indexOf(':');
        if (startIndex != -1) {
            startIndex += 1;
            for (i = startIndex; i < subOrgUrl.length(); i++) {
                char ch = subOrgUrl.charAt(i);
                if (ch <= '9' && ch >= '0') continue; else break;
            }
            endIndex = i;
            port = ":" + subOrgUrl.substring(startIndex, endIndex);
        } else port = "";
        startIndex = subOrgUrl.indexOf('/');
        if (startIndex != -1 && startIndex + 1 < subOrgUrl.length()) {
            startIndex += 1;
            for (i = startIndex; i < subOrgUrl.length(); i++) {
                char ch = subOrgUrl.charAt(i);
                if (ch <= 'z' && ch >= 'a' || ch <= 'Z' && ch >= 'A' || ch <= '9' && ch >= '0' || ch == '_') continue; else break;
            }
            endIndex = i;
            appName = subOrgUrl.substring(startIndex, endIndex);
            if (endIndex < urlLen - 7) {
                other = subOrgUrl.substring(endIndex + 1, urlLen - 7);
            }
        } else appName = null;
        if (appName != null) {
            HashSet<AppItem> appItem = routingTable.getByKey(appName);
            if (appItem != null) {
                Iterator<AppItem> it = appItem.iterator();
                AppItem item = it.next();
                SubstitutteUrl = item.appRoot;
                if (other != null && other.length() != 0) {
                    int len = other.length();
                    if (other.charAt(len - 1) == '/') other = other.substring(0, len - 1);
                    SubstitutteUrl += other;
                }
            } else SubstitutteUrl = orgUrl;
        } else SubstitutteUrl = orgUrl;
        return SubstitutteUrl;
    }
}
