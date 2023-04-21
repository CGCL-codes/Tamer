package cn.imgdpu.util;

public class FileUrlConv {

    public static String UrlConv(String relUrl) {
        String url = FileUrlConv.class.getClass().getResource("/").getPath().substring(1);
        if (url.endsWith("bin/")) {
            url = url.replaceAll("/bin/", "/");
        }
        String osType = System.getProperty("user.dir").substring(0, 1);
        if (osType.equals("/")) {
            url = "/" + url;
        }
        return url + relUrl;
    }

    public static String UrlConvIo(String relUrl) {
        String url;
        String osType = System.getProperty("user.dir").substring(0, 1);
        if (osType.equals("/")) {
            url = System.getProperty("user.dir") + "\\" + relUrl;
            url = url.replaceAll("\\\\", "/");
        } else {
            url = System.getProperty("user.dir") + "\\" + relUrl;
        }
        return url;
    }
}
