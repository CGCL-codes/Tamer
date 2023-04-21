package net.sf.xdc;

import java.util.List;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import net.sf.xdc.util.FileTokenizer;
import net.sf.xdc.util.Logging;

/**
 * This class provides a command line parser which mainly differs from its
 * parent, {@link BasicParser}, in the fact that it resolves paths to files
 * containing additional command line options ('@' notation).
 *
 * @author Jens Vo�
 * @since 0.5
 * @version 0.5
 */
public class XdcParser extends BasicParser {

    private static final Logger LOG = Logging.getLogger();

    protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption) {
        List retVal = new Vector();
        for (int i = 0; i < arguments.length; i++) {
            String arg = arguments[i];
            if (arg.length() == 0 || arg.charAt(0) != '@') {
                retVal.add(arg);
            } else {
                try {
                    FileTokenizer tok = new FileTokenizer(new File(arg.substring(1)));
                    retVal.addAll(Arrays.asList(tok.getContents()));
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return (String[]) retVal.toArray(new String[retVal.size()]);
    }
}
