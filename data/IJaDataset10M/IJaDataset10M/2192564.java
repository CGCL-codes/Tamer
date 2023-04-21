package com.f2ms.service.booking;

import java.util.Date;
import java.util.List;
import com.f2ms.dao.DAOFactory;
import com.f2ms.exception.DAOException;
import com.f2ms.model.Booking;
import com.f2ms.model.BookingFiles;

public class BookingServiceImpl implements IBookingService {

    private DAOFactory daoFactory;

    public void setDaoFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Booking createBooking(Booking booking) throws DAOException {
        Booking created = daoFactory.getBookingDAO().create(booking);
        String bookingNo = created.getDocNo();
        for (BookingFiles files : booking.getListFiles()) {
            files.setBookingNo(bookingNo);
            daoFactory.getBookingFilesDAO().createBookingFile(files);
        }
        return created;
    }

    @Override
    public Booking editBooking(Booking booking) throws DAOException {
        Booking edited = daoFactory.getBookingDAO().edit(booking);
        String bookingNo = edited.getDocNo();
        for (BookingFiles files : booking.getListFiles()) {
            files.setBookingNo(bookingNo);
            daoFactory.getBookingFilesDAO().createBookingFile(files);
        }
        return edited;
    }

    @Override
    public List<Booking> findAllBooking() throws DAOException {
        return daoFactory.getBookingDAO().findAllBooking();
    }

    @Override
    public Booking findBookingById(String bookingNo) throws DAOException {
        Booking booking = daoFactory.getBookingDAO().findBookingById(bookingNo);
        booking.setShipperName(booking.getShipperObj().getName());
        booking.setConsigneeName(booking.getConsigneeObj().getName());
        booking.setPlaceOfReceiptDesc(booking.getPlaceOfReceiptObj().getDescription());
        booking.setPolDesc(booking.getPolObj().getDescription());
        booking.setPodDesc(booking.getPodObj().getDescription());
        booking.setFinalDestinationDesc(booking.getFinalDestinationObj().getDescription());
        List<BookingFiles> files = daoFactory.getBookingFilesDAO().findFilesByBookingNo(bookingNo);
        booking.setListFiles(files);
        return booking;
    }

    @Override
    public List<Booking> findBookingByCriteria(Booking booking) throws DAOException {
        return daoFactory.getBookingDAO().findBookingByCriteria(booking);
    }

    @Override
    public List<Booking> findAvailableBooking(Date startPeriod, Date endPeriod, Long pol, Long pod, Long customerCode) throws DAOException {
        return daoFactory.getBookingDAO().findAvailableBooking(startPeriod, endPeriod, pol, pod, customerCode);
    }

    @Override
    public BookingFiles createBookingFiles(BookingFiles bookingFiles) throws DAOException {
        return daoFactory.getBookingFilesDAO().createBookingFile(bookingFiles);
    }

    @Override
    public List<BookingFiles> findBookingFilesByBookingNo(String bookingNo) throws DAOException {
        return daoFactory.getBookingFilesDAO().findFilesByBookingNo(bookingNo);
    }

    @Override
    public void deleteBookingFiles(BookingFiles bookingFile) throws DAOException {
        daoFactory.getBookingFilesDAO().deleteBookingFile(bookingFile);
    }

    @Override
    public BookingFiles findBookingFileById(Long id) throws DAOException {
        return daoFactory.getBookingFilesDAO().findById(id);
    }

    @Override
    public Booking editBookingOnly(Booking booking) throws DAOException {
        return daoFactory.getBookingDAO().edit(booking);
    }
}
