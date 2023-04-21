package net.sf.jerkbot.plugins.funny;

import net.sf.jerkbot.commands.AbstractCommand;
import net.sf.jerkbot.commands.Command;
import net.sf.jerkbot.commands.MessageContext;
import net.sf.jerkbot.exceptions.CommandSyntaxException;
import net.sf.jerkbot.util.IOUtil;
import net.sf.jerkbot.util.MessageUtil;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 *         [INSERT DESCRIPTION HERE]
 * @version 0.0.1
 */
@Component(immediate = true)
@Service(value = Command.class)
public class HomerCommandImpl extends AbstractCommand {

    private static final String COMMAND_HELP = "~homer";

    private static final String COMMAND_NAME = "HOMER";

    private static final String COMMAND_DESCRIPTION = "Random quote from Homer Simpson";

    private static final List<String> homerQuotes = new ArrayList<String>();

    public HomerCommandImpl() {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, COMMAND_HELP);
    }

    @Activate
    protected void activate(ComponentContext componentContext) {
        homerQuotes.addAll(IOUtil.addLinesToList(getClass().getClassLoader().getResourceAsStream("homer.txt")));
    }

    public void execute(MessageContext context) throws CommandSyntaxException {
        int quotesLen = homerQuotes.size();
        Random rnd = new Random(System.currentTimeMillis());
        int i = rnd.nextInt(quotesLen);
        MessageUtil.talk(context, homerQuotes.get(i));
    }
}
