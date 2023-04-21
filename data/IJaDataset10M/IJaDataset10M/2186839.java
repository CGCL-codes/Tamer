package pubcrawl;

import java.util.Random;

/**
 *
 * @author Rune
 */
public class Alcoholic extends Actor {

    private Bar bar;

    private Room room;

    private boolean isProtected;

    private boolean hasDancedWithLady;

    public Alcoholic(String n, int a) {
        super(n, a);
        isProtected = false;
        hasDancedWithLady = false;
    }

    public void enterBar(Bar bar) {
        this.bar = bar;
    }

    public void enterRoom(Room r1) {
        this.room = r1;
    }

    public boolean isIsProtected() {
        return isProtected;
    }

    public void setIsProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    public boolean hasDancedWithLady() {
        return hasDancedWithLady;
    }

    public void setHasDancedWithLady(boolean hasDancedWithLady) {
        this.hasDancedWithLady = hasDancedWithLady;
    }

    public void buyBeer() {
        bar.buyBeer();
    }

    public void pickUpCondom() {
        room.pickUpCondom();
    }

    public int beerGame() {
        Random r = new Random();
        return r.nextInt(9999999);
    }

    public int defenceInFight() {
        Random r = new Random();
        return r.nextInt(9999999);
    }

    public void askToDance(Lady l) {
        l.acceptDance();
    }

    public void seduceLady(Lady l) {
        if (hasDancedWithLady && isProtected == true) {
            System.out.println("Mission completed!\nThe lady finds you attractive " + "and she agrees to f*ck. Well done, you little pervert.");
        } else {
            System.out.println("Game over!\nYou have been showing too little patience, you " + "little pervert. Try again.");
        }
    }

    public Bar getBar() {
        return bar;
    }

    public Room getRoom() {
        return room;
    }
}
