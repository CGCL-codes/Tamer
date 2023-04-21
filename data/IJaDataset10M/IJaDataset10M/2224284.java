package com.etc.controller.beans.nullify;

import com.etc.controller.beans.*;
import java.math.BigDecimal;

/**
 *
 * @author magicbank
 */
public class NullUnitBeans extends UnitBeans {

    public NullUnitBeans() {
        super(0, 0, "", "", 0, "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
