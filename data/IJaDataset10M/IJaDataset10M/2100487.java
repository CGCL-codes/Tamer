package com.inetmon.jn.update;

import java.io.ByteArrayInputStream;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlModuleParser {

    public Vector getModuleInf(String xmlInput) {
        ModuleInfo moduleInfo = null;
        Vector vecModule = new Vector();
        try {
            Document doc;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            byte[] b = xmlInput.getBytes();
            doc = builder.parse(new ByteArrayInputStream(b));
            NodeList children = doc.getElementsByTagName("DATA");
            System.out.println("children.getLength() = " + children.getLength());
            for (int num = 0; num < children.getLength(); num++) {
                Node dataInfo = children.item(num);
                if (dataInfo.getNodeType() == Node.ELEMENT_NODE) {
                    Element firstPersonElement = (Element) dataInfo;
                    String noList = "";
                    NodeList nodeNoList = firstPersonElement.getElementsByTagName("NO");
                    Element SELFIDText = (Element) nodeNoList.item(0).getChildNodes();
                    NodeList NOList = SELFIDText.getChildNodes();
                    noList = NOList.item(0).getNodeValue();
                    System.out.println("no" + num + " = " + noList);
                    String nameList = "";
                    NodeList nodeNameList = firstPersonElement.getElementsByTagName("NAME");
                    Element NAMEText = (Element) nodeNameList.item(0).getChildNodes();
                    NodeList NAMEList = NAMEText.getChildNodes();
                    if (NAMEText.getFirstChild() != null) {
                        nameList = new String(NAMEList.item(0).getNodeValue().getBytes(), "ISO8859_1");
                    } else {
                        nameList = "";
                    }
                    System.out.println("name" + num + " = " + nameList);
                    String sizeList = "";
                    NodeList nodeSizeList = firstPersonElement.getElementsByTagName("SIZE");
                    Element SIZEText = (Element) nodeSizeList.item(0).getChildNodes();
                    NodeList SIZEList = SIZEText.getChildNodes();
                    if (SIZEText.getFirstChild() != null) {
                        sizeList = new String(SIZEList.item(0).getNodeValue().getBytes(), "ISO8859_1");
                    } else {
                        sizeList = "";
                    }
                    System.out.println("Size" + num + " = " + sizeList);
                    String descList = "";
                    NodeList nodeDescList = firstPersonElement.getElementsByTagName("DESC");
                    Element DESCText = (Element) nodeDescList.item(0).getChildNodes();
                    NodeList DESCList = DESCText.getChildNodes();
                    if (DESCText.getFirstChild() != null) {
                        descList = new String(DESCList.item(0).getNodeValue().getBytes(), "ISO8859_1");
                    } else {
                        descList = "";
                    }
                    System.out.println("Desc" + num + " = " + descList);
                    String urlList = "";
                    NodeList nodeUrlList = firstPersonElement.getElementsByTagName("URL");
                    Element URLText = (Element) nodeUrlList.item(0).getChildNodes();
                    NodeList URLList = URLText.getChildNodes();
                    if (URLText.getFirstChild() != null) {
                        urlList = new String(URLList.item(0).getNodeValue().getBytes(), "ISO8859_1");
                    } else {
                        urlList = "";
                    }
                    System.out.println("urlList" + num + " = " + urlList);
                    String userList = "";
                    NodeList nodeUserList = firstPersonElement.getElementsByTagName("USER");
                    Element USERText = (Element) nodeUserList.item(0).getChildNodes();
                    NodeList USERList = USERText.getChildNodes();
                    if (USERText.getFirstChild() != null) {
                        userList = new String(USERList.item(0).getNodeValue().getBytes(), "ISO8859_1");
                    } else {
                        userList = "";
                    }
                    String passwordList = "";
                    NodeList nodePasswordList = firstPersonElement.getElementsByTagName("PASSWORD");
                    Element PASSWORDText = (Element) nodePasswordList.item(0).getChildNodes();
                    NodeList PASSWORDList = PASSWORDText.getChildNodes();
                    if (PASSWORDText.getFirstChild() != null) {
                        passwordList = new String(PASSWORDList.item(0).getNodeValue().getBytes(), "ISO8859_1");
                    } else {
                        passwordList = "";
                    }
                    moduleInfo = new ModuleInfo();
                    moduleInfo.setNo(noList.trim());
                    moduleInfo.setName(nameList);
                    moduleInfo.setSize(sizeList.trim());
                    moduleInfo.setDesc(descList);
                    moduleInfo.setUrl(urlList.trim());
                    moduleInfo.setUser(userList);
                    moduleInfo.setPassword(passwordList);
                    vecModule.addElement(moduleInfo);
                    System.out.println(moduleInfo.getUser());
                }
            }
        } catch (Exception e) {
            System.out.println("xml parser error");
        }
        return vecModule;
    }
}
