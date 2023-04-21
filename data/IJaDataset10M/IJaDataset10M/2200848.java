package cen5501c.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Error from the server. 
 * 
 * Server can send an error message in response to client�s query.
 * The format is
 * 		<ERROR, explanation of error>
 * 
 * @author Jiangyan Xu
 *
 */
public class ErrorCmd extends CmdBase {

    public static final String OPERATION = "ERROR";

    private String message;

    /**
	 * @return the message
	 */
    public String getMessage() {
        return message;
    }

    /**
	 * @param message the message to set
	 */
    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorCmd(String message) {
        this.message = message;
    }

    /**
	 * @param args
	 * @throws Exception
	 */
    public ErrorCmd(List<String> args) throws Exception {
        super(args);
    }

    @Override
    protected void fillArgs(List<String> args) throws Exception {
        message = args.get(0);
    }

    @Override
    public List<String> getArgList() {
        List<String> ret = new ArrayList<String>();
        ret.add(message);
        return ret;
    }

    @Override
    public String getOperation() {
        return OPERATION;
    }
}
