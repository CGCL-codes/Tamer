package org.aacc.utils.comm;

/**
 *
 * @author Fernando
 */
public class TransferMessage extends DialMessage implements AACCMessageInterface {

    public TransferMessage() {
        command = CMD_TRANSFER;
    }
}
