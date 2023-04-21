package org.perfmon4j.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgentParser {

    private UserAgentParser() {
    }

    static final Pattern MSIE_PATTERN = Pattern.compile(".*?; MSIE ([\\d\\.]*).*");

    static final Pattern OPERA_PATTERN = Pattern.compile("(.*\\) Opera (\\d+\\.\\w*).*)|(Opera/(\\d+\\.\\w*).*)");

    static final Pattern NETSCAPE_PATTERN = Pattern.compile(".*Netscape\\d?((/)|(\\s))([\\d\\.]*).*");

    static final Pattern NAVIGATOR_PATTERN = Pattern.compile(".*Navigator/(.*)");

    static final Pattern MOZILLA_4x_NETSCAPE_PATTERN = Pattern.compile("Mozilla/(4\\.\\d++).*");

    static final Pattern MOZILLA_PATTERN = Pattern.compile("Mozilla/\\d\\.\\d.*; rv:(.*)\\).*");

    static final Pattern FIREFOX_PATTERN = Pattern.compile(".*Firefox/([\\d\\.]*).*");

    static final Pattern SAFARI_PATTERN = Pattern.compile(".*AppleWebKit/([\\d\\.]*).*Safari/([\\d\\.]*).*");

    static final Pattern SAFARI_PATTERN2 = Pattern.compile(".*Safari\\s([\\d\\.]*).*WebKit\\s([\\d\\.]*).*");

    static final Pattern CHROME_PATTERN = Pattern.compile(".*AppleWebKit/([\\d\\.]*).*Chrome/([\\d\\.]*).*");

    static final Pattern WINDOWS_PATTERN = Pattern.compile(".*; (Wind.*?)((\\d+\\.\\d+)|(\\d+)).*");

    static final Pattern WINDOWS_NETSCAPE_PATTERN = Pattern.compile(".*(Wind.*?)((\\d+\\.\\d+)|(\\d+)).*");

    static final Pattern LINUX_PATTERN = Pattern.compile(".*X11; .*?;(.*?);.*.*");

    static final Pattern MAC_PATTERN = Pattern.compile(".*;(.*Mac.*?);.*");

    static final Pattern FALLBACK_OS_PATTERN = Pattern.compile(".*((\\(X11;)|(\\())(.*); [A-Z]\\).*");

    public static UserAgentVO parseUserAgentString(String userAgent) {
        UserAgentVO result = null;
        String browser = null;
        String browserVersion = null;
        String osName = null;
        String osVersion = null;
        String suffix = null;
        boolean MSIE = false;
        if (userAgent.indexOf("Opera") >= 0) {
            Matcher matcher = OPERA_PATTERN.matcher(userAgent);
            if (matcher.matches()) {
                browser = "Opera";
                browserVersion = matcher.group(2);
                if (browserVersion == null) {
                    browserVersion = matcher.group(4);
                }
            }
        } else if (userAgent.indexOf("compatible; MSIE") > 0) {
            Matcher matcher = MSIE_PATTERN.matcher(userAgent);
            if (matcher.matches()) {
                MSIE = true;
                browser = "MSIE";
                browserVersion = matcher.group(1);
            }
        } else if (userAgent.indexOf("Netscape") > 0) {
            Matcher matcher = NETSCAPE_PATTERN.matcher(userAgent);
            if (matcher.matches()) {
                browser = "Netscape";
                browserVersion = matcher.group(matcher.groupCount());
            }
        } else if (userAgent.indexOf("Navigator") > 0) {
            Matcher matcher = NAVIGATOR_PATTERN.matcher(userAgent);
            if (matcher.matches()) {
                browser = "Netscape";
                browserVersion = matcher.group(matcher.groupCount());
            }
        } else if (userAgent.indexOf("Firefox") > 0) {
            Matcher matcher = FIREFOX_PATTERN.matcher(userAgent);
            if (matcher.matches()) {
                browser = "Firefox";
                browserVersion = matcher.group(1);
            }
        } else if (userAgent.indexOf("Chrome") > 0) {
            Matcher matcher = CHROME_PATTERN.matcher(userAgent);
            if (matcher.matches()) {
                suffix = "WebKit " + matcher.group(1);
                browser = "Chrome";
                browserVersion = matcher.group(2);
            }
        } else if (userAgent.contains("Safari")) {
            Matcher matcher = SAFARI_PATTERN.matcher(userAgent);
            if (matcher.matches()) {
                suffix = "WebKit " + matcher.group(1);
                browser = "Safari";
                browserVersion = matcher.group(2);
            } else {
                matcher = SAFARI_PATTERN2.matcher(userAgent);
                if (matcher.matches()) {
                    browser = "Safari";
                    browserVersion = matcher.group(1);
                    suffix = "WebKit " + matcher.group(2);
                }
            }
        } else if (userAgent.indexOf("Mozilla") == 0) {
            Matcher matcher = MOZILLA_4x_NETSCAPE_PATTERN.matcher(userAgent);
            if (matcher.matches()) {
                browser = "Netscape";
                browserVersion = matcher.group(1);
            } else {
                matcher = MOZILLA_PATTERN.matcher(userAgent);
                if (matcher.matches()) {
                    browser = "Mozilla";
                    browserVersion = matcher.group(1);
                }
            }
        }
        boolean tryFallbackOS = false;
        if (userAgent.indexOf("; Windows ME") > 0) {
            osName = "Windows ME";
        } else if (userAgent.indexOf("; Windows XP") > 0) {
            osName = "Windows XP";
        } else if (userAgent.indexOf("; Wind") > 0) {
            Matcher matcher = WINDOWS_PATTERN.matcher(userAgent);
            if (matcher.matches()) {
                osName = matcher.group(1).trim();
                if (userAgent.indexOf(matcher.group(2) + "x") == -1) {
                    osVersion = matcher.group(2);
                }
            }
        } else if (userAgent.indexOf("Macintosh") > 0) {
            Matcher matcher = MAC_PATTERN.matcher(userAgent);
            if (matcher.matches()) {
                osName = matcher.group(1).trim();
            } else {
                osName = "Macintosh";
            }
        } else if (userAgent.indexOf("X11") > 0 || userAgent.indexOf("x11") > 0) {
            Matcher matcher = LINUX_PATTERN.matcher(userAgent);
            if (matcher.matches()) {
                osName = matcher.group(1).trim();
            } else {
                tryFallbackOS = true;
                osName = "X11";
            }
        } else {
            tryFallbackOS = true;
        }
        if (!MSIE && osName == null) {
            if (userAgent.indexOf("Win95") > 0) {
                osName = "Windows";
                osVersion = "95";
                tryFallbackOS = false;
            } else if (userAgent.indexOf("Win98") > 0) {
                osName = "Windows";
                osVersion = "98";
                tryFallbackOS = false;
            } else if (userAgent.indexOf("Win 9x") > 0) {
                osName = "Windows";
                osVersion = "9x";
                tryFallbackOS = false;
            } else if (userAgent.indexOf("WinNT") > 0) {
                osName = "WinNT";
                tryFallbackOS = false;
            } else if (userAgent.indexOf("Windows") > 0) {
                Matcher matcher = WINDOWS_NETSCAPE_PATTERN.matcher(userAgent);
                if (matcher.matches()) {
                    osName = matcher.group(1).trim();
                    osVersion = matcher.group(2);
                    tryFallbackOS = false;
                }
            }
        }
        if (tryFallbackOS) {
            Matcher matcher = FALLBACK_OS_PATTERN.matcher(userAgent);
            if (matcher.matches()) {
                osName = matcher.group(matcher.groupCount()).trim();
            }
        }
        if (browser != null) {
            result = new UserAgentVO(browser, browserVersion, osName, osVersion, suffix);
        } else {
            result = new UserAgentVO(userAgent);
        }
        return result;
    }
}
