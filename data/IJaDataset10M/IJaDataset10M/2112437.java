package org.aacc.utils.comm;

/**
 * Instant messaging
 * @author Fernando
 */
public class IMMessage extends AACCMessage implements AACCMessageInterface {

    private String receptor = "";

    public IMMessage() {
        command = CMD_IM;
    }

    public IMMessage(String receptor) {
        command = CMD_IM;
        this.receptor = receptor;
        properties2Args();
    }

    public void args2Properties() {
        if (arguments.size() >= 1) {
            receptor = arguments.get(0);
        }
    }

    public void properties2Args() {
        arguments.clear();
        arguments.add(receptor);
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }
}
