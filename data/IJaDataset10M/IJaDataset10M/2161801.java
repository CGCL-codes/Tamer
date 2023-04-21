package se.sics.isl.transport;

public interface BinaryTransport {

    public static final byte START_NODE = 'n';

    public static final byte END_NODE = '\n';

    public static final byte NODE = 'N';

    public static final byte TABLE = 'T';

    public static final byte INT = 'i';

    public static final byte LONG = 'l';

    public static final byte FLOAT = 'f';

    public static final byte STRING = 's';

    public static final byte CONSTANT_STRING = 'S';

    public static final byte INT_ARR = 'I';

    public static final byte ALIAS = 'A';
}
