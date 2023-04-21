package org.boehn.kmlframework.todo.servlet;

import org.boehn.kmlframework.kml.Kml;
import org.boehn.kmlframework.kml.KmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NetworkLinkControl {

    private String message;

    private String cookie;

    private String linkName;

    private String linkDescription;

    private Integer minRefreshPeriod;

    public NetworkLinkControl() {
    }

    public NetworkLinkControl(String message, String cookie, String linkName, String linkDescription, Integer minRefreshPeriod) {
        this.message = message;
        this.cookie = cookie;
        this.linkName = linkName;
        this.linkDescription = linkDescription;
        this.minRefreshPeriod = minRefreshPeriod;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getLinkDescription() {
        return linkDescription;
    }

    public void setLinkDescription(String linkDescription) {
        this.linkDescription = linkDescription;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMinRefreshPeriod() {
        return minRefreshPeriod;
    }

    public void setMinRefreshPeriod(Integer minRefreshPeriod) {
        this.minRefreshPeriod = minRefreshPeriod;
    }

    public void addKml(Element parentElement, Kml model, Document xmlDocument) throws KmlException {
        Element networkLinkControlElement = xmlDocument.createElement("NetworkLinkControl");
        if (cookie != null) {
            Element cookieElement = xmlDocument.createElement("cookie");
            cookieElement.appendChild(xmlDocument.createCDATASection(cookie));
            networkLinkControlElement.appendChild(cookieElement);
        }
        if (linkDescription != null) {
            Element linkDescriptionElement = xmlDocument.createElement("linkDescription");
            linkDescriptionElement.appendChild(xmlDocument.createCDATASection(linkDescription));
            networkLinkControlElement.appendChild(linkDescriptionElement);
        }
        if (linkName != null) {
            Element linkNameElement = xmlDocument.createElement("linkName");
            linkNameElement.appendChild(xmlDocument.createCDATASection(linkName));
            networkLinkControlElement.appendChild(linkNameElement);
        }
        if (message != null) {
            Element messageElement = xmlDocument.createElement("message");
            messageElement.appendChild(xmlDocument.createCDATASection(message));
            networkLinkControlElement.appendChild(messageElement);
        }
        if (minRefreshPeriod != null) {
            Element minRefreshPeriodElement = xmlDocument.createElement("minRefreshPeriod");
            minRefreshPeriodElement.appendChild(xmlDocument.createTextNode(minRefreshPeriod.toString()));
            networkLinkControlElement.appendChild(minRefreshPeriodElement);
        }
        parentElement.appendChild(networkLinkControlElement);
    }
}
