package part_3;

public class Cards {

    public static void main(String[] args) {
        System.out.println("The number on the card is :" + draw(false));
        System.out.println("And the suit is :" + draw(true));
    }

    public static int draw(boolean suit) {
        if (suit) {
            return ((int) Math.round(Math.random() * 3));
        } else {
            return (int) Math.round((Math.random() * 12) + 1);
        }
    }
}
