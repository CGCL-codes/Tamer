package blasar;

import blasar.Services.Com.vms.PluginInterface;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 *
 * @author Jesus Navalon i Pastor <jnavalon at redhermes dot net>
 */
public final class Config {

    public final class CMD {

        public static final short NULL = 0;

        public static final short ANSWER = 1;

        public static final short QUESTION = 2;

        public static final short INFO = 3;

        public final class CHARS {

            public static final char ANSWER = '<';

            public static final char QUESTION = '>';

            public static final char INFO = '#';
        }
    }

    public static final class BLASAR {

        public static final String ConfigFile = "conf.cfg";

        public static final String passSUFile = "supasswd";

        public static final String passFile = "passwd";

        public static final String DNIeChar = "*";

        public static final String VERSION = "0.01d";

        public static final String PublicKeyDIR = "pks/";

        public static final String PublicKeyEXT = ".pk";

        public static final String PluginsDIR = "plugins/";

        public static final String PluginEXT = ".jet";

        public static String startDir;

        public static int port = 46600;

        public static int max_users = 50;

        public static int max_wait = 10000;

        public static InetAddress bind = getBind();

        public static boolean verbose = false;

        public static boolean interactive = false;

        public static boolean log = false;

        public static String logFile = null;

        public static int online = 0;

        public static HashMap<String, PluginInterface> plugins = new HashMap<String, PluginInterface>();
    }

    private static InetAddress getBind() {
        try {
            return InetAddress.getByAddress(new byte[] { 0, 0, 0, 0 });
        } catch (UnknownHostException ex) {
            return null;
        }
    }
}
