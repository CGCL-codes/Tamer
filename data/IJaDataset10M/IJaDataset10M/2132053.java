package mbis.service;

import mbis.entity.PriceCategory;
import mbis.entity.Reservation;
import mbis.entity.lesson.RealLesson;
import mbis.persistence.LessonDAO;
import mbis.persistence.ReservationDAO;
import mbis.service.reservation.*;
import mbis.test.AbstractIntegrationTest;
import mbis.utils.CalendarUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * User: tmichalec
 * Date: Nov 3, 2008
 * Time: 4:58:50 PM
 */
public class ReservationServiceTest extends AbstractIntegrationTest {

    @Resource
    private LessonDAO lessonDAO;

    @Resource
    private ReservationDAO reservationDAO;

    @Resource
    private ReservationService reservationService;

    private RealLesson lesson;

    private static final String TEST_RATE = "Test rate";

    @BeforeClass
    public void before() {
        Locale.setDefault(new Locale("sk"));
        lesson = new RealLesson();
        lesson.setCapacity(3);
        lesson.setFrom(CalendarUtils.createDateAndRoll(new Date(), Calendar.MONTH, 1));
        lesson.setDuration(45);
        lesson.addLessonPrice(PriceCategory.BASE, BigDecimal.valueOf(90.0));
        lesson.addLessonPrice(PriceCategory.DISCOUNT, BigDecimal.valueOf(70.0));
        lesson.setLabel("lekcia");
        lessonDAO.makePersistent(lesson);
    }

    @Test
    public void testReservation() throws NotEnoughLessonCapacityException, LockedLessonException, QuantityMustBePositiveException {
        Reservation reservation = new Reservation();
        reservation.setLesson(lesson);
        reservation.setQuantity(2);
        reservationService.reserve(reservation);
        List<Reservation> reservations = reservationDAO.findAllActiveReservationForLesson(lesson.getId());
        Assert.assertEquals(reservations.size(), 1);
        Assert.assertEquals(reservation, reservations.get(0));
    }

    @Test
    public void testReservationEnoughCapacity() throws LockedLessonException, QuantityMustBePositiveException {
        Reservation reservation = new Reservation();
        reservation.setLesson(lesson);
        reservation.setQuantity(2);
        Reservation reservation2 = new Reservation();
        reservation2.setLesson(lesson);
        reservation2.setQuantity(2);
        List<Reservation> list = new ArrayList<Reservation>();
        list.add(reservation);
        list.add(reservation2);
        try {
            reservationService.reserve(list);
        } catch (NotEnoughLessonCapacityException e) {
            Assert.assertEquals(e.getCapacity(), 3);
            Assert.assertEquals(e.getNeeded(), 2);
            Assert.assertEquals(e.getUsed(), 2);
        }
    }

    @Test
    public void testCancelReservation() throws NotEnoughLessonCapacityException, CancelReservationException, LockedLessonException, QuantityMustBePositiveException {
        Reservation reservation = new Reservation();
        reservation.setLesson(lesson);
        reservation.setQuantity(2);
        reservationService.reserve(reservation);
        reservationService.cancel(reservation.getId(), Reservation.CancelledReason.CANCELLED_BY_CUSTOMER);
    }

    @Test(expectedExceptions = CancelReservationException.class)
    public void testCancelClosedReservation() throws NotEnoughLessonCapacityException, CancelReservationException, LockedLessonException, QuantityMustBePositiveException {
        Reservation reservation = new Reservation();
        reservation.setLesson(lesson);
        reservation.setQuantity(2);
        reservationService.reserve(reservation);
        try {
            reservationService.cancel(reservation.getId(), Reservation.CancelledReason.CANCELLED_BY_CUSTOMER);
        } catch (CancelReservationException e) {
            Assert.fail();
        }
        reservationService.cancel(reservation.getId(), Reservation.CancelledReason.CANCELLED_BY_CUSTOMER);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCancelInvalidReservation() throws CancelReservationException {
        reservationService.cancel((long) 0, Reservation.CancelledReason.CANCELLED_BY_CUSTOMER);
    }

    @Test
    public void testRateReservation() throws CancelReservationException {
        Reservation reservation = new Reservation();
        reservation.setLesson(lesson);
        reservationDAO.makePersistent(reservation);
        reservationService.rate(reservation.getId(), TEST_RATE);
        Assert.assertEquals(TEST_RATE, reservation.getNote());
    }
}
