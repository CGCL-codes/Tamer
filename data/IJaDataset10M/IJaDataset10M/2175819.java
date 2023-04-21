package org.vosao.entity.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.entity.PluginEntity;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class PluginHelper {

    protected static final Log logger = LogFactory.getLog(PluginHelper.class);

    public static Map<String, PluginParameter> parseParameters(PluginEntity plugin) {
        Map<String, PluginParameter> result = new HashMap<String, PluginParameter>();
        Document dataDoc = null;
        try {
            dataDoc = DocumentHelper.parseText(plugin.getConfigData());
        } catch (DocumentException e) {
        }
        try {
            Document configDoc = DocumentHelper.parseText(plugin.getConfigStructure());
            for (Element element : (List<Element>) configDoc.getRootElement().elements()) {
                PluginParameter param = new PluginParameter(element);
                try {
                    param.setValue(dataDoc.getRootElement().elementText(param.getName()));
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                result.put(param.getName(), param);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return result;
    }
}
