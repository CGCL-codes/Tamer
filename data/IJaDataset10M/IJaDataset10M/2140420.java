package moveOverridesAbstract;

class C {
}

interface Test {

    boolean isTrue(Object obj);
}

public class A extends C implements Test {

    public boolean isTrue(Object obj) {
        return true;
    }
}

class D {
}

class E extends C {
}
