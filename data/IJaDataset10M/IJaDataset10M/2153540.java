package edu.java.texbooks.test.chapter20;

public class Lion extends Felidae implements Walk, Move {

    private int weight = 0;

    public Lion() {
        this(true, 0);
    }

    public Lion(boolean male, int weight) {
        super(male);
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void walk() {
        System.out.println(moveLap("front left Lap"));
        System.out.println(moveLap("back right Lap"));
        System.out.println(moveLap("front right Lap"));
        System.out.println(moveLap("back left Lap"));
    }

    public String moveLap(String lapInfo) {
        return "Move Lap: " + lapInfo;
    }

    public void move() {
        System.out.println("Stay; Walk; Run;");
    }

    public void tasteMe() {
        System.out.println("You're so sweet, Lemon");
    }

    public static void main(String... strings) {
        Lion leo = new Lion(true, 1);
        System.out.println(leo);
        leo.walk();
        System.out.println(leo.min_step_lenght);
        System.out.println(Walk.min_step_lenght);
        leo.move();
    }
}
