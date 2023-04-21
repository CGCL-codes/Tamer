package samples.suppressconstructor;

public class SuppressConstructorHierarchyParent extends SuppressConstructorHeirarchyEvilGrandParent {

    private String message;

    SuppressConstructorHierarchyParent() {
        System.out.println("Parent constructor");
        this.message = "Default message";
    }

    SuppressConstructorHierarchyParent(String message) {
        System.out.println("Parent constructor with message");
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
