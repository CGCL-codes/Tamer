package tpac.lib.DAPSpider;

import dods.dap.*;
import dods.dap.parser.*;

public class DataDDSUtils {

    private static DataDDS retrieveDataDDS(String fileUrl, String arrayname) {
        return DataDDSUtils.retrieveDataDDS(fileUrl, arrayname, "");
    }

    public static DataDDS retrieveDataDDS(String fileUrl, String arrayname, int size) {
        return DataDDSUtils.retrieveDataDDS(fileUrl, arrayname, "0:1:" + (size - 1));
    }

    private static DataDDS retrieveDataDDS(String fileUrl, String arrayname, String constraint) {
        StatusUI progressFeedback = null;
        boolean acceptDeflate = false;
        DConnect connection = null;
        DataDDS result = null;
        String url = fileUrl + "?" + arrayname + "[" + constraint + "]";
        try {
            MsgLog.mumble("CrawlerArray().getOpenDAPData(): " + "using url " + url);
            MsgLog.mumble("CrawlerArray().getOpenDAPData(): " + "setting up connection.. ");
            connection = new DConnect(url, acceptDeflate);
            MsgLog.mumble("CrawlerArray().getOpenDAPData(): " + "retrieving data.. ");
            result = connection.getData(progressFeedback);
        } catch (java.net.MalformedURLException e) {
            MsgLog.error("CrawlerArray().getOpenDAPData(): malformed URL: " + url);
        } catch (java.io.FileNotFoundException e) {
            MsgLog.error("CrawlerArray().getOpenDAPData(): File not found: " + url);
        } catch (java.io.IOException e) {
            MsgLog.error("CrawlerArray().getOpenDAPData(): IOException");
        } catch (ParseException e) {
            MsgLog.error("CrawlerArray().getOpenDAPData(): " + "the DDS parser returned an error ");
        } catch (DDSException e) {
            MsgLog.error("CrawlerArray().getOpenDAPData(): " + " error constructing the DDS ");
        } catch (DODSException e) {
            MsgLog.error("CrawlerArray().getOpenDAPData(): " + " error returned by the remote server " + e.toString());
        }
        return result;
    }

    public static void printDataDDS(String fileUrl, String arrayname, int size) {
        DataDDSUtils.retrieveDataDDS(fileUrl, arrayname, size).print(System.out);
    }
}
