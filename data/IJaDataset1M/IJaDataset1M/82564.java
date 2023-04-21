package example;

public class SuperClass {

    static boolean constructorWasCalled = false;

    SuperClass() {
        constructorWasCalled = true;
    }

    SuperClass(String param) {
        System.out.println("����Ŭ����(SuperClass ����)\n" + param);
        constructorWasCalled = true;
    }
}
