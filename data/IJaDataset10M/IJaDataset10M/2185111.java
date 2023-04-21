package net.sf.ofx4j.domain.data.seclist;

import net.sf.ofx4j.meta.Aggregate;
import net.sf.ofx4j.meta.Element;
import java.util.Date;

/**
 * Info about a mutual fund security.
 * @see "Section 13.8.5.3, OFX Spec"
 *
 * @author Jon Perlow
 */
@Aggregate("MFINFO")
public class MutualFundSecurityInfo extends BaseSecurityInfo {

    private String mfType;

    private Double yield;

    private Date dateYieldAsOf;

    /**
   * Gets the mutual fund type. One of "OPENEND", "CLOSEEND", or "OTHER". This is an optional field
   * according to the OFX spec.
   *
   * @return the mutual fund type
   */
    @Element(name = "MFTYPE", order = 20)
    public String getType() {
        return mfType;
    }

    /**
   * Sets the mutual fund type. One of "OPENEND", "CLOSEEND", or "OTHER". This is an optional field
   * according to the OFX spec.
   *
   * @param mfType the mutual fund type
   */
    public void setType(String mfType) {
        this.mfType = mfType;
    }

    /**
   * Gets the mutual fund type as one of the well-known types.
   *
   * @return the mutual fund type or null if it's not one of the well-known types
   */
    public MutualFundType getTypeEnum() {
        return MutualFundType.fromOfx(getType());
    }

    /**
   * Gets the yield as a rate. This is an optional field according to the OFX spec.
   *
   * @return the yield as a rate
   */
    @Element(name = "YIELD", order = 30)
    public Double getYield() {
        return yield;
    }

    /**
   * Sets the yield as a rate. This is an optional field according to the OFX spec.
   *
   * @param yield the yield as a rate
   */
    public void setYield(Double yield) {
        this.yield = yield;
    }

    /**
   * Gets the as-of date for the yield. This is an optional field according to the OFX spec.
   *
   * @return the as-of date for the yield
   */
    @Element(name = "DTYIELDASOF", order = 40)
    public Date getDateYieldAsOf() {
        return dateYieldAsOf;
    }

    /**
   * Sets the as-of date for the yield. This is an optional field according to the OFX spec.
   *
   * @param dateYieldAsOf the as-of date for the yield
   */
    public void setDateYieldAsOf(Date dateYieldAsOf) {
        this.dateYieldAsOf = dateYieldAsOf;
    }
}
