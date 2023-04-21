package com.chilkatsoft;

public class CkSpider {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected CkSpider(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(CkSpider obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            chilkatJNI.delete_CkSpider(swigCPtr);
        }
        swigCPtr = 0;
    }

    public CkSpider() {
        this(chilkatJNI.new_CkSpider(), true);
    }

    public String getAvoidPattern(int index) {
        return chilkatJNI.CkSpider_getAvoidPattern(swigCPtr, this, index);
    }

    public String getOutboundLink(int index) {
        return chilkatJNI.CkSpider_getOutboundLink(swigCPtr, this, index);
    }

    public String getFailedUrl(int index) {
        return chilkatJNI.CkSpider_getFailedUrl(swigCPtr, this, index);
    }

    public String getSpideredUrl(int index) {
        return chilkatJNI.CkSpider_getSpideredUrl(swigCPtr, this, index);
    }

    public String getUnspideredUrl(int index) {
        return chilkatJNI.CkSpider_getUnspideredUrl(swigCPtr, this, index);
    }

    public void get_ProxyDomain(CkString str) {
        chilkatJNI.CkSpider_get_ProxyDomain(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public String proxyDomain() {
        return chilkatJNI.CkSpider_proxyDomain(swigCPtr, this);
    }

    public void put_ProxyDomain(String newVal) {
        chilkatJNI.CkSpider_put_ProxyDomain(swigCPtr, this, newVal);
    }

    public void get_ProxyLogin(CkString str) {
        chilkatJNI.CkSpider_get_ProxyLogin(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public String proxyLogin() {
        return chilkatJNI.CkSpider_proxyLogin(swigCPtr, this);
    }

    public void put_ProxyLogin(String newVal) {
        chilkatJNI.CkSpider_put_ProxyLogin(swigCPtr, this, newVal);
    }

    public void get_ProxyPassword(CkString str) {
        chilkatJNI.CkSpider_get_ProxyPassword(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public String proxyPassword() {
        return chilkatJNI.CkSpider_proxyPassword(swigCPtr, this);
    }

    public void put_ProxyPassword(String newVal) {
        chilkatJNI.CkSpider_put_ProxyPassword(swigCPtr, this, newVal);
    }

    public int get_ProxyPort() {
        return chilkatJNI.CkSpider_get_ProxyPort(swigCPtr, this);
    }

    public void put_ProxyPort(int newVal) {
        chilkatJNI.CkSpider_put_ProxyPort(swigCPtr, this, newVal);
    }

    public boolean get_VerboseLogging() {
        return chilkatJNI.CkSpider_get_VerboseLogging(swigCPtr, this);
    }

    public void put_VerboseLogging(boolean newVal) {
        chilkatJNI.CkSpider_put_VerboseLogging(swigCPtr, this, newVal);
    }

    public void put_UserAgent(String ua) {
        chilkatJNI.CkSpider_put_UserAgent(swigCPtr, this, ua);
    }

    public String userAgent() {
        return chilkatJNI.CkSpider_userAgent(swigCPtr, this);
    }

    public void get_UserAgent(CkString strOut) {
        chilkatJNI.CkSpider_get_UserAgent(swigCPtr, this, CkString.getCPtr(strOut), strOut);
    }

    public String cacheDir() {
        return chilkatJNI.CkSpider_cacheDir(swigCPtr, this);
    }

    public String avoidPattern(int index) {
        return chilkatJNI.CkSpider_avoidPattern(swigCPtr, this, index);
    }

    public String outboundLink(int index) {
        return chilkatJNI.CkSpider_outboundLink(swigCPtr, this, index);
    }

    public String failedUrl(int index) {
        return chilkatJNI.CkSpider_failedUrl(swigCPtr, this, index);
    }

    public String spideredUrl(int index) {
        return chilkatJNI.CkSpider_spideredUrl(swigCPtr, this, index);
    }

    public String unspideredUrl(int index) {
        return chilkatJNI.CkSpider_unspideredUrl(swigCPtr, this, index);
    }

    public String domain() {
        return chilkatJNI.CkSpider_domain(swigCPtr, this);
    }

    public String lastHtmlDescription() {
        return chilkatJNI.CkSpider_lastHtmlDescription(swigCPtr, this);
    }

    public String lastHtmlKeywords() {
        return chilkatJNI.CkSpider_lastHtmlKeywords(swigCPtr, this);
    }

    public String lastHtmlTitle() {
        return chilkatJNI.CkSpider_lastHtmlTitle(swigCPtr, this);
    }

    public String lastHtml() {
        return chilkatJNI.CkSpider_lastHtml(swigCPtr, this);
    }

    public String lastUrl() {
        return chilkatJNI.CkSpider_lastUrl(swigCPtr, this);
    }

    public String lastModDateStr() {
        return chilkatJNI.CkSpider_lastModDateStr(swigCPtr, this);
    }

    public String fetchRobotsText() {
        return chilkatJNI.CkSpider_fetchRobotsText(swigCPtr, this);
    }

    public void GetDomain(String url, CkString domainOut) {
        chilkatJNI.CkSpider_GetDomain(swigCPtr, this, url, CkString.getCPtr(domainOut), domainOut);
    }

    public void GetBaseDomain(String domain, CkString domainOut) {
        chilkatJNI.CkSpider_GetBaseDomain(swigCPtr, this, domain, CkString.getCPtr(domainOut), domainOut);
    }

    public void CanonicalizeUrl(String url, CkString urlOut) {
        chilkatJNI.CkSpider_CanonicalizeUrl(swigCPtr, this, url, CkString.getCPtr(urlOut), urlOut);
    }

    public String getDomain(String url) {
        return chilkatJNI.CkSpider_getDomain(swigCPtr, this, url);
    }

    public String getBaseDomain(String domain) {
        return chilkatJNI.CkSpider_getBaseDomain(swigCPtr, this, domain);
    }

    public String canonicalizeUrl(String url) {
        return chilkatJNI.CkSpider_canonicalizeUrl(swigCPtr, this, url);
    }

    public String lastErrorText() {
        return chilkatJNI.CkSpider_lastErrorText(swigCPtr, this);
    }

    public String lastErrorXml() {
        return chilkatJNI.CkSpider_lastErrorXml(swigCPtr, this);
    }

    public String lastErrorHtml() {
        return chilkatJNI.CkSpider_lastErrorHtml(swigCPtr, this);
    }

    public void get_LastHtmlDescription(CkString strOut) {
        chilkatJNI.CkSpider_get_LastHtmlDescription(swigCPtr, this, CkString.getCPtr(strOut), strOut);
    }

    public void get_LastHtmlKeywords(CkString strOut) {
        chilkatJNI.CkSpider_get_LastHtmlKeywords(swigCPtr, this, CkString.getCPtr(strOut), strOut);
    }

    public void get_LastHtmlTitle(CkString strOut) {
        chilkatJNI.CkSpider_get_LastHtmlTitle(swigCPtr, this, CkString.getCPtr(strOut), strOut);
    }

    public void get_LastHtml(CkString strOut) {
        chilkatJNI.CkSpider_get_LastHtml(swigCPtr, this, CkString.getCPtr(strOut), strOut);
    }

    public boolean get_LastFromCache() {
        return chilkatJNI.CkSpider_get_LastFromCache(swigCPtr, this);
    }

    public void get_LastModDate(SYSTEMTIME sysTime) {
        chilkatJNI.CkSpider_get_LastModDate(swigCPtr, this, SYSTEMTIME.getCPtr(sysTime), sysTime);
    }

    public void get_LastUrl(CkString strOut) {
        chilkatJNI.CkSpider_get_LastUrl(swigCPtr, this, CkString.getCPtr(strOut), strOut);
    }

    public void get_LastModDateStr(CkString strOut) {
        chilkatJNI.CkSpider_get_LastModDateStr(swigCPtr, this, CkString.getCPtr(strOut), strOut);
    }

    public void SleepMs(int millisec) {
        chilkatJNI.CkSpider_SleepMs(swigCPtr, this, millisec);
    }

    public void SkipUnspidered(int index) {
        chilkatJNI.CkSpider_SkipUnspidered(swigCPtr, this, index);
    }

    public boolean FetchRobotsText(CkString strOut) {
        return chilkatJNI.CkSpider_FetchRobotsText(swigCPtr, this, CkString.getCPtr(strOut), strOut);
    }

    public void get_Domain(CkString strOut) {
        chilkatJNI.CkSpider_get_Domain(swigCPtr, this, CkString.getCPtr(strOut), strOut);
    }

    public void AddMustMatchPattern(String pattern) {
        chilkatJNI.CkSpider_AddMustMatchPattern(swigCPtr, this, pattern);
    }

    public void AddAvoidOutboundLinkPattern(String pattern) {
        chilkatJNI.CkSpider_AddAvoidOutboundLinkPattern(swigCPtr, this, pattern);
    }

    public boolean GetAvoidPattern(int index, CkString strOut) {
        return chilkatJNI.CkSpider_GetAvoidPattern(swigCPtr, this, index, CkString.getCPtr(strOut), strOut);
    }

    public void AddAvoidPattern(String pattern) {
        chilkatJNI.CkSpider_AddAvoidPattern(swigCPtr, this, pattern);
    }

    public boolean GetOutboundLink(int index, CkString strOut) {
        return chilkatJNI.CkSpider_GetOutboundLink(swigCPtr, this, index, CkString.getCPtr(strOut), strOut);
    }

    public boolean GetFailedUrl(int index, CkString strOut) {
        return chilkatJNI.CkSpider_GetFailedUrl(swigCPtr, this, index, CkString.getCPtr(strOut), strOut);
    }

    public boolean GetSpideredUrl(int index, CkString strOut) {
        return chilkatJNI.CkSpider_GetSpideredUrl(swigCPtr, this, index, CkString.getCPtr(strOut), strOut);
    }

    public boolean GetUnspideredUrl(int index, CkString strOut) {
        return chilkatJNI.CkSpider_GetUnspideredUrl(swigCPtr, this, index, CkString.getCPtr(strOut), strOut);
    }

    public boolean RecrawlLast() {
        return chilkatJNI.CkSpider_RecrawlLast(swigCPtr, this);
    }

    public void ClearOutboundLinks() {
        chilkatJNI.CkSpider_ClearOutboundLinks(swigCPtr, this);
    }

    public void ClearFailedUrls() {
        chilkatJNI.CkSpider_ClearFailedUrls(swigCPtr, this);
    }

    public void ClearSpideredUrls() {
        chilkatJNI.CkSpider_ClearSpideredUrls(swigCPtr, this);
    }

    public int get_WindDownCount() {
        return chilkatJNI.CkSpider_get_WindDownCount(swigCPtr, this);
    }

    public void put_WindDownCount(int newVal) {
        chilkatJNI.CkSpider_put_WindDownCount(swigCPtr, this, newVal);
    }

    public int get_NumAvoidPatterns() {
        return chilkatJNI.CkSpider_get_NumAvoidPatterns(swigCPtr, this);
    }

    public int get_NumOutboundLinks() {
        return chilkatJNI.CkSpider_get_NumOutboundLinks(swigCPtr, this);
    }

    public int get_NumFailed() {
        return chilkatJNI.CkSpider_get_NumFailed(swigCPtr, this);
    }

    public int get_NumSpidered() {
        return chilkatJNI.CkSpider_get_NumSpidered(swigCPtr, this);
    }

    public int get_NumUnspidered() {
        return chilkatJNI.CkSpider_get_NumUnspidered(swigCPtr, this);
    }

    public boolean CrawlNext() {
        return chilkatJNI.CkSpider_CrawlNext(swigCPtr, this);
    }

    public boolean get_ChopAtQuery() {
        return chilkatJNI.CkSpider_get_ChopAtQuery(swigCPtr, this);
    }

    public void put_ChopAtQuery(boolean newVal) {
        chilkatJNI.CkSpider_put_ChopAtQuery(swigCPtr, this, newVal);
    }

    public boolean get_AvoidHttps() {
        return chilkatJNI.CkSpider_get_AvoidHttps(swigCPtr, this);
    }

    public void put_AvoidHttps(boolean newVal) {
        chilkatJNI.CkSpider_put_AvoidHttps(swigCPtr, this, newVal);
    }

    public int get_MaxResponseSize() {
        return chilkatJNI.CkSpider_get_MaxResponseSize(swigCPtr, this);
    }

    public void put_MaxResponseSize(int newVal) {
        chilkatJNI.CkSpider_put_MaxResponseSize(swigCPtr, this, newVal);
    }

    public int get_MaxUrlLen() {
        return chilkatJNI.CkSpider_get_MaxUrlLen(swigCPtr, this);
    }

    public void put_MaxUrlLen(int newVal) {
        chilkatJNI.CkSpider_put_MaxUrlLen(swigCPtr, this, newVal);
    }

    public void get_CacheDir(CkString strOut) {
        chilkatJNI.CkSpider_get_CacheDir(swigCPtr, this, CkString.getCPtr(strOut), strOut);
    }

    public void put_CacheDir(String dir) {
        chilkatJNI.CkSpider_put_CacheDir(swigCPtr, this, dir);
    }

    public boolean get_UpdateCache() {
        return chilkatJNI.CkSpider_get_UpdateCache(swigCPtr, this);
    }

    public void put_UpdateCache(boolean newVal) {
        chilkatJNI.CkSpider_put_UpdateCache(swigCPtr, this, newVal);
    }

    public boolean get_FetchFromCache() {
        return chilkatJNI.CkSpider_get_FetchFromCache(swigCPtr, this);
    }

    public void put_FetchFromCache(boolean newVal) {
        chilkatJNI.CkSpider_put_FetchFromCache(swigCPtr, this, newVal);
    }

    public int get_ConnectTimeout() {
        return chilkatJNI.CkSpider_get_ConnectTimeout(swigCPtr, this);
    }

    public void put_ConnectTimeout(int newVal) {
        chilkatJNI.CkSpider_put_ConnectTimeout(swigCPtr, this, newVal);
    }

    public int get_ReadTimeout() {
        return chilkatJNI.CkSpider_get_ReadTimeout(swigCPtr, this);
    }

    public void put_ReadTimeout(int newVal) {
        chilkatJNI.CkSpider_put_ReadTimeout(swigCPtr, this, newVal);
    }

    public void AddUnspidered(String url) {
        chilkatJNI.CkSpider_AddUnspidered(swigCPtr, this, url);
    }

    public void Initialize(String domain) {
        chilkatJNI.CkSpider_Initialize(swigCPtr, this, domain);
    }
}
