package praktikumid.k09.p02.yahtzee;

/**
 * @author Ivor
 *
 */
public class Yahtzee {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Player player1;
        Player player2;
        player1 = new Player();
        player1.setName("Aadu");
        player2 = new Player();
        player2.setName("Beedu");
        System.out.println("Player 1: " + player1.getName());
        System.out.println("Player 2: " + player2.getName());
        player2.setName("Ceedu");
        System.out.println("Player 1: " + player1.getName());
        System.out.println("Player 2: " + player2.getName());
    }
}
