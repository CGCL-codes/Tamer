package update5.static_import;

public class MyOut {

    public static MyOut out = new MyOut();

    public void println(String s) {
        System.out.println("MyOut=" + s);
    }
}
