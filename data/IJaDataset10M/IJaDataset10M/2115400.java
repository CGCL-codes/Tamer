package com.eip.yost.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * @author abrisard
 */
public final class ConfigParser {

    private static ConfigParser sInstance = null;

    /**
	 * Private constructor
	 */
    private ConfigParser() {
    }

    /**
     * This method allow to access to the object instance
     * @return sInstance Singleton.
     */
    public static synchronized ConfigParser getInstance() {
        if (sInstance == null) {
            sInstance = new ConfigParser();
        }
        return sInstance;
    }

    public Boolean isTypeField(String pPathFile, String pKey, String pType) throws JDOMException, IOException {
        boolean vIsTypeField = false;
        SAXBuilder sxb = new SAXBuilder();
        Document document = sxb.build(pPathFile);
        Element racine = document.getRootElement();
        List listField = racine.getChild("configuration").getChildren("field");
        Iterator i = listField.iterator();
        while (i.hasNext()) {
            Element courant = (Element) i.next();
            if (pKey.equalsIgnoreCase(courant.getAttributeValue("name")) && pType.equalsIgnoreCase(courant.getAttributeValue("type"))) {
                vIsTypeField = true;
            }
        }
        return vIsTypeField;
    }

    public String getValidator(String pPathFile, String pKey) throws JDOMException, IOException {
        String vValidator = "";
        SAXBuilder sxb = new SAXBuilder();
        Document document = sxb.build(pPathFile);
        Element racine = document.getRootElement();
        List listField = racine.getChild("configuration").getChildren("field");
        Iterator i = listField.iterator();
        while (i.hasNext()) {
            Element courant = (Element) i.next();
            if (pKey.equalsIgnoreCase(courant.getAttributeValue("name"))) {
                vValidator = courant.getAttributeValue("validator");
            }
        }
        return vValidator;
    }

    public List<OptionModel> getOptionList(String pPathFile, String pKey) throws JDOMException, IOException {
        List<OptionModel> optionsModel = new ArrayList<OptionModel>();
        SAXBuilder sxb = new SAXBuilder();
        Document document = sxb.build(new File(pPathFile));
        Element racine = document.getRootElement();
        List listField = racine.getChild("configuration").getChildren("field");
        Iterator i = listField.iterator();
        while (i.hasNext()) {
            Element courant = (Element) i.next();
            if (pKey.equalsIgnoreCase(courant.getAttributeValue("name"))) {
                List listOption = courant.getChildren("option");
                Iterator j = listOption.iterator();
                while (j.hasNext()) {
                    Element optionCourant = (Element) j.next();
                    OptionModelImpl option = new OptionModelImpl(optionCourant.getAttributeValue("label"), optionCourant.getAttributeValue("value"));
                    optionsModel.add(option);
                }
            }
        }
        return optionsModel;
    }

    public List<String> getKeyList(String pPathFile) throws JDOMException, IOException {
        List<String> mKeyList = new ArrayList<String>();
        SAXBuilder sxb = new SAXBuilder();
        Document document = sxb.build(new File(pPathFile));
        Element racine = document.getRootElement();
        List listField = racine.getChild("configuration").getChildren("field");
        Iterator i = listField.iterator();
        while (i.hasNext()) {
            Element courant = (Element) i.next();
            mKeyList.add(courant.getAttributeValue("name"));
        }
        return mKeyList;
    }
}
