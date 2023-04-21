package tests.command;

import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.List;

public class StringCommand {

    private String command_;

    private List<String> params_ = null;

    /**
	 * 
	 */
    public void parseCommand(String cmd_line) throws IllegalArgumentException {
        StringTokenizer parser = new StringTokenizer(cmd_line);
        if (parser.countTokens() == 0) {
            throw new IllegalArgumentException();
        }
        command_ = parser.nextToken();
        if (!parser.hasMoreElements()) {
            return;
        }
        params_ = new LinkedList<String>();
        while (parser.hasMoreTokens()) {
            params_.add(parser.nextToken());
        }
    }

    /**
	 * 
	 */
    public String getCommand() {
        return command_;
    }

    /**
	 * 
	 */
    public List<String> getParams() {
        return params_;
    }
}
