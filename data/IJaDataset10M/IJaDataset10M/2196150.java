package org.nightlabs.jfire.store;

import java.io.Serializable;
import org.nightlabs.jdo.ObjectIDUtil;

/**
 * @author Marco Schulze - marco at nightlabs dot de
 *
 * @jdo.persistence-capable
 *		identity-type="application"
 *		objectid-class="org.nightlabs.jfire.store.id.ProductReferenceGroupID"
 *		detachable="true"
 *		table="JFireTrade_ProductReferenceGroup"
 *
 * @jdo.inheritance strategy="new-table"
 *
 * @jdo.create-objectid-class
 *		field-order="organisationID, anchorTypeID, productReferenceGroupID, productOrganisationID, productProductID"
 */
public class ProductReferenceGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String organisationID;

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String anchorTypeID;

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="201"
	 */
    private String productReferenceGroupID;

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String productOrganisationID;

    /**
	 * @jdo.field primary-key="true"
	 */
    private long productProductID;

    /**
	 * This must be in the range -1 &lt;= quantity &lt;= 1 at the end of a transaction. During the
	 * transaction, it might be more or less.
	 *
	 * @jdo.field persistence-modifier="persistent"
	 */
    private int quantity = 0;

    private ProductReference significantProductReference = null;

    public ProductReferenceGroup(String organisationID, String anchorTypeID, String productReferenceGroupID, String productOrganisationID, long productProductID) {
        this.organisationID = organisationID;
        this.anchorTypeID = anchorTypeID;
        this.productReferenceGroupID = productReferenceGroupID;
        this.productOrganisationID = productOrganisationID;
        this.productProductID = productProductID;
    }

    /**
	 * @deprecated Only for JDO!
	 */
    @Deprecated
    protected ProductReferenceGroup() {
    }

    public String getOrganisationID() {
        return organisationID;
    }

    public String getAnchorTypeID() {
        return anchorTypeID;
    }

    public String getProductReferenceGroupID() {
        return productReferenceGroupID;
    }

    public String getProductOrganisationID() {
        return productOrganisationID;
    }

    public long getProductProductID() {
        return productProductID;
    }

    public String getPrimaryKey() {
        return organisationID + '/' + anchorTypeID + '/' + productReferenceGroupID + '/' + productOrganisationID + '/' + ObjectIDUtil.longObjectIDFieldToString(productProductID);
    }

    public int getQuantity() {
        return quantity;
    }

    protected void setQuantity(int quantity) {
        this.quantity = quantity;
        if (quantity == 0 && significantProductReference != null) significantProductReference = null;
    }

    /**
	 * @return Returns the ProductReference that caused {@link #quantity} to be 1 or -1. Returns <code>null</code>,
	 *		if quantity == 0.
	 */
    public ProductReference getSignificantProductReference() {
        return significantProductReference;
    }

    protected void setSignificantProductReference(ProductReference significantProductReference) {
        this.significantProductReference = significantProductReference;
    }
}
