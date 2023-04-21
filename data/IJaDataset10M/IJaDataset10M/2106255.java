package be.lassi.lanbox.udp;

/**
 * A message within the Lanbox UDP packet structure.
 */
public interface Message {

    /**
     * Appends a textual representation of this message to given
     * string builder.
     * 
     * @param b the string builder to append to
     */
    void append(StringBuilder b);
}
