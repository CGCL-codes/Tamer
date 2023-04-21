package org.surveyforge.core.metadata.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;

/**
 * @author jgonzalez
 */
@Entity
public class QuantityValueDomain extends AbstractValueDomain {

    private static final long serialVersionUID = 475119088217988415L;

    private int precision = 1;

    private int scale;

    private BigDecimal minimum = BigDecimal.valueOf(Integer.MIN_VALUE);

    private BigDecimal maximum = BigDecimal.valueOf(Integer.MAX_VALUE);

    ;

    protected QuantityValueDomain() {
    }

    /**
   * 
   */
    public QuantityValueDomain(int precision, int scale) {
        this.setPrecision(precision);
        this.setScale(scale);
    }

    /**
   * @return Returns the precision.
   */
    public int getPrecision() {
        return precision;
    }

    /**
   * @param precision The precision to set.
   */
    public void setPrecision(int precision) {
        if (precision > 0 && precision >= this.getScale()) this.precision = precision; else throw new IllegalArgumentException();
    }

    /**
   * @return Returns the scale.
   */
    public int getScale() {
        return scale;
    }

    /**
   * @param scale The scale to set.
   */
    public void setScale(int scale) {
        if (scale >= 0 && scale <= this.getPrecision()) this.scale = scale; else throw new IllegalArgumentException();
    }

    /**
   * @return Returns the minimum.
   */
    public BigDecimal getMinimum() {
        return this.minimum;
    }

    /**
   * @param minimum The minimum to set.
   */
    public void setMinimum(BigDecimal minimum) {
        if (minimum.compareTo(this.getMaximum()) < 0) this.minimum = minimum; else throw new IllegalArgumentException();
    }

    /**
   * @return Returns the maximum.
   */
    public BigDecimal getMaximum() {
        return this.maximum;
    }

    /**
   * @param maximum The maximum to set.
   */
    public void setMaximum(BigDecimal maximum) {
        if (this.getMinimum().compareTo(maximum) < 0) this.maximum = maximum; else throw new IllegalArgumentException();
    }

    public boolean isValid(Serializable object) {
        if (object instanceof Integer) {
            BigDecimal quantity = new BigDecimal((Integer) object);
            return quantity.scale() <= this.getScale() && (quantity.precision() - quantity.scale()) <= (this.getPrecision() - this.getScale()) && this.getMinimum().compareTo(quantity) <= 0 && quantity.compareTo(this.getMaximum()) <= 0;
        } else if (object instanceof BigDecimal) {
            BigDecimal quantity = (BigDecimal) object;
            return quantity.scale() <= this.getScale() && (quantity.precision() - quantity.scale()) <= (this.getPrecision() - this.getScale()) && this.getMinimum().compareTo(quantity) <= 0 && quantity.compareTo(this.getMaximum()) <= 0;
        } else return false;
    }

    @Override
    public QuantityValueDomain clone() {
        QuantityValueDomain copy = (QuantityValueDomain) super.clone();
        return copy;
    }
}
