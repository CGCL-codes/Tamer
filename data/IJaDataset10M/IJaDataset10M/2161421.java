package server.options;

import objects.GalaxyException;
import objects.IRaceOption;
import objects.Race;
import java.io.PrintWriter;
import java.util.ListIterator;

/**
 * Change list, used for report generating (it's parameters).
 * <dl>
 * <dt>COMMAND
 * <dd>Show current value.
 * <dt>COMMAND parameters
 * <dd>Set default sequence of parameters for report generating.
 * </dl>
 */
public class ReportParameters implements IRaceOption {

    @Override
    public void exec(Race race, ListIterator<String> cmd, PrintWriter out) throws GalaxyException {
        if (!cmd.hasNext()) {
            out.println("Current report parameters: " + race.getRepParams());
            return;
        }
        String flags = cmd.next();
        for (int i = 0; i < flags.length(); ++i) {
            String s = flags.substring(i, i + 1);
            if (race.getGalaxy().reportParameters.get(s) == null) throw new GalaxyException("Unknown report parameter - {0}", s);
        }
        race.setRepParams(flags);
    }
}
