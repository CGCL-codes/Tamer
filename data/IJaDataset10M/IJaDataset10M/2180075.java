package org.easy.eao.spring.schema;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easy.eao.exception.EaoDefinitionParserException;
import org.easy.eao.spring.Eao;
import org.easy.eao.spring.SpringEaoFactory;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ScanEaoDefinitionParser {

    /** 默认资源匹配符 */
    protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    protected final Log logger = LogFactory.getLog(getClass());

    /** 资源搜索分析器，由它来负责检索EAO接口 */
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /** 类的元数据读取器，由它来负责读取类上的注释信息 */
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;

    class EaoResource {

        String id;

        String classname;
    }

    public Set<BeanDefinitionHolder> parse(Element element, ParserContext parserContext) {
        Set<BeanDefinitionHolder> beans = new HashSet<BeanDefinitionHolder>();
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;
            Element e = (Element) node;
            if (!e.getLocalName().equals(EaoSchemaNames.PACKAGE_ELEMENT)) continue;
            try {
                EaoResource[] resources = getResources(e.getAttribute(EaoSchemaNames.PACKAGE_ATTRIBUTE_NAME));
                for (EaoResource eao : resources) {
                    BeanDefinitionBuilder factory = BeanDefinitionBuilder.genericBeanDefinition(SpringEaoFactory.class);
                    factory.addPropertyValue(SpringEaoFactory.ATTRIBUTE_CLAZZ, eao.classname);
                    String id = eao.id;
                    if (id.length() == 0) {
                        String name = StringUtils.substringAfterLast(eao.classname, ".");
                        id = StringUtils.uncapitalize(name);
                    }
                    BeanDefinitionHolder bean = new BeanDefinitionHolder(factory.getBeanDefinition(), id);
                    beans.add(bean);
                }
            } catch (IOException ex) {
                throw new EaoDefinitionParserException("获取EAO资源出错", ex);
            }
        }
        return beans;
    }

    /**
	 * 获取指定包下的全部 EAO 资源
	 * @param packageName 包名
	 * @return
	 * @throws IOException 
	 */
    private EaoResource[] getResources(String packageName) throws IOException {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(packageName) + "/" + this.resourcePattern;
        Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
        Set<EaoResource> result = new HashSet<EaoResource>();
        for (Resource resource : resources) {
            if (!resource.isReadable()) continue;
            MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
            AnnotationMetadata annoData = metadataReader.getAnnotationMetadata();
            if (!annoData.hasAnnotation(Eao.class.getName())) continue;
            EaoResource eao = new EaoResource();
            ClassMetadata clazzData = metadataReader.getClassMetadata();
            eao.classname = clazzData.getClassName();
            Map<String, Object> map = annoData.getAnnotationAttributes(Eao.class.getName());
            eao.id = (String) map.get("id");
            result.add(eao);
        }
        return result.toArray(new EaoResource[] {});
    }

    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }
}
