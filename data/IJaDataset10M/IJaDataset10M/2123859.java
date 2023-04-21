package net.oschina.utils;

import my.mvc.RequestContext;
import my.util.Multimedia;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Action类常用工具
 * 转载的时候，文章中有图片，但是因为很多网站会做防盗链处理，使得在阅读文章时图片无法显示。
 * OSChina 也做了防盗链处理，没办法，流量有限。 解决的办法，发文章时程序自动解析出文章中所有的图片，并下载到本地，
 * 然后将图片的URL地址改为本地地址。 其中用到的 HTML 解析库是 jsoup。
 *
 * @author Winter Lau @ OSChina
 */
public class HTMLImageFetcher {

    public static void main(String[] args) {
        String html = "这张图片很漂亮啊！" + "<img src='http://techcn.com.cn/uploads/201004/12705551313uy0urE3.jpg' alt=''/>" + " ，太帅了！<img src='/img/logo.gif' alt='oschina'/>";
        System.out.println(fetchHTML_Images(html));
    }

    /**
     * 下载HTML文档中的所有图片
     *
     * @param html
     * @return
     */
    protected static String fetchHTML_Images(String html) {
        if (StringUtils.isBlank(html)) return html;
        HashMap<String, String> img_urls = new HashMap<String, String>();
        Document doc = Jsoup.parse(html);
        Elements imgs = doc.select("img");
        for (int i = 0; i < imgs.size(); i++) {
            Element img = imgs.get(i);
            String src = img.attr("src");
            if (!src.startsWith("/")) try {
                URL imgUrl = new URL(src);
                String imgHost = imgUrl.getHost().toLowerCase();
                if (!imgHost.endsWith(".oschina.net")) {
                    String new_src = img_urls.get(src);
                    if (new_src == null) {
                        new_src = fetchImageViaHttp(imgUrl);
                        img_urls.put(src, new_src);
                    }
                    img.attr("src", new_src);
                }
            } catch (MalformedURLException e) {
                img.remove();
            } catch (Exception e) {
                e.printStackTrace();
                img.remove();
            }
        }
        return doc.body().html();
    }

    private static String fetchImageViaHttp(URL imgUrl) throws IOException {
        String sURL = imgUrl.toString();
        String imgFile = imgUrl.getPath();
        HttpURLConnection cnx = (HttpURLConnection) imgUrl.openConnection();
        String uri = null;
        try {
            cnx.setAllowUserInteraction(false);
            cnx.setDoOutput(true);
            cnx.addRequestProperty("Cache-Control", "no-cache");
            RequestContext ctx = RequestContext.get();
            if (ctx != null) cnx.addRequestProperty("User-Agent", ctx.header("user-agent")); else cnx.addRequestProperty("User-Agent", user_agent);
            cnx.addRequestProperty("Referer", sURL.substring(0, sURL.indexOf('/', sURL.indexOf('.')) + 1));
            cnx.connect();
            if (cnx.getResponseCode() != HttpURLConnection.HTTP_OK) return null;
            InputStream imgData = cnx.getInputStream();
            String ext = FilenameUtils.getExtension(imgFile).toLowerCase();
            if (!Multimedia.isImageFile("aa." + ext)) ext = "jpg";
            uri = FMT_FN.format(new Date()) + RandomStringUtils.randomAlphanumeric(4) + '.' + ext;
            File fileDest = new File(img_path + uri);
            if (!fileDest.getParentFile().exists()) fileDest.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(fileDest);
            try {
                IOUtils.copy(imgData, fos);
            } finally {
                IOUtils.closeQuietly(imgData);
                IOUtils.closeQuietly(fos);
            }
        } finally {
            cnx.disconnect();
        }
        return RequestContext.get().contextPath() + "/uploads/img/" + uri;
    }

    protected static final String img_path = RequestContext.root() + "uploads" + File.separator + "img" + File.separator;

    protected static final SimpleDateFormat FMT_FN = new SimpleDateFormat("yyyyMM/ddHHmmss_");

    private static final String user_agent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)";
}
