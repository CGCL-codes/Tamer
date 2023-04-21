package at.urbiflock.ui;

import java.util.Calendar;
import edu.vub.at.objects.natives.grammar.AGSymbol;

/**
 * @author alombide
 *
 */
public interface AbstractFieldType {

    public boolean isString();

    public boolean isEnumeration();

    public boolean isInteger();

    public boolean isDate();

    public boolean isPossibleValue(Object aValue);

    public String name();

    public AGSymbol[] getPossibleValues();

    public Object defaultValue();

    public int getFieldSize();

    public AGSymbol[] comparators();
}
