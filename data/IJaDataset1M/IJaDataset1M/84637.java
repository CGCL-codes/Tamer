package org.sharp.vocreader.core;

import org.sharp.intf.DownloadEventListener;
import org.sharp.intf.Response;
import org.sharp.intf.Response.StatusCode;
import org.sharp.utils.IOUtils;
import org.sharp.utils.Option;
import org.sharp.vocreader.beans.Course;
import org.sharp.vocreader.beans.UrlSetting;
import org.sharp.vocreader.intf.OsSupport;

public class NetWorkSupport {

    public static final String APP_NAME = "JpWordReader";

    public static final String QUERY_COMMAND = "q";

    private static final String appUseUrl = "http://wd4web.sinaapp.com/new-appuse.php";

    private static final String jpwordCourseZipUrlBase = "http://web-word-list.googlecode.com/files/";

    private static final String jpwordQueryUrl = "http://wd4web.sinaapp.com/jpword-httpquery.php";

    private static final String QUERY_COMMAND_COURSE_DOWN = "jp-course";

    private static final String QUERY_PNAME_COURSENO = "courseNo";

    private static final String QUERY_COMMAND_COURSES_MANIFEST = "jp-courses-manifest";

    private static final String QUERY_COMMAND_PAYINFO = "payInfo";

    private static UrlSetting mOnlineSetting;

    public static boolean isPayed(OsSupport os, int price, String type) {
        Response response = os.httpGet(payQueryUrl(os.getMdn(), os.getImei(), os.getUniqueId(), price, type));
        if (Response.StatusCode.NORMAL.equals(response.statusCode())) {
            String content = response.content();
            os.log("setting string:" + content);
            mOnlineSetting = (UrlSetting) os.fromString(content, UrlSetting.class);
        }
        return true;
    }

    public static Option<UrlSetting> addAppUse(OsSupport os) {
        Response response = os.httpGet(appUseUrl(appUseUrl, APP_NAME, os.getUniqueId()));
        if (Response.StatusCode.NORMAL.equals(response.statusCode())) {
            String content = response.content();
            os.log("setting string:" + content);
            mOnlineSetting = (UrlSetting) os.fromString(content, UrlSetting.class);
        }
        return new Option<UrlSetting>(mOnlineSetting);
    }

    private static String appUseUrl(String url, String appName, String userid) {
        return url + "?appname=" + appName + "&userid=" + userid;
    }

    private static String courseZipUrl(OsSupport os, int courseNo) {
        String zipDownUrl = null;
        Response rs = os.httpGet(jpwordQUrl() + "?" + QUERY_COMMAND + "=" + QUERY_COMMAND_COURSE_DOWN + "&" + QUERY_PNAME_COURSENO + "=" + courseNo);
        if (StatusCode.NORMAL.equals(rs.statusCode())) {
            zipDownUrl = rs.content();
        } else {
            zipDownUrl = IOUtils.fullPath(jpwordCourseZipUrlBase, courseNo + ".zip");
        }
        return zipDownUrl;
    }

    private static String payQueryUrl(String mdn, String imei, String userid, int amount, String type) {
        return jpwordQUrl() + "?" + QUERY_COMMAND + "=" + QUERY_COMMAND_PAYINFO + "&mdn=" + mdn + "&imei=" + imei + "&userid=" + userid + "&amount=" + amount + "&type=" + type;
    }

    private static String coursesManifestUrl() {
        return jpwordQUrl() + "?" + QUERY_COMMAND + "=" + QUERY_COMMAND_COURSES_MANIFEST;
    }

    public static void downloadCourseZip(OsSupport os, int courseNo, String savePath, DownloadEventListener del) {
        String downloadUrl = courseZipUrl(os, courseNo);
        os.downloadFile(downloadUrl, savePath, del);
    }

    public static Course[] fetchCourseList(OsSupport os) {
        Response response = os.httpGet(coursesManifestUrl());
        Course[] courses = null;
        if (Response.StatusCode.NORMAL.equals(response.statusCode())) {
            String content = response.content();
            os.log("course list string:" + content);
            courses = (Course[]) os.fromString(content, Course[].class);
        }
        return courses;
    }

    public static boolean isOnlineSettingNull() {
        return mOnlineSetting == null;
    }

    private static String jpwordQUrl() {
        if (isOnlineSettingNull()) return jpwordQueryUrl; else return mOnlineSetting.jpcourse_qurl;
    }

    public static UrlSetting onlineSetting() {
        return mOnlineSetting;
    }
}
