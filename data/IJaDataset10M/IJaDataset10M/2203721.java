package gawky.incubator.referencer;

public class Test {

    public static void main(String[] args) {
        Test tt = new Test();
        Obj t = tt.new Obj();
        test(t.intern);
        System.out.println("[" + t.intern.vals + "]");
    }

    public static void test(Helper val) {
        val.vals = "hello";
    }

    class Obj {

        public String val;

        public Helper intern = new Helper();
    }
}
