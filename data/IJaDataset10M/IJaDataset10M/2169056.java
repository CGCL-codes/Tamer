package au.gov.qld.dnr.dss.control.command;

import au.gov.qld.dnr.dss.interfaces.command.IHelpDSSCommand;

/**
 * Help.DSS command.
 */
public class HelpDSSCommand extends Command {

    IHelpDSSCommand _handler = null;

    public HelpDSSCommand(IHelpDSSCommand handler) {
        _handler = handler;
    }

    /**
     * Invoke implementation
     */
    public void execute() {
        _handler.helpDSS();
    }
}
