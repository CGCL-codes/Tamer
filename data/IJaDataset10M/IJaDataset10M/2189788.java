package net.da.tools;

public class FieldDescriptor {

    private TypeDescriptor type;

    private String accessQualifier;

    private String name;

    public String getAccessQualifier() {
        return accessQualifier;
    }

    public void setAccessQualifier(String accessQualifier) {
        this.accessQualifier = accessQualifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeDescriptor getType() {
        return type;
    }

    public void setType(TypeDescriptor type) {
        this.type = type;
    }
}
