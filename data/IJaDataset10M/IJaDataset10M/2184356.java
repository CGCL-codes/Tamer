package uebung03.as.aufgabe04;

public class TowerOfHanoi {

    public void solveHanoi(int n, String to, String from, String u) {
    }

    public void moveIt(int nr, String from, String to) {
        System.out.println("Move disc number " + nr + " from " + from + " to " + to);
    }

    public static void main(String args[]) {
        TowerOfHanoi hanoi = new TowerOfHanoi();
        if (args.length != 1) {
            System.err.println("Error: a single integer argument needed");
            System.exit(1);
        }
        Integer N = new Integer(args[0]);
        hanoi.solveHanoi(N.intValue(), "Sink", "Source", "Workplace");
    }
}
