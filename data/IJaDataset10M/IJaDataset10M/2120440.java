package mystuff;

public class Implementor implements Implementee {

    int z;

    public Implementor(int z) {
        this.z = z;
    }

    public int howMuch() {
        return z;
    }

    public void foo() {
        z = 3;
    }

    public void bletch() {
        a = 4;
    }
}
