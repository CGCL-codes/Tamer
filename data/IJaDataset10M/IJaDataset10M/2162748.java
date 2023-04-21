package org.warko.fom.protocol.fomdebug.command;

import org.warko.sts.ProtocolHandler;
import org.warko.sts.protocol.command.SimpleTextProtocolCommand;

/** * The <code>Cmd__hell__v1_0</code> class ...<p> * * @author  Olivier Hoareau * @version 0.1, Feb 17, 2005 *  * @since   JDK1.4 */
public class Cmd_hell extends SimpleTextProtocolCommand {

    /**	 * TODO	 * 	 * @param ph	 */
    public Cmd_hell(ProtocolHandler ph) {
        super(ph, "HELL");
    }
}
