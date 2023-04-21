package org.deft.representation.identifiers.ecore;

public class EcoreParameterIdentifier extends AbstractEcoreIdentifier {

    private String type;

    public EcoreParameterIdentifier(String name, String id, String type) {
        super(name, id);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getIconPath() {
        return "icons/eparameter.gif";
    }
}
