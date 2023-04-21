package annone.engine.defs;

public class TypeDef extends Def {

    private ComponentDef targetDef;

    public TypeDef() {
    }

    public ComponentDef getTargetDef() {
        return targetDef;
    }

    public void setTargetDef(ComponentDef targetDef) {
        this.targetDef = targetDef;
    }
}
