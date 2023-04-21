package com.idna.gav.rules.international.volt;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import com.idna.gav.exceptions.VoltMissingORInvalidInputException;

/**
 * Author: vlad.shiligin
 * Date: 24-Sep-2008
 */
public class CountryRuleManager {

    BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("applicationContext-voltCountryRules.xml"));

    /**
	 * Apply country rules as per <code>countryCode</code> in the request
	 * 
	 * @param gavRequest Cleansed GA request
	 * @throws NotEnoughSearchDataException
	 */
    public void process(Document gavRequest) throws VoltMissingORInvalidInputException {
        String countryCode = getCountryCode(gavRequest);
        CountryRule rule = (CountryRule) beanFactory.getBean(countryCode);
        rule.applyRules(gavRequest);
    }

    public void processReverseSearch(Document gavRequest) throws VoltMissingORInvalidInputException {
        String countryCode = getCountryCode(gavRequest);
        CountryRule rule = (CountryRule) beanFactory.getBean(countryCode);
    }

    private String getCountryCode(Document gavRequest) {
        Node node = null;
        if (gavRequest != null) {
            node = gavRequest.selectSingleNode("/Search/CountryCode");
        }
        if (node != null && node.hasContent()) {
            String text = node.getText();
            if (StringUtils.isNotBlank(text)) {
                return text;
            }
        }
        return null;
    }
}
