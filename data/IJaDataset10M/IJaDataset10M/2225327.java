package claseGenericePtTest;

public class Test {

    public void rulare() {
        for (int i = 0; i < 100; i++) {
            System.out.println("rulare1");
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void rularea() {
        for (int i = 0; i < 50; i++) {
            System.out.println("rulare2");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
