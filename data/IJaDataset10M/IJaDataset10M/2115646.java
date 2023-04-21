package net.simplemodel.core.ast;

import java.util.List;

public class SMParameterizedTypeReference extends SMTypeReference {

    private List<SMTypeReference> parameters;

    private final SMTypeReference mainType;

    public SMParameterizedTypeReference(int start, int end, SMTypeReference mainType, List<SMTypeReference> parameters) {
        super(start, end, mainType.getName());
        this.parameters = parameters;
        this.mainType = mainType;
    }

    public SMTypeReference getMainType() {
        return mainType;
    }

    public List<SMTypeReference> getParameters() {
        return parameters;
    }

    @Override
    public SMParameterizedTypeReference parameterize(SMTypeReference... parameters) {
        throw new UnsupportedOperationException();
    }

    public void setParameters(List<SMTypeReference> parameters) {
        this.parameters = parameters;
    }
}
