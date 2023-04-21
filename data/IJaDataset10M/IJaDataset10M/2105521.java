package example.datamappers2.test;

import net.nothinginteresting.datamappers2.*;
import net.nothinginteresting.datamappers2.models.*;
import net.nothinginteresting.datamappers2.DomainObject;

public class Department extends DomainObject {

    public static final Field<Department, Integer> id = new Field<Department, Integer>("TEST", "Department", "id");

    public static final Field<Department, String> name = new Field<Department, String>("TEST", "Department", "name");

    public static final Field<Department, Integer> parent = new Field<Department, Integer>("TEST", "Department", "parent");

    public Integer getId() {
        return getFieldValue(Department.id);
    }

    public void setId(Integer id) {
        setFieldValue(Department.id, id);
    }

    public String getName() {
        return getFieldValue(Department.name);
    }

    public void setName(String name) {
        setFieldValue(Department.name, name);
    }

    public Integer getParent() {
        return getFieldValue(Department.parent);
    }

    public void setParent(Integer parent) {
        setFieldValue(Department.parent, parent);
    }

    public static class Inner<T extends DomainObject> extends AbstractInner<T> {

        public final Field<T, Integer> id = new Field<T, Integer>("TEST", "Department", "id", this);

        public final Field<T, String> name = new Field<T, String>("TEST", "Department", "name", this);

        public final Field<T, Integer> parent = new Field<T, Integer>("TEST", "Department", "parent", this);

        public Inner(ClassReference classReference, AbstractInner<T> prevInner) {
            super(classReference, prevInner);
        }

        public Inner(ClassReference classReference) {
            super(classReference);
        }
    }

    @Override
    public String toString() {
        return "Department[" + "id=" + getId() + ", " + "name=" + getName() + ", " + "parent=" + getParent() + "]";
    }
}
