package de.flingelli.scrum.datastructure.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Markus Flingelli
 * 
 */
@XmlRootElement(name = "bugzillaType")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class BugzillaConnectionData implements Serializable, Cloneable {

    private static final long serialVersionUID = 2863309426045550781L;

    private String url;

    private String login;

    private String password;

    private String product;

    @XmlElement(name = "Url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlElement(name = "Login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @XmlElement(name = "Password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlElement(name = "Product")
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void writeXml(String fileName) throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(BugzillaConnectionData.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(this, new FileOutputStream(fileName));
    }

    public void readXML(String fileName) throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(BugzillaConnectionData.class);
        Unmarshaller um = context.createUnmarshaller();
        BugzillaConnectionData data = (BugzillaConnectionData) um.unmarshal(new FileInputStream(fileName));
        this.setLogin(data.getLogin());
        this.setProduct(data.getProduct());
        this.setUrl(data.getUrl());
        this.setPassword(data.getPassword());
    }
}
