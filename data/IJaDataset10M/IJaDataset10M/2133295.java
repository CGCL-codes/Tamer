package codebush.test.learn;

/**
 *
 *2008-5-19 @author Fution Bai Copyright 2007 
 */
class AA {

    protected int i;

    public AA(int i) {
        this.i = i;
    }
}

public class BB extends AA {

    private int j;

    public BB(int i, int j) {
        super(i);
        this.j = j;
    }

    public double getDouble() {
        return i + 2.0;
    }

    public static void main(String[] args) {
        AA a = new AA(3);
        BB bb = new BB(5, 6);
        System.out.println(bb.getDouble());
        String str = "This is a old String";
        bb.changeString(str);
        System.out.println(str);
    }

    public void changeString(String s) {
        s = "This is a new string";
    }
}
