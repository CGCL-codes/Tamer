package org.iptc.ines.component.persistence.model;

import java.io.Serializable;
import org.w3c.dom.Document;

public class NewsMLDocument implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4406698669119783933L;

    private Long m_identity;

    private StoreAnyItem m_storeAnyItem;

    private Integer m_version;

    private Document m_xml;

    public NewsMLDocument() {
    }

    public Long getIdentity() {
        return m_identity;
    }

    public void setIdentity(Long identity) {
        m_identity = identity;
    }

    public Integer getVersion() {
        return m_version;
    }

    public void setVersion(Integer version) {
        m_version = version;
    }

    public Document getXml() {
        return m_xml;
    }

    public void setXml(Document xml) {
        m_xml = xml;
    }

    public StoreAnyItem getStoreAnyItem() {
        return m_storeAnyItem;
    }

    public void setStoreAnyItem(StoreAnyItem storeAnyItem) {
        m_storeAnyItem = storeAnyItem;
    }
}
