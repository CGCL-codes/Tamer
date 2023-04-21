package org.fao.waicent.xmap2D.coordsys;

import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.JTextField;

public class NumericField extends JTextField {

    protected static DecimalFormat _eformat = new DecimalFormat("#.#####E0");

    protected double _maxValue;

    protected double _minValue;

    protected NumberFormat _format;

    public double validation() throws RangeValidationException, InvalidDataException {
        double d;
        try {
            d = _format.parse(getText()).doubleValue();
        } catch (Exception exception) {
            throw new InvalidDataException();
        }
        if (d < _minValue || d > _maxValue) {
            throw new RangeValidationException();
        } else {
            return d;
        }
    }

    public void setRange(double d, double d1) {
        _minValue = d;
        _maxValue = d1;
    }

    public void setValue(double d) {
        if (d > 10000000000D || d < -10000000000D) {
            setText(_eformat.format(d));
        } else {
            setText(_format.format(d));
        }
    }

    public double getValue() {
        double d = 0.0D;
        try {
            if (getText().equals("-")) {
                return 0.0D;
            }
            d = _format.parse(getText()).doubleValue();
        } catch (ParseException parseexception) {
            Toolkit.getDefaultToolkit().beep();
        }
        return d;
    }

    public NumericField(double d, int i, NumberFormat numberformat) {
        super(i);
        _maxValue = 1E+030D;
        _minValue = -1E+030D;
        setDocument(new FormattedDocument(numberformat));
        _format = numberformat;
        setValue(d);
    }
}
