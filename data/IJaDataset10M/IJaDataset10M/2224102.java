package jcomicdownloader.module;

import jcomicdownloader.tools.*;
import jcomicdownloader.enums.*;
import jcomicdownloader.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class ParseJumpcncn extends ParseOnlineComicSite {

    private int radixNumber;

    private String jsName;

    protected String indexName;

    protected String indexEncodeName;

    protected String orinialWholeTitle;

    /**
 *
 * @author user
 */
    public ParseJumpcncn() {
        siteID = Site.JUMPCNCN;
        indexName = Common.getStoredFileName(SetUp.getTempDirectory(), "index_jumpcn_parse_", "html");
        indexEncodeName = Common.getStoredFileName(SetUp.getTempDirectory(), "index_jumpcn_encode_parse_", "html");
        jsName = "index_jumpcn.js";
        radixNumber = 185271;
        orinialWholeTitle = "";
    }

    public ParseJumpcncn(String webSite, String titleName) {
        this();
        this.webSite = webSite;
        this.title = titleName;
    }

    @Override
    public void setParameters() {
        Common.debugPrintln("開始解析各參數 :");
        Common.debugPrintln("開始解析title和wholeTitle :");
        Common.downloadFile(webSite, SetUp.getTempDirectory(), indexName, false, "");
        Common.newEncodeFile(SetUp.getTempDirectory(), indexName, indexEncodeName);
        String allPageString = Common.getFileString(SetUp.getTempDirectory(), indexEncodeName);
        String[] tokens = allPageString.split("\\d*>\\d*|\\d*<\\d*");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].matches("(?s).*" + webSite + "(?s).*")) {
                tokens[i + 1] = tokens[i + 1].replaceAll("\\.", "");
                orinialWholeTitle = tokens[i + 1];
                if (getWholeTitle() == null || getWholeTitle().equals("")) setWholeTitle(getVolumeWithFormatNumber(Common.getStringRemovedIllegalChar(Common.getTraditionalChinese(orinialWholeTitle))));
                break;
            }
        }
        Common.debugPrintln("作品名稱(title) : " + getTitle());
        Common.debugPrintln("章節名稱(wholeTitle) : " + getWholeTitle());
    }

    @Override
    public void parseComicURL() {
        String[] lines = Common.getFileStrings(SetUp.getTempDirectory(), indexName);
        int beginIndex = Common.getIndexOfOrderKeyword(webSite, "/", 4) + 1;
        int endIndex = Common.getIndexOfOrderKeyword(webSite, "/", 5);
        String idString = webSite.substring(beginIndex, endIndex);
        Common.debugPrintln("漫畫代號為：" + idString);
        Common.debugPrint("開始解析這一集有幾頁 :");
        Common.downloadFile(webSite + "index.js", SetUp.getTempDirectory(), jsName, false, "");
        String allJsPageString = Common.getFileString(SetUp.getTempDirectory(), jsName);
        String[] jsTokens = allJsPageString.split("=|;");
        for (int i = 0; i < jsTokens.length; i++) {
            if (jsTokens[i].matches("(?s).*total")) {
                totalPage = Integer.parseInt(jsTokens[i + 1]);
                Common.debugPrintln("共 " + totalPage + " 頁");
                comicURL = new String[totalPage];
                break;
            }
        }
        Common.debugPrintln("取得圖片網址父目錄 :");
        allJsPageString = getAllPageString("http://www.jumpcn.com.cn/Scripts/picshow.js");
        beginIndex = allJsPageString.indexOf("http://");
        endIndex = allJsPageString.indexOf("'", beginIndex);
        String baseURL = allJsPageString.substring(beginIndex, endIndex) + idString + "/" + orinialWholeTitle + "/";
        baseURL = baseURL.replaceAll("\\s", "%20");
        Common.debugPrintln("開始解析每一頁圖片的網址 :");
        boolean noNeedToAddZero = false;
        if (Common.urlIsOK(Common.getFixedChineseURL(baseURL) + "1.jpg")) noNeedToAddZero = true;
        for (int p = 1; p <= totalPage; p++) {
            String frontURL = String.valueOf(p) + ".jpg";
            if (noNeedToAddZero) comicURL[p - 1] = Common.getFixedChineseURL(baseURL + frontURL); else {
                NumberFormat formatter = new DecimalFormat("000");
                String fileName = formatter.format(p) + ".jpg";
                comicURL[p - 1] = Common.getFixedChineseURL(baseURL + fileName);
            }
        }
    }

    public void showParameters() {
        Common.debugPrintln("----------");
        Common.debugPrintln("totalPage = " + totalPage);
        Common.debugPrintln("webSite = " + webSite);
        Common.debugPrintln("----------");
    }

    @Override
    public String getAllPageString(String urlString) {
        String indexName = Common.getStoredFileName(SetUp.getTempDirectory(), "index_jumpcn_", "html");
        String indexEncodeName = Common.getStoredFileName(SetUp.getTempDirectory(), "index_jumpcn_encode_", "html");
        Common.downloadFile(urlString, SetUp.getTempDirectory(), indexName, false, "");
        Common.newEncodeFile(SetUp.getTempDirectory(), indexName, indexEncodeName);
        return Common.getFileString(SetUp.getTempDirectory(), indexEncodeName);
    }

    @Override
    public boolean isSingleVolumePage(String urlString) {
        if (urlString.matches("(?s).*/\\d+/\\d+/(?s).*")) return true; else return false;
    }

    @Override
    public String getTitleOnSingleVolumePage(String urlString) {
        int endIndex = Common.getIndexOfOrderKeyword(urlString, "/", 5) + 1;
        String mainPageUrlString = urlString.substring(0, endIndex);
        return getTitleOnMainPage(mainPageUrlString, getAllPageString(mainPageUrlString));
    }

    @Override
    public String getTitleOnMainPage(String urlString, String allPageString) {
        String[] tokens = allPageString.split(">|<");
        String title = "";
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].matches("(?s).*'fleft blue'(?s).*")) {
                title = tokens[i + 1];
                break;
            }
        }
        return Common.getStringRemovedIllegalChar(Common.getTraditionalChinese(title));
    }

    @Override
    public List<List<String>> getVolumeTitleAndUrlOnMainPage(String urlString, String allPageString) {
        List<List<String>> combinationList = new ArrayList<List<String>>();
        List<String> urlList = new ArrayList<String>();
        List<String> volumeList = new ArrayList<String>();
        int beginIndex = allPageString.indexOf("\"bookList\"");
        int endIndex = allPageString.indexOf("</table>");
        String tempString = allPageString.substring(beginIndex, endIndex);
        String[] tokens = tempString.split("\\d*>\\d*|\\d*<\\d*|\"");
        int volumeCount = 0;
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].matches("(?s).*http://www.pcomic.com.cn/(?s).*")) {
                String idString = tokens[i].split("/")[tokens[i].split("/").length - 1];
                urlList.add(urlString + idString + "/");
                String volumeTitle = "";
                if (tokens[i - 4].equals("") || tokens[i - 4] == null) volumeTitle = tokens[i - 10].replaceAll("\\.", ""); else volumeTitle = tokens[i - 4].replaceAll("\\.", "");
                volumeList.add(getVolumeWithFormatNumber(Common.getStringRemovedIllegalChar(Common.getTraditionalChinese(volumeTitle.trim()))));
                volumeCount++;
            }
        }
        totalVolume = volumeCount;
        Common.debugPrintln("共有" + totalVolume + "集");
        combinationList.add(volumeList);
        combinationList.add(urlList);
        return combinationList;
    }

    @Override
    public void outputVolumeAndUrlList(List<String> volumeList, List<String> urlList) {
        Common.outputFile(volumeList, SetUp.getTempDirectory(), Common.tempVolumeFileName);
        Common.outputFile(urlList, SetUp.getTempDirectory(), Common.tempUrlFileName);
    }

    @Override
    public String[] getTempFileNames() {
        return new String[] { indexName, indexEncodeName, jsName };
    }

    @Override
    public void printLogo() {
        System.out.println(" __________________________________");
        System.out.println("|                               ");
        System.out.println("| Run the JumpCNCN module: ");
        System.out.println("|__________________________________\n");
    }
}
