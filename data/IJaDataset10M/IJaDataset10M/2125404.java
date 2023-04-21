package mh.top.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import mh.top.dao.BizDao;
import mh.top.domain.CreateHtml;

/**
 * 本类完成jsp页面转成html页面的功能。
 * @author Administrator
 *
 */
public class HtmlCreater {

    private BizDao dao;

    private Logger logger;

    /**
	 * 无参构造
	 */
    public HtmlCreater() {
        dao = new BizDao();
        logger = Logger.getLogger(HtmlCreater.class);
    }

    /**
	 * 根据指定配置生成html
	 */
    public boolean createHtmlList() {
        boolean rtn = false;
        List list = dao.getUseCreateHtmlList();
        CreateHtml ch;
        String url = "", path = "";
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            ch = (CreateHtml) it.next();
            url = ch.getUrl();
            path = ch.getPath();
            this.createHtml(url, path);
            logger.debug("url:" + url + " path:" + path);
        }
        return false;
    }

    /**
	 * 根据url和path生成文件
	 * @param url
	 * @param path
	 * @return
	 */
    public boolean createHtml(String url, String path) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(path)) return false;
        boolean rtn = false;
        HttpURLConnection huc = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            huc = (HttpURLConnection) new URL(url).openConnection();
            huc.connect();
            InputStream stream = huc.getInputStream();
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
            br = new BufferedReader(new InputStreamReader(stream, "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().length() > 0) {
                    bw.write(line);
                    bw.newLine();
                }
            }
            rtn = true;
        } catch (IOException ioe) {
            logger.error("", ioe);
        } finally {
            try {
                br.close();
            } catch (IOException ioe) {
                logger.error("", ioe);
            }
            try {
                bw.close();
            } catch (IOException ioe) {
                logger.error("", ioe);
            }
            huc.disconnect();
        }
        return rtn;
    }
}
