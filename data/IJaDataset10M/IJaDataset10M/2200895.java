package datastructures;

public class Node {

    private HangmanRecord rec;

    public Node next;

    public Node(HangmanRecord rec, Node next) {
        this.next = next;
        this.rec = rec;
    }

    public int getDifficulty() {
        return this.rec.getDifficulty();
    }

    public HangmanRecord getRecord() {
        return this.rec;
    }

    public String getCategory() {
        return this.rec.getCategory();
    }
}
