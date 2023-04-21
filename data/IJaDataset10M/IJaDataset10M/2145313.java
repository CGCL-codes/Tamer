package com.idna.gav.rules.international.postgav.chain.processor.postcode;

import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class NetherlandsPostCodeProcessor extends DefaultPostCodeProcessor {

    @Override
    protected void processPostCode(Document voltResponse) {
        logger.debug("[" + beanName + "] processing postcode...");
        List<Element> listings = voltResponse.selectNodes("//xOasis/Results/Listing");
        for (int i = 0; i < listings.size(); i++) {
            Element listingNode = listings.get(i);
            Element localityNameNode = (Element) listingNode.selectSingleNode("./AddressDetails/Country/Locality/LocalityName");
            if (localityNameNode != null && localityNameNode.hasContent()) {
                String locality = localityNameNode.getText();
                if (StringUtils.isNotBlank(locality)) {
                    if (locality.length() >= 7) {
                        String word = locality.substring(0, 7);
                        word = word.trim();
                        if (isPostCodeValid(word)) {
                            Node postcode = listingNode.selectSingleNode("./AddressDetails/Country/Locality/PostalCode/AddressLine");
                            if (postcode != null) {
                                logger.debug("[" + beanName + "] setting ./AddressDetails/Country/Locality/PostalCode/AddressLine: " + word);
                                postcode.setText(word);
                            } else {
                                logger.debug("[" + beanName + "] setting ./AddressDetails/Country/Locality/PostalCode/AddressLine/PostalCode/AddressLine: " + word);
                                postcode = listingNode.addElement("PostalCode").addElement("AddressLine");
                                postcode.setText(word);
                            }
                            logger.debug("[" + beanName + "] setting ./AddressDetails/Country/Locality/LocalityName: " + StringUtils.remove(locality, word).trim());
                            localityNameNode.setText(StringUtils.remove(locality, word).trim());
                        }
                    }
                }
            }
        }
    }
}
