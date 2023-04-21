package se.marianna.simpleDB;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 
 *
 * @author darwin
 */
public interface SimpleDBValue extends SimpleDBComparator {

    public static class ValueParsingError extends SimpleDBException {

        public ValueParsingError() {
        }

        public ValueParsingError(String message) {
            super(message);
        }
    }

    public SimpleDBValue fromDataInputStream(DataInputStream datainput) throws IOException;

    public void toDataOutputStream(DataOutputStream dataOut) throws IOException;

    public boolean equals(Object arg0);

    public int hashCode();

    /**
     * should always be true 
     * 
     * XValue.fromString(xValueInstance.toString()).equals(xValueInstance)
     * 
     * @param valueAsString
     * @return
     */
    public SimpleDBValue fromString(String valueAsString) throws ValueParsingError;
}
