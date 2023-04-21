package de.jaret.examples.timebars.touren;

import de.jaret.util.date.IntervalImpl;
import de.jaret.util.date.JaretDate;

public class TourElement extends IntervalImpl {

    String _beginOrt;

    String _endeOrt;

    int _typ;

    String _label;

    public TourElement(JaretDate begin, JaretDate end, String beginOrt, String endeOrt, int typ, String label) {
        setBegin(begin);
        setEnd(end);
        _beginOrt = beginOrt;
        _endeOrt = endeOrt;
        _typ = typ;
        _label = label;
    }

    /**
     * @return Returns the beginOrt.
     */
    public String getBeginOrt() {
        return _beginOrt;
    }

    /**
     * @return Returns the endeOrt.
     */
    public String getEndeOrt() {
        return _endeOrt;
    }

    /**
     * @return Returns the label.
     */
    public String getLabel() {
        return _label;
    }

    /**
     * @return Returns the typ.
     */
    public int getTyp() {
        return _typ;
    }
}
