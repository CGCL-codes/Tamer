package net.sourceforge.processdash.data.applet;

import net.sourceforge.processdash.data.DoubleData;
import net.sourceforge.processdash.data.MalformedValueException;
import net.sourceforge.processdash.data.repository.Repository;
import net.sourceforge.processdash.util.FormatUtil;

public class PercentInterpreter extends DoubleInterpreter {

    public PercentInterpreter(Repository r, String name, int numDigits, boolean readOnly) {
        super(r, name, numDigits, readOnly);
    }

    public String getString() {
        if (value instanceof DoubleData && value.isDefined()) return FormatUtil.formatPercent(((DoubleData) value).getDouble(), numDigits); else return super.getString();
    }

    public void setString(String s) throws MalformedValueException {
        try {
            value = new DoubleData(FormatUtil.parsePercent(s.trim()), true);
        } catch (Exception e) {
            throw new MalformedValueException();
        }
    }
}
