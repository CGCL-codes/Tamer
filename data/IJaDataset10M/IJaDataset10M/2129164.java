package inda;

/**
 * Instansierar klassen HallKall
 * 
 * @author Frej Connolly and Elias Lousseief
 * @version 2008-04-24
 */
public class Main {

    public Main() {
    }

    public static void main(String[] args) {
        if (args.length != 0) System.exit(1); else {
            new Gui();
        }
    }
}
