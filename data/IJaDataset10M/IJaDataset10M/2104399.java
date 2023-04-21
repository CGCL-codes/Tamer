package CinemaGuide;

import Kino;
import Sale.*;
import MovieHallManagement.*;
import FilmDistributors.*;
import Timer.*;
import java.util.Enumeration;

/** enth�lt Auslastungen der Filme */
public class MovieStatus {

    private static final String NAME = "Auslastung der Filme";

    private StoringStock ss;

    /** neue Auslastungen (Katalog) anlegen */
    public MovieStatus() {
        if (Kino.existsGlobalStock(NAME)) ss = (StoringStock) Stock.forName(NAME); else {
            ss = new StoringStock(NAME, Kino.getMovies().getCatalog());
            Stock.addGlobalStock(ss);
        }
    }

    /** sucht Auslastung zum Film movie am Tag date */
    public StatusMovies findStatusMovies(String movie, String date) {
        Enumeration e = null;
        try {
            e = ss.getObjects(movie);
        } catch (NoSuchElementException ex) {
            System.out.println("keine Eintr�ge vorhanden in public StatusMovies findStatusMovies(String key,String date) - StatusMovies.java");
        }
        StatusMovies sms = null;
        if (e != null) {
            int counter = 0;
            do {
                try {
                    sms = (StatusMovies) e.nextElement();
                    counter++;
                } catch (java.util.NoSuchElementException exc) {
                    System.out.println("Kein Eintrag vorhanden im Status der Filme mit Datum : " + date);
                }
            } while ((!sms.getDate().equals(date)) && (counter < ss.countObjects(movie)));
        }
        if (sms != null) if (!sms.getDate().equals(date)) sms = null;
        return sms;
    }

    /** - reserviert Bereich zwischen begin und end <br>
      - am Tag date im Kinosaal movieHall <br>
      - return - Erfolg d. Resevierung true sonst false */
    public boolean reserve(int begin, int end, String date, String movie, String movieHall) {
        boolean isSuccesful = true;
        Catalog c = ss.getCatalog();
        CatalogItem ci = c.findKey(movie);
        StatusMovies sms = findStatusMovies(movie, date);
        if (sms == null) {
            System.out.println("solch ein Eintrag nicht vorhanden");
            sms = new StatusMovies(ci, date);
            ss.addItem(sms);
        }
        StoringStock stst = sms.getStatusStock();
        MovieHallManagement.MovieHall mh = (MovieHallManagement.MovieHall) stst.getCatalog().findKey(movieHall);
        StatusMovie sm = sms.findStatusMovie(movieHall);
        if (sm == null) {
            sm = new StatusMovie(mh);
            isSuccesful = sm.setReserved(begin, end);
            stst.addItem(sm);
        } else {
            isSuccesful = sm.setReserved(begin, end);
        }
        return isSuccesful;
    }

    /** l�scht Reservierung f�r Bereich zwischen begin und end am Tag date im Kinosaal movieHall */
    public void unreserve(int begin, int end, String date, String movie, String movieHall) {
        StatusMovies sms = findStatusMovies(movie, date);
        StatusMovie sm = sms.findStatusMovie(movieHall);
        sm.setUnreserved(begin, end);
    }

    /** - f�gt eine neue Auslastung zu <br>
      - String[] s - Text der InputBar <br>
      - return false, wenn Fehler aufgetreten -> �berschneidung <br>
      - sonst true */
    public boolean addEntry(String[] s) {
        String movie = s[0];
        String date = s[1];
        String time = s[2];
        String movieHall = s[3];
        int breakLength = new Integer(s[4]).intValue();
        boolean isSuccesful = true;
        int z = 0;
        int length = ((Movie) Kino.getMovies().getCatalog().findKey(movie)).getLength() + breakLength;
        int begin = Kino.getCinemaGuide().getHours(time) * 60 + Kino.getCinemaGuide().getMinutes(time);
        int end = begin + length;
        int rest = 0;
        if (end >= 24 * 60) rest = length - ((24 * 60) - begin);
        if (rest == 0) {
            isSuccesful = reserve(begin, end, date, movie, movieHall);
            z = countMovies(movie, date);
            Kino.getDistributedMovies().setDistributedMovies(date, movie, z);
        } else {
            reserve(begin, 24 * 60, date, movie, movieHall);
            String nextDate = Kino.getTimer().getNextDate(date);
            reserve(0, rest, nextDate, movie, movieHall);
            z = countMovies(movie, nextDate);
            Kino.getDistributedMovies().setDistributedMovies(date, movie, z);
        }
        System.out.println(z);
        return isSuccesful;
    }

    /** - l�scht eine Auslastung <br>
      - String[] s - Text der InputBar */
    public void deleteEntry(String[] s) {
        String movie = s[0];
        String date = s[1];
        String time = s[2];
        String movieHall = s[3];
        int breakLength = new Integer(s[4]).intValue();
        int z = 0;
        int length = ((Movie) Kino.getMovies().getCatalog().findKey(movie)).getLength() + breakLength;
        int begin = Kino.getCinemaGuide().getHours(time) * 60 + Kino.getCinemaGuide().getMinutes(time);
        int end = begin + length;
        int rest = 0;
        if (end >= 24 * 60) rest = length - ((24 * 60) - begin);
        if (rest == 0) {
            unreserve(begin, end, date, movie, movieHall);
            z = countMovies(movie, date);
            Kino.getDistributedMovies().setDistributedMovies(date, movie, z);
        } else {
            unreserve(begin, 24 * 60, date, movie, movieHall);
            String nextDate = Kino.getTimer().getNextDate(date);
            unreserve(0, rest, nextDate, movie, movieHall);
            z = countMovies(movie, nextDate);
            Kino.getDistributedMovies().setDistributedMovies(date, movie, z);
        }
        System.out.println(z);
    }

    /** z�hlt Anzahl gleicher Filme, die notwendig ist um Film am Datum date in allen Kinos�len gleichzeitig zeigen zu k�nnen */
    public int countMovies(String movie, String date) {
        int maxCounter = 0;
        int counter = 0;
        StatusMovies sms = findStatusMovies(movie, date);
        if (sms != null) {
            for (int i = 0; i < 24 * 60; i++) {
                counter = sms.countMovies(i);
                if (i == 0) maxCounter = counter; else {
                    if (counter > maxCounter) maxCounter = counter;
                }
            }
        }
        return maxCounter;
    }

    /** l�scht alle Auslastungen */
    public void delAllItems() {
        Enumeration e = ss.elements();
        while (e.hasMoreElements()) {
            StockItem si = (StockItem) e.nextElement();
            ss.deleteItem(si);
        }
    }

    /** liefert zugrundeliegenden Stock */
    public StoringStock getStock() {
        return ss;
    }
}
