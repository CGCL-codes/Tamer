package org.aacc.utils.comm;

/**
 *
 * @author Fernando
 */
public class UnbreakMessage extends AACCMessage implements AACCMessageInterface {

    public UnbreakMessage() {
        command = CMD_UNBREAK;
    }

    public void args2Properties() {
    }

    public void properties2Args() {
    }
}
