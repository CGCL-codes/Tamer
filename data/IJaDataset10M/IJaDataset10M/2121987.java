package org.zamia.verilog.pre;

/**
 * 
 * @author Guenter Bartsch
 *
 */
public class VPMacro {

    private final String fId, fBody;

    public VPMacro(String aId, String aBody) {
        fId = aId;
        fBody = aBody;
    }

    public String getId() {
        return fId;
    }

    public String getBody() {
        return fBody;
    }
}
