package oop.coin;

public class Coin1 implements Coin, Cow {

    private boolean headsUp;

    public Coin1() {
        headsUp = true;
    }

    public void flip() {
        if (Math.random() <= 0.5) {
            headsUp = true;
        } else {
            headsUp = false;
        }
    }

    public boolean isHeads() {
        return headsUp;
    }

    public boolean isTails() {
        return !isHeads();
    }

    public void moo() {
        System.out.println("moo");
    }
}
