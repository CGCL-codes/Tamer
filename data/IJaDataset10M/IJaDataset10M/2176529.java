package com.acv.common.model.bus;

import com.acv.common.model.BusRequestObject;
import com.acv.common.model.entity.PaymentInformation;

/**
 * <p>The AddPaymentRequest enables the requestor to get make a payment on
 * a Booking that has already been booked. Not all user may add payment to
 * every Booking, the user must be the owner of the target Booking.
 */
public interface AddPaymentRequest extends BusRequestObject {

    /**
     * Returns the Booking Number of the booking that will received this payment.
     * @return the Booking Number of the booking that will received this payment.
     */
    String getBookingNumber();

    /**
     * Returns the PaymentInformation structure that represent the payment to apply.
     * @return the PaymentInformation structure that represent the payment to apply.
     */
    PaymentInformation getPayment();
}
