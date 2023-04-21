package org.nakedobjects.application.value;

public interface SimpleBusinessValue extends BusinessValue {

    Object getValue();

    void parseUserEntry(String text);

    void restoreFromEncodedString(String data);

    String asEncodedString();
}
