package net.sf.chellow.physical;

import java.text.NumberFormat;
import java.util.Locale;
import net.sf.chellow.monad.ProgrammerException;
import net.sf.chellow.monad.UserException;
import net.sf.chellow.monad.types.MonadString;

public class DsoCode extends MonadString {

    public DsoCode() {
        setTypeName("dso-code");
        setMaximumLength(2);
        onlyDigits = true;
    }

    public DsoCode(String code) throws UserException, ProgrammerException {
        this(null, code);
    }

    public DsoCode(String label, String code) throws UserException, ProgrammerException {
        this();
        setLabel(label);
        update(code);
    }

    public void update(String code) throws UserException, ProgrammerException {
        NumberFormat profileClassCodeFormat = NumberFormat.getIntegerInstance(Locale.UK);
        profileClassCodeFormat.setMinimumIntegerDigits(2);
        super.update(profileClassCodeFormat.format(Integer.parseInt(code)));
    }
}
