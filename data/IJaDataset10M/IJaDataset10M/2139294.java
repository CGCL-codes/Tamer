package Inheritance2Delegation;

public class Client extends Parent2 {

    public Client() {
        init();
    }

    public void init() {
        Inheritance2Delegation.Parent2 test = new Inheritance2Delegation.Parent2();
        test.getValue().toString();
        setValue(new Integer(24));
        System.out.println("Value of i now = " + getValue().toString());
        System.out.println("(Add) Value of i now = " + add(getValue().intValue(), getValue().intValue()).toString());
    }

    public static void main(String argv[]) {
        System.out.println("Client startet");
        Client c = new Client();
    }
}
