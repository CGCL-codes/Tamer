package org.deft.repository.artifacttype.ecore;

import org.deft.repository.artifacttype.ArtifactType;
import org.deft.repository.artifacttype.Validator;

public class EcoreArtifactType implements ArtifactType {

    public static final String ID = "org.deft.repository.artifacttype.ecore";

    private static Validator validator = new EcoreValidator();

    @Override
    public String getTypeId() {
        return ID;
    }

    @Override
    public Validator getValidator() {
        return validator;
    }

    @Override
    public String getArtifactTypeName() {
        return "Generic Ecore";
    }
}
