package org.torweg.pulse.component.util.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.sf.json.JSONObject;
import org.jdom.Element;

/**
 * Represents a {@code Representative} as used by the utility-component
 * "representative".
 * 
 * @author Christian Schatt, Daniel Dietz
 * @version $Revision: 1577 $
 */
@Entity
@XmlRootElement(name = "representative")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.FIELD)
public class Representative extends AbstractExtendedAddress {

    /**
	 * The serialVersionUID.
	 */
    private static final long serialVersionUID = -4650633524808332259L;

    /**
	 * The region where the country represented by the
	 * {@code Representative} is located.
	 */
    @Basic
    @Column(nullable = false)
    @XmlElement(name = "represents-region")
    private String representsRegion = null;

    /**
	 * The country represented by the {@code Representative}.
	 */
    @Basic
    @Column(nullable = false)
    @XmlElement(name = "represents-country")
    private String representsCountry = null;

    /**
	 * The state represented by the {@code Representative}.
	 */
    @Basic
    @XmlElement(name = "represents-state")
    private String representsState = null;

    /**
	 * The name affix of the {@code Representative}.
	 */
    @Basic
    @XmlElement(name = "name-affix")
    private String nameAffix = null;

    /**
	 * The address affix of the {@code Representative}.
	 */
    @Basic
    @XmlElement(name = "address-affix")
    private String addressAffix = null;

    /**
	 * The default {@code Constructor}.
	 */
    public Representative() {
        super();
    }

    /**
	 * Returns the region where the country represented by the
	 * {@code Representative} is located.
	 * 
	 * @return the region where the country represented by the
	 *         {@code Representative} is located.
	 */
    public final String getRepresentsRegion() {
        return this.representsRegion;
    }

    /**
	 * Sets the region where the country represented by the
	 * {@code Representative} is located.
	 * 
	 * @param r
	 *            the represented country's region to be set.
	 */
    public final void setRepresentsRegion(final String r) {
        this.representsRegion = r;
    }

    /**
	 * Returns the country represented by the {@code Representative}.
	 * 
	 * @return the country represented by the {@code Representative}.
	 */
    public final String getRepresentsCountry() {
        return this.representsCountry;
    }

    /**
	 * Sets the country represented by the {@code Representative}.
	 * 
	 * @param c
	 *            the represented country to be set.
	 */
    public final void setRepresentsCountry(final String c) {
        this.representsCountry = c;
    }

    /**
	 * Returns the state represented by the {@code Representative}.
	 * 
	 * @return the state represented by the {@code Representative}.
	 */
    public final String getRepresentsState() {
        return this.representsState;
    }

    /**
	 * Sets the state represented by the {@code Representative}.
	 * 
	 * @param s
	 *            the represented state to be set.
	 */
    public final void setRepresentsState(final String s) {
        this.representsState = s;
    }

    /**
	 * Returns the name affix of the {@code Representative} or
	 * {@code null}.
	 * 
	 * @return the name affix of the {@code Representative} or
	 *         {@code null}.
	 */
    public final String getNameAffix() {
        return this.nameAffix;
    }

    /**
	 * Sets the name affix of the {@code Representative}.
	 * 
	 * @param na
	 *            the name affix to be set.
	 */
    public final void setNameAffix(final String na) {
        this.nameAffix = na;
    }

    /**
	 * Returns the address affix of the {@code Representative} or
	 * {@code null}.
	 * 
	 * @return the address affix of the {@code Representative} or
	 *         {@code null}.
	 */
    public final String getAddressAffix() {
        return this.addressAffix;
    }

    /**
	 * Sets the address affix of the {@code Representative}.
	 * 
	 * @param aa
	 *            the address affix to be set.
	 */
    public final void setAddressAffix(final String aa) {
        this.addressAffix = aa;
    }

    /**
	 * De-serialises the state of the {@code Representative} as a JDOM
	 * {@code Element}.
	 * 
	 * @return the state of the {@code Representative} as a JDOM
	 *         {@code Element}.
	 * 
	 */
    @Override
    public final Element deserializeToJDOM() {
        Element representativeEl = super.deserializeToJDOM();
        if (getRepresentsRegion() != null) {
            representativeEl.addContent(new Element("represents-region").setText(getRepresentsRegion()));
        }
        if (getRepresentsCountry() != null) {
            representativeEl.addContent(new Element("represents-country").setText(getRepresentsCountry()));
        }
        if (getRepresentsCountry() != null) {
            representativeEl.addContent(new Element("represents-state").setText(getRepresentsState()));
        }
        if (getNameAffix() != null) {
            representativeEl.addContent(new Element("name-affix").setText(getNameAffix()));
        }
        if (getAddressAffix() != null) {
            representativeEl.addContent(new Element("address-affix").setText(getAddressAffix()));
        }
        return representativeEl;
    }

    /**
	 * Returns a JSON-representation of the {@code Representative}.
	 * 
	 * @return a {@code JSONObject}
	 */
    @Override
    public final JSONObject toJSON() {
        JSONObject representativeJSON = super.toJSON();
        representativeJSON.put("representsRegion", getRepresentsRegion());
        representativeJSON.put("representsCountry", getRepresentsCountry());
        representativeJSON.put("representsState", getRepresentsState());
        representativeJSON.put("nameAffix", getNameAffix());
        representativeJSON.put("addressAffix", getAddressAffix());
        return representativeJSON;
    }
}
