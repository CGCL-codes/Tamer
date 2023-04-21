package net.sf.chellow.physical;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import net.sf.chellow.monad.ProgrammerException;
import net.sf.chellow.monad.UserException;
import net.sf.chellow.monad.types.MonadInteger;

public class ProfileClassCode extends MonadInteger {

    public ProfileClassCode() {
        init();
    }

    public ProfileClassCode(int code) throws UserException, ProgrammerException {
        this(null, code);
    }

    public ProfileClassCode(String label, int code) throws UserException, ProgrammerException {
        init();
        setLabel(label);
        update(code);
    }

    private void init() {
        setTypeName("ProfileClassCode");
        setMaximum(8);
        setMinimum(0);
    }

    public void update(String code) throws UserException, ProgrammerException {
        NumberFormat profileClassCodeFormat = NumberFormat.getIntegerInstance(Locale.UK);
        profileClassCodeFormat.setMinimumIntegerDigits(2);
        super.update(profileClassCodeFormat.format(Integer.parseInt(code.trim())));
    }

    public Attr toXML(Document doc) {
        Attr attr = doc.createAttribute("code");
        attr.setValue(toString());
        return attr;
    }

    public String toString() {
        DecimalFormat pcFormat = new DecimalFormat("00");
        return pcFormat.format(getInteger());
    }
}
