package pl.edu.agh.io.sylvia.logic;

public class BusinessLogic {

    public static void main(String[] args) {
        NewsDaemon daemon = new NewsDaemon();
        Thread t = new Thread(daemon);
        t.start();
    }
}
