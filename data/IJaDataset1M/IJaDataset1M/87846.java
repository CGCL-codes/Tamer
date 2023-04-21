package com.ivata.mask.web.demo.product;

import java.math.BigDecimal;
import org.apache.log4j.Logger;
import com.ivata.mask.web.demo.valueobject.DemoValueObject;

/**
 * <p>
 * Represents a single product which can be ordered.
 * </p>
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since ivata masks 0.1 (2004-05-09)
 * @version $Revision: 1.9 $
 */
public final class ProductDO extends DemoValueObject {

    /**
     * Serialization version (for <code>Serializable</code> interface).
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(ProductDO.class);

    /**
     * <p>
     * Full text description of the product.
     * </p>
     */
    private String description;

    /**
     * <p>
     * Name of the product. Clear text name, should be unique, though this is
     * not enforced.
     * </p>
     */
    private String name;

    /**
     * <p>
     * Cost of each item of this product.
     * </p>
     */
    private BigDecimal price;

    /**
     * <p>
     * Construct a new product instance with no id.
     * </p>
     */
    public ProductDO() {
        super();
    }

    /**
     * <p>
     * Construct a new product instance with the given unique identifier.
     * </p>
     *
     * @param id
     *            unique identifier of this product.
     */
    public ProductDO(final int id) {
        super(id);
    }

    /**
     * The product's description is a long text designed to make you desperate
     * to own one immediately.
     *
     * @return long text designed to make you drool.
     */
    public String getDescription() {
        return description;
    }

    /**
     * For products, the value displayed is always the product's name.
     *
     * @return just returns the product's name.
     * @see com.ivata.mask.valueobject.ValueObject#getStringValue()
     */
    public String getDisplayValue() {
        return name;
    }

    /**
     * The name of a product is unique among products - that short text which
     * uniquely identifies it.
     *
     * @return unique name of this product.
     */
    public String getName() {
        return name;
    }

    /**
     * What price quality? This is the cost of a single unit of the product.
     *
     * @return cost of a single unit of the product.
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * The product's description is a long text designed to make you desperate
     * to own one immediately.
     *
     * @param descriptionParam long text designed to make you drool.
     */
    public void setDescription(final String descriptionParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Set description was '" + description + "', now '" + descriptionParam + "'");
        }
        description = descriptionParam;
    }

    /**
     * The name of a product is unique among products - that short text which
     * uniquely identifies it.
     *
     * @param nameParam unique name of this product.
     */
    public void setName(final String nameParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Set name was '" + name + "', now '" + nameParam + "'");
        }
        name = nameParam;
    }

    /**
     * Set the cost of one unit of this product.
     *
     * @param priceParam cost of a single unit of this product.
     */
    public void setPrice(final BigDecimal priceParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Set price was '" + price + "', now '" + priceParam + "'");
        }
        if ((price != null) && (priceParam == null)) {
            throw new NullPointerException();
        }
        price = priceParam;
    }
}
