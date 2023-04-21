package biz.xl.thinkinjava.ch2;

public class TestClass {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        BaseClass1 bc = new DerivedClass1();
        bc.testFriendly();
        DerivedClass1 dc = new DerivedClass1();
    }
}
