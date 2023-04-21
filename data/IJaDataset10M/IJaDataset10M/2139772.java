package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import org.eclipse.dltk.dbgp.DbgpRequest;

/**
 * DBGp XML Stdout Packet. Example :
 * 
 * <pre>
 * <response command="stdout"
 *           success="0|1"
 *           transaction_id="transaction_id"/>
 * </pre>
 * 
 * @see specification at
 *      http://xdebug.org/docs-dbgp.php#stdout-stderr
 */
public class StdoutPacket extends DbgpXmlResponsePacket {

    public StdoutPacket(DbgpRequest command) {
        super(command);
        addAttribute(SUCCESS_ATTR, ONE);
    }
}
