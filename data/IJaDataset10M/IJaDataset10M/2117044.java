package cn.common.action;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import cn.common.util.ResourceResolver;
import com.opensymphony.xwork2.config.providers.XmlConfigurationProvider;

/**
 * struts2框架扩展类，XML提供程序自动加载struts2配置文件
 * 
 * @author 孙树林
 * 
 */
public class LoadXmlConfigurationProvider extends XmlConfigurationProvider {

    /** 解析的struts2文件配置路径 */
    public static final String FILE_PATTERN = "classpath*:/**/struts-*.xml";

    public LoadXmlConfigurationProvider() {
        Map<String, String> mappings = new HashMap<String, String>();
        mappings.put("-//OpenSymphony Group//XWork 2.1.3//EN", "xwork-2.1.3.dtd");
        mappings.put("-//OpenSymphony Group//XWork 2.1//EN", "xwork-2.1.dtd");
        mappings.put("-//OpenSymphony Group//XWork 2.0//EN", "xwork-2.0.dtd");
        mappings.put("-//OpenSymphony Group//XWork 1.1.2//EN", "xwork-1.1.2.dtd");
        mappings.put("-//OpenSymphony Group//XWork 1.1.1//EN", "xwork-1.1.1.dtd");
        mappings.put("-//OpenSymphony Group//XWork 1.1//EN", "xwork-1.1.dtd");
        mappings.put("-//OpenSymphony Group//XWork 1.0//EN", "xwork-1.0.dtd");
        mappings.put("-//Apache Software Foundation//DTD Struts Configuration 2.0//EN", "struts-2.0.dtd");
        mappings.put("-//Apache Software Foundation//DTD Struts Configuration 2.1//EN", "struts-2.1.dtd");
        mappings.put("-//Apache Software Foundation//DTD Struts Configuration 2.1.7//EN", "struts-2.1.7.dtd");
        setDtdMappings(mappings);
    }

    /**
	 * 读取struts2配置文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
    @Override
    protected Iterator<URL> getConfigurationUrls(String fileName) throws IOException {
        ResourceResolver resolver = new ResourceResolver();
        List<URL> results = resolver.findClassPathResources(FILE_PATTERN);
        return results.iterator();
    }
}
