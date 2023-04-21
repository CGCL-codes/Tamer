package chrriis.udoc.ui.widgets;

import chrriis.udoc.model.ClassInfo;
import chrriis.udoc.model.FieldInfo;
import chrriis.udoc.ui.ClassComponent;

public class FieldLink extends Link {

    protected ClassComponent classComponent;

    protected ClassInfo classInfo;

    protected FieldInfo fieldInfo;

    public FieldLink(ClassComponent classComponent, String text, ClassInfo classInfo, FieldInfo fieldInfo) {
        super(classComponent, text);
        this.classComponent = classComponent;
        this.classInfo = classInfo;
        this.fieldInfo = fieldInfo;
    }

    protected void processLink() {
        classComponent.openField(fieldInfo);
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }
}
