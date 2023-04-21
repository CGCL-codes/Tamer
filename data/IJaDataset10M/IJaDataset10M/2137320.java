package com.nickokiss.investor.fin.env;

import java.math.BigDecimal;

/**
 * 
 * @author Tomasz Koscinski <tomasz.koscinski@nickokiss.com>
 */
public interface InterestRateStrategy {

    public BigDecimal getSpotRate(BigDecimal time);
}
