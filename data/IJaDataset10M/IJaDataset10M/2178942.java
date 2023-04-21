package com.filesearch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import com.filesearch.FileSearchFBDAO;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class WebSearch {

    private static final Logger _log = Logger.getLogger(WebSearch.class);

    private static final String _log4jConfig = "log4j.properties";

    private static final FileSearchFBDAO _fileDao = new FileSearchFBDAO();

    private static final LinkParser _linkParser = new LinkParser();

    private static final String _fileSearchProps = "filesearch.properties";

    private static Integer _urlLimit = 0;

    private static String _downloadPath = "";

    private static String _linksFile = "";

    private static Integer _minFileSize = 0;

    private static Integer _maxUrls = 0;

    private static Integer _maxDownloads = 0;

    private static String _mode = "";

    private static int _maxThreads = 1;

    private static int _maxRecursion = 1;

    public static void main(String[] args) {
        PropertyConfigurator.configure(_log4jConfig);
        WebSearch ws = new WebSearch();
        if (ws.getSettings()) {
            ws.getFiles();
        }
    }

    private void getFiles() {
        try {
            while (true) {
                downloadFiles();
                processLinks();
            }
        } catch (Exception e) {
            _log.info("--->getFiles(): " + e.getMessage());
        }
    }

    private boolean getSettings() {
        boolean status = true;
        try {
            Properties prop = PropertyManager.loadProp(_fileSearchProps);
            _mode = prop.getProperty("mode");
            _downloadPath = prop.getProperty("downloadPath");
            _linksFile = prop.getProperty("linksFile");
            _urlLimit = Integer.valueOf(prop.getProperty("urlLimit"));
            _minFileSize = Integer.valueOf(prop.getProperty("minFileSize"));
            _maxUrls = Integer.valueOf(prop.getProperty("maxUrls"));
            _maxDownloads = Integer.valueOf(prop.getProperty("maxDownloads"));
            _maxRecursion = Integer.valueOf(prop.getProperty("maxRecursion"));
            _maxThreads = Integer.valueOf(prop.getProperty("maxThreads"));
            _fileDao.setUrlLimit(_urlLimit);
            _fileDao.setMaxUrls(_maxUrls);
            _fileDao.setMode(_mode);
            _fileDao.setMaxDownloads(_maxDownloads);
            _fileDao.setDownloadPath(_downloadPath);
            _linkParser.setExcludeTerms(_fileDao.getExcludeTerms());
            _linkParser.setExcludeTypes(_fileDao.getExcludeTypes());
            _linkParser.setIncludeTerms(_fileDao.getIncludeTerms());
            _linkParser.setIncludeTypes(_fileDao.getIncludeTypes());
            _linkParser.setLogger(_log);
        } catch (Exception e) {
            _log.info("--->getSettings(): " + e.getMessage());
            status = false;
        }
        return status;
    }

    @SuppressWarnings("unchecked")
    private void processLinks() throws Exception {
        String redirectURL = "";
        String urlAddress = "";
        String links = "";
        try {
            ArrayList urlList = _fileDao.getURLList();
            for (int i = 0; i < urlList.size(); i++) {
                ArrayList urlLinks = (ArrayList) urlList.get(i);
                ByteArrayOutputStream bos = null;
                Object[] urlBos = null;
                try {
                    if (urlLinks.size() == 1) {
                        urlAddress = (String) urlLinks.get(0);
                        urlBos = LinkParser.getURLStream(urlAddress);
                        redirectURL = (String) urlBos[0];
                        Integer code = (Integer) urlBos[1];
                        bos = (ByteArrayOutputStream) urlBos[2];
                        String host = _fileDao.getRootUrl(redirectURL);
                        urlBos = null;
                        if (!urlAddress.equalsIgnoreCase(redirectURL)) {
                            _fileDao.updateURLStatus("urls", urlAddress);
                            urlAddress = redirectURL;
                        }
                        if (code < 400) {
                            if (bos != null && bos.size() > 0) {
                                _log.debug("HTML Size=" + Integer.toString(bos.size()));
                                String html = bos.toString().toLowerCase();
                                bos = null;
                                links = _linkParser.getLinks(html, host);
                                html = null;
                                processURL(urlAddress, links, 1);
                            } else {
                                throw new Exception("No html to process.");
                            }
                        } else {
                            _log.info("Invalid URL: " + urlAddress + " URLResponse: " + code.toString());
                            _fileDao.updateURLStatus("urls", urlAddress);
                        }
                    } else {
                        processURL(urlAddress, urlLinks, 1);
                    }
                } catch (Exception e) {
                    _log.error("--->processLinks(): error processing url: " + urlAddress + " Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            _log.error("--->processLinks(): " + e.getMessage());
        }
        garbageCleanup();
    }

    private void addURL(String urlAddress, int recursionLevel) {
        Object[] urlBos = new Object[3];
        ByteArrayOutputStream bos = null;
        Integer code = 0;
        String host = "";
        int level = recursionLevel + 1;
        try {
            if (_linkParser.linkIsRelevant(urlAddress)) {
                _log.debug("Adding URL to search list: " + urlAddress);
                _fileDao.saveURL(urlAddress, "");
            } else {
                urlBos = LinkParser.getURLStream(urlAddress);
                String urlRedirect = (String) urlBos[0];
                code = (Integer) urlBos[1];
                bos = (ByteArrayOutputStream) urlBos[2];
                host = _fileDao.getRootUrl(urlRedirect);
                urlBos = null;
                if (!urlAddress.equalsIgnoreCase(urlRedirect)) {
                    _fileDao.updateURLStatus("urls", urlAddress);
                    urlAddress = urlRedirect;
                }
                if (code < 400) {
                    String html = bos.toString().toLowerCase();
                    if (_linkParser.pageIsRelevant(html)) {
                        String links = _linkParser.getLinks(html, host);
                        html = null;
                        if (urlHasFiles(links)) {
                            _log.debug("Adding URL to search list: " + urlAddress);
                            _fileDao.saveURL(urlAddress, links);
                            garbageCleanup();
                            if (recursionLevel < _maxRecursion) {
                                processURL(urlAddress, links, level);
                            } else {
                                _log.info("Max recursion level reached, saving URL to process later.");
                            }
                        } else {
                            _log.debug("Adding URL to search list: " + urlAddress);
                            _fileDao.saveURL(urlAddress, links);
                        }
                    } else {
                        _log.info("Rejecting Irrelevant URL: " + urlAddress);
                    }
                } else {
                    _log.info("Rejecting Bad URL: " + urlAddress + " URLResponse: " + code.toString());
                }
            }
        } catch (Exception e) {
            _log.error("--->addURL(): " + e.getMessage());
        }
        garbageCleanup();
    }

    private boolean urlHasFiles(String linkString) {
        boolean status = false;
        String[] links = linkString.split("\n");
        for (int i = 0; i < links.length; i++) {
            if (_linkParser.linkIsFileURL(links[i])) {
                status = true;
                break;
            }
        }
        return status;
    }

    private void garbageCleanup() {
        Long freeMemory = (Runtime.getRuntime().freeMemory());
        Long maxMemory = Runtime.getRuntime().maxMemory();
        Long totalMemory = Runtime.getRuntime().totalMemory();
        Long totalFree = (freeMemory + (maxMemory - totalMemory));
        _log.debug("Total Memory Before GC: " + Long.toString(totalFree / 1024) + "K");
        Runtime.getRuntime().gc();
        freeMemory = (Runtime.getRuntime().freeMemory());
        maxMemory = Runtime.getRuntime().maxMemory();
        totalMemory = Runtime.getRuntime().totalMemory();
        totalFree = (freeMemory + (maxMemory - totalMemory));
        _log.debug("Total Memory After GC: " + Long.toString(totalFree / 1024) + "K");
    }

    private boolean downloadFiles() throws Exception {
        String outputFolder = System.getProperty("user.dir") + _downloadPath;
        Timer timer = new Timer();
        garbageCleanup();
        try {
            if (_fileDao.getThreadCount() == 0) {
                _fileDao.cleanupOldDownloads(outputFolder, _linksFile);
            }
            ArrayList fileList = _fileDao.getDownloadList();
            if (fileList != null) {
                for (int i = 0; i < fileList.size(); i++) {
                    String urlFile = (String) fileList.get(i);
                    String newUrlFile = urlFile.replace(".new", ".old");
                    if (_fileDao.getThreadCount() < _maxThreads) {
                        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                        FileManager.renameFile(urlFile, newUrlFile);
                        DownloadTask downloadFiles = new DownloadTask();
                        downloadFiles.setFolderName(fmt.format(new Date()));
                        downloadFiles.setFileDao(_fileDao);
                        downloadFiles.setLinksFile(_linksFile);
                        downloadFiles.setMinFileSize(_minFileSize);
                        downloadFiles.setOutputFolder(outputFolder);
                        downloadFiles.setUrlFile(newUrlFile);
                        timer.schedule(downloadFiles, 0);
                        Thread.sleep(1000);
                    } else {
                        _log.info("Max thread count limit reached, saving files to download later.");
                    }
                }
            }
        } catch (Exception e) {
            _log.error("--->downloadFiles(): " + e.getMessage());
        }
        return true;
    }

    private void processURL(String urlAddress, ArrayList<String> linksArr, int recursionLevel) throws Exception {
        String[] links = (String[]) linksArr.toArray(new String[linksArr.size()]);
        processURL(urlAddress, links, recursionLevel);
    }

    private void processURL(String urlAddress, String linksString, int recursionLevel) throws Exception {
        String[] links = linksString.split("\n");
        processURL(urlAddress, links, recursionLevel);
    }

    private void processURL(String urlAddress, String[] links, int recursionLevel) throws Exception {
        int urlHasFiles = 0;
        try {
            garbageCleanup();
            _log.debug("Processing URL: " + urlAddress);
            _log.debug("Recursion Level: " + Integer.toString(recursionLevel));
            try {
                for (int i2 = 0; i2 < links.length; i2++) {
                    String link = links[i2];
                    try {
                        if (link != null && link.length() > 0) {
                            if (_linkParser.linkIsFileURL(link)) {
                                _log.debug("Adding file to download list: " + link);
                                _fileDao.addToDownloads(urlAddress, link);
                                urlHasFiles = 1;
                            } else {
                                if (!_linkParser.linkIsExcluded(link)) {
                                    if (!_fileDao.duplicateURL(link)) {
                                        addURL(link, recursionLevel);
                                    } else {
                                        _log.debug("Duplicate URL: " + link);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        _log.error("error processing link: " + link + " Error: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                _log.error("error processing url: " + urlAddress + " Error: " + e.getMessage());
            }
            if (urlHasFiles == 1) {
                downloadFiles();
            }
            _fileDao.updateURLStatus("urls", urlAddress);
        } catch (Exception e) {
            _log.error("--->processURL(): " + e.getMessage());
        }
    }
}
