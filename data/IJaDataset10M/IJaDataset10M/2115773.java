package com.google.checkout.schema._2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for CheckoutShoppingCart complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CheckoutShoppingCart">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="checkout-flow-support">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="merchant-checkout-flow-support" type="{http://checkout.google.com/schema/2}MerchantCheckoutFlowSupport"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="shopping-cart" type="{http://checkout.google.com/schema/2}ShoppingCart"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CheckoutShoppingCart", propOrder = {  })
public class CheckoutShoppingCart {

    @XmlElement(name = "checkout-flow-support", required = true)
    protected CheckoutShoppingCart.CheckoutFlowSupport checkoutFlowSupport;

    @XmlElement(name = "shopping-cart", required = true)
    protected ShoppingCart shoppingCart;

    /**
     * Gets the value of the checkoutFlowSupport property.
     * 
     * @return
     *     possible object is
     *     {@link CheckoutShoppingCart.CheckoutFlowSupport }
     *     
     */
    public CheckoutShoppingCart.CheckoutFlowSupport getCheckoutFlowSupport() {
        return checkoutFlowSupport;
    }

    /**
     * Sets the value of the checkoutFlowSupport property.
     * 
     * @param value
     *     allowed object is
     *     {@link CheckoutShoppingCart.CheckoutFlowSupport }
     *     
     */
    public void setCheckoutFlowSupport(CheckoutShoppingCart.CheckoutFlowSupport value) {
        this.checkoutFlowSupport = value;
    }

    /**
     * Gets the value of the shoppingCart property.
     * 
     * @return
     *     possible object is
     *     {@link ShoppingCart }
     *     
     */
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    /**
     * Sets the value of the shoppingCart property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShoppingCart }
     *     
     */
    public void setShoppingCart(ShoppingCart value) {
        this.shoppingCart = value;
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="merchant-checkout-flow-support" type="{http://checkout.google.com/schema/2}MerchantCheckoutFlowSupport"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "merchantCheckoutFlowSupport" })
    public static class CheckoutFlowSupport {

        @XmlElement(name = "merchant-checkout-flow-support")
        protected MerchantCheckoutFlowSupport merchantCheckoutFlowSupport;

        /**
         * Gets the value of the merchantCheckoutFlowSupport property.
         * 
         * @return
         *     possible object is
         *     {@link MerchantCheckoutFlowSupport }
         *     
         */
        public MerchantCheckoutFlowSupport getMerchantCheckoutFlowSupport() {
            return merchantCheckoutFlowSupport;
        }

        /**
         * Sets the value of the merchantCheckoutFlowSupport property.
         * 
         * @param value
         *     allowed object is
         *     {@link MerchantCheckoutFlowSupport }
         *     
         */
        public void setMerchantCheckoutFlowSupport(MerchantCheckoutFlowSupport value) {
            this.merchantCheckoutFlowSupport = value;
        }
    }
}
