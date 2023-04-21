package jolie.lang;

import java.nio.charset.Charset;
import jolie.lang.parse.Scanner;
import jolie.util.Range;

public final class Constants {

    private Constants() {
    }

    public interface Manifest {

        public static final String ChannelExtension = "X-JOLIE-ChannelExtension";

        public static final String ListenerExtension = "X-JOLIE-ListenerExtension";

        public static final String ProtocolExtension = "X-JOLIE-ProtocolExtension";

        public static final String MainProgram = "X-JOLIE-Main-Program";

        public static final String Options = "X-JOLIE-Options";
    }

    public static enum Predefined {

        ATTRIBUTES("@Attributes", "@Attributes"), HTTP_BASIC_AUTHENTICATION("@HttpBasicAuthentication", "@HttpBasicAuthentication"), PI("PI", java.lang.Math.PI);

        private final String id;

        private final Scanner.Token token;

        public static Predefined get(String id) {
            for (Predefined p : Predefined.values()) {
                if (p.id.equals(id)) {
                    return p;
                }
            }
            return null;
        }

        Predefined(String id, String content) {
            this.id = id;
            this.token = new Scanner.Token(Scanner.TokenType.STRING, content);
        }

        Predefined(String id, Integer content) {
            this.id = id;
            this.token = new Scanner.Token(Scanner.TokenType.INT, content.toString());
        }

        Predefined(String id, Double content) {
            this.id = id;
            this.token = new Scanner.Token(Scanner.TokenType.DOUBLE, content.toString());
        }

        public final String id() {
            return id;
        }

        public final Scanner.Token token() {
            return token;
        }
    }

    public static final Range RANGE_ONE_TO_ONE = new Range(1, 1);

    public static interface Keywords {

        public static final String DEFAULT_HANDLER_NAME = "default";
    }

    public static final String TYPE_MISMATCH_FAULT_NAME = "TypeMismatch";

    public static final String IO_EXCEPTION_FAULT_NAME = "IOException";

    public static final String MONITOR_OUTPUTPORT_NAME = "#Monitor";

    public static final String INPUT_PORTS_NODE_NAME = "inputPorts";

    public static final String PROTOCOL_NODE_NAME = "protocol";

    public static final String LOCATION_NODE_NAME = "location";

    public static final String LOCAL_LOCATION_KEYWORD = "local";

    public static final String VERSION = "JOLIE 1.0_beta4 (0.9.98)";

    public static final String COPYRIGHT = "(C) 2006-2012 the JOLIE team";

    public static final String fileSeparator = System.getProperty("file.separator");

    public static final String pathSeparator = System.getProperty("path.separator");

    public static final String GLOBAL = "global";

    public static final String CSETS = "csets";

    public static final Charset defaultCharset;

    static {
        defaultCharset = Charset.forName("UTF-8");
    }

    public enum EmbeddedServiceType {

        JOLIE, JAVA, JAVASCRIPT, UNSUPPORTED
    }

    public static EmbeddedServiceType stringToEmbeddedServiceType(String str) {
        if ("jolie".equalsIgnoreCase(str)) {
            return EmbeddedServiceType.JOLIE;
        } else if ("java".equalsIgnoreCase(str)) {
            return EmbeddedServiceType.JAVA;
        } else if ("javascript".equalsIgnoreCase(str)) {
            return EmbeddedServiceType.JAVASCRIPT;
        }
        return EmbeddedServiceType.UNSUPPORTED;
    }

    public enum ExecutionMode {

        SINGLE, SEQUENTIAL, CONCURRENT
    }

    public enum OperationType {

        ONE_WAY, REQUEST_RESPONSE
    }

    public enum OperandType {

        ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULUS
    }

    public static long serialVersionUID() {
        return 1L;
    }
}
