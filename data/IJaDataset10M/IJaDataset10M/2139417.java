package com.acv.common.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Guy_acv
 * Date: 7-Nov-2007
 * Time: 4:51:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PaymentInformation extends Serializable {

    enum PaymentInfoType {

        CREDIT_CARD, CHECK, WRA_CHECK
    }

    float getAmount();

    Date getPaymentDate();

    PaymentInfoType getPaymentInfoType();
}
