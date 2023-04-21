package cease.command.friendship;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;
import cease.butter.FriendshipExecutor;
import cease.command.HasOptionCommand;
import cease.urs.Session;

public class UnfollowCommand extends HasOptionCommand {

    @Option(name = "-u", usage = "user, email or userid", metaVar = "required", required = true)
    private String user;

    private void reset() {
        user = null;
    }

    public String getCmdDesc() {
        return "unfollow a user";
    }

    public void run(Session session, String[] args) throws Exception {
        try {
            parser.parseArgument(args);
            FriendshipExecutor.getInstance().delete(session, user);
            System.out.println("unfollow success");
        } catch (CmdLineException e) {
            parser.printUsage(System.out);
        } finally {
            reset();
        }
    }
}
