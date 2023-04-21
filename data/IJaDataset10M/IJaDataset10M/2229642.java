package jdk1_5;

public class GenericDemo {

    public static void main(String[] args) {
        A a = new A();
        a.hello("asdf");
    }
}

class A<T> {

    <E> void hello(T name) {
        System.out.println(name.getClass());
    }

    /**
	 * 
	 * @param s
	 */
    void hello(String s) {
    }

    {
    }
}
