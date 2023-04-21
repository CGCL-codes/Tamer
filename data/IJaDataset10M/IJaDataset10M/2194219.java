package com.core.md;

import java.math.BigDecimal;
import javax.servlet.http.HttpSession;
import com.be.bo.GlobalParameter;

public class CommissionMD extends BaseMD {

    public CommissionMD(HttpSession session) {
        super(session);
    }

    public static BigDecimal getCommission(String type, BigDecimal amount) {
        if (GlobalParameter.commissionTypeDebitCard.equals(type)) {
            if (amount.doubleValue() > 50) {
                return new BigDecimal(0.28);
            } else {
                return new BigDecimal(0.19);
            }
        } else if (GlobalParameter.commissionTypeCreditCardVisa.equals(type)) {
            return amount.divide(new BigDecimal(100.00)).multiply(new BigDecimal(2.73));
        } else if (GlobalParameter.commissionTypeCreditCardMasterCard.equals(type)) {
            return amount.divide(new BigDecimal(100.00)).multiply(new BigDecimal(2.73));
        }
        return new BigDecimal(0.0);
    }
}
