package net.sf.brightside.travelsystem.service.facade.hibernate;

import net.sf.brightside.travelsystem.core.CreditCard;
import net.sf.brightside.travelsystem.core.persistence.PersistenceManager;
import net.sf.brightside.travelsystem.metamodel.Reservation;
import net.sf.brightside.travelsystem.service.facade.BookFlight;
import net.sf.brightside.travelsystem.service.facade.Payment;
import net.sf.brightside.travelsystem.service.facade.hibernate.exception.PaymentErrorException;
import net.sf.brightside.travelsystem.service.facade.hibernate.exception.ReservationValidationException;
import org.springframework.transaction.annotation.Transactional;

public class BookFlightImpl implements BookFlight {

    @SuppressWarnings("unchecked")
    private PersistenceManager persistenceManager;

    @SuppressWarnings("unchecked")
    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    @SuppressWarnings("unchecked")
    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    private Payment paymentService;

    public Payment getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(Payment paymentService) {
        this.paymentService = paymentService;
    }

    private boolean validateReservation(Reservation reservation) throws ReservationValidationException {
        if (reservation.getCabinClass() == null || reservation.getFlight() == null || reservation.getReservationTime() == null || reservation.getPassenger() == null || reservation.getSeat() == null) {
            throw new ReservationValidationException();
        }
        return true;
    }

    private void payForReservation(Reservation reservation, CreditCard creditCard) throws PaymentErrorException {
        paymentService.chargeReservation(reservation, creditCard);
    }

    @Override
    @Transactional
    public void bookFlight(Reservation reservation, CreditCard creditCard) throws PaymentErrorException, ReservationValidationException {
        validateReservation(reservation);
        payForReservation(reservation, creditCard);
        this.getPersistenceManager().saveOrUpdate(reservation);
    }
}
