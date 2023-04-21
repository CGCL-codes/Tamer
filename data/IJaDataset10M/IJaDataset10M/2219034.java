package net.infordata.ifw2.util;

import java.io.Serializable;
import java.text.ParseException;

public class DummyFormatter implements IFormatter<String>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final DummyFormatter cvInstance = new DummyFormatter();

    public static DummyFormatter getInstance() {
        return cvInstance;
    }

    protected DummyFormatter() {
    }

    @Override
    public String format(String value) {
        return value;
    }

    @Override
    public String parse(String text) throws ParseException {
        return text;
    }

    @Override
    public Class<? extends String> getValueType() {
        return String.class;
    }

    @Override
    public String cast(String value) {
        return value;
    }
}
