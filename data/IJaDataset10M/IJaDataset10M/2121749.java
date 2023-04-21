package rgzm.bean;

import static rgzm.bean.TRAIN_STATE.AWAY_OUT;
import static rgzm.bean.TRAIN_STATE.IN_STATION_TRACK;
import static rgzm.bean.TRAIN_STATE.IN_TIMETABLE;
import rgzm.Main;
import rgzm.gui.View;
import base.time.TimeListener;
import base.time.TimeListener.TimeData;
import base.util.ModelTime;

public class Zug implements Comparable<Zug> {

    public Zug(final String nr, final ModelTime an, final ModelTime ab) {
        setZugNr(nr);
        setAn(an);
        setAb(ab);
    }

    public int compareTo(final Zug o) {
        ModelTime ab = getAb() != null ? getAb() : getAn();
        ModelTime vAb = o.getAb() != null ? o.getAb() : o.getAn();
        return ab.compareTo(vAb);
    }

    public String getZugNr() {
        return zugNr;
    }

    public void setZugNr(final String zugNr) {
        this.zugNr = zugNr;
        setGattung(zugNr.split(" ")[0]);
    }

    protected void setGattung(final String zugGattung) {
        this.zugGattung = zugGattung;
    }

    public String getGattung() {
        return zugGattung;
    }

    public ModelTime getAb() {
        return ab;
    }

    public void setAb(final ModelTime ab) {
        this.ab = ab;
    }

    public ModelTime getAn() {
        return an;
    }

    public void setAn(final ModelTime an) {
        this.an = an;
    }

    public String getGleis() {
        return gleis;
    }

    public void setGleis(final String gleis) {
        this.gleis = gleis;
    }

    public String getNach() {
        return nach;
    }

    public void setNach(final String nach) {
        this.nach = nach;
    }

    public String getVon() {
        return von;
    }

    public void setVon(final String von) {
        this.von = von;
    }

    public int getPlusAbMin() {
        return plusAbMin;
    }

    protected void setPlusAbMin(final int plusAbMin) {
        this.plusAbMin = plusAbMin;
    }

    public int getPlusAnMin() {
        return plusAnMin;
    }

    protected void setPlusAnMin(final int plusAnMin) {
        this.plusAnMin = plusAnMin;
    }

    public TRAIN_STATE getState() {
        return state;
    }

    public void setState(final TRAIN_STATE state) {
        noteDelay(state);
        TRAIN_STATE old = this.state;
        this.state = state;
        View v = getView();
        if (v != null) {
            v.notifyChanged(getState(), old);
        }
    }

    /**
	 * Zustand darf noch nicht umgesetzt sein für die Ermittlung!!
	 * 
	 * @param postState
	 */
    private void noteDelay(final TRAIN_STATE postState) {
        if (postState == IN_TIMETABLE) {
            plusAbMin = 0;
            plusAnMin = 0;
        } else if (postState == IN_STATION_TRACK && state != IN_TIMETABLE) {
            if (an != null) {
                plusAnMin = getDivInMinutes(an.getStunde(), an.getMinute());
            }
        } else if (postState == AWAY_OUT) {
            if (ab != null) {
                plusAbMin = getDivInMinutes(ab.getStunde(), ab.getMinute());
            }
        }
    }

    public int getDivInMinutes(final int hh, final int mm) {
        int diff = 0;
        if (lastTime != null) {
            int h = lastTime.getTime().getStunde();
            int m = lastTime.getTime().getMinute();
            diff = (h - hh) * 60 + m - mm;
        }
        return diff;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(getZugNr());
        buf.append("(");
        buf.append(getAn());
        buf.append("/");
        buf.append(getAb());
        buf.append("): ");
        buf.append(getGleis());
        buf.append("   von: ");
        buf.append(getVon());
        buf.append("   nach: ");
        buf.append(getNach());
        return buf.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Zug && getZugNr().equals(((Zug) obj).getZugNr());
    }

    @Override
    public int hashCode() {
        return getZugNr().hashCode();
    }

    public View getView() {
        return view;
    }

    public void setView(final View view) {
        this.view = view;
    }

    private String zugNr;

    private String zugGattung;

    private ModelTime an;

    private ModelTime ab;

    private String gleis;

    private String von;

    private String nach;

    private TRAIN_STATE state = TRAIN_STATE.IN_TIMETABLE;

    private View view;

    private int plusAbMin;

    private int plusAnMin;

    private static TimeData lastTime;

    private static Clock clock = new Clock();

    private static class Clock implements TimeListener {

        public Clock() {
            Main.CLOCK.addTimeListener(this);
        }

        public void pushTime(final TimeData timeData) {
            lastTime = timeData;
        }
    }
}
