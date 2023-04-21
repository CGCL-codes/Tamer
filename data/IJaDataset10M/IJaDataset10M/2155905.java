package grace.log.test;

import grace.log.PrintCatcher;

public class PrintCatcherTest extends grace.test.Base {

    public static void main(String[] args) {
        new PrintCatcherTest().run();
    }

    public void run() {
        System.out.println("message without JavaLog");
        PrintCatcher.install();
        System.out.print("message to out");
        System.out.print("; a continued message to out");
        System.out.println("; end of message to out.");
        System.out.println("message to err.");
        PrintCatcher.uninstall();
        System.out.println("message again without JavaLog");
    }
}
