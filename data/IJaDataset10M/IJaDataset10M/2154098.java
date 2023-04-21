package world.test;

import image.*;
import world.World;

/** World OnKey Test */
public class KeyMoveWorld extends World {

    public static void main(String[] s) {
        new KeyMoveWorld(new Posn(200, 200)).bigBang();
    }

    /** The position of the dot */
    Posn p;

    /** Construct a new World with the given Dot */
    KeyMoveWorld(Posn p) {
        this.p = p;
    }

    /** Move the World by DX/DY */
    KeyMoveWorld move(int dx, int dy) {
        return new KeyMoveWorld(this.p.move(dx, dy));
    }

    /** On an arrow key move the dot in the right direction */
    public KeyMoveWorld onKey(String ke) {
        if (ke.equals("up")) return this.move(0, -10); else if (ke.equals("down")) return this.move(0, 10); else if (ke.equals("left")) return this.move(-10, 0); else if (ke.equals("right")) return this.move(10, 0); else if (ke.equals(" ")) {
            System.out.println("SPACES");
        }
        return this;
    }

    /** Draw the dot into an EmptyScene */
    public Scene onDraw() {
        return this.p.draw(new EmptyScene(400, 400));
    }
}

/** Represents a 2D Position */
class Posn {

    /** The Coordinates of this Posn */
    int x, y;

    /** Construct a Posn from X/Y */
    Posn(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Move this Posn */
    Posn move(int dx, int dy) {
        return new Posn(this.x + dx, this.y + dy);
    }

    /** Draw a Dot at X/Y */
    Scene draw(Scene scn) {
        return scn.placeImage(new Circle(10, "solid", "red"), this.x, this.y);
    }
}
