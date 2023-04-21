package samples.singleton;

public class StaticWithPrivateCtor {

    private StaticWithPrivateCtor() {
    }

    public static String staticMethod() {
        return "something";
    }
}
