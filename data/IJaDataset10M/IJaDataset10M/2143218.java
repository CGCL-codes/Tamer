package org.webcastellum;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class RequestUtils {

    private RequestUtils() {
    }

    private static final boolean DEBUG = false;

    private static boolean isOldJavaEE13 = false;

    private static final String SENSITIVE_VALUE_REMOVED = "<SENSITIVE-DATA-REMOVED>";

    private static final String CLIENT_LOGGING_DISABLED = "<CLIENT-LOGGING-DISABLED>";

    private static final String IMAGE_MAP_EXLUDE_EXPRESSION = "(?i).*(_(.y|.x){0,2}\\z)";

    public static String getContentType(final ServletRequest request) {
        String type = request.getContentType();
        if (type == null && request instanceof HttpServletRequest) {
            type = ((HttpServletRequest) request).getHeader("Content-Type");
        }
        return type;
    }

    public static String determineClientIp(final HttpServletRequest request, final ClientIpDeterminator clientIpDeterminator) {
        if (clientIpDeterminator != null) {
            try {
                final String result = clientIpDeterminator.determineClientIp(request);
                return result != null ? result : request.getRemoteAddr();
            } catch (ClientIpDeterminationException e) {
                System.err.println("Unable to determine client IP: " + e.getMessage());
            } catch (RuntimeException e) {
                System.err.println("Unable to determine client IP (RuntimeException): " + e.getMessage());
            }
        }
        return request.getRemoteAddr();
    }

    public static String extractSecurityRelevantRequestContent(final HttpServletRequest request, final String ip, final boolean logSessionValues, final Pattern sensitiveRequestParamNamePattern, final Pattern sensitiveRequestParamNameAndValueUrlPattern, final Pattern sensitiveValuePattern, final boolean logClientUserData) {
        final StringBuilder logMessage = new StringBuilder();
        try {
            appendValueToMessage(logMessage, "client", logClientUserData ? ip : CLIENT_LOGGING_DISABLED);
            appendValueToMessage(logMessage, "date", "" + new Date());
            appendValueToMessage(logMessage, "servletPath", request.getServletPath());
            final Matcher sensitiveRequestParamNameMatcherToReuse = sensitiveRequestParamNamePattern.matcher("");
            final Matcher sensitiveRequestParamNameAndValueUrlMatcherToReuse = sensitiveRequestParamNameAndValueUrlPattern.matcher("");
            final Matcher sensitiveValueMatcherToReuse = sensitiveValuePattern.matcher("");
            final String queryString = removeSensitiveData(null, request.getQueryString(), sensitiveRequestParamNameMatcherToReuse, sensitiveRequestParamNameAndValueUrlMatcherToReuse, sensitiveValueMatcherToReuse);
            appendValueToMessage(logMessage, "queryString (sensitive data removed)", queryString);
            appendValueToMessage(logMessage, "requestedSessionId", request.getRequestedSessionId());
            appendValueToMessage(logMessage, "requestedSessionIdValid", "" + request.isRequestedSessionIdValid());
            appendValueToMessage(logMessage, "requestURL", request.getRequestURL());
            appendValueToMessage(logMessage, "requestURI", request.getRequestURI());
            appendValueToMessage(logMessage, "method", request.getMethod());
            appendValueToMessage(logMessage, "protocol", request.getProtocol());
            appendValueToMessage(logMessage, "mimeType", request.getContentType());
            if (logClientUserData) {
                appendValueToMessage(logMessage, "remoteAddr", request.getRemoteAddr());
                appendValueToMessage(logMessage, "remoteHost", request.getRemoteHost());
                appendValueToMessage(logMessage, "remoteUser", request.getRemoteUser());
                appendValueToMessage(logMessage, "userPrincipal", "" + request.getUserPrincipal());
            } else {
                appendValueToMessage(logMessage, "remoteAddr", CLIENT_LOGGING_DISABLED);
                appendValueToMessage(logMessage, "remoteHost", CLIENT_LOGGING_DISABLED);
                appendValueToMessage(logMessage, "remoteUser", CLIENT_LOGGING_DISABLED);
                appendValueToMessage(logMessage, "userPrincipal", CLIENT_LOGGING_DISABLED);
            }
            appendValueToMessage(logMessage, "encoding", request.getCharacterEncoding());
            appendValueToMessage(logMessage, "contentLength", "" + request.getContentLength());
            appendValueToMessage(logMessage, "scheme", request.getScheme());
            appendValueToMessage(logMessage, "secure", "" + request.isSecure());
            appendValueToMessage(logMessage, "serverName", request.getServerName());
            appendValueToMessage(logMessage, "serverPort", "" + request.getServerPort());
            appendValueToMessage(logMessage, "authType", request.getAuthType());
            appendValueToMessage(logMessage, "contextPath", request.getContextPath());
            appendValueToMessage(logMessage, "pathInfo", request.getPathInfo());
            appendValueToMessage(logMessage, "pathTranslated", request.getPathTranslated());
            appendValueToMessage(logMessage, "locale", "" + request.getLocale());
            final HttpSession session = request.getSession(false);
            appendValueToMessage(logMessage, "hasSession", "" + (session != null));
            if (session != null) {
                appendValueToMessage(logMessage, "sessionNew", "" + session.isNew());
                appendValueToMessage(logMessage, "sessionMaxInactiveInterval", "" + session.getMaxInactiveInterval());
                appendValueToMessage(logMessage, "sessionCreationTime", formatDateTime(session.getCreationTime()));
                appendValueToMessage(logMessage, "sessionLastAccessedTime", formatDateTime(session.getLastAccessedTime()));
                if (logSessionValues) {
                    if (DEBUG) {
                        for (final Enumeration names = ServerUtils.getAttributeNamesIncludingInternal(session); names.hasMoreElements(); ) {
                            final String name = (String) names.nextElement();
                            final Object value = ServerUtils.getAttributeIncludingInternal(session, name);
                            appendValueToMessage(logMessage, "session (sensitive data removed): " + name, removeSensitiveData(name, value, sensitiveRequestParamNameMatcherToReuse, sensitiveRequestParamNameAndValueUrlMatcherToReuse, sensitiveValueMatcherToReuse));
                        }
                    } else {
                        for (final Enumeration names = session.getAttributeNames(); names.hasMoreElements(); ) {
                            final String name = (String) names.nextElement();
                            final Object value = session.getAttribute(name);
                            appendValueToMessage(logMessage, "session (sensitive data removed): " + name, removeSensitiveData(name, value, sensitiveRequestParamNameMatcherToReuse, sensitiveRequestParamNameAndValueUrlMatcherToReuse, sensitiveValueMatcherToReuse));
                        }
                    }
                }
            }
            if (!isOldJavaEE13) {
                try {
                    appendValueToMessage(logMessage, "remotePort", "" + request.getRemotePort());
                    appendValueToMessage(logMessage, "localPort", "" + request.getLocalPort());
                    appendValueToMessage(logMessage, "localAddr", request.getLocalAddr());
                    appendValueToMessage(logMessage, "localName", request.getLocalName());
                } catch (NoSuchMethodError e) {
                    isOldJavaEE13 = true;
                }
            }
            {
                final Enumeration names = request.getParameterNames();
                if (names != null) {
                    while (names.hasMoreElements()) {
                        final String name = (String) names.nextElement();
                        final String[] values = request.getParameterValues(name);
                        if (values != null) {
                            for (int i = 0; i < values.length; i++) {
                                final String requestParam = removeSensitiveData(name, values[i], sensitiveRequestParamNameMatcherToReuse, sensitiveRequestParamNameAndValueUrlMatcherToReuse, sensitiveValueMatcherToReuse);
                                appendValueToMessage(logMessage, "requestParam (sensitive data removed): " + name, requestParam);
                            }
                        }
                    }
                } else {
                    System.err.println("This servlet-container does not allow the access of request params... VERY STRANGE");
                }
            }
            {
                final Enumeration names = request.getHeaderNames();
                if (names != null) {
                    while (names.hasMoreElements()) {
                        final String name = (String) names.nextElement();
                        if (!logClientUserData) {
                            final String nameLowercased = name.toLowerCase();
                            if (nameLowercased.indexOf("forward") > -1 || nameLowercased.indexOf("proxy") > -1 || nameLowercased.indexOf("client") > -1 || nameLowercased.indexOf("user") > -1) {
                                appendValueToMessage(logMessage, "header: " + name, CLIENT_LOGGING_DISABLED);
                                continue;
                            }
                        }
                        for (final Enumeration values = request.getHeaders(name); values.hasMoreElements(); ) {
                            final String value = (String) values.nextElement();
                            appendValueToMessage(logMessage, "header: " + name, value);
                        }
                    }
                } else {
                    System.err.println("This servlet-container does not allow the access of HTTP headers (unfortunately)");
                }
            }
            {
                final Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (int i = 0; i < cookies.length; i++) {
                        final Cookie cookie = cookies[i];
                        if (cookie != null) {
                            final String name = cookie.getName();
                            final String value = cookie.getValue();
                            appendValueToMessage(logMessage, "cookie: " + name, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            appendValueToMessage(logMessage, "Unable to create security log message (unexpected exception during message creation)", e.getMessage());
        }
        return logMessage.toString();
    }

    public static String printParameterMap(final Map parameterMap) {
        if (parameterMap == null) return null;
        final StringBuilder result = new StringBuilder();
        for (final Iterator entries = parameterMap.entrySet().iterator(); entries.hasNext(); ) {
            final Map.Entry entry = (Map.Entry) entries.next();
            result.append(entry.getKey()).append("-->").append(Arrays.asList((Object[]) entry.getValue())).append("   ");
        }
        return result.toString();
    }

    public static final void changeKeysToUpperCaseAndUnifyValues(final Map map) {
        if (map == null) return;
        final Map copy = new HashMap(map.size());
        for (final Iterator iter = map.keySet().iterator(); iter.hasNext(); ) {
            final String key = (String) iter.next();
            final String[] value = (String[]) map.get(key);
            final String keyUpper = key.toUpperCase();
            if (copy.containsKey(keyUpper)) {
                final String[] alreadyContainedValue = (String[]) copy.get(keyUpper);
                copy.put(keyUpper, combineArrays(alreadyContainedValue, value));
            } else copy.put(keyUpper, value);
        }
        map.clear();
        map.putAll(copy);
    }

    public static final String[] combineArrays(final String[] leftPart, final String[] rightPart) {
        final String[] result = new String[leftPart.length + rightPart.length];
        if (leftPart.length > 0) System.arraycopy(leftPart, 0, result, 0, leftPart.length);
        if (rightPart.length > 0) System.arraycopy(rightPart, 0, result, leftPart.length, rightPart.length);
        return result;
    }

    public static Map createHeaderMap(final HttpServletRequest request) {
        final Map headerMap = new HashMap();
        final Enumeration names = request.getHeaderNames();
        if (names == null) {
            System.err.println("This servlet-container does not allow the access of HTTP headers (unfortunately)");
            return headerMap;
        }
        while (names.hasMoreElements()) {
            final String name = (String) names.nextElement();
            if (!headerMap.containsKey(name)) {
                final List collectedValues = new ArrayList();
                for (final Enumeration values = request.getHeaders(name); values.hasMoreElements(); ) {
                    final String value = (String) values.nextElement();
                    collectedValues.add(value);
                }
                final String[] collectedValuesAsArray = (String[]) collectedValues.toArray(new String[0]);
                headerMap.put(name, collectedValuesAsArray);
            }
        }
        changeKeysToUpperCaseAndUnifyValues(headerMap);
        return headerMap;
    }

    public static Map createCookieMap(final HttpServletRequest request) {
        final Map cookieMap = new HashMap();
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                final Cookie cookie = cookies[i];
                if (cookie != null) {
                    final String name = cookie.getName();
                    List valueList = (List) cookieMap.get(name);
                    if (valueList == null) {
                        valueList = new ArrayList();
                        cookieMap.put(name, valueList);
                    }
                    final String value = cookie.getValue();
                    valueList.add(value);
                }
            }
        }
        final Map result = convertMapOfListOfStrings2MapOfStringArray(cookieMap);
        changeKeysToUpperCaseAndUnifyValues(result);
        return result;
    }

    public static String removeParameter(String queryString, final String parameterKey) {
        final int index;
        if (queryString != null && queryString.length() > 0 && parameterKey != null && (index = queryString.indexOf(parameterKey)) != -1) {
            final boolean isAmpersandBefore = index > 0 && queryString.charAt(index - 1) == '&';
            final boolean isMaskedAmpersandBefore = !isAmpersandBefore && index > 4 && queryString.charAt(index - 5) == '&' && queryString.charAt(index - 4) == 'a' && queryString.charAt(index - 3) == 'm' && queryString.charAt(index - 2) == 'p' && queryString.charAt(index - 1) == ';';
            final int start = isAmpersandBefore ? index - 1 : isMaskedAmpersandBefore ? index - 5 : index;
            int end = queryString.length() - 1;
            char c;
            for (int i = index + parameterKey.length(); i < queryString.length(); i++) {
                c = queryString.charAt(i);
                if (c == '&' || c == '#') {
                    end = i - 1;
                    break;
                }
            }
            final boolean isMaskedAmpersandAfter = queryString.length() >= end + 6 && queryString.charAt(end + 1) == '&' && queryString.charAt(end + 2) == 'a' && queryString.charAt(end + 3) == 'm' && queryString.charAt(end + 4) == 'p' && queryString.charAt(end + 5) == ';';
            final boolean isAmpersandAfter = !isMaskedAmpersandAfter && queryString.length() >= end + 2 && queryString.charAt(end + 1) == '&';
            if (!isAmpersandBefore && !isMaskedAmpersandBefore) {
                end = isAmpersandAfter ? end + 1 : isMaskedAmpersandAfter ? end + 5 : end;
            }
            end++;
            if (DEBUG) {
                System.out.println("=================");
                System.out.println("queryString: " + queryString);
                System.out.println("isAmpersandBefore: " + isAmpersandBefore);
                System.out.println("isMaskedAmpersandBefore: " + isMaskedAmpersandBefore);
                System.out.println("isAmpersandAfter: " + isAmpersandAfter);
                System.out.println("isMaskedAmpersandAfter: " + isMaskedAmpersandAfter);
                System.out.println("Extracting: " + queryString.substring(start, end));
            }
            queryString = queryString.substring(0, start) + queryString.substring(end);
            if (DEBUG) {
                System.out.println("Result: " + queryString);
            }
        }
        return queryString;
    }

    public static String createOrRetrieveRandomTokenFromSession(final HttpSession session, final String sessionKey) {
        return createOrRetrieveRandomTokenFromSession(session, sessionKey, -1, -1);
    }

    public static String createOrRetrieveRandomTokenFromSession(final HttpSession session, final String sessionKey, final int minimumLength, final int maximumLength) {
        String value = (String) ServerUtils.getAttributeIncludingInternal(session, sessionKey);
        if (value == null) {
            if (maximumLength > minimumLength && minimumLength > 0) value = CryptoUtils.generateRandomToken(true, CryptoUtils.generateRandomNumber(true, minimumLength, maximumLength)); else value = CryptoUtils.generateRandomToken(true);
            session.setAttribute(sessionKey, value);
        }
        assert value != null;
        return value;
    }

    public static String retrieveRandomTokenFromSessionIfExisting(final HttpServletRequest request, final String sessionKey) {
        final HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (String) ServerUtils.getAttributeIncludingInternal(session, sessionKey);
    }

    public static CryptoKeyAndSalt createOrRetrieveRandomCryptoKeyFromSession(final HttpSession session, final String sessionKey, final boolean extraEncryptedValueHashProtection) throws NoSuchAlgorithmException {
        CryptoKeyAndSalt value = (CryptoKeyAndSalt) ServerUtils.getAttributeIncludingInternal(session, sessionKey);
        if (value == null) {
            value = CryptoUtils.generateRandomCryptoKeyAndSalt(extraEncryptedValueHashProtection);
            session.setAttribute(sessionKey, value);
        }
        assert value != null;
        return value;
    }

    public static final class DecryptedQuerystring {

        String decryptedString = null;

        Boolean isFormSubmit = null;

        Boolean resourceEndsWithSlash = null;

        Boolean isFormMultipart = null;

        Boolean wasManipulated = Boolean.FALSE;

        public String toString() {
            return this.decryptedString;
        }
    }

    /**
     * returns null when the URL is not encrypted
     */
    public static DecryptedQuerystring decryptQueryStringInServletPathWithQueryString(final String contextPath, final String servletPath, String servletPathWithQueryStringEncrypted, final String cryptoDetectionString, final CryptoKeyAndSalt key, final String uriRequested, final boolean isRequestHavingAdditionalParameters, final boolean isRequestMethodPOST, final boolean useFullPathForResourceToBeAccessedProtection, final boolean additionalFullOrMediumResourceRemoval, final boolean appendQuestionmarkOrAmpersandToLinks) {
        if (servletPath == null || servletPathWithQueryStringEncrypted == null) return null;
        if (contextPath == null) throw new NullPointerException("contextPath must not be null");
        if (cryptoDetectionString == null) throw new NullPointerException("cryptoDetectionString must not be null");
        if (key == null) throw new NullPointerException("key must not be null");
        if (uriRequested == null) throw new NullPointerException("uriRequested must not be null");
        final int firstQuestionMark = servletPathWithQueryStringEncrypted.indexOf('?');
        if (firstQuestionMark > -1 && servletPathWithQueryStringEncrypted.length() > firstQuestionMark + 1) {
            try {
                if (appendQuestionmarkOrAmpersandToLinks && servletPathWithQueryStringEncrypted.endsWith("&")) servletPathWithQueryStringEncrypted = servletPathWithQueryStringEncrypted.substring(0, servletPathWithQueryStringEncrypted.length() - 1);
                final int pos = servletPathWithQueryStringEncrypted.indexOf(cryptoDetectionString);
                if (pos == -1) return null;
                servletPathWithQueryStringEncrypted = servletPathWithQueryStringEncrypted.substring(0, pos) + servletPathWithQueryStringEncrypted.substring(pos + cryptoDetectionString.length());
                final int firstQuestionMarkOrAmpersandLeftFromCryptoDetectionString = Math.max(firstQuestionMark, servletPathWithQueryStringEncrypted.lastIndexOf('&', pos));
                final String alreadyUnencryptedPrefix = servletPathWithQueryStringEncrypted.substring(0, firstQuestionMarkOrAmpersandLeftFromCryptoDetectionString + 1);
                final int firstAmpersandOrEqualsRightFromCryptoDetectionString;
                final int tmpAmpersandRightFromCryptoDetectionString = servletPathWithQueryStringEncrypted.indexOf('&', pos);
                final int tmpEqualsRightFromCryptoDetectionString = servletPathWithQueryStringEncrypted.indexOf('=', pos);
                if (tmpAmpersandRightFromCryptoDetectionString == -1) firstAmpersandOrEqualsRightFromCryptoDetectionString = tmpEqualsRightFromCryptoDetectionString; else if (tmpEqualsRightFromCryptoDetectionString == -1) firstAmpersandOrEqualsRightFromCryptoDetectionString = tmpAmpersandRightFromCryptoDetectionString; else firstAmpersandOrEqualsRightFromCryptoDetectionString = Math.min(tmpEqualsRightFromCryptoDetectionString, tmpAmpersandRightFromCryptoDetectionString);
                final String alreadyUnencryptedSuffix = firstAmpersandOrEqualsRightFromCryptoDetectionString > -1 ? servletPathWithQueryStringEncrypted.substring(firstAmpersandOrEqualsRightFromCryptoDetectionString) : "";
                String decrypt = servletPathWithQueryStringEncrypted.substring(firstQuestionMarkOrAmpersandLeftFromCryptoDetectionString + 1, firstAmpersandOrEqualsRightFromCryptoDetectionString > -1 ? firstAmpersandOrEqualsRightFromCryptoDetectionString : servletPathWithQueryStringEncrypted.length());
                String anchor = null;
                final int anchorPos = decrypt.indexOf('#');
                if (anchorPos > -1) {
                    anchor = decrypt.substring(anchorPos);
                    decrypt = decrypt.substring(0, anchorPos);
                }
                if (decrypt.length() > 1 && decrypt.charAt(decrypt.length() - 1) == '=') decrypt = decrypt.substring(0, decrypt.length() - 1);
                final String alreadyUnencryptedPrefixWithParamsRemoved = removeAfterFirstQuestionMark(alreadyUnencryptedPrefix);
                final String alreadyUnencryptedSuffixWithParamsRemoved = removeBeforeFirstHash(alreadyUnencryptedSuffix);
                final String resourceToBeAccessed = ServerUtils.extractResourceToBeAccessed(uriRequested, contextPath, servletPath, useFullPathForResourceToBeAccessedProtection);
                StringBuilder result = new StringBuilder(servletPathWithQueryStringEncrypted.length());
                String decryptedQueryString = CryptoUtils.decryptURLSafe(decrypt, key);
                if (DEBUG) System.out.println("decryptedQueryString: " + decryptedQueryString);
                int delimiterPos = decryptedQueryString.lastIndexOf(WebCastellumFilter.INTERNAL_URL_DELIMITER);
                if (delimiterPos == -1 || delimiterPos == decryptedQueryString.length() - 1) throw new ServerAttackException("Decrypted URL contains no matching flags");
                final DecryptedQuerystring decryptedQuerystring = new DecryptedQuerystring();
                final char resourceEndsWithSlashFlag = decryptedQueryString.charAt(delimiterPos + 1);
                final boolean resourceEndsWithSlash;
                if (WebCastellumFilter.INTERNAL_RESOURCE_ENDS_WITH_SLASH_YES_FLAG == resourceEndsWithSlashFlag) resourceEndsWithSlash = true; else if (WebCastellumFilter.INTERNAL_RESOURCE_ENDS_WITH_SLASH_NO_FLAG == resourceEndsWithSlashFlag) resourceEndsWithSlash = false; else throw new ServerAttackException("Decrypted URL contains unknown 'resource ends with slash' value: " + resourceEndsWithSlashFlag);
                decryptedQuerystring.resourceEndsWithSlash = Boolean.valueOf(resourceEndsWithSlash);
                delimiterPos = decryptedQueryString.lastIndexOf(WebCastellumFilter.INTERNAL_URL_DELIMITER, delimiterPos - 1);
                final char formMultipartFlag = decryptedQueryString.charAt(delimiterPos + 1);
                final boolean isFormMultipart;
                if (WebCastellumFilter.INTERNAL_MULTIPART_YES_FLAG == formMultipartFlag) isFormMultipart = true; else if (WebCastellumFilter.INTERNAL_MULTIPART_NO_FLAG == formMultipartFlag) isFormMultipart = false; else throw new ServerAttackException("Decrypted URL contains unknown 'form multipart' value: " + formMultipartFlag);
                decryptedQuerystring.isFormMultipart = Boolean.valueOf(isFormMultipart);
                delimiterPos = decryptedQueryString.lastIndexOf(WebCastellumFilter.INTERNAL_URL_DELIMITER, delimiterPos - 1);
                final char formLinkFlag = decryptedQueryString.charAt(delimiterPos + 1);
                final boolean isFormAction;
                if (WebCastellumFilter.INTERNAL_TYPE_FORM_FLAG == formLinkFlag) isFormAction = true; else if (WebCastellumFilter.INTERNAL_TYPE_LINK_FLAG == formLinkFlag) isFormAction = false; else throw new ServerAttackException("Decrypted URL contains unknown 'form/link' value: " + formLinkFlag);
                decryptedQuerystring.isFormSubmit = Boolean.valueOf(isFormAction);
                if (isRequestHavingAdditionalParameters && !isFormAction) decryptedQuerystring.wasManipulated = Boolean.TRUE;
                delimiterPos = decryptedQueryString.lastIndexOf(WebCastellumFilter.INTERNAL_URL_DELIMITER, delimiterPos - 1);
                if (delimiterPos == -1 || delimiterPos == decryptedQueryString.length() - 1) throw new ServerAttackException("Decrypted URL contains no 'resource-to-be-accessed' and/or 'request-type-get/post' and/or 'form/link' value");
                final char flag = decryptedQueryString.charAt(delimiterPos + 1);
                if (flag != WebCastellumFilter.INTERNAL_METHOD_TYPE_UNDEFINED) {
                    if (isRequestMethodPOST) {
                        if (flag != WebCastellumFilter.INTERNAL_METHOD_TYPE_POST) throw new ServerAttackException("Decrypted URL contains mismatching 'request-type-get/post' value (expected POST)");
                    } else {
                        if (flag != WebCastellumFilter.INTERNAL_METHOD_TYPE_GET) throw new ServerAttackException("Decrypted URL contains mismatching 'request-type-get/post' value (expected GET)");
                    }
                }
                final int previousDelimiter = delimiterPos;
                delimiterPos = decryptedQueryString.lastIndexOf(WebCastellumFilter.INTERNAL_URL_DELIMITER, delimiterPos - 1);
                if (delimiterPos == -1 || delimiterPos == decryptedQueryString.length() - 1) throw new ServerAttackException("Decrypted URL contains no 'resource-to-be-accessed' and/or 'request-type-get/post' and/or 'form/link' value");
                final String actualResourceToBeAccessed = decryptedQueryString.substring(delimiterPos + 1, previousDelimiter);
                if (!additionalFullOrMediumResourceRemoval) {
                    boolean mismatch = !actualResourceToBeAccessed.equals(resourceToBeAccessed);
                    if (mismatch) {
                        if (resourceToBeAccessed.length() == 0) {
                            if (uriRequested.endsWith("/" + actualResourceToBeAccessed + "/") || uriRequested.startsWith(actualResourceToBeAccessed + "/")) mismatch = false;
                            if (uriRequested.endsWith("/") && servletPath.endsWith("/" + actualResourceToBeAccessed)) mismatch = false;
                        } else if (resourceToBeAccessed.equals(actualResourceToBeAccessed + "/")) {
                            mismatch = false;
                        }
                    }
                    if (mismatch) {
                        throw new ServerAttackException("Decrypted URL contains mismatching 'resource-to-be-accessed' value (expected=" + resourceToBeAccessed + " vs. actual=" + actualResourceToBeAccessed + ")");
                    }
                }
                decryptedQueryString = decryptedQueryString.substring(0, delimiterPos);
                if (additionalFullOrMediumResourceRemoval) {
                    if (actualResourceToBeAccessed.startsWith(contextPath)) {
                        result.append(actualResourceToBeAccessed.substring(contextPath.length()));
                    } else result.append(actualResourceToBeAccessed);
                    if (decryptedQuerystring.resourceEndsWithSlash != null && decryptedQuerystring.resourceEndsWithSlash.booleanValue()) {
                        if (DEBUG) {
                            System.out.println("Intermediate result: " + result);
                            System.out.println("alreadyUnencryptedPrefixWithParamsRemoved: " + alreadyUnencryptedPrefixWithParamsRemoved);
                        }
                        if (alreadyUnencryptedPrefixWithParamsRemoved.charAt(alreadyUnencryptedPrefixWithParamsRemoved.length() - 1) == '?') {
                            result.append(alreadyUnencryptedPrefixWithParamsRemoved.substring(0, alreadyUnencryptedPrefixWithParamsRemoved.length() - 1));
                        }
                    }
                    result.append("?");
                } else {
                    result.append(alreadyUnencryptedPrefixWithParamsRemoved);
                    if (DEBUG) System.out.println("alreadyUnencryptedPrefixWithParamsRemoved: " + alreadyUnencryptedPrefixWithParamsRemoved);
                }
                if (DEBUG) System.out.println("Decrypted yet without querystring: " + result);
                result.append(decryptedQueryString);
                result.append(alreadyUnencryptedSuffixWithParamsRemoved);
                if (anchor != null) result.append(anchor);
                if (result.indexOf(cryptoDetectionString) > -1) throw new ServerAttackException("URL is encrypted multiple times (possible denial of service attack with endless decryption loop): " + result);
                if (appendQuestionmarkOrAmpersandToLinks) {
                    if (result.charAt(result.length() - 1) == '&' && result.length() > 1) {
                        result.deleteCharAt(result.length() - 1);
                    }
                    result = new StringBuilder(result.toString().replaceAll("\\&\\&", "&"));
                }
                if (DEBUG) System.out.println("result=" + result);
                decryptedQuerystring.decryptedString = result.toString();
                return decryptedQuerystring;
            } catch (IllegalBlockSizeException e) {
                if (DEBUG) e.printStackTrace();
                throw new ServerAttackException("Unable to decrypt URL: " + e.getMessage());
            } catch (InvalidKeyException e) {
                if (DEBUG) e.printStackTrace();
                throw new ServerAttackException("Unable to decrypt URL: " + e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                if (DEBUG) e.printStackTrace();
                throw new ServerAttackException("Unable to decrypt URL: " + e.getMessage());
            } catch (BadPaddingException e) {
                if (DEBUG) e.printStackTrace();
                throw new ServerAttackException("Unable to decrypt URL: " + e.getMessage());
            } catch (NoSuchPaddingException e) {
                if (DEBUG) e.printStackTrace();
                throw new ServerAttackException("Unable to decrypt URL: " + e.getMessage());
            } catch (UnsupportedEncodingException e) {
                if (DEBUG) e.printStackTrace();
                throw new ServerAttackException("Unable to decrypt URL: " + e.getMessage());
            } catch (ServerAttackException e) {
                if (DEBUG) e.printStackTrace();
                throw e;
            } catch (RuntimeException e) {
                if (DEBUG) e.printStackTrace();
                throw new ServerAttackException("Unable to decrypt URL: " + e.getMessage());
            }
        } else {
            return null;
        }
    }

    public static boolean isMismatch(final List expectedValues, final String[] actualSubmittedValues) {
        if (expectedValues == null) throw new NullPointerException("expectedValues must not be null");
        if (actualSubmittedValues == null || actualSubmittedValues.length == 0) return true;
        if (expectedValues.size() != actualSubmittedValues.length) return true;
        for (int i = 0; i < actualSubmittedValues.length; i++) {
            final String actualSubmittedValue = actualSubmittedValues[i];
            final boolean wasThere = expectedValues.remove(actualSubmittedValue);
            if (!wasThere) return true;
        }
        return !expectedValues.isEmpty();
    }

    private static String removeAfterFirstQuestionMark(final String value) {
        if (value == null) return null;
        final int pos = value.indexOf('?');
        if (pos > -1) return value.substring(0, pos + 1);
        return value;
    }

    private static String removeBeforeFirstHash(final String value) {
        if (value == null) return null;
        final int pos = value.indexOf('#');
        if (pos > -1) return value.substring(pos);
        return "";
    }

    private static Map convertMapOfListOfStrings2MapOfStringArray(final Map map) {
        if (map == null) return null;
        final Map result = new HashMap();
        for (final Iterator entries = map.entrySet().iterator(); entries.hasNext(); ) {
            final Map.Entry entry = (Map.Entry) entries.next();
            result.put(entry.getKey(), ((List) entry.getValue()).toArray(new String[0]));
        }
        return result;
    }

    private static String formatDateTime(long value) {
        return new SimpleDateFormat(RequestDefinitionContainer.FORMAT_TIME).format(new Date(value));
    }

    public static void appendValueToMessage(final StringBuilder logMessage, final String key, final Object value) {
        logMessage.append("\t").append(key).append(" = ").append(value == null ? "" : value).append("\n");
    }

    private static String removeSensitiveData(final String name, final Object obj, final Matcher sensitiveRequestParamNameMatcherToReuse, final Matcher sensitiveRequestParamNameAndValueUrlMatcherToReuse, final Matcher sensitiveValueMatcherToReuse) {
        if (obj == null) return null;
        String value = obj.toString();
        if (name != null) {
            sensitiveRequestParamNameMatcherToReuse.reset(name);
            if (sensitiveRequestParamNameMatcherToReuse.find()) return SENSITIVE_VALUE_REMOVED;
        }
        sensitiveValueMatcherToReuse.reset(value);
        value = sensitiveValueMatcherToReuse.replaceAll(SENSITIVE_VALUE_REMOVED);
        sensitiveRequestParamNameAndValueUrlMatcherToReuse.reset(value);
        value = sensitiveRequestParamNameAndValueUrlMatcherToReuse.replaceAll(SENSITIVE_VALUE_REMOVED);
        return value;
    }

    /**
	 * Filters the given requestParamterMap for Image Map indicators. These are _.x _.y as coordinates for the map. 
	 * This params would fire an attack, cause of not expected values in request.
	 * 
	 * @param requestParamaterMap
	 * @return cleaned Set of RequestParams
	 */
    public static Set filterRequestParameterMap(final Set requestParamaterMap) {
        Set cleanedParamSet = new HashSet();
        Iterator iter = requestParamaterMap.iterator();
        while (iter.hasNext()) {
            Object param = iter.next();
            if (param instanceof String) {
                String newParam = (String) param;
                if (newParam.matches(IMAGE_MAP_EXLUDE_EXPRESSION)) {
                    param = newParam.substring(0, newParam.length() - 2);
                }
            }
            cleanedParamSet.add(param);
        }
        return cleanedParamSet;
    }
}
