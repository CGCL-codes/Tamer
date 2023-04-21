package cease.command.timeline;

import java.util.Collection;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;
import cease.butter.TimelineExecutor;
import cease.command.HasOptionCommand;
import cease.console.PrintHelper;
import cease.urs.Session;
import cease.vo.Butter;

/**
 * @author dhf
 */
public class ShowHomeTLCommand extends HasOptionCommand {

    @Option(name = "-c", usage = "max result size", metaVar = "optional")
    private Integer size;

    @Option(name = "-o", usage = "butter cursor offset", metaVar = "optional")
    private String cursor;

    @Option(name = "-s", usage = "whether need sort result", metaVar = "optional")
    private String sort;

    private void reset() {
        size = null;
        cursor = null;
        sort = null;
    }

    public String getCmdDesc() {
        return "show home timeline";
    }

    public void run(Session session, String[] args) throws Exception {
        try {
            parser.parseArgument(args);
            Collection<Butter> butters = TimelineExecutor.getInstance().home(session, size, cursor, sort);
            PrintHelper.printButter(butters);
        } catch (CmdLineException e) {
            parser.printUsage(System.out);
        } finally {
            reset();
        }
    }
}
