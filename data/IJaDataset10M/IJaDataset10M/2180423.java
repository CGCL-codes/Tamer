package com.gusto.engine.spring.parsers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import com.gusto.engine.semsim.measures.impl.DateIntervalValueSimilarity;

public class ParserDateIntervalSimilarity extends AbstractBeanDefinitionParser {

    @SuppressWarnings("unchecked")
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(DateIntervalValueSimilarity.class);
        if (element.hasAttribute("unit")) {
            String unit = element.getAttribute("unit");
            beanDefinitionBuilder.addPropertyValue("unit", unit);
        }
        Element entries = DomUtils.getChildElementByTagName(element, "entries");
        List entriesElements = DomUtils.getChildElementsByTagName(entries, "intervalEntry");
        Map<String, Double> intervals = new HashMap<String, Double>();
        for (int i = 0; i < entriesElements.size(); ++i) {
            Element childElement = (Element) entriesElements.get(i);
            String from = childElement.getAttribute("from");
            String to = childElement.getAttribute("to");
            Double sim = (Double.parseDouble(childElement.getAttribute("sim")));
            intervals.put(from + "-" + to, sim);
        }
        beanDefinitionBuilder.addPropertyValue("intervals", intervals);
        return beanDefinitionBuilder.getBeanDefinition();
    }
}
