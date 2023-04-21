package br.com.devx.scenery.sitemesh;

import br.com.devx.scenery.web.Sitemesh;
import br.com.devx.scenery.web.SitemeshException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleSitemesh implements Sitemesh {

    private static final Logger s_log = Logger.getLogger(SimpleSitemesh.class);

    private String m_requestUri;

    private DecoratorsXml m_xml;

    private Map<String, String> m_html = new HashMap<String, String>();

    public SimpleSitemesh(String targetPath, String requestURI) throws SitemeshException {
        String filePath = targetPath + "/WEB-INF/decorators.xml";
        m_requestUri = requestURI;
        try {
            m_xml = new DecoratorsXml(filePath);
        } catch (ParserConfigurationException e) {
            throw new SitemeshException(filePath, e);
        } catch (SAXException e) {
            throw new SitemeshException(filePath, e);
        } catch (IOException e) {
            s_log.info("sitemesh: " + filePath + " not found.");
        }
    }

    public boolean isActive() {
        return m_xml != null && m_xml.isActive(m_requestUri);
    }

    public String getTemplate() {
        return m_xml.getTemplate(m_requestUri);
    }

    public void decorate(String html) {
        m_html = parse(html, new String[] { "head", "body" });
        if (m_html.size() == 0) {
            m_html.put("body", html);
        }
    }

    private Map<String, String> parse(String html, String[] tags) {
        Map<String, String> result = new HashMap<String, String>();
        for (String tag : tags) {
            String content = extract(tag, html);
            if (content != null) {
                result.put(tag, content);
            }
        }
        return result;
    }

    private String extract(String tag, String html) {
        String regex = new StringBuilder().append(".*<").append(tag).append("[^>]*>(.*?)</").append(tag).append("\\b*>.*").toString();
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(html);
        return m.find() ? m.group(1) : null;
    }

    public String get(String tag) {
        String result = m_html.get(tag);
        return result != null ? result : "";
    }
}
