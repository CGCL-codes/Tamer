package pr855;

public class BothMain2 {

    public BothMain2() {
        super();
    }

    public static void main(String[] args) {
        BothMain2 main1 = new BothMain2();
        Compiler.disable();
        new Compiler().test();
    }
}
