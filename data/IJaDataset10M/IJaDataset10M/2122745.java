package khl.ooo.util;

import java.util.Calendar;

public class TijdsIntervalWeekdag extends TijdsInterval implements Cloneable {

    public TijdsIntervalWeekdag(int dagNummer, int vanUur, int vanMinuten, int totUur, int totMinuten) {
        super(Calendar.getInstance(), Calendar.getInstance());
        this.setDagNummer(dagNummer);
        this.setVanUur(vanUur);
        this.setVanMinuten(vanMinuten);
        this.setTotUur(totUur);
        this.setTotMinuten(totMinuten);
        if (this.getLengteInMinuten() < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void setDagNummer(int dagnummer) {
        if (dagnummer >= 1 && dagnummer <= 7) {
            this.getVanMoment().set(Calendar.DAY_OF_WEEK, dagnummer + 1);
            this.getTotMoment().set(Calendar.DAY_OF_WEEK, dagnummer + 1);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int getDagNummer() {
        return this.getVanMoment().get(Calendar.DAY_OF_WEEK) - 1;
    }

    public int getVanUur() {
        return this.getVanMoment().get(Calendar.HOUR_OF_DAY);
    }

    public int getVanMinuten() {
        return this.getVanMoment().get(Calendar.MINUTE);
    }

    public void setVanUur(int uur) {
        if (uur >= 0 && uur <= 23) {
            this.getVanMoment().set(Calendar.HOUR_OF_DAY, uur);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setVanMinuten(int minuten) {
        if (minuten >= 0 && minuten <= 59) {
            if (this.getVanUur() == this.getTotUur()) {
                if (this.getTotMinuten() >= minuten) {
                    this.getVanMoment().set(Calendar.MINUTE, minuten);
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int getTotUur() {
        return this.getTotMoment().get(Calendar.HOUR_OF_DAY);
    }

    public int getTotMinuten() {
        return this.getTotMoment().get(Calendar.MINUTE);
    }

    public void setTotUur(int uur) {
        if (uur >= 0 && uur <= 23) {
            this.getTotMoment().set(Calendar.HOUR_OF_DAY, uur);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setTotMinuten(int minuten) {
        if (minuten >= 0 && minuten <= 59) {
            this.getTotMoment().set(Calendar.MINUTE, minuten);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Calendar getVanMoment() {
        return super.getVanMoment();
    }

    public Calendar getTotMoment() {
        return super.getTotMoment();
    }

    public String getDagNaam() {
        Weekdagen[] dagen = Weekdagen.values();
        return dagen[this.getDagNummer() - 1].getName();
    }

    public String toString() {
        String output = "";
        output += this.getDagNaam() + " ";
        if (this.getVanUur() < 10) output += "0";
        output += this.getVanUur() + ":";
        if (this.getVanMinuten() < 10) output += "0";
        output += this.getVanMinuten() + " - ";
        if (this.getTotUur() < 10) output += "0";
        output += this.getTotUur() + ":";
        if (this.getTotMinuten() < 10) output += "0";
        output += this.getTotMinuten();
        return output;
    }
}
