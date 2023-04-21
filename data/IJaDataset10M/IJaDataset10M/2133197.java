package org.openXpertya.wstore;

import java.math.BigDecimal;
import org.openXpertya.util.Env;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class WebBasketLine {

    /**
     * Constructor de la clase ...
     *
     *
     * @param M_Product_ID
     * @param Name
     * @param Qty
     * @param Price
     */
    public WebBasketLine(int M_Product_ID, String Name, BigDecimal Qty, BigDecimal Price) {
        setM_Product_ID(M_Product_ID);
        setName(Name);
        setQuantity(Qty);
        setPrice(Price);
    }

    /** Descripción de Campos */
    private int m_line;

    /** Descripción de Campos */
    private int m_M_Product_ID;

    /** Descripción de Campos */
    private String m_Name;

    /** Descripción de Campos */
    private BigDecimal m_Price;

    /** Descripción de Campos */
    private BigDecimal m_Quantity;

    /** Descripción de Campos */
    private BigDecimal m_Total;

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String toStringX() {
        StringBuffer sb = new StringBuffer("WebBasketLine[");
        sb.append(m_line).append("-M_Product_ID=").append(m_M_Product_ID).append(",Qty=").append(m_Quantity).append(",Price=").append(m_Price).append(",Total=").append(getTotal()).append("]");
        return sb.toString();
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(m_Quantity).append(" * ").append(m_Name).append(" = ").append(getTotal());
        return sb.toString();
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public int getLine() {
        return m_line;
    }

    /**
     * Descripción de Método
     *
     *
     * @param line
     */
    protected void setLine(int line) {
        m_line = line;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public int getM_Product_ID() {
        return m_M_Product_ID;
    }

    /**
     * Descripción de Método
     *
     *
     * @param M_Product_ID
     */
    protected void setM_Product_ID(int M_Product_ID) {
        m_M_Product_ID = M_Product_ID;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getName() {
        if (m_Name == null) {
            return "-?-";
        }
        return m_Name;
    }

    /**
     * Descripción de Método
     *
     *
     * @param name
     */
    protected void setName(String name) {
        m_Name = name;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public BigDecimal getPrice() {
        if (m_Price == null) {
            return Env.ZERO;
        }
        return m_Price;
    }

    /**
     * Descripción de Método
     *
     *
     * @param price
     */
    protected void setPrice(BigDecimal price) {
        if (price == null) {
            m_Price = Env.ZERO;
        } else {
            m_Price = price;
        }
        m_Total = null;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public BigDecimal getQuantity() {
        if (m_Quantity == null) {
            return Env.ZERO;
        }
        return m_Quantity;
    }

    /**
     * Descripción de Método
     *
     *
     * @param quantity
     */
    public void setQuantity(BigDecimal quantity) {
        if (quantity == null) {
            m_Quantity = Env.ZERO;
        } else {
            m_Quantity = quantity;
        }
        m_Total = null;
    }

    /**
     * Descripción de Método
     *
     *
     * @param addedQuantity
     *
     * @return
     */
    public BigDecimal addQuantity(BigDecimal addedQuantity) {
        if (addedQuantity == null) {
            return getQuantity();
        }
        m_Quantity = getQuantity();
        m_Quantity = m_Quantity.add(addedQuantity);
        m_Total = null;
        return m_Quantity;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public BigDecimal getTotal() {
        if (m_Total == null) {
            m_Total = getQuantity().multiply(getPrice());
        }
        return m_Total;
    }
}
