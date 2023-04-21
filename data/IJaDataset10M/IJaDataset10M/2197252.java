package com.hifiremote.jp1;

/**
 * The Class UDSMSpecialProtocol.
 */
public class UDSMSpecialProtocol extends SpecialProtocol {

    /**
   * Instantiates a new uDSM special protocol.
   * 
   * @param name the name
   * @param pid the pid
   */
    public UDSMSpecialProtocol(String name, Hex pid) {
        super(name, pid);
    }

    public SpecialProtocolFunction createFunction(KeyMove keyMove) {
        return new UDSMFunction(keyMove);
    }

    public SpecialProtocolFunction createFunction(Macro macro) {
        return new UDSMFunction(macro);
    }

    public Hex createHex(SpecialFunctionDialog dlg) {
        return UDSMFunction.createHex(dlg);
    }

    public String[] getFunctions() {
        return functions;
    }

    /** The Constant functions. */
    private static final String[] functions = { "UDSM" };
}
