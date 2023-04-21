package com.memory.framework.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.memory.framework.i.IMemory;

/**
 * <h1>Servlet控制器</h1>
 * 
 * @version: v1.01 2011-3-27 上午10:48:40 李超
 */
public class ConfigMemory implements IMemory<Object> {

    private Logger logger = Logger.getLogger(this.getClass());

    private Map<String, Object> _config = null;

    private String _configPath;

    public ConfigMemory(String configPath) {
        _configPath = configPath;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ConfigMemory load() {
        Map<String, Object> config = new HashMap<String, Object>();
        Document document = null;
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(_configPath);
        } catch (DocumentException e) {
            logger.error(e, e);
        }
        List<Element> list = document.getRootElement().element("system").element("properties").elements("propertie");
        if (list != null) {
            for (Element element : list) {
                String key = element.attributeValue("key").trim();
                String value = element.attributeValue("value").trim();
                config.put(key, value);
                logger.info("readConfig property success: " + key + "=" + "\"" + value + "\"");
            }
        }
        List<Map<String, Object>> filterLst = new ArrayList<Map<String, Object>>();
        List<Element> filters = document.getRootElement().element("system").element("filters").elements("filter");
        logger.info("开始读取过滤器配置");
        for (Element filter : filters) {
            Map<String, Object> fltMap = new HashMap<String, Object>();
            String className = filter.attributeValue("class");
            logger.info("\t读取过滤器配置:" + className);
            fltMap.put("class", className);
            List<Element> elements = filter.elements("propertie");
            Properties properties = new Properties();
            for (Element element : elements) {
                String key = element.attributeValue("key");
                String value = element.attributeValue("value");
                properties.put(key, value);
                logger.info("\t\t读取参数: key[" + key + "] value[" + value + "]");
            }
            fltMap.put("properties", properties);
            filterLst.add(fltMap);
        }
        config.put("filters", filterLst);
        try {
            List<Map<String, Object>> logLst = new ArrayList<Map<String, Object>>();
            List<Element> logEs = document.getRootElement().element("system").element("logs").elements("log");
            logger.info("开始读取日志配置");
            for (Element logE : logEs) {
                Map<String, Object> logMap = new HashMap<String, Object>();
                String id = logE.attributeValue("id");
                logMap.put("id", id);
                String className = logE.attributeValue("class");
                logger.info("\t读取日志配置:" + id + "[" + className + "]");
                logMap.put("class", className);
                logLst.add(logMap);
            }
            config.put("logConfig", logLst);
        } catch (Exception e) {
            logger.error("加载log配置信息失败", e);
        }
        _config = config;
        return this;
    }

    @Override
    public void add(String key, Object object) {
        _config.put(key, object);
    }

    @Override
    public Object get(String key) {
        return _config.get(key);
    }
}
