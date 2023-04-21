package org.compiere.wstore;

import java.math.*;
import org.compiere.util.*;

/**
 *  Web Basket Line
 *
 *  @author Jorg Janke
 *  @version $Id: WebBasketLine.java,v 1.2 2006/07/30 00:53:21 jjanke Exp $
 */
public class WebBasketLine {

    /**
	 * 	Web Basket Line
	 * 	@param M_Product_ID product
	 * 	@param Name Name
	 *	@param Qty Qty
	 * 	@param Price Price
	 */
    public WebBasketLine(int M_Product_ID, String Name, BigDecimal Qty, BigDecimal Price) {
        setM_Product_ID(M_Product_ID);
        setName(Name);
        setQuantity(Qty);
        setPrice(Price);
    }

    private int m_line;

    private int m_M_Product_ID;

    private String m_Name;

    private BigDecimal m_Price;

    private BigDecimal m_Quantity;

    private BigDecimal m_Total;

    /**
	 * 	Extended String Representation
	 * 	@return info
	 */
    public String toStringX() {
        StringBuffer sb = new StringBuffer("WebBasketLine[");
        sb.append(m_line).append("-M_Product_ID=").append(m_M_Product_ID).append(",Qty=").append(m_Quantity).append(",Price=").append(m_Price).append(",Total=").append(getTotal()).append("]");
        return sb.toString();
    }

    /**
	 * 	Extended String Representation
	 * 	@return info
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(m_Quantity).append(" * ").append(m_Name).append(" = ").append(getTotal());
        return sb.toString();
    }

    /**
	 * 	Get Line number
	 *	@return line no
	 */
    public int getLine() {
        return m_line;
    }

    /**
	 * 	Set Line number
	 *	@param line no
	 */
    protected void setLine(int line) {
        m_line = line;
    }

    /**
	 * 	Get M_Product_ID product
	 *	@return product
	 */
    public int getM_Product_ID() {
        return m_M_Product_ID;
    }

    /**
	 * 	Set M_Product_ID
	 *	@param M_Product_ID id
	 */
    protected void setM_Product_ID(int M_Product_ID) {
        m_M_Product_ID = M_Product_ID;
    }

    /**
	 * 	Get Name
	 *	@return name or -?-
	 */
    public String getName() {
        if (m_Name == null) return "-?-";
        return m_Name;
    }

    /**
	 * 	Set Product Name
	 *	@param name
	 */
    protected void setName(String name) {
        m_Name = name;
    }

    /**
	 * 	Get Price
	 *	@return price
	 */
    public BigDecimal getPrice() {
        if (m_Price == null) return Env.ZERO;
        return m_Price;
    }

    /**
	 * 	Set Price
	 *	@param price
	 */
    protected void setPrice(BigDecimal price) {
        if (price == null) m_Price = Env.ZERO; else m_Price = price;
        m_Total = null;
    }

    /**
	 * 	Get Quantity
	 *	@return quantity
	 */
    public BigDecimal getQuantity() {
        if (m_Quantity == null) return Env.ZERO;
        return m_Quantity;
    }

    /**
	 * 	Set Quantity
	 *	@param quantity quantity
	 */
    public void setQuantity(BigDecimal quantity) {
        if (quantity == null) m_Quantity = Env.ZERO; else m_Quantity = quantity;
        m_Total = null;
    }

    /**
	 * 	Add Quantity
	 *	@param addedQuantity
	 *	@return new quantity
	 */
    public BigDecimal addQuantity(BigDecimal addedQuantity) {
        if (addedQuantity == null) return getQuantity();
        m_Quantity = getQuantity();
        m_Quantity = m_Quantity.add(addedQuantity);
        m_Total = null;
        return m_Quantity;
    }

    /**
	 * 	Get Total (calculate)
	 *	@return total price
	 */
    public BigDecimal getTotal() {
        if (m_Total == null) m_Total = getQuantity().multiply(getPrice());
        return m_Total;
    }
}
