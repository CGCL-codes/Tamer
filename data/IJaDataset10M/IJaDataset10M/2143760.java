package lang;

import org.nutz.lang.Dumps;
import org.nutz.lang.Lang;

public class MyPojo {

    int i = 11;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public PojoAttr getAttr() {
        return attr;
    }

    public void setAttr(PojoAttr attr) {
        this.attr = attr;
    }

    double d = 123.344;

    String string = "pojo hair";

    PojoAttr attr = new PojoAttr();

    public static String staticString = "Static String";

    protected String instanceString = "instance String";

    public MyPojo() {
        attr.put("integer", 123);
        attr.put("string", "13's string");
        attr.put("MyPojo", this);
    }

    public static void main(String[] args) {
        MyPojo pojo = new MyPojo();
        System.out.println(Dumps.obj(pojo));
    }
}
