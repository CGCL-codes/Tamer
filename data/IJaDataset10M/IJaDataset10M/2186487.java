package org.mainlove.project.web.demo.baiduOpenId.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@SuppressWarnings("unchecked")
public class ConfigRepository {

    public static final String X_OAUTH = "oauth";

    public static final String X_OPENAPI = "openapi";

    public static final String X_HTTPMETHOD = "httpmethod";

    private static final String X_URL = "url";

    private static final String X_NAME = "name";

    private static final String X_PARAM = "param";

    public static final String V_AUTHORIZE = "authorize";

    public static final String V_TOKEN = "token";

    private static Map<String, Map<String, URIConfig>> configsMap = null;

    public static Map<String, Map<String, URIConfig>> getConfigsMap() throws Exception {
        if (configsMap == null) {
            initConfigMap();
        }
        return configsMap;
    }

    private static void initConfigMap() throws Exception {
        configsMap = new HashMap<String, Map<String, URIConfig>>();
        String rootPath = ConfigRepository.class.getResource("/").getPath().substring(1);
        System.out.println(rootPath);
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(rootPath + "config.xml"));
        Element root = document.getRootElement();
        for (Element element : (List<Element>) root.elements()) {
            String typeName = (element.getName());
            Map<String, URIConfig> map = configsMap.get(typeName);
            if (map == null) {
                map = new HashMap<String, URIConfig>();
            }
            configsMap.put(element.getName(), map);
            initURIMap(map, element);
        }
    }

    private static void initURIMap(Map<String, URIConfig> map, Element element) {
        String name = element.attributeValue(X_NAME);
        URIConfig uriConfig = new URIConfig();
        uriConfig.setScheme(element.elementText(X_HTTPMETHOD));
        uriConfig.setHost(element.elementText(X_URL));
        for (Element elem : (List<Element>) element.elements(X_PARAM)) {
            uriConfig.getQparams().add(new BasicNameValuePair(elem.attributeValue(X_NAME), elem.getTextTrim()));
        }
        map.put(name, uriConfig);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getConfigsMap());
    }
}
