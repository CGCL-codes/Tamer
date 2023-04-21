package jp.eflow.hisano.dalvikvm.jvmtests;

public class ParentClass {

    String parentField;

    String hiddenField;

    void parentMethod() {
        System.out.println("parent");
    }

    void hiddenMethod() {
        System.out.println("parent_hidden");
    }
}
