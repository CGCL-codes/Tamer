package common;

public interface Initializer {

    public enum ObjectType {

        Application, Template, Localization
    }

    ;

    public Object init(Object obj);

    public void setManagedObject(ObjectType obj);
}
