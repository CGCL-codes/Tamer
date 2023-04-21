package kino.Verwaltung;

import java.util.*;
import java.io.*;

/**
 Entspricht einem Programmeintrag an einem Tag im Leben eines Kino's und sollte als
 Element von Tagesprogramm verwendet werden.
 @author Stephan G&ouml;tter, <a href="mailto:sg17@irz.inf.tu-dresden.de">sg17@irz.inf.tu-dresden.de</a>
*/
public class ProgrammEintrag extends Element {

    protected int SaalIndex;

    protected Date FilmStart;

    protected Date FilmEnde;

    protected int FilmIndex;

    /**
     Dieser Konstruktor erzeugt eine Klasse ProgrammEintrag.
    */
    public ProgrammEintrag(int saalindex, Date start, Date ende, int filmindex) {
        SaalIndex = saalindex;
        FilmStart = start;
        FilmEnde = ende;
        FilmIndex = filmindex;
    }

    /**
     Dieser Konstruktor erzeugt eine Klasse ProgrammEintrag.
    */
    public ProgrammEintrag() {
    }

    /**
     Diese Methode liefert den SaalIndex.
    */
    public int SaalIndex() {
        return SaalIndex;
    }

    /**
     Diese Methode liefert den FilmIndex.
    */
    public int FilmIndex() {
        return FilmIndex;
    }

    /**
     Diese Methode liefert den Zeitpunkt, wann der Film beginnt.
    */
    public Date FilmStart() {
        return FilmStart;
    }

    /**
     Diese Methode liefert den Zeitpunkt, wann der Film endet.
    */
    public Date FilmEnde() {
        return FilmEnde;
    }

    /**
     Diese Methode setzt den SaalIndex.
    */
    public void setzeSaalIndex(int si) {
        SaalIndex = si;
    }

    /**
     Diese Methode setzt den FilmIndex.
    */
    public void setzeFilmIndex(int fi) {
        FilmIndex = fi;
    }

    /**
     Diese Methode setzt den Zeitpunkt, wann der Film beginnt.
    */
    public void setzeFilmStart(Date fs) {
        FilmStart = fs;
    }

    /**
     Diese Methode setzt den Zeitpunkt, wann der Film endet.
    */
    public void setzeFilmEnde(Date fe) {
        FilmEnde = fe;
    }

    /**
     Diese Methode l&auml;dt das Object aus dem Stream.
     Element.ladeAusStream() wird nicht aufgerufen.
    */
    public void ladeAusStream(ObjectInputStream s) {
        try {
            SaalIndex = s.readInt();
            FilmStart = (Date) s.readObject();
            FilmEnde = (Date) s.readObject();
            FilmIndex = s.readInt();
        } catch (Exception e) {
        }
    }

    /**
     Diese Methode speichert das Object in dem Stream.
     Element.speichereInStream() wird nicht aufgerufen.
    */
    public void speichereInStream(ObjectOutputStream s) {
        try {
            s.writeInt(SaalIndex);
            s.writeObject(FilmStart);
            s.writeObject(FilmEnde);
            s.writeInt(FilmIndex);
        } catch (Exception e) {
        }
    }

    /**
     Diese Methode liefert einen String zur&uuml;ck, und wird von fuelleListe aufgerufen.
    */
    public String toString() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(FilmStart);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        if (h < 10) {
            if (m < 10) return "0" + h + ":0" + m; else return "0" + h + ":" + m;
        } else {
            if (m < 10) return "" + h + ":0" + m; else return "" + h + ":" + m;
        }
    }

    /**
     Diese Methode erzeugt einen einzigartigen String f&uuml;r den Programmeintrag.
    */
    public String zuString() {
        return "" + FilmIndex + "" + SaalIndex + FilmStart();
    }
}
