package org.openscience.jmol;

public class RasMolScriptException extends JmolException {

    public RasMolScriptException(String message) {
        super("RasMolScriptHandler.handle()", message);
    }
}
