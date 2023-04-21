package net.sf.brightside.travelsystem.service.facade;

import net.sf.brightside.travelsystem.core.CreditCard;
import net.sf.brightside.travelsystem.metamodel.Reservation;
import net.sf.brightside.travelsystem.service.facade.hibernate.exception.PaymentErrorException;

public interface Payment {

    void chargeReservation(Reservation reservation, CreditCard card) throws PaymentErrorException;
}
