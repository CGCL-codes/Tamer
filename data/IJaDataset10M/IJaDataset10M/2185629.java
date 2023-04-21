package BLK.io.Console;

/**
 *
 * @author andresrg
 */
public class Console {

    private String prompt = ">";

    public static Console getConsole() {
        return new Console();
    }

    public void write(String text) {
        System.out.println(this.prompt + text);
    }

    public void error(String text) {
        System.err.println(this.prompt + text);
    }
}
