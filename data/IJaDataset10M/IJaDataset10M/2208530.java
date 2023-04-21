package icebird.compiler.ncomp.vs;

import icebird.compiler.ncomp.X86Register.XMM;

/**
 * Item with this location must be in SSE(XMM)
 * register.
 * @author Sergey Shulepoff[Knott]
 */
public class SSELocation extends Location {

    private XMM reg;

    public SSELocation(XMM reg) {
        super("SSE: " + reg);
    }

    /**
	 * Returns SSE register.
	 * @return XMM
	 */
    public XMM getXMM() {
        return reg;
    }

    public int getWeight() {
        return 3;
    }
}
