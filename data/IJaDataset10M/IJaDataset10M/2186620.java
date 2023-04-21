package org.makumba;

/** Error thrown if a data definition cannot be found */
public class DataDefinitionNotFoundError extends DataDefinitionParseError {

    private static final long serialVersionUID = 1L;

    public DataDefinitionNotFoundError(String typeName, java.io.IOException e) {
        super(typeName, e);
    }

    public DataDefinitionNotFoundError(String expl) {
        super(expl);
    }

    public DataDefinitionNotFoundError(String typeName, String name) {
        super(typeName, "could not find " + name);
    }
}
