package tpac.lib.DAPSpider;

import tpac.lib.DigitalLibrary.Dataset;
import java.net.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
* Dap HTML Parser
* Originally based on class by Jeff Heaton(http://www.jeffheaton.com)
* Article source: http://www.developer.com/java/other/article.php/1573761
* Modified by Ian C to process DAP urls
* This is the base DapParser.  This is subclassed into DapHtmlParser and IPCCParser.
**/
public class DapParser {

    protected URL base;

    protected DapSpider spider;

    protected ReportWriter report;

    protected boolean followQueries;

    protected boolean followExternalLinks;

    protected boolean followLowerBase;

    protected static long MAX_DEPTH_COUNT = 100;

    protected String filenameRegex;

    protected String baseFileRegex;

    protected String currentURL;

    protected String currentDir;

    protected URL currentURL_asURL;

    protected URL currentDir_asURL;

    /**
	* Sole constructor for this class.
	* @param _spider - spider used for storing crawler result.
	* @param _base - the base URL to parse and spider.
	* @param _report - a report writer.
	**/
    public DapParser(DapSpider _spider, URL _base, String _currentURL, ReportWriter _report) {
        int lastSlash;
        spider = _spider;
        base = _base;
        report = _report;
        followQueries = false;
        followExternalLinks = false;
        followLowerBase = false;
        currentURL = _currentURL;
        lastSlash = currentURL.lastIndexOf('/');
        currentDir = currentURL.substring(0, lastSlash);
        MsgLog.mumble("DapParser(): url is " + currentURL);
        MsgLog.mumble("DapParser(): path is " + currentDir);
        try {
            currentURL_asURL = new URL(currentURL);
        } catch (MalformedURLException e) {
            MsgLog.error("DapParser(): malformed URL: " + currentURL);
        }
        try {
            currentDir_asURL = new URL(currentDir + "/");
        } catch (MalformedURLException e) {
            MsgLog.error("DapParser(): malformed URL: " + currentDir);
        }
    }

    /**
	* Set a spider for this DapParser. 
	* @param _spider - spider used for storing URL that this parser finds.
	**/
    public void setSpider(DapSpider _spider) {
        spider = _spider;
    }

    public void setRegex(String _filenameRegex, String _baseFileRegex) {
        baseFileRegex = _baseFileRegex;
        filenameRegex = _filenameRegex;
    }

    /**
	* Parses an input stream.
	* @param is - input stream.
	**/
    public void parse(InputStream is) throws IOException {
        DapHtmlParser htmlParser = new DapHtmlParser(report, base, this);
        htmlParser.parseStream(is);
    }

    /**
	* Get the depth (number of path element, or directory level) of a URL.
	* @param base - base URL to find level from.
	* @param url - the url to find how many levels away it is from base.
	* @return a count of how many levels beyond the base URL that the specified URL is.
	**/
    public int getLevel(URL base, URL url) {
        if (url.toString().indexOf(base.toString()) > -1) {
            String fromBasePath = url.toString().replaceAll(base.toString(), "");
            String[] split = fromBasePath.split("/");
            return split.length;
        } else {
            return 0;
        }
    }

    /**
	* check if a url is a valid link.
	* @return true if it is, false otherwise
	**/
    protected boolean foundLink(URL base, URL url) {
        MsgLog.mumble("DapParser.foundLink(): Checking link " + url.toString());
        report.count(0);
        if (!followExternalLinks && (!url.getHost().equalsIgnoreCase(base.getHost()))) {
            MsgLog.mumble("DapParser.foundLink(): Url " + url.toString() + " does not have host of base url " + base.toString());
            if (!spider.workloadError.contains(url)) spider.workloadError.add(url);
            return false;
        }
        if (!followLowerBase && (url.getPath().indexOf(base.getPath()) == -1)) {
            MsgLog.mumble("DapParser.foundLink(): Url " + url.toString() + " is outside of path of base url " + base.toString());
            if (!spider.workloadError.contains(url)) spider.workloadError.add(url);
            return false;
        }
        if (!followQueries && (url.getQuery() != null)) {
            MsgLog.message("DapParser.foundLink(): Url " + url.toString() + " is a query and followQueries set false " + base.toString());
            if (!spider.workloadError.contains(url)) spider.workloadError.add(url);
            return false;
        }
        return true;
    }

    /**
	* Check if a link is a DAP file
	* @return  if it is return true, otherwise return false
	**/
    protected boolean foundDapLink(URL base, URL url) {
        MsgLog.mumble("DapParser.foundDapLink(): Checking DAP link " + url.toString());
        if (!isDapUrl(url)) {
            MsgLog.mumble("DapParser.foundDapLink(): Url " + url.toString() + " is not a valid DAP url");
            return false;
        }
        return true;
    }

    /**
	* Check if a URL matches a DAP url 
	* @param url - URL to test.
	* @return true if it is a DAP url, false otherwise.
	* e.g.  http://www.antcrc.utas.edu.au/dods/nph-dods/dods-ncep2/gaussian_grid/pres.sfc.gauss/pres.sfc.gauss.2003.nc.html
	**/
    protected boolean isDapUrl(URL url) {
        Pattern pattern = Pattern.compile(this.filenameRegex);
        Matcher matcher = pattern.matcher(url.toString());
        boolean dapFound = matcher.find();
        if (dapFound) MsgLog.mumble("DapParser.isDapUrl(): This is a valid DAP url: " + url.toString()); else MsgLog.mumble("DapParser.isDapUrl(): Invalid DAP url: " + url.toString());
        return dapFound;
    }

    /**
	* return the base URL from a DAP link
	* @param url - a DAP link.
	* @return - the base URL
	* e.g.  http://www.antcrc.utas.edu.au/dods/nph-dods/dods-ncep2/gaussian_grid/pres.sfc.gauss/pres.sfc.gauss.2003.nc
     * removes .das, .dds, .dods, .info, .html, .ver, .help
	**/
    public URL getDapBaseUrl(URL url) {
        URL baseUrl;
        Pattern pattern = Pattern.compile(this.baseFileRegex);
        Matcher matcher = pattern.matcher(url.toString());
        String url_string = "";
        if (matcher.find()) {
            url_string = matcher.group();
        }
        try {
            baseUrl = new URL(url_string);
        } catch (MalformedURLException e) {
            MsgLog.error("DapParser.getDapBaseUrl(): Found malformed URL: " + e);
            return url;
        }
        return baseUrl;
    }

    /**
	* Handle link, this is called whenever a link has been encountered.
	* determine if the link is a DAP file or a url. If a DAP file, then add it to the spider's list of DAP files
	* If a regular link, then add it to the spider's list of links to traverse 
	* @param base - the base URL.
	* @param str - new URL.
	**/
    protected void handleLink(URL base, String str) {
        MsgLog.mumble("DapParser.handleLink(): base is " + base.toString() + ", ref is " + str);
        MsgLog.mumble("DapParser.handleLink(): currentDir_asURL is " + currentDir_asURL.toString());
        try {
            URL url = new URL(currentDir_asURL, str);
            MsgLog.mumble("DapParser.handleLink(): url is " + url.toString());
            if (getLevel(base, url) <= MAX_DEPTH_COUNT) {
                if (foundDapLink(base, url)) {
                    MsgLog.mumble("DapParser.handleLink(): Found DAP URL " + url.toString());
                    spider.addDapURL(getDapBaseUrl(url).toString());
                    return;
                }
                if (foundLink(base, url)) {
                    MsgLog.mumble("DapParser.handleLink(): Found link " + url.toString());
                    MsgLog.mumble("DapParser.handleLink(): Adding link in DapParser");
                    spider.addURL(url);
                    return;
                }
            }
        } catch (MalformedURLException e) {
            MsgLog.error("DapParser.handleLink(): Found malformed URL: " + str);
        }
    }

    /**
	* Check whether this link is accessible.
	* @param url - URL to check.
	* @return true if it can be connected, false otherwise.  Note that if proxy has been set,
	* this will use the proxy to access the link.
	**/
    protected boolean checkLink(URL url) {
        try {
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (IOException e) {
            MsgLog.error("DapParser.checkLink(): IOException: " + e.toString());
            return false;
        }
    }

    public boolean getFollowQueries() {
        return followQueries;
    }

    public boolean getFollowExternalLinks() {
        return followExternalLinks;
    }

    public boolean getFollowLowerBase() {
        return followLowerBase;
    }

    public void setFollowQueries(boolean _followQueries) {
        followQueries = _followQueries;
    }

    public void setFollowExternalLinks(boolean _followExternalLinks) {
        followExternalLinks = _followExternalLinks;
    }

    public void setFollowLowerBase(boolean _followLowerBase) {
        followLowerBase = _followLowerBase;
    }
}
