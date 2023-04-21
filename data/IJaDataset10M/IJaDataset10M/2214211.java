package net.sourceforge.freejava.type.parser;

import net.sourceforge.freejava.type.traits.AbstractParser;
import net.sourceforge.freejava.util.exception.ParseException;

public class FloatParser extends AbstractParser<Float> {

    @Override
    public Float parse(String text) throws ParseException {
        try {
            return Float.valueOf(text);
        } catch (NumberFormatException e) {
            throw new ParseException(e.getMessage(), e);
        }
    }
}
