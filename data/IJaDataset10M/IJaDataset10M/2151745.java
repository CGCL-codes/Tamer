package abc.om.ast;

public class OpenClassFlags {

    boolean isField;

    boolean isParent;

    boolean isMethod;

    public OpenClassFlags() {
        this.isField = false;
        this.isParent = false;
        this.isMethod = false;
    }

    public OpenClassFlags(boolean isField, boolean isParent, boolean isMethod) {
        this.isField = isField;
        this.isParent = isParent;
        this.isMethod = isMethod;
    }

    public boolean isField() {
        return isField;
    }

    public void setField(boolean isField) {
        this.isField = isField;
    }

    public boolean isMethod() {
        return isMethod;
    }

    public void setMethod(boolean isMethod) {
        this.isMethod = isMethod;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean isParent) {
        this.isParent = isParent;
    }

    public OpenClassFlags disjoin(OpenClassFlags flags) {
        isField = isField || flags.isField();
        isParent = isParent || flags.isParent();
        isMethod = isMethod || flags.isMethod();
        return this;
    }
}
