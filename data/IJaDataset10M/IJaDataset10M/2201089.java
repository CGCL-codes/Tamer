package org.linkerproject.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.imlinker.util.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.linkerproject.message.MessageList;
import org.linkerproject.setting.SettingFile;

public class LocalServer {

    private String path;

    private ArrayList<RefreshListener> listeners = new ArrayList<RefreshListener>();

    private MessageList list = new MessageList();

    public LocalServer(String path) {
        this.path = path + "/http/";
        String settingUrl = this.path + "setting.html";
        SettingFile settingFile = new SettingFile(null, settingUrl);
        ArrayList<Element> es = settingFile.getChildren("BODY.DIV");
        Boolean fanfouEnable = false;
        String fanfouUser = "";
        String fanfouPass = "";
        for (Element e : es) {
            if (!StringUtils.equal("INPUT", e.getName())) {
                continue;
            }
            if (StringUtils.equal("fanfoucheck", e.getAttributeValue("id"))) {
                fanfouEnable = Boolean.valueOf(e.getAttributeValue("checked"));
            } else if (StringUtils.equal("fanfouuser", e.getAttributeValue("id"))) {
                fanfouUser = e.getAttributeValue("value");
            } else if (StringUtils.equal("fanfoupass", e.getAttributeValue("id"))) {
                fanfouPass = e.getAttributeValue("value");
            }
        }
        if (fanfouEnable) {
            this.list.initFanfou(fanfouUser, fanfouPass);
        } else {
            list.removeFanfou();
        }
    }

    public void addRefreshListener(RefreshListener refreshListener) {
        listeners.add(refreshListener);
    }

    public String getLocalFile(String fileUrl) {
        File file = new File(fileUrl);
        if (!file.exists()) {
            return null;
        }
        try {
            StringBuffer content = new StringBuffer("");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(String lastUrl, String html) {
        System.out.println("saving:" + lastUrl);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(lastUrl));
            bw.write(html);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void view(String url) {
        System.out.println("local view:" + url);
        String content = getLocalFile(url);
        String startLabel = "<DIV id=\"messages_start\">";
        String endLabel = "<DIV id=\"messages_end\">";
        if (content.indexOf(startLabel) >= 0 && content.indexOf(endLabel) > 0) {
            int start = content.indexOf(startLabel);
            int end = content.indexOf(endLabel);
            content = content.substring(0, start) + startLabel + list.getHtml() + "</div>" + content.substring(end);
            this.save(url, content);
        }
    }

    public Document getDocumentByFile(String url) {
        Document document = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            document = builder.build(new File(url));
        } catch (JDOMException e) {
        } catch (IOException e) {
        }
        return document;
    }
}
