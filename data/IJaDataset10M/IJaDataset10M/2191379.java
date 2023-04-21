package webserg.pazzlers.ch6;

/**
 * @author Sergiy Doroshenko
 *	solution for class MyThing
 *  making new local constructor 
 */
public class MyThingSolution extends Thing {

    private final int arg;

    /**
	 * 
	 */
    public MyThingSolution() {
        this(SomeOtherClass.func());
    }

    private MyThingSolution(int i) {
        super(i);
        this.arg = i;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println(new MyThingSolution().arg);
    }
}
