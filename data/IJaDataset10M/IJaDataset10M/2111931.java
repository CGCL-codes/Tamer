package elib;

import entity.Author;
import entity.Book;
import entity.Comment;
import entity.Publisher;
import entity.Reservation;
import entity.User;
import entity.enumeration.BookState;
import entity.enumeration.UserRole;
import exception.CustomerException;
import exception.TestException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import managers.AdminManagerBean;
import managers.AdminManagerRemote;
import managers.CustomerManagerBean;
import managers.CustomerManagerRemote;
import managers.GuestManagerBean;
import managers.GuestManagerRemote;
import managers.LibrarienBean;
import managers.LibrarienRemote;

/**
 *
 * @author xvanac
 */
public class Main {

    @EJB
    private static CustomerManagerRemote customerManager;

    @EJB
    private static AdminManagerRemote adminManager;

    @EJB
    private static GuestManagerRemote guestManager;

    @EJB
    private static LibrarienRemote librarien;

    public static void main(String[] args) {
        if (guestManager == null) {
            System.err.println("GUEST je NULL");
        } else {
            System.err.println("guest úspěšně injektován");
        }
        System.err.println("Všechny knihy");
        for (Book book : guestManager.getAllBooks()) {
            System.out.println("\t * " + book);
        }
        System.out.println();
        System.err.println("Všechny knihy dle ISBN 5665-5-5-55");
        for (Book book : guestManager.getBooksWithISBN("5665-5-5-55")) {
            System.out.println("\t * " + book);
        }
        System.out.println();
        System.err.println("Všechny knihy od Otesánka");
        for (Book book : guestManager.getBooksWithPublisher(18l)) {
            System.out.println("\t * " + book);
        }
        System.out.println();
        System.err.println("Všechny knihy od Bezručové");
        for (Book book : guestManager.getBooksWithAuthor(3l)) {
            System.out.println("\t * " + book);
        }
        System.out.println();
        System.err.println("Komentáře od Koblížka");
        for (Comment comment : guestManager.getCommentsFromUser(21l)) {
            System.out.println("\t * " + comment);
        }
        System.out.println();
        System.err.println("Komentáře ke knize Pusík");
        for (Comment comment : guestManager.getCommentsAboutBook("5665-5-5-55")) {
            System.out.println("\t * " + comment);
        }
        System.out.println();
        System.err.println("Komentáře s hodnocenim 1 a 2");
        for (Comment comment : guestManager.getCommentsWithRatingFromTo(1, 2)) {
            System.out.println("\t * " + comment);
        }
        System.out.println();
        System.err.println("Průměr pro knihu Pusík:" + guestManager.getAveragegeRatingAbouBook("5665-5-5-55"));
        System.err.println("Počet  komentářů pro Pusíka:" + guestManager.getCommentCountAbouBook("5665-5-5-55"));
        System.err.println("Všechny rezervace");
        for (Reservation reservation : adminManager.getAllReservations()) {
            System.out.println("\t * " + reservation);
        }
        System.err.println("Všechny rezervace od Hliníka");
        for (Reservation reservation : customerManager.getReservationsOfUser(23l)) {
            System.out.println("\t * " + reservation);
        }
        System.out.println();
        System.err.println("Všechny rezervace knihy 15");
        for (Reservation reservation : adminManager.getReservationsOfBook(15l)) {
            System.out.println("\t * " + reservation);
        }
        System.out.println();
        System.err.println("Všechny rezervace od ted do ");
        for (Reservation reservation : adminManager.getReservationsFromToDate(new Date(), new Date(110, 12, 07))) {
            System.out.println("\t * " + reservation);
        }
        System.out.println();
    }
}
