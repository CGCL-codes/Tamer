package foo;

import java.io.IOException;

public class GCBox {

    public static void main(final String[] args) throws InterruptedException, IOException {
        System.out.println("Press any key to continue...");
        System.in.read();
        new GCBox().run();
        Thread.sleep(10 * 1000);
        System.out.println("Press any key to continue...");
        System.in.read();
    }

    private void run() throws InterruptedException {
        byte[] n;
        for (int i = 0; i < 1000000; i++) {
            n = new byte[1000000];
            final String n2 = "Hello:" + i;
            System.out.println("Hello" + n2 + ", n=" + n);
        }
    }
}
