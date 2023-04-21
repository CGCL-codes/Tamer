package pieces;

/**
 * @author �����
 *
 */
public class Pawn {

    private String color;

    public Pawn(String color) {
        this.color = color;
    }

    public String color() {
        return this.color;
    }

    public Pawn() {
        this("white");
    }

    public class Pawn2 {

        String color;

        static final String White = "white";

        static final String Black = "black";

        public Pawn2() {
            this("white");
        }

        public Pawn2(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }
}
