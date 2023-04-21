package asg.cliche;

import java.util.List;

/**
 * Input provider for Shell.
 * Shell asks Input, "What does the user want to execute?", and Input reads and returns line from the user.
 * @author ASG
 */
public interface Input {

    String readCommand(List<String> path);
}
