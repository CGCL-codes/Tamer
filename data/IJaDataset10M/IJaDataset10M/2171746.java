package com.gusto.engine.spring.parsers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import com.gusto.engine.semsim.measures.impl.JaccardBinarySetSimilarity;

public class ParserJaccardBinarySimilarity extends AbstractBeanDefinitionParser {

    @SuppressWarnings("unchecked")
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(JaccardBinarySetSimilarity.class);
        if (element.hasAttribute("type")) {
            String type = element.getAttribute("type");
            beanDefinitionBuilder.addConstructorArgValue(type);
        }
        Element stopWords = DomUtils.getChildElementByTagName(element, "stopwords");
        if (stopWords != null) {
            List childElements = DomUtils.getChildElementsByTagName(stopWords, "stopword");
            if (childElements != null) {
                List<String> sw = new ArrayList<String>();
                for (int i = 0; i < childElements.size(); ++i) {
                    Element childElement = (Element) childElements.get(i);
                    sw.add(childElement.getTextContent());
                }
                beanDefinitionBuilder.addPropertyValue("stopwords", sw);
            }
        }
        return beanDefinitionBuilder.getBeanDefinition();
    }
}
