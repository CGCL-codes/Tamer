package org.warko.fom.protocol.fomdebug.command;

import org.warko.sts.ProtocolHandler;
import org.warko.sts.protocol.command.CsvTextProtocolCommand;

/** * The <code>BregisterCommand</code> class ...<p> * * @author  Olivier Hoareau * @version 0.1, Feb 17, 2005 *  * @since   JDK1.4 */
public class Cmd_bregister extends CsvTextProtocolCommand {

    /**	 * TODO	 * 	 * @param ph	 */
    public Cmd_bregister(ProtocolHandler ph) {
        super(ph, "BREGISTER");
    }
}
