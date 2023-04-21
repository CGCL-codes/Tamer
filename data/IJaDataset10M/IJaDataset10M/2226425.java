package foundation;

import java.io.Serializable;

public class SerializationTestObject implements Serializable {

    public boolean booleanField;

    public byte byteField;

    public char charField;

    public short shortField;

    public int intField;

    public float floatField;

    public long longField;

    public double doubleField;

    public String toString() {
        return "booleanField: " + booleanField + "\n" + "byteField: " + byteField + "\n" + "charField: " + charField + "\n" + "shortField: " + shortField + "\n" + "intField: " + intField + "\n" + "floatField: " + floatField + "\n" + "longField: " + longField + "\n" + "doubleField: " + doubleField + "\n";
    }
}
