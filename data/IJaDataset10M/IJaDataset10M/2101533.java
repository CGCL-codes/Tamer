package tests.generics;

public interface InterfaceWithFieldsInit {

    public enum Platform {

        JAVA, DOTNET
    }

    public interface InnerType {

        int VALUE1 = 1;
    }

    public Platform getPlat();
}
